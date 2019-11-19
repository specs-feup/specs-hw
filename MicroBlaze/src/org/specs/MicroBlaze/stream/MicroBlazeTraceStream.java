package org.specs.MicroBlaze.stream;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.LineStream;

public class MicroBlazeTraceStream implements InstructionStream {

    private static final Pattern MB_REGEX = Pattern.compile("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");
    private LineStream insts;

    public MicroBlazeTraceStream(File elfname) {

        // 1. make the gdb script file
        try {
            String elfpath = elfname.getAbsolutePath();
            String dtbfullpath = "./resources/org/specs/MicroBlaze/qemu/system.dtb";

            PrintWriter gdbinit = new PrintWriter("tmpscript.gdb");
            gdbinit.println("set confirm off");
            gdbinit.println("undisplay");
            gdbinit.println("set print address off");
            gdbinit.println("set height 0");
            gdbinit.println("file " + elfpath);
            gdbinit.println(
                    "target remote | /media/nuno/HDD/work/projects/qemu/microblazeel-softmmu/qemu-system-microblazeel "
                            + "-M microblaze-fdt-plnx -m 64 -device loader,file=" + elfpath + " -gdb stdio "
                            + "-hw-dtb " + dtbfullpath + " -display none -S");
            gdbinit.println("while $pc != 0x80");
            gdbinit.println("stepi");
            gdbinit.println("x/x $pc");
            gdbinit.println("end");
            gdbinit.println("kill");
            gdbinit.println("quit");
            gdbinit.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        var output = SpecsSystem.runProcess(
                Arrays.asList("mb-gdb", "-x", "tmpscript.gdb"), new File("."), true, false);
        insts = LineStream.newInstance(output.getStdOut());
    }

    @Override
    public Instruction nextInstruction() {

        String line = null;
        while (((line = insts.nextLine()) != null) && !SpecsStrings.matches(line, MB_REGEX))
            ;

        if (line == null) {
            return null;
        }

        var addressAndInst = SpecsStrings.getRegex(line, MB_REGEX);
        var addr = addressAndInst.get(0).trim();
        var inst = addressAndInst.get(1).trim();
        return MicroBlazeInstruction.newInstance(addr, inst);
    }

    @Override
    public InstructionStreamType getType() {
        return InstructionStreamType.TRACE;
    }

    @Override
    public void close() {
        insts.close();
    }

    @Override
    public int getInstructionWidth() {
        return 4; // return in bytes
        // TODO replace this with something smarter
    }
}
