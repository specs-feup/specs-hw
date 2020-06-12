/**
 * Copyright 2020 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.binarytranslationlauncher;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.Base.*;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.SetupFieldOptions.*;
import pt.up.fe.specs.guihelper.Setups.SimpleIo.SimpleIoSetup;

public enum BinaryTranslationSetup implements SetupFieldEnum, SingleSetup, DefaultValue {

    GeneralIo(FieldType.integratedSetup),
    CleanOutputFolder(FieldType.bool);

    private final String name;
    private final FieldType fieldType;

    private BinaryTranslationSetup(FieldType fieldType) {
        this.fieldType = fieldType;
        this.name = name();
    }

    @Override
    public FieldType getType() {
        return fieldType;
    }

    @Override
    public String getSetupName() {
        return "BinaryTranslationFrameworkSetup";
    }

    @Override
    public FieldValue getDefaultValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SetupDefinition getSetupOptions() {
        if (this == GeneralIo) {
            return SetupDefinition.create(SimpleIoSetup.class);
        }
        return null;
    }
}
