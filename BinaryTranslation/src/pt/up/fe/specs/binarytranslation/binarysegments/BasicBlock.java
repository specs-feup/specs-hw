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

package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class BasicBlock extends ABinarySegment {

    private boolean terminated = false;
    private int delaycounter = 0;
    // value must be true if an instruction is added
    // which is a backwards branch; no other instructions
    // may be added

    /*
     * Constructor for building the BB inst by inst later
     */
    public BasicBlock() {
        super();
        this.segtype = SegmentType.STATIC_BASIC_BLOCK;
    }

    /*
     * Constructor builds the BB on the spot with an existing list
     */
    public BasicBlock(List<Instruction> ilist) {
        super();
        this.instlist = ilist;
        this.segtype = SegmentType.STATIC_BASIC_BLOCK;
        buildLiveInsAndLiveOuts();
    }
    /*
    public void addInst(Instruction newinst) {
    
        // cannot (should not!) add any more
        if (this.terminated == true && this.delaycounter == 0) {
            // TODO proper exception handling
            return;
        }
    
        // should not be able to add more branches
        else if (this.terminated == true && newinst.isJump()) {
            // TODO proper exception handling
            return;
        }
    
        // should not be able to add forward jumps
        else if (newinst.isJump() && !newinst.isBackwardsJump()) {
            // TODO proper exception handling
            return;
        }
    
        /// add
        else {
    
            this.instlist.add(newinst);
    
            // countdown to pipe fill due to delay
            if (this.delaycounter > 0) {
                this.delaycounter--;
                if (this.delaycounter == 0) {
                    buildLiveIns();
                    buildLiveOuts();
                    // finally actually over
    
                    // TODO refactor this method "addInst"
                }
            }
    
            // if added inst is branch allow
            // addition of at most getDelay insts after this
            if (newinst.isBackwardsJump() && newinst.isConditionalJump()) {
                this.terminated = true; // only one branch permitted
                this.delaycounter = newinst.getDelay();
            }
        }
    }
    */
}
