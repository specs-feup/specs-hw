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

import java.util.BitSet;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class SimpleBasicBlockInOuts extends ASequenceInOuts {
    private InstructionSets inouts;

    public SimpleBasicBlockInOuts(List<Instruction> insts) {
        super(insts);
    }
    
    @Override
    public void calculateInOuts() {
        inouts = new InstructionSets(regs);

        Instruction last = insts.get(insts.size() - 1);
        InstructionSets usedef = new InstructionSets(regs);
        findUseDefs(last, usedef);
        
        BitSet use = usedef.getUseSet();
        BitSet def = usedef.getDefSet();
        BitSet out = def;
        BitSet in = use;
        //System.out.println(AnalysisUtils.printSet(in, regs) + "  " + AnalysisUtils.printSet(out, regs));
        
        for (int i = insts.size() - 2; i >= 0; i--) {
            Instruction inst = insts.get(i);
            InstructionSets tmp = new InstructionSets(regs);
            findUseDefs(inst, tmp);
            
            out.or(tmp.getDefSet());
            
            in.andNot(tmp.getDefSet());
            in.or(tmp.getUseSet());
            //System.out.println(AnalysisUtils.printSet(in, regs) + "  " + AnalysisUtils.printSet(out, regs));
        }
        inouts.setOutSet(out);
        inouts.setInSet(in);
    }

    @Override
    public void printResult() {
        StringBuilder sb = new StringBuilder();
        BitSet in = inouts.getInSet();
        BitSet out = inouts.getOutSet();
        
        for (Instruction i : insts) {
            sb.append(i.getString()).append("\n");
        }
        
        sb.append("\nBasic Block In/Outs:\n");
        sb.append("In: ");
        for (int i = 0; i < in.length(); i++) {
            if (in.get(i))
                sb.append(regs.get(i) + " ");
        }
        
        sb.append("\nOut: ");
        for (int i = 0; i < out.length(); i++) {
            if (out.get(i))
                sb.append(regs.get(i) + " ");
        }
        System.out.println(sb.toString());
    }

    public InstructionSets getInouts() {
        return inouts;
    }

}
