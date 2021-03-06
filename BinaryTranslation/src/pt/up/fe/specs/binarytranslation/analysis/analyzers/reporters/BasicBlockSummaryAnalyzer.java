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

package pt.up.fe.specs.binarytranslation.analysis.analyzers.reporters;

import java.util.List;

import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.analysis.analyzers.ABasicBlockAnalyzer;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class BasicBlockSummaryAnalyzer extends ABasicBlockAnalyzer {

    public BasicBlockSummaryAnalyzer(ATraceInstructionStream stream, ELFProvider elf, int window) {
        super(stream, elf, window);
        // TODO Auto-generated constructor stub
    }
    
    public BasicBlockSummaryAnalyzer(List<List<Instruction>> basicBlocks) {
        super(basicBlocks);
    }

}
