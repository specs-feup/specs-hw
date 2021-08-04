package pt.up.fe.specs.binarytranslation.producer.detailed;

import java.util.function.BiFunction;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.detailed.filter.GDBFilter;
import pt.up.fe.specs.binarytranslation.producer.detailed.filter.GDBRegisterFilter;

public class DetailedRegisterInstructionProducer extends DetailedTraceProducer {
    protected int count = 0;
    protected boolean showStillAlive = true;

    protected DetailedRegisterInstructionProducer(Application app,
            BiFunction<String, String, Instruction> produceMethod) {
        super(app, produceMethod);
    }

    public RegisterDump nextRegister() {
        GDBFilter fil = nextElement(GDBRegisterFilter.class);
        if (fil instanceof GDBRegisterFilter)
            return (RegisterDump) fil.getResult();
        else
            return null; // RegisterDump.nullDump;
        // TODO fix later
    }

    @Override
    public Instruction nextInstruction() {
        RegisterDump reg = nextRegister();

        Instruction inst = super.nextInstruction();
        if (inst != null) {
            inst.setRegisters(reg);
            if (showStillAlive) {
                count++;
                if (count % 1000 == 0)
                    System.err.println("Producer still alive (inst=" + count + ")");
            }
        }
        return inst;
    }

    public void setDisplayStillAlive(boolean display) {
        this.showStillAlive = display;
    }
}
