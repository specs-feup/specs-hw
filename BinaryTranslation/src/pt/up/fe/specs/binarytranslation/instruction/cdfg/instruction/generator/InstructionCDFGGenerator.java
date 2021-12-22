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
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.InstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.conditional.InstructionCDFGFalseEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.conditional.InstructionCDFGTrueEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
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

public class InstructionCDFGGenerator extends PseudoInstructionBaseVisitor<AInstructionCDFGSubgraph>{

    private InstructionCDFG icdfg; 
    
    private InstructionCDFGSubgraphGenerator subgraphGenerator;
    
    private AInstructionCDFGSubgraph last_node;
    private AInstructionCDFGEdge last_node_edge;
    
    private Map<String, Integer> uidMap;

    public InstructionCDFGGenerator() {
        this.subgraphGenerator = new InstructionCDFGSubgraphGenerator();
        this.uidMap = new HashMap<>();
    }
    
    private void finish() {
        this.icdfg.generateInputs();
        this.icdfg.generateOutputs();
        this.icdfg.generateDataInputs();
        this.icdfg.generateDataOutputs();
    }
    
    
    public InstructionCDFG generate(Instruction instruction) {

        this.icdfg = new InstructionCDFG(instruction);
        
        this.visit(instruction.getPseudocode().getParseTree());

        this.finish();
        
        return this.icdfg;
    }
    
    public InstructionCDFG generate(PseudoInstructionContext  instruction) {

        this.icdfg = new InstructionCDFG(null);
        
        this.visit(instruction);

        this.finish();
        
        return this.icdfg;
    }
    
    public InstructionCDFGDataFlowSubgraph generateDataFlowGraph(List<PseudoInstructionParser.StatementContext>  ctx){
       
        if(ctx.isEmpty()) {
            return null;
        }
        
        InstructionCDFGDataFlowSubgraph new_dfg = (InstructionCDFGDataFlowSubgraph) this.subgraphGenerator.generate(new InstructionCDFGDataFlowSubgraph(), this.uidMap, ctx);
        this.icdfg.addVertex(new_dfg);
        
        this.addPreviousStatementEdge(new_dfg);
        
        this.last_node = new_dfg;
        this.last_node_edge = new InstructionCDFGEdge();
        
        this.uidMap = new_dfg.getCurrentUIDMap();
        
        ctx.clear();
        
        return new_dfg; 
    }

    @Override
    public AInstructionCDFGSubgraph visitPseudoInstruction(PseudoInstructionContext ctx) {     
        
        this.visitStatementlist(ctx.statement());
        
       return null;
    }
    
    public AInstructionCDFGSubgraph addPreviousStatementEdge(AInstructionCDFGSubgraph new_statement) {
        
        if(this.last_node != null) {
            this.icdfg.addEdge(this.last_node, new_statement, this.last_node_edge);
            this.last_node = null;
        }
        
        return new_statement;
    }
    
    public AInstructionCDFGSubgraph visitStatementlist(List<PseudoInstructionParser.StatementContext> ctx) {

        AInstructionCDFGSubgraph return_graph = null;
        AInstructionCDFGSubgraph new_dfg = null;
        
        List<PseudoInstructionParser.StatementContext> ctx_sub = new ArrayList<>();
        
        for(PseudoInstructionParser.StatementContext statement : ctx) {
            
            if((statement instanceof IfStatementContext) || (statement instanceof IfElseStatementContext)) {
                 
                this.generateDataFlowGraph(ctx_sub);
                
                return_graph = this.visit(statement);
                
                this.last_node = return_graph;
                this.last_node_edge = new InstructionCDFGEdge();
                
            }else {
                ctx_sub.add(statement);
            }
        }
                        
        new_dfg = (AInstructionCDFGSubgraph) this.generateDataFlowGraph(ctx_sub);
   
        return (new_dfg == null) ? return_graph : new_dfg;
    }
    
    @Override
    public AInstructionCDFGSubgraph visitStatementlist(StatementlistContext ctx) {
        return this.visitStatementlist(ctx.statement());
    }
    
    @Override
    public AInstructionCDFGSubgraph visitIfStatement(IfStatementContext ctx) {
             
        InstructionCDFGControlFlowMerge merge = new InstructionCDFGControlFlowMerge();
        this.icdfg.addVertex(merge);
        
        InstructionCDFGControlFlowIf condition =  (InstructionCDFGControlFlowIf) this.subgraphGenerator.generate(new InstructionCDFGControlFlowIf(merge), this.uidMap, ctx.condition);
        this.icdfg.addVertex(condition);
       
        this.addPreviousStatementEdge(condition);

        AInstructionCDFGSubgraph path_true = this.visitConditionalPathStatements(condition, true, ctx.ifsats);
        this.icdfg.addVertex(path_true);
 
        this.icdfg.addEdge(condition, merge, new InstructionCDFGFalseEdge());
        this.icdfg.addEdge(path_true, merge);
        
        return merge;
    }
    
    @Override
    public AInstructionCDFGSubgraph visitIfElseStatement(IfElseStatementContext ctx) {
       
        InstructionCDFGControlFlowMerge merge = new InstructionCDFGControlFlowMerge();
        this.icdfg.addVertex(merge);
        
        InstructionCDFGControlFlowIfElse condition = (InstructionCDFGControlFlowIfElse) this.subgraphGenerator.generate(new InstructionCDFGControlFlowIfElse(merge), this.uidMap, ctx.condition);
        this.icdfg.addVertex(condition);
        this.addPreviousStatementEdge(condition); 

        AInstructionCDFGSubgraph path_true = this.visitConditionalPathStatements(condition, true, ctx.ifsats);
        AInstructionCDFGSubgraph path_false = this.visitConditionalPathStatements(condition, false, ctx.elsestats);

        this.icdfg.addVertices(Set.of(path_true, path_false));
        this.icdfg.addIncomingEdgesTo(merge, Set.of(path_true, path_false));
        
        return merge;
    }
        
    @Override
    public AInstructionCDFGSubgraph visitPlainStmt(PlainStmtContext ctx) {
        
        InstructionCDFGDataFlowSubgraph dfg = (InstructionCDFGDataFlowSubgraph) this.subgraphGenerator.generate(new InstructionCDFGDataFlowSubgraph(), this.uidMap, ctx);
        this.icdfg.addVertex(dfg);
        
        return dfg;
    }
    
    public AInstructionCDFGSubgraph visitConditionalPathStatements(AInstructionCDFGControlFlowConditionalSubgraph decision, boolean condition, StatementlistContext ctx){
        this.last_node = decision;
        this.last_node_edge = (condition) ? new InstructionCDFGTrueEdge() : new InstructionCDFGFalseEdge();
        return this.visitStatementlist(ctx);
    }
    
}
