package org.schabi.newpipe.settings.model.provider;

import org.schabi.newpipe.settings.exceptions.WrongSettingsDataException;
import org.schabi.newpipe.settings.typ.SettingsTyp;

public class StringLineProvider extends SettingProvider<String> {
    public StringLineProvider(String defaultData) throws WrongSettingsDataException {
        super(defaultData, SettingsTyp.TEXT_LINE);
    }
}
