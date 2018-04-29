package org.schabi.newpipe.extractor.services.dtube;

import org.schabi.newpipe.extractor.UrlIdHandler;
import org.schabi.newpipe.extractor.constants.Encodings;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.url.helper.UrlParsingHelper;
import org.schabi.newpipe.url.model.UrlParsingFeature;
import org.schabi.newpipe.url.model.UrlQuery;
import org.schabi.newpipe.url.model.list.UrlPseudoQueryList;
import org.schabi.newpipe.url.model.list.filepath.UrlFilepathPrivate;
import org.schabi.newpipe.url.model.protocol.wellknown.WellKnownProtocolHelper;
import org.schabi.newpipe.url.navigator.UrlNavigator;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class DTubeUrlIdHandler implements UrlIdHandler {
    private static final DTubeUrlIdHandler channelInstance = new DTubeUrlIdHandler(false, true, false);
    private static final DTubeUrlIdHandler videoInstance = new DTubeUrlIdHandler(true, false, false);
    private static final DTubeUrlIdHandler kioskInstance = new DTubeUrlIdHandler(false, false, true);

    private static final String VIDEO = "v";
    private static final String CHANNEL = "c";

    private boolean acceptVideo;
    private boolean acceptChannel;
    private boolean acceptKiosk;

    public DTubeUrlIdHandler(boolean acceptVideo, boolean acceptChannel, boolean acceptKiosk) {
        this.acceptVideo = acceptVideo;
        this.acceptChannel = acceptChannel;
        this.acceptKiosk = acceptKiosk;
    }

    public static DTubeUrlIdHandler getVideoInstance() {
        return videoInstance;
    }

    public static DTubeUrlIdHandler getChannelInstance() {
        return channelInstance;
    }

    public static DTubeUrlIdHandler getKioskInstance() {
        return kioskInstance;
    }

    @Override
    public String getUrl(String id) throws ParsingException {
        try {
            List<String> partsFromPath = UrlParsingHelper.getPartsFromPath(
                    id,
                    true,
                    Encodings.UTF_8,
                    UrlParsingHelper.FILE_PATH_DELIMITER
            );
            if (partsFromPath.size() > 0) {
                String firstPart = partsFromPath.get(0);
                if (firstPart.startsWith("@")) {
                    String author = firstPart.substring(1);

                    if (partsFromPath.size() == 1) {
                        return DTubeParsingHelper.DOMAIN_ENDPOINT +"/"+CHANNEL+"/" + author;
                    } else if (partsFromPath.size() == 2) {
                        String video = partsFromPath.get(1);
                        return DTubeParsingHelper.DOMAIN_ENDPOINT +"/"+VIDEO+"/" + author + "/" + video;
                    }
                    throw new ParsingException("The dtube id need to contains one or two arguments");
                } else if (partsFromPath.size() == 1) {
                    return DTubeParsingHelper.DOMAIN_ENDPOINT +"/" + firstPart;
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new ParsingException(e.getMessage(), e.getCause());
        }
        throw new ParsingException("The id do not match the requirements of an id");
    }

    @Override
    public String getId(String url) throws ParsingException {
        try {
            UrlNavigator navi = UrlParsingHelper.parse(
                    url,
                    Encodings.UTF_8,
                    UrlParsingFeature.values()
            );

            for (UrlQuery queryPath:navi.getFilepaths()) {
                UrlPseudoQueryList<String> filepath = (UrlPseudoQueryList<String>) queryPath;
                int size = filepath.size();
                if (size > 0) {
                    String indicator = filepath.get(0);
                    if (DTubeKiosk.getKioskById(indicator) != null) {
                        return filepath.get(1);
                    } else if (size > 1 && CHANNEL.equals(indicator)) {
                        return "@" + filepath.get(1);
                    } else if (size > 2 && VIDEO.equals(indicator)) {
                        String author = filepath.get(1);
                        String video = filepath.get(2);
                        return "@" + author + "/" + video;
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new ParsingException(e.getMessage(), e.getCause());
        }
        throw new ParsingException("The dtube url do not contains a complete id");
    }

    public String getPermId(String url) throws ParsingException {
        try {
            UrlNavigator navi = UrlParsingHelper.parse(
                    url,
                    Encodings.UTF_8,
                    UrlParsingFeature.values()
            );
            for (UrlQuery queryPath:navi.getFilepaths()) {
                UrlPseudoQueryList<String> filepath = (UrlPseudoQueryList<String>) queryPath;

                if (filepath.size() > 2 && VIDEO.equals(filepath.get(0))) {
                    return filepath.get(2);
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new ParsingException(e.getMessage(), e.getCause());
        }
        throw new ParsingException("The dtube url do not contains a perm id");
    }

    @Override
    public String cleanUrl(String complexUrl) throws ParsingException {
        return complexUrl;
    }

    @Override
    public boolean acceptUrl(String url) {
        try {
            UrlNavigator navi = UrlParsingHelper.parse(
                    url,
                    Encodings.UTF_8,
                    UrlParsingFeature.values()
            );
            boolean noWrongProtocol = navi.gotProtocol(WellKnownProtocolHelper.HTTP.getProtocol()) || navi.gotProtocol(WellKnownProtocolHelper.HTTPS.getProtocol());
            if (noWrongProtocol && navi.gotDomain("d.tube")) {
                for (UrlQuery queryPath:navi.getFilepaths()) {
                    UrlPseudoQueryList<String> filepath = (UrlPseudoQueryList<String>) queryPath;
                    int size = filepath.size();
                    if (size > 0) {
                        String indicator = filepath.get(0);
                        if (DTubeKiosk.getKioskById(indicator) != null && acceptKiosk) {
                            return true;
                        }
                        if (size > 1 && indicator.equals(VIDEO) && acceptVideo) {
                            return true;
                        }
                        if (size > 2 && indicator.equals(CHANNEL) && acceptChannel) {
                            return true;
                        }
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            // TODO better error handling
        }
        return false;
    }
}
