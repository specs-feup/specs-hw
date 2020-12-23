package pt.up.fe.specs.binarytranslation.test.stream;

import java.io.File;
import java.lang.reflect.Constructor;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.SpecsIo;

public class InstructionStreamTestUtils {

    private interface WorkloadInterface {
        void apply(InstructionStream el);
    }

    private static void workload(String filename, Class<?> streamClass, WorkloadInterface doWork) {

        File fd = SpecsIo.resourceCopy(filename);
        fd.deleteOnExit();

        Constructor<?> cons;
        try {
            cons = streamClass.getConstructor(File.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try (InstructionStream el = (InstructionStream) cons.newInstance(fd)) {
            doWork.apply(el);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void printStreamWorkload(InstructionStream el) {
        Instruction inst = null;
        while ((inst = el.nextInstruction()) != null) {
            inst.printInstruction();
        }
    }

    public static void printStream(String filename, Class<?> streamClass) {
        workload(filename, streamClass, InstructionStreamTestUtils::printStreamWorkload);
    }

    private static void rawDumpWorkload(InstructionStream el) {
        // el.rawDump();
    }

    public static void rawDump(String filename, Class<?> streamClass) {
        workload(filename, streamClass, InstructionStreamTestUtils::rawDumpWorkload);
    }
}
