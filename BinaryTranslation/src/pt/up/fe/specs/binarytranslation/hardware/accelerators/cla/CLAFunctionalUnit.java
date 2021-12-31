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

    public InputPort ins;
    public InputPort sels;
    public OutputPort out;

    public CLAFunctionalUnit(CoarseGrainUnit functionalUnit,
            ShiftRegister sreg, List<MuxNto1> inmuxes) {

        super(functionalUnit.getName() + String.valueOf(functionalUnit.hashCode()).substring(0, 3));

        int totalselBits = 0;
        int totalInputBits = 0;
        for (int i = 0; i < inmuxes.size(); i++) {
            var selcount = inmuxes.get(i).getSelectionCount();
            var portSize = functionalUnit.getPort(i).getWidth();
            totalInputBits += selcount * portSize;
            totalselBits += selcount;
        }

        ins = addInputPort("ins", totalInputBits);
        sels = addInputPort("sels", totalselBits);

        var auxIns = new ArrayList<Wire>();
        auxIns.add(addWire("auxA", functionalUnit.inA.getWidth()));
        auxIns.add(addWire("auxB", functionalUnit.inB.getWidth()));

        int lastIdx = 0;
        int lastSelIdx = 0;
        for (int i = 0; i < inmuxes.size(); i++) {
            var selcount = inmuxes.get(i).getSelectionCount();
            var portSize = functionalUnit.getPort(i).getWidth();
            var totalBits = selcount * portSize;
            instantiate(inmuxes.get(i),
                    ins.idx(totalBits + lastIdx, lastIdx),
                    sels.idx(selcount + lastSelIdx, lastSelIdx),
                    auxIns.get(i));
            lastIdx += totalBits;
            lastSelIdx += selcount;
        }

        var auxOut = addWire("fu_to_sreg", functionalUnit.outC.getWidth());
        instantiate(functionalUnit, auxIns, auxOut);

        var aux2 = addWire("sreg_to_out", functionalUnit.outC.getWidth());
        instantiate(sreg, clk, rst, enable, auxOut, aux2);
        _do(this.out.assign(aux2));
    }
}
