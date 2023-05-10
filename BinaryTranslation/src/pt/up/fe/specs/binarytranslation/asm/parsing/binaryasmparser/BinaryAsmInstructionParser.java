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

package pt.up.fe.specs.binarytranslation.asm.parsing.binaryasmparser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldType;
import pt.up.fe.specs.binarytranslation.asm.parsing.AsmParser;
import pt.up.fe.specs.util.SpecsStrings;
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
public class BinaryAsmInstructionParser implements AsmParser {

    private final AsmFieldType type;
    private final List<BinaryAsmRule> rules;
    private final Predicate<Map<String, String>> predicate;

    public BinaryAsmInstructionParser(AsmFieldType type, String rule,
            Predicate<Map<String, String>> predicate) {

        this(type, parseBinaryAsmRule(rule), predicate);
    }

    private BinaryAsmInstructionParser(AsmFieldType type, List<BinaryAsmRule> rules,
            Predicate<Map<String, String>> predicate) {

        this.type = type;
        this.rules = rules;
        this.predicate = predicate;
    }

    private static List<BinaryAsmRule> parseBinaryAsmRule(String rule) {
        String currentRule = rule;

        List<BinaryAsmRule> rules = new ArrayList<>();
        while (!currentRule.isEmpty()) {
            // Ignore underscore
            if (currentRule.startsWith("_")) {
                currentRule = currentRule.substring(1);
                continue;
            }

            // If 0 or 1, create constant rule
            if (currentRule.startsWith("0") || currentRule.startsWith("1")) {
                String constString = currentRule.substring(0, 1);
                currentRule = currentRule.substring(1);

                // Check if has ()
                var result = extractAmount(currentRule, rule);
                if (result != null) {
                    constString = SpecsStrings.buildLine(constString, result.getAmount());
                    currentRule = result.getCurrentString();
                }

                rules.add(new ConstantRule(constString));
                continue;
            }

            // If x, create ignore rule
            if (currentRule.startsWith("x")) {
                int amount = 1;
                currentRule = currentRule.substring(1);

                // Check if has ()
                var result = extractAmount(currentRule, rule);
                if (result != null) {
                    amount = result.getAmount();
                    currentRule = result.getCurrentString();
                }

                rules.add(new IgnoreRule(amount));
                continue;
            }

            // Otherwise, interpret as a field with ()
            int startIndex = currentRule.indexOf('(');
            if (startIndex == -1) {
                throw new RuntimeException("Expected field name to have () associated: " + rule);
            }

            String fieldName = currentRule.substring(0, startIndex);
            currentRule = currentRule.substring(startIndex);
            var result = extractAmount(currentRule, rule);

            rules.add(new FieldRule(fieldName, result.getAmount()));
            currentRule = result.getCurrentString();
        }

        // Rules could be optimized - e.g., fuse constant rules together
        // System.out.println("RULES: " + rules);
        return rules;
    }

    private static ExtractResult extractAmount(String currentRule, String fullRule) {
        if (!currentRule.startsWith("(")) {
            return null;
        }

        int endIndex = currentRule.indexOf(')');
        if (endIndex == -1) {
            throw new RuntimeException("Unbalanced parenthesis on rule: " + fullRule);
        }

        int amount = Integer.parseInt(currentRule.substring(1, endIndex));
        String updatedCurrentRule = currentRule.substring(endIndex + 1);

        return new ExtractResult(updatedCurrentRule, amount);
    }

    static class ExtractResult {
        private final String currentString;
        private final int amount;

        public ExtractResult(String currentString, int amount) {
            this.currentString = currentString;
            this.amount = amount;
        }

        public int getAmount() {
            return amount;
        }

        public String getCurrentString() {
            return currentString;
        }

    }

    @Override
    public Optional<AsmFieldData> parse(String addr, String asmInstruction) {

        long aux = Long.parseLong(asmInstruction, 16);
        var binaryString = Long.toBinaryString(aux);
        binaryString = SpecsStrings.padLeft(binaryString, 32, '0');

        var currentString = new StringParser(binaryString);
        Map<String, String> fields = new LinkedHashMap<>();

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

        return Optional.of(new AsmFieldData(Long.parseLong(addr, 16), type, fields)); // TODO: return "fields" as
                                                                                      // optional??
    }

}
