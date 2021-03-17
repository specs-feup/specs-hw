package pt.up.fe.specs.binarytranslation.analysis.occurrence;

import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;

public class BasicBlockOccurrence {
    
    private int endPos;
    private int startPos;
    private BinarySegment bb;
    private int id;

    public BasicBlockOccurrence(int id, BinarySegment bb, int startInstPos) {
        this.id = id;
        this.bb = bb;
        this.startPos = startInstPos;
        this.endPos = startInstPos + bb.getInstructions().size();
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
        var startInst = bb.getInstructions().get(0);
        var endInst = bb.getInstructions().get(bb.getInstructions().size() - 1);
        sb.append("Basic block (occurr = ").append(id).append(", id = ").append(bb.getUniqueId());
        sb.append(" [").append(startPos).append("-").append(startPos + bb.getSegmentLength()).append("])\n");
        sb.append(startPos).append(": ").append(startInst.getString()).append("\n");
        sb.append("...\n");
        sb.append(endPos).append(": ").append(endInst.getString()).append("\n");
        return sb.toString();
    }
}
