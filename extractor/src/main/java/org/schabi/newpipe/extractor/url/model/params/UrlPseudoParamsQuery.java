package org.schabi.newpipe.extractor.url.model.params;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class UrlPseudoParamsQuery implements UrlParamsQueryInterface, Map<String, List<String>> {
    protected UrlParamsQuery query;

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

    @Override
    public int size() {
        return query.size();
    }

    @Override
    public boolean isEmpty() {
        return query.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return query.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return query.containsValue(o);
    }

    @Override
    public List<String> get(Object o) {
        return query.get(o);
    }

    @Override
    public List<String> put(String s, List<String> strings) {
        return query.put(s, strings);
    }

    @Override
    public List<String> remove(Object o) {
        return query.remove(o);
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<String>> map) {
        query.putAll(map);
    }

    @Override
    public void clear() {
        query.clear();
    }

    @Override
    public Set<String> keySet() {
        return query.keySet();
    }

    @Override
    public Collection<List<String>> values() {
        return query.values();
    }

    @Override
    public Set<Entry<String, List<String>>> entrySet() {
        return query.entrySet();
    }
}
