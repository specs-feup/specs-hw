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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pt.up.specs.cgra.control.PEControlSetting;
import pt.up.specs.cgra.structure.pes.ProcessingElement;
import pt.up.specs.cgra.structure.pes.alu.ALUControlSetting;
import pt.up.specs.cgra.structure.pes.alu.ALUElement;

public class ControlDecoder {

    /*
     * helper mapping of per-instruction decoders
     * (turns integer value into appropriate enum key)
     */
    interface ControlDecode {
        PEControlSetting apply(int value);
    }

    private static final Map<Class<? extends ProcessingElement>, ControlDecode> CtrlDecoders;
    static {
        var amap = new HashMap<Class<? extends ProcessingElement>, ControlDecode>();
        amap.put(ALUElement.class, ControlDecoder::ALUDecoder);
        // amap.put("set_io", InstructionDecoder::set_io);

        CtrlDecoders = Collections.unmodifiableMap(amap);
    }

    public static ALUControlSetting ALUDecoder(int value) {

        ALUControlSetting ectrl = null;

        for (var e : ALUControlSetting.values()) {
            ectrl = e;
            if (value == ectrl.getValue()) {
                return ectrl;
            }
        }
        return null;
    }

}
