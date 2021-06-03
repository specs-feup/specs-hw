package pt.up.fe.specs.binarytranslation.test.stream;

import java.io.File;
import java.lang.reflect.Constructor;

import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class InstructionStreamTestUtils {

    private interface WorkloadInterface {
        void apply(InstructionStream el);
    }

    private static void workload(ELFProvider elf, Class<?> streamClass, WorkloadInterface doWork) {

        var fd = BinaryTranslationUtils.getFile(elf);

        Constructor<?> cons;
        try {
            cons = streamClass.getConstructor(File.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try (InstructionStream el = (InstructionStream) cons.newInstance(fd)) {

            el.advanceTo(elf.getKernelStart().longValue());
            doWork.apply(el);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printStreamWorkload(InstructionStream el) {
        Instruction inst = null;
        while ((inst = el.nextInstruction()) != null) {
            inst.printInstruction();
        }
    }

    public static void printStream(ELFProvider elf, Class<?> streamClass) {
        workload(elf, streamClass, InstructionStreamTestUtils::printStreamWorkload);
    }

    private static void rawDumpWorkload(InstructionStream el) {
        el.rawDump();
    }

    public static void rawDump(ELFProvider elf, Class<?> streamClass) {
        workload(elf, streamClass, InstructionStreamTestUtils::rawDumpWorkload);
    }
}
