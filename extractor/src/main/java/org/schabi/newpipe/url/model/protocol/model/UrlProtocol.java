package org.schabi.newpipe.url.model.protocol.model;

import org.schabi.newpipe.url.model.UrlQuery;
import org.schabi.newpipe.url.model.UrlQueryState;

public interface UrlProtocol extends UrlQuery {
    void setPort(Integer port);
    boolean gotPort();
    Integer getPort();
    String getName();
    String getReadableName();
    UrlProtocol clone();
    UrlProtocol clonePrivate();
    UrlProtocol clonePublic();
    UrlQueryState getState();
}
