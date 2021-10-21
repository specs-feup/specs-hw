/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.generator;

import java.util.*;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.graph.ControlFlowNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.graph.DataFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.control.ControlFlowNodeDecision;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.control.ControlFlowNodeMerge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.conditional.InstructionCDFGFalseEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.conditional.InstructionCDFGTrueEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseVisitor;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.*;

public class InstructionCDFGGenerator extends PseudoInstructionBaseVisitor<GeneralFlowGraph<?,AInstructionCDFGEdge>>{

    private InstructionCDFG icdfg; 
    
    private InstructionCDFGDataFlowGraphGenerator dfg_visitor;
    
    private GeneralFlowGraph<?, AInstructionCDFGEdge> last_node;
    private AInstructionCDFGEdge last_node_edge;

    private Map<String, Integer> uid_map;

    public InstructionCDFGGenerator() {
        this.dfg_visitor = new InstructionCDFGDataFlowGraphGenerator();
        this.uid_map = new HashMap<>();
    }
    
    public static void generateUID(Map<String, Integer> uid_map, AInstructionCDFGNode vertex) {
        
        uid_map.put(vertex.getReference(), (uid_map.containsKey(vertex.getReference())) ? uid_map.get(vertex.getReference()) + 1 : 0);
 
        vertex.setUID(String.valueOf(uid_map.get(vertex.getReference())));
    }
    
    public InstructionCDFG generate(Instruction instruction) {
        return this.generate(instruction.getPseudocode().getParseTree());
    }
    
    public InstructionCDFG generate(PseudoInstructionContext instruction) {
        
        this.icdfg = new InstructionCDFG();
        
        this.visit(instruction);

        return this.icdfg;
    }
    
    public DataFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> generateDataFlowGraph(List<PseudoInstructionParser.StatementContext>  ctx){
       
        DataFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> new_dfg = this.dfg_visitor.generate(this.uid_map, ctx);
        this.uid_map = this.dfg_visitor.getUIDMap();
        this.icdfg.addVertex(new_dfg);
        
        return new_dfg; 
    }
    
    @Override
    public GeneralFlowGraph<?, AInstructionCDFGEdge> visitPseudoInstruction(PseudoInstructionContext ctx) {     
        
      ctx.statement().forEach((statement) -> {this.visit(statement);});
        
       return null;
    }
    
    public void addPreviousStatementEdge(GeneralFlowGraph<?, AInstructionCDFGEdge> new_statement) {
        if(this.last_node != null) {
            this.icdfg.addEdge(this.last_node, new_statement, this.last_node_edge);
            this.last_node = null;
        }
    }
    
    @Override
    public GeneralFlowGraph<?, AInstructionCDFGEdge> visitStatementlist(StatementlistContext ctx) {

        GeneralFlowGraph<?, AInstructionCDFGEdge> return_graph = null;
        GeneralFlowGraph<?, AInstructionCDFGEdge> new_dfg = null;
        
        List<PseudoInstructionParser.StatementContext> ctx_sub = new ArrayList<>();
        
        for(PseudoInstructionParser.StatementContext statement : ctx.statement()) {

            if((statement instanceof IfStatementContext) || (statement instanceof IfElseStatementContext)) {
                
                if(!ctx_sub.isEmpty()) {
                   
                    this.generateDataFlowGraph(ctx_sub);
                    this.addPreviousStatementEdge(new_dfg);
                    ctx_sub.clear();
                }
                
                return_graph = this.visit(statement); 
                this.addPreviousStatementEdge(return_graph);
               
            }else {
                ctx_sub.add(statement);
            }
        }

        if(!ctx_sub.isEmpty()) {
            
            new_dfg = this.generateDataFlowGraph(ctx_sub);

            if(return_graph != null) {
                this.icdfg.addEdge(return_graph, new_dfg);
            }
            
            this.addPreviousStatementEdge(new_dfg);
            
            return new_dfg;
        }
        
        return return_graph;
    }
    
    @Override
    public GeneralFlowGraph<?, AInstructionCDFGEdge> visitIfStatement(IfStatementContext ctx) {
        
        DataFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> condition_expression = this.dfg_visitor.generate(this.uid_map, ctx.condition);
        this.uid_map = this.dfg_visitor.getUIDMap();
        this.icdfg.addVertex(condition_expression);
        
        this.addPreviousStatementEdge(condition_expression);
        
        ControlFlowNode<AInstructionCDFGNode, AInstructionCDFGEdge> decision = new ControlFlowNodeDecision();
        generateUID(this.uid_map, decision.getVertex());
        this.icdfg.addVertex(decision);
        
        decision.addControlInput(condition_expression.getFirstOutput());

        ControlFlowNode<AInstructionCDFGNode, AInstructionCDFGEdge>merge = new ControlFlowNodeMerge();
        generateUID(this.uid_map, merge.getVertex());
        
        GeneralFlowGraph<?, AInstructionCDFGEdge> path_true = this.visitConditionalPathStatements(decision, true, ctx.ifsats);
 
        this.icdfg.addVertexes(path_true, merge);
        this.icdfg.addEdge(condition_expression, decision);
        
        //this.icdfg.addControlEdgesTo(decision, path_true, merge);
        this.icdfg.addEdge(decision, merge, new InstructionCDFGFalseEdge());
        this.icdfg.addIncomingEdgesTo(merge, path_true);
        
        return merge;
    }
    
   
    
    @Override
    public GeneralFlowGraph<?, AInstructionCDFGEdge> visitIfElseStatement(IfElseStatementContext ctx) {
        
        DataFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> condition_expression = this.dfg_visitor.generate(this.uid_map, ctx.condition);
        this.uid_map = this.dfg_visitor.getUIDMap();
        this.icdfg.addVertex(condition_expression);
        
        this.addPreviousStatementEdge(condition_expression);
        
        ControlFlowNode<AInstructionCDFGNode,AInstructionCDFGEdge> decision = new ControlFlowNodeDecision();
        generateUID(this.uid_map, decision.getVertex());
        decision.addControlInput(condition_expression.getFirstOutput());
        this.icdfg.addVertex(decision);
        
        ControlFlowNode<AInstructionCDFGNode, AInstructionCDFGEdge> merge = new ControlFlowNodeMerge();
        generateUID(this.uid_map, merge.getVertex());
        
        GeneralFlowGraph<?, AInstructionCDFGEdge> path_true = this.visitConditionalPathStatements(decision, true, ctx.ifsats);
        GeneralFlowGraph<?, AInstructionCDFGEdge> path_false = this.visitConditionalPathStatements(decision, false, ctx.elsestats);

        this.icdfg.addVertexes(path_true, path_false, merge);
        this.icdfg.addEdge(condition_expression, decision);
        //this.icdfg.addControlEdgesTo(decision, path_true, path_false);
        this.icdfg.addIncomingEdgesTo(merge, path_true, path_false);
        
        return merge;
    }
        
    @Override
    public GeneralFlowGraph<?, AInstructionCDFGEdge> visitPlainStmt(PlainStmtContext ctx) {
        
        DataFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> dfg = this.dfg_visitor.generate(this.uid_map, ctx);
        
        this.icdfg.addVertex(dfg);
        
        return dfg;
    }
    
    public GeneralFlowGraph<?, AInstructionCDFGEdge> visitConditionalPathStatements(ControlFlowNode<AInstructionCDFGNode, AInstructionCDFGEdge> decision, boolean condition, StatementlistContext ctx){
        this.last_node = decision;
        this.last_node_edge = (condition) ? new InstructionCDFGTrueEdge() : new InstructionCDFGFalseEdge();
        return this.visitStatementlist(ctx);
    }
    
}
