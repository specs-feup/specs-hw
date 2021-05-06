package pt.up.fe.specs.binarytranslation.tracer;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class StreamBasicBlock extends AStreamUnit {

    private List<StreamInstruction> tilist;

    public StreamBasicBlock(List<StreamInstruction> tilist, StreamUnitType bbtype) {
        super(bbtype, StreamBasicBlock.getBranchTarget(tilist));
        this.tilist = tilist;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (var tinst : this.tilist) {
            hash += tinst.hashCode();
        }
        return hash; // TODO: works??
    }

    @Override
    public String toString() {
        var bld = new StringBuilder();
        for (var tinst : this.tilist) {
            bld.append(tinst.toString() + "\n");
        }
        return bld.toString();
    }

    @Override
    public Instruction getStart() {
        return this.tilist.get(0).getActual();
    }

    @Override
    public Instruction getEnd() {
        return this.tilist.get(this.tilist.size() - 1).getActual();
    }

    @Override
    public boolean containsAddr(Long addr) {
        for (var tinst : this.tilist) {
            if (tinst.getActual().getAddress().longValue() == addr.longValue())
                return true;
        }
        return false;
    }

    /*
     * True if any instruction in this TraceUnit
     * includes the target of the "other"
     */
    @Override
    public boolean includesTarget(StreamUnit other) {
        var otherTargetAddr = other.getTargetAddr();
        for (var inst : this.tilist) {
            if (inst.getActual().getAddress().longValue() == otherTargetAddr.longValue())
                return true;
        }
        return false;
    }
}
