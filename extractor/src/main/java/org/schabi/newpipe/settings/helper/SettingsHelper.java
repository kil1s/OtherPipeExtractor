package org.schabi.newpipe.settings.helper;
import org.schabi.newpipe.settings.typ.SettingsOutput;
import org.schabi.newpipe.settings.typ.SettingsTyp;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SettingsHelper {
    private static boolean isUnsupportedSingle(Object instance) {
        return instance instanceof Collection || instance instanceof Map;
    }

    public static boolean instanceOfSettingsTyp(List instance, SettingsTyp typ) {
        for (SettingsOutput validOutput:typ.getValidOutputs()) {
            if (validOutput.getTyp() != SettingsOutput.Typ.LIST) {
                continue;
            }

            for (Object entry:instance) {
                if (validOutput.getClazz().isInstance(entry)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean instanceOfSettingsTyp(Object instance, SettingsTyp typ) {
        if (instance instanceof List) {
            return instanceOfSettingsTyp((List) instance, typ);
        }

        if (isUnsupportedSingle(instance)) {
            return false;
        }

        for (SettingsOutput validOutput:typ.getValidOutputs()) {
            if (validOutput.getTyp() == SettingsOutput.Typ.LIST) {
                continue;
            }

            if (validOutput.getClazz().isInstance(instance)) {
                return true;
            }
        }

        return false;
    }
}
