package pt.up.fe.specs.binarytranslation.detection.segments;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class MegaBlock extends ABinarySegment {

    private List<Integer> startAddresses;

    public MegaBlock(List<Instruction> symbolicseq, List<SegmentContext> contexts, Application app) {
        super(symbolicseq, contexts, app);
        this.segtype = BinarySegmentType.MEGA_BLOCK;
        this.startAddresses = new ArrayList<Integer>();
        for (SegmentContext context : contexts)
            this.startAddresses.add(context.getStartaddresses());
    }

    @Override
    public int getExecutionCycles() {

        // multiply by number of contexts and their ocurrences
        Integer ocurrences = 0;
        for (SegmentContext c : this.getContexts()) {
            ocurrences += c.getOcurrences();
        }

        return this.getLatency() * ocurrences;
    }

    @Override
    public int getOccurences() {
        int occ = 0;
        for (SegmentContext c : this.getContexts()) {
            occ += c.getOcurrences();
        }
        return occ;
    }

    @Override
    public List<Integer> getAddresses() {
        return this.startAddresses;
    }

    @Override
    protected String getAddressListRepresentation() {
        String ret = "";
        for (Integer addr : this.startAddresses) {
            ret += "0x" + Integer.toHexString(addr) + " ";
        }
        ret += "]\n" + "Total execution cycles: " + this.getExecutionCycles() + "\n";
        return ret;
    }
}
