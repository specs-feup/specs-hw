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

package pt.up.fe.specs.crispy.coarse;

import java.util.List;

import pt.up.fe.specs.crispy.ast.expression.operator.HardwareOperator;
import pt.up.fe.specs.crispy.ast.statement.ModuleInstance;

public class Adder extends CoarseGrainedUnit implements UInlineApply<Adder> { // implements CoarseGrainFunctionalUnit ,
                                                                              // OR extends
    // ACorseGrainFunctionalUnit<T>

    public Adder(int bitwidth) {
        super(Adder.class.getSimpleName(), bitwidth);
        this.outC.nonBlocking(this.inA.add(this.inB));
    }

    @Override
    public ModuleInstance newInstance(int bitWidth, String instanceName,
            List<HardwareOperator> connections,
            HardwareOperator output) {

        return (new Adder(bitWidth)).instantiate(instanceName, connections, output);
    }
}
