/**
 * Copyright 2019 SPeCS.
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

package pt.up.fe.specs.binarytranslation;

/**
 * A generic instruction operand, composed of type, and value
 * 
 * @author NunoPaulino
 *
 */
public interface Operand {

    /*
     * Get value as integer (good for non symbilic cases)
     * If operand isnt symbolic, getStringValue returns the
     * integer value as a String
     */
    public Integer getIntegerValue();

    /*
     * As string (good for symbolic values)
     */
    public String getStringValue();

    /*
     * Get properties of operand (can query for 
     * type, e.g., register or immediate, and other things)
     */
    public OperandProperties getProperties();

    /*
     * True if is register
     */
    public Boolean isRegister();

    /*
     * True if is immediate
     */
    public Boolean isImmediate();

    /*
     * Return formated string with <prefix><value>
     */
    public String getRepresentation();

    /*
     * Used to abstract an operand away from an 
     * executed representation, to a symbolic representation
     */
    public void setSymbolic(String value);
}
