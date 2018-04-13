package org.schabi.newpipe.extractor.url.model.protocol;

public class UrlUnknownProtocol implements UrlProtocol {
    private String name;
    private String readableName;

    public UrlUnknownProtocol(String name, String readableName) {
        this.name = name;
        this.readableName = readableName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getReadableName() {
        return readableName;
    }
}
