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

package pt.up.fe.specs.binarytranslation.hardware;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.tree.HardwareTree;
import pt.up.fe.specs.crispy.ast.meta.HardwareRootNode;

public abstract class AHardwareArchitecture implements HardwareArchitecture {

    // TODO: the HardareInstance should be either a tree itself, or contain a tree, or have single root node with a
    // header child, and the other children are other subtrees (e.g., each is a verilog module with its only
    // declaration block???)

    private final String moduleName;
    private final HardwareRootNode tree; // TODO: replace with list of modules that make up the design
    private final List<String> inputs, outputs; // raw list of port names just for instantiation

    /*
     * HardwareTree tree will have to be entirely defined so we can
     * close the specification of the module
     
    public AHardwareModule(String moduleName, HardwareTree tree) {
        this.moduleName = moduleName;
        this.tree = tree;
    
        this.inputs = new ArrayList<String>();
        for (var port : tree.getRoot().getChildrenOf(InputPortDeclaration.class))
            this.inputs.add(port.getVariableName());
    
        this.outputs = new ArrayList<String>();
        for (var port : tree.getRoot().getChildrenOf(OutputPortDeclaration.class))
            this.outputs.add(port.getVariableName());
    }*/

    public AHardwareArchitecture(String moduleName, HardwareTree tree) {

        this.moduleName = moduleName;
        this.tree = tree;

        this.inputs = new ArrayList<String>();
        for (var port : tree.getInputPortDeclarations())
            this.inputs.add(port.getVariableName());

        this.outputs = new ArrayList<String>();
        for (var port : tree.getOutputPortDeclarations())
            this.outputs.add(port.getVariableName());
    }

    /*
    private static List<String> getInputPorts(HardwareTree tree) {
        var list = new ArrayList<String>();
        for (var port : tree.getPortDeclarations()) {
            if (port.getDirection() == ModulePortDirection.input)
                list.add(port.getVariableName());
        }
        return list;
    }
    
    private static List<String> getOutputPorts(HardwareTree tree) {
        var list = new ArrayList<String>();
        for (var port : tree.getPortDeclarations()) {
            if (port.getDirection() == ModulePortDirection.output)
                list.add(port.getVariableName());
        }
        return list;
    }*/

    /*
     * 
     */
    @Override
    public String getName() {
        return this.moduleName;
    }

    /*
     * 
     */
    @Override
    public List<String> getPorts() {
        var all = new ArrayList<String>();
        all.addAll(this.inputs);
        all.addAll(this.outputs);
        return all;
    }

    @Override
    public List<String> getInputPorts() {
        return this.inputs;
    }

    @Override
    public List<String> getOutputPorts() {
        return this.outputs;
    }

    @Override
    public void emit(OutputStream os) {
        this.tree.emit(os);
    }

    @Override
    public void emit() {
        this.tree.emit();
    }
}
