package pt.up.fe.specs.binarytranslation.detection.segments;

import java.util.List;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class MegaBlock extends ABinarySegment {

    public MegaBlock(List<Instruction> symbolicseq, List<SegmentContext> contexts, Application app) {
        super(symbolicseq, contexts, app);
        this.segtype = BinarySegmentType.MEGA_BLOCK;
    }

    @Override
    public int getExecutionCycles() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Integer> getAddresses() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getUniqueId() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getOccurences() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected String getAddressListRepresentation() {
        // TODO Auto-generated method stub
        return null;
    }
}
