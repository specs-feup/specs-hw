package org.specs.Arm.asm;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.StaticStream;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.LineStream;

public class ArmElfStream implements StaticStream {

    private static final Pattern ARM_REGEX = Pattern.compile("\\s(.[0-9a-f]):\\s*([0-9a-f]+)");
    private final LineStream insts;

    public ArmElfStream(File elfname) {
        var output = SpecsSystem.runProcess(Arrays.asList("cmd", "/c", "arm-none-eabi-objdump", "-d",
                elfname.getAbsolutePath()), new File("."), true, false);

        insts = LineStream.newInstance(output.getStdOut());
    }

    @Override
    public Instruction nextInstruction() {
        String line = null;
        while (((line = insts.nextLine()) != null) && !SpecsStrings.matches(line, ARM_REGEX))
            ;
        var addressAndInst = SpecsStrings.getRegex(line, ARM_REGEX);
        return Instruction.newInstance(addressAndInst.get(0), addressAndInst.get(1));
    }

    @Override
    public void close() throws IOException {
        insts.close();
        close();
    }
}
