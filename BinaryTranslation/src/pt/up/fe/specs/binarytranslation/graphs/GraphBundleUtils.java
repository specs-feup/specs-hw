package pt.up.fe.specs.binarytranslation.graphs;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GraphBundleUtils {

    // quick hack fix
    static String outputfolder;

    private static void setPrefix(String parentfolder) {
        // output folder
        outputfolder = null;
        if (parentfolder != null)
            outputfolder = parentfolder;
        else
            outputfolder = "./output/";
    }

    /*
     * Return full path for output folder (includes upper most parent)
     */
    private static String getOutputFolder(GraphBundle bundle) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
        var foldername = bundle.getAppinfo().getAppName();
        foldername = foldername.substring(0, foldername.lastIndexOf('.'));
        foldername = foldername + "_bundle_" + formatter.format(bundle.getDate());
        return outputfolder + "/" + foldername;
    }

    /*
     * Return full path for JSON file
     */
    private static String getJSONPathname(GraphBundle bundle) {
        return getOutputFolder(graph) + "/graph_"
                + Integer.toString(graph.getSegment().hashCode()) + ".json";
    }

    public static void generateOutput(GraphBundle bundle, String parentfolder) {

        setPrefix(parentfolder);

        // bundle top level info to JSON (?)
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        var bytes = gson.toJson(bundle).getBytes();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filename);
            BufferedOutputStream bw = new BufferedOutputStream(os);
            try {
                bw.write(bytes);
                bw.flush();
                bw.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // HTML, JSON, etc, for all graphs
        for (var g : bundle.getGraphs()) {
            g.generateOutput(parentfolder);
        }
    }
}
