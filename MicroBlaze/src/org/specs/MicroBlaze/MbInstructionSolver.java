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

package org.specs.MicroBlaze;

import pt.up.fe.specs.util.SpecsBits;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.asm.ArithmeticResult32;
import pt.up.fe.specs.util.SpecsAsm;

/**
 * Utility methods for solving Microblaze instructions.
 *
 * @author Joao Bispo
 */
public class MbInstructionSolver {

   public static int solveBinaryLogic(int input1, int input2,
           MbInstructionName mbInstructionName) {

      switch(mbInstructionName) {
         case and:
            return SpecsAsm.and32(input1, input2);
         case andi:
            return SpecsAsm.and32(input1, input2);
         case andn:
            return SpecsAsm.andNot32(input1, input2);
         case andni:
            return SpecsAsm.andNot32(input1, input2);
         case or:
            return SpecsAsm.or32(input1, input2);
         case ori:
            return SpecsAsm.or32(input1, input2);
         case xor:
            return SpecsAsm.xor32(input1, input2);
         case xori:
            return SpecsAsm.xor32(input1, input2);
         case cmp:
            return SpecsAsm.mbCompareSigned(input1, input2);
         case cmpu:
            return SpecsAsm.mbCompareUnsigned(input1, input2);
         case bsll:
            return SpecsAsm.shiftLeftLogical(input1, input2);
         case bslli:
            return SpecsAsm.shiftLeftLogical(input1, input2);
         case bsra:
            return SpecsAsm.shiftRightArithmetical(input1, input2);
         case bsrai:
            return SpecsAsm.shiftRightArithmetical(input1, input2);
         case bsrl:
            return SpecsAsm.shiftRightLogical(input1, input2);
         case bsrli:
            return SpecsAsm.shiftRightLogical(input1, input2);
         default:
            SpecsLogs.getLogger().
                    warning("Case not defined:"+mbInstructionName);
            return -1;
      }
   }

   public static ArithmeticResult32 solveUnaryLogic(int input1, Integer carryInValue,
           MbInstructionName mbInstructionName) {

      // Get MSB
      Integer msb = null;
      switch (mbInstructionName) {
         case sra:
            msb = SpecsBits.getBit(31, input1);
            break;
         case srl:
            msb = 0;
            break;
         case src:
            msb = carryInValue;
            break;
         default:
            SpecsLogs.getLogger().
                    warning("Case '" + mbInstructionName + "' not defined");
            break;
      }
      if (msb == null) {
         System.err.println("Error solving unary logic.");
         return null;
      }

      // Shift value right by 1
      int newValue = input1 >> 1;
      // Set/Clear the most significant bit
      if (msb == 0) {
         newValue = SpecsBits.clearBit(31, newValue);
      } else {
         newValue = SpecsBits.setBit(31, newValue);
      }

      // Get discarded bit from original input
      int carryOut = SpecsBits.getBit(0, input1);

      return new ArithmeticResult32(newValue, carryOut);
   }
}
