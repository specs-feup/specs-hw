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
 
package pt.up.fe.specs.binarytranslation.tracer;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public interface StreamUnit {

    /*
     * Deep copy
     
    public StreamUnit deepCopy();*/

    /*
     * 
     */
    public StreamUnitType getType();

    /*
     * 
     */
    public Instruction getStart();

    /*
     * 
     */
    public Instruction getEnd();

    /*
     * 
     */
    public Long getTargetAddr();

    /*
     * 
     */
    public boolean containsAddr(Long addr);

    /*
     * True if addresses of two units follow
     * i.e. if "this" comes after "other" 
     */
    public boolean follows(StreamUnit other);

    /*
     * True if addresses of two units follow
     * i.e. if "other" comes after "this"
     */
    public boolean precedes(StreamUnit other);

    /*
     * True if any instruction in this TraceUnit
     * includes the target of the "other"
     */
    public boolean includesTarget(StreamUnit other);

    /*
     * True if "this" jumps to "other"
     */
    public boolean jumpsTo(StreamUnit other);

    /*
     * True if "other" jumps to "this"
     */
    public boolean targetOf(StreamUnit other);
}
