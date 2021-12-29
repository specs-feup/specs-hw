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

import pt.up.fe.specs.crispy.ast.definition.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.operator.Port;

public abstract class CoarseGrainedUnit extends HardwareModule {

    // TODO: parametrize this with a generic that specifies the interface of the module??

    // todo methods for single line connections of modules with equivalent interfaces

    public Port inA;
    public Port inB;
    public Port outC;

    public CoarseGrainedUnit(String name, int bitwidth) {
        super(name);
        inA = addInputPort("inA", bitwidth);
        inB = addInputPort("inB", bitwidth);
        outC = addOutputPort("outA", bitwidth + 1);
    }
}