package org.specs.MicroBlaze.stream;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.StaticInstructionStream;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.LineStream;

public class MicroBlazeElfStream implements StaticInstructionStream {

    private static final Pattern MB_REGEX = Pattern.compile("([0-9a-f]+):\\s([0-9a-f]+)");
    private LineStream insts;

    // TODO modify MB_REGEX

    public MicroBlazeElfStream(File elfname) {
        var output = SpecsSystem.runProcess(Arrays.asList("cmd", "/c", "type", //"mb-objdump", "-d",
                elfname.getAbsolutePath()), new File("."), true, false);
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
    public void close() {
        insts.close();
    }

    @Override
    public int getInstructionWidth() {
        return 4; // return in bytes
        // TODO replace this with something smarter
    }
}
