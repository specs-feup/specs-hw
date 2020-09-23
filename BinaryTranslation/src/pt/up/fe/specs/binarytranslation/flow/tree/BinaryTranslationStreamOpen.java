package pt.up.fe.specs.binarytranslation.flow.tree;

import java.io.File;
import java.lang.reflect.Constructor;

import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.SpecsIo;

public class BinaryTranslationStreamOpen extends BinaryTranslationStep {

    private Class<?> streamClass;
    private String filename;
    private InstructionStream istream;

    public BinaryTranslationStreamOpen(String filename, Class<?> streamClass) {
        this.streamClass = streamClass;
        this.filename = filename;
    }

    @Override
    public void execute() {
        File fd = SpecsIo.resourceCopy(filename);
        fd.deleteOnExit();

        Constructor<?> cons;
        try {
            cons = streamClass.getConstructor(File.class);

        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }

        // open the stream
        try {
            this.istream = (InstructionStream) cons.newInstance(fd);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public InstructionStream getStream() {
        return this.istream;
    }
}
