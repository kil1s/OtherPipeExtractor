package org.schabi.newpipe.extractor.manage;

import com.github.kil1s.other.settings.model.provider.SettingProvider;
import com.github.kil1s.other.settings.model.settings.interfaces.Settings;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;

import org.schabi.newpipe.extractor.StreamingService;

import java.lang.reflect.Method;
import java.util.Map;

public class StreamingServiceInfo {
    private int id;
    private Settings settings;
    private Class<? extends StreamingService> typ;

    public StreamingServiceInfo(int id, Settings settings, Class<? extends StreamingService> typ) {
        this.id = id;
        this.settings = settings;
        this.typ = typ;
    }

    public int getId() {
        return id;
    }

    public Settings getSettings() {
        return settings;
    }

    public Class<? extends StreamingService> getTyp() {
        return typ;
    }

    public JsonObject json() throws JsonParserException {
        JsonObject rootObject = new JsonObject();
        rootObject.put("id", id);
        rootObject.put("class", typ.getName());

        JsonObject settingsRoot = new JsonObject();
        settingsRoot.put("class", settings.getClass().getName());
        JsonObject settingsProvider = new JsonObject();
        for (Map.Entry<String, SettingProvider> setting:settings.getSettingProviders().entrySet()) {
            String key = setting.getKey();
            Object value = setting.getValue();
            Class valueClass = value.getClass();
            Class typ = setting.getClass();

            if (!(value instanceof JsonObject)) {
                try {
                    Method jsonConvertMethod = value.getClass().getMethod("json");
                    value = jsonConvertMethod.invoke(value);
                    if (value instanceof String) {
                        value = JsonParser.any().from((String) value);
                    }
                } catch (ReflectiveOperationException ex) {
                }
            }
            JsonObject jsonSettingValue = new JsonObject();
            jsonSettingValue.put("value", value);
            jsonSettingValue.put("class", valueClass.getName());

            JsonObject jsonSettingProvider = new JsonObject();
            jsonSettingProvider.put("value", jsonSettingValue);
            jsonSettingProvider.put("class", typ);
            settingsProvider.put(key, jsonSettingProvider);
        }
        settingsRoot.put("providers", settingsProvider);
        rootObject.put("settings", settingsRoot);
        return rootObject;
    }
}
