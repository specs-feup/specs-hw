package pt.up.fe.specs.binarytranslation.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionType;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.producer.detailed.DetailedRegisterInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class MemoryProfilerAnalyzer extends ATraceAnalyzer {

    private DetailedRegisterInstructionProducer prod;
    private Queue<Instruction> queue = new LinkedList<>();
    private ArrayList<InstructionType> loadstores = new ArrayList<>();
    {
        loadstores.add(InstructionType.G_LOAD);
        loadstores.add(InstructionType.G_STORE);
    }

    public MemoryProfilerAnalyzer(ATraceInstructionStream stream) {
        super(stream);
    }
    
    public MemoryProfilerAnalyzer(DetailedRegisterInstructionProducer prod) {
        super(null);
        this.prod = prod;
    }

    public boolean profileWithStream() {
        Instruction inst = stream.nextInstruction();
        if (inst == null)
            return false;
        
        while (inst != null) {
            if (!Collections.disjoint(inst.getData().getGenericTypes(), loadstores)) {
                queue.add(inst);
            }
            inst = stream.nextInstruction();
//            if (inst != null) {
//                if (inst.getRegisters().isEmpty())
//                    inst = stream.nextInstruction();
//            }
        }
        printResolvedTrace(true);

        var table = buildHistogram();

        printHistogram(table, false);
        return true;
    }

    public boolean profileWithProducer() {
        Instruction inst = prod.nextInstruction();
        if (inst == null)
            return false;
        
        while (inst != null) {
            if (!Collections.disjoint(inst.getData().getGenericTypes(), loadstores)) {
                queue.add(inst);
            }
            inst = prod.nextInstruction();
        }
        printResolvedTrace(true);

        var table = buildHistogram();

        printHistogram(table, false);
        return true;
    }
    
    private void printHistogram(HashMap<Long, Integer[]> table, boolean decimal) {
        List<Long> sortedAddr = new ArrayList<Long>(table.keySet());
        Collections.sort(sortedAddr);

        System.out.println("\nAddress  | Ld | St");
        System.out.println("-------------------");
        for (Long addr : sortedAddr) {
            Integer[] counts = table.get(addr);
            String strAddr = decimal ? String.format("%8s ", addr) : String.format("%08X", addr) + " ";
            String strCnt0 = String.format("%-4s", counts[0]);
            String strCnt1 = String.format("%-4s", counts[1]);

            System.out.println(strAddr + "|" + strCnt0 + "|" + strCnt1);
        }
        System.out.println("-------------------");
    }

    public void printResolvedTrace(boolean decimal) {
        System.out.println("Detected load/store operations");
        System.out.println("---------------------------------------");
        Queue<Instruction> newq = new LinkedList<>();
        Instruction inst = queue.poll();
        while (inst != null) {
            AnalysisUtils.printInstructionWithRegisters(inst, decimal);
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
            Integer[] counts = { 0, 0 };
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

    /**
     * Calculates the address of a load/store instruction Supports offsets from both other registers and immediate
     * values MicroBlaze-specific for now
     * 
     * @param inst
     *            a load/store instruction
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
