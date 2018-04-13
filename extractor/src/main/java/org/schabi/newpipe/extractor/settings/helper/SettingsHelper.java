/*
 *MIT License
 *
 *Copyright (c) 2018 Florian Steenbuck
 *
 *Permission is hereby granted, free of charge, to any person obtaining a copy
 *of this software and associated documentation files (the "Software"), to deal
 *in the Software without restriction, including without limitation the rights
 *to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *copies of the Software, and to permit persons to whom the Software is
 *furnished to do so, subject to the following conditions:
 *
 *The above copyright notice and this permission notice shall be included in all
 *copies or substantial portions of the Software.
 *
 *THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *SOFTWARE.
 */
package org.schabi.newpipe.extractor.settings.helper;
import org.schabi.newpipe.extractor.settings.typ.SettingsOutput;
import org.schabi.newpipe.extractor.settings.typ.SettingsTyp;
import sun.reflect.generics.tree.Tree;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SettingsHelper {
    private static boolean isUnsupportedSingle(Object instance) {
        return instance instanceof Collection || instance instanceof Map || instance instanceof Tree;
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
