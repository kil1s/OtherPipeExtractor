package org.schabi.newpipe.settings.model.provider;

import org.schabi.newpipe.settings.exceptions.WrongSettingsDataException;

import static org.schabi.newpipe.settings.typ.SettingsTyp.INPUT_NUMBER;

public class NumberInputProvider extends SettingProvider<Number> {
    public NumberInputProvider(Number defaultData) throws WrongSettingsDataException {
        super(defaultData, INPUT_NUMBER);
    }
}
