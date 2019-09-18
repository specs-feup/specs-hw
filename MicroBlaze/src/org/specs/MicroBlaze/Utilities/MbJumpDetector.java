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
package org.specs.MicroBlaze.Utilities;

import org.specs.MicroBlaze.MbInstructionName;
import org.specs.MicroBlaze.OperationProperties;
import org.specs.MicroBlaze.Parsing.MbInstruction;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.asm.processor.DelaySlotBranchCorrector;
import pt.up.fe.specs.util.asm.processor.JumpDetector;

/**
 * MicroBlaze implementation of a Jump detector.
 *
 * @author Joao Bispo
 */
public class MbJumpDetector implements JumpDetector {

   public MbJumpDetector() {
      branchCorrector = new DelaySlotBranchCorrector();
      previousAddress = null;
      currentAddress = null;
   }

   @Override
public void giveInstruction(Object instruction) {
      // Only accepts MbInstructions
      MbInstruction mbInstruction = null;
      try {
         mbInstruction = (MbInstruction) instruction;
      } catch (ClassCastException ex) {
         SpecsLogs.getLogger().
                 warning(ex.toString());
         SpecsLogs.getLogger().
                 warning("This class only supports objects of the class '" + MbInstruction.class + "'.");
         return;
      }

      giveInstruction(mbInstruction);
   }

   public void giveInstruction(MbInstruction mbInstruction) {
      // Get info from instruction
      updateAddress(mbInstruction.getAddress());

      MbInstructionName name = mbInstruction.getInstructionName();
      boolean isJump = OperationProperties.isJump(name);
      int delaySlots = OperationProperties.getDelaySlots(name);
      if(isJump) {
         lastJumpWasConditional = OperationProperties.isConditionalJump(name);
      }

      branchCorrector.giveInstruction(isJump, delaySlots);
      //jumpHappened = branchCorrector.wasJumpPoint();
      //wasJumpPoint = branchCorrector.isJumpPoint(isJumpPoint, delaySlots);
      
   }

   @Override
public boolean wasJumpPoint() {
      return branchCorrector.wasJumpPoint();
      //return jumpHappened;
   }

   @Override
public Boolean wasConditionalJump() {
      if (!wasJumpPoint()) {
         return null;
      }

      return lastJumpWasConditional;
   }


   @Override
public boolean isJumpPoint() {
      return branchCorrector.isJumpPoint();
   }

   @Override
public Boolean isConditionalJump() {
      if (!isJumpPoint()) {
         return null;
      }

      return lastJumpWasConditional;
   }

   @Override
public Boolean wasForwardJump() {
      if (!wasJumpPoint()) {
         return null;
      }

      return isForward();
   }

   @Override
public Boolean wasBranchTaken() {
      if (!wasJumpPoint()) {
         return null;
      }

      if (!lastJumpWasConditional) {
         return null;
      }

      if (getInstructionStep() == DEFAULT_STEP) {
         return false;
      }

      return true;

   }

   private void updateAddress(int address) {
      previousAddress = currentAddress;
      currentAddress = address;
   }

   private boolean isForward() {
      if (previousAddress == null || currentAddress == null) {
         SpecsLogs.getLogger().
                 warning("Check if program should arrive here");
         return true;
      }

      int step = getInstructionStep();

      if (step > 0) {
         return true;
      }

      if (step < 0) {
         return false;
      }

      SpecsLogs.getLogger().
              warning("Instructions have same address.");
      return false;
   }

   private int getInstructionStep() {
      if (previousAddress == null || currentAddress == null) {
         return DEFAULT_STEP;
      }

      return currentAddress - previousAddress;
   }
   private DelaySlotBranchCorrector branchCorrector;
   private Integer previousAddress;
   private Integer currentAddress;
   //private boolean jumpHappened;
   private boolean lastJumpWasConditional;
   public static final int DEFAULT_STEP = 4;

}
