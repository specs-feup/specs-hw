/**
 * Copyright 2022 SPeCS.
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

package pt.up.fe.specs.crispy.test.workshop;

import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.operator.InputPort;
import pt.up.fe.specs.crispy.ast.expression.operator.OutputPort;

public class Add3 extends HardwareModule {

    public InputPort inA = addInputPort("inA", 8);
    public InputPort inB = addInputPort("inB", 8);
    public InputPort inC = addInputPort("inC", 8);
    public OutputPort outD = addOutputPort("outD", 8);

    public Add3() {
        super(Add3.class.getSimpleName());

        var aux1 = addWire("aux1", 8);
        instantiate(new Add(), inA, inB, aux1);
        instantiate(new Add(), aux1, inC, outD);
    }
}
