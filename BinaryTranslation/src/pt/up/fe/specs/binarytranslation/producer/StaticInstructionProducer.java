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
 
package pt.up.fe.specs.binarytranslation.producer;

import java.util.regex.Pattern;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.InstructionInstantiator;
import pt.up.fe.specs.binarytranslation.processes.ObjDump;
import pt.up.fe.specs.specshw.processes.StringProcessRun;
import pt.up.fe.specs.specshw.processes.TxtDump;

public class StaticInstructionProducer extends AInstructionProducer {

    private final static Pattern REGEX = Pattern.compile("([0-9a-f]+):\\s([0-9a-f]+)");

    /*
     * Output from GNU based objdump
     */
    public StaticInstructionProducer(Application app, InstructionInstantiator produceMethod) {
        super(app, getProperProcess(app), REGEX, produceMethod);
    }

    private static StringProcessRun getProperProcess(Application app) {

        var name = app.getElffile().getName();
        var extension = name.subSequence(name.length() - 3, name.length());

        // Output from GNU based objdump
        if (extension.equals("elf"))
            return new ObjDump(app);

        // txt dump
        else
            return new TxtDump(app.getElffile());
    }
}
