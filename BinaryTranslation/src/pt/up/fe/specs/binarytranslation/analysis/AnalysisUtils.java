package pt.up.fe.specs.binarytranslation.analysis;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.detection.segments.SegmentContext;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionType;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class AnalysisUtils {
    private static HashMap<String, String> enumToSymbol;
    static {
        enumToSymbol = new HashMap<>();
        enumToSymbol.put("bslli", "<<");
    }
    
    public static String mapEnum(String elem) {
        if (enumToSymbol.containsKey(elem))
            return enumToSymbol.get(elem);
        else return elem;
    }
    
    public static boolean isLoadStore(Instruction inst) {
        var types = inst.getData().getGenericTypes();
        var loadstores = new ArrayList<>();
        loadstores.add(InstructionType.G_LOAD);
        loadstores.add(InstructionType.G_STORE);

        return !Collections.disjoint(types, loadstores);
    }
    
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

    public static String getRegName(Operand op) {
        return op.getProperties().getPrefix() + op.getStringValue();
    }
    
    public static void printSeparator(int size) {
        String s = "";
        for (int i = 0; i < size; i++)
            s += "-";
        System.out.println(s);
    }
    
    public static List<BinarySegment> getSegments(ATraceInstructionStream stream, TraceBasicBlockDetector det) {
        SegmentBundle bun = det.detectSegments(stream);
        if (bun.getSegments().size() == 0) {
            System.out.println("No basic blocks were detected");
            return null;
        }
        
        System.out.println(bun.getSummary());
        return bun.getSegments();
    }
    
    public static String graphToDot(Graph<AddressVertex, DefaultEdge> graph) {
        DOTExporter<AddressVertex, DefaultEdge> exporter = new DOTExporter<>();
        exporter.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute(v.getLabel()));
            map.put("type", DefaultAttribute.createAttribute(v.getType().toString()));
            return map;
        });
        Writer writer = new StringWriter();
        exporter.exportGraph(graph, writer);
        return writer.toString();
    }

    public static ArrayList<String> findAllRegistersOfSeq(List<Instruction> insts) {
        ArrayList<String> lst = new ArrayList<>();
    
        for (Instruction i : insts) {
            for (Operand op : i.getData().getOperands()) {
                if (op.isRegister()) {
                    String reg = getRegName(op);
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
}
