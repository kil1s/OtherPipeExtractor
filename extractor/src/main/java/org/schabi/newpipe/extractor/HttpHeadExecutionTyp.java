package org.schabi.newpipe.extractor;

public enum HttpHeadExecutionTyp {
    RANGE,
    METHOD,
    FULL_BODY;

    public static HttpHeadExecutionTyp[] defaults() {
        return new HttpHeadExecutionTyp[] {RANGE, METHOD};
    }
}
