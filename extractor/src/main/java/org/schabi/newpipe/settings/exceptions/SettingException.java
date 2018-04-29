package org.schabi.newpipe.settings.exceptions;

public class SettingException extends Exception {
    public SettingException(String message) {
        super(message);
    }

    public SettingException(Throwable cause) {
        super(cause);
    }

    public SettingException(String message, Throwable cause) {
        super(message, cause);
    }
}
