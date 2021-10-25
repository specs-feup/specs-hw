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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.instruction;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.AGenericCDFG;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * @author João Conceição
 */

public class InstructionCDFG extends AGenericCDFG{

    private Instruction instruction;
    private Object generator;
    private InstructionAST iast;
    private static final String unique_node_str = "i";
    

    public InstructionCDFG() {
        super(unique_node_str);
        this.generator = new InstructionCDFGGenerator(); 
    }
    
    public InstructionCDFG(Instruction instruction) {
        super(unique_node_str);
        this.generator = new InstructionCDFGGenerator(); 
        
        this.instruction = instruction;
        
        this.mergeGraph(((InstructionCDFGGenerator)this.generator).generate(instruction));

    }
    
    public InstructionCDFG(InstructionAST iast) {
        super(unique_node_str);
        this.iast = iast;
        this.instruction = iast.getInst();
        this.generator = new InstructionCDFGFromInstructionASTGenenerator();
        
        try {
            this.mergeGraph(((InstructionCDFGFromInstructionASTGenenerator)this.generator).generate(this.iast));
        } catch (Exception e) {
            SpecsLogs.msgWarn("Error message:\n", e);
        }
        
    }
   
    public Object getGenerator() {
        return this.generator;
    }
    
    public Instruction getInstruction() {
        
        return this.instruction;
    }
    
    public PseudoInstructionContext getInstructionParseTree() {
        return this.instruction.getPseudocode().getParseTree();
    }
}
