package org.schabi.newpipe.settings.model.provider;

import org.schabi.newpipe.settings.exceptions.WrongSettingsDataException;
import org.schabi.newpipe.settings.typ.SettingsTyp;

import java.util.List;

public class StringListProvider extends SettingProvider<List<String>> {
    public StringListProvider(List<String> defaultData) throws WrongSettingsDataException {
        super(defaultData, SettingsTyp.LIST);
    }
}
