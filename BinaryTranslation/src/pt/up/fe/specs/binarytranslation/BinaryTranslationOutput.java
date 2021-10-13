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
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.SpecsIo;

/**
 * Defaults methods for outputting results from binary translation to JSON. E.g., SegmentBundle and GraphBundle may
 * inherit this
 * 
 * @author nuno
 *
 */
public interface BinaryTranslationOutput {

    public default String getOutputFolderName() {
        // return this.getClass().getSimpleName() + this.hashCode();
        var dateFormat = new SimpleDateFormat("ddMMyyyy");
        var date = Calendar.getInstance().getTime();
        return this.getClass().getSimpleName() + "_" + this.hashCode() + "_" + dateFormat.format(date);
    }

    public default File getOutputFolder() {
        return SpecsIo.mkdir("./output/" + this.getOutputFolderName());
    }

    public default String getJSONName() {
        return this.getClass().getSimpleName() + this.hashCode() + ".json";
    }

    public default void toJSON() {
        this.toJSON(this.getOutputFolder());
    }

    public default void toJSON(File f) {
        SpecsIo.mkdir(f);
        var bytes = this.getJSONBytes();
        BinaryTranslationUtils.bytesToFile(new File(f, getJSONName()), bytes);
    }

    public default byte[] getJSONBytes() {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this).getBytes();
    }

    /*
     * 
     */
    public default void generateOutput() {
        generateOutput(this.getOutputFolder());
    }

    /*
     * Generate a folder and output
     */
    public default void generateOutput(File parentfolder) {
    }
}
