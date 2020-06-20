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

package pt.up.fe.specs.binarytranslation.lex.listeners;

import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseListener;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.*;

public class TestListener extends PseudoInstructionBaseListener {

    private String codeLine;

    public TestListener() {

    }

    private void emit() {
        System.out.println(this.codeLine);
    }

    /*
     * For each pseudoinstruction in the grammar
     * (non-Javadoc)
     * @see pt.up.fe.specs.binarytranslation.lex.InstructionBaseListener#enterPseudoinstruction(pt.up.fe.specs.binarytranslation.lex.InstructionParser.PseudoinstructionContext)
     */
    @Override
    public void enterPseudoInstruction(PseudoInstructionContext ctx) {
        this.codeLine = "// translation for " + ctx.getText() + "\n";

    }

    @Override
    public void exitPseudoInstruction(PseudoInstructionContext ctx) {
        this.codeLine += "// end of translation";
        this.emit();
    }

    @Override
    public void enterStatement(StatementContext ctx) {
        this.codeLine += "assign";
    }

    @Override
    public void exitStatement(StatementContext ctx) {
        this.codeLine += ";\n";
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
