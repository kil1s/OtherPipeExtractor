/*
 *MIT License
 *
 *Copyright (c) 2018 Florian Steenbuck
 *
 *Permission is hereby granted, free of charge, to any person obtaining a copy
 *of this software and associated documentation files (the "Software"), to deal
 *in the Software without restriction, including without limitation the rights
 *to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *copies of the Software, and to permit persons to whom the Software is
 *furnished to do so, subject to the following conditions:
 *
 *The above copyright notice and this permission notice shall be included in all
 *copies or substantial portions of the Software.
 *
 *THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *SOFTWARE.
 */
package org.schabi.newpipe.extractor.settings.model.settings.abstracts;

import org.schabi.newpipe.extractor.settings.exceptions.WrongSettingsDataException;
import org.schabi.newpipe.extractor.settings.model.provider.BooleanCheckboxProvider;
import org.schabi.newpipe.extractor.settings.model.provider.NumberInputProvider;
import org.schabi.newpipe.extractor.settings.model.provider.SettingProvider;
import org.schabi.newpipe.extractor.settings.model.provider.StringLineProvider;

import java.util.HashMap;
import java.util.Map;

public abstract class OverrideDynamicSettings extends PreSupportDynamicWriteableSettings {
    protected Map<String, SettingProvider> liveProviders = new HashMap<String, SettingProvider>();
    protected Map<String, SettingProvider> overrideProviders = new HashMap<String, SettingProvider>();

    protected boolean needUpdateCache = true;
    protected Map<String, SettingProvider> cachedProviders = new HashMap<String, SettingProvider>();

    @Override
    public Map<String, SettingProvider> getSettingProviders() {
        if (needUpdateCache) {
            cachedProviders = liveProviders;
            for (Map.Entry<String, SettingProvider> provider : overrideProviders.entrySet()) {
                cachedProviders.put(provider.getKey(), provider.getValue());
            }
            needUpdateCache = false;
        }
        return cachedProviders;
    }

    @Override
    public void setString(String id, String settingValue) throws WrongSettingsDataException {
        SettingProvider settingProvider = new StringLineProvider(settingValue);
        if (has(id)) {
            settingProvider = getSettingProviders().get(id);
            settingProvider.setData(settingValue);
        }
        overrideProviders.put(id, settingProvider);
        needUpdateCache = true;
    }

    @Override
    public void setNumber(String id, Number settingValue) throws WrongSettingsDataException {
        SettingProvider settingProvider = new NumberInputProvider(settingValue);
        if (has(id)) {
            settingProvider = getSettingProviders().get(id);
            settingProvider.setData(settingValue);
        }
        overrideProviders.put(id, settingProvider);
        needUpdateCache = true;
    }

    @Override
    public void setBoolean(String id, Boolean settingValue) throws WrongSettingsDataException {
        SettingProvider settingProvider = new BooleanCheckboxProvider(settingValue);
        if (has(id)) {
            settingProvider = getSettingProviders().get(id);
            settingProvider.setData(settingValue);
        }
        overrideProviders.put(id, settingProvider);
        needUpdateCache = true;
    }

    @Override
    public boolean has(String id) {
        return getSettingProviders().containsKey(id);
    }

    @Override
    public Object get(String id) {
        return getSettingProviders().get(id).getData();
    }

    @Override
    public String getString(String id) {
        return (String) getSettingProviders().get(id).getData();
    }

    @Override
    public Number getNumber(String id) {
        return (Number) getSettingProviders().get(id).getData();
    }

    @Override
    public Boolean getBoolean(String id) {
        return (Boolean) getSettingProviders().get(id).getData();
    }
}
