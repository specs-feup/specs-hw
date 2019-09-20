package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.Instruction;

public class BasicBlock implements BinarySegment {

    private segmentType segtype = segmentType.BasicBlock;
    private List<Instruction> instlist;

    public BasicBlock() {
        this.instlist = new ArrayList<Instruction>();
    }

    public void addInst(Instruction newinst) {
        this.instlist.add(newinst);

        // TODO if added inst is branch, prevent addition of new insts
    }

    @Override
    public List<Integer> getLiveIns() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Integer> getLiveOuts() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getTotalLatency() {
        int totlat = 0;
        for (int i = 0; i < instlist.size(); i++)
            totlat += instlist.get(i).getLatency();
        return totlat;
    }

    @Override
    public int getSegmentLength() {
        return instlist.size();
    }

    @Override
    public segmentType getSegmentType() {
        return this.segtype;
    }
}
