package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class TraceBasicBlock extends ABasicBlock {

    /*
     * 
     */
    public TraceBasicBlock(List<Instruction> ilist, List<SegmentContext> contexts) {
        super(ilist, contexts);
        this.segtype = BinarySegmentType.TRACE_BASIC_BLOCK;
    }

    public TraceBasicBlock(List<Instruction> ilist, SegmentContext context) {
        super(ilist, Arrays.asList(context));
        this.segtype = BinarySegmentType.TRACE_BASIC_BLOCK;
    }

    @Override
    public Integer getExecutionCycles() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getUniqueId() {
        // TODO Auto-generated method stub
        return 0;
    }

}
