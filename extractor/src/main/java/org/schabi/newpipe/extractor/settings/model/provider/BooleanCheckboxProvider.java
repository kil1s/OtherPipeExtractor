package org.schabi.newpipe.extractor.settings.model.provider;

import org.schabi.newpipe.extractor.settings.exceptions.WrongSettingsDataException;

import static org.schabi.newpipe.extractor.settings.typ.SettingsTyp.CHECKBOX;

public class BooleanCheckboxProvider extends SettingProvider<Boolean> {
    public BooleanCheckboxProvider(Boolean defaultData) throws WrongSettingsDataException {
        super(defaultData, CHECKBOX);
    }
}
