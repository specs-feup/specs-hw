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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node;

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;

/** Class that is the parent of all nodes of an InstructionCDFG. Because of some weirdness, also the Modifiers of the edges are nodes (this will probably change)
 * 
 * @author João Conceição
 *
 */
public abstract class AInstructionCDFGNode{

    private String reference;   // Reference of the node, used for checking if different data nodes represent the same variable/literal. Also used for naming the node when exporting
    private String uid = ""; // Unique Id of the node, mostly needed for exporting DOT and in the future for generating signals in HDL
    
    
    protected AInstructionCDFGNode (String reference) {
        this.reference = reference;
    }
    
    /** Returns the reference of the node<br>
     *  Data nodes that represent the same variable/literal have the same reference
     * @return The reference of the data node
     */
    public String getReference() {
        return this.reference;
    }
    
    /** Sets the UID value for the node
     * 
     * @param uid The UID for this node
     */
    public void setUID(String uid) {
        this.uid = uid;
    }
    
    public void setUID(Integer uid) {
        this.setUID(String.valueOf(uid));
    }
    
    /** Returns the UID value of the node
     * 
     * @return The UID value
     */
    public String getUIDVal() {
        return this.uid;
    }
    
    /** Returns the unique reference of the node, that is composed by the node's reference and UID
     * 
     * @return The unique reference of the node
     */
    public String getUID() {
        return this.getReference() + this.uid;
    }
    
    @Override
    public String toString() {
        return this.getReference();
    }
    
    /** Returns an Attribute, an object type required by the DOT exporter<br>
     * The returned label is the reference of the node
     * @return The Attribute of the DOT node label
     */
    public Attribute getDOTLabel() {
        return DefaultAttribute.createAttribute(this.getReference() + "\\n" + this.getUID());
    }
    
    /** Returns an Attribute, an object type required by the DOT exporter <br>
     * By default it returns a circle
     * @return The Attribute of the DOT node shape
     */
    public Attribute getDOTShape() {
        return DefaultAttribute.createAttribute("circle");
    }
    
    /*
    @Override
    public boolean equals(Object obj) {
        
        if(obj instanceof AInstructionCDFGNode) {
            return (this.hashCode() == ((AInstructionCDFGNode) obj).hashCode());
        }
        
        return false;
    }
    
    
    @Override
    public int hashCode() {
        return this.getReference().hashCode();
    }*/
}
