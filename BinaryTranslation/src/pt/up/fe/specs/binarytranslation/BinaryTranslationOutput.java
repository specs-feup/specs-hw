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
