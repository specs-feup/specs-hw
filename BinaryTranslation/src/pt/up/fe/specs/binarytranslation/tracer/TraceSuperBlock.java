package pt.up.fe.specs.binarytranslation.tracer;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class TraceSuperBlock extends ATraceUnit {

    private List<TraceBasicBlock> tbblist;

    public TraceSuperBlock(List<TraceBasicBlock> tbblist) {
        super(TraceUnitType.TraceSuperBlock);
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
}
