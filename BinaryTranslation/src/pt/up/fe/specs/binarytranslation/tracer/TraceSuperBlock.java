package pt.up.fe.specs.binarytranslation.tracer;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class TraceSuperBlock extends ATraceUnit {

    private List<TraceBasicBlock> tbblist;

    public TraceSuperBlock(List<TraceBasicBlock> tbblist) {
        super(TraceUnitType.TraceSuperBlock,
                tbblist.get(tbblist.size() - 1).getTargetAddr());
        this.tbblist = tbblist;
    }

    // used in map comparisons
    @Override
    public int hashCode() {
        int hash = 0;
        for (var tbb : this.tbblist) {
            hash += tbb.hashCode();
        }
        return hash; // TODO: works??
    }

    @Override
    public String toString() {
        var bld = new StringBuilder();
        for (var bb : this.tbblist) {
            bld.append(bb.toString());
        }
        return bld.toString();
    }

    @Override
    public Instruction getStart() {
        return this.tbblist.get(0).getStart();
    }

    @Override
    public Instruction getEnd() {
        return this.tbblist.get(this.tbblist.size() - 1).getEnd();
    }

    /*
     * True if any instruction in this TraceUnit
     * includes the target of the "other"
     */
    public boolean includesTarget(TraceUnit other) {
        for (var tbb : this.tbblist) {
            if (tbb.includesTarget(other))
                return true;
        }
        return false;
    }
}
