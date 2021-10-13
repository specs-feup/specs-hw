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
 
package pt.up.fe.specs.binarytranslation;

import java.io.File;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.stream.StaticInstructionStream;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.providers.ResourceProvider;

public interface ELFProvider extends ResourceProvider {

    default public Application toApplication() {
        throw new NotImplementedException("toApplication()");
    }

    default public String getFunctionName() {
        return SpecsIo.removeExtension(this.getELFName());
    }

    default public StaticInstructionStream toStaticStream() {
        throw new NotImplementedException("toStaticStream()");
    }

    default public TraceInstructionStream toTraceStream() {
        throw new NotImplementedException("toTraceStream()");
    }

    public String getELFName();

    default File getFile() {
        return this.write();
    }
}
