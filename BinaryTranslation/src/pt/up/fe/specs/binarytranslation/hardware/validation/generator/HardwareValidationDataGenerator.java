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

package pt.up.fe.specs.binarytranslation.hardware.validation.generator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;

import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.AHardwareValidationBinaryOperation;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.HardwareValidationBinaryOperationMap;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.unary.AHardwareValidationUnaryOperation;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.unary.HardwareValidationUnaryOperationMap;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseVisitor;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AssignmentExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.BinaryExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.FieldContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.FunctionExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfElseStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.LiteralContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.MetaFieldOperandContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.OperatorContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ParenExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PlainStmtContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.StatementlistContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.UnaryExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.VariableExprContext;

public class HardwareValidationDataGenerator extends PseudoInstructionBaseVisitor<Object>{

    private Map<String, Number> valueMap;
    private Map<String, Number> outputs;
    
    public HardwareValidationDataGenerator() {
        this.valueMap = new HashMap<>();
        this.outputs = new HashMap<>();
    }
    
    public Map<String, Number> getValueMap(){
        return this.valueMap;
    }
    
    public Map<String, Number> getOutputMap(){
        return this.outputs;
    }
    
    @SuppressWarnings("unchecked")
    public static Map<Map<String, Number>, Map<String, Number>> generateValidationData(Instruction instruction, Set<String> inputRegisters, int samples) {
        
        Map<Map<String, Number>, Map<String, Number>> validationData = new HashMap<>();
        
        Random randomInputData = new Random();
        
        for(int i = 0; i < samples; i++) {
            HardwareValidationDataGenerator validationDataGenerator = new HardwareValidationDataGenerator();
            
            inputRegisters.forEach(input -> validationDataGenerator.getValueMap().put(input, randomInputData.nextInt()));
            Map<String, Number> inputs = validationDataGenerator.getValueMap();
            
            validationDataGenerator.visitPseudoInstruction(instruction.getPseudocode().getParseTree());
            
            
            validationData.put(inputs, validationDataGenerator.getOutputMap());

        }
        return validationData;
    }
    
    public static String generateHexMemFile(String fileName, Collection<Map<String,Number>> data) {
        
        StringBuilder fileBuilder = new StringBuilder();
        
        data.forEach(valueMap -> {
            valueMap.values().forEach(value -> {
                if(value instanceof Float)
                    fileBuilder.append(Float.toHexString((Float) value));
                else if (value instanceof Integer)
                    fileBuilder.append(Integer.toHexString((Integer) value));
            });
            fileBuilder.append("\n");
        });
        
        return fileBuilder.toString();
    }
    
    @Override
    public Object visitPseudoInstruction(PseudoInstructionContext ctx) {
        
        ctx.statement().forEach(statement -> this.visit(statement));

        return null;
    }
    
    @Override
    public Object visitPlainStmt(PlainStmtContext ctx) {
        return super.visit(ctx.expression());
    }
    
    @Override
    public Object visitStatementlist(StatementlistContext ctx) {
        
        ctx.statement().forEach(statement -> this.visit(statement));
        
        return null;
    }
    
    @Override
    public Object visitIfStatement(IfStatementContext ctx) {
        
        if(((Number)this.visit(ctx.condition)).intValue() != 0) {
            this.visit(ctx.ifsats);
        }
        
        return null;
    }
    
    @Override
    public Object visitIfElseStatement(IfElseStatementContext ctx) {
        
        this.visit((((Number)this.visit(ctx.condition)).intValue() != 0) ? ctx.ifsats : ctx.elsestats);
        
        return null;
    }
    
    @Override
    public Object visitAssignmentExpr(AssignmentExprContext ctx) {
        
        String output = (String) this.visit(ctx.left);
        Number value = (Number) this.visit(ctx.right);
        
        this.valueMap.put(output, value);
        this.outputs.put(output, value);
        
        return null;
    }
    
    @Override
    public Object visitParenExpr(ParenExprContext ctx) {
        return this.visit(ctx.expression());
    }
    
    private Number visitOperand(ParserRuleContext ctx) {
        
        if((ctx instanceof BinaryExprContext) || (ctx instanceof UnaryExprContext) || (ctx instanceof ParenExprContext)) {
            return (Number) this.visit(ctx);
        }else if(ctx instanceof VariableExprContext) {
            return this.valueMap.get(this.visit(ctx));
        }
        
        throw new IllegalArgumentException();
    }

    @Override
    public Object visitBinaryExpr(BinaryExprContext ctx) {
        return ((AHardwareValidationBinaryOperation)this.visit(ctx.operator())).apply(this.visitOperand(ctx.left), this.visitOperand(ctx.right));
    }
    
    @Override
    public Object visitUnaryExpr(UnaryExprContext ctx) {
        return ((AHardwareValidationUnaryOperation)this.visit(ctx.operator())).apply(this.visitOperand(ctx.right));
    }
    
    @Override
    public Object visitOperator(OperatorContext ctx) {
        
        if(HardwareValidationBinaryOperationMap.get(ctx.getText()) != null)
            return HardwareValidationBinaryOperationMap.get(ctx.getText());
        else 
            return HardwareValidationUnaryOperationMap.get(ctx.getText());
    }
    
    @Override
    public Object visitVariableExpr(VariableExprContext ctx) {
        
        String operand = (String) this.visit(ctx.operand());
        
        this.valueMap.putIfAbsent(operand, 0);
        
        return operand;
    }
    
    @Override
    public Object visitLiteral(LiteralContext ctx) {
        
        this.valueMap.putIfAbsent(ctx.getText(), Integer.valueOf(ctx.getText()));
        
        return ctx.getText();
    }
    
    
    @Override
    public Object visitField(FieldContext ctx) {
        return ctx.getText();
    }
    
    @Override
    public Object visitFunctionExpr(FunctionExprContext ctx) {
        return super.visitFunctionExpr(ctx);
    }
    
    @Override
    public Object visitMetaFieldOperand(MetaFieldOperandContext ctx) {
        return "meta" + ctx.getText().replace("$", "");
    }
    
}
