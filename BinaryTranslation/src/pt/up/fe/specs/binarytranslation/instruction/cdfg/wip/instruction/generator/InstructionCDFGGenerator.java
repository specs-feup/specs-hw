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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.graph.ControlFlowNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.graph.DataFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.control.ControlFlowNodeDecision;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.control.ControlFlowNodeMerge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseVisitor;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfElseStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;

public class InstructionCDFGGenerator extends PseudoInstructionBaseVisitor<GeneralFlowGraph<?,DefaultEdge>>{

    private InstructionCDFG icdfg; 
    
    private InstructionCDFGDataFlowGraphGenerator dfg_visitor;
    
    private Stack<GeneralFlowGraph<?, DefaultEdge>> recursion_stack;
    
    private Map<String, Integer> uid_map;

    public InstructionCDFGGenerator() {
        this.dfg_visitor = new InstructionCDFGDataFlowGraphGenerator();
        this.uid_map = new HashMap<>();
        
        this.recursion_stack = new Stack<>();
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
    
    public DataFlowGraph<AInstructionCDFGNode, DefaultEdge> generateDataFlowGraph(List<PseudoInstructionParser.StatementContext>  ctx){
       
        DataFlowGraph<AInstructionCDFGNode, DefaultEdge> new_dfg = this.dfg_visitor.generate(this.uid_map, ctx);
        this.uid_map = this.dfg_visitor.getUIDMap();
        this.icdfg.addVertex(new_dfg);
        return new_dfg;
        
    }
    
    public GeneralFlowGraph<?, DefaultEdge> visitStatementList(List<PseudoInstructionParser.StatementContext> ctx){

        GeneralFlowGraph<?, DefaultEdge> return_graph = null;
        GeneralFlowGraph<?, DefaultEdge> new_dfg = null;
        
        List<PseudoInstructionParser.StatementContext> ctx_sub = new ArrayList<>();
        
        
        
        for(PseudoInstructionParser.StatementContext statement : ctx) {

            if((statement instanceof IfStatementContext) || (statement instanceof IfElseStatementContext)) {
                
                if(!ctx_sub.isEmpty()) {
                    
                    this.generateDataFlowGraph(ctx_sub);

                    //this.icdfg.addEdge(this.recursion_stack.pop(),new_dfg);
                    ctx_sub.clear();
                   // this.recursion_stack.push(new_dfg);
                }
                
                return_graph = this.visit(statement); 
               
            }else {
                ctx_sub.add(statement);
            }
        }

        if(!ctx_sub.isEmpty()) {
            
            new_dfg = this.generateDataFlowGraph(ctx_sub);
            
            //this.recursion_stack.push(new_dfg);
            
            if(return_graph != null) {
                this.icdfg.addEdge(return_graph, new_dfg);
            }
            
            return new_dfg;
        }
        
        return return_graph;
    }  
    
    @Override
    public GeneralFlowGraph<?, DefaultEdge> visitPseudoInstruction(PseudoInstructionContext ctx) {         
       return this.visitStatementList(ctx.statement());

    }
    
    @Override
    public GeneralFlowGraph<?, DefaultEdge> visitIfStatement(IfStatementContext ctx) {
        
        DataFlowGraph<AInstructionCDFGNode, DefaultEdge> condition_expression = this.dfg_visitor.generate(this.uid_map, ctx.condition);
        this.uid_map = this.dfg_visitor.getUIDMap();
        this.recursion_stack.push(condition_expression);
        
        ControlFlowNode<AInstructionCDFGNode> decision = new ControlFlowNodeDecision();
        generateUID(this.uid_map, decision.getVertex());
        decision.addControlInput(condition_expression.getFirstOutput());

        ControlFlowNode<AInstructionCDFGNode> merge = new ControlFlowNodeMerge();
        generateUID(this.uid_map, merge.getVertex());
        
        GeneralFlowGraph<?, DefaultEdge> path_true = this.visitStatementList(ctx.ifsats);
 
        this.icdfg.addVertexes(condition_expression, decision, path_true, merge);
        this.icdfg.addEdge(condition_expression, decision);
        this.icdfg.addControlEdgesTo(decision, path_true, merge);
        this.icdfg.addIncomingEdgesTo(merge, path_true);
        
        
        
        return merge;
    }
    
    @Override
    public GeneralFlowGraph<?, DefaultEdge> visitIfElseStatement(IfElseStatementContext ctx) {
        
        DataFlowGraph<AInstructionCDFGNode, DefaultEdge> condition_expression = this.dfg_visitor.generate(this.uid_map, ctx.condition);
        this.uid_map = this.dfg_visitor.getUIDMap();
        this.recursion_stack.push(condition_expression);
        
        ControlFlowNode<AInstructionCDFGNode> decision = new ControlFlowNodeDecision();
        generateUID(this.uid_map, decision.getVertex());
        decision.addControlInput(condition_expression.getFirstOutput());
        
        ControlFlowNode<AInstructionCDFGNode> merge = new ControlFlowNodeMerge();
        generateUID(this.uid_map, merge.getVertex());
        
        GeneralFlowGraph<?, DefaultEdge> path_true = this.visitStatementList(ctx.ifsats);
    
        GeneralFlowGraph<?, DefaultEdge> path_false = this.visitStatementList(ctx.elsestats);

        this.icdfg.addVertexes(condition_expression, decision, path_true, path_false, merge);
        this.icdfg.addEdge(condition_expression, decision);
        this.icdfg.addControlEdgesTo(decision, path_true, path_false);
        this.icdfg.addIncomingEdgesTo(merge, path_true, path_false);
        
        
        
        return merge;
    }
        
}
