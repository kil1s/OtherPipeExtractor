package org.schabi.newpipe.extractor.url.model;

public class UrlRawQuery<T> {
    private T value;
    private UrlQueryState state;

    public UrlRawQuery(T value) {
        this(value, UrlQueryState.OTHER);
    }

    public UrlRawQuery(T value, UrlQueryState state) {
        this.value = value;
        this.state = state;
    }

    public T getValue() {
        return value;
    }

    public UrlQueryState getState() {
        return state;
    }
}
