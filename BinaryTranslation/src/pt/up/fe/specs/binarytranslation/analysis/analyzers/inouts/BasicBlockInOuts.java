/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
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
