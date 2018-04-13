package org.schabi.newpipe.extractor.url.model.request;

import javax.annotation.Nullable;

public class RawUrlRequest {
    private String protocol;
    private String request;
    private String port;

    public RawUrlRequest(String protocol, String request, String port) {
        this.protocol = protocol;
        this.request = request;
        this.port = port;
    }

    @Nullable
    public String getPort() {
        return port;
    }

    @Nullable
    public String getProtocol() {
        return protocol;
    }

    @Nullable
    public String getRequest() {
        return request;
    }
}
