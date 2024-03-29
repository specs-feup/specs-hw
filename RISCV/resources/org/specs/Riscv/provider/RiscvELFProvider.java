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
 
package org.specs.Riscv.provider;

import org.specs.Riscv.RiscvApplication;
import org.specs.Riscv.stream.RiscvElfStream;
import org.specs.Riscv.stream.RiscvTraceStream;

import pt.up.fe.specs.binarytranslation.elf.ELFProvider;
import pt.up.fe.specs.binarytranslation.stream.StaticInstructionStream;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;

public interface RiscvELFProvider extends ELFProvider {

    final static String PREFIX = "org/specs/Riscv/asm/";

    @Override
    default RiscvApplication toApplication() {
        return new RiscvApplication(this);
    }

    @Override
    default StaticInstructionStream toStaticStream() {
        return new RiscvElfStream(this);
    }

    @Override
    default TraceInstructionStream toTraceStream() {
        return new RiscvTraceStream(this);
    }

    @Override
    default public String getResource() {
        return PREFIX + this.getClass().getSimpleName() + ".7z";
    }
}
