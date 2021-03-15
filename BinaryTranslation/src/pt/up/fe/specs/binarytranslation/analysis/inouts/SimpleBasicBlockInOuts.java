package pt.up.fe.specs.binarytranslation.analysis.inouts;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class SimpleBasicBlockInOuts extends ASequenceInOuts {

    private BinarySegment basicBlock;
    private InstructionSets inouts;

    public SimpleBasicBlockInOuts(BinarySegment seg) {
        super(seg.getInstructions());
        this.basicBlock = seg;
    }
    
    @Override
    public void calculateInOuts() {
        inouts = new InstructionSets(regs);

        Instruction last = insts.get(insts.size() - 1);
        InstructionSets usedef = new InstructionSets(regs);
        findUseDefs(last, usedef);
        
        BitSet out = usedef.getDefSet();
        BitSet in = usedef.getUseSet();
        
        for (int i = insts.size() - 2; i >= 0; i--) {
            Instruction inst = insts.get(i);
            findUseDefs(inst, usedef);
            
            out.or(usedef.getDefSet());
            
            in.andNot(usedef.getDefSet());
            in.or(usedef.getUseSet());
        }
        inouts.setOutSet(out);
        inouts.setInSet(in);
    }

    @Override
    public void printResult() {
        StringBuilder sb = new StringBuilder();
        BitSet in = inouts.getInSet();
        BitSet out = inouts.getOutSet();
        
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

}
