package org.schabi.newpipe.extractor.settings.exceptions;

public class UnsupportedSettingValueException extends SettingException {
    public UnsupportedSettingValueException(String message) {
        super(message);
    }

    public UnsupportedSettingValueException(Throwable cause) {
        super(cause);
    }

    public UnsupportedSettingValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
