/**
 * Copyright 2023 SPeCS.
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

package pt.up.specs.cgra.microcontroler;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.specs.cgra.control.PEControlSetting;
import pt.up.specs.cgra.structure.pes.PEType;
import pt.up.specs.cgra.structure.pes.ProcessingElement;
import pt.up.specs.cgra.structure.pes.alu.ALUControlSetting;
import pt.up.specs.cgra.structure.pes.alu.ALUElement;
import pt.up.specs.cgra.structure.pes.loadstore.LSControlSetting;
import pt.up.specs.cgra.structure.pes.loadstore.LSElement;

public class ControlDecoder {

    /*
     * helper mapping of per-instruction decoders
     * (turns integer value into appropriate enum key)
     */
    interface ControlDecode {
        boolean apply(ProcessingElement pe, int value);
    }

    private static final Map<PEType, ControlDecode> CtrlDecoders;
    static {
        var amap = new HashMap<PEType, ControlDecode>();
        amap.put(PEType.ALUElement, ControlDecoder::ALUDecoder);
        amap.put(PEType.LSElement, ControlDecoder::LSDecoder);
        CtrlDecoders = Collections.unmodifiableMap(amap);
    }

    public static boolean applyControl(ProcessingElement pe, int value) {
        var func = CtrlDecoders.get(pe.getType());
        if (func == null)
            throw new RuntimeException("ControlDecoder: attempted to control PE w/o control setting: " + pe.getAt());

        return func.apply(pe, value);
    }

    private static PEControlSetting resolve(List<? extends PEControlSetting> list, int value) {
        for (var e : list)
            if (value == e.getValue())
                return e;
        return null;
    }

    private static boolean ALUDecoder(ProcessingElement pe, int value) {
        var list = Arrays.asList(ALUControlSetting.values());
        var ctrl = (ALUControlSetting) ControlDecoder.resolve(list, value);
        var alu = (ALUElement) pe;
        return alu.setOperation(ctrl);
    }

    private static boolean LSDecoder(ProcessingElement pe, int value) {
        var list = Arrays.asList(LSControlSetting.values());
        var ctrl = (LSControlSetting) ControlDecoder.resolve(list, value);
        var ls = (LSElement) pe;
        return ls.setOperation(ctrl);
    }
}
