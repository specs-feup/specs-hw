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

package pt.up.fe.specs.crispy.ast.definition;

import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.HardwareNodeType;

public abstract class HardwareDefinition extends HardwareNode {

    public HardwareDefinition(HardwareNodeType type) {
        super(type);
    }

    /*
     * 
     */
    public abstract String getName();

    /*
     * 
     
    public abstract ModuleInstance instantiate(String instanceName, List<HardwareOperator> connections);*/

    /*
     * 
     
    public final ModuleInstance instantiate(String instanceName, HardwareOperator... connections) {
        return instantiate(instanceName, Arrays.asList(connections));
    }*/

    /*
     * TODO: very important here!!
     */
    // public abstract ModuleInstance simulate(<List of stimuli and expected outputs>);

    // TODO: children
    // HardwareFunction
    // HardwareModule
    // ???
    // make HardwareModule class a child of HardwareNode???

    // Maybe make classes like CustomInstructionUnit the only ones associate with production of files
    // and they contain one or more trees of HardwareModule, each one being produced as a file
    // instead of having CustomInstructionUnit, SingleInstructionUnit, etc, themselves being
    // seen as a type of HardwareModule; makes sense since they represent whole architectures (?)
    //
    // create a new class HardwareArchitecture which is parent of CustomInstructionUnit etc?
}
