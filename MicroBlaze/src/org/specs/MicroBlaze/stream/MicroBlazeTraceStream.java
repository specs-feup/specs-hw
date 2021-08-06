package org.specs.MicroBlaze.stream;

import org.specs.MicroBlaze.asm.MicroBlazeApplication;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.ChanneledInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class MicroBlazeTraceStream extends ATraceInstructionStream {

    // three auxiliary vars to help with mb-gdb bug
    // private final MicroBlazeELFDump elfdump;
    // private Instruction afterbug = null;
    // private RegisterDump savedRegs = null;
    // private boolean haveStoredInst = false;

    /*
     * IMPORTANT: this bug occurs using the mb-gdb binary bundled with vivado 2018.3; 
     * although the bug disappears in 2019.2, another bug is introduced where instructions
     *  after a branch that is not taken are not printed to the instruction stream; therefore, 
     *  its best to use the 2018 version, since at least we know that the instruction after 
     *  an IMM always executes, and therefore we can go fetch it. For branches, there is 
     *  no way to know which instruction to fetch...
     
    private static MicroBlazeELFDump GDBbugHandle(MicroBlazeELFProvider elfprovider) {
    
        // if trace dump fetch original name, to then produce an objdump in memory
        if (elfprovider instanceof MicroBlazeTraceDumpProvider) {
            var original = ((MicroBlazeTraceDumpProvider) elfprovider).getOriginal();
            return new MicroBlazeELFDump(original);
        } else {
            return new MicroBlazeELFDump(elfprovider);
        }
    }*/

    /*
     * Auxiliary constructor so that streams can be built from the threading engine
     * with the channels created by the parent InstructionProducer
     */
    public MicroBlazeTraceStream(MicroBlazeELFProvider elfprovider, ChannelConsumer<Instruction> channel) {
        super(new ChanneledInstructionProducer(new MicroBlazeApplication(elfprovider), channel));
        // this.elfdump = MicroBlazeTraceStream.GDBbugHandle(elfprovider);
    }

    public MicroBlazeTraceStream(MicroBlazeELFProvider elfprovider) {
        super(new MicroBlazeTraceProducer(elfprovider));
        // this.elfdump = MicroBlazeTraceStream.GDBbugHandle(elfprovider);
    }

    /**
     * Use a custom trace provider instead of the default
     * 
     * @param prod
     *            an initialized custom trace provider
     */
    public MicroBlazeTraceStream(MicroBlazeDetailedTraceProducer prod) {
        super(prod);
        // var mprovid = (MicroBlazeELFProvider) prod.getApp().getELFProvider();
        // this.elfdump = new MicroBlazeELFDump(mprovid);
    }

    /*@Override
    public Instruction peekNext() {
        if (this.haveStoredInst)
            return this.afterbug;
        else
            return super.peekNext();
    }*/

    /*
     * NOTE: this override will no longer be necessary once mg-gdb is bug free...
     
    @Override
    public Instruction nextInstruction() {
    
        Instruction i = null;
    
        if (haveStoredInst == true) {
            i = afterbug;
            if (savedRegs != null)
                i.setRegisters(new RegisterDump(savedRegs));
            haveStoredInst = false;
    
            // this is called via super.nextInstruction in other cases
            if (i != null)
                this.counterIncreases(i);
    
        } else {
            i = super.nextInstruction();
        }
    
        if (i != null) {
            // get another one if true
            if (i.isImmediateValue() || (i.getDelay() > 0)) {
    
                // NOTE, doing simple elfdump.getInstruction returns a reference, and we want new objects
                // after a call to nextInstruction() ALWAYS! Therefore, copy() must be appended
                var tmpInst = elfdump.getInstruction(i.getAddress() + this.getInstructionWidth());
                afterbug = tmpInst.copy();
                haveStoredInst = true;
                savedRegs = i.getRegisters();
            }
    
            // TODO: temporary fix for duplicated insts
            // if (i.getRegisters().isEmpty())
            // return nextInstruction();
        }
    
        return i;
    }*/
}
