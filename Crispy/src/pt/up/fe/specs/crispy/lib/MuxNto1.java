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
import pt.up.fe.specs.crispy.ast.expression.operator.Wire;

public class MuxNto1 extends HardwareModule {

    public List<InputPort> inputs;
    public InputPort sel;
    public OutputPort out;

    public MuxNto1(int n, int dataWidth) {
        super("Mux" + n + "to1");

        // ports
        inputs = new ArrayList<InputPort>();
        for (int i = 0; i < n; i++)
            inputs.add(addInputPort("in" + i, dataWidth));

        sel = addInputPort("sel", inputs.size()); // hot bit encoding
        out = addOutputPort("out", dataWidth);

        // logic
        var auxWires = new ArrayList<Wire>();
        for (int i = 0; i < n; i++) {
            auxWires.add(addWire("aux" + i, dataWidth));
            var auxsel = sel.addSubscript(i).rep(dataWidth);
            var selbit = auxsel;
            assign(auxWires.get(i), inputs.get(i).and(selbit));
        }

        // OR all
        // var finalWire = addWire("finalOR", dataWidth);
        var prevWire = auxWires.get(0);
        for (int i = 1; i < n; i++)
            prevWire = (Wire) assign(prevWire.or(auxWires.get(i))); // TODO: fix this typechecking

        // final assign
        assign(out, prevWire);
    }
}
