package pt.up.fe.specs.binarytranslation.hardware.accelerators.custominstruction;

import pt.up.fe.specs.binarytranslation.graphs.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.graphs.GraphNode;
import pt.up.fe.specs.binarytranslation.graphs.edge.GraphInput;
import pt.up.fe.specs.binarytranslation.graphs.edge.GraphOutput;
import pt.up.fe.specs.binarytranslation.hardware.HardwareInstance;
import pt.up.fe.specs.binarytranslation.hardware.generation.AHardwareGenerator;
import pt.up.fe.specs.binarytranslation.hardware.generation.visitors.InstructionASTConverter;
import pt.up.fe.specs.binarytranslation.hardware.tree.VerilogModuleTree;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysCombBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModulePortDirection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.PortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.HardwareCommentNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.HardwareErrorMessage;
import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.PseudoInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.ApplyInstructionPass;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.ApplySSAPass;

/**
 * Generates a single dedicated verilog module for a single binary segment
 * 
 * @author nuno
 *
 */
public class CustomInstructionUnitGenerator extends AHardwareGenerator {

    // todo pass parameter to constructor of generator?
    // e.g. float enable, max latency, max units, etc?

    private Boolean isClocked;
    private int numStages = 1;

    public CustomInstructionUnitGenerator() {
        // TODO pass options to constructor
        // like generation hints

        // e.g.,
        // 1. number of register stages
        // 2. insert handshaking logic
        // 3. insert halt signal
    }

    public CustomInstructionUnitGenerator(Boolean isClocked) {
        this();
        this.isClocked = isClocked;
    }

    // TODO: exception here if graph type is different than a frequent sequence! (static or dynamic)

    // NOTE: graph is necessary instead of segment so we can make
    // good use of blocking and non blocking statements
    // in a combinatorial Verilog block, by relying on node levels

    // TODO: need to combine several AST, potentially after generation of each, since
    // the instruction information needs to be applied...
    // After applying each instruction, I just need to attach the ASTs to eachother, last to first (last operation is
    // topmost on the tree)
    // by finding the ExpressionASTNode whose target matches the operands in the upper subtrees
    // then it should be possible to walk an entire combined AST to generate verilog!

    @Override
    public HardwareInstance generateHardware(BinarySegmentGraph graph) {

        // The VerilogModuleTree
        var name = graph.getSegment().getSegmentType().toString().toLowerCase() + "_" + graph.hashCode();
        var moduletree = new VerilogModuleTree(name);
        var module = moduletree.getModule();

        // print segment text before module declaration
        module.getParent().addChildLeftOf(module, new HardwareCommentNode(graph.getSegment().getRepresentation()));

        /*
         * Input and output ports
         * Inputs may include IMM edges, if IMM has different contexts
         */

        // clk and rst ports if module is registered
        if (this.isClocked) {
            moduletree.addDeclaration(new PortDeclaration("clk", 1, ModulePortDirection.input));
            moduletree.addDeclaration(new PortDeclaration("rst", 1, ModulePortDirection.input));
        }

        // top level graph liveins
        for (GraphInput n : graph.getLiveins()) {
            moduletree.addDeclaration(PortDeclaration.newInputPort(n));
        }

        // top level graph liveouts
        for (GraphOutput n : graph.getLiveouts()) {
            moduletree.addDeclaration(PortDeclaration.newOutputPort(n));
        }

        /*
         * Convert the nodes, from top level downwards   
         */
        var ssaApplier = new ApplySSAPass();
        ;
        for (int i = 0; i < graph.getCpl(); i++) {

            int level = i;
            HardwareNode parentBlock = null;
            var nodes = graph.getNodes(data -> data.getLevel() == level);

            // add comment
            module.addChild(new HardwareCommentNode("level " + i + " of graph (" + nodes.size() + " nodes)"));

            // Use always comb block if multiple nodes on this level, otherwise a single statement
            if (nodes.size() > 1) {
                parentBlock = new AlwaysCombBlock();
                module.addChild(parentBlock);

            } else {
                parentBlock = module;
            }

            // TODO: when to use Always_ff block??

            // transform all nodes
            for (GraphNode n : graph.getNodes(data -> data.getLevel() == level)) {

                // get AST of instruction
                var ast = new InstructionAST(n.getInst());

                // Apply first pass (attaches executed instruction information to the AST of the instruction)
                ast.accept(new ApplyInstructionPass());

                // Single static assignment pass!!
                ast.accept(ssaApplier);
                // TODO every time this runs, new wires may be declared
                // go through the map, and add the declarations at the top

                // add comment
                parentBlock.addChild(new HardwareCommentNode("implementation for node " + n.getRepresentation()
                        + ", instruction is : " + n.getInst().getRepresentation()));

                // new converter
                var converter = new InstructionASTConverter();

                // convert all statements to Verilog code components
                if (ast.getRootnode().getType() == InstructionASTNodeType.PseudoInstructionNode)
                    parentBlock.addChild(
                            converter.convertInstruction(parentBlock, (PseudoInstructionASTNode) ast.getRootnode()));

                else
                    parentBlock.addChild(
                            new HardwareErrorMessage(
                                    "Couldn't convert this instruction: " + n.getInst().getRepresentation()));
            }
        }

        /*
        // node level constants (which might have different contexts?)
        for (GraphNode n : graph.getNodes()) {
            for (GraphEdge ed : n.getEdges()) {
                // if(ed.isImmediate())
            }
            // TODO: need a way to have a list of context values per edge...
            // right now I only have a list of contexts, where each one has a map of contexts
        }
        */

        /////////////////////////////////////////////////////
        /*
        // The list of components
        var components = new ArrayList<HardwareComponent>();
        components.add(new ModuleHeader(graph));
        
        // module declaration
        var segtype = graph.getSegment().getSegmentType().toString().toLowerCase();
        var id = graph.getSegment().getUniqueId();
        components.add(new PlainCode("module " + segtype + "_" + id + ";"));
        
        // ports
        components.addAll(ports);
        
        // body
        // TODO: THIS IS THE HARDEST PART!!
        for (var node : graph.getNodes()) {
            // transform(n, predicate?)
        }
        
        // NOTE: the binarysegmentgraph is already a form of SSA, since i rename
        // the edges (graphinput or graphoutput class objects) based on their producers
        
        // TODO:
        // if I do a getEdges on a graph, then create all verilog identifiers, with name based on the edge name, and
        // type based on livein/liveout or internal, then i have the set of verilog variables (?)
        // conclusion: this is one valid approach, approach 2
        // the first one is to do the transformation of the BInarySegmentGraph into an AST, and walk it...
        
        // new begin-end combinatorial block
        // components.add(new StatementBlock(statements));
        
        // end module
        components.add(new PlainCode("endmodule; //" + segtype + "_" + id + "\n"));
        */

        return new CustomInstructionUnit(graph, Integer.toString(graph.hashCode()), moduletree);
    }

    /*
    private static final Map<GraphEdgeType, String> PREFIXNAMES;
    static {
        Map<GraphEdgeType, String> aMap = new LinkedHashMap<GraphEdgeType, String>();
        aMap.put(GraphEdgeType.livein, "in");
        aMap.put(GraphEdgeType.liveout, "out");
        aMap.put(GraphEdgeType.immediate, "imm");
        aMap.put(GraphEdgeType.noderesult, "w");
        aMap.put(GraphEdgeType.control, "en");
        PREFIXNAMES = Collections.unmodifiableMap(aMap);
    }
    
    @Override
    public HardwareInstance generateHardware(BinarySegmentGraph graph) {
    
    
        /*
         * Map of counters per edge type
         
        Map<GraphEdgeType, Integer> counters = new LinkedHashMap<GraphEdgeType, Integer>();
        for (GraphEdgeType type : GraphEdgeType.values()) {
            counters.put(type, 0);
        }
    
        /*
         * Map to remap names of edges into HW signal names
         
        Map<GraphEdge, String> renameMap = new LinkedHashMap<GraphEdge, String>();
    
        for (GraphNode n : graph.getNodes()) {
            for (GraphEdge ed : n.getEdges()) {
                if (!renameMap.containsKey(ed)) {
                    var type = ed.getType();
                    int nr = counters.get(type);
                    renameMap.put(ed, PREFIXNAMES.get(type) + Integer.toString(nr));
                    counters.put(type, nr + 1);
                }
            }
        }
    
        // all lines of code
        List<String> code = new ArrayList<String>();
    
        // put declaration
        int uniqueid = graph.getSegment().getUniqueId();
        String segtype = graph.getSegment().getSegmentType().toString().toLowerCase();
        code.add("module " + segtype + "_" + uniqueid + ";\n\n");
    
        // inputs
        for (GraphInput gi : graph.getLiveins()) {
            code.add("\tinput [" + gi.getWidth()
                    + "-1:0" + "] " + renameMap.get(gi)
                    + ";\t//" + gi.getRepresentation() + "\n");
        }
        code.add("\n");
    
        // outputs
        for (GraphOutput go : graph.getLiveouts()) {
            code.add("\toutput [" + go.getWidth()
                    + "-1:0" + "] " + renameMap.get(go)
                    + ";\t//" + go.getRepresentation() + "\n");
        }
        code.add("\n");
    
        // if number of contexts is 1, then imms
        // can be a static value, otherwise they are inputs
        var sz = graph.getSegment().getContexts().size();
        String datatype = (sz == 1) ? "localparam" : "input";
        for (GraphNode n : graph.getNodes()) {
            for (GraphEdge ed : n.getEdges()) {
                if (ed.getType() == GraphEdgeType.immediate) {
                    code.add("\t" + datatype + "[" + go.getWidth()
                            + "-1:0" + "] " + renameMap.get(ed)
                            + ";\t//" + ed.getRepresentation() + "\n");
                }
            }
        }
    
        // put body
        // TODO a way to transform generic algorithm erxpressions into code...
        // i might need to transform segmenst into graphs first after all...
        // for (Instruction i : segment.getInstructions()) {
        // String line = "assign " +
        // ??
        // }
    
        // end module
        code.add("endmodule; //" + segtype + "_" + uniqueid + "\n\n");
    
        return new CustomInstructionUnit(graph, code);
    }
    */
}
