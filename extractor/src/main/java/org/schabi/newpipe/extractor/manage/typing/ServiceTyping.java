package org.schabi.newpipe.extractor.manage.typing;

import com.github.kil1s.other.settings.model.provider.SettingProvider;
import com.github.kil1s.other.settings.model.settings.interfaces.Settings;

import org.schabi.newpipe.model.Language;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.manage.StreamingServiceInfo;

import java.util.Map;

import javax.annotation.Nullable;

public interface ServiceTyping {
    @Nullable
    SettingProvider provide(Object value, String clazz) throws Exception;

    StreamingService create(
            int version, StreamingServiceInfo info,
            int id, Settings settings, Language language
    ) throws Exception;

    StreamingService create(
            int version, StreamingServiceInfo info,
            int id, @Nullable Map<String, ?> setting, Language language
    ) throws Exception;

    StreamingService create(
            int version, StreamingServiceInfo info,
            Settings settings, Language language
    ) throws Exception;

    StreamingService create(
            int version, StreamingServiceInfo info,
            @Nullable Map<String, ?> setting, Language language
    ) throws Exception;
}
