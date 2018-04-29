package org.schabi.newpipe.settings.model.settings;

import org.schabi.newpipe.settings.model.provider.SettingProvider;
import org.schabi.newpipe.settings.model.settings.interfaces.Settings;

import java.util.HashMap;
import java.util.Map;

public class EmptySettings implements Settings {
    @Override
    public Map<String, SettingProvider> getSettingProviders() {
        return new HashMap<String, SettingProvider>();
    }

    @Override
    public boolean has(String id) {
        return false;
    }

    @Override
    public Object get(String id) {
        return null;
    }

    @Override
    public String getString(String id) {
        return null;
    }

    @Override
    public Number getNumber(String id) {
        return null;
    }

    @Override
    public Boolean getBoolean(String id) {
        return null;
    }
}
