/*
 *  Copyright 2010 SPeCS Research Group.
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

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Information about the carry in MicroBlaze operations.
 *
 * @author Joao Bispo
 */
public class CarryProperties {

   public CarryProperties(boolean hasCarryIn, boolean hasCarryOut) {
      this.hasCarryIn = hasCarryIn;
      this.hasCarryOut = hasCarryOut;
   }

   /**
    * INSTANCE VARIABLES
    */
   private boolean hasCarryIn;
   private boolean hasCarryOut;
   private static final Map<MbInstructionName, CarryProperties> instructionProperties;

   static {
      Map<MbInstructionName, CarryProperties> aMap = new EnumMap<>(MbInstructionName.class);

      aMap.put(MbInstructionName.add, new CarryProperties(false, true));
      aMap.put(MbInstructionName.addc, new CarryProperties(true, true));
      aMap.put(MbInstructionName.addi, new CarryProperties(false, true));
      aMap.put(MbInstructionName.addic, new CarryProperties(true, true));
      aMap.put(MbInstructionName.addik, new CarryProperties(false, false));
      aMap.put(MbInstructionName.addikc, new CarryProperties(true, false));
      aMap.put(MbInstructionName.addk, new CarryProperties(false, false));
      aMap.put(MbInstructionName.addkc, new CarryProperties(true, false));

      aMap.put(MbInstructionName.sra, new CarryProperties(false, true));
      aMap.put(MbInstructionName.src, new CarryProperties(true, true));
      aMap.put(MbInstructionName.srl, new CarryProperties(false, true));

      aMap.put(MbInstructionName.rsub, new CarryProperties(false, true));
      aMap.put(MbInstructionName.rsubc, new CarryProperties(true, true));
      aMap.put(MbInstructionName.rsubi, new CarryProperties(false, true));
      aMap.put(MbInstructionName.rsubic, new CarryProperties(true, true));
      aMap.put(MbInstructionName.rsubik, new CarryProperties(false, false));
      aMap.put(MbInstructionName.rsubikc, new CarryProperties(true, false));
      aMap.put(MbInstructionName.rsubk, new CarryProperties(false, false));
      aMap.put(MbInstructionName.rsubkc, new CarryProperties(true, false));


      instructionProperties = Collections.unmodifiableMap(aMap);
   }


   public static boolean usesCarryIn(MbInstructionName instructionName) {
      CarryProperties props = instructionProperties.get(instructionName);

      if (props == null) {
         return false;
      }

      return props.hasCarryIn;
   }

   public static boolean usesCarryOut(MbInstructionName instructionName) {
      CarryProperties props = instructionProperties.get(instructionName);

      if (props == null) {
         return false;
      }

      return props.hasCarryOut;
   }

}


