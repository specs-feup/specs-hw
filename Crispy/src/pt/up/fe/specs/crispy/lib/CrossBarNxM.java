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

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.operator.InputPort;
import pt.up.fe.specs.crispy.ast.expression.operator.OutputPort;

// public class CrossBarNxM<T extends HardwareInterface> extends HardwareModule {
public class CrossBarNxM extends HardwareModule {

    public List<InputPort> inputs;
    public List<InputPort> sel;
    public List<OutputPort> outputs;

    public CrossBarNxM(int n, int m, int dataWidth) {
        super("CrossBar" + n + "x" + m);

        // ports
        inputs = new ArrayList<InputPort>();
        for (int i = 0; i < n; i++)
            inputs.add(addInputPort("in" + i, dataWidth));

        sel = new ArrayList<InputPort>();
        for (int i = 0; i < m; i++)
            sel.add(addInputPort("sel" + i, inputs.size())); // hot bit encoding

        outputs = new ArrayList<OutputPort>();
        for (int i = 0; i < m; i++)
            outputs.add(addOutputPort("out" + i, dataWidth));

        // parametrize module
        // var baseMux = new MuxNto1(n, dataWidth); // TODO: how to force this call to be in a wrapper of type
        // "addInstance" ??

        // M instances
        for (int i = 0; i < m; i++)
            instantiate(new MuxNto1(n, dataWidth), inputs, sel.get(i), outputs.get(i));
    }
}
