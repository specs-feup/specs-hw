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

package org.specs.Riscv.instruction;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import pt.up.fe.specs.binarytranslation.instruction.InstructionPseudocode;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionLexer;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;

public enum RiscvPseudocode implements InstructionPseudocode {
    add("RD = RS1 + RS2;"),
    addi("RD = RS1 + IMM;"),
    defaultCode("RD = RS1;");
    
    private final String name;
    private final String pseudocode;

    private RiscvPseudocode(String pseudocode) {
        this.pseudocode = pseudocode;
        this.name = name();
    }

    private RiscvPseudocode(String explicitname, String pseudocode) {
        this.pseudocode = pseudocode;
        this.name = explicitname;
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

    public static InstructionPseudocode getDefault() {
        return RiscvPseudocode.defaultCode;
    }

}
