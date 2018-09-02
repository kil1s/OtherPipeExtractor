package org.schabi.newpipe.extractor.graphql.fragment;

import java.util.HashMap;

public class GraphQLFragment extends HashMap<String, GraphQLFragmentValue> {
    protected String on = null;

    public GraphQLFragment(String on) {
        this.on = on;
    }
}
