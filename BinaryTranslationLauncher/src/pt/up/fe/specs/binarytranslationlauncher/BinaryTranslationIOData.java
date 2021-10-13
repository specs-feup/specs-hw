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

import java.io.File;

import org.suikasoft.jOptions.DataStore.ADataClass;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

public class BinaryTranslationIOData extends ADataClass<BinaryTranslationIOData> {

    public static final DataKey<File> ELFFILE = KeyFactory.file("ELFFile").setLabel("ELF file");
    public static final DataKey<File> OUTPUTFOLDER = KeyFactory.file("OutputFolder").setLabel("Output Folder");
    public static final DataKey<Boolean> CLEANOUTPUT = KeyFactory.bool("CleanOutputFolder");
}
