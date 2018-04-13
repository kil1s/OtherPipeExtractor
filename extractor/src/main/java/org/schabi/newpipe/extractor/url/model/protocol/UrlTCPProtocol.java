package org.schabi.newpipe.extractor.url.model.protocol;

public class UrlTCPProtocol implements UrlProtocol {
    @Override
    public String getName() {
        return "TCP";
    }

    @Override
    public String getReadableName() {
        return "Transmission Control Protocol";
    }
}
