package pt.up.fe.specs.binarytranslation.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;
import pt.up.fe.specs.util.SpecsLogs;

public class AnalysisUtils {
    private static HashMap<String, String> enumToSymbol;
    static {
        enumToSymbol = new HashMap<>();
        enumToSymbol.put("bslli", "<<");
        enumToSymbol.put("addk", "+");
        enumToSymbol.put("addik", "+");
        enumToSymbol.put("muli", "*");
        enumToSymbol.put("rsubk", "-");
        enumToSymbol.put("fmul", "*");
        enumToSymbol.put("fadd", "+");
        enumToSymbol.put("fsub", "-");
        enumToSymbol.put("cmp", "==");
        enumToSymbol.put("cmpu", "==");
        enumToSymbol.put("frsub", "-");
    }

    public static String mapInstructionsToSymbol(String elem) {
        if (enumToSymbol.containsKey(elem))
            return enumToSymbol.get(elem);
        else
            return elem;
    }

    /**
     * Prints an instruction with the values of its registers
     * 
     * @param inst
     *            the instruction to print
     * @param decimal
     *            true to print values in decimal; false to print in hexadecimal
     */
    public static void printInstructionWithRegisters(Instruction inst, boolean decimal) {
        StringBuilder sb = new StringBuilder();
        String space = "  ";

        sb.append(String.format("%-4s", inst.getName())).append(space);

        for (Operand op : inst.getData().getOperands()) {
            if (op.isRegister()) {
                String reg = op.getProperties().getPrefix() + op.getStringValue();
                Long val = inst.getRegisters().getValue(reg);
                String strVal = val == null ? "??" : (decimal ? String.valueOf(val) : String.format("0x%X", val));
                sb.append(String.format("%-16s", reg + "{" + strVal + "}")).append(space);
            }
            if (op.isImmediate()) {
                long imm = Long.parseLong(op.getStringValue(), 16);
                String strImm = decimal ? String.valueOf(imm) : String.format("0x%X", imm);
                sb.append(String.format("%-10s", strImm)).append(space);
            }
        }
        System.out.println(sb.toString());
    }

    public static String mapBitsetToRegisters(BitSet set, ArrayList<String> regs) {
        var registerList = new ArrayList<String>();
        for (int i = 0; i < set.length(); i++) {
            if (set.get(i)) {
                registerList.add(regs.get(i));
            }
        }
        return "{" + String.join(",", registerList) + "}";
    }

    public static String getRegisterName(Operand op) {
        return op.getProperties().getPrefix() + op.getStringValue();
    }

    public static void printSeparator(int size) {
        String s = "";
        for (int i = 0; i < size; i++)
            s += "-";
        System.out.println(s);
    }

    public static List<BinarySegment> getSegments(TraceInstructionStream stream, TraceBasicBlockDetector det) {
        SegmentBundle bun = det.detectSegments(stream);
        if (bun.getSegments().size() == 0) {
            System.out.println("No basic blocks were detected");
            return null;
        }

        System.out.println(bun.getSummary());
        return bun.getSegments();
    }

    public static ArrayList<String> findAllRegistersOfSeq(List<Instruction> insts) {
        ArrayList<String> lst = new ArrayList<>();

        for (Instruction i : insts) {
            for (Operand op : i.getData().getOperands()) {
                if (op.isRegister()) {
                    String reg = getRegisterName(op);
                    lst.add(reg);
                }
            }
        }
        Comparator<String> regCompare = (String r1, String r2) -> {
            r1 = r1.replaceAll("[^\\d.]", "");
            r2 = r2.replaceAll("[^\\d.]", "");
            int n1 = Integer.parseInt(r1);
            int n2 = Integer.parseInt(r2);
            return n1 > n2 ? 1 : -1;
        };
        // Remove duplicates
        var newList = lst.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
        // Sort array with custom comparator
        Collections.sort(newList, regCompare);
        return newList;
    }

    public static String hexToDec(String hex) {
        return "" + Long.decode(hex);
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static void saveAsCsv(StringBuilder sb, String filename) {
        var csv = new File(filename + ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csv))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            SpecsLogs.warn("Error message:\n", e);
        }
    }
}
