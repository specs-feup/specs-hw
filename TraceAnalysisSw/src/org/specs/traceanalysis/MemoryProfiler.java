package org.specs.traceanalysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.stream.Collectors;

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
        printResolvedTrace(true);

        var table = buildHistogram();
        
        printHistogram(table, false);
    }

    private void printHistogram(HashMap<Long, Integer[]> table, boolean decimal) {
        List<Long> sortedAddr = new ArrayList<Long>(table.keySet());
        Collections.sort(sortedAddr);

        System.out.println("\nAddress  | Ld | St");
        System.out.println("-------------------");
        for (Long addr : sortedAddr) {
            Integer[] counts = table.get(addr);
            String strAddr = decimal ? String.format("%8s ", addr) 
                             : stripZeros(String.format("%08X", addr)) + " ";
            String strCnt0 = String.format("%-4s", counts[0]);
            String strCnt1 = String.format("%-4s", counts[1]);
            
            System.out.println(strAddr + "|" + strCnt0 + "|" + strCnt1);
        }
        System.out.println("-------------------");
    }
    
    private String stripZeros(String addr) {
        char[] arr = addr.toCharArray();
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] == '0') {
                arr[i] = ' ';
            }
            else break;
        }
        return String.valueOf(arr);
    }

    public void printResolvedTrace(boolean decimal) {
        System.out.println("Detected load/store operations");
        System.out.println("---------------------------------------");
        Queue<Instruction> newq = new LinkedList<>();
        Instruction inst = queue.poll();
        while (inst != null) {
            printInstructionWithRegisters(inst, decimal);
            newq.add(inst);
            inst = queue.poll();
        }
        System.out.println("---------------------------------------");
        this.queue = newq;
    }
    
    private HashMap<Long, Integer[]> buildHistogram() {
        HashMap<Long, Integer[]> table = new HashMap<>();
        Instruction inst = queue.poll();
        while (inst != null) {
            Long addr = getMemoryAddr(inst);
            Integer[] counts = {0, 0};
            if (table.containsKey(addr))
                counts = table.get(addr);

            if (inst.getData().getGenericTypes().contains(InstructionType.G_LOAD))
                counts[0]++;
            else
                counts[1]++;
            table.put(addr, counts);
            inst = queue.poll();
        }
        return table;
        
    }

    private void printInstructionWithRegisters(Instruction inst, boolean decimal) {
        StringBuilder sb = new StringBuilder();
        String space = "  ";

        sb.append(String.format("%-4s", inst.getName())).append(space);
        
        for (Operand op : inst.getData().getOperands()) {
            if (op.isRegister()) {
                String reg = op.getProperties().getPrefix() + op.getStringValue();
                Long val = inst.getRegisters().getValue(reg);
                String strVal = val == null ? "??" : (decimal ? String.valueOf(val) : String.format("0x%08X", val));
                sb.append(String.format("%-16s", reg + "{" + strVal + "}")).append(space);
            }
            if (op.isImmediate()) {
                long imm = Long.parseLong(op.getStringValue(), 16);
                String strImm = decimal ? String.valueOf(imm) : String.format("0x%08X", imm);
                sb.append(String.format("%-10s", strImm)).append(space);
            }
        }
        System.out.println(sb.toString());
    }
    
    /**
     * Calculates the address of a load/store instruction
     * Supports offsets from both other registers and immediate values
     * MicroBlaze-specific for now
     * @param inst a load/store instruction
     * @return the memory address being used by the instruction
     */
    private Long getMemoryAddr(Instruction inst) {
        Operand op = inst.getData().getOperands().get(1);
        long res = inst.getRegisters().getValue(op.getProperties().getPrefix() + op.getStringValue());
        
        if (inst.getData().getOperands().size() == 3) {
            op = inst.getData().getOperands().get(2);
            long add = 0;
            if (op.isRegister()) {
                add = inst.getRegisters().getValue(op.getProperties().getPrefix() + op.getStringValue());
            }
            if (op.isImmediate()) {
                add = Long.parseLong(op.getStringValue(), 16);
            }
            res += add;
        }
        return res;
    }
}
