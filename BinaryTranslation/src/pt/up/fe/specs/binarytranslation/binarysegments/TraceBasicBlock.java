package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.asm.ApplicationInformation;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

/**
 * 
 * @author Nuno
 *
 */
public class TraceBasicBlock extends ABasicBlock {

    /**
     * 
     */
    private static final long serialVersionUID = -6379462738784714851L;

    /*
     * 
     */
    public TraceBasicBlock(List<Instruction> ilist,
            List<SegmentContext> contexts, ApplicationInformation appinfo) {
        super(ilist, contexts, appinfo);
        this.segtype = BinarySegmentType.TRACE_BASIC_BLOCK;
    }

    public TraceBasicBlock(List<Instruction> ilist,
            SegmentContext context, ApplicationInformation appinfo) {
        super(ilist, Arrays.asList(context), appinfo);
        this.segtype = BinarySegmentType.TRACE_BASIC_BLOCK;
    }
}
