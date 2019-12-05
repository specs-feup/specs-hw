package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class FrequentTraceSequence extends AFrequentSequence {

    /*
     * Addr, Frequency Count
     */
    private Map<Integer, Integer> startAddresses;

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    private FrequentTraceSequence(List<Instruction> ilist) {
        super(ilist);
        this.segtype = BinarySegmentType.TRACE_FREQUENT_SEQUENCE;
    }

    @Override
    public Integer getExecutionCycles() {

        Integer totalCycles = 0;
        Integer staticCycles = this.getSegmentCycles();

        for (Integer i : startAddresses.values()) {
            totalCycles += (staticCycles * i);
        }
        return totalCycles;
    }

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    public FrequentTraceSequence(List<Instruction> ilist, Map<Integer, Integer> startAddresses) {
        this(ilist);
        this.startAddresses = startAddresses;
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
