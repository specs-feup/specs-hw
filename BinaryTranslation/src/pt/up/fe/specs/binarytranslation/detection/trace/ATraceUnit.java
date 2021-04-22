package pt.up.fe.specs.binarytranslation.detection.trace;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public abstract class ATraceUnit implements TraceUnit {

    private List<Instruction> ilist;

    public ATraceUnit(List<Instruction> ilist) {
        this.ilist = ilist;
    }

    public List<Instruction> getList() {
        return ilist;
    }

    @Override
    public Instruction getStart() {
        return ilist.get(0);
    }

    @Override
    public Instruction getEnd() {
        return ilist.get(ilist.size() - 1);
    }

    /*
    @Override
    public int getHash() {
        // TODO replace with something better?
        var bld = new StringBuilder();
        for (var inst : this.ilist)
            bld.append(inst.getAddress().toString());
    
        // TODO: this hash isnt sensitive to operand abstraction (should it be)?
        return bld.toString().hashCode();
    }*/
}
