package pt.up.fe.specs.binarytranslation.test.processes;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.processes.TxtDump;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class TxtDumpTest {

    @Test
    public void test() {
        var txtfile = BinaryTranslationUtils.getFile("org/specs/BinaryTranslation/specs_cr_text.txt");
        try (var txtDumper = new TxtDump(txtfile)) {
            String line = null;
            while ((line = txtDumper.getLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
