package org.schabi.newpipe.extractor.manage.typing;

import com.github.kil1s.other.settings.model.provider.SettingProvider;
import com.github.kil1s.other.settings.model.settings.interfaces.Settings;

import org.schabi.newpipe.model.Language;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.manage.StreamingServiceInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.annotation.Nullable;

public class NewPipeTyping implements ServiceTyping {
    private int id;

    public NewPipeTyping(int id) {
        this.id = id;
    }

    private synchronized int id() {
        id++;
        return id;
    }

    @Nullable
    @Override
    public SettingProvider provide(Object value, String clazz) {
        return null;
    }

    @Override
    public StreamingService create(
        int version, StreamingServiceInfo info,
        int id, Settings settings, Language language
    ) throws Exception {
        return create(version, info, id);
    }

    @Override
    public StreamingService create(
            int version, StreamingServiceInfo info,
            int id, @Nullable Map<String, ?> setting, Language language
    ) throws Exception {
        return create(version, info, id);
    }

    @Override
    public StreamingService create(
        int version, StreamingServiceInfo info,
        Settings settings, Language language
    ) throws Exception {
        return create(version, info, id());
    }

    @Override
    public StreamingService create(
        int version, StreamingServiceInfo info,
        @Nullable Map<String, ?> setting, Language language
    ) throws Exception {
        return create(version, info, id());
    }

    @SuppressWarnings({"RedundantThrows", "DuplicateThrows"})
    private StreamingService create(
        int version, StreamingServiceInfo info, int id
    ) throws Exception, InvocationTargetException, InstantiationException, IllegalAccessException {
        return (StreamingService) info.getTyp().getConstructors()[0].newInstance(id);
    }
}
