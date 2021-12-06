/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.binarytranslation.hardware.accelerators.custominstruction;

import pt.up.fe.specs.binarytranslation.graph.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.graph.GraphNode;
import pt.up.fe.specs.binarytranslation.hardware.HardwareArchitecture;
import pt.up.fe.specs.binarytranslation.hardware.generation.AHardwareGenerator;
import pt.up.fe.specs.binarytranslation.hardware.generation.visitors.InstructionASTConverter;
import pt.up.fe.specs.binarytranslation.hardware.tree.VerilogModuleTree;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysCombBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.OutputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.HardwareCommentNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.task.HardwareErrorMessage;
import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.PseudoInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.OperandASTNodeSide;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.ApplyInstructionPass;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.ApplySSAPass;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.MetaFieldFetcherPass;

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
        super();
        this.isClocked = false;
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
    public HardwareArchitecture generateHardware(BinarySegmentGraph graph) {

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
            moduletree.addPort(new InputPortDeclaration("clk", 1));
            moduletree.addPort(new InputPortDeclaration("rst", 1));
        }

        // top level graph liveins
        for (var livein : graph.getLiveins()) {
            var liveinname = livein.getRepresentation();
            // TODO: replace symbolic names?
            moduletree.addPort(new InputPortDeclaration(liveinname, livein.getWidth()));
        }

        // top level graph liveouts
        for (var liveout : graph.getLiveouts()) {
            var liveoutname = liveout.getRepresentation();
            // TODO: replace symbolic names?
            moduletree.addPort(new OutputPortDeclaration(liveoutname, liveout.getWidth()));
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

                // quick hack
                var mf = new MetaFieldFetcherPass(ast.getRootnode());
                var metafieldlist = mf.getMetaFields();
                for (var metafield : metafieldlist) {

                    /*
                     * Reminder: Any inheritor of @OperandASTNode
                     * can query if it is either LHS or RSH with
                     * getSide(), which returns @OperandASTNodeSide
                     */
                    var side = metafield.getSide();
                    var newport = (side == OperandASTNodeSide.LeftHandSide)
                            ? new InputPortDeclaration(metafield.getAsString(), 32)
                            : new OutputPortDeclaration(metafield.getAsString(), 32);

                    // TODO: to know the real width, we have to consult the @RegisterProperties
                    // of the respective source ISA; assuming 32 bits for now

                    moduletree.addPort(newport);

                }

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
}
