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
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.hardware.tree.HardwareTree;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.OutputPortDeclaration;

public abstract class AHardwareInstance implements HardwareInstance {

    // TODO: the HardareInstance should be either a tree itself, or contain a tree, or have single root node with a
    // header child, and the other children are other subtrees (e.g., each is a verilog module with its only
    // declaration block???)

    protected String instancename;
    protected HardwareTree tree;

    public AHardwareInstance(String instancename, HardwareTree tree) {
        this.instancename = instancename;
        this.tree = tree;
    }

    public String getName() {
        return this.instancename;
    }
    
    public List<HardwareNode> getPorts(){
        return  this.getTree().getRoot().getChild(1).getChild(0).getChildren();
    }
    
    private List<HardwareNode> getPorts(Predicate<HardwareNode> portType){
        return  this.getPorts().stream().filter(portType).collect(Collectors.toList());
    }
    
    public List<HardwareNode> getInputPorts(){
        return this.getPorts(port -> port instanceof InputPortDeclaration);
    }
    
    public List<HardwareNode> getOutputPorts(){
        return this.getPorts(port -> port instanceof OutputPortDeclaration);
    }
    
    
    @Override
    public void emit(OutputStream os) {
        this.tree.emit(os);
    }

    @Override
    public void emit() {
        this.tree.emit();
    }
    
    @Override
    public HardwareTree getTree() {
        return this.tree;
    }
}
