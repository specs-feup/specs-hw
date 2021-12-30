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

import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.operator.HardwareOperator;
import pt.up.fe.specs.crispy.ast.expression.operator.VariableOperator;
import pt.up.fe.specs.crispy.ast.expression.operator.Wire;

public interface UInlineApply {

    /*
     * must be implemented by the hardware module where we want this interface to work in (?) 
     */
    // public HardwareModule getParent();

    /*
     * Should declare the new instance into the ModuleBlock, and return an output port (?)
     
    public static <T> T getInstance(int bitWidth) {
        var c = Class<T>.get
    }*/

    public interface FunctionalUnitInstantiator {
        public HardwareModule apply(int bitWidth);
    };

    public default Wire apply(FunctionalUnitInstantiator constr,
            List<HardwareOperator> inputs) {

        // TODO: fetch parent module via the operators themselves??
        // TODO: maybe every declared node should have a reference to a kind of singleton node
        // that keeps all generation context?
        var parentmodule = ((VariableOperator) inputs.get(0)).getAssociatedIdentifier()
                .getAncestorTry(HardwareModule.class).get();
        if (parentmodule == null)
            throw new RuntimeException("UInlineApply"
                    + ": cannot apply interface method without parent HardwareModule context!");

        // TODO: problem, any HardwareOperator is a node that is not attached to a parent ModuleBlock, or HarwareBlock
        // since its a reference to a declaration; however, I might create a meta declaration block to hold all
        // generated
        // references, and then not emit that content during emit()
        // alterantively, every reference may hold a backreference to its identifier? which it already does, duh
        // UNLESS, its an immediate...

        var opA = inputs.get(0);
        var auxname = opA.getName() + inputs.get(1).getName();
        var output = parentmodule.addWire(auxname, opA.getWidth());
        parentmodule.instantiate(constr.apply(opA.getWidth() + 1), inputs, output);
        return output;

        /*var outputwire = (new WireDeclaration(auxname, opA.getResultWidth())).getReference();
        var module = newInstance(opA.getResultWidth()).instantiate("mod_" + auxname, connections, outputwire);
        
        return outputwire;*/

        // TODO: this might work, but I need to improve the module instantiation method..
        // Chisel3 works since all instantiations are wrapped in the Module() call, which handles
        // the backend data structure which is keeping record of things
    }
}
