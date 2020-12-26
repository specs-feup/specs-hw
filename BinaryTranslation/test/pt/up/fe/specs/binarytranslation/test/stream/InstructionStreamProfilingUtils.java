package pt.up.fe.specs.binarytranslation.test.stream;

import java.io.File;
import java.lang.reflect.Constructor;

import pt.up.fe.specs.binarytranslation.profiling.InstructionStreamProfiler;
import pt.up.fe.specs.binarytranslation.profiling.data.InstructionProfileResult;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.SpecsIo;

public class InstructionStreamProfilingUtils {

    public static InstructionProfileResult profile(String filename, Class<?> streamClass, Class<?> profilerClass) {

        File fd = SpecsIo.resourceCopy(filename);
        fd.deleteOnExit();

        /*
         * Construct stream
         */
        Constructor<?> streamcons;
        try {
            streamcons = streamClass.getConstructor(File.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /*
         * Construct profiler
         */
        Constructor<?> profilecons;
        try {
            profilecons = profilerClass.getConstructor();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        InstructionProfileResult result = null;
        try (InstructionStream el = (InstructionStream) streamcons.newInstance(fd)) {
            var profiler = (InstructionStreamProfiler) profilecons.newInstance();
            result = profiler.profile(el);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
