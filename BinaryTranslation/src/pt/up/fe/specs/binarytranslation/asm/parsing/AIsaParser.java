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

package pt.up.fe.specs.binarytranslation.asm.parsing;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author Nuno
 *
 */
public abstract class AIsaParser implements IsaParser {

    private final List<AsmParser> instructionParsers;
    private final Set<String> allowedFields;

    public AIsaParser(List<AsmParser> instructionParsers, Set<String> allowedFields) {
        this.instructionParsers = instructionParsers;
        this.allowedFields = allowedFields;
    }

    public AIsaParser(List<AsmParser> instructionParsers) {
        this(instructionParsers, null);
    }

    protected AsmFieldData doparse(String addr, String instruction) {
        // Iterate over all parsers
        for (var parser : instructionParsers) {
            var instData = parser.parse(addr, instruction).orElse(null);

            // Parser not successful, try next one
            if (instData == null) {
                continue;
            }

            // If allowed fields set, test parsed fields
            if (allowedFields != null) {
                var fields = instData.get(AsmFieldData.FIELDS);

                if (!allowedFields.containsAll(fields.keySet())) {
                    Set<String> undefinedKeys = new HashSet<>(fields.keySet());
                    undefinedKeys.removeAll(allowedFields);

                    throw new RuntimeException(
                            "Found undefined fields: " + undefinedKeys + "\nAllowed fields: " + allowedFields);
                }

            }

            return instData;
        }

        throw new RuntimeException("Could not parse instruction: " + instruction);
    }
}
