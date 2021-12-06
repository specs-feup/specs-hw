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
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.OutputPortDeclaration;

public abstract class AHardwareModule implements HardwareModule {

    // TODO: the HardareInstance should be either a tree itself, or contain a tree, or have single root node with a
    // header child, and the other children are other subtrees (e.g., each is a verilog module with its only
    // declaration block???)

    private final String instancename;
    private final HardwareTree tree;
    private final List<String> inputs, outputs; // raw list of port names just for instantiation

    /*
     * HardwareTree tree will have to be entirely defined so we can
     * close the specification of the module
     */
    public AHardwareModule(String instancename, HardwareTree tree) {
        this.instancename = instancename;
        this.tree = tree;

        this.inputs = new ArrayList<String>();
        for (var port : tree.getRoot().getChildrenOf(InputPortDeclaration.class))
            this.inputs.add(port.getVariableName());

        this.outputs = new ArrayList<String>();
        for (var port : tree.getRoot().getChildrenOf(OutputPortDeclaration.class))
            this.outputs.add(port.getVariableName());
    }

    /*
     * 
     */
    @Override
    public String getName() {
        return this.instancename;
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
