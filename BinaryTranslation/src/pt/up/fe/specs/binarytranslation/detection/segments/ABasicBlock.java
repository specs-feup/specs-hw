package pt.up.fe.specs.binarytranslation.detection.segments;

import java.util.List;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public abstract class ABasicBlock extends ABinarySegment {

    /*
     * Constructor builds the BB on the spot with an existing list
     */
    public ABasicBlock(List<Instruction> ilist,
            List<SegmentContext> contexts, Application appinfo) {
        super(ilist, contexts, appinfo);
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
    public int getExecutionCycles() {

        // multiply by number of contexts and their ocurrences
        Integer ocurrences = 0;
        for (SegmentContext c : this.getContexts()) {
            ocurrences += c.getOcurrences();
        }

        return this.getLatency() * ocurrences;
    }
}
