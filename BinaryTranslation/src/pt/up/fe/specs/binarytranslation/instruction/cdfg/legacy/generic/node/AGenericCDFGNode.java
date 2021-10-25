/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node;

public abstract class AGenericCDFGNode {

    private String name;
     
    protected AGenericCDFGNode(IGenericCDFGNodeType type) {
        this.name = type.getName();
    }
    
    protected AGenericCDFGNode(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String setName(String name) {
        this.name = name;
        return this.name;
    }
    
    public String appendToName(String append) {
        this.name = this.name + append;
        return this.name;
    }
    
    public String prependToName(String prepend) {
        this.name = prepend + this.name;
        return this.name;
    }
    
    @Override
    public String toString() {
        return this.name;
    }

    public abstract String getDotShape();
    
}
