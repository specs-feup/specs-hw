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

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Joao Bispo
 */
public class StackData {

   public StackData() {
      //instructionsWithR1 = new HashMap<Integer, String>();
      addressesWhichUseR1 = new HashSet<>();
      totalCounter = 0l;
      memCounter = 0l;
   }



   //private Map<Integer, String> instructionsWithR1;
   public Set<Integer> addressesWhichUseR1;
   public long totalCounter = 0l;
   public long memCounter = 0l;
}
