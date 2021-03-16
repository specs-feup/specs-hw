package pt.up.fe.specs.binarytranslation.analysis;

import java.util.ArrayList;
import java.util.BitSet;

import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.detection.segments.SegmentContext;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class AnalysisUtils {
    
    /**
     * Prints an instruction with the values of its registers
     * @param inst the instruction to print
     * @param decimal true to print values in decimal; false to print in hexadecimal
     */
    public static void printInstructionWithRegisters(Instruction inst, boolean decimal) {
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
    
    public static void printSegmentWithRegisters(BinarySegment seg) {
        SegmentContext con = seg.getContexts().get(0);
        System.out.println(con.getContextMap());
        for (var inst : seg.getInstructions()) {
            printInstructionWithRegisters(inst, false);
        }
    }
    
    public static String printSet(BitSet set, ArrayList<String> regs) {
        var regsToPrint = new ArrayList<String>();
        for (int i = 0; i < set.length(); i++) {
            if (set.get(i)) {
                regsToPrint.add(regs.get(i));
            }
        }
        return "{" + String.join(",", regsToPrint) + "}";
    }
}
