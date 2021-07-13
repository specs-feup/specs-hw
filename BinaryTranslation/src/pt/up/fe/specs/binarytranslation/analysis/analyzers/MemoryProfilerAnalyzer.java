package pt.up.fe.specs.binarytranslation.analysis.analyzers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionType;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.producer.detailed.DetailedRegisterInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

@Deprecated
public class MemoryProfilerAnalyzer extends ABasicBlockAnalyzer {

    private DetailedRegisterInstructionProducer prod;
    private Queue<Instruction> queue = new LinkedList<>();

    public MemoryProfilerAnalyzer(ATraceInstructionStream stream, ELFProvider elf, int window) {
        super(stream, elf, window);
    }

//    public boolean profile(boolean useStream) {
//        Instruction inst = nextInstruction(useStream);
//        if (inst == null)
//            return false;
//        
//        while (inst != null) {
//            if (inst.isMemory()) {
//                queue.add(inst);
//            }
//            inst = nextInstruction(useStream);
//        }
//        printResolvedTrace(true);
//
//        var table = buildHistogram();
//
//        printHistogram(table, false);
//        return true;
//    }
    
//    public void printLoadStoreInstructions() {
//        Instruction inst = stream.nextInstruction();
//        while (inst != null) {
//             System.out.println(inst.getRegisters().isEmpty());
//             inst = stream.nextInstruction();
//        }
//    }
    
//    public Instruction nextInstruction(boolean useStream) {
//        if (useStream)
//            return stream.nextInstruction();
//        else
//            return prod.nextInstruction();
//    }
    
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
        long res = inst.getRegisters().getValue(AnalysisUtils.getRegisterName(op));

        if (inst.getData().getOperands().size() == 3) {
            op = inst.getData().getOperands().get(2);
            long add = 0;
            if (op.isRegister()) {
                add = inst.getRegisters().getValue(AnalysisUtils.getRegisterName(op));
            }
            if (op.isImmediate()) {
                add = Long.parseLong(op.getStringValue(), 16);
            }
            res += add;
        }
        return res;
    }
}
