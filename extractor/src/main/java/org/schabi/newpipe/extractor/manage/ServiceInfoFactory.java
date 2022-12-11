package org.schabi.newpipe.extractor.manage;

import com.github.FlorianSteenbuck.other.settings.exceptions.UnsupportedSettingValueException;
import com.github.FlorianSteenbuck.other.settings.exceptions.WrongSettingsDataException;
import com.github.FlorianSteenbuck.other.settings.model.provider.SettingProvider;
import com.github.FlorianSteenbuck.other.settings.model.settings.PreStoredSettings;
import com.github.FlorianSteenbuck.other.settings.model.settings.interfaces.WriteableSettings;
import com.grack.nanojson.JsonObject;

import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.manage.typing.ServiceTyping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class ServiceInfoFactory {
    private ServiceTyping typing;

    public ServiceInfoFactory(ServiceTyping typing) {
        this.typing = typing;
    }

    public Object value(JsonObject setting) throws ClassNotFoundException {
        Object value = setting.get("value");
        Class valueClass = Class.forName(setting.getString("class"));
        if (value instanceof JsonObject) {
            try {
                Method fromJson = valueClass.getMethod("fromJson", JsonObject.class);
                value = fromJson.invoke(null, value);
            } catch (ReflectiveOperationException ex) {
            }
        }
        return value;
    }

    public boolean support(JsonObject json) {
        return (!(typing instanceof SettingProvider)) || json.has("kills");
    }
    
    @SuppressWarnings("DuplicateThrows")
    public StreamingServiceInfo service(JsonObject json)
            throws Exception, ClassNotFoundException
    {
        int id = json.getInt("id");
        Class<? extends StreamingService> typ = null;
        try {
            //noinspection unchecked
            typ = (Class<? extends StreamingService>) Class.forName(json.getString("class"));
        } catch (ClassNotFoundException ex) {
            throw new CanNotFindStreamingServiceClass(ex.getMessage(), ex.getCause());
        }
        // Here starts the reflections monster grab a coffee and start reading
        JsonObject settingsRoot = json.getObject("settings");
        JsonObject providers = json.getObject("providers");
        Class<?> settingsClass = Class.forName(settingsRoot.getString("class"));

        boolean writeable = settingsClass == WriteableSettings.class;
        if (!writeable) {
            ArrayList<Class<?>> interfaces = new ArrayList<Class<?>>();
            for (Class<?> mInterface:settingsClass.getInterfaces()) {
                writeable = mInterface == WriteableSettings.class;
                if (writeable) {
                    break;
                }
            }
        }
        
        if (writeable) {
            StreamingServiceInfo service = service(id, typ, providers, settingsClass);
            if (service != null) {
                return service;
            }
        }

        return prestore(id, typ, providers);
    }

    @SuppressWarnings("DuplicateThrows")
    protected StreamingServiceInfo prestore(
        int id,
        Class<? extends StreamingService> typ,
        JsonObject providers
    ) throws Exception, ClassNotFoundException {
        Map<String, SettingProvider> settings = new HashMap<String, SettingProvider>();
        for (Map.Entry<String, Object> providerEntry: providers.entrySet()) {
            Object obj = providerEntry.getValue();
            if (!(obj instanceof JsonObject)) {
                continue;
            }
            JsonObject providerJson = (JsonObject) obj;

            Object value = value(providerJson.getObject("value"));
            String key = providerEntry.getKey();
            SettingProvider provider = typing.provide(value, providerJson.getString("class"));

            if (provider != null) {
                settings.put(key, provider);
            } else {
                throw new NullPointerException(
                        "Can not find matching constructor(" + value.getClass().getName() +
                                " value) for init SettingProvider for " + key
                );
            }
        }

        return new StreamingServiceInfo(id, new PreStoredSettings(settings), typ);
    }

    public @Nullable StreamingServiceInfo service(
            int id,
            Class<? extends StreamingService> typ,
            JsonObject providers, 
            Class<?> settingsClass
    ) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        WriteableSettings settings = (WriteableSettings) settingsClass.newInstance();
        for (Map.Entry<String, SettingProvider> settingProviderEntry : settings.getSettingProviders().entrySet()) {
            String key = settingProviderEntry.getKey();
            if (providers.containsKey(key)) {
                JsonObject provider = providers.getObject(key);
                Class providerClass = Class.forName(provider.getString("class"));
                if (providerClass != settingProviderEntry.getValue().getClass()) {
                    return null;
                }

                try {
                    settings.set(key, value(provider.getObject("value")));
                } catch (UnsupportedSettingValueException e) {
                    // TODO better error handling
                } catch (WrongSettingsDataException e) {
                    return null;
                }
            }
        }
        
        return new StreamingServiceInfo(id, settings, typ);
    }
}
