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

/**
 * General definitions of the MicroBlaze Architecture
 *
 * @author Joao Bispo
 */
public interface MbDefinitions {

   int BITS_IMMEDIATE = 16;
   int BITS_REGISTER = 32;
   int BITS_CARRY = 1;
   int INSTRUCTION_ADDR_STEP = 4;

   String CARRY_REGISTER = "MSR[29]";

   String GCC_ID_HEXSTRING = "baab";
   int GCC_ID_INT = Integer.parseInt(GCC_ID_HEXSTRING,16);
}
