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
 
package pt.up.fe.specs.binarytranslation.flow.tree;

import java.io.File;
import java.lang.reflect.Constructor;

import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.SpecsIo;

public class BinaryTranslationStreamOpen extends BinaryTranslationStep {

    private Class<?> streamClass;
    private String filename;
    private InstructionStream istream;

    public BinaryTranslationStreamOpen(String filename, Class<?> streamClass) {
        this.streamClass = streamClass;
        this.filename = filename;
    }

    @Override
    protected BinaryTranslationStep copyPrivate() {
        return new BinaryTranslationStreamOpen(this.filename, this.streamClass);
    }

    @Override
    public void execute() {
        File fd = SpecsIo.resourceCopy(filename);
        fd.deleteOnExit();

        Constructor<?> cons;
        try {
            cons = streamClass.getConstructor(File.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // open the stream
        try {
            this.istream = (InstructionStream) cons.newInstance(fd);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public InstructionStream getStream() {
        return this.istream;
    }
}
