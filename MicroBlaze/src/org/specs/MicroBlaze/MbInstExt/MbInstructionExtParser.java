/*
 *  Copyright 2011 SPeCS Research Group.
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

package org.specs.MicroBlaze.MbInstExt;

import java.util.ArrayList;
import java.util.List;
import org.specs.MicroBlaze.CarryProperties;
import org.specs.MicroBlaze.Parsing.MbInstruction;
import org.specs.MicroBlaze.legacy.MbInstructionName;

/**
 * MbInstruction extended with additional information. Adds carry operands to
 * list of operands.
 *
 * @author Joao Bispo
 */
public class MbInstructionExtParser {

   public MbInstructionExtParser() {
      reset();
      makeImmInstNull = false;
   }

   /**
    * Adds information to MbInstructions.
    *
    * @param mbInstructions
    * @return
    */
   public List<MbInstructionExt> parse(List<MbInstruction> mbInstructions) {
      reset();

      // Make a copy of the input instructions
      List<MbInstruction> mbInstructionsClone = new ArrayList<>();
      for(MbInstruction mbInst : mbInstructions) {
         mbInstructionsClone.add(mbInst.clone());
      }

      // Replace R0 for immediate 0
      if(makeImmInstNull) {
         MbExtUtils.replaceR0(mbInstructionsClone);
      }
      // Add information which is dependent of the address order
      for(int i=0; i<mbInstructionsClone.size(); i++) {
         addInstruction(mbInstructionsClone, i);
      }

      // Remove IMM
      //MbExtImmRemover immRemover = new MbExtImmRemover();
      //extendedInsts = immRemover.removeImm(extendedInsts);

      // Replace IMM for null instructions
      if (makeImmInstNull) {
         for (int i = 0; i < extendedInsts.size(); i++) {
            MbInstructionExt extInst = extendedInsts.get(i);
            if (extInst.inst.getInstructionName() != MbInstructionName.imm) {
               continue;
            }

            extendedInsts.set(i, null);
         }
      }
       
       

      return extendedInsts;
   }  

   private void reset() {
      extendedInsts = new ArrayList<>();
   }

   private void addInstruction(List<MbInstruction> mbInstructions, int i) {
      MbInstruction mbInstruction = mbInstructions.get(i);

      Boolean branchTaken = MbExtUtils.getExitDirection(mbInstructions, i);
      boolean isExit = MbExtUtils.isExit(mbInstructions, i);
      boolean hasCarryIn = CarryProperties.usesCarryIn(mbInstruction.getInstructionName());
      boolean hasCarryOut = CarryProperties.usesCarryOut(mbInstruction.getInstructionName());
      int nextAtomicAddress = MbExtUtils.getNextAtomicAddress(mbInstructions, i);
      int noJumpAddress = MbExtUtils.getNoJumpAddress(mbInstructions, i);

  // Update: do not add carry operands, this will change the original information of the instruction.
      // Just add information, such as the booleans indicating if there are carrys or not
      // Add carry operands
      /*
      if(hasCarryIn) {
         mbInstruction.getOperands().add(MbOperand.newCarryIn());
      }
      if(hasCarryOut) {
         mbInstruction.getOperands().add(MbOperand.newCarryOut());
      }
*/
      extendedInsts.add(new MbInstructionExt(mbInstruction, branchTaken, isExit, hasCarryIn, hasCarryOut, nextAtomicAddress, noJumpAddress));
   }

   /**
    * INSTANCE VARIABLES
    */
   private List<MbInstructionExt> extendedInsts;
   private boolean makeImmInstNull;
}
