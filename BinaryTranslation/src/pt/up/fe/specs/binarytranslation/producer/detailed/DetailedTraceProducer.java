package pt.up.fe.specs.binarytranslation.producer.detailed;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.InstructionInstantiator;
import pt.up.fe.specs.binarytranslation.producer.TraceInstructionProducer;
import pt.up.fe.specs.binarytranslation.producer.detailed.filter.GDBFilter;
import pt.up.fe.specs.binarytranslation.producer.detailed.filter.GDBNullFilter;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.utilities.LineStream;

@Deprecated
public class DetailedTraceProducer extends TraceInstructionProducer {

    protected DetailedTraceProducer(Application app, InstructionInstantiator produceMethod) {
        super(app, produceMethod);
    }

    public GDBFilter nextElement(Class<? extends GDBFilter> gdbFilter) {
        GDBFilter fil = null;
        try {
            Constructor<?> cons = gdbFilter.getConstructor(LineStream.class);
            fil = (GDBFilter) cons.newInstance(null);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            SpecsLogs.msgSevere("Error message:\n" + e.toString());
        }
        if (fil.filter())
            return fil;
        else {
            return new GDBNullFilter(null);
        }
    }
}
