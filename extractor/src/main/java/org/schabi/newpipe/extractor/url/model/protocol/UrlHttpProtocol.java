package org.schabi.newpipe.extractor.url.model.protocol;

public class UrlHttpProtocol extends UrlTCPProtocol {
    @Override
    public String getName() {
        return "HTTP";
    }

    @Override
    public String getReadableName() {
        return "Hypertext Transfer Protocol";
    }
}
