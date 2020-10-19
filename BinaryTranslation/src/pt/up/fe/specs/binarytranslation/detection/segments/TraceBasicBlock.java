package pt.up.fe.specs.binarytranslation.detection.segments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

/**
 * 
 * @author Nuno
 *
 */
public class TraceBasicBlock extends ABasicBlock {

    /*
     * Addr, Frequency Count
     */
    private Map<Integer, Integer> startAddresses;

    /*
     * 
     */
    public TraceBasicBlock(List<Instruction> ilist,
            List<SegmentContext> contexts, Application appinfo) {
        super(ilist, contexts, appinfo);
        this.segtype = BinarySegmentType.TRACE_BASIC_BLOCK;
        for (SegmentContext context : contexts) {
            this.startAddresses.put(context.getStartaddresses(), context.getOcurrences());
        }
    }

    @Override
    public List<Integer> getAddresses() {
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
