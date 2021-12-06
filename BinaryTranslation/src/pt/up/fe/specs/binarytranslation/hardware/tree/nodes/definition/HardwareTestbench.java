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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.definition;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModuleHeader;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.TimeScaleDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.FileHeader;

public class HardwareTestbench extends HardwareDefinition {

    private final String testBenchName;
    private final HardwareModule dut;

    public HardwareTestbench(String testBenchName, HardwareModule dut) {
        super(HardwareNodeType.TestBenchDefinition);
        this.testBenchName = testBenchName;
        this.dut = dut;

        // setup basic testbench stuff (testbenches have no ports!)
        this.addChild(new FileHeader());
        this.addChild(new ModuleHeader(testBenchName));
        this.addChild(new TimeScaleDeclaration());

        // TODO: move all content of @HardwareTestbenchGenerator into @Verilog as a static
        // factory like method?
    }

    @Override
    protected HardwareTestbench copyPrivate() {
        return new HardwareTestbench(this.testBenchName, this.dut);
    }

    @Override
    public HardwareTestbench copy() {
        return (HardwareTestbench) super.copy();
    }

    @Override
    public String getName() {
        return testBenchName;
    }
}
