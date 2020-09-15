package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

/**
 * 
 * @author Nuno
 *
 */
public class TraceBasicBlock extends ABasicBlock {

    /*
     * 
     */
    public TraceBasicBlock(List<Instruction> ilist,
            List<SegmentContext> contexts, Application appinfo) {
        super(ilist, contexts, appinfo);
        this.segtype = BinarySegmentType.TRACE_BASIC_BLOCK;
    }

    public TraceBasicBlock(List<Instruction> ilist,
            SegmentContext context, Application appinfo) {
        super(ilist, Arrays.asList(context), appinfo);
        this.segtype = BinarySegmentType.TRACE_BASIC_BLOCK;
    }
}
