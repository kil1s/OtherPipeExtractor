package org.schabi.newpipe.extractor.settings.model.settings.interfaces;

import org.schabi.newpipe.extractor.settings.exceptions.UnsupportedSettingValueException;
import org.schabi.newpipe.extractor.settings.exceptions.WrongSettingsDataException;

public interface WriteableSettings extends Settings {
    void set(String id, Object settingValue) throws UnsupportedSettingValueException, WrongSettingsDataException;
    void setString(String id, String settingValue) throws WrongSettingsDataException;
    void setNumber(String id, Number settingValue) throws WrongSettingsDataException;
    void setBoolean(String id, Boolean settingValue) throws WrongSettingsDataException;
}
