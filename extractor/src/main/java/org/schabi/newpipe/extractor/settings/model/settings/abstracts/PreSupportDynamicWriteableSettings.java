package org.schabi.newpipe.extractor.settings.model.settings.abstracts;

import org.schabi.newpipe.extractor.settings.exceptions.UnsupportedSettingValueException;
import org.schabi.newpipe.extractor.settings.exceptions.WrongSettingsDataException;
import org.schabi.newpipe.extractor.settings.model.settings.interfaces.DynamicWriteableSettings;

public abstract class PreSupportDynamicWriteableSettings implements DynamicWriteableSettings {
    @Override
    public void set(String id, Object settingValue) throws UnsupportedSettingValueException, WrongSettingsDataException {
        if (settingValue instanceof String) {
            setString(id, (String) settingValue);
        } else if (settingValue instanceof Number) {
            setNumber(id, (Number) settingValue);
        } else if (settingValue instanceof Boolean) {
            setBoolean(id, (Boolean) settingValue);
        } else {
            throw new UnsupportedSettingValueException(settingValue.getClass().getName()+" is unsupported");
        }
    }
}
