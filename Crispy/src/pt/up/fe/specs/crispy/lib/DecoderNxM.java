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

import pt.up.fe.specs.crispy.ast.definition.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.operator.Immediate;
import pt.up.fe.specs.crispy.ast.expression.operator.Port;

public class DecoderNxM extends HardwareModule {

    public Port in;
    public Port out;

    public DecoderNxM(int n) {
        super("Decoder" + n + "x" + (int) Math.pow(n, 2));
        in = addInputPort("in", n);
        out = addOutputPort("out", (int) Math.pow(n, 2));
        assign(out, Immediate.Ones(1).lsl(in));
    }
}
