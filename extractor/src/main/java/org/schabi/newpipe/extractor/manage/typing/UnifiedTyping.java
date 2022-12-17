package org.schabi.newpipe.extractor.manage.typing;

import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.manage.StreamingServiceInfo;
import org.schabi.newpipe.model.Language;

import javax.annotation.Nullable;

public abstract class UnifiedTyping implements ServiceTyping {
    abstract protected StreamingService create(
            int version, StreamingServiceInfo info,
            int id, Language language, @Nullable Object settings
    ) throws Exception;
}
