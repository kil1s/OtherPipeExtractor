package org.schabi.newpipe.extractor.url.model.protocol;

import org.schabi.newpipe.extractor.url.model.UrlQuery;

public enum UrlProtocolTyp implements UrlQuery {
    HTTP(new UrlHttpProtocol(), 80),
    HTTPS(new UrlHttpsProtocol(), 443),
    TCP(new UrlTCPProtocol()),
    TLS(new UrlTLSProtocol()),
    UNKNOWN(new UrlUnknownProtocol("unknown","Unknown Protocol"));

    private UrlProtocol protocol;
    private Integer port;

    UrlProtocolTyp(UrlProtocol protocol) {
        this(protocol, null);
    }

    UrlProtocolTyp(UrlProtocol protocol, Integer port) {
        this.protocol = protocol;
        this.port = port;
    }

    protected void setProtocol(UrlProtocol protocol) {
        this.protocol = protocol;
    }

    protected void setPort(Integer port) {
        this.port = port;
    }

    public boolean gotPort() {
        return port != null;
    }

    public Integer getPort() {
        return port;
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

    public static UrlProtocolTyp selectProtocolByName(String name) {
        return selectProtocolByName(name, null);
    }

    public static UrlProtocolTyp selectProtocolByName(String name, Integer port) {
        for (UrlProtocolTyp wellKnownProtocol: UrlProtocolTyp.values()) {
            if (wellKnownProtocol.getName().equalsIgnoreCase(name)) {
                if (port != null) {
                    wellKnownProtocol.setPort(port);
                }
                return wellKnownProtocol;
            }
        }
        UrlProtocolTyp unknownTyp = UrlProtocolTyp.UNKNOWN;
        unknownTyp.setProtocol(new UrlUnknownProtocol(name.toUpperCase(), unknownTyp.getReadableName()));
        unknownTyp.setPort(port);
        return unknownTyp;
    }
}
