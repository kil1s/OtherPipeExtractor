package org.schabi.newpipe.extractor.graphql.fragment;

import javax.annotation.Nullable;

public class GraphQLFragmentValue {
    protected GraphQLFragmentValueTyp typ = GraphQLFragmentValueTyp.VALUE;
    protected GraphQLFragment fragment = null;
    protected String key;

    public GraphQLFragmentValue(String key, GraphQLFragment fragment) {
        this.key = key;
        this.fragment = fragment;
        typ = GraphQLFragmentValueTyp.FRAGMENT;
    }

    public GraphQLFragmentValue(String key) {
        this.key = key;
    }

    public GraphQLFragmentValueTyp getTyp() {
        return typ;
    }

    public @Nullable GraphQLFragment getFragment() {
        return fragment;
    }
}
