package org.specs.MicroBlaze.test;

import java.io.BufferedOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonTester {

    @Test
    public void testInstructionToJson() {
        Gson gson = new GsonBuilder().create();

        var addi = MicroBlazeInstruction.newInstance("248", "20c065e8");

        var bytes = gson.toJson(addi).getBytes();

        BufferedOutputStream bw = new BufferedOutputStream(System.out);
        try {
            bw.write(bytes);
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
