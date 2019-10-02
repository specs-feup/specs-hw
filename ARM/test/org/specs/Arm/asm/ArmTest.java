/**
 * Copyright 2019 SPeCS.
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

package org.specs.Arm.asm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.specs.Arm.asmparser.ArmInstructionParsers;

import pt.up.fe.specs.binarytranslation.asmparser.IsaParser;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.utilities.LineStream;

public class ArmTest {

    @Test
    public void testArmParser() {
        IsaParser parser = ArmInstructionParsers.getArmIsaParser();

        StringBuilder output = new StringBuilder();
        try (var lines = LineStream.newInstance(SpecsIo.resourceToStream("org/specs/Arm/asm/test/asm.txt"), "");) {
            while (lines.hasNextLine()) {
                String asmInstruction = lines.nextLine();
                var data = parser.parse(asmInstruction);
                output.append(data).append("\n");
            }
        }

        String expected = SpecsStrings
                .normalizeFileContents(SpecsIo.getResource("org/specs/Arm/asm/test/arm_parser.result"), true);
        String current = SpecsStrings.normalizeFileContents(output.toString(), true);

        assertEquals(expected, current);
    }

}
