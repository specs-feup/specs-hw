/**
 * Copyright 2019 SPeCS.
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

package pt.up.fe.specs.binarytranslation.asmparser;

import java.util.HashMap;
import java.util.Map;

import org.suikasoft.jOptions.DataStore.ADataClass;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

public class AsmInstructionData extends ADataClass<AsmInstructionData> {

    public static final DataKey<AsmInstructionType> TYPE = KeyFactory.object("type", AsmInstructionType.class);

    public static final DataKey<Map<String, String>> FIELDS = KeyFactory.generic("fields",
            (Map<String, String>) new HashMap<String, String>());

    public static AsmInstructionData newInstance(AsmInstructionType type, Map<String, String> fields) {
        var data = new AsmInstructionData();

        data.set(TYPE, type);
        data.set(FIELDS, fields);

        return data;
    }
}
