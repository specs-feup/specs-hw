package pt.up.fe.f4pga;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import pt.up.fe.specs.util.utilities.Replacer;

public class F4PGAHDL {
	
	private String hdlSourceDir; // TODO: replace with class that repreesnts all src HDLs
	
    final List<String> hdls; // TODO: replace with class that repreesnts all src HDLs (should inbclude which
                                     // file is top level)
    
    
    public F4PGAHDL(String hdlSourceDir, List<String> hdls) {
    	
    	this.hdlSourceDir = hdlSourceDir;
    	this.hdls = hdls;
    	
    }
	
    public String getHdlSourceDir() {
    	return this.hdlSourceDir;
    }
    
    public List<String> getHdls(){
    	return this.hdls;
    }
	
	
	
	
    
    /**     * 
     * 
     * @param _hdlSourceDir
     */
    public void sourceFromList(String _hdlSourceDir) {  
    	
    	var tmpl = F4PGAResource.F4PGAJSON_TEMPLATE;
    	var tmplAsString = tmpl.read();
        var replacer = new Replacer(tmplAsString);
        
        String fileExtension = "v";
        
        //last file from the path -hdlSourceDir
        File f = new File(_hdlSourceDir);  
        
        //Gives the sourcelist of all files with .v extension
        var sourceListString = getAllFilesWithCertainExtension (f,fileExtension);
        
        //diretorio fornecido pelo utilizador 
        //ficheiros verilog are .v
    
        replacer.replace("<source_list>", sourceListString);
        hdlSourceDir = _hdlSourceDir;
    }
    
    
    
    /**
     * Gives all folders with a certain extension in a String[] variable
     * 
     * 
     * @param folder
     * @param filterExt
     * @return
     */
    public  String[] getAllFilesWithCertainExtension(File folder,String filterExt){
    	MyExtFilter extFilter=new MyExtFilter(filterExt);
      
    	// list out all the file name and filter by the extension
    	String[] list = folder.list(extFilter);
     
    	for (int i = 0; i < list.length; i++) {
        System.out.println("File :"+list[i]);
       }
       
    	return list;
      
    }
     
    
    
    
    /**
     * Class that helps the implementation the method above
     * 
     * 
     * @author Francisco
     *
     */
    public class MyExtFilter implements FilenameFilter {
     
    	private String ext;
     
    	public MyExtFilter(String ext) {
    		this.ext = ext;
    	}
     
    	public boolean accept(File dir, String name) {
    		return (name.endsWith(ext));
    	}
    	
    }
	
	
	

}
