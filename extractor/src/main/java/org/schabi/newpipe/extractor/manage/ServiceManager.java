package org.schabi.newpipe.extractor.manage;

import com.github.kil1s.other.settings.exceptions.SettingException;
import com.github.kil1s.other.settings.exceptions.UnsupportedSettingValueException;
import com.github.kil1s.other.settings.exceptions.WrongSettingsDataException;
import com.github.kil1s.other.settings.model.provider.SettingProvider;
import com.github.kil1s.other.settings.model.settings.interfaces.Settings;
import com.github.kil1s.other.settings.model.settings.interfaces.WriteableSettings;
import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParserException;

import javax.annotation.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import org.schabi.newpipe.model.Language;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.manage.typing.ServiceTyping;

public final class ServiceManager {
    private ServiceTyping[] typings;
    private ServiceInfoFactory[] factories;

    private int version;
    private Language language;
    private HashMap<Integer, StreamingServiceInfo> infos;
    private HashMap<Integer, StreamingService> services;

    private ServiceManager(
        int version,
        Language language,
        HashMap<Integer, StreamingServiceInfo> infos,
        HashMap<Integer, StreamingService> services,
        ServiceTyping...typings
    ) throws RuntimeException {
        this.version = version;
        this.language = language;

        this.infos = infos;
        this.services = services;
        this.typings = typings;

        ArrayList<ServiceInfoFactory> factories = new ArrayList<ServiceInfoFactory>();
        for (ServiceTyping typing : typings) {
            factories.add(new ServiceInfoFactory(typing));
        }
        this.factories = factories.toArray(new ServiceInfoFactory[0]);
    }

    public boolean writeable(int id, String name) {
        return !(services.get(id).getSettings() instanceof WriteableSettings);
    }

    public void flash(Language language) {
        this.language = language;
        for (Map.Entry<Integer, StreamingService> entry : services.entrySet()) {
            flash(entry.getKey(), language);
        }
    }

    public boolean flash(int id, Language language) {
        StreamingService service = services.get(id);
        if (!service.getLanguages().contains(language)) {
            return false;
        }
        service.setLanguage(language);
        return true;
    }

    public void flash(int id, Map<String, ?> settings)
            throws WrongSettingsDataException, UnsupportedSettingValueException
    {
        for (Map.Entry<String, ?> entry : settings.entrySet()){
            flash(id, entry.getKey(), entry.getValue());
        }
    }

    public boolean flash(int id, String name, Object value)
            throws WrongSettingsDataException, UnsupportedSettingValueException
    {
        if (writeable(id, name)) {
            return false;
        }
        ((WriteableSettings) services.get(id).getSettings()).set(name, value);
        return true;
    }

    public Collection<StreamingServiceInfo> infos() {
        return infos.values();
    }

    public Collection<StreamingService> all() {
        return services.values();
    }

    public synchronized int add(
        StreamingService service, Map<String, Object> initSettings
    ) throws UnsupportedSettingValueException, WrongSettingsDataException {
        if (initSettings != null) {
            Settings settings = service.getSettings();
            if (settings instanceof WriteableSettings) {
                WriteableSettings writeableSettings = (WriteableSettings) settings;
                for (Map.Entry<String, ?> settingEntry : initSettings.entrySet()) {
                    writeableSettings.set(settingEntry.getKey(), settingEntry.getValue());
                }
                service.updateSettings(writeableSettings);
            }
        }
        services.put(service.getServiceId(), service);
        return services.size() - 1;
    }

    public synchronized int add(StreamingService service) throws SettingException {
        return add(service, null);
    }

    public JsonObject json() throws JsonParserException {
        JsonObject rootObject = new JsonObject();
        rootObject.put("version", version);
        JsonArray services = new JsonArray();
        for (StreamingServiceInfo serviceInfo:export()) {
            services.add(serviceInfo.json());
        }
        rootObject.put("services", services);
        return rootObject;
    }

    private List<StreamingServiceInfo> export() {
        ArrayList<StreamingServiceInfo> dump = new ArrayList<StreamingServiceInfo>();
        for (StreamingService service : all()) {
            dump.add(new StreamingServiceInfo(
                    service.getServiceId(),
                    service.getSettings(),
                    service.getClass()
            ));
        }
        return dump;
    }

    @SuppressWarnings({"RedundantThrows", "DuplicateThrows"})
    public boolean load(JsonObject json)
            throws Exception,
                   IllegalAccessException,
                   InvocationTargetException,
                   InstantiationException
    {
        ArrayList<StreamingServiceInfo> infos = new ArrayList<StreamingServiceInfo>();
        JsonArray services = json.getArray("services");
        int version = json.getInt("version");

        json.remove("version");
        json.remove("services");

        for (Object service : services) {
            if (!(service instanceof JsonObject)) {
                continue;
            }

            for (ServiceInfoFactory factory : factories) {
                if (!factory.support((JsonObject) service)) {
                    continue;
                }
                infos.add(factory.service((JsonObject) service));
            }
        }
        return flush(infos, version, json);
    }

    @SuppressWarnings({"RedundantThrows", "DuplicateThrows"})
    private boolean flush(
        List<StreamingServiceInfo> set,
        int version,
        @Nullable Map<String, ?> specifications
    ) throws Exception,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException
    {
        boolean kills = specifications != null && specifications.containsKey("kills");

        for (StreamingServiceInfo info : set) {
            int id = info.getId();
            infos.put(id, info);

            for (ServiceTyping typing : typings) {
                if (!(kills || typing instanceof SettingProvider)) {
                    continue;
                }

                services.put(id, typing.create(
                        version, info,
                        id, specifications, this.language
                ));
                break;
            }
        }
        return true;
    }
}
