package org.schabi.newpipe.extractor.graphql.navigator;

import java.util.List;
import java.util.Map;

public class GraphQLNavigateable {
    protected Map<String, GraphQLNavigateable> rootQuerys;
    protected List<String> availAt;
    protected GraphQLParameters parameters;

    public GraphQLNavigateable(GraphQLParameters parameters, Map<String, GraphQLNavigateable> rootQuerys, List<String> availAt) {
        this.parameters = parameters;
        this.rootQuerys = rootQuerys;
        this.availAt = availAt;
    }

    public GraphQLParameters getParameters() {
        return parameters;
    }

    public Map<String, GraphQLNavigateable> getRootQuerys() {
        return rootQuerys;
    }

    public List<String> getAvailAt() {
        return availAt;
    }
}
