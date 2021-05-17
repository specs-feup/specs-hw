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

package pt.up.fe.specs.binarytranslation.analysis;

import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class MemoryAccessTypesAnalyzer extends ATraceAnalyzer {

    public MemoryAccessTypesAnalyzer(ATraceInstructionStream stream, ELFProvider elf) {
        super(stream, elf);
    }

    public void analyze() {
        var trace = AnalysisUtils.getCompleteTrace(stream,
                elf.getKernelStart() == null ? 0 : elf.getKernelStart().longValue(),
                elf.getKernelStop() == null ? -1 : elf.getKernelStop().longValue());
        System.out.println("Trace has " + trace.size() + " instructions");
    }
}
