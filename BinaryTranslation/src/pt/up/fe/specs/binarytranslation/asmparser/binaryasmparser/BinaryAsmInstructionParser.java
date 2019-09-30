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

package pt.up.fe.specs.binarytranslation.asmparser.binaryasmparser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionData;
import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionParser;
import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionType;
import pt.up.fe.specs.util.stringparser.StringParser;

/**
 * Parses a binary string representing an instruction, according to a given rule in String format.
 * 
 * <p>
 * Parsing rules:<br>
 * - 0, 1 and x match a 0, 1 and both 0 and 1;<br>
 * - FIELD(NUM_BITS) will capture NUM_BITS and store them with the key FIELD;<br>
 * - 0, 1 and x can also have () to specify a number of bits to match (e.g., 0(11));<br>
 * - Underscores can be used to separate components (e.g., 0_Rd(10)_x(11)_SHIFT_AMOUNT(3));
 * 
 * @author JoaoBispo
 *
 */
public class BinaryAsmInstructionParser implements AsmInstructionParser {

    private final AsmInstructionType type;
    private final List<BinaryAsmRule> rules;
    private final Predicate<Map<String, String>> predicate;

    private BinaryAsmInstructionParser(AsmInstructionType type, List<BinaryAsmRule> rules,
            Predicate<Map<String, String>> predicate) {

        this.type = type;
        this.rules = rules;
        this.predicate = predicate;
    }

    @Override
    public Optional<AsmInstructionData> parse(String asmInstruction) {
        var currentString = new StringParser(asmInstruction);
        Map<String, String> fields = new HashMap<>();

        for (var rule : rules) {
            boolean success = rule.apply(currentString, fields);

            if (!success) {
                return Optional.empty();
            }
        }

        // All rules applied successfully, check if string was completely consume
        if (!currentString.isEmpty()) {
            throw new RuntimeException("All rules where successfully applied, but instruction '" + asmInstruction
                    + "' was not completely consumed: '" + currentString + "'");
        }

        // If predicate is defined, check if it passes
        if (predicate != null && !predicate.test(fields)) {
            return Optional.empty();
        }

        return Optional.of(AsmInstructionData.newInstance(type, fields));
    }

}
