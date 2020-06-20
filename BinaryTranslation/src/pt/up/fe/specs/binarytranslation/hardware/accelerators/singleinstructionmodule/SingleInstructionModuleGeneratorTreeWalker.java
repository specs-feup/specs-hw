/**
 * Copyright 2020 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.binarytranslation.hardware.accelerators.singleinstructionmodule;

import java.util.*;

import pt.up.fe.specs.binarytranslation.hardware.component.*;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseListener;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.*;

public class SingleInstructionModuleGeneratorTreeWalker extends PseudoInstructionBaseListener {

    /*
     * Instruction data used to help construct the SingleInstructionModule
     */
    private Instruction inst;

    // TODO: a map with operands names in relation to asm names?

    protected SingleInstructionModuleGeneratorTreeWalker(Instruction inst) {
        this.inst = inst;
    }

    @Override
    public void enterPseudoInstruction(PseudoInstructionContext ctx) {
        this.components.add(new PlainCode("// translation for " + ctx.getText() + "\n"));

        // module header
        var id = this.inst.getRepresentation();
        components.add(new PlainCode("module " + id + ";"));

        // inputs/outputs
        for (var op : this.inst.getData().getOperands()) {
            var dir = (op.isRead()) ? "input" : "output";
            var width = op.getProperties().getWidth();
            components.add(new PlainCode(dir + " " + "[" + (width - 1) + "0]" + op.getAsmField().toString()));
        }
    }

    @Override
    public void exitPseudoInstruction(PseudoInstructionContext ctx) {
        this.components.add(new PlainCode("// end of translation"));
    }

    @Override
    public void enterStatement(StatementContext ctx) {
        this.components.add(new PlainCode("assign"));
    }

    @Override
    public void exitStatement(StatementContext ctx) {
        this.components.add(new PlainCode(";\n"));
    }

    @Override
    public void enterAsmfield(AsmfieldContext ctx) {
        // System.out.println("ASMFIELD: " + ctx.getText());
        this.codeLine += " " + ctx.getText();
    }

    @Override
    public void enterNumber(NumberContext ctx) {
        // System.out.println("Number: " + ctx.getText());
        this.codeLine += " " + ctx.getText();
    }

    @Override
    public void enterOperator(OperatorContext ctx) {
        // System.out.println("Operator: " + ctx.getText());
        this.codeLine += " " + ctx.getText();
    }

    @Override
    public void enterRlop(RlopContext ctx) {
        // System.out.println("Operator: " + ctx.getText());
        this.codeLine += " " + ctx.getText();
    }

}
