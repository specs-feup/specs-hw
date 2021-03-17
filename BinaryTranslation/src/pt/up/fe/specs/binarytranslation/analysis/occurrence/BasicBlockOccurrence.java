package pt.up.fe.specs.binarytranslation.analysis.occurrence;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.detailed.RegisterDump;

public class BasicBlockOccurrence {
    
    private int endPos;
    private int startPos;
    private BinarySegment bb;
    private int id;
    private List<Instruction> insts;

    public BasicBlockOccurrence(int id, BinarySegment bb, int startInstPos, List<Instruction> insts) {
        this.id = id;
        this.bb = bb;
        this.startPos = startInstPos;
        this.insts = insts;
        this.endPos = startPos + bb.getSegmentLength();
    }
    
    public boolean belongsToOccurrence(int instPos) {
        return instPos >= startPos && instPos <= endPos;
    }

    public BinarySegment getBasicBlock() {
        return bb;
    }

    public int getId() {
        return id;
    }

    public int getEndPos() {
        return endPos;
    }

    public int getStartPos() {
        return startPos;
    }
    
    @Override
    public String toString() {
        var sb = new StringBuilder();
        var startInst = insts.get(0);
        var endInst = insts.get(insts.size() - 1);
        sb.append("Basic block (occurr = ").append(id).append(", id = ").append(bb.getUniqueId());
        sb.append(" [").append(startPos).append("-").append(endPos).append("])\n");
        sb.append(startPos).append(": ").append(startInst.getString()).append("\n");
        sb.append("...\n");
        sb.append(endPos).append(": ").append(endInst.getString()).append("\n");
        return sb.toString();
    }

    public RegisterDump getRegisters() {
        var dump = new RegisterDump();
        for (var i : insts) {
            dump.merge(i.getRegisters());
        }
        return dump;
    }

    public List<Instruction> getInsts() {
        return insts;
    }
}
