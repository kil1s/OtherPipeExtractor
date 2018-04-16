package org.schabi.newpipe.extractor.settings.model.settings.interfaces;

import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;

import java.io.IOException;

public interface DynamicSettings extends Settings {
    void refresh() throws IOException, ReCaptchaException;
}
