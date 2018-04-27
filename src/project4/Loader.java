/**
 * 
 */
package project4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jassimran
 *
 */
public class Loader {
	
	Map<String, RavensFigure> hmap ;
	String problemType ;
	String problemName ;
	String objmapping;
	
	public Loader(RavensProblem problem) {
		hmap = problem.getFigures();
		problemType = problem.getProblemType();
    	problemName = problem.getName();
    }
	
	
	
	public Loader(RavensProblem problem, String mapper) {
		hmap = problem.getFigures();
		problemType = problem.getProblemType();
    	problemName = problem.getName();
    	//objmapping = mapper.split("-");
    }
	
	public HashMap <String, HashMap <Integer, HashMap<String, String>>> getProblemMap(){
		
    	System.out.println("--------- "+problemName+" ----------");    	
    	HashMap <String, HashMap <Integer, HashMap<String, String>>> rawMap = new HashMap <String, HashMap <Integer, HashMap<String, String>>>();
        
    	RavensFigure rfig = hmap.get("A");
        rawMap.put("A", load(rfig));
        
        rfig = hmap.get("B");
        rawMap.put("B", load(rfig));
        	
        rfig = hmap.get("C");
        rawMap.put("C", load(rfig));
        
        for (int i = 1; i<= 6; i++){
        	rfig = hmap.get(String.valueOf(i));
        	rawMap.put(String.valueOf(i), load(rfig));
        }
        
        return rawMap;
        	
	}
	
public HashMap <String, HashMap <Integer, HashMap<String, String>>> getProblemMap3x3(){
		
    	System.out.println("--------- "+problemName+" ----------");    	
    	HashMap <String, HashMap <Integer, HashMap<String, String>>> rawMap = new HashMap <String, HashMap <Integer, HashMap<String, String>>>();
        
    	RavensFigure rfig = hmap.get("A");
        rawMap.put("A", load(rfig));
        
        rfig = hmap.get("B");
        rawMap.put("B", load(rfig));
        	
        rfig = hmap.get("C");
        rawMap.put("C", load(rfig));
        
        rfig = hmap.get("D");
        rawMap.put("D", load(rfig));
        
        rfig = hmap.get("E");
        rawMap.put("E", load(rfig));
        
        rfig = hmap.get("F");
        rawMap.put("F", load(rfig));
        
        rfig = hmap.get("G");
        rawMap.put("G", load(rfig));
        
        rfig = hmap.get("H");
        rawMap.put("H", load(rfig));
        
        for (int i = 1; i<= 6; i++){
        	rfig = hmap.get(String.valueOf(i));
        	rawMap.put(String.valueOf(i), load(rfig));
        }
        
        return rawMap;
        	
	}
	
	public HashMap <Integer, HashMap<String, String>> load (RavensFigure rfig){
		HashMap<String, String> attrMap = new HashMap<String, String> ();
		HashMap <Integer, HashMap<String, String>> objMap = new HashMap <Integer, HashMap<String, String>> ();
        String attrName;
        String attrValue;
        
		if(rfig.getObjects().size() == 0){
			return objMap;
		}
        
		int k = 1;
        for(RavensObject ravObj : rfig.getObjects()){
        	for( RavensAttribute attr : ravObj.getAttributes()){
        		attrName = attr.getName();
        		attrValue = attr.getValue();
        		attrMap.put(attrName, attrValue) ;
        	}
        	objMap.put((k), attrMap);
        	k++;
        }
        
        return objMap;
	}
	
	public HashMap <Integer, HashMap<String, String>> load (RavensFigure rfig, String mapper){
		HashMap<String, String> attrMap = new HashMap<String, String> ();
		HashMap <Integer, HashMap<String, String>> objMap = new HashMap <Integer, HashMap<String, String>> ();
        String attrName;
        String attrValue;
        
		
		int k = 1;
        for(RavensObject ravObj : rfig.getObjects()){
        	for( RavensAttribute attr : ravObj.getAttributes()){
        		attrName = attr.getName();
        		attrValue = attr.getValue();
        		attrMap.put(attrName, attrValue) ;
        	}
        	objMap.put((k), attrMap);
        	k++;
        }
        
        return objMap;
	}

}
