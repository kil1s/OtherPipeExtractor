package org.schabi.newpipe.extractor.services.dtube;

import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import org.schabi.newpipe.http.HttpDownloader;
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
import org.schabi.newpipe.url.helper.UrlParsingHelper;
import org.schabi.newpipe.url.model.UrlParsingFeature;
import org.schabi.newpipe.url.model.UrlQuery;
import org.schabi.newpipe.url.navigator.UrlNavigator;

import java.io.IOException;
import java.net.URLEncoder;
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

    public DTubeStreamInfoItemNavigator(String url, int limit, String tag, StreamingService service, Object[] params, DTubeUrlIdHandler urlIdHandler) {
        this.url = url;
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
        if (params != null) {
            for (Map.Entry<String, Object> entry:addOptions.entrySet()) {
                options.put(entry.getKey(), entry.getValue());
            }
        }

        int len = params.length;
        Object[] actualParams = new Object[len];
        for (int i = 0; i < len; i++) {
            actualParams[i] = params[i];
        }
        actualParams[len] = options;

        byte[] request = DTubeParsingHelper.getSteemitRequest("call", actualParams).getBytes();
        String response = downloader.download(DTubeParsingHelper.STEEMIT_ENDPOINT, request);
        try {
            for (Object obj: JsonParser.object().from(response).getArray(Words.RESULT)) {
                if (!(obj instanceof JsonObject)) {
                    continue;
                }

                JsonObject json = (JsonObject) obj;
                if (json.has(Keys.JSON_METADATA) && json.get(Keys.JSON_METADATA) instanceof JsonObject) {
                    JsonObject meta = json.getObject(Keys.JSON_METADATA);
                    if (meta.has(Words.VIDEO) && meta.get(Words.VIDEO) instanceof JsonObject) {
                        if (meta.has(Words.INFO) && meta.get(Words.INFO) instanceof JsonObject) {
                            collector.commit(new DTubeStreamInfoItemExtractor(service, meta.getObject(Words.VIDEO).getObject(Words.INFO), json));
                        }
                    }
                }
            }
        } catch (JsonParserException ex) {
            throw new ParsingException(ex.getMessage(), ex.getCause());
        }

        return collector;
    }

    public ListExtractor.InfoItemsPage<StreamInfoItem> getInitialPage() throws IOException, ExtractionException {
        currentCollector = getCollectorWithParams(null);
        return new ListExtractor.InfoItemsPage<StreamInfoItem>(currentCollector, getNextPageUrl());
    }

    public String getNextPageUrl() throws IOException, ExtractionException {
        List<StreamInfoItem> items = currentCollector.getStreamInfoItemList();
        StreamInfoItem lastItem = items.get(items.size()-1);
        String permId = urlIdHandler.getPermId((lastItem.getUrl()));
        return url+"?start_author="+ URLEncoder.encode(lastItem.getUploaderUrl(), Encodings.UTF_8)+"&start_permlink="+URLEncoder.encode(permId, Encodings.UTF_8);
    }

    public ListExtractor.InfoItemsPage<StreamInfoItem> getPage(String nextPageUrl) throws IOException, ExtractionException {
        UrlNavigator navi = UrlParsingHelper.parse(
                nextPageUrl,
                Encodings.UTF_8,
                UrlParsingFeature.values()
        );

        List<UrlQuery> publicParams = navi.getPublicParams();
        currentCollector = getCollectorWithParams((Map) publicParams.get(publicParams.size()-1));

        return new ListExtractor.InfoItemsPage<StreamInfoItem>(currentCollector, getNextPageUrl());
    }
}
