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
 
package org.specs.Arm.asm;

import org.specs.Arm.ArmApplication;
import org.specs.Arm.provider.ArmELFProvider;
import org.specs.Arm.stream.ArmStaticProducer;

import pt.up.fe.specs.binarytranslation.asm.ELFDump;

/**
 * Map holding an in memory dump of a ARM ELF; unique addresses are assumed
 * 
 * @author nuno
 *
 */
public class ArmELFDump extends ELFDump {

    public ArmELFDump(ArmApplication app) {
        super(new ArmStaticProducer(app));
    }

    public ArmELFDump(ArmELFProvider elfprovider) {
        this(elfprovider.toApplication());
    }
}
