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
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.subscript.HardwareValidationScalarSubscript;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.ternary.subscript.HardwareValidationRangeSubscript;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.unary.AHardwareValidationUnaryOperation;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.unary.HardwareValidationUnaryOperationMap;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseVisitor;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AsmFieldOperandContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AssignmentExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.BinaryExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.FunctionExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfElseStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.LiteralOperandContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.MetafieldContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.OperatorContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ParenExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PlainStmtContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.RangesubscriptContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ScalarsubscriptContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.StatementlistContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.UnaryExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.VariableExprContext;

public class HardwareValidationDataGenerator extends PseudoInstructionBaseVisitor<Object>{

    private Map<String, Number> value;
    private Map<String, Number> outputs;
    private Map<String, Number> inputs;
    
    public HardwareValidationDataGenerator() {
        this.inputs = new HashMap<>();
        this.outputs = new HashMap<>();
        this.value = new HashMap<>();
    }
    
    public Map<String, Number> getInputMap(){
        return this.inputs;
    }
    
    public Map<String, Number> getOutputMap(){
        return this.outputs;
    }
    
    public Map<String, Number> getValueMap(){
        return this.value;
    }
    
    @SuppressWarnings("unchecked")
    public static Map<Map<String, Number>, Map<String, Number>> generateValidationData(Instruction instruction, Set<String> inputRegisters, int samples) {
        
        Map<Map<String, Number>, Map<String, Number>> validationData = new HashMap<>();
        
        Random randomInputData = new Random();
        
        for(int i = 0; i < samples; i++) {
            HardwareValidationDataGenerator validationDataGenerator = new HardwareValidationDataGenerator();
            
            inputRegisters.forEach(input -> validationDataGenerator.getInputMap().put(input, randomInputData.nextInt()));
            
            validationDataGenerator.getValueMap().putAll(validationDataGenerator.getInputMap());
            
            validationDataGenerator.visitPseudoInstruction(instruction.getPseudocode().getParseTree()); 
            validationData.put(validationDataGenerator.getInputMap(), validationDataGenerator.getOutputMap());
   
        }
        return validationData;
    }
    
    @SuppressWarnings("unchecked")
    public static Map<Map<String, Number>, Map<String, Number>> generateValidationData(PseudoInstructionContext instruction, Set<String> inputRegisters, int samples) {
        
        Map<Map<String, Number>, Map<String, Number>> validationData = new HashMap<>();
        
        Random randomInputData = new Random();
        
        for(int i = 0; i < samples; i++) {
            HardwareValidationDataGenerator validationDataGenerator = new HardwareValidationDataGenerator();
            
            inputRegisters.forEach(input -> validationDataGenerator.getInputMap().put(input, randomInputData.nextInt()));
            
            validationDataGenerator.getValueMap().putAll(validationDataGenerator.getInputMap());
            
            validationDataGenerator.visitPseudoInstruction(instruction); 
            validationData.put(validationDataGenerator.getInputMap(), validationDataGenerator.getOutputMap());
   
        }
        return validationData;
    }
    
    public static String generateHexMemFile(Collection<Map<String,Number>> data) {
        
        StringBuilder fileBuilder = new StringBuilder();
        
        data.forEach(valueMap -> {
            valueMap.values().forEach(value -> {
                if(value instanceof Float)
                    fileBuilder.append(Float.toHexString((Float) value));
                else if (value instanceof Integer)
                    fileBuilder.append(String.format("%08X", value.intValue()));
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
            return this.getValueMap().get(this.visit(ctx));
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
        return (HardwareValidationBinaryOperationMap.get(ctx.getText()) != null) ?  HardwareValidationBinaryOperationMap.get(ctx.getText()) : HardwareValidationUnaryOperationMap.get(ctx.getText());
    }
    
    @Override
    public Object visitVariableExpr(VariableExprContext ctx) {     
        return this.visit(ctx.operand());
    }
    

    @Override
    public Object visitLiteralOperand(LiteralOperandContext ctx) {
        
        this.getValueMap().putIfAbsent(ctx.getText(), Integer.valueOf(ctx.getText()));
        
        return ctx.getText();
    }
    

    @Override
    public Object visitAsmFieldOperand(AsmFieldOperandContext ctx) {
        
        this.getValueMap().putIfAbsent(ctx.getText(), 0);
        
        return this.getValueMap().get(ctx.getText());
    }
    
    @Override
    public Object visitFunctionExpr(FunctionExprContext ctx) {
        return super.visitFunctionExpr(ctx);
    }
    
    
    @Override
    public Object visitMetafield(MetafieldContext ctx) {
        return "meta" + ctx.getText().replace("$", "");
    }
    
    
    @Override
    public Object visitScalarsubscript(ScalarsubscriptContext ctx) {
        return (new HardwareValidationScalarSubscript()).apply(this.getValueMap().get(ctx.getText().substring(0, ctx.getText().indexOf("["))), Integer.valueOf(ctx.idx.getText()));
    }
    
    @Override
    public Object visitRangesubscript(RangesubscriptContext ctx) {
        return (new HardwareValidationRangeSubscript()).apply(this.getValueMap().get(ctx.getText().substring(0, ctx.getText().indexOf("["))), Integer.valueOf(ctx.hiidx.getText()), Integer.valueOf(ctx.loidx.getText()));
    }
}
