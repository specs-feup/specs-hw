package org.specs.Arm.stream;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.specs.Arm.ArmResource;
import org.specs.Arm.instruction.ArmInstruction;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.asm.processor.RegisterTable;
import pt.up.fe.specs.util.system.OutputType;
import pt.up.fe.specs.util.system.StreamToString;
import pt.up.fe.specs.util.utilities.LineStream;
import pt.up.fe.specs.util.utilities.Replacer;

public class ArmTraceStream implements TraceInstructionStream {

    private static final Pattern REGEX = Pattern.compile("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");
    private LineStream insts = null;

    public ArmTraceStream(File elfname) {

        // 1. make the gdb script file
        String elfpath = elfname.getAbsolutePath();

        var gdbScript = new Replacer(ArmResource.QEMU_GDB_TEMPLATE);
        gdbScript.replace("<ELFNAME>", elfpath);
        gdbScript.replace("<QEMUBIN>", "/media/nuno/HDD/work/projects/qemu/aarch64-softmmu/qemu-system-aarch64");
        SpecsIo.write(new File("tmpscript.gdb"), gdbScript.toString());

        Function<InputStream, Boolean> stdout = inputStream -> {
            insts = LineStream.newInstance(inputStream, "gdb_stdout");
            return true;
        };

        // Function<InputStream, String> stdout = new StreamToString(true, false, OutputType.StdOut);
        Function<InputStream, String> stderr = new StreamToString(false, true, OutputType.StdErr);

        var executor = Executors.newSingleThreadScheduledExecutor();
        executor.execute(() -> SpecsSystem.runProcess(
                Arrays.asList("aarch64-none-elf-gdb", "-x", "tmpscript.gdb"), new File("."), stdout, stderr));

        while (insts == null)
            ;

        // insts = LineStream.newInstance(output.getStdOut());
    }

    public void rawDump() {
        String line = null;
        while ((line = insts.nextLine()) != null) {
            System.out.print(line + "\n");
        }
    }

    @Override
    public Instruction nextInstruction() {

        String line = null;
        while (((line = insts.nextLine()) != null) && !SpecsStrings.matches(line, REGEX))
            ;

        if (line == null) {
            return null;
        }

        var addressAndInst = SpecsStrings.getRegex(line, REGEX);
        var addr = addressAndInst.get(0).trim();
        var inst = addressAndInst.get(1).trim();
        return ArmInstruction.newInstance(addr, inst);
    }

    @Override
    public void close() {
        insts.close();
    }

    @Override
    public Instruction nextTraceInstruction() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getNumInstructions() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public RegisterTable getRegisters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getCycles() {
        // TODO Auto-generated method stub
        return 0;
    }
}
