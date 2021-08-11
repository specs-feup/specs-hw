package pt.up.fe.specs.binarytranslation.test.processes;

import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.processes.QEMU;

public class QEMUTester {

    /**
     * Tests if QEMU launches then dies
     * 
     * @param elf
     */
    protected static void testQEMULaunch(ELFProvider elf) {

        try (var qemu = new QEMU(elf.toApplication())) {
            qemu.start();
            System.out.println("waiting...");
            TimeUnit.SECONDS.sleep(5);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
