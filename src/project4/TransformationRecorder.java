package project4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TransformationRecorder {
	
	Comparer compObj;

	HashMap <Integer, HashMap<String, String>> getTransformations(HashMap <Integer, HashMap<String, String>> firstMap, 
			HashMap <Integer, HashMap<String, String>> secondMap){
		
		//Create compaper Object:
    	compObj = new Comparer();
    	
    	//Map for objects
    	HashMap <Integer, HashMap<String, String>> mapObj = new HashMap <Integer, HashMap<String, String>> ();
    	
    	//Map for deleted attributes for objects from A
    	HashMap <Integer, HashMap<String, String>> deletedAttrsMap = new HashMap <Integer, HashMap<String, String>> (); 
    	//Map for added attributes for objects to B
    	HashMap <Integer, HashMap<String, String>> addedAttrsMap = new HashMap <Integer, HashMap<String, String>> ();
    	
    	//List for deleted objects from A
    	List <Integer> deletedObjList = new ArrayList <Integer> ();
    	//List for added objects to B
    	List <Integer> addedObjList = new ArrayList <Integer> ();
    	
    	int countObjsA = firstMap.size();
    	int countObjsB = secondMap.size();
    	int counter = 0;
    	if(countObjsA >= countObjsB)
    		counter = countObjsA;
    	else 
    		counter = countObjsB;
    	
    	
    	
    	
	    //if(countObjsA >=countObjsB){
    	for (int i = 1; i <= firstMap.size(); i++ ){  
    	  if(firstMap.containsKey(i) && secondMap.containsKey(i)){        		
    		HashMap<String,String> matchattrMap = new HashMap<String,String>();
    		int attrCountA = firstMap.get(i).entrySet().size();
    		int attrCountB = secondMap.get(i).entrySet().size();
    		int countA = 0;
    		int countB = 0;
    		for (Map.Entry <String,String> entryA : firstMap.get(i).entrySet()) {
			  String attrNameA = entryA.getKey();
			  String attrValueA = entryA.getValue(); 
			 
			  for (Map.Entry <String,String> entryB : secondMap.get(i).entrySet()) {
				  String attrNameB = entryB.getKey();
				  String attrValueB = entryB.getValue();
				  
				  //comparison
				  if(attrNameA.equalsIgnoreCase(attrNameB)){
					  countA++;
					  countB++;
					  
          			//check if values are equal or not
          			if(attrNameB.equalsIgnoreCase("shape"))
          					matchattrMap.put(attrNameA, compObj.shapeComparor(attrValueA, attrValueB));
          			else if(attrNameB.equalsIgnoreCase("fill"))
          					matchattrMap.put(attrNameA, compObj.fillComparor(attrValueA, attrValueB));
          			else if(attrNameB.equalsIgnoreCase("size"))
          					matchattrMap.put(attrNameA, compObj.sizeComparor(attrValueA, attrValueB));
          			else if(attrNameB.equalsIgnoreCase("angle")){
          					matchattrMap.put(attrNameA, compObj.angleComparor2x2(attrValueA, attrValueB));
          					if(compObj.getFlip()==1)
          						matchattrMap.put("vertical-flip", "flipped");
          					compObj.setFlip(0);
          			}
          			else if(attrNameB.equalsIgnoreCase("above"))
          					matchattrMap.put(attrNameA, compObj.aboveComparor(attrValueA, attrValueB));
          			else if(attrNameB.equalsIgnoreCase("left-of"))
          					matchattrMap.put(attrNameA, compObj.leftOfComparor(attrValueA, attrValueB));
          			else if(attrNameB.equalsIgnoreCase("inside"))
          					matchattrMap.put(attrNameA, compObj.insideComparor(attrValueA, attrValueB));
          			else if(attrNameB.equalsIgnoreCase("overlap") || attrNameB.equalsIgnoreCase("overlaps"))
          					matchattrMap.put(attrNameA, compObj.overlapComparor(attrValueA, attrValueB));
          			break;
					  
				  }
        	
			  }
    		}
    	
    		if ((countA != attrCountA)){
				HashMap<String,String> tempMapA = new HashMap<String,String>();
				for (Map.Entry <String,String> attrMapI : firstMap.get(i).entrySet()) {
					if(!matchattrMap.containsKey(attrMapI.getKey())){
						//if it does not contain key - add it!
						
						tempMapA.put(attrMapI.getKey(), attrMapI.getValue());
						countA++;
						if(countA == attrCountA)
							break;
					}
				}
				deletedAttrsMap.put((i), tempMapA);
			}
			if ((countB != attrCountB)){
				//non-matching attributes exist.
				HashMap<String,String> tempMapB = new HashMap<String,String>();
				for (Map.Entry <String,String> attrMapI : secondMap.get(i).entrySet()) {
					if(!matchattrMap.containsKey(attrMapI.getKey())){
						//if it does not contain key - add it!
						
						tempMapB.put(attrMapI.getKey(), attrMapI.getValue());
						countB++;
						if(countB == attrCountB)
							break;
					}
				}
				addedAttrsMap.put((i), tempMapB);
			}
			mapObj.put(i, matchattrMap );
    	} else if(!firstMap.containsKey(i)){
    		if(secondMap.containsKey(i))
    			addedObjList.add((i));
    	} else if (!secondMap.containsKey(i)){
    		deletedObjList.add((i));
    	}
    	}
	     
	    if (countObjsA <countObjsB){
	    	if(countObjsA == 0){
	    		//add all objects in B in addedList.
	    		Iterator<Map.Entry<Integer, HashMap<String,String>>> objsB  = secondMap.entrySet().iterator();
	    	    while (objsB.hasNext()) {
	    	    	Map.Entry<Integer, HashMap<String, String>> obj = objsB.next();
	    	    	addedObjList.add(obj.getKey()); 
	     			
	    	    }
	    	} else {
	    		Iterator<Map.Entry<Integer, HashMap<String,String>>> objsB  = secondMap.entrySet().iterator();
	    	    while (objsB.hasNext()) {
	    	    	Map.Entry<Integer, HashMap<String, String>> obj = objsB.next();
	        		if(!mapObj.containsKey(obj.getKey()))
	        			addedObjList.add(obj.getKey());
	        	}
	        	
	    	}
	    }    	
    	
		return mapObj;
	}
}
