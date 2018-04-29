package org.schabi.newpipe.settings.model.provider;

import org.schabi.newpipe.settings.exceptions.WrongSettingsDataException;

import static org.schabi.newpipe.settings.typ.SettingsTyp.CHECKBOX;

public class BooleanCheckboxProvider extends SettingProvider<Boolean> {
    public BooleanCheckboxProvider(Boolean defaultData) throws WrongSettingsDataException {
        super(defaultData, CHECKBOX);
    }
}
