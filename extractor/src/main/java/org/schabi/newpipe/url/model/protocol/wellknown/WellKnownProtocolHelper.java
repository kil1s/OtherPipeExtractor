package org.schabi.newpipe.url.model.protocol.wellknown;

import org.schabi.newpipe.url.model.UrlQuery;
import org.schabi.newpipe.url.model.UrlQueryState;
import org.schabi.newpipe.url.model.protocol.model.UrlProtocol;
import org.schabi.newpipe.url.model.protocol.wellknown.model.*;

public enum WellKnownProtocolHelper implements UrlQuery {
    HTTP(new UrlHttpProtocol(80)),
    HTTPS(new UrlHttpsProtocol(443)),
    TCP(new UrlTCPProtocol()),
    TLS(new UrlTLSProtocol()),
    UNKNOWN(new UrlUnknownProtocol("unknown","Unknown Protocol", UrlQueryState.OTHER));

    protected UrlProtocol protocol;

    WellKnownProtocolHelper(UrlProtocol protocol) {
        this.protocol = protocol;
    }

    public UrlProtocol getProtocol() {
        return protocol;
    }

    public String getName() {
        return protocol.getName();
    }

    public String getReadableName() {
        return protocol.getReadableName();
    }

    public static UrlProtocol selectProtocolByName(String name) {
        return selectProtocolByName(name, null);
    }

    public static UrlProtocol selectProtocolByName(String name, Integer port) {
        for (WellKnownProtocolHelper wellKnownProtocol: WellKnownProtocolHelper.values()) {
            if (wellKnownProtocol.getName().equalsIgnoreCase(name)) {
                UrlProtocol protocol = wellKnownProtocol.getProtocol().clone();
                if (port != null) {
                    protocol.setPort(port);
                }
                return protocol;
            }
        }

        String unknownReadableName = WellKnownProtocolHelper.UNKNOWN.getReadableName();
        if (port != null) {
            return new UrlUnknownProtocol(name.toUpperCase(), unknownReadableName, UrlQueryState.OTHER, port);
        }
        return new UrlUnknownProtocol(name.toUpperCase(), unknownReadableName, UrlQueryState.OTHER);
    }
}
