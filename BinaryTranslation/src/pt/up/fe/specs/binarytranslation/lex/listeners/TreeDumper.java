/**
 * Copyright 2021 SPeCS.
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

import org.antlr.v4.runtime.ParserRuleContext;

import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseListener;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;

public class TreeDumper extends PseudoInstructionBaseListener {

    private int nestLevel = 0;

    public TreeDumper() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void enterPseudoInstruction(PseudoInstructionContext ctx) {
        this.nestLevel = 0;
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {

        for (int i = 0; i < this.nestLevel; i++)
            System.out.print('\t');

        System.out.println(ctx.getClass().getSimpleName() + " " + ctx.getText());
        this.nestLevel++;
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        this.nestLevel--;
    }
}
