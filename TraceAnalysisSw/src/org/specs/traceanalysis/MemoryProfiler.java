package org.specs.traceanalysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionType;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.producer.detailed.DetailedRegisterInstructionProducer;

public class MemoryProfiler {

    private DetailedRegisterInstructionProducer provider;
    private Queue<Instruction> queue = new LinkedList<>();
    private ArrayList<InstructionType> loadstores = new ArrayList<>();
    {
        loadstores.add(InstructionType.G_LOAD);
        loadstores.add(InstructionType.G_STORE);
    }

    public MemoryProfiler(DetailedRegisterInstructionProducer provider) {
        this.provider = provider;
    }

    public void profile() {
        Instruction inst = provider.nextInstruction();

        while (inst != null) {
            if (!Collections.disjoint(inst.getData().getGenericTypes(), loadstores)) {
                queue.add(inst);
            }
            inst = provider.nextInstruction();
        }
        prettyPrint();
    }

    public void prettyPrint() {
        Instruction inst = queue.poll();
        while (inst != null) {
            //System.out.println(inst.getData().getOperands());
            printInstWithRegs(inst);
            inst = queue.poll();
        }
    }
    
    private void printInstWithRegs(Instruction inst) {
        StringBuilder sb = new StringBuilder();
        sb.append(inst.getRepresentation()).append("|  ");
        
        for (Operand op : inst.getData().getOperands()) {
            if (op.isRegister()) {
                String reg = "r" + op.getStringValue();
                Long res = inst.getRegisters().getValue(reg);
                String regVal = res == null ? "??" : res.toString();
                sb.append(reg).append("{").append(regVal).append("} ");
            }
        }
        System.out.println(sb.toString());
    }
}
