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

import java.util.List;

import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.declaration.port.PortDeclaration;
import pt.up.fe.specs.crispy.ast.meta.FileHeader;
import pt.up.fe.specs.crispy.ast.statement.ModuleInstance;
import pt.up.fe.specs.specshw.SpecsHwUtils;

public class HardwareModule extends HardwareDefinition implements ModuleBlockInterface {

    protected static int MAXCHILDREN;
    protected static String ADDCHILDERRMSG;
    static {
        MAXCHILDREN = 2;
        ADDCHILDERRMSG = "HardwareModule: Expected only two children! " +
                "Use addStatement() and addBlock() to add content to the module body!";
    }

    // public assignMethods assign;

    /*
     * Outer most node of the Hardware module definition, 
     * which includes copyright, header and body
     */
    public HardwareModule(String moduleName) {
        super(HardwareNodeType.HardwareModule);

        // child 0
        this.addChild(new FileHeader(SpecsHwUtils.generateFileHeader()));

        // child 1
        this.addChild(new ModuleBlock(moduleName));

        // this.assign = new assignMethods(this.getBody());
    }

    public HardwareModule(String moduleName, PortDeclaration... ports) {
        this(moduleName);
        for (var port : ports)
            this.addPort(port);
    }

    /* *****************************
     * For copying (children are handled as usual by @ATreeNode.copy)
     */
    protected HardwareModule(HardwareNodeType type) {
        super(type);
    }

    @Override
    public ModuleBlock getBody() {
        return this.getChild(ModuleBlock.class, 1);
    }

    @Override
    public int incrementCombCounter() {
        return getBody().incrementCombCounter();
    }

    @Override
    public int incrementFFCounter() {
        return getBody().incrementFFCounter();
    }

    @Override
    public List<PortDeclaration> getPortList() {
        return this.getBody().getPortList();
    }

    @Override
    public HardwareNode addChild(HardwareNode node) {
        if (getNumChildren() >= MAXCHILDREN)
            throw new RuntimeException(ADDCHILDERRMSG);
        return super.addChild(node);
    }

    /* *****************************
     * Public stuff
     
    @Override
    public ModuleInstance instantiate(String instanceName, List<HardwareOperator> connections) {
        return new ModuleInstance(this, instanceName, connections);
    }*/

    @Override
    public String getName() {
        return getBody().moduleName;
    }

    @Override
    protected HardwareModule copyPrivate() {
        return new HardwareModule(this.type);
    }

    @Override
    public HardwareModule copy() {
        return (HardwareModule) super.copy();
    }

    @Override
    public void emit() {

        // emit this
        super.emit();

        var childrenModules = this.getBody().getChildrenOf(ModuleInstance.class);
        for (var mod : childrenModules) {
            var def = mod.getModuleDefinition();
            def.emit();
        }
    }
}
