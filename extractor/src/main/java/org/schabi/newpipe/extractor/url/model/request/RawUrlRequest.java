package org.schabi.newpipe.extractor.url.model.request;

public class RawUrlRequest {
    private String protocol;
    private String filepath;
    private String port;
    private String domain;

    public RawUrlRequest(String protocol, String domain, String port, String filepath) {
        this.protocol = protocol;
        this.filepath = filepath;
        this.port = port;
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public String getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getFilepath() {
        return filepath;
    }
}
