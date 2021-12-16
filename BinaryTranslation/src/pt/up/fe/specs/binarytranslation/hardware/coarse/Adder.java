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

package pt.up.fe.specs.binarytranslation.hardware.coarse;

public class Adder extends CoarseGrainedUnit { // implements CoarseGrainFunctionalUnit , OR extends
                                               // ACorseGrainFunctionalUnit<T>

    /*
     * pre-built adder FU
     */
    public Adder(int bitwidth) {
        super(Adder.class.getSimpleName());

        addClock();
        addReset();
        var a = addInputPort("inA", bitwidth);
        var b = addInputPort("inB", bitwidth);
        var c = addOutputPort("outA", bitwidth + 1);

        var ie1 = alwaysposedge()._ifelse(getPort("rst"));
        ie1.then().nonBlocking(c, 0);
        ie1.orElse().nonBlocking(c, a.add(b));
    }
}
