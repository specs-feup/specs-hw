package pt.up.fe.specs.binarytranslation.analysis.inouts;

import java.util.ArrayList;
import java.util.BitSet;

import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;

public class BasicBlockInOuts extends ASequenceInOuts {

    private BinarySegment seg;

    public BasicBlockInOuts(BinarySegment seg) {
        super(seg.getInstructions());
        this.seg = seg;
    }

    public BitSet calculateBBOuts(ArrayList<InstructionSets> sets) {
        BitSet outs = new BitSet(regs.size());
        for (InstructionSets s : sets) {
            BitSet out = s.getDefSet();
            outs.or(out);
        }
        return outs;
    }

    @Override
    public void printResult() {
      StringBuilder sb = new StringBuilder();
      
      sb.append("\nBasic Block In/Outs:\n");
      sb.append("In: ");
      for (int i = 0; i < sets.get(0).getInSet().length(); i++) {
          if (sets.get(0).getInSet().get(i))
              sb.append(regs.get(i) + " ");
      }
      
      sb.append("\nOut: ");
      BitSet outs = calculateBBOuts(sets);
      for (int i = 0; i < outs.length(); i++) {
          if (outs.get(i))
              sb.append(regs.get(i) + " ");
      }
      
      sb.append("\n");
      System.out.println(sb.toString());
    }

    public BinarySegment getSeg() {
        return seg;
    }

    public void setSeg(BinarySegment seg) {
        this.seg = seg;
    }
}
