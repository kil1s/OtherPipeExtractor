package org.schabi.newpipe.extractor.url.model.protocol;

public class UrlTLSProtocol extends UrlTCPProtocol {
    @Override
    public String getName() {
        return "TLS";
    }

    @Override
    public String getReadableName() {
        return "Transport Layer Security";
    }
}
