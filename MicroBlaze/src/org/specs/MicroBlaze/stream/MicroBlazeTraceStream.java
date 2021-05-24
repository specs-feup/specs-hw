package org.specs.MicroBlaze.stream;

import java.io.File;

import org.specs.MicroBlaze.asm.MicroBlazeApplication;
import org.specs.MicroBlaze.asm.MicroBlazeELFDump;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.ChanneledInstructionProducer;
import pt.up.fe.specs.binarytranslation.producer.TraceInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class MicroBlazeTraceStream extends ATraceInstructionStream {

    // three auxiliary vars to help with mb-gdb bug
    private final MicroBlazeELFDump elfdump;
    private Instruction afterbug = null;
    private boolean haveStoredInst = false;

    /*
     * IMPORTANT: this bug occurs using the mb-gdb binary bundled with vivado 2018.3; 
     * although the bug disappears in 2019.2, another bug is introduced where instructions
     *  after a branch that is not taken are not printed to the instruction stream; therefore, 
     *  its best to use the 2018 version, since at least we know that the instruction after 
     *  an IMM always executes, and therefore we can go fetch it. For branches, there is 
     *  no way to know which instruction to fetch...
     */
    private static MicroBlazeELFDump GDBbugHandle(File elfname) {

        // this if-else is only here to replace the "cholesky_trace.txt" auxiliary
        // trace file with the equivalent ELF dump, so that tests can run on Jenkins without GNU tools
        File auxname = null;
        // if (elfname.getName().equals("cholesky_trace.txt")) {
        // auxname = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/cholesky.txt");
        // auxname.deleteOnExit();
        // } else {
        // auxname = elfname;
        // }

        if (elfname.getName().contains("_trace.txt")) {
            System.out.println(elfname.getPath());
            String name = elfname.getPath().replace(".\\", "").replace("\\", "/").replace("_trace", "");
            auxname = SpecsIo.resourceCopy(name);
            auxname.deleteOnExit();
            System.out.println(name);
        } else {
            auxname = elfname;
        }



        // Workaround for mb-gdb bug of stepping over
        // two instructions at once when it hits an "imm"
        // NOTE: it also doesn't output the instructions in delay slots!!
        // 1. open the ELF
        // 2. dump all the ELF into a local list
        return new MicroBlazeELFDump(auxname);
    }

    /*
     * Auxiliary constructor so that streams can be built from the threading engine
     * with the channels created by the parent InstructionProducer
     */
    public MicroBlazeTraceStream(File elfname, ChannelConsumer<Instruction> channel) {
        super(new ChanneledInstructionProducer(new MicroBlazeApplication(elfname), channel));
        this.elfdump = MicroBlazeTraceStream.GDBbugHandle(elfname);
    }

    public MicroBlazeTraceStream(File elfname) {
        super(new MicroBlazeTraceProvider(elfname));
        this.elfdump = MicroBlazeTraceStream.GDBbugHandle(elfname);
    }

    /**
     * Use a custom trace provider instead of the default
     * 
     * @param prod
     *            an initialized custom trace provider
     */
    public MicroBlazeTraceStream(TraceInstructionProducer prod) {
        this(prod.getApp().getElffile());
    }

    @Override
    public Instruction peekNext() {
        if (this.haveStoredInst)
            return this.afterbug;
        else
            return super.peekNext();
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

                // NOTE, doing simple elfdump.getInstruction returns a reference, and we want new objects
                // after a call to nextInstruction() ALWAYS! Therefore, copy() must be appended
                Instruction tmpInst = elfdump.getInstruction(i.getAddress() + this.getInstructionWidth());
                afterbug = tmpInst.copy();
                haveStoredInst = true;
            }

            this.numcycles += i.getLatency();
            this.numinsts++;

            // TODO: temporary fix for duplicated insts
            // if (i.getRegisters().isEmpty())
            // return nextInstruction();
        }

        return i;
    }
}
