package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class FrequentTraceSequence extends AFrequentSequence {

    /**
     * 
     */
    private static final long serialVersionUID = -5673675221461684247L;

    /*
     * Addr, Frequency Count
     */
    private Map<Integer, Integer> startAddresses;

    @Override
    public int getExecutionCycles() {

        Integer totalCycles = 0;
        Integer staticCycles = this.getLatency();

        for (Integer i : startAddresses.values()) {
            totalCycles += (staticCycles * i);
        }
        return totalCycles;
    }

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    public FrequentTraceSequence(List<Instruction> ilist,
            List<SegmentContext> contexts, Application appinfo) {
        super(ilist, contexts, appinfo);
        this.segtype = BinarySegmentType.TRACE_FREQUENT_SEQUENCE;
        this.startAddresses = new HashMap<Integer, Integer>();
        for (SegmentContext context : contexts) {
            this.startAddresses.put(context.getStartaddresses(), context.getOcurrences());
        }
    }

    @Override
    protected List<Integer> getAddresses() {
        return new ArrayList<Integer>(this.startAddresses.keySet());
    }

    @Override
    protected String getAddressListRepresentation() {
        String ret = "";
        for (Map.Entry<Integer, Integer> entry : this.startAddresses.entrySet()) {
            var addr = entry.getKey();
            var count = entry.getValue();
            ret += "0x" + Integer.toHexString(addr) + "(" + count + ")" + " ";
        }
        ret += "] " + "Total execution cycles: " + this.getExecutionCycles() + "\n";
        return ret;
    }
}
