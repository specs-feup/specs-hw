/**
 * Copyright 2021 SPeCS.
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

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.AppKernel;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitionBuilder;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;

public class BinaryTranslation implements AppKernel {

    @Override
    public int execute(DataStore options) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static StoreDefinition getDefinition() {
        var builder = new StoreDefinitionBuilder(BinaryTranslation.class);

        // IO options
        var opts0 = StoreDefinitions.fromInterface(BinaryTranslationIOData.class);
        builder.addNamedDefinition("Input Options", opts0);

        // stream options
        var opts1 = StoreDefinitions.fromInterface(BinaryTranslationStreamData.class);
        builder.addNamedDefinition("Instruction Stream Options", opts1);

        // detection options
        var opts2 = StoreDefinitions.fromInterface(BinaryTranslationDetectionData.class);
        builder.addNamedDefinition("Segment Detection Options", opts2);

        // graph options (?)...

        // hardware generation options (?)...

        return builder.build();
    }
}
