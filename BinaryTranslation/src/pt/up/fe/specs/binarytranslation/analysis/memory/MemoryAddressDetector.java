package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class MemoryAddressDetector extends APropertyDetector {
    public MemoryAddressDetector(BinarySegment bb, List<Instruction> insts) {
        super(bb, insts);
    }
    
    public void printOccurrenceRegisters() {
        for (var o : tracker.getOccurrences()) {
            var regs = o.getRegisters();
            regs.prettyPrint();
            AnalysisUtils.printSeparator(40);
        }
    }
    
    public ArrayList<Graph<AddressVertex, DefaultEdge>> detectGraphs() {
        var out = new ArrayList<Graph<AddressVertex, DefaultEdge>>();
        
        for (var i : tracker.getBasicBlock().getInstructions()) {
            if (AnalysisUtils.isLoadStore(i)) {
                var builder = new AddressGraphBuilder(tracker.getBasicBlock().getInstructions(), i);
                var graph = builder.calculateChain();
                out.add(graph);
            }
        }
        return out;
    }
}
