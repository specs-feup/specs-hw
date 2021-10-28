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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.dataflowgraph.DataFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.InstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.conditional.InstructionCDFGFalseEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.conditional.InstructionCDFGTrueEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.AInstructionCDFGControlFlowConditionalSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIf;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIfElse;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.merge.InstructionCDFGControlFlowMerge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseVisitor;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfElseStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PlainStmtContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.StatementlistContext;

public class InstructionCDFGGenerator extends PseudoInstructionBaseVisitor<GeneralFlowGraph<AInstructionCDFGNode,AInstructionCDFGEdge>>{

    private InstructionCDFG icdfg; 
    
    private InstructionCDFGDataFlowGraphGenerator dfg_visitor;
    
    private GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> last_node;
    private AInstructionCDFGEdge last_node_edge;
    
    
    private Stack<Map<String, Integer>> uid_map_stack;
    
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

        this.icdfg = new InstructionCDFG(instruction);
        
        this.visit(instruction.getPseudocode().getParseTree());

        this.icdfg.generateInputs();
        this.icdfg.generateOutputs();
        
        return this.icdfg;
    }
    
    public InstructionCDFG generate(PseudoInstructionContext  instruction) {

        this.icdfg = new InstructionCDFG(null);
        
        this.visit(instruction);

        this.icdfg.generateInputs();
        this.icdfg.generateOutputs();
        
        return this.icdfg;
    }
    
    public DataFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> generateDataFlowGraph(List<PseudoInstructionParser.StatementContext>  ctx){
       
        if(ctx.isEmpty()) {
            return null;
        }
        
        InstructionCDFGDataFlowSubgraph new_dfg = this.dfg_visitor.generate(new InstructionCDFGDataFlowSubgraph(this.uid_map), ctx);
        this.icdfg.addVertex(new_dfg);
        
        this.addPreviousStatementEdge(new_dfg);
        
        this.last_node = new_dfg;
        this.last_node_edge = new InstructionCDFGEdge();
        
        ctx.clear();
        
        return new_dfg; 
    }

    @Override
    public GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> visitPseudoInstruction(PseudoInstructionContext ctx) {     
        
        this.visitStatementlist(ctx.statement());
        
       return null;
    }
    
    public GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> addPreviousStatementEdge(GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> new_statement) {
        if(this.last_node != null) {
            this.icdfg.addEdge(this.last_node, new_statement, this.last_node_edge);
            this.last_node = null;
        }
        return new_statement;
    }
    
    public GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> visitStatementlist(List<PseudoInstructionParser.StatementContext> ctx) {

        GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> return_graph = null;
        GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> new_dfg = null;
        
        List<PseudoInstructionParser.StatementContext> ctx_sub = new ArrayList<>();
        
        for(PseudoInstructionParser.StatementContext statement : ctx) {
            
            if((statement instanceof IfStatementContext) || (statement instanceof IfElseStatementContext)) {
                
                this.generateDataFlowGraph(ctx_sub);
                return_graph = this.visit(statement);
             
            }else {
                ctx_sub.add(statement);
            }
        }
            
            
        new_dfg = this.generateDataFlowGraph(ctx_sub);
   
        return (new_dfg == null) ? return_graph : new_dfg;
    }
    
    @Override
    public GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> visitStatementlist(StatementlistContext ctx) {
        return this.visitStatementlist(ctx.statement());
    }
    
    @Override
    public GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> visitIfStatement(IfStatementContext ctx) {
             
        InstructionCDFGControlFlowMerge merge = new InstructionCDFGControlFlowMerge(this.uid_map);
        this.icdfg.addVertex(merge);
        
        InstructionCDFGControlFlowIf condition =  (InstructionCDFGControlFlowIf) this.dfg_visitor.generate(new InstructionCDFGControlFlowIf(merge, this.uid_map), ctx.condition);
        this.icdfg.addVertex(condition);
        this.addPreviousStatementEdge(condition);

        GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> path_true = this.visitConditionalPathStatements(condition, true, ctx.ifsats);
        this.icdfg.addVertex(path_true);
 
        this.icdfg.addEdge(condition, merge, new InstructionCDFGFalseEdge());
        this.icdfg.addEdge(path_true, merge);
        
        return merge;
    }
    
    @Override
    public GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> visitIfElseStatement(IfElseStatementContext ctx) {
       
        InstructionCDFGControlFlowMerge merge = new InstructionCDFGControlFlowMerge(this.uid_map);
        this.icdfg.addVertex(merge);
        
        InstructionCDFGControlFlowIfElse condition = (InstructionCDFGControlFlowIfElse) this.dfg_visitor.generate(new InstructionCDFGControlFlowIfElse(merge, this.uid_map), ctx.condition);
        this.icdfg.addVertex(condition);
        this.addPreviousStatementEdge(condition); 

        GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> path_true = this.visitConditionalPathStatements(condition, true, ctx.ifsats);
        GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> path_false = this.visitConditionalPathStatements(condition, false, ctx.elsestats);

        this.icdfg.addVertices(Set.of(path_true, path_false));
        this.icdfg.addIncomingEdgesTo(merge, Set.of(path_true, path_false));
        
        return merge;
    }
        
    @Override
    public GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> visitPlainStmt(PlainStmtContext ctx) {
        
        InstructionCDFGDataFlowSubgraph dfg = this.dfg_visitor.generate(new InstructionCDFGDataFlowSubgraph(this.uid_map), ctx);
        this.icdfg.addVertex(dfg);
        
        return dfg;
    }
    
    public GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> visitConditionalPathStatements(AInstructionCDFGControlFlowConditionalSubgraph decision, boolean condition, StatementlistContext ctx){
        this.last_node = decision;
        this.last_node_edge = (condition) ? new InstructionCDFGTrueEdge() : new InstructionCDFGFalseEdge();
        return this.visitStatementlist(ctx);
    }
    
}
