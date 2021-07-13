package pt.up.fe.specs.binarytranslation.analysis.analyzers.inouts;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class BasicBlockInOuts extends ASequenceInOuts {


    public BasicBlockInOuts(List<Instruction> insts) {
        super(insts);
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
}
