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

package pt.up.fe.specs.crispy.lib;

import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.operator.Port;

public class Mux2to1 extends HardwareModule {

    public Port i0;
    public Port i1;
    public Port sel;
    public Port out;

    public Mux2to1(int bitwidth) {
        super(Mux2to1.class.getSimpleName());

        i0 = addInputPort("i0", bitwidth);
        i1 = addInputPort("i1", bitwidth);
        sel = addInputPort("sel", 1);
        out = addOutputPort("out", bitwidth);

        alwayscomb("muxBlock")._ifelse(sel.not())
                .then()._do(out.nonBlocking(i0))
                .orElse()._do(out.nonBlocking(i1));
    }
}
