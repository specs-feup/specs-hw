package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.List;

import pt.up.fe.specs.binarytranslation.asm.ApplicationInformation;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public abstract class ABasicBlock extends ABinarySegment {

    /**
     * 
     */
    private static final long serialVersionUID = 2615607635750210706L;

    /*
     * Constructor builds the BB on the spot with an existing list
     */
    public ABasicBlock(List<Instruction> ilist,
            List<SegmentContext> contexts, ApplicationInformation appinfo) {
        super(ilist, contexts, appinfo);
    }

    @Override
    public int getUniqueId() {
        String hashstring = "";
        for (Instruction i : this.instlist) {
            hashstring += i.getRepresentation();
        }
        return hashstring.hashCode();
    }

    @Override
    public int getExecutionCycles() {

        // instruction cycles of the segment
        Integer cycles = 0;
        for (Instruction i : this.instlist) {
            cycles += i.getLatency();
        }

        // multiply by number of contexts and their ocurrences
        Integer ocurrences = 0;
        for (SegmentContext c : this.getContexts()) {
            ocurrences += c.getOcurrences();
        }

        return cycles * ocurrences;
    }
}
