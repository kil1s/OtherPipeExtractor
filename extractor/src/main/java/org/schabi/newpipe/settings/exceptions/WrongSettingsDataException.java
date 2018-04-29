package org.schabi.newpipe.settings.exceptions;

public class WrongSettingsDataException extends SettingException {
    public WrongSettingsDataException(String message) {
        super(message);
    }

    public WrongSettingsDataException(Throwable cause) {
        super(cause);
    }

    public WrongSettingsDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
