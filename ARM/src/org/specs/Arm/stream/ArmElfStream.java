package org.specs.Arm.stream;

import java.io.File;
import java.util.regex.Pattern;

import org.specs.Arm.instruction.ArmInstruction;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;
import pt.up.fe.specs.util.SpecsStrings;

public class ArmElfStream extends AStaticInstructionStream {

    private static final String OBJDUMP_EXE = "aarch64-none-elf-objdump";
    private static final Pattern REGEX = Pattern.compile("([0-9a-f]+):\\s([0-9a-f]+)");

    public ArmElfStream(File elfname) {
        super(elfname, OBJDUMP_EXE);
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
    public boolean hasNext() {
        return this.insts.hasNextLine();
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
