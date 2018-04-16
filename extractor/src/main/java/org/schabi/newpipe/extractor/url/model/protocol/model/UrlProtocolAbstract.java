package org.schabi.newpipe.extractor.url.model.protocol.model;

import org.schabi.newpipe.extractor.url.model.UrlQueryState;

public abstract class UrlProtocolAbstract implements UrlProtocol {
    protected String name;
    protected String readableName;
    protected Integer port;
    protected UrlQueryState state;

    public UrlProtocolAbstract(String name, String readableName, UrlQueryState state) {
        this(name, readableName, state,null);
    }

    public UrlProtocolAbstract(String name, String readableName, UrlQueryState state, Integer port) {
        this.name = name;
        this.readableName = readableName;
        this.port = port;
        this.state = state;
    }

    @Override
    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public boolean gotPort() {
        return port != null;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getReadableName() {
        return readableName;
    }

    @Override
    public UrlQueryState getState() {
        return state;
    }

    @Override
    public abstract UrlProtocol clone();

    @Override
    public abstract UrlProtocol clonePrivate();

    @Override
    public abstract UrlProtocol clonePublic();
}
