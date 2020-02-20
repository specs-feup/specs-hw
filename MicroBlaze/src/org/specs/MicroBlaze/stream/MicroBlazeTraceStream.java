package org.specs.MicroBlaze.stream;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.specs.MicroBlaze.MicroBlazeResource;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import pt.up.fe.specs.binarytranslation.BinaryTranslationUtils;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.SpecsStrings;

public class MicroBlazeTraceStream extends ATraceInstructionStream {

    private static final String GDB_EXE = "mb-gdb";
    private static final String QEMU_EXE = "/media/nuno/HDD/work/projects/myqemus/qemu-system-microblazeel";
    private static final Pattern REGEX = Pattern.compile("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");
    private static final MicroBlazeResource QEMU_DTB = MicroBlazeResource.QEMU_MICROBLAZE_BAREMETAL_DTB;
    private static final MicroBlazeResource QEMU_GDB_TMPL = MicroBlazeResource.QEMU_MICROBLAZE_GDB_TEMPLATE;

    // two auxiliary vars to help with mb-gdb bug
    private final Map<Integer, Instruction> elfdump = new HashMap<Integer, Instruction>();
    private Instruction afterbug = null;
    private boolean haveStoredInst = false;

    public MicroBlazeTraceStream(File elfname) {
        // super(MicroBlazeTraceStream.newSimulatorBuilder(elfname));

        super(elfname, QEMU_GDB_TMPL, GDB_EXE, QEMU_DTB, QEMU_EXE);

        this.appName = elfname.getName();
        this.compilationInfo = BinaryTranslationUtils.getCompilationInfo(elfname.getPath(),
                MicroBlazeResource.MICROBLAZE_READELF.getResource());

        // Workaround for mb-gdb bug of stepping over
        // two instructions at once when it hits an "imm"
        // NOTE: it also doesn't output the instructions in delay slots!!
        // 1. open the ELF
        // 2. dump all the ELF into a local list
        var elf = new MicroBlazeElfStream(elfname);
        Instruction i = null;
        while ((i = elf.nextInstruction()) != null) {
            elfdump.put(i.getAddress().intValue(), i);
        }
        elf.close();

        /*
         * IMPORTANT: this bug occurs using the mb-gdb binary bundled with vivado 2018.3; 
         * although the bug disappears in 2019.2, another bug is introduced where instructions
         *  after a branch that is not taken are not printed to the instruction stream; therefore, 
         *  its best to use the 2018 version, since at least we know that the instruction after 
         *  an IMM always executes, and therefore we can go fetch it. For branches, there is 
         *  no way to know which instruction to fetch...
         */
    }

    /*
     * This function will be usable once mg-gdb is bug free...
     * 
    @Override
    public Instruction nextInstruction() {
    
        var newinst = MicroBlazeInstructionStreamMethods.nextInstruction(this.insts, REGEX);
        if (newinst == null) {
            return null;
        }
        this.numcycles += newinst.getLatency();
        this.numinsts++;
        return newinst;
    }
    */

    @Override
    public Instruction nextInstruction() {

        Instruction i = null;

        if (haveStoredInst == true) {
            i = afterbug;
            haveStoredInst = false;
        }

        else {
            String line = null;
            while (((line = insts.nextLine()) != null) && !SpecsStrings.matches(line, REGEX))
                ;

            if (line == null) {
                return null;
            }

            var addressAndInst = SpecsStrings.getRegex(line, REGEX);
            var addr = addressAndInst.get(0).trim();
            var inst = addressAndInst.get(1).trim();
            i = MicroBlazeInstruction.newInstance(addr, inst);
        }

        // get another one if true
        if (i.isImmediateValue() || (i.getDelay() > 0)) {
            afterbug = elfdump.get(i.getAddress().intValue() + this.getInstructionWidth());
            haveStoredInst = true;
        }

        this.numcycles += i.getLatency();
        this.numinsts++;

        // i.printInstruction();

        return i;
    }

    @Override
    public int getInstructionWidth() {
        return MicroBlazeInstructionStreamMethods.getInstructionWidth();
    }
}
