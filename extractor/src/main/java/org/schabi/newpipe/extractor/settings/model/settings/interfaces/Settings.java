package org.schabi.newpipe.extractor.settings.model.settings.interfaces;

import org.schabi.newpipe.extractor.settings.model.provider.SettingProvider;

import java.util.Map;

public interface Settings {
    Map<String, SettingProvider> getSettingProviders();
    boolean has(String id);
    Object get(String id);
    String getString(String id);
    Number getNumber(String id);
    Boolean getBoolean(String id);
}
