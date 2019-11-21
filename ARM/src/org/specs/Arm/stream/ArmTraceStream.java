package org.specs.Arm.stream;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.specs.Arm.instruction.ArmInstruction;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.asm.processor.RegisterTable;
import pt.up.fe.specs.util.utilities.LineStream;

public class ArmTraceStream implements TraceInstructionStream {

    private static final Pattern REGEX = Pattern.compile("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");
    private LineStream insts;

    public ArmTraceStream(File elfname) {

        // 1. make the gdb script file
        try {
            String elfpath = elfname.getAbsolutePath();

            PrintWriter gdbinit = new PrintWriter("tmpscript.gdb");
            gdbinit.println("set confirm off");
            gdbinit.println("undisplay");
            gdbinit.println("set print address off");
            gdbinit.println("set height 0");
            gdbinit.println("file " + elfpath);
            gdbinit.println(
                    "target remote | /media/nuno/HDD/work/projects/qemu/aarch64-softmmu/qemu-system-aarch64 "
                            + " -M virt -cpu cortex-a53 -nographic -kernel " + elfpath
                            + " -chardev stdio,mux=on,id=char0 -mon chardev=char0,mode=readline"
                            + " -serial chardev:char0 -gdb chardev:char0 -S");
            gdbinit.println("set $v = 0");
            gdbinit.println("while $pc != $v");
            gdbinit.println("set $v = $pc");
            gdbinit.println("stepi 1");
            gdbinit.println("x/x $pc");
            gdbinit.println("end");
            gdbinit.println("kill");
            gdbinit.println("quit");
            gdbinit.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        var output = SpecsSystem.runProcess(
                Arrays.asList("aarch64-none-elf-gdb", "-x", "tmpscript.gdb"), new File("."), true, false);
        insts = LineStream.newInstance(output.getStdOut());
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
