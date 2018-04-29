package org.schabi.newpipe.settings.model.settings.interfaces;

import org.schabi.newpipe.settings.exceptions.UnsupportedSettingValueException;
import org.schabi.newpipe.settings.exceptions.WrongSettingsDataException;

public interface WriteableSettings extends Settings {
    void set(String id, Object settingValue) throws UnsupportedSettingValueException, WrongSettingsDataException;
    void setString(String id, String settingValue) throws WrongSettingsDataException;
    void setNumber(String id, Number settingValue) throws WrongSettingsDataException;
    void setBoolean(String id, Boolean settingValue) throws WrongSettingsDataException;
}
