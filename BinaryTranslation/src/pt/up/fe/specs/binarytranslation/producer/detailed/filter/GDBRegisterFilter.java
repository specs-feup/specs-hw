package pt.up.fe.specs.binarytranslation.producer.detailed.filter;

import java.util.Arrays;
import java.util.regex.Pattern;

import pt.up.fe.specs.binarytranslation.producer.detailed.RegisterDump;
import pt.up.fe.specs.util.utilities.LineStream;

public class GDBRegisterFilter extends GDBFilter {
    private RegisterDump dump = new RegisterDump();
    private String[] regs = { "ra","sp","gp","tp","t0","t1","t2","fp","a0","a1",
            "a2","a3","a4","a5","a6","a7","s2","s3","s4","s5","s6","s7","s8",
            "s9","s10","s11","t3","t4","t5","t6","pc" };

    public GDBRegisterFilter(LineStream lines) {
        super(lines);
    }

    @Override
    public boolean filter() {
        String line = null;

        while (((line = lines.peekNextLine()) != null)) {
            //System.out.println("RegFilter processing line: " + line);
            if (line.startsWith("mucounteren")) {
                lines.nextLine();
                break;
            } else {
                line = line.replaceAll("\\s+", " ");
                String[] elems = line.split(" ");
                if (belongsToISA(elems[0])) {
                    //System.out.println("Found valid reg: " + line);
                    long val = Long.decode(elems[1]);
                    dump.add(elems[0], val);
                    
                }
            }
            lines.nextLine();
        }
        return !dump.isEmpty();
    }

    private boolean belongsToISA(String string) {
        return Arrays.asList(this.regs).contains(string);
    }

    @Override
    public RegisterDump getResult() {
        return dump;
    }

}
