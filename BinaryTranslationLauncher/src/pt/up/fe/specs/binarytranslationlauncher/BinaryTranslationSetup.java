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
import pt.up.fe.specs.guihelper.Base.SetupDefinition;
import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.guihelper.SetupFieldOptions.DefaultValue;
import pt.up.fe.specs.guihelper.SetupFieldOptions.SingleSetup;

public enum BinaryTranslationSetup implements SetupFieldEnum, SingleSetup, DefaultValue {

    // I/O
    IOSetup(FieldType.integratedSetup),

    // type
    StreamOptions(FieldType.integratedSetup),

    // detectors
    DetectorOptions(FieldType.integratedSetup);

    private final FieldType fieldType;

    private BinaryTranslationSetup(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public FieldType getType() {
        return fieldType;
    }

    @Override
    public String getSetupName() {
        return "Binary Translation Framework Setup";
    }

    @Override
    public SetupDefinition getSetupOptions() {
        if (this == IOSetup)
            return SetupDefinition.create(BinaryTranslationIOSetup.class);

        else if (this == StreamOptions)
            return SetupDefinition.create(BinaryTranslationStreamSetup.class);

        else if (this == DetectorOptions)
            return SetupDefinition.create(BinaryTranslationDetectionSetup.class);

        else
            return null;
    }
}
