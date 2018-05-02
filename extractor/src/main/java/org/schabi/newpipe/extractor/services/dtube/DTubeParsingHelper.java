package org.schabi.newpipe.extractor.services.dtube;

import com.grack.nanojson.*;
import org.schabi.newpipe.http.HttpDownloader;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.constants.Encodings;
import org.schabi.newpipe.extractor.constants.Keys;
import org.schabi.newpipe.extractor.constants.Versions;
import org.schabi.newpipe.extractor.constants.Words;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;
import org.schabi.newpipe.settings.model.settings.interfaces.Settings;
import org.schabi.newpipe.extractor.stream.StreamInfoItemExtractor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DTubeParsingHelper {
    public static final String DOMAIN_ENDPOINT = "https://d.tube";
    public static final String STEEMIT_ENDPOINT = "https://api.steemit.com";
    public static final String STEEMIT_IMG_ENDPOINT = "https://steemitimages.com";

    public static final String DIRECT_FILES_ENDPOINT = DOMAIN_ENDPOINT+"/DTube_files";
    public static final String ASKSTEEM_ENDPOINT = "https://api.asksteem.com";

    public static class DTubeResultAndMeta {
        private JsonObject result;
        private JsonObject meta;

        public DTubeResultAndMeta(JsonObject result, JsonObject meta) {
            this.result = result;
            this.meta = meta;
        }

        public JsonObject getResult() {
            return result;
        }

        public JsonObject getMeta() {
            return meta;
        }
    }

    public static class DTubePathReturnData<D> {
        private String path;
        private D data;

        public DTubePathReturnData(String path, D data) {
            this.path = path;
            this.data = data;
        }

        public D getData() {
            return data;
        }

        public String getPath() {
            return path;
        }
    }

    private static int id = -1;

    public synchronized static int getId() {
        id++;
        return id;
    }

    public static String getSteemitRequest(String method, Object[] params) {
        JsonStringWriter steemitRequestsWriter = JsonWriter.string().object();
        steemitRequestsWriter.value(Words.ID, getId());
        steemitRequestsWriter.value(Words.JSONRPC, Versions.SECOND_ONE_DOT);
        steemitRequestsWriter.value(Words.METHOD, method);
        steemitRequestsWriter.value(Words.PARAMS, params);
        return steemitRequestsWriter.end().done();
    }

    public static long getLongTimeJson(JsonObject json, String path, String stringedDataKey, String dateFormat, SimpleDateFormat dateFormatter) throws ParsingException {
        if (json.has(stringedDataKey)) {
            if (json.isString(stringedDataKey)) {
                try {
                    return dateFormatter.parse(json.getString(stringedDataKey)).getTime();
                } catch (ParseException e) {
                    throw new ParsingException("Can not parse date pattern \""+dateFormat+"\"", e);
                }
            } else {
                throw new ParsingException(path+"."+ stringedDataKey +" is not a number");
            }
        }
        throw new ParsingException(path+" got no "+ stringedDataKey +" object");
    }

    public static DTubePathReturnData<JsonObject> getInfoObject(JsonObject meta, String rootPath) throws ParsingException {
        String failSafeRootPath = rootPath == null ? Words.ANONYMOUS : rootPath;
        String path = failSafeRootPath+"."+ Words.INFO;
        if (meta.has(Words.INFO)) {
            return new DTubePathReturnData<JsonObject>(path, meta.getObject(Words.INFO));
        }
        throw new ParsingException(path+" do not exists");
    }

    public static DTubePathReturnData<JsonObject> getContentObject(JsonObject meta, String rootPath) throws ParsingException {
        String failSafeRootPath = rootPath == null ? Words.ANONYMOUS : rootPath;
        String path = failSafeRootPath+"."+Words.CONTENT;
        if (meta.has(Words.CONTENT)) {
            return new DTubePathReturnData<JsonObject>(path, meta.getObject(Words.CONTENT));
        }
        throw new ParsingException(path+" do not exists");
    }

    public static DTubePathReturnData<String> getStringFromMetaInfo(JsonObject meta, String key, String rootPath) throws ParsingException {
        DTubePathReturnData<JsonObject> args = getInfoObject(meta,rootPath);
        return getStringFromJson(args.getPath(), args.getData(), key);
    }

    public static DTubePathReturnData<String> getStringFromMetaInfo(JsonObject meta, String key) throws ParsingException {
        DTubePathReturnData<JsonObject> args = getInfoObject(meta,null);
        return getStringFromJson(args.getPath(), args.getData(), key);
    }

    public static DTubePathReturnData<String> getStringFromJson(String path, JsonObject json, String key) throws ParsingException {
        if (json.has(key)) {
            path += "."+key;
            if (json.isString(key)) {
                return new DTubePathReturnData<String>(path, json.getString(key));
            } else {
                throw new ParsingException(path+" is not string");
            }
        }
        throw new ParsingException(path+" got no "+key+" object");
    }

    // TODO remove and use other method instead
    public static long getLongTimeJsonFromResult(JsonObject resultData, String stringedDataKey, String dateFormat, SimpleDateFormat dateFormatter) throws ParsingException {
        return getLongTimeJson(resultData, Words.RESULT, stringedDataKey, dateFormat, dateFormatter);
    }

    public static String getThumbnail(JsonObject meta, StreamingService service) throws ParsingException {
        DTubeParsingHelper.DTubePathReturnData<JsonObject> args = DTubeParsingHelper.getInfoObject(meta, null);
        JsonObject info = args.getData();
        String path = args.getPath();
        if (info.has(Words.SNAPHASH)) {
            path += "."+Words.SNAPHASH;
            if (info.isString(Words.SNAPHASH)) {
                String thumbnailIdHash = info.getString(Words.SNAPHASH);
                if (!thumbnailIdHash.trim().isEmpty()) {
                    Settings settings = service.getSettings();
                    if (settings.has(DTubeSettings.ID_IPFS_GATEWAYS)) {
                        List<String> gateways = (List<String>) settings.get(DTubeSettings.ID_IPFS_GATEWAYS);
                        if (gateways.size() > 0) {
                            return gateways.get(0) + "/" + thumbnailIdHash;
                        }
                    }
                    throw new ParsingException("Their is no gateway in the settings for resolve the thumbnail");
                }
                throw new ParsingException("Thumbnail id is not valid (no empty (spaces))");
            }
            throw new ParsingException("\""+ path +"\" is no String");
        }
        throw new ParsingException("\""+ path +"\" got no key \""+ Words.SNAPHASH +"\"");
    }

    public static DTubeResultAndMeta getResultAndMetaFromSteemitContent(HttpDownloader downloader, String typeKey, Object[] params) throws ParsingException, UnsupportedEncodingException, IOException, ReCaptchaException {
        return getResultAndMetaFromSteemitContent(downloader, "get_content", typeKey, params);
    }

    public static DTubeResultAndMeta getResultAndMetaFromSteemitContent(HttpDownloader downloader,  String callName, String typeKey, Object[] params) throws ParsingException, UnsupportedEncodingException, IOException, ReCaptchaException {
        JsonObject metaData;
        JsonObject resultData = null;

        String strRequestBody = getSteemitRequest(callName, params);
        byte[] requestBody = strRequestBody.getBytes(Encodings.UTF_8);
        String strResponse = downloader.download(STEEMIT_ENDPOINT, requestBody);
        try {
            JsonObject response = JsonParser.object().from(strResponse);
            if (response.has(Words.RESULT)) {
                Object data = response.get(Words.RESULT);
                if (data instanceof JsonArray) {
                    JsonArray arr = (JsonArray) data;
                    if (arr.size() > 0) {
                        boolean gotJsonObject = false;
                        for (Object obj:arr) {
                            if (obj instanceof JsonObject) {
                                resultData = (JsonObject) obj;
                                gotJsonObject = true;
                                break;
                            }
                        }

                        if (!gotJsonObject) {
                            throw new ParsingException("none of the result array entries '"+arr.toString()+"' is a object");
                        }
                    } else {
                        throw new ParsingException("the result array '"+arr.toString()+"' is empty or broken");
                    }
                } else if (data instanceof JsonObject) {
                    resultData = (JsonObject) data;
                }

                if (resultData == null) {
                    throw new ParsingException("result '"+resultData.toString()+"' is not a object and not a array");
                }

                if (resultData.has(Keys.JSON_METADATA) && resultData.isString(Keys.JSON_METADATA)) {
                    try {
                        metaData = JsonParser.object().from(resultData.getString(Keys.JSON_METADATA));
                        if (metaData.has(typeKey)) {
                            if (metaData.get(typeKey) instanceof JsonObject) {
                                metaData = metaData.getObject(typeKey);
                            } else {
                                throw new ParsingException("\""+typeKey+"\" endpoint is not a object");
                            }
                        } else {
                            throw new ParsingException("The meta data need to got a \""+typeKey+"\" endpoint");
                        }
                    } catch (JsonParserException e) {
                        throw new ParsingException("Could not parse meta json", e);
                    }
                } else {
                    throw new ParsingException("result \""+resultData.toString()+"\" has no json_metadata");
                }
            } else {
                throw new ParsingException("response \""+response.toString()+"\" has no result");
            }
        } catch (JsonParserException e) {
            throw new ParsingException("Could not parse result json", e);
        }

        return new DTubeResultAndMeta(resultData, metaData);
    }

    public static List<StreamInfoItemExtractor> getAskSteemStreamExtractorsByUrl(String url, StreamingService service) throws ParsingException, IOException, ReCaptchaException {
        List<StreamInfoItemExtractor> extractors = new ArrayList<StreamInfoItemExtractor>();

        HttpDownloader dl = NewPipe.getDownloader();
        String strContent = dl.download(url);
        try {
            JsonObject object = JsonParser.object().from(strContent);
            if ((!object.has(Words.RESULTS)) && object.has("error") && object.isBoolean("error") && object.getBoolean("error")) {
                String message = object.has("message") && object.isString("message") ? object.getString("message") : "server response with error and without message";
                throw new ParsingException(message);
            }
            if (object.has(Words.RESULTS) && object.get(Words.RESULTS) instanceof JsonArray) {
                for (Object video:object.getArray(Words.RESULTS)) {
                    if (!(video instanceof JsonObject)) {
                        continue;
                    }

                    JsonObject jsonVideo = (JsonObject) video;
                    if (jsonVideo.has(Words.META) && jsonVideo.get(Words.META) instanceof JsonObject) {
                        extractors.add(new DTubeStreamInfoItemExtractor(service, jsonVideo.getObject(Words.META), jsonVideo));
                    }
                }
            }
        } catch (JsonParserException e) {
            throw new ParsingException(e.getMessage(), e);
        }
        return extractors;
    }
}
