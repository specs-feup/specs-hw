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

import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.AHardwareValidationBinaryOperation;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.unary.AHardwareValidationUnaryOperation;
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
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.StatementlistContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.UnaryExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.VariableExprContext;

/** TODO: Still missing subscript nodes
 * 
 * @author Joao Conceicao
 *
 */

public class HardwareValidationDataGenerator extends PseudoInstructionBaseVisitor<HardwareValidationWrapper>{
    
    private Map<Map<String, Number>, Map<String, Number>> validationData;
    private Map<String, HardwareValidationRegister> registerMap;
    private Map<String, Number> outputValuesMap;

    public HardwareValidationDataGenerator() {
        
        this.validationData = new HashMap<>();
        this.registerMap = new HashMap<>();
        
        this.outputValuesMap = new HashMap<>();
    }
    
    private void clearCache() {
        
        this.validationData.clear();
        this.registerMap.clear();
        this.outputValuesMap.clear();
        
    }
    
    public Map<String, HardwareValidationRegister> getCurrentRegisterMap(){
        return this.registerMap;
    }
    
    private Map<String, Number> getOutputValuesMap(){
        return this.outputValuesMap;
    }
    
    public Map<Map<String, Number>, Map<String, Number>> getValidationData(){
        return this.validationData;
    }
    
    public Collection<Map<String, Number>> getValidationInputData(){
        return this.validationData.keySet();
    }
    
    public Collection<Map<String, Number>> getValidationOutputData(){
        return this.validationData.values();
    }
    
    public void generateValidationData(PseudoInstructionContext instructionContext, Set<String> inputRegisters, int samples) {
        
        Random inputDataGenerator = new Random();

        Map<String, HardwareValidationRegister> inputRegisterMap = new HashMap<>();
        inputRegisters.forEach(input -> inputRegisterMap.put(input, new HardwareValidationRegister(input, 0)));
        
        for(int i = 0; i < samples; i++) {
            
            this.clearCache();
            
            inputRegisters.forEach(input -> inputRegisterMap.get(input).setValue(inputDataGenerator.nextInt()));
            
            this.getCurrentRegisterMap().putAll(inputRegisterMap);
            
            this.visit(instructionContext);
            
            System.out.println(this.convertRegisterMap(this.getCurrentRegisterMap()));
            
            this.getValidationData().put(this.convertRegisterMap(inputRegisterMap), this.getOutputValuesMap());
            
        }
    }
    
    public void generateValidationData(Instruction instruction, Set<String> inputRegisters, int samples) {
        this.generateValidationData(instruction.getPseudocode().getParseTree(), inputRegisters, samples);
    }
    
    private Map<String, Number> convertRegisterMap(Map<String, HardwareValidationRegister> registerMap){
        
        Map<String, Number> newMap = new HashMap<>();
        
        registerMap.forEach((name, register) -> newMap.put(name, register.getValue()));
        
        return newMap;
    }
    
    public String buildInputHexMemFile() {
        return this.buildHexMemFile(this.getValidationInputData());
    }
    
    public String buildOutputHexMemFile() {
        return this.buildHexMemFile(this.getValidationOutputData());
    }
    
    private String buildHexMemFile(Collection<Map<String, Number>> validationData) {
        
        StringBuilder fileBuilder = new StringBuilder();
        
        validationData.forEach(valueMap -> {
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
    public HardwareValidationRegister visitPseudoInstruction(PseudoInstructionContext ctx) {
        
        ctx.statement().forEach(statement -> this.visit(statement));

        return null;
    }
    
    @Override
    public HardwareValidationWrapper visitPlainStmt(PlainStmtContext ctx) {
        return super.visit(ctx.expression());
    }
    
    @Override
    public HardwareValidationRegister visitStatementlist(StatementlistContext ctx) {
        
        ctx.statement().forEach(statement -> this.visit(statement));
        
        return null;
    }
    
    @Override
    public HardwareValidationRegister visitIfStatement(IfStatementContext ctx) {
        
        HardwareValidationRegister condition = (HardwareValidationRegister) this.visit(ctx.condition);
        
        if(condition.getValue().intValue() != 0) {
            this.visit(ctx.ifsats);
        }
        
        return null;
    }
    
    @Override
    public HardwareValidationRegister visitIfElseStatement(IfElseStatementContext ctx) {
        
        HardwareValidationRegister condition = (HardwareValidationRegister) this.visit(ctx.condition);
        
        this.visit((condition.getValue().intValue() != 0) ? ctx.ifsats : ctx.elsestats);
        
        return null;
    }
    
    @Override
    public HardwareValidationRegister visitAssignmentExpr(AssignmentExprContext ctx) {
        
        HardwareValidationRegister output = (HardwareValidationRegister) this.visit(ctx.left);
        
        HardwareValidationRegister value = (HardwareValidationRegister) this.visit(ctx.right);
        
        this.getOutputValuesMap().put(output.getName(), value.getValue());
        
        output.setValue(value.getValue());
        
        return null;
    }
    
    @Override
    public HardwareValidationWrapper visitParenExpr(ParenExprContext ctx) {
        return  this.visit(ctx.expression());
    }

    @Override
    public HardwareValidationRegister visitBinaryExpr(BinaryExprContext ctx) {
        
        HardwareValidationOperator operator = (HardwareValidationOperator) this.visit(ctx.operator());
        
        AHardwareValidationBinaryOperation operation = (AHardwareValidationBinaryOperation) operator.getOperation();
        
        HardwareValidationRegister operandLeft = (HardwareValidationRegister) this.visit(ctx.left);
        HardwareValidationRegister operandRight = (HardwareValidationRegister) this.visit(ctx.right);
        
        return new HardwareValidationRegister(ctx.operator().getText(), operation.apply(operandLeft, operandRight));
    }
    
    @Override
    public HardwareValidationRegister visitUnaryExpr(UnaryExprContext ctx) {
        
        HardwareValidationOperator operator = (HardwareValidationOperator) this.visit(ctx.operator());
        AHardwareValidationUnaryOperation operation = (AHardwareValidationUnaryOperation) operator.getOperation();
        
        HardwareValidationRegister operand = (HardwareValidationRegister) this.visit(ctx.right);
                
        return new HardwareValidationRegister(ctx.operator().getText(), operation.apply(operand));
    }
    
    @Override
    public HardwareValidationWrapper visitOperator(OperatorContext ctx) {
        return new HardwareValidationOperator(ctx.getText());
    }
    
    @Override
    public HardwareValidationWrapper visitVariableExpr(VariableExprContext ctx) {     
        return this.visit(ctx.operand());
    }
    
    protected HardwareValidationRegister resolveOperand(String registerName) {
        
        if(this.getCurrentRegisterMap().keySet().contains(registerName)) {
            
            return this.getCurrentRegisterMap().get(registerName);
            
        }else {
            
            HardwareValidationRegister newRegister = new HardwareValidationRegister(registerName, 0);;
            
            this.getCurrentRegisterMap().put(registerName, newRegister);
            
            return newRegister;
            
        }
    }

    @Override
    public HardwareValidationRegister visitLiteralOperand(LiteralOperandContext ctx) {
        return new HardwareValidationRegister("IMM", Integer.valueOf(ctx.getText()));
    }
    

    @Override
    public HardwareValidationRegister visitAsmFieldOperand(AsmFieldOperandContext ctx) {
        return this.resolveOperand(ctx.getText());
    }
    
    @Override
    public HardwareValidationWrapper visitFunctionExpr(FunctionExprContext ctx) {
        return super.visitFunctionExpr(ctx);
    }
    
    
    @Override
    public HardwareValidationRegister visitMetafield(MetafieldContext ctx) {
        return this.resolveOperand("meta" + ctx.getText().replace("$", ""));
    }
     /*
    @Override
    public HardwareValidationRegister visitScalarsubscript(ScalarsubscriptContext ctx) {
        return (new HardwareValidationScalarSubscript()).apply(this.getValueMap().get(ctx.getText().substring(0, ctx.getText().indexOf("["))), Integer.valueOf(ctx.idx.getText()));
    }
    
    @Override
    public HardwareValidationRegister visitRangesubscript(RangesubscriptContext ctx) {
        return (new HardwareValidationRangeSubscript()).apply(this.getValueMap().get(ctx.getText().substring(0, ctx.getText().indexOf("["))), Integer.valueOf(ctx.hiidx.getText()), Integer.valueOf(ctx.loidx.getText()));
    }
    */
}
