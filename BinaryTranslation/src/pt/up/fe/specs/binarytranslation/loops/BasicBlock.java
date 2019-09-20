package pt.up.fe.specs.binarytranslation.loops;

import java.util.List;

import pt.up.fe.specs.binarytranslation.Instruction;

public class BasicBlock implements BinarySegment {

    private segmentType segtype = segmentType.BasicBlock;
    private List<Instruction> instlist;

    @Override
    public int getSegmentLength() {
        return instlist.size();
    }

    @Override
    public segmentType getSegmentType() {
        // TODO Auto-generated method stub
        return null;
    }
}
