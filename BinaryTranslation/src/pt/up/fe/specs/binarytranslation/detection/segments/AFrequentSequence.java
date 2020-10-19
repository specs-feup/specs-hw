package pt.up.fe.specs.binarytranslation.detection.segments;

import java.util.List;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public abstract class AFrequentSequence extends ABinarySegment {

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    protected AFrequentSequence(List<Instruction> ilist,
            List<SegmentContext> contexts, Application appinfo) {
        super(ilist, contexts, appinfo);
    }

    /*
     * 
     */
    @Override
    public int getUniqueId() {
        String uniqueid = "";
        for (Integer i : this.getAddresses()) {
            uniqueid += i.toString();
        }

        return uniqueid.hashCode();
    }
}
