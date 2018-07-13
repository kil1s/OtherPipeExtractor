package org.schabi.newpipe.extractor.services.dtube;

import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import com.github.FlorianSteenbuck.other.http.HttpDownloader;
import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.constants.Encodings;
import org.schabi.newpipe.extractor.constants.Keys;
import org.schabi.newpipe.extractor.constants.Words;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.extractor.stream.StreamInfoItemsCollector;
import com.github.FlorianSteenbuck.other.url.helper.UrlParsingHelper;
import com.github.FlorianSteenbuck.other.url.model.UrlParsingFeature;
import com.github.FlorianSteenbuck.other.url.model.UrlQuery;
import com.github.FlorianSteenbuck.other.url.navigator.UrlNavigator;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO create interface and abstracts
public class DTubeStreamInfoItemNavigator {
    protected StreamingService service;
    protected String url;
    protected int limit;
    protected String tag;
    protected Object[] params;
    protected DTubeUrlIdHandler urlIdHandler;
    protected StreamInfoItemsCollector currentCollector;

    public DTubeStreamInfoItemNavigator(int limit, String tag, StreamingService service, Object[] params, DTubeUrlIdHandler urlIdHandler) {
        this.limit = limit;
        this.tag = tag;
        this.service = service;
        this.params = params;
        this.urlIdHandler = urlIdHandler;
    }

    protected StreamInfoItemsCollector getCollectorWithParams(Map<String, Object> addOptions) throws IOException, ExtractionException {
        StreamInfoItemsCollector collector = new StreamInfoItemsCollector(service.getServiceId());
        HttpDownloader downloader = NewPipe.getDownloader();

        JsonObject options = new JsonObject();
        options.put("tag", tag);
        // without no output
        options.put("limit", limit);
        if (addOptions != null) {
            for (Map.Entry<String, Object> entry:addOptions.entrySet()) {
                options.put(entry.getKey(), entry.getValue());
            }
        }

        int len = params.length;
        Object[] actualParams = new Object[len+1];
        for (int i = 0; i < len; i++) {
            actualParams[i] = params[i];
        }
        actualParams[len] = new Object[]{options};

        byte[] request = DTubeParsingHelper.getSteemitRequest("call", actualParams).getBytes();
        String response = downloader.download(DTubeParsingHelper.STEEMIT_ENDPOINT, request);
        try {
            JsonObject jsonResponse = JsonParser.object().from(response);
            if (jsonResponse.has(Words.RESULT)) {
                for (Object obj : jsonResponse.getArray(Words.RESULT)) {
                    if (!(obj instanceof JsonObject)) {
                        continue;
                    }

                    JsonObject json = (JsonObject) obj;
                    if (json.has(Keys.JSON_METADATA) && (
                        json.isString(Keys.JSON_METADATA) ||
                        json.get(Keys.JSON_METADATA) instanceof JsonObject
                        )) {
                        JsonObject meta = json.isString(Keys.JSON_METADATA) ?
                                JsonParser.object().from(json.getString(Keys.JSON_METADATA)) :
                                json.getObject(Keys.JSON_METADATA);

                        // currently only d.tube support
                        if (meta.has("app")
                            && meta.isString("app")
                            && meta.getString("app").startsWith("dtube")
                            && meta.has(Words.VIDEO)
                            && meta.get(Words.VIDEO) instanceof JsonObject) {
                            collector.commit(new DTubeStreamInfoItemExtractor(service, meta.getObject(Words.VIDEO), json));
                        }
                    }
                }
            } else {
                throw new ParsingException("response \""+jsonResponse.toString()+"\" has no result");
            }
        } catch (JsonParserException ex) {
            throw new ParsingException(ex.getMessage(), ex.getCause());
        }

        return collector;
    }

    protected void ensureCurrentCollectorIsLoaded() throws IOException, ExtractionException {
        if (currentCollector == null) {
            currentCollector = getCollectorWithParams(null);
        }
    }

    public ListExtractor.InfoItemsPage<StreamInfoItem> getInitialPage() throws IOException, ExtractionException {
        ensureCurrentCollectorIsLoaded();
        return new ListExtractor.InfoItemsPage<StreamInfoItem>(currentCollector, getNextPageUrl());
    }

    public String getNextPageUrl() throws IOException, ExtractionException {
        ensureCurrentCollectorIsLoaded();
        List<StreamInfoItem> items = currentCollector.getStreamInfoItemList();
        int size = items.size();
        if (size > 0) {
            StreamInfoItem lastItem = items.get(size - 1);
            urlIdHandler.setUrl(lastItem.getUrl());
            String permId = urlIdHandler.getPermId();
            String author = urlIdHandler.getAuthor();
            return url + "?start_author=" + URLEncoder.encode(author, Encodings.UTF_8) +
                    "&start_permlink=" + URLEncoder.encode(permId, Encodings.UTF_8);
        }
        return url;
    }

    public ListExtractor.InfoItemsPage<StreamInfoItem> getPage(String nextPageUrl) throws IOException, ExtractionException {
        UrlNavigator navi = UrlParsingHelper.parse(
                nextPageUrl,
                Encodings.UTF_8,
                UrlParsingFeature.values()
        );

        List<UrlQuery> publicParams = navi.getPublicParams();
        Map<String, Object> params = new HashMap<String, Object>();
        // TODO move to params package
        if (publicParams.size() > 0) {
            Map<String, List<String>> rawParams = (Map<String, List<String>>) publicParams.get(publicParams.size() - 1);
            for (Map.Entry<String, List<String>> rawParam:rawParams.entrySet()) {
                String key = rawParam.getKey();
                List<String> value = rawParam.getValue();
                int valueSize = value.size();
                if (valueSize > 0) {
                    params.put(key, value.get(valueSize - 1));
                }
            }
        }
        currentCollector = getCollectorWithParams(params);
        return new ListExtractor.InfoItemsPage<StreamInfoItem>(currentCollector, getNextPageUrl());
    }
}
