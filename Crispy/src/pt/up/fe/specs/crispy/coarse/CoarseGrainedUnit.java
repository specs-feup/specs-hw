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

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.crispy.ast.definition.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.operator.HardwareOperator;
import pt.up.fe.specs.crispy.ast.expression.operator.Port;
import pt.up.fe.specs.crispy.ast.expression.operator.VariableOperator;
import pt.up.fe.specs.crispy.ast.expression.operator.Wire;

public abstract class CoarseGrainedUnit extends HardwareModule {

    // TODO: parametrize this with a generic that specifies the interface of the module??

    // todo methods for single line connections of modules with equivalent interfaces

    public Port inA;
    public Port inB;
    public Port outC;
    protected static FunctionalUnitInstantiator constr;

    protected CoarseGrainedUnit(String name, int bitwidth) {
        super(name);
        inA = addInputPort("inA", bitwidth);
        inB = addInputPort("inB", bitwidth);
        outC = addOutputPort("outA", bitwidth + 1);
    }

    public static Wire _do(HardwareOperator... inputs) {
        return _do(Arrays.asList(inputs));
    }

    public static Wire _do(List<HardwareOperator> inputs) {
        return apply(constr, inputs);
    }

    protected interface FunctionalUnitInstantiator {
        public HardwareModule apply(int bitWidth);
    };

    protected static Wire apply(FunctionalUnitInstantiator constr,
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
        var auxname = opA.getResultName() + inputs.get(1).getResultName();
        var output = parentmodule.addWire(auxname, opA.getResultWidth());
        parentmodule.instantiate(constr.apply(opA.getResultWidth() + 1), inputs, output);
        return output;

        /*var outputwire = (new WireDeclaration(auxname, opA.getResultWidth())).getReference();
        var module = newInstance(opA.getResultWidth()).instantiate("mod_" + auxname, connections, outputwire);
        
        return outputwire;*/

        // TODO: this might work, but I need to improve the module instantiation method..
        // Chisel3 works since all instantiations are wrapped in the Module() call, which handles
        // the backend data structure which is keeping record of things
    }
}
