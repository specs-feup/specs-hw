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

import java.util.List;

import pt.up.fe.specs.crispy.ast.definition.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.operator.HardwareOperator;
import pt.up.fe.specs.crispy.ast.expression.operator.Wire;
import pt.up.fe.specs.crispy.ast.statement.ModuleInstance;

public interface UInlineApply<T extends CoarseGrainedUnit> {

    /*
     * must be implemented by the hardware module where we want this interface to work in (?) 
     */
    public HardwareModule getParent();

    /*
     * Should declare the new instance into the ModuleBlock, and return an output port (?)
     */
    public ModuleInstance newInstance(int bitWidth, String instanceName,
            List<HardwareOperator> inputs,
            HardwareOperator output);

    public default Wire apply(List<HardwareOperator> inputs) {

        var opA = inputs.get(0);
        var auxname = opA.getResultName() + inputs.get(1).getResultName();
        var output = getParent().addWire(auxname, opA.getResultWidth());
        getParent().addInstance(newInstance(opA.getResultWidth() + 1, auxname, inputs, output));
        return output;

        /*var outputwire = (new WireDeclaration(auxname, opA.getResultWidth())).getReference();
        var module = newInstance(opA.getResultWidth()).instantiate("mod_" + auxname, connections, outputwire);
        
        return outputwire;*/

        // TODO: this might work, but I need to improve the module instantiation method..
        // Chisel3 works since all instantiations are wrapped in the Module() call, which handles
        // the backend data structure which is keeping record of things
    }
}
