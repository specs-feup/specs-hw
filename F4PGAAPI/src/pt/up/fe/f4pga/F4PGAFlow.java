package pt.up.fe.f4pga;

import java.io.File;
import java.util.List;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.utilities.Replacer;

public class F4PGAFlow {

    // fix later, assume known install dir
    final static String DEFAULT_F4PGA_INSTALL_DIR = "~/opt/f4pga";

    // String flowString;
    // List<Object> targetList;
    // XDC xilinixDC;

    private final String topName;
    private final String buildDir;

    private final F4PGATarget target;

    public F4PGAFlow(String topName, String buildDir, List<String> hdls, F4PGATarget target) {
        this.topName = topName;
        this.buildDir = buildDir;
        this.hdls = hdls;
        this.target = target;
    }

    private static void _prepBuildScript(F4PGATarget target, String topName) {

        var tmpl = F4PGAResource.F4PGABASH_SCRIPT_TEMPLATE;
        var tmplAsString = tmpl.read();

        var repl = new Replacer(tmplAsString);
        repl.replace("<INSTALATION_DIR>", DEFAULT_F4PGA_INSTALL_DIR);
        repl.replace("<FAM_NAME>", target.getFpgaFamily());

        var tmplChanged = new File("./" + topName + "_f4pgabuild.sh");
        SpecsIo.write(tmplChanged, repl.toString());
        // tmplChanged.deleteOnExit();
    }

    private static void _prepJson(F4PGATarget target, List<String> hdls, String topName) {

        var tmpl = F4PGAResource.F4PGAJSON_TEMPLATE;
        var tmplAsString = tmpl.read();

        var repl = new Replacer(tmplAsString);
        repl.replace("<SOURCE_LIST>", hdls);
        repl.replace("<PART_NUMBER>", target.getPartName());

        var tmplChanged = new File("./" + topName + "_jsonFLow.json");
        SpecsIo.write(tmplChanged, repl.toString());
        // tmplChanged.deleteOnExit();
    }

    public void setupFlow() {

        // setup build.sh
        _prepBuildScript(this.target, this.topName);

        // setup json flow
        _prepJson(this.target, getHdls(), this.topName);
    }

    public void runF4PGARunOutput() {

        // How to connect the first part (mkdir and new file) to the second part (procesbuilder)

        // mkdir
        // new file this.buildDir
        File newFile = new File(this.buildDir);
        newFile.mkdir();

        // using a pRocessbuilder, call this:
        // bash "./" + topName + "_f4pgabuild.sh"

        ProcessBuilder probuild = new ProcessBuilder("./" + topName + "_f4pgabuild.sh");
    }

    /*
    public String newFlow(String _hdlSourceDir, String _topName) {
    
        flowString = SpecsIo.getResource("flow.json");
    
        var _targetList = new ArrayList<>();
        _targetList.add("bitstream");
    
        sourceFromList(_hdlSourceDir);
        changeTopName(_topName);
        changeTarget(_targetList);
        changeBuildDir("local_dir");
        // default values from creating a flow.json file
    
        return flowString;
    }*/

    /*
    public void changeTopName(String _topName) {
    
        var replacer = new Replacer(flowString);
    
        replacer.replace("<source_list>", _topName);
        topName = _topName;
    }
    
    public void changeBuildDir(String _buildDir) {
    
        var replacer = new Replacer(flowString);
    
        replacer.replace("build_dir>", _buildDir);
        buildDir = _buildDir;
    }
    
    public void changeTarget(ArrayList<Object> _targetList) {
    
        var replacer = new Replacer(flowString);
        String sourceListString = "TODO";
    
        replacer.replace("<source_list>", sourceListString);
        targetList = _targetList;
    }*/
}
