package org.specs.Arm.asm;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.specs.Arm.isa.ArmInstruction;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.StaticStream;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.LineStream;

public class ArmElfStream implements StaticStream {

    private static final Pattern ARM_REGEX = Pattern.compile("\\s(.[0-9a-f]):\\s*([0-9a-f]+)");
    private final LineStream insts;

    public ArmElfStream(File elfname) {
        var output = SpecsSystem.runProcess(Arrays.asList("arm-none-eabi-objdump", "-d",
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
        return new ArmInstruction(Long.parseLong(addressAndInst.get(0).strip(), 16), addressAndInst.get(1));
    }

    @Override
    public void close() {
        insts.close();
    }
}
