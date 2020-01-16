package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public abstract class ABasicBlock extends ABinarySegment {

    /*
     * Constructor builds the BB on the spot with an existing list
     */
    public ABasicBlock(List<Instruction> ilist) {
        this.instlist = ilist;
        // buildLiveInsAndLiveOuts();
    }
}
