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

package org.specs.MicroBlaze.isa;

import java.util.EnumMap;
import java.util.Map;

import org.specs.MicroBlaze.legacy.MbInstructionName;

/**
 *
 * @author Joao Bispo
 */
public class MbLatencies {

   public static int getLatency(MbInstructionName instName, boolean areaOptimized) {
      if(areaOptimized) {
         Integer latency = areaOptimizedLatencies.get(instName);
         if(latency != null) {
            return latency;
         }
      } else {
         Integer latency = nonAreaOptimizedLatencies.get(instName);
         if(latency != null) {
            return latency;
         }
      }

      Integer latency = regularLatencies.get(instName);
      if(latency != null) {
         return latency;
      }

      // Default latency
      return 1;
   }

   public static final Map<MbInstructionName, Integer> regularLatencies;
   public static final Map<MbInstructionName, Integer> nonAreaOptimizedLatencies;
   public static final Map<MbInstructionName, Integer> areaOptimizedLatencies;

   static {
      regularLatencies = new EnumMap<>(MbInstructionName.class);

      regularLatencies.put(MbInstructionName.wdc, 2);
      regularLatencies.put(MbInstructionName.wdc_clear, 2);
      regularLatencies.put(MbInstructionName.wdc_flush, 2);
      regularLatencies.put(MbInstructionName.wic, 2);
   }

   static {
      nonAreaOptimizedLatencies = new EnumMap<>(MbInstructionName.class);

      nonAreaOptimizedLatencies.put(MbInstructionName.fadd, 4);
      nonAreaOptimizedLatencies.put(MbInstructionName.frsub, 4);
      nonAreaOptimizedLatencies.put(MbInstructionName.fmul, 4);
      nonAreaOptimizedLatencies.put(MbInstructionName.fdiv, 28);
      nonAreaOptimizedLatencies.put(MbInstructionName.flt, 4);
      nonAreaOptimizedLatencies.put(MbInstructionName.fint, 5);
      nonAreaOptimizedLatencies.put(MbInstructionName.fsqrt, 27);
      nonAreaOptimizedLatencies.put(MbInstructionName.idiv, 32);
   }

   static {
      areaOptimizedLatencies = new EnumMap<>(MbInstructionName.class);

      areaOptimizedLatencies.put(MbInstructionName.bsrl, 2);
      areaOptimizedLatencies.put(MbInstructionName.bsra, 2);
      areaOptimizedLatencies.put(MbInstructionName.bsll, 2);
      areaOptimizedLatencies.put(MbInstructionName.bsrli, 2);
      areaOptimizedLatencies.put(MbInstructionName.bsrai, 2);
      areaOptimizedLatencies.put(MbInstructionName.bslli, 2);
      areaOptimizedLatencies.put(MbInstructionName.fadd, 6);
      areaOptimizedLatencies.put(MbInstructionName.frsub, 6);
      areaOptimizedLatencies.put(MbInstructionName.fmul, 6);
      areaOptimizedLatencies.put(MbInstructionName.fdiv, 30);
      areaOptimizedLatencies.put(MbInstructionName.fcmp_eq, 3);
      areaOptimizedLatencies.put(MbInstructionName.fcmp_ge, 3);
      areaOptimizedLatencies.put(MbInstructionName.fcmp_gt, 3);
      areaOptimizedLatencies.put(MbInstructionName.fcmp_le, 3);
      areaOptimizedLatencies.put(MbInstructionName.fcmp_lt, 3);
      areaOptimizedLatencies.put(MbInstructionName.fcmp_ne, 3);
      areaOptimizedLatencies.put(MbInstructionName.fcmp_un, 3);
      areaOptimizedLatencies.put(MbInstructionName.flt, 6);
      areaOptimizedLatencies.put(MbInstructionName.fsqrt, 29);
      areaOptimizedLatencies.put(MbInstructionName.get, 2);
      areaOptimizedLatencies.put(MbInstructionName.getd, 2);
      areaOptimizedLatencies.put(MbInstructionName.idiv, 34);
      areaOptimizedLatencies.put(MbInstructionName.lbu, 2);
      areaOptimizedLatencies.put(MbInstructionName.lbui, 2);
      areaOptimizedLatencies.put(MbInstructionName.lhu, 2);
      areaOptimizedLatencies.put(MbInstructionName.lhui, 2);
      areaOptimizedLatencies.put(MbInstructionName.lw, 2);
      areaOptimizedLatencies.put(MbInstructionName.lwi, 2);
      areaOptimizedLatencies.put(MbInstructionName.lwx, 2);
      areaOptimizedLatencies.put(MbInstructionName.mul, 3);
      areaOptimizedLatencies.put(MbInstructionName.mulh, 3);
      areaOptimizedLatencies.put(MbInstructionName.mulhsu, 3);
      areaOptimizedLatencies.put(MbInstructionName.mulhu, 3);
      areaOptimizedLatencies.put(MbInstructionName.muli, 3);
      areaOptimizedLatencies.put(MbInstructionName.put, 2);
      areaOptimizedLatencies.put(MbInstructionName.putd, 2);
      areaOptimizedLatencies.put(MbInstructionName.sb, 2);
      areaOptimizedLatencies.put(MbInstructionName.sbi, 2);
      areaOptimizedLatencies.put(MbInstructionName.sh, 2);
      areaOptimizedLatencies.put(MbInstructionName.shi, 2);
      areaOptimizedLatencies.put(MbInstructionName.sw, 2);
      areaOptimizedLatencies.put(MbInstructionName.swi, 2);
      areaOptimizedLatencies.put(MbInstructionName.swx, 2);

   }

}
