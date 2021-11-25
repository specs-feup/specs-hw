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
 
package pt.up.fe.specs.binarytranslation.hardware.tree.nodes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import pt.up.fe.specs.util.treenode.ATreeNode;

public abstract class HardwareNode extends ATreeNode<HardwareNode> {

    protected HardwareNodeType type;

    public HardwareNode() {
        super(null);
    }

    public HardwareNodeType getType() {
        return this.type;
    }

    @Override
    public HardwareNode getThis() {
        return this;
    }

    /*
     * Content string should just be the text associate with this node
     */
    @Override
    public String toContentString() {
        return this.getClass().getSimpleName();
    }

    /*
     * getAsString should emit final code, from this node to all
     * children downwards
     */
    public String getAsString() {

        var builder = new StringBuilder();
        for (HardwareNode comp : this.getChildren()) {
            builder.append(comp.getAsString() + "\n");
        }
        return builder.toString();
    }

    /*
     * 
     */
    public void emit(OutputStream os) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(this.getAsString());
            bw.flush();
            bw.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void emit() {
        System.out.println(this.getAsString());
    }
}
