package org.schabi.newpipe.extractor.url.model.params;

import java.util.HashMap;
import java.util.List;

public abstract class UrlPseudoParamsQuery extends HashMap<String, String> implements UrlParamsQueryInterface {
    private UrlParamsQuery query;

    public UrlPseudoParamsQuery(UrlParamsQuery query) {
        this.query = query;
    }

    @Override
    public boolean got(String key) {
        return query.got(key);
    }

    @Override
    public boolean gotOption(String option) {
        return query.gotOption(option);
    }

    @Override
    public List<String> getOptions() {
        return query.getOptions();
    }

    @Override
    public List<String> get(String key) {
        return query.get(key);
    }

    @Override
    public void remove(String entry) {
        query.remove(entry);
    }

    @Override
    public void remove(String key, String value) {
        query.remove(key, value);
    }

    @Override
    public void add(String entry) {
        query.remove(entry);
    }

    @Override
    public void add(String key, String value) {
        query.add(key, value);
    }
}
