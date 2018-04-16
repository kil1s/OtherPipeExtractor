package org.schabi.newpipe.extractor.url.model.protocol.wellknown.model;

import org.schabi.newpipe.extractor.url.model.UrlQueryState;
import org.schabi.newpipe.extractor.url.model.protocol.model.UrlProtocol;
import org.schabi.newpipe.extractor.url.model.protocol.model.UrlProtocolAbstract;

public class UrlTCPProtocol extends UrlProtocolAbstract {
    public UrlTCPProtocol() {
        this(null);
    }

    public UrlTCPProtocol(Integer port) {
        super("TCP", "Transmission Control Protocol", UrlQueryState.OTHER, port);
    }

    public UrlTCPProtocol(String name, String readableName, UrlQueryState state, Integer port) {
        super(name, readableName, state, port);
    }

    public UrlTCPProtocol(String name, String readableName, UrlQueryState state) {
        this(name, readableName, state, null);
    }

    @Override
    public UrlProtocol clone() {
        return new UrlTCPProtocol(name, readableName, state, port);
    }

    @Override
    public UrlProtocol clonePrivate() {
        return new UrlTCPProtocol(name, readableName, UrlQueryState.PRIVATE, port);
    }

    @Override
    public UrlProtocol clonePublic() {
        return new UrlTCPProtocol(name, readableName, UrlQueryState.PUBLIC, port);
    }
}
