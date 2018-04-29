package org.schabi.newpipe.url.model.protocol.wellknown.model;

import org.schabi.newpipe.url.model.UrlQueryState;
import org.schabi.newpipe.url.model.protocol.model.UrlProtocol;
import org.schabi.newpipe.url.model.protocol.model.UrlProtocolAbstract;

public class UrlUnknownProtocol extends UrlProtocolAbstract {
    public UrlUnknownProtocol(String name, String readableName, UrlQueryState state, Integer port) {
        super(name, readableName, state, port);
    }

    public UrlUnknownProtocol(String name, String readableName, UrlQueryState state) {
        this(name, readableName, state, null);
    }

    @Override
    public UrlProtocol clone() {
        return new UrlUnknownProtocol(name, readableName, state, port);
    }

    @Override
    public UrlProtocol clonePrivate() {
        return new UrlUnknownProtocol(name, readableName, UrlQueryState.PRIVATE, port);
    }

    @Override
    public UrlProtocol clonePublic() {
        return new UrlUnknownProtocol(name, readableName, UrlQueryState.PUBLIC, port);
    }
}
