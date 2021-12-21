/**
 * Copyright 2021 SPeCS.
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

package pt.up.fe.specs.binarytranslation.analysis.analyzers.scheduling;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;

public class Schedule {
    private Queue<Integer> queueAlus;
    private Queue<Integer> queueMemPorts;
    private List<BtfVertex> resourcesAlus;
    private List<BtfVertex> resourcesMemPorts;
    private StringBuilder finalSchedule;
    private int currCycle = 0;

    public Schedule(int alus, int memoryPorts, Set<BtfVertex> vertices) {
        // Discrete resources for output purposes
        this.resourcesAlus = new ArrayList<BtfVertex>();
        this.queueAlus = new LinkedList<>();
        for (var i = 0; i < alus; i++) {
            resourcesAlus.add(BtfVertex.nullVertex);
            queueAlus.add(i);
        }
        
        this.resourcesMemPorts = new ArrayList<BtfVertex>();
        this.queueMemPorts = new LinkedList<>();
        for (var i = 0; i < memoryPorts; i++) {
            resourcesMemPorts.add(BtfVertex.nullVertex);
            queueMemPorts.add(i);
        }
        
        // Cycle-by-cycle schedule as a CSV
        finalSchedule = new StringBuilder("Cycle");
        for (var i = 0; i < alus; i++)
            finalSchedule.append(",").append("ALU_").append(i);
        for (var i = 0; i < memoryPorts; i++)
            finalSchedule.append(",").append("MEM_").append(i);
        finalSchedule.append("\n");
    }
    
    public void enqueueMemoryPortOp(BtfVertex op) {
        var idx = queueMemPorts.poll();
        resourcesMemPorts.set(idx, op);
    }
    
    public void enqueueAluOp(BtfVertex op) {
        var idx = queueAlus.poll();
        resourcesAlus.set(idx, op);
    }
    
    public void dequeueMemoryPortOp(BtfVertex op) {
        var idx = resourcesMemPorts.indexOf(op);
        resourcesMemPorts.set(idx, BtfVertex.nullVertex);
        queueMemPorts.add(idx);
    }
    
    public void dequeueAluOp(BtfVertex op) {
        var idx = resourcesAlus.indexOf(op);
        resourcesAlus.set(idx, BtfVertex.nullVertex);
        queueAlus.add(idx);
    }

    public String getFinalSchedule() {
        return finalSchedule.toString();
    }
    
    public void endCycle() {
        this.currCycle++;
        finalSchedule.append(currCycle);
        for (var i = 0; i < resourcesAlus.size(); i++) {
            var v = resourcesAlus.get(i);
            finalSchedule.append(",");
            if (v != BtfVertex.nullVertex)
                finalSchedule.append(sanitizeForCsv(v.getLabel()) + " (" + v.getTempId() + ")");
        }
        for (var i = 0; i < resourcesMemPorts.size(); i++) {
            var v = resourcesMemPorts.get(i);
            finalSchedule.append(",");
            if (v != BtfVertex.nullVertex)
                finalSchedule.append(sanitizeForCsv(v.getLabel()) + " (" + v.getTempId() + ")");
        }
        finalSchedule.append("\n");
    }

    private String sanitizeForCsv(String label) {
        if (label.equals("+"))
            return "add";
        if (label.equals("-"))
            return "sub";
        if (label.equals("*"))
            return "mul";
        if (label.equals("=="))
            return "cmp";
        return label;
    }

    public int getScheduleLatency() {
        return currCycle - 1;
    }

    public int getAluCount() {
        return resourcesAlus.size();
    }

    public int getMemPortCount() {
        return resourcesMemPorts.size();
    }

    public void dequeueAGU(BtfVertex op) {
        // TODO Auto-generated method stub
        
    }

    public void enqueueAGU(BtfVertex op) {
        // TODO Auto-generated method stub
        
    }
}
