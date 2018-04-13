package org.schabi.newpipe.extractor.url.model;

public class UrlRawQuery<T> {
    public enum UrlRawQueryState {
        PRIVATE,
        PUBLIC,
        OTHER
    }
    private T value;
    private UrlRawQueryState state;

    public UrlRawQuery(T value) {
        this(value, UrlRawQueryState.OTHER);
    }

    public UrlRawQuery(T value, UrlRawQueryState state) {
        this.value = value;
        this.state = state;
    }

    public T getValue() {
        return value;
    }

    public UrlRawQueryState getState() {
        return state;
    }
}
