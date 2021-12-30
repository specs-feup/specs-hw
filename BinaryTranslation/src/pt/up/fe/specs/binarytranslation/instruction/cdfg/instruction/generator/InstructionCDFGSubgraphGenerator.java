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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.InstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.AInstructionCDFGModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.data_type.InstructionCDFGFloatCastModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.data_type.InstructionCDFGSignExtendModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.data_type.InstructionCDFGSignedCastModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.data_type.InstructionCDFGUnsignedCastModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.subscript.InstructionCDFGRangeSubscript;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.subscript.InstructionCDFGScalarSubscript;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.control.AInstructionCDFGControlNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.control.InstructionCDFGControlConditionalNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.AInstructionCDFGDataNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGLiteralNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGVariableFunctionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGVariableNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.InstructionCDFGOperationNodeMap;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.arithmetic.InstructionCDFGAssignmentNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.AInstructionCDFGControlFlowSubgraph;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseVisitor;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ArgumentsContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AsmFieldOperandContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AssignmentExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.BinaryExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.FunctionExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.FunctionNameContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfElseStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.LiteralOperandContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.MetafieldContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.OperatorContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ParenExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.RangesubscriptContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ScalarsubscriptContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.UnaryExprContext;

public class InstructionCDFGSubgraphGenerator extends PseudoInstructionBaseVisitor<AInstructionCDFGNode>{

    private AInstructionCDFGSubgraph subgraph;
    
    private Map<String, AInstructionCDFGNode> current_outputs;
    private Map<String, Integer> seedUIDMap;

    private void setup(AInstructionCDFGSubgraph dfg, Map<String, Integer> seedUIDMap){
        this.subgraph = dfg;
        this.current_outputs = new HashMap<>();
        this.seedUIDMap = new HashMap<>();
    }

    private AInstructionCDFGSubgraph finish(){
        return this.subgraph.resolveAll(this.seedUIDMap);
    }

    public AInstructionCDFGSubgraph generate(AInstructionCDFGSubgraph dfg, Map<String, Integer> seedUIDMap, PseudoInstructionParser.ExpressionContext ctx){
        
        this.setup(dfg, seedUIDMap);
        
        AInstructionCDFGNode output = this.visit(ctx);

        
        if((ctx instanceof BinaryExprContext) || (ctx instanceof UnaryExprContext)) {
            AInstructionCDFGNode empty = new InstructionCDFGControlConditionalNode();
            
            ((AInstructionCDFGControlFlowSubgraph)dfg).setControlVertex((AInstructionCDFGControlNode) empty);
            
            this.subgraph.addVertex(empty);

            this.subgraph.addEdge(output, empty, new InstructionCDFGEdge());
        }
        
        return this.finish();
    }

    public AInstructionCDFGSubgraph generate(AInstructionCDFGSubgraph dfg, Map<String, Integer> seedUIDMap, PseudoInstructionParser.PlainStmtContext ctx){
        
        this.setup(dfg, seedUIDMap);
        
        this.visit(ctx.expression());

        return this.finish();
    }
    
    public AInstructionCDFGSubgraph generate(AInstructionCDFGSubgraph dfg, Map<String, Integer> seedUIDMap, AssignmentExprContext ctx){
        
        this.setup(dfg, seedUIDMap);
        
        this.visit(ctx);

        return this.finish();
    }
    
    public AInstructionCDFGSubgraph generate(AInstructionCDFGSubgraph dfg, Map<String, Integer> seedUIDMap, List<PseudoInstructionParser.StatementContext> ctx){
        
        this.setup(dfg, seedUIDMap);

        ctx.forEach(statement -> this.visit(statement));

        return this.finish();
    }

    @Override
    public AInstructionCDFGNode visitAssignmentExpr(AssignmentExprContext ctx) {
        
        AInstructionCDFGNode output = this.visit(ctx.left);
        AInstructionCDFGNode operand =  this.visit(ctx.right);

        
        if(this.current_outputs.containsKey(output.getReference())) {

            this.subgraph.incomingEdgesOf(this.current_outputs.get(output.getReference())).stream()
            .filter(e -> (this.subgraph.getEdgeSource(e) instanceof InstructionCDFGAssignmentNode))
            .collect(Collectors.toSet()).forEach(e -> this.subgraph.suppressVertex(this.subgraph.getEdgeSource(e)));;

            this.subgraph.suppressVertex(this.current_outputs.get(output.getReference()));
            this.current_outputs.remove(output.getReference());
        }
        
        this.subgraph.addVertex(output);
        this.current_outputs.put(output.getReference(), output);
        
        
        if(ctx.right instanceof PseudoInstructionParser.VariableExprContext) {
            
            this.subgraph.addVertex(operand);
            
            AInstructionCDFGNode assignment = InstructionCDFGOperationNodeMap.generate("=");
            
            this.subgraph.addVertex(assignment);
            
            this.subgraph.addEdge(operand, assignment, new InstructionCDFGEdge());
            this.subgraph.addEdge(assignment, output, new InstructionCDFGEdge());
            
           return assignment; 
        }
        
        this.subgraph.addEdge(operand, output, new InstructionCDFGEdge());
        
        return output;
       
    }
    
    @Override
    public AInstructionCDFGNode visitBinaryExpr(BinaryExprContext ctx) { 

        AInstructionCDFGNode operandLeft = this.visit(ctx.left);
        AInstructionCDFGNode operandRight = this.visit(ctx.right);

        return this.subgraph.addOperation(
                this.visit(ctx.operator()), 
                this.current_outputs.getOrDefault(operandLeft.getReference(), operandLeft), 
                this.current_outputs.getOrDefault(operandRight.getReference(), operandRight)
                );
    }
    
    @Override
    public AInstructionCDFGNode visitParenExpr(ParenExprContext ctx) {
        return this.visit(ctx.expression());
    }
    
    @Override
    public AInstructionCDFGNode visitUnaryExpr(UnaryExprContext ctx) {

        AInstructionCDFGNode operand = this.visit(ctx.right);

        return this.subgraph.addOperation(
                this.visit(ctx.operator()),
                this.current_outputs.getOrDefault(operand.getReference(), operand)
                );
    }
    
    @Override
    public AInstructionCDFGNode visitIfElseStatement(IfElseStatementContext ctx) {
        return null;
    }
    
    @Override
    public AInstructionCDFGNode visitIfStatement(IfStatementContext ctx) {
        return null;
    }
    
    @Override
    public AInstructionCDFGNode visitOperator(OperatorContext ctx) {
        return InstructionCDFGOperationNodeMap.generate(ctx.getText());
    }
    
 
    @Override
    public AInstructionCDFGNode visitLiteralOperand(LiteralOperandContext ctx) {
        return new InstructionCDFGLiteralNode(ctx.number().getText());
    }

    @Override
    public AInstructionCDFGNode visitAsmFieldOperand(AsmFieldOperandContext ctx) {
        return new InstructionCDFGVariableNode(ctx.getText());
    }
    
    @Override
    public AInstructionCDFGNode visitRangesubscript(RangesubscriptContext ctx) {
        return new InstructionCDFGVariableNode(ctx.getText(), new InstructionCDFGRangeSubscript(Integer.valueOf(ctx.loidx.getText()), Integer.valueOf(ctx.hiidx.getText())));
    }
    @Override
    public AInstructionCDFGNode visitScalarsubscript(ScalarsubscriptContext ctx) {
        return new InstructionCDFGVariableNode(ctx.getText(), new InstructionCDFGScalarSubscript(Integer.valueOf(ctx.idx.getText())));
    }
    
    @Override
    public AInstructionCDFGNode visitMetafield(MetafieldContext ctx) {
        return new InstructionCDFGVariableNode(ctx.getText().replace("$", "meta"));
    }
  
    @Override
    public AInstructionCDFGNode visitFunctionExpr(FunctionExprContext ctx) {
        
        if(ctx.arguments() == null) {
            return new InstructionCDFGVariableFunctionNode(ctx.getText());
        }else {
            
            AInstructionCDFGNode variable = this.visit(ctx.arguments().getChild(0));
            
            if(variable instanceof AInstructionCDFGDataNode) {
                
                AInstructionCDFGNode modifier = this.visit(ctx.functionName());
                
                ((AInstructionCDFGDataNode)variable).setModifier((AInstructionCDFGModifier) modifier);

                return variable;
            }else {
                return null;
            }
        }
    }
    
    @Override
    public AInstructionCDFGNode visitFunctionName(FunctionNameContext ctx) {
        
        String ctxText = ctx.getText();
        
        if(ctxText.equals("signed")) {
            return new InstructionCDFGSignedCastModifier();
        }else if(ctxText.equals("unsigned")) {
            return new InstructionCDFGUnsignedCastModifier();
        }else if (ctxText.equals("float")) {
            return new InstructionCDFGFloatCastModifier();
        }else if (ctxText.equals("sext")) {
            return new InstructionCDFGSignExtendModifier();
        }else {
            return null;
        }
    }
    
    @Override
    public AInstructionCDFGNode visitArguments(ArgumentsContext ctx) {
        return this.visit(ctx.getChild(0));
    }
}
