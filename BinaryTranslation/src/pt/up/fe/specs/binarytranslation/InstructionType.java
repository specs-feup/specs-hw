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
 * List generic instruction types All instructions from any ISA should be mapped to at least one of these types after
 * being parsed and abstracted
 * 
 * @author NunoPaulino
 *
 */
public enum InstructionType {

    /*
     * Generic instruction types.
     * Specific architecture implementations 
     * must map each instruction in the ISA
     * to one of these types 
     */
    G_ADD,
    G_SUB,
    G_MUL,
    G_LOGICAL,
    G_UNARY,
    G_JUMP, // is jump
    G_CJUMP, // jump is conditional
    G_UJUMP, // jump is unconditional
    G_RJUMP, // jump is relative
    G_AJUMP, // jump is absolute
    G_IJUMP, // jump with immediate value
    G_STORE,
    G_LOAD,
    G_MEMORY,
    G_FLOAT,
    G_CMP,
    G_OTHER,
    G_UNKN // TODO should fire off an exception
    // TODO add more types
}
