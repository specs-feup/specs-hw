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

import java.util.Map;

import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.stringparser.StringParser;
import pt.up.fe.specs.util.stringparser.StringParsers;

public class ConstantRule implements BinaryAsmRule {

    private final String string;

    public ConstantRule(String string) {
        this.string = string;
    }

    public ConstantRule(char c, int numChars) {
        this(SpecsStrings.buildLine(Character.toString(c), numChars));
    }

    @Override
    public boolean apply(StringParser currentString, Map<String, String> fields) {
        return currentString.apply(StringParsers::checkStringStarts, string).isPresent();
    }

    @Override
    public String toString() {
        return "Constant " + string;
    }
}
