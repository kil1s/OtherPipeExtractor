package org.schabi.newpipe.extractor.manage.typing;

import com.github.kil1s.other.settings.model.provider.SettingProvider;
import com.github.kil1s.other.settings.model.settings.interfaces.Settings;

import org.schabi.newpipe.model.Language;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.manage.StreamingServiceInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.annotation.Nullable;

public final class SettingsTyping extends UnifiedTyping {
    private int id;

    public SettingsTyping(int id) {
        this.id = id;
    }

    private synchronized int id() {
        id++;
        return id;
    }

    @Nullable
    @Override
    public SettingProvider provide(Object value, String clazz) throws ClassNotFoundException {
        Class<? extends SettingProvider> providerClass =
                (Class<? extends SettingProvider>) Class.forName(clazz);

        for (Constructor providerConstructor : providerClass.getConstructors()) {
            Class[] providerConstructParameters = providerConstructor.getParameterTypes();
            if (
                    providerConstructParameters.length == 1 &&
                            providerConstructParameters[0] == value.getClass()
            ) {
                try {
                    return (SettingProvider) providerConstructor.newInstance(value);
                } catch (Exception ex) {
                    // TODO better error handling
                }
            }
        }
        return null;
    }

    @SuppressWarnings({"RedundantThrows", "DuplicateThrows"})
    @Override
    public StreamingService create(
        int version, StreamingServiceInfo info,
        int id, Settings settings, Language language
    ) throws Exception, InvocationTargetException, InstantiationException, IllegalAccessException {
        return create(version, info, id, language, settings);
    }

    @SuppressWarnings({"RedundantThrows", "DuplicateThrows"})
    @Override
    public StreamingService create(
        int version, StreamingServiceInfo info,
        int id, @Nullable Map<String, ?> setting, Language language
    ) throws Exception, InvocationTargetException, InstantiationException, IllegalAccessException {
        return create(version, info, id, language, setting);
    }

    @SuppressWarnings({"RedundantThrows", "DuplicateThrows"})
    @Override
    public StreamingService create(
        int version, StreamingServiceInfo info,
        Settings settings, Language language
    ) throws Exception, InvocationTargetException, InstantiationException, IllegalAccessException {
        return create(version, info, id(), language, settings);
    }

    @SuppressWarnings({"RedundantThrows", "DuplicateThrows"})
    @Override
    public StreamingService create(
        int version, StreamingServiceInfo info,
        @Nullable Map<String, ?> setting, Language language
    ) throws Exception, InvocationTargetException, InstantiationException, IllegalAccessException {
        return create(version, info, id(), language, setting);
    }

    @SuppressWarnings({"RedundantThrows", "DuplicateThrows"})
    protected StreamingService create(
        int version, StreamingServiceInfo info,
        int id, Language language, @Nullable Object settings
    ) throws Exception, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<? extends StreamingService> serviceTyp = info.getTyp();
        for (Constructor serviceConstructor : serviceTyp.getConstructors()) {
            Class[] types = serviceConstructor.getParameterTypes();
            switch (types.length) {
                case 3:
                case 2:
                    boolean configured = settings == null;
                    if (types.length == 3) {
                        if (configured) {
                            break;
                        }
                        return (StreamingService) serviceConstructor.newInstance(id, settings, language);
                    }

                    if (serviceConstructor.getParameterTypes()[1].equals(Language.class)) {
                        return (StreamingService) serviceConstructor.newInstance(id, language);
                    }

                    if (configured) {
                        break;
                    }
                    return (StreamingService) serviceConstructor.newInstance(id, settings);
                case 1:
                    return (StreamingService) serviceConstructor.newInstance(id);
                case 0:
                    return (StreamingService) serviceConstructor.newInstance();
            }
        }
        throw new Exception("Can not find matching constructor");
    }
}
