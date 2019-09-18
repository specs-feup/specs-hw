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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.specs.MicroBlaze.MbRegister;
import org.specs.MicroBlaze.OperationProperties;
import org.specs.MicroBlaze.Parsing.MbInstruction;
import org.specs.MicroBlaze.Parsing.MbOperand;

/**
 * Keeps tracks of accesses to memory addresses which uses R1.
 *
 * @author Joao Bispo
 */
public class MbStackAnalyser {

   public MbStackAnalyser() {
      //instructionsWithR1 = new HashMap<Integer, String>();
      //stackRegisters = new HashSet<Integer>();
      stackRegisters = new HashSet<>();
      stackRegisters.add(MB_STACK_REGISTER);
      //stackRegisters.add(MbRegisterId.R1.getName());
      stackData = new StackData();
   }

   public StackData getStackData() {
      return stackData;
   }

   public boolean nextInstruction(int address, String instruction) {
      MbInstruction mbInst = MbInstruction.create(address, instruction);
      return nextInstruction(mbInst);
   }

   /**
    *
    * @param mbInst
    * @return true if the instruction uses the stack register. False otherwise
    */
   public boolean nextInstruction(MbInstruction mbInst) {
      

      /*
     boolean a = checkR1(mbInst);
     if(a) {
        System.out.println(mbInst);
     }
       *
       */
       
       
      // Add address to table

      //System.out.println(mbInst.getOperands().get(0).getType());
      //System.out.println(mbInst.getOperands().get(0).getIntValue());

      // Determine if instruction uses R1
      boolean usesR1 = usesR1(mbInst);

      if(usesR1) {
         //System.out.println(mbInst);
         //totalCounter++;
         stackData.totalCounter++;
         if(OperationProperties.isMemoryOperation(mbInst.getInstructionName())) {
            //memCounter++;
            stackData.memCounter++;
         }
         //if(!instructionsWithR1.containsKey(address)) {
         if(!stackData.addressesWhichUseR1.contains(mbInst.getAddress())) {
            //instructionsWithR1.put(address, instruction);
            stackData.addressesWhichUseR1.add(mbInst.getAddress());
         }
         
      }

      
      // Check outputs
      checkOutputs(mbInst, usesR1);
/*
      if(instructionsWithR1.containsKey(address)) {
         return;
      }

      instructionsWithR1.put(address, instruction);
*/
      return usesR1;
   }

   /**
    * Check inputs to see if they are stack registers.
    *
    * @param mbInst
    * @return
    */
   private boolean usesR1(MbInstruction mbInst) {
      for(MbOperand op : mbInst.getOperands()) {

         if(!MbOperand.isInput(op.getFlow())) {
            continue;
         }

         if(!checkStackReg(op)) {
            continue;
         }

         return true;
      }

      return false;
   }


/*
   private boolean checkR1(MbOperand op) {
         if(op.getType() != MbOperand.Type.register) {
            return false;
         }

         if(op.getIntValue() != 1) {
            return false;
         }

         // Found R1
         return true;
   }
*/
   private boolean checkStackReg(MbOperand op) {
         if(op.getType() != MbOperand.Type.REGISTER) {
            return false;
         }

         //if(!stackRegisters.contains(op.getIntValue())) {
         if(!stackRegisters.contains(op.getId())) {
            return false;
         }

         // Found stack register on operand
         return true;
   }

   private void checkOutputs(MbInstruction mbInst, boolean usesR1) {
      // Check if uses stack registers and is an add
      boolean isAdd = OperationProperties.isAdd(mbInst.getInstructionName());
      // Add output to stack registers
      if(isAdd & usesR1) {
         for(MbOperand op : mbInst.getOperands()) {
            if(MbOperand.isInput(op.getFlow())) {
               continue;
            }
            if(op.getType() != MbOperand.Type.REGISTER) {
               continue;
            }

            // Is output
            //if(stackRegisters.contains(op.getIntValue())) {
            if(stackRegisters.contains(op.getId())) {
               continue;
            }

            //stackRegisters.add(op.getIntValue());
            stackRegisters.add(op.getId());
            //System.out.println("Added op "+op);
         }
      } else {
         // If instruction is not an add with stack registers, remove from set
         List<MbOperand> ops = mbInst.getOperands(MbOperand.Flow.WRITE, MbOperand.Type.REGISTER);
         for(MbOperand op : ops) {
            //if(!stackRegisters.contains(op.getIntValue())) {
            if(!stackRegisters.contains(op.getId())) {
               continue;
            }

            //if(op.getIntValue() == MB_STACK_REGISTER) {
            if(op.getId().equals(MB_STACK_REGISTER)) {
               continue;
            }

            // Is output
            //stackRegisters.remove(op.getIntValue());
            stackRegisters.remove(op.getId());
         }
         /*
            for(MbOperand op : mbInst.getOperands()) {
            if(MbOperand.isInput(op.getFlow())) {
               continue;
            }
            if(op.getType() != MbOperand.Type.register) {
               continue;
            }

            if(!stackRegisters.contains(op.getIntValue())) {
               continue;
            }

            if(op.getIntValue() == MB_STACK_REGISTER) {
               continue;
            }

            // Is output
            stackRegisters.remove(op.getIntValue());
            //System.out.println("Removed op "+op);
         }
          * 
          */
      }

      /*
      if(!usesR1) {
         for(MbOperand op : mbInst.getOperands()) {
            if(MbOperand.isInput(op.getFlow())) {
               continue;
            }
            if(op.getType() != MbOperand.Type.register) {
               continue;
            }

            if(!stackRegisters.contains(op.getIntValue())) {
               continue;
            }

            if(op.getIntValue() == MB_STACK_REGISTER) {
               continue;
            }

            // Is output
            stackRegisters.remove(op.getIntValue());
            //System.out.println("Removed op "+op);
         }
      }
      */
   }

   /**
    * INSTANCE VARIABLES
    */
   //public Map<Integer, String> instructionsWithR1;
   //private Set<Integer> stackRegisters;
   private Set<String> stackRegisters;
   //public long totalCounter = 0l;
   //public long memCounter = 0l;
   private StackData stackData;



   //public static final int MB_STACK_REGISTER = 1;
   public static final String MB_STACK_REGISTER = MbRegister.R1.getName();

}
