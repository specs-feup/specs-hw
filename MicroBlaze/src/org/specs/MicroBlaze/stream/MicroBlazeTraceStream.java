package org.specs.MicroBlaze.stream;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class MicroBlazeTraceStream extends ATraceInstructionStream {

    // two auxiliary vars to help with mb-gdb bug
    private final Map<Integer, Instruction> elfdump = new HashMap<Integer, Instruction>();
    private Instruction afterbug = null;
    private boolean haveStoredInst = false;

    public MicroBlazeTraceStream(File elfname) {

        super(new MicroBlazeTrace(elfname));

        // Workaround for mb-gdb bug of stepping over
        // two instructions at once when it hits an "imm"
        // NOTE: it also doesn't output the instructions in delay slots!!
        // 1. open the ELF
        // 2. dump all the ELF into a local list

        try (var elf = new MicroBlazeElfStream(elfname)) {
            Instruction i = null;
            while ((i = elf.nextInstruction()) != null) {
                elfdump.put(i.getAddress().intValue(), i);
            }
        }

        /*
         * IMPORTANT: this bug occurs using the mb-gdb binary bundled with vivado 2018.3; 
         * although the bug disappears in 2019.2, another bug is introduced where instructions
         *  after a branch that is not taken are not printed to the instruction stream; therefore, 
         *  its best to use the 2018 version, since at least we know that the instruction after 
         *  an IMM always executes, and therefore we can go fetch it. For branches, there is 
         *  no way to know which instruction to fetch...
         */
    }

    @Override
    public int getInstructionWidth() {
        return 4; // return in bytes
        // TODO replace this with something smarter
    }

    /*
     * NOTE: this override will no longer be necessary once mg-gdb is bug free...
     */
    @Override
    public Instruction nextInstruction() {

        Instruction i = null;

        if (haveStoredInst == true) {
            i = afterbug;
            haveStoredInst = false;

        } else {
            i = super.nextInstruction();
        }

        if (i != null) {
            // get another one if true
            if (i.isImmediateValue() || (i.getDelay() > 0)) {
                afterbug = elfdump.get(i.getAddress().intValue() + this.getInstructionWidth());
                haveStoredInst = true;
            }

            this.numcycles += i.getLatency();
            this.numinsts++;
        }
        return i;
    }
}
