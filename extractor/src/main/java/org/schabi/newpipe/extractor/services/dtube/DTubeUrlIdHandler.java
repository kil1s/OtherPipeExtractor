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

    protected interface DTubeDataHunter<H, P, E extends Exception> {
        // return null if no  data is given
        H get(P...params) throws E;
    }

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

    private String acceptString() {
        return "accept { "+
                (acceptVideo ? "VIDEO" : "")+
                (acceptChannel ? "CHANNEL" : "")+
                (acceptKiosk ? "KIOSK" : "")
                + " }";
    }

    @Override
    public String toString() {
        return "DTubeUrlIdHandler { "+
                acceptString()
                + " }";
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
            if (partsFromPath.size() > 0 && partsFromPath.size() < 3) {
                String firstPart = partsFromPath.get(0);
                String author = firstPart.startsWith("@") ? firstPart.substring(1) : firstPart;

                if (partsFromPath.size() == 1 && DTubeKiosk.getKioskById(firstPart) != null && acceptKiosk) {
                    return DTubeParsingHelper.DOMAIN_ENDPOINT +"/" + firstPart;
                } else if (partsFromPath.size() == 1 && acceptChannel) {
                    return DTubeParsingHelper.DOMAIN_ENDPOINT +"/"+CHANNEL+"/" + author;
                } else if (partsFromPath.size() == 2 && acceptVideo) {
                    String video = partsFromPath.get(1);
                    return DTubeParsingHelper.DOMAIN_ENDPOINT +"/"+VIDEO+"/" + author + "/" + video;
                }
            } else {
                throw new ParsingException("The dtube id need to contains one or two arguments ("+acceptString()+")");
            }
        } catch (UnsupportedEncodingException e) {
            throw new ParsingException(e.getMessage(), e.getCause());
        }
        throw new ParsingException("The id do not match the requirements of an id ("+acceptString()+")");
    }

    protected Object getDataFromUrlParse(String url, String huntedName, DTubeDataHunter<?, UrlPseudoQueryList, ParsingException> hunter) throws ParsingException {
        if (url == null) {
            throw new IllegalArgumentException("url shouldn't be null");
        }
        try {
            UrlNavigator navi = UrlParsingHelper.parse(
                    url,
                    Encodings.UTF_8,
                    UrlParsingFeature.values()
            );

            for (UrlQuery queryPath : navi.getFilepaths()) {
                UrlPseudoQueryList<String> filepath = (UrlPseudoQueryList<String>) queryPath;
                Object result = hunter.get(filepath);
                if (result != null) {
                    return result;
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new ParsingException(e.getMessage(), e.getCause());
        }
        throw new ParsingException("The dtube url do not contains a "+huntedName+" (" + acceptString() + ")");
    }

    @Override
    public String getId(String url) throws ParsingException {
        return (String) getDataFromUrlParse(url, "complete id", new DTubeDataHunter<String, UrlPseudoQueryList, ParsingException>() {
            @Override
            public String get(UrlPseudoQueryList... params) throws ParsingException {
                UrlPseudoQueryList<String> filepath = (UrlPseudoQueryList<String>) params[0];
                int size = filepath.size();
                if (size > 0) {
                    // old fashion @ support which is used by steemit in an older api
                    // TODO remove @ support
                    String indicator = filepath.get(0);
                    if (DTubeKiosk.getKioskById(indicator) != null && acceptKiosk) {
                        return indicator;
                    } else if (size > 1 && CHANNEL.equals(indicator) && acceptChannel) {
                        return "@" + filepath.get(1);
                    } else if (size > 2 && VIDEO.equals(indicator) && acceptVideo) {
                        String author = filepath.get(1);
                        String video = filepath.get(2);
                        return "@" + author + "/" + video;
                    }
                }
                return null;
            }
        });
    }

    public String getPermId(String url) throws ParsingException {
        return (String) getDataFromUrlParse(url, "perm id", new DTubeDataHunter<String, UrlPseudoQueryList, ParsingException>() {
            @Override
            public String get(UrlPseudoQueryList... params) throws ParsingException {
                UrlPseudoQueryList<String> filepath = (UrlPseudoQueryList<String>) params[0];

                if (filepath.size() > 2 && VIDEO.equals(filepath.get(0)) && acceptVideo) {
                    return filepath.get(2);
                }
                return null;
            }
        });
    }

    public String getAuthor(String url) throws ParsingException {
        return (String) getDataFromUrlParse(url, "author", new DTubeDataHunter<String, UrlPseudoQueryList, ParsingException>() {
            @Override
            public String get(UrlPseudoQueryList... params) throws ParsingException {
                UrlPseudoQueryList<String> filepath = (UrlPseudoQueryList<String>) params[0];
                int size = filepath.size();
                if (size > 0) {
                    String indicator = filepath.get(0);
                    if ((size > 1 && CHANNEL.equals(indicator) && acceptChannel) ||
                        (size > 2 && VIDEO.equals(indicator) && acceptVideo)) {
                        return filepath.get(1);
                    }
                }

                return null;
            }
        });
    }

    public Object[] getSteemitParams(String url) throws ParsingException {
        return (Object[]) getDataFromUrlParse(url, "ids that can be casted to steemit params", new DTubeDataHunter<Object[], UrlPseudoQueryList, ParsingException>() {
            @Override
            public Object[] get(UrlPseudoQueryList... params) throws ParsingException {
                UrlPseudoQueryList<String> filepath = (UrlPseudoQueryList<String>) params[0];
                int size = filepath.size();
                if (size > 0) {
                    String indicator = filepath.get(0);
                    if (size > 1 && CHANNEL.equals(indicator) && acceptChannel) {
                        return new Object[][]{new String[]{filepath.get(1)}};
                    } else if (size > 2 && VIDEO.equals(indicator) && acceptVideo) {
                        return new String[]{filepath.get(1), filepath.get(2)};
                    }
                }
                return null;
            }
        });
    }

    @Override
    public String cleanUrl(String complexUrl) throws ParsingException {
        return (String) getDataFromUrlParse(complexUrl, "clean url", new DTubeDataHunter<String, UrlPseudoQueryList, ParsingException>() {
            @Override
            public String get(UrlPseudoQueryList... params) throws ParsingException {
                UrlPseudoQueryList<String> filepath = (UrlPseudoQueryList<String>) params[0];
                int size = filepath.size();
                if (size > 0) {
                    String indicator = filepath.get(0);
                    if (DTubeKiosk.getKioskById(indicator) != null && acceptKiosk) {
                        return DTubeParsingHelper.DOMAIN_ENDPOINT + "/" + indicator;
                    } else if (size > 1 && CHANNEL.equals(indicator) && acceptChannel) {
                        return DTubeParsingHelper.DOMAIN_ENDPOINT + "/" + CHANNEL + "/" + filepath.get(1);
                    } else if (size > 2 && VIDEO.equals(indicator) && acceptVideo) {
                        String author = filepath.get(1);
                        String video = filepath.get(2);
                        return DTubeParsingHelper.DOMAIN_ENDPOINT + "/" + VIDEO + "/" + author + "/" + video;
                    }
                }
                return null;
            }
        });
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
