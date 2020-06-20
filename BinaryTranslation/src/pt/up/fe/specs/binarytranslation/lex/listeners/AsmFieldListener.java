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

import java.util.Stack;

import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseListener;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AsmfieldContext;
import pt.up.fe.specs.binarytranslation.parsing.AsmField;

public class AsmFieldListener extends PseudoInstructionBaseListener {

    private Stack<String> asmFields;
    // private final Class<T> asmFieldClass;

    public AsmFieldListener(/*Class<T> asmFieldClass*/) {
        // this.asmFieldClass = asmFieldClass;
        this.asmFields = new Stack<String>();
    }

    /*
     * Walk the tree and gather all AsmFields
     * @see pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseListener#enterAsmfield(pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AsmfieldContext)
     */
    @Override
    public void enterAsmfield(AsmfieldContext ctx) {
        // var field = ctx.getText()
        // var test = this.asmFieldClass.getEnumConstants();
        this.asmFields.push(ctx.getText());
    }

    public Stack<String> getAsmFields() {
        return asmFields;
    }
}
