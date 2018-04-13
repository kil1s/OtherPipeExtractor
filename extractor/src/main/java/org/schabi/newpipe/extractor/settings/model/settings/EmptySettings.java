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
package org.schabi.newpipe.extractor.settings.model.settings;

import org.schabi.newpipe.extractor.settings.model.provider.SettingProvider;
import org.schabi.newpipe.extractor.settings.model.settings.interfaces.Settings;

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
