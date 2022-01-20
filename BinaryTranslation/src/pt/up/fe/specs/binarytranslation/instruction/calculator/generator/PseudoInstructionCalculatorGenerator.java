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

package pt.up.fe.specs.binarytranslation.instruction.calculator.generator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.APseudoInstructionCalculatorBinaryOperation;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.unary.APseudoInstructionCalculatorUnaryOperation;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseVisitor;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ArgumentsContext;
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

public class PseudoInstructionCalculatorGenerator extends PseudoInstructionBaseVisitor<PseudoInstructionCalculatorWrapper>{
    
    protected Map<String, PseudoInstructionCalculatorRegister> registerMap;
    protected Map<String, Number> outputValuesMap;

    private static final Map<String, String> pseudoCodeName2operandName = Map.of("RD","RD", "RA", "RS1", "RB", "RS2", "IMM", "IMMTWELVE");
    private static final Map<String, String> operandName2pseudoCodeName = Map.of("RD","RD", "RS1", "RA", "RS2", "RB", "IMMTWELVE", "IMM");
    
    public PseudoInstructionCalculatorGenerator() {
        
        this.registerMap = new HashMap<>();
        this.outputValuesMap = new HashMap<>();
        
    }
    
    public Map<String, Number> calculate(PseudoInstructionContext instructionContext, Map<String, Number> inputs){
        
        this.clearCache();
        
        inputs.forEach((uid, value) -> 
            this.getCurrentRegisterMap().put(uid, new PseudoInstructionCalculatorRegister(uid, value))
        );
        
        this.visit(instructionContext);
        
        return this.getOutputValuesMap();
    }
    
    public Map<String, Number> calculate(Instruction instruction, Map<String, Number> inputs){
        
        // vou buscar aos inputs o valor (atraves do nome do registo verdadeiro
        
        Map<String, Number> resolvedInputs = new HashMap<>();
        
        inputs.forEach((reference, value) -> {
            
            instruction.getData().getOperands().forEach(operand -> {
                
                if(reference.equals(operand.getRepresentation()) && !operand.getAsmField().toString().equals("RD")) {
                    resolvedInputs.put(PseudoInstructionCalculatorGenerator.operandName2pseudoCodeName.get(operand.getAsmField().toString()), value);
                }else if(operand.getAsmField().toString().startsWith("IMM")) {
                    resolvedInputs.put("IMM", Integer.valueOf(operand.getRepresentation().substring(2), 16));
                }
                
            });
            
            
        });
        
        
        
        Map<String, Number> rawOutputs = this.calculate(instruction.getPseudocode().getParseTree(), resolvedInputs);
        
        Map<String, Number> resolvedOutputs = new HashMap<>();
        
        rawOutputs.forEach((reference, value) -> {
            
            instruction.getData().getOperands().forEach(operand -> {
                
                if(operand.getAsmField().toString().startsWith(PseudoInstructionCalculatorGenerator.pseudoCodeName2operandName.get(reference)) && operand.getAsmField().toString().startsWith("RD")) {
                    resolvedOutputs.put(operand.getRepresentation(), value);
                }
                
            });
            
        });
        
        return resolvedOutputs;
    }
    
    protected void clearCache() {
        
        this.registerMap.clear();
        this.outputValuesMap.clear();
        
    }
    
    public Map<String, PseudoInstructionCalculatorRegister> getCurrentRegisterMap(){
        return this.registerMap;
    }
    
    protected Map<String, Number> getOutputValuesMap(){
        return this.outputValuesMap;
    }
    
    protected Map<String, Number> convertRegisterMap(Map<String, PseudoInstructionCalculatorRegister> registerMap){
        
        Map<String, Number> newMap = new HashMap<>();
        
        registerMap.forEach((name, register) -> newMap.put(name, register.getValue()));
        
        return newMap;
    }
    
    @Override
    public PseudoInstructionCalculatorRegister visitPseudoInstruction(PseudoInstructionContext ctx) {
        
        ctx.statement().forEach(statement -> this.visit(statement));

        return null;
    }
    
    @Override
    public PseudoInstructionCalculatorWrapper visitPlainStmt(PlainStmtContext ctx) {
        return super.visit(ctx.expression());
    }
    
    @Override
    public PseudoInstructionCalculatorRegister visitStatementlist(StatementlistContext ctx) {
        
        ctx.statement().forEach(statement -> this.visit(statement));
        
        return null;
    }
    
    @Override
    public PseudoInstructionCalculatorRegister visitIfStatement(IfStatementContext ctx) {
        
        PseudoInstructionCalculatorRegister condition = (PseudoInstructionCalculatorRegister) this.visit(ctx.condition);
        
        if(condition.getValue().intValue() != 0) {
            this.visit(ctx.ifsats);
        }
        
        return null;
    }
    
    @Override
    public PseudoInstructionCalculatorRegister visitIfElseStatement(IfElseStatementContext ctx) {
        
        PseudoInstructionCalculatorRegister condition = (PseudoInstructionCalculatorRegister) this.visit(ctx.condition);

        this.visit((condition.getValue().intValue() != 0) ? ctx.ifsats : ctx.elsestats);
        
        return null;
    }
    
    @Override
    public PseudoInstructionCalculatorRegister visitAssignmentExpr(AssignmentExprContext ctx) {
        
        PseudoInstructionCalculatorRegister output = (PseudoInstructionCalculatorRegister) this.visit(ctx.left);
        
        PseudoInstructionCalculatorRegister value = (PseudoInstructionCalculatorRegister) this.visit(ctx.right);
        
        this.getOutputValuesMap().put(output.getName(), value.getValue());
        
        output.setValue(value.getValue());
        
        return null;
    }
    
    @Override
    public PseudoInstructionCalculatorWrapper visitParenExpr(ParenExprContext ctx) {
        return  this.visit(ctx.expression());
    }

    @Override
    public PseudoInstructionCalculatorRegister visitBinaryExpr(BinaryExprContext ctx) {
        
        PseudoInstructionCalculatorOperator operator = (PseudoInstructionCalculatorOperator) this.visit(ctx.operator());
        
        APseudoInstructionCalculatorBinaryOperation operation = (APseudoInstructionCalculatorBinaryOperation) operator.getOperation();
        
        PseudoInstructionCalculatorRegister operandLeft = (PseudoInstructionCalculatorRegister) this.visit(ctx.left);
        PseudoInstructionCalculatorRegister operandRight = (PseudoInstructionCalculatorRegister) this.visit(ctx.right);

        return new PseudoInstructionCalculatorRegister(ctx.operator().getText(), operation.apply(operandLeft, operandRight));
    }
    
    @Override
    public PseudoInstructionCalculatorRegister visitUnaryExpr(UnaryExprContext ctx) {
        
        PseudoInstructionCalculatorOperator operator = (PseudoInstructionCalculatorOperator) this.visit(ctx.operator());
        APseudoInstructionCalculatorUnaryOperation operation = (APseudoInstructionCalculatorUnaryOperation) operator.getOperation();
        
        PseudoInstructionCalculatorRegister operand = (PseudoInstructionCalculatorRegister) this.visit(ctx.right);
                
        return new PseudoInstructionCalculatorRegister(ctx.operator().getText(), operation.apply(operand));
    }
    
    @Override
    public PseudoInstructionCalculatorWrapper visitOperator(OperatorContext ctx) {
        return new PseudoInstructionCalculatorOperator(ctx.getText());
    }
    
    @Override
    public PseudoInstructionCalculatorWrapper visitVariableExpr(VariableExprContext ctx) {     
        return this.visit(ctx.operand());
    }
    
    protected PseudoInstructionCalculatorRegister resolveOperand(String registerName) {
        
        if(this.getCurrentRegisterMap().keySet().contains(registerName)) {
            
            return this.getCurrentRegisterMap().get(registerName);
            
        }else {
            
            PseudoInstructionCalculatorRegister newRegister = new PseudoInstructionCalculatorRegister(registerName, 0);;
            
            this.getCurrentRegisterMap().put(registerName, newRegister);
            
            return newRegister;
            
        }
    }

    @Override
    public PseudoInstructionCalculatorRegister visitLiteralOperand(LiteralOperandContext ctx) {        
        return new PseudoInstructionCalculatorRegister("IMM", Integer.valueOf(ctx.getText()));
    }
    

    @Override
    public PseudoInstructionCalculatorRegister visitAsmFieldOperand(AsmFieldOperandContext ctx) {
        return this.resolveOperand(ctx.getText());
    }
    
    @Override
    public PseudoInstructionCalculatorWrapper visitFunctionExpr(FunctionExprContext ctx) {
        return this.visit(ctx.arguments());
    }
    
    
    @Override
    public PseudoInstructionCalculatorWrapper visitArguments(ArgumentsContext ctx) {
        return this.visit(ctx.getChild(0));
    }
    
    @Override
    public PseudoInstructionCalculatorRegister visitMetafield(MetafieldContext ctx) {
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
