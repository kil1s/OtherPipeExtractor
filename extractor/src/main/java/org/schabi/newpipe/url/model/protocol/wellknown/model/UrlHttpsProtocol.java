package org.schabi.newpipe.url.model.protocol.wellknown.model;

import org.schabi.newpipe.url.model.UrlQueryState;
import org.schabi.newpipe.url.model.protocol.model.UrlProtocol;

public class UrlHttpsProtocol extends UrlTLSProtocol {
    public UrlHttpsProtocol() {
        this(null);
    }

    public UrlHttpsProtocol(Integer port) {
        super("HTTPS", "Hypertext Transfer Protocol Secure", UrlQueryState.OTHER, port);
    }

    public UrlHttpsProtocol(String name, String readableName, UrlQueryState state, Integer port) {
        super(name, readableName, state, port);
    }

    public UrlHttpsProtocol(String name, String readableName, UrlQueryState state) {
        this(name, readableName, state, null);
    }

    @Override
    public UrlProtocol clone() {
        return new UrlHttpsProtocol(name, readableName, state, port);
    }

    @Override
    public UrlProtocol clonePrivate() {
        return new UrlHttpsProtocol(name, readableName, UrlQueryState.PRIVATE, port);
    }

    @Override
    public UrlProtocol clonePublic() {
        return new UrlHttpsProtocol(name, readableName, UrlQueryState.PUBLIC, port);
    }
}
