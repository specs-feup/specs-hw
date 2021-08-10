package pt.up.fe.specs.binarytranslation.test.asm;

import java.util.List;

import pt.up.fe.specs.binarytranslation.ELFProvider;

public class ELFProviderTestTester {

    public static void testStartStopAddrReading(List<Class<? extends ELFProvider>> listOfELFProviders) {

        for (var elfset : listOfELFProviders) {
            var elfs = elfset.getEnumConstants();
            System.out.println(elfset.getSimpleName());
            for (var elf : elfs) {
                var app = elf.toApplication();
                System.out.print(elf.getELFName() + ": ");
                System.out.print("0x" + Integer.toHexString(app.getKernelStart()) + " - ");
                System.out.println("0x" + Integer.toHexString(app.getKernelStop()));
            }
        }
    }
}
