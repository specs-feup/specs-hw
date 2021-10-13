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
 
package pt.up.fe.specs.binarytranslation;

import java.io.File;
import java.util.function.Function;

import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

public interface ZippedELFProvider extends ELFProvider {

    default public ELFProvider asTxtDump() {
        throw new NotImplementedException("asTxtDump()");
    }

    default public ELFProvider asTraceTxtDump() {
        throw new NotImplementedException("asTraceTxtDump()");
    }

    @Override
    default File getFile() {
        return this.write();
    }

    @Override
    default String read() {
        var file = write(SpecsIo.getTempFolder());
        return SpecsIo.read(file);
    }

    @Override
    default File write(File folder, boolean overwrite, Function<String, String> nameMapper) {

        var unzipFolder = SpecsIo.getTempFolder("unzip_" + SpecsIo.removeExtension(getResourceName()));

        // copy of zip on disk (outside jar/bin)
        var zipFile = SpecsIo.resourceCopy(this.getResource(), unzipFolder);
        BinaryTranslationUtils.unzip(zipFile, getELFName(), unzipFolder);

        var unzippedFile = new File(unzipFolder, getELFName());

        var finalFile = new File(folder, unzippedFile.getName());

        if (finalFile.isFile() && !overwrite) {
            return finalFile;
        }

        SpecsIo.copy(unzippedFile, finalFile);
        finalFile.deleteOnExit();
        return finalFile;
    }
}
