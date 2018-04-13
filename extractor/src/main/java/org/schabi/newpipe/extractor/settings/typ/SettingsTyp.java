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
package org.schabi.newpipe.extractor.settings.typ;

public enum SettingsTyp {
    TEXT_LINE(new SettingsOutput(String.class)),
    TEXT_AREA(new SettingsOutput(String.class)),
    INPUT_NUMBER(new SettingsOutput(Number.class)),
    INPUT_INT(new SettingsOutput(Integer.class)),
    INPUT_DOUBLE(new SettingsOutput(Double.class)),
    SELECT(new SettingsOutput(String.class), new SettingsOutput(Integer.class)),
    CHECKBOXES(new SettingsOutput(String.class, SettingsOutput.Typ.LIST), new SettingsOutput(Integer.class, SettingsOutput.Typ.LIST)),
    LIST(new SettingsOutput(String.class, SettingsOutput.Typ.LIST)),
    CHECKBOX(new SettingsOutput(Boolean.class));

    SettingsOutput[] validOutputs = new SettingsOutput[0];

    SettingsTyp(SettingsOutput...validOutputs) {
        if (validOutputs == null || validOutputs.length <= 0) {
            return;
        }
        this.validOutputs = validOutputs;
    }

    public SettingsOutput[] getValidOutputs() {
        return validOutputs;
    }
}
