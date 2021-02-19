package pt.up.fe.specs.binarytranslation.producer.detailed;

import java.util.function.BiFunction;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.detailed.filter.GDBFilter;
import pt.up.fe.specs.binarytranslation.producer.detailed.filter.GDBRegisterFilter;
import pt.up.fe.specs.util.providers.ResourceProvider;

public class DetailedRegisterInstructionProducer extends ADetailedTraceProducer {
    protected DetailedRegisterInstructionProducer(Application app, ResourceProvider regex,
            BiFunction<String, String, Instruction> produceMethod) {
        super(app, regex, produceMethod);
        // TODO Auto-generated constructor stub
    }

    public RegisterDump nextRegister() {
        GDBFilter fil = nextElement(GDBRegisterFilter.class);
        if (fil instanceof GDBRegisterFilter)
            return (RegisterDump) fil.getResult();
        else
            return new RegisterDump();
    }
}
