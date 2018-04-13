package org.schabi.newpipe.extractor.url.model.protocol;

public class UrlHttpsProtocol extends UrlTLSProtocol {
    @Override
    public String getName() {
        return "HTTPS";
    }

    @Override
    public String getReadableName() {
        return "Hypertext Transfer Protocol Secure";
    }
}
