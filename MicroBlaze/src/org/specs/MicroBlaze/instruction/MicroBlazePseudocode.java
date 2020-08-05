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

package org.specs.MicroBlaze.instruction;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import pt.up.fe.specs.binarytranslation.instruction.InstructionPseudocode;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionLexer;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;

public enum MicroBlazePseudocode implements InstructionPseudocode {

    add("RD = RA + RB;"),
    addk("RD = RA + RB;"),
    addc("RD = RA + RB + ANS[32];"),
    addkc("RD = RA + RB + ANS[32];"),
    // TODO: how to handle carry input?? keeping carry
    // is easier (?) but enforcing a carry out back to the processor is not

    rsub("RD = RB + ~RA + 1;"),
    rsubk("RD = RB + ~RA + 1;"),
    rsubc("RD = RB + ~RA + 1 + ANS[32];"),
    rsubkc("RD = RB + ~RA + 1 + ANS[32];"),
    // TODO: how to handle carry input?? keeping carry
    // is easier (?) but enforcing a carry out back to the processor is not

    cmp("RD = RB + ~RA + 1; msb(RD) = (RA > RB);"),
    // TODO implement the built-in function "msb"

    // cmpu("RD = RB + ~RA + 1; msb(RD) = (RA > RB);"); TODO: how to deal with unsigned values??

    mul("RD = lsw(RA x RB);"),
    mulh("RD = msw(RA x RB);");
    // TODO implement the modifiers/functions/built-ins "lsw" and "msw"

    /*
    
    
    
    
    mulhu(0x4000_0003, 3, 0, TYPE_A, G_MUL),
    mulhsu(0x4000_0002, 3, 0, TYPE_A, G_MUL),*/

    private final String name;
    private final String pseudocode;

    private MicroBlazePseudocode(String pseudocode) {
        this.pseudocode = pseudocode;
        this.name = name();
    }

    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return pseudocode;
    }

    @Override
    public PseudoInstructionContext getParseTree() {
        var parser = new PseudoInstructionParser(
                new CommonTokenStream(new PseudoInstructionLexer(new ANTLRInputStream(this.getCode()))));
        return parser.pseudoInstruction();
    }
}
