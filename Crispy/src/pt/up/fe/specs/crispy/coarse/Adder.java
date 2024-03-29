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

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.crispy.ast.expression.operator.HardwareOperator;
import pt.up.fe.specs.crispy.ast.expression.operator.Wire;

public class Adder extends CoarseGrainUnit {// implements UInlineApply { // implements CoarseGrainFunctionalUnit ,
                                              // OR extends
    // ACorseGrainFunctionalUnit<T>

    public Adder(int bitwidth) {
        super(Adder.class.getSimpleName(), bitwidth);
        this._do(outC.nonBlocking(inA.add(inB)));
    }

    public static Wire _do(HardwareOperator... inputs) {
        return _do(Arrays.asList(inputs));
    }

    public static Wire _do(List<HardwareOperator> inputs) {
        return apply((i) -> new Adder(i), inputs);
    }
}
