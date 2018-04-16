package org.schabi.newpipe.extractor.settings.model.provider;

import org.schabi.newpipe.extractor.settings.exceptions.WrongSettingsDataException;
import org.schabi.newpipe.extractor.settings.typ.SettingsTyp;

public class StringLineProvider extends SettingProvider<String> {
    public StringLineProvider(String defaultData) throws WrongSettingsDataException {
        super(defaultData, SettingsTyp.TEXT_LINE);
    }
}
