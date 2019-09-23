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

package org.specs.MicroBlaze.legacy;

import java.util.HashMap;
import java.util.Map;

import org.specs.MicroBlaze.isa.MbInstructionName;

import pt.up.fe.specs.util.SpecsEnums;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Maps MicroBlaze instruction names to MbInstructionName enumeration.
 *
 * @author Joao Bispo
 */
public class MbInstNameDispenser {

   public MbInstNameDispenser() {
      this.instNameCache = new HashMap<>();
   }

   /**
    * Decodes an operation name into the equivalent MbInstructionName.
    *
    * <p>Returns null if no mapping is found. Logs a message if this happens.
    *
    * @param operationName
    * @return
    */
   public MbInstructionName getMbInstName(String operationName) {
      MbInstructionName mbInst = instNameCache.get(operationName);
      if(mbInst != null) {
         return mbInst;
      }

      mbInst = SpecsEnums.valueOf(MbInstructionName.class, operationName);
      if(mbInst != null) {
         instNameCache.put(operationName, mbInst);
      } else {
         SpecsLogs.getLogger().
                 warning("Could not find a MicroBlaze instruction called '"+operationName+"'.");
      }


      return mbInst;
   }

   private Map<String, MbInstructionName> instNameCache;
}
