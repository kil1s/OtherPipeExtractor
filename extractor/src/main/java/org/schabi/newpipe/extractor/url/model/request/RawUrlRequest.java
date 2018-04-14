package org.schabi.newpipe.extractor.url.model.request;

public class RawUrlRequest {
    private String protocol;
    private String request;
    private String port;
    private String filepath;

    public RawUrlRequest(String protocol, String filepath, String port, String request) {
        this.protocol = protocol;
        this.request = request;
        this.port = port;
        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getRequest() {
        return request;
    }
}
