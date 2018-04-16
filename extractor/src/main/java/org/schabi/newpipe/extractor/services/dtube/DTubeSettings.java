package org.schabi.newpipe.extractor.services.dtube;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import org.schabi.newpipe.extractor.Downloader;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.constants.Encodings;
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;
import org.schabi.newpipe.extractor.settings.exceptions.WrongSettingsDataException;
import org.schabi.newpipe.extractor.settings.model.provider.StringListProvider;
import org.schabi.newpipe.extractor.settings.model.settings.abstracts.OverrideDynamicSettings;
import org.schabi.newpipe.extractor.url.helper.UrlParsingHelper;
import org.schabi.newpipe.extractor.url.model.UrlParsingFeature;
import org.schabi.newpipe.extractor.url.model.UrlQueryState;
import org.schabi.newpipe.extractor.url.model.protocol.wellknown.WellKnownProtocolHelper;
import org.schabi.newpipe.extractor.url.navigator.UrlNavigator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class DTubeSettings extends OverrideDynamicSettings {
    public static final String ID_IPFS_ENDPOINT = "ipfs";
    public static final String ID_IPFS_GATEWAYS = ID_IPFS_ENDPOINT+".gateways";

    public static final String REMOTE = "remote";
    public static final String DISPLAY_NODES = "displayNodes";

    /*
     * getSafeIPFSGateways
     *
     * @return List of all gateways that are not 127.0.0.1 or localhost and using https
     */
    public static List<String> getSafeIPFSGateways(List<String> gateways) throws UnsupportedEncodingException {
        List<String> safeGateways = new ArrayList<String>();
        for (String gateway:gateways) {
            UrlNavigator navi = UrlParsingHelper.parseTillPort(
                    gateway,
                    Encodings.UTF_8,
                    false,
                    UrlQueryState.PUBLIC
            );

            boolean isUnsafe = navi.gotDomain("127.0.0.1") && navi.gotDomain("localhost");
            if ((!isUnsafe) && navi.gotProtocol(WellKnownProtocolHelper.HTTPS.getProtocol())) {
                safeGateways.add(gateway.endsWith("/") ? gateway.substring(1) : gateway);
            }
        }
        return safeGateways;
    }

    @Override
    public void refresh() throws IOException, ReCaptchaException {
        Downloader dl = NewPipe.getDownloader();
        String settings = dl.download(DTubeParsingHelper.DIRECT_FILES_ENDPOINT +"/settings.json");
        try {
            JsonObject jsonSettings = JsonParser.object().from(settings);
            if (jsonSettings.has(REMOTE) && jsonSettings.get(REMOTE) instanceof JsonObject) {
                JsonObject remoteJson = jsonSettings.getObject(REMOTE);
                if (remoteJson.has(DISPLAY_NODES) && remoteJson.get(DISPLAY_NODES) instanceof JsonArray) {
                    List<String> urls = new ArrayList<String>();

                    for (Object displayNode:remoteJson.getArray(DISPLAY_NODES)) {
                        if (displayNode instanceof String) {
                            String url = (String) displayNode;

                            UrlNavigator navi = UrlParsingHelper.parse(
                                    url,
                                    Encodings.UTF_8,
                                    UrlParsingFeature.values()
                            );
                            if (navi.gotProtocol(WellKnownProtocolHelper.HTTPS.getProtocol())) {
                                urls.add(url);
                            }
                        }
                    }

                    liveProviders.put(ID_IPFS_GATEWAYS, new StringListProvider(getSafeIPFSGateways(urls)));
                    needUpdateCache = true;
                }
            }
        } catch (JsonParserException e) {
            throw new IOException("Settings are no valid JSON", e);
        } catch (WrongSettingsDataException e) {
            throw new IOException("Settings are not in valid format", e);
        }
    }
}
