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

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.InstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.AInstructionCDFGModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.data_type.InstructionCDFGFloatCastModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.data_type.InstructionCDFGSignedCastModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.data_type.InstructionCDFGUnsignedCastModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.subscript.InstructionCDFGRangeSubscript;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.subscript.InstructionCDFGScalarSubscript;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.AInstructionCDFGDataNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGGeneratedVariable;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGLiteralNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGVariableFunctionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGVariableNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.InstructionCDFGOperationNodeMap;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.arithmetic.InstructionCDFGAssignmentNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseVisitor;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ArgumentsContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AssignmentExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.BinaryExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.FieldContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.FunctionExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.FunctionNameContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfElseStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.LiteralContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.MetafieldContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.OperatorContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ParenExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.RangesubscriptContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ScalarsubscriptContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.UnaryExprContext;

public class InstructionCDFGDataFlowGraphGenerator extends PseudoInstructionBaseVisitor<AInstructionCDFGNode>{

    private InstructionCDFGDataFlowSubgraph dfg;
    
    private Map<String, AInstructionCDFGNode> current_outputs;

    private void setup(Map<String, Integer> uid_map){
        this.dfg = new InstructionCDFGDataFlowSubgraph(uid_map);
        this.current_outputs = new HashMap<>();
    }

    private InstructionCDFGDataFlowSubgraph finish(){
        
        this.dfg.generateInputs();
        this.dfg.generateOutputs();

        return this.dfg;
    }
    
    public Map<String, Integer> getUIDMap(){
        return this.dfg.getUIDMap();
    }
    
    public InstructionCDFGDataFlowSubgraph generate(Map<String, Integer> uid_map, PseudoInstructionParser.ExpressionContext ctx){
        
        this.setup(uid_map);
        
        AInstructionCDFGNode output = this.visit(ctx);

        
        if((ctx instanceof BinaryExprContext) || (ctx instanceof UnaryExprContext)) {
            AInstructionCDFGNode empty = new InstructionCDFGGeneratedVariable("generated");
            this.dfg.addVertex(empty);

            this.dfg.addEdge(output, empty, new InstructionCDFGEdge());
        }
        
        return this.finish();
    }

    public InstructionCDFGDataFlowSubgraph generate(Map<String, Integer> uid_map, PseudoInstructionParser.PlainStmtContext ctx){
        
        this.setup(uid_map);
        
        this.visit(ctx.expression());

        return this.finish();
    }
    
    public InstructionCDFGDataFlowSubgraph generate(Map<String, Integer> uid_map, AssignmentExprContext ctx){
        
        this.setup(uid_map);
        
        this.visit(ctx);

        return this.finish();
    }
    
    public InstructionCDFGDataFlowSubgraph generate(Map<String, Integer> uid_map,List<PseudoInstructionParser.StatementContext> ctx){
        
        this.setup(uid_map);

        ctx.forEach(statement -> {this.visit(statement);});

        return this.finish();
    }

    @Override
    public AInstructionCDFGNode visitAssignmentExpr(AssignmentExprContext ctx) {
        
        AInstructionCDFGNode output = this.visit(ctx.left);
        AInstructionCDFGNode operand =  this.visit(ctx.right);

        
        if(this.current_outputs.containsKey(output.getReference())) {

            this.dfg.incomingEdgesOf(this.current_outputs.get(output.getReference())).stream().filter(e -> (this.dfg.getEdgeSource(e) instanceof InstructionCDFGAssignmentNode)).toList().forEach(e -> this.dfg.suppressVertex(this.dfg.getEdgeSource(e)));;

            this.dfg.suppressVertex(this.current_outputs.get(output.getReference()));
            this.current_outputs.remove(output.getReference());
        }
        
        this.dfg.addVertex(output);
        this.current_outputs.put(output.getReference(), output);
        
        
        if(ctx.right instanceof PseudoInstructionParser.VariableExprContext) {
            
            this.dfg.addVertex(operand);
            
            AInstructionCDFGNode assignment = InstructionCDFGOperationNodeMap.generate("=");
            
            this.dfg.addVertex(assignment);
            
            this.dfg.addEdge(operand, assignment, new InstructionCDFGEdge());
            this.dfg.addEdge(assignment, output, new InstructionCDFGEdge());
            
           return assignment; 
        }
        
        this.dfg.addEdge(operand, output, new InstructionCDFGEdge());
        
        return output;
       
    }
    
    @Override
    public AInstructionCDFGNode visitBinaryExpr(BinaryExprContext ctx) { 
        
        //AInstructionCDFGNode operator = this.visit(ctx.operator());
        AInstructionCDFGNode operandLeft = this.visit(ctx.left);
        AInstructionCDFGNode operandRight = this.visit(ctx.right);

        return this.dfg.addOperation(
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
        
        //AInstructionCDFGNode operator = this.visit(ctx.operator());
        AInstructionCDFGNode operand = this.visit(ctx.right);

        return this.dfg.addOperation(
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
    public AInstructionCDFGNode visitLiteral(LiteralContext ctx) {
        return new InstructionCDFGLiteralNode(ctx.number().getText());
    }
    
   /* @Override
    public AInstructionCDFGNode visitVariableExpr(VariableExprContext ctx) {
        return new InstructionCDFGLiteralNode(ctx.getText());
    }*/
    
    @Override
    public AInstructionCDFGNode visitField(FieldContext ctx) {
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
        
        if(ctx.getText().equals("signed")) {
            return new InstructionCDFGSignedCastModifier();
        }else if(ctx.getText().equals("unsigned")) {
            return new InstructionCDFGUnsignedCastModifier();
        }else if (ctx.getText().equals("float")) {
            return new InstructionCDFGFloatCastModifier();
        }else {
            return null;
        }
    }
    
    @Override
    public AInstructionCDFGNode visitArguments(ArgumentsContext ctx) {
        return this.visit(ctx.getChild(0));
    }
}
