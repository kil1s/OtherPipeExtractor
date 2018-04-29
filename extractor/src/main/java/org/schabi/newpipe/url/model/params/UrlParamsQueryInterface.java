package org.schabi.newpipe.url.model.params;

import org.schabi.newpipe.url.model.UrlQuery;

import java.util.List;

public interface UrlParamsQueryInterface extends UrlQuery {
    boolean got(String key);
    boolean gotOption(String option);
    List<String> getOptions();
    List<String> get(String key);
    void remove(String key);
    void remove(String key, String value);
    void add(String key);
    void add(String key, String value);
}
