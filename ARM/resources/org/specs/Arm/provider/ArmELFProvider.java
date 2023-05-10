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
 
package org.specs.Arm.provider;

import org.specs.Arm.ArmApplication;
import org.specs.Arm.stream.ArmElfStream;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.elf.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.stream.StaticInstructionStream;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;

public interface ArmELFProvider extends ZippedELFProvider {

    final static String PREFIX = "org/specs/Arm/asm/";

    @Override
    default ArmApplication toApplication() {
        return new ArmApplication(this);
    }

    @Override
    default StaticInstructionStream toStaticStream() {
        return new ArmElfStream(this.toApplication());
    }

    @Override
    default TraceInstructionStream toTraceStream() {
        return new ArmTraceStream(this.toApplication());
    }

    @Override
    default public String getResource() {
        return PREFIX + this.getClass().getSimpleName() + ".7z";
    }
}
