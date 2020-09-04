package pt.up.fe.specs.binarytranslation;

import java.io.File;

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
        return this.getClass().getSimpleName() + this.hashCode();
    }

    public default File getOutputFolder() {
        return SpecsIo.mkdir("./" + this.getOutputFolderName());
    }

    public default String getJSONName() {
        return this.getClass().getSimpleName() + this.hashCode() + ".json";
    }

    public default void toJSON() {
        this.toJSON(this.getOutputFolder());
    }

    public default void toJSON(File f) {
        SpecsIo.mkdir(f);
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        var bytes = gson.toJson(this).getBytes();
        BinaryTranslationUtils.bytesToFile(new File(f, getJSONName()), bytes);
    }

    /*
     * 
     */
    public default void generateOutput() {
        generateOutput(null);
    }

    /*
     * Generate a folder and output
     */
    public default void generateOutput(String parentfolder) {
    }
}
