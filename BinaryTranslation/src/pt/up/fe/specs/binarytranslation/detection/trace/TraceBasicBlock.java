package pt.up.fe.specs.binarytranslation.detection.trace;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class TraceBasicBlock extends ATraceUnit {

    public TraceBasicBlock(List<Instruction> ilist) {
        super(ilist);
    }

    /*
     * basic block can be defined by start and end 
     * (do I need the end???)
     */
    @Override
    public int getHash() {
        var bld = new StringBuilder();
        bld.append(this.getStart().getAddress().toString());
        bld.append(this.getEnd().getAddress().toString());

        // TODO: replace with something better?
        // this hash isnt sensitive to operand abstraction (should it be)?
        return bld.toString().hashCode();
    }
}
