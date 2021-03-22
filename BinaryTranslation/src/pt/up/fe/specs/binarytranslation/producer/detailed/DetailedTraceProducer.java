package pt.up.fe.specs.binarytranslation.producer.detailed;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.TraceInstructionProducer;
import pt.up.fe.specs.binarytranslation.producer.detailed.filter.GDBFilter;
import pt.up.fe.specs.binarytranslation.producer.detailed.filter.GDBNullFilter;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.utilities.LineStream;

public class DetailedTraceProducer extends TraceInstructionProducer {

    protected DetailedTraceProducer(Application app, ResourceProvider regex,
            BiFunction<String, String, Instruction> produceMethod) {
        super(app, regex, produceMethod);
    }

    public GDBFilter nextElement(Class<? extends GDBFilter> gdbFilter) {
        GDBFilter fil = null;
        try {
            Constructor<?> cons = gdbFilter.getConstructor(LineStream.class);
            fil = (GDBFilter) cons.newInstance(this.insts);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            SpecsLogs.msgSevere("Error message:\n" + e.toString());
        }
        if (fil.filter())
            return fil;
        else {
            return new GDBNullFilter(this.insts);
        }
    }
}
