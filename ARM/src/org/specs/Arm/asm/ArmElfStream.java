package org.specs.Arm.asm;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.specs.Arm.isa.ArmInstruction;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.interfaces.StaticStream;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.LineStream;

public class ArmElfStream implements StaticStream {

    private static final Pattern ARM_REGEX = Pattern.compile("([0-9a-f]+):\\s([0-9a-f]+)");
    private final LineStream insts;

    public ArmElfStream(File elfname) {
        var output = SpecsSystem.runProcess(Arrays.asList("aarch64-none-elf-objdump", "-d",
                elfname.getAbsolutePath()), new File("."), true, false);

        insts = LineStream.newInstance(output.getStdOut());
    }

    @Override
    public Instruction nextInstruction() {
        String line = null;
        while (((line = insts.nextLine()) != null) && !SpecsStrings.matches(line, ARM_REGEX))
            ;

        if (line == null) {
            return null;
        }

        var addressAndInst = SpecsStrings.getRegex(line, ARM_REGEX);
        var addr = addressAndInst.get(0).trim();
        var inst = addressAndInst.get(1).trim();
        // System.out.print(addr + "\n");
        return ArmInstruction.newInstance(addr, inst);
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
