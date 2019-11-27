package org.specs.MicroBlaze.stream;

import java.io.File;
import java.util.regex.Pattern;

import org.specs.MicroBlaze.MicroBlazeResource;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.SpecsStrings;

public class MicroBlazeTraceStream extends ATraceInstructionStream {

    private static final String GDB_EXE = "mb-gdb";
    private static final String QEMU_EXE = "qemu-system-microblazeel";
    private static final Pattern REGEX = Pattern.compile("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");

    public MicroBlazeTraceStream(File elfname) {
        super(elfname, MicroBlazeResource.QEMU_MICROBLAZE_GDB_TEMPLATE, GDB_EXE, QEMU_EXE);
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
        var newinst = MicroBlazeInstruction.newInstance(addr, inst);

        this.numcycles += newinst.getLatency();
        this.numinsts++;

        return newinst;
    }

    @Override
    public int getInstructionWidth() {
        return 4; // return in bytes
        // TODO replace this with something smarter
    }
}
