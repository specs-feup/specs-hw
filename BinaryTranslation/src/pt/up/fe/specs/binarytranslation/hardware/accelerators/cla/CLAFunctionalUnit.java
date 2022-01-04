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

package pt.up.fe.specs.binarytranslation.hardware.accelerators.cla;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.operator.InputPort;
import pt.up.fe.specs.crispy.ast.expression.operator.OutputPort;
import pt.up.fe.specs.crispy.ast.expression.operator.Wire;
import pt.up.fe.specs.crispy.coarse.CoarseGrainUnit;
import pt.up.fe.specs.crispy.lib.MuxNto1;
import pt.up.fe.specs.crispy.lib.ShiftRegister;

public class CLAFunctionalUnit extends HardwareModule {

    public InputPort clk = addClock();
    public InputPort rst = addReset();
    public InputPort enable = addInputPort("en", 1);

    public List<InputPort> ins = new ArrayList<InputPort>();
    public List<InputPort> sels = new ArrayList<InputPort>();
    public OutputPort out;

    public CLAFunctionalUnit(CoarseGrainUnit functionalUnit,
            ShiftRegister sreg, List<MuxNto1> inmuxes) {

        super(functionalUnit.getName() + String.valueOf(functionalUnit.hashCode()).substring(0, 3));

        for (int i = 0; i < inmuxes.size(); i++)
            for (int j = 0; j < inmuxes.get(i).getSelectionCount(); j++)
                ins.add(addInputPort("in" + i + "ch" + j, functionalUnit.getPort(i).getWidth()));

        for (int i = 0; i < inmuxes.size(); i++)
            sels.add(addInputPort("sel" + i, inmuxes.get(i).getSelectionCount()));

        out = addOutputPort("out", functionalUnit.outC.getWidth());

        var auxIns = new ArrayList<Wire>();
        for (int i = 0; i < inmuxes.size(); i++)
            auxIns.add(addWire("aux" + i, functionalUnit.inA.getWidth()));

        int prevIdx = 0;
        for (int i = 0; i < inmuxes.size(); i++) {
            var selCount = inmuxes.get(i).getSelectionCount();
            instantiate(inmuxes.get(i), ins.subList(prevIdx, prevIdx + selCount), sels.get(i), auxIns.get(i));
            prevIdx += selCount;
        }

        var auxOut = addWire("fu_to_sreg", functionalUnit.outC.getWidth());
        instantiate(functionalUnit, auxIns, auxOut);

        var aux2 = addWire("sreg_to_out", functionalUnit.outC.getWidth());
        instantiate(sreg, clk, rst, enable, auxOut, aux2);
        _do(this.out.assign(aux2));
    }
}
