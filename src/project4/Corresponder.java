/**
 * 
 */
package project4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Jassimran
 *
 */
class Corresponder {
	
	List <String> globalObjectNames;
	Map <String, Integer> updateObjNames;
	int isImageEmpty;
	int j = 1;
	String shape;
	
	public Corresponder(String problemType){
		globalObjectNames = new ArrayList <String> ();
		if(problemType.equals( "2x1") || problemType.equals("2x1 (Image)")){
		globalObjectNames.add(0, "-");
		globalObjectNames.add(1, "Z");
		globalObjectNames.add(2, "Y");
		globalObjectNames.add(3, "X");
		}
		updateObjNames = new HashMap <String, Integer> ();
		isImageEmpty = 0;
	}
	
	public Corresponder(){
		globalObjectNames = new ArrayList <String> ();
		globalObjectNames.add(0, "-");
		globalObjectNames.add(1, "A");
		globalObjectNames.add(2, "B");
		globalObjectNames.add(3, "C");
		globalObjectNames.add(4, "D");
		globalObjectNames.add(5, "E");
		updateObjNames = new HashMap <String, Integer> ();
		isImageEmpty = 0;
		
	}
	
	public HashMap<Integer, HashMap<String, String>> getMatch(ArrayList <RavensObject> listA, ArrayList <RavensObject> list ){
				
		HashMap<Integer, HashMap<String, String>> objMap = new HashMap <Integer, HashMap<String, String>> ();
		//Map for attributes deleted from B
    	HashMap <String, HashMap<String, String>> deletedAttrsMap = new HashMap <String, HashMap<String, String>> (); 
    	//Map for attributes added to B
    	HashMap <String, HashMap<String, String>> addedAttrsMap = new HashMap <String, HashMap<String, String>> ();
		
    	//List for deleted objects from B
    	List <String> deletedObjList = new ArrayList <String> ();
    	//List for added objects to B
    	List <String> addedObjList = new ArrayList <String> ();
    	
    	//Array list of mapped Raven's Object
    	List <String> mappedObjsList = new ArrayList <String> ();
    	
		HashMap<String,Integer> weightMap = new HashMap<String,Integer>();
    		
		int k = 1;
		//case 1 : When listA.size is 0
		if(listA.size() == 0){
			HashMap<String, String> attrMap = new HashMap<String, String> ();
			for(RavensObject ravObj : list){
				String objectName = ravObj.getName();
				for( RavensAttribute attr : ravObj.getAttributes()){
					attrMap.put(attr.getName(), attr.getValue()) ;
				}
				
				objMap.put(j, attrMap);	
				j++;
				k++;
				//add it to mapped items list
				mappedObjsList.add(objectName);	
				updateObjNames.put(objectName, globalObjectNames.indexOf(objectName));
			}   
		} 
		else if(listA.size() <= list.size()) {
			//DO Nothing
			for(RavensObject ravObjA : listA){
			
				HashMap<String, String> attrMap = new HashMap<String, String> ();
				for(RavensObject ravObj : list){
					String objectName = ravObj.getName();
					int weight = 0;
					if(ravObjA.getAttributes().size() == ravObj.getAttributes().size()){
						weight ++;
					}
					for( RavensAttribute attrA : ravObjA.getAttributes()){
						for( RavensAttribute attr : ravObj.getAttributes()){
							String attrNameA = attrA.getName();
							String attrName = attr.getName();
							if(attrNameA.equalsIgnoreCase(attrName)){
    	        			
								weight = weight + 4;
								String attrValueA = attrA.getValue();
								String attrValue = attr.getValue();
								if(attrValueA.equalsIgnoreCase(attrValue))
								{
									weight = weight + 5;
									if(attrNameA.equals("shape"))
										weight = weight + 4;
								}
							}
						}
					}
					weightMap.put(objectName , weight);
				}
    		
				//select transformation with most weight
				String key  = "";
				int maxValueInMap=(Collections.max(weightMap.values())); 
				int countMaxValues = 0;
				List<String> secondBestArray = new ArrayList<String>();
				
				for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {  
					if (entry.getValue()== maxValueInMap) {
						secondBestArray.add(entry.getKey());
						countMaxValues++;
						key = entry.getKey();
					}
					
					if (countMaxValues > 1) {
							//TODO: Resolve conflict if two shapes have exact same weight
							//update maxValueInMap
							//check objMap
						for (Map.Entry<String, Integer> notMappedObj : weightMap.entrySet()) {  
							if (!mappedObjsList.contains(notMappedObj.getKey())) {
								if(secondBestArray.contains(notMappedObj.getKey())){
									key = notMappedObj.getKey();
								}
							}
						}
					}
				}
        	
				int maxNewInMap = 0;
				List<Integer> newBestArray = new ArrayList<Integer>();
				if(mappedObjsList.contains(key)){
					//pick the other one which is second best match
					for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {  
						if (entry.getValue()== maxValueInMap) {
							
						} else
							newBestArray.add(entry.getValue());
					}
					maxNewInMap = newBestArray.get(newBestArray.indexOf(Collections.max(newBestArray)));
					for (Map.Entry<String, Integer> entry : weightMap.entrySet()) { 
						if (entry.getValue()== maxNewInMap) {
							key = entry.getKey();
						}
					}
				}
				
				if(mappedObjsList.contains(key)){
					List<Integer> newBestArrayThird = new ArrayList<Integer>();
					//pick the third best match
					for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {  
						if (entry.getValue()== maxValueInMap || entry.getValue() == maxNewInMap) {
							
						} else
							newBestArrayThird.add(entry.getValue());
					}
					if(newBestArrayThird.size() == 0){
						for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {
							if (!mappedObjsList.contains(entry.getKey())) {
									key = entry.getKey();	
							}
						}
					} else {
						maxNewInMap = newBestArrayThird.get(newBestArrayThird.indexOf(Collections.max(newBestArrayThird)));
						for (Map.Entry<String, Integer> entry : weightMap.entrySet()) { 
							if (entry.getValue()== maxNewInMap) {
								key = entry.getKey();
							}
						}
					}
				}
				
				
				//A maps to 1, B maps to 2 etc
				updateObjNames.put(key, globalObjectNames.indexOf(ravObjA.getName()));

        	
				//Map objectName A to maxKey from objectB
				for(RavensObject ravObj : list){
					String objectName = ravObj.getName();
					if (objectName.equals(key) ){
						
						for( RavensAttribute attr : ravObj.getAttributes()){
							//frame.addToFrame(attr.getName(), attr.getValue());
							attrMap.put(attr.getName(), attr.getValue()) ;
						}
						objMap.put(globalObjectNames.indexOf(ravObjA.getName()), attrMap);
						k++;
						//add it to mapped items list
						mappedObjsList.add(objectName);
						break;
					}    			
				}              		
			}
		}	
		else if (listA.size() > list.size()){
			//TODO: Add this case
			//----------------------------------------------
			//Deleted from B
			int objsDiff = 0;
			for(RavensObject ravObj : list){
				String objectName = ravObj.getName();
				
				HashMap<String, String> attrMap = new HashMap<String, String> ();
				for(RavensObject ravObjA : listA){
					String objectNameA = ravObjA.getName();
					int weight = 0;
					if(ravObjA.getAttributes().size() == ravObj.getAttributes().size()){
						weight ++;
					}
					for( RavensAttribute attrA : ravObjA.getAttributes()){
						for( RavensAttribute attr : ravObj.getAttributes()){
							String attrNameA = attrA.getName();
							String attrName = attr.getName();
							if(attrNameA.equalsIgnoreCase(attrName)){
    	        			
								weight = weight + 4;
								String attrValueA = attrA.getValue();
								String attrValue = attr.getValue();
								if(attrValueA.equalsIgnoreCase(attrValue))
								{
									weight = weight + 5;
									if(attrNameA.equals("shape"))
										weight = weight + 4;
								}
							}
						}
					}
					weightMap.put(objectNameA , weight);
				}
				
				//select transformation with most weight
				String key  = "";
				int maxValueInMap=(Collections.max(weightMap.values())); 
				List<String> secondBestArray = new ArrayList<String>();
				int countMaxValues = 0;
								
				for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {  
					if (entry.getValue()== maxValueInMap) {
						secondBestArray.add(entry.getKey());
						countMaxValues++;
						key = entry.getKey();
					}
					
					if (countMaxValues > 1) {
						//TODO: Resolve conflict if two shapes have exact same weight
						//update maxValueInMap
						//check objMap
						for (Map.Entry<String, Integer> notMappedObj : weightMap.entrySet()) {  
							if (!mappedObjsList.contains(notMappedObj.getKey())) {
								if(secondBestArray.contains(notMappedObj.getKey())){
									key = notMappedObj.getKey();
								}
							}
						}
					}
				}
        	
				int maxNewInMap = 0;
				List<Integer> newBestArray = new ArrayList<Integer>();
				if(mappedObjsList.contains(key)){
					//pick the other one which is second best match
					for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {  
						if (entry.getValue()== maxValueInMap) {
							
						} else
							newBestArray.add(entry.getValue());
					}
					maxNewInMap = newBestArray.get(newBestArray.indexOf(Collections.max(newBestArray)));
					for (Map.Entry<String, Integer> entry : weightMap.entrySet()) { 
						if (entry.getValue()== maxNewInMap) {
							key = entry.getKey();
						}
					}
				}
				
				if(mappedObjsList.contains(key)){
					List<Integer> newBestArrayThird = new ArrayList<Integer>();
					//pick the third best match
					for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {  
						if (entry.getValue()== maxValueInMap || entry.getValue() == maxNewInMap) {
							
						} else
							newBestArrayThird.add(entry.getValue());
					}
					if(newBestArrayThird.size() == 0){
						for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {
							if (!mappedObjsList.contains(entry.getKey())) {
									key = entry.getKey();	
							}
						}
					} else {
					maxNewInMap = newBestArrayThird.get(newBestArrayThird.indexOf(Collections.max(newBestArrayThird)));
					for (Map.Entry<String, Integer> entry : weightMap.entrySet()) { 
						if (entry.getValue()== maxNewInMap) {
							key = entry.getKey();
						}
					}
					}
				}
				
				
				//A maps to 1, B maps to 2 etc
				updateObjNames.put(objectName, globalObjectNames.indexOf(key));


        	
				//Map objectName A to maxKey from objectB
				for( RavensAttribute attr : ravObj.getAttributes()){
					attrMap.put(attr.getName(), attr.getValue()) ;
				}
				objMap.put(globalObjectNames.indexOf(key), attrMap);
				//add it to mapped items list
				mappedObjsList.add(objectName);				
			}
			
		} 
		if (listA.size() < list.size()){
			//Added to B
			int objsDiff = 0;
			
			for(RavensObject ravObj : list){
    			String objectName = ravObj.getName();
    			if (!mappedObjsList.contains(objectName) ){
    				HashMap<String, String> attrMap = new HashMap<String, String> ();
    				for( RavensAttribute attr : ravObj.getAttributes()){
                		attrMap.put(attr.getName(), attr.getValue()) ;
    				}
    				objMap.put(k, attrMap);
    				//A maps to 1, B maps to 2 etc
    				updateObjNames.put(objectName, k);
    				k++;
    				
    				objsDiff++;
    				if(objsDiff == (list.size() - listA.size()))
    					break;
    			}    			
           	} 			
		}
		
		//Update values of attributes as per the updateObjNames Map for the object
		
		final Iterator<Map.Entry<Integer, HashMap<String,String>>> obj  = objMap.entrySet().iterator();
	    while (obj.hasNext()) {
	        Map.Entry<Integer, HashMap<String, String>> mapEntry = obj.next();
			HashMap <String, String> attrMap = mapEntry.getValue();
			for(Map.Entry <String, String> entry: attrMap.entrySet()){
				String attrName = entry.getKey();
				String attrValue = entry.getValue();
				//if (updateObjNames.containsKey(attrValue)){
					////update value
					//objMap.get(mapEntry.getKey()).put(attrName, String.valueOf(updateObjNames.get(attrValue)) ) ;
				//}
				String sol = "";
				int count = 0;
				for (String val: attrValue.split(",")){
					if(count > 0){
		    			sol = sol.concat(",");
		    		}
		    		if (updateObjNames.containsKey(val)){
		    			sol = sol.concat(String.valueOf(updateObjNames.get(val)));
		    			count ++;
		    		}
		    	}
				if(count > 0)
					objMap.get(mapEntry.getKey()).put(attrName, sol ) ;
				
			}  
		}
       	
       	
		return objMap;
	}
	
	public HashMap<Integer, HashMap<String, String>> getMatchOfOptions(HashMap<Integer, HashMap<String, String>> MapA, ArrayList <RavensObject> list ){
		
		HashMap<Integer, HashMap<String, String>> objMap = new HashMap <Integer, HashMap<String, String>> ();
		//Map for attributes deleted from B
    	HashMap <String, HashMap<String, String>> deletedAttrsMap = new HashMap <String, HashMap<String, String>> (); 
    	//Map for attributes added to B
    	HashMap <String, HashMap<String, String>> addedAttrsMap = new HashMap <String, HashMap<String, String>> ();
		
    	//List for deleted objects from B
    	List <String> deletedObjList = new ArrayList <String> ();
    	//List for added objects to B
    	List <String> addedObjList = new ArrayList <String> ();
    	
    	//Array list of mapped Raven's Object
    	List <String> mappedObjsList = new ArrayList <String> ();
    	
		HashMap<String,Integer> weightMap = new HashMap<String,Integer>();
    		
		int k = 1;
		
		if(MapA.size() <= list.size()) {
			
			for(int m = 1; m <= MapA.size(); m++){
			
				HashMap<String, String> attrMap = new HashMap<String, String> ();
				for(RavensObject ravObj : list){
					String objectName = ravObj.getName();
					int weight = 0;
					if(MapA.get(m).size() == ravObj.getAttributes().size()){
						weight ++;
					}
					for(Map.Entry <String, String> entry: MapA.get(m).entrySet()){
						String attrNameA = entry.getKey();
						String attrValueA = entry.getValue();
						for( RavensAttribute attr : ravObj.getAttributes()){
							String attrName = attr.getName();
							if(attrNameA.equalsIgnoreCase(attrName)){
								weight = weight + 4;
								String attrValue = attr.getValue();
								if(attrValueA.equalsIgnoreCase(attrValue))
								{
									weight = weight + 5;
									if(attrNameA.equals("shape"))
										weight = weight + 4;
								}
							}
						}
					}
					weightMap.put(objectName , weight);
				}
    		
				//select transformation with most weight
				String key  = "";
				int maxValueInMap=(Collections.max(weightMap.values())); 
				List<String> secondBestArray = new ArrayList<String>();
				int countMaxValues = 0;
								
				for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {  
					if (entry.getValue()== maxValueInMap) {
						secondBestArray.add(entry.getKey());
						countMaxValues++;
						key = entry.getKey();
					}
					
					if (countMaxValues > 1) {
						//TODO: Resolve conflict if two shapes have exact same weight
						//update maxValueInMap
						//check objMap
						for (Map.Entry<String, Integer> notMappedObj : weightMap.entrySet()) {  
							if (!mappedObjsList.contains(notMappedObj.getKey())) {
								if(secondBestArray.contains(notMappedObj.getKey())){
									key = notMappedObj.getKey();
								}
							}
						}
					}
				}
        	
				int maxNewInMap = 0;
				List<Integer> newBestArray = new ArrayList<Integer>();
				if(mappedObjsList.contains(key)){
					//pick the other one which is second best match
					for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {  
						if (entry.getValue()== maxValueInMap) {
							
						} else
							newBestArray.add(entry.getValue());
					}
					maxNewInMap = newBestArray.get(newBestArray.indexOf(Collections.max(newBestArray)));
					for (Map.Entry<String, Integer> entry : weightMap.entrySet()) { 
						if (entry.getValue()== maxNewInMap) {
							key = entry.getKey();
						}
					}
				}
				
				if(mappedObjsList.contains(key)){
					List<Integer> newBestArrayThird = new ArrayList<Integer>();
					//pick the third best match
					for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {  
						if (entry.getValue()== maxValueInMap || entry.getValue() == maxNewInMap) {
							
						} else
							newBestArrayThird.add(entry.getValue());
					}
					if(newBestArrayThird.size() == 0){
						for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {
							if (!mappedObjsList.contains(entry.getKey())) {
									key = entry.getKey();	
							}
						}
					} else {
					maxNewInMap = newBestArrayThird.get(newBestArrayThird.indexOf(Collections.max(newBestArrayThird)));
					for (Map.Entry<String, Integer> entry : weightMap.entrySet()) { 
						if (entry.getValue()== maxNewInMap) {
							key = entry.getKey();
						}
					}
					}
				}
        	
				//A maps to 1, B maps to 2 etc
				updateObjNames.put(key, m);

        	
				//Map objectName A to maxKey from objectB
				for(RavensObject ravObj : list){
					String objectName = ravObj.getName();
					if (objectName.equals(key) ){
						
						for( RavensAttribute attr : ravObj.getAttributes()){
							//frame.addToFrame(attr.getName(), attr.getValue());
							attrMap.put(attr.getName(), attr.getValue()) ;
						}
						objMap.put(m, attrMap);
						k++;
						//add it to mapped items list
						mappedObjsList.add(objectName);
						break;
					}    			
				}              	
			}
		}
		
		if (MapA.size() < list.size()){
			//Added to B
			int objsDiff = 0;
			HashMap<String, String> attrMap = new HashMap<String, String> ();
			for(RavensObject ravObj : list){
    			String objectName = ravObj.getName();
    			if (!mappedObjsList.contains(objectName) ){
    				for( RavensAttribute attr : ravObj.getAttributes()){
                		attrMap.put(attr.getName(), attr.getValue()) ;
    				}
    				objMap.put(k, attrMap); 
    				updateObjNames.put(objectName, k);
    				k++;
    				objsDiff++;
    				if(objsDiff == (list.size() - MapA.size()))
    					break;
    			}    			
           	} 			
		} else if (MapA.size() > list.size()){
			//TODO: Add this case
			//----------------------------------------------
			//Deleted from B
			int objsDiff = 0;
			for(RavensObject ravObj : list){
				String objectName = ravObj.getName();
				
				HashMap<String, String> attrMap = new HashMap<String, String> ();
				for(int m = 1; m <= MapA.size(); m++){	
					int weight = 0;
					if(MapA.get(m).size() == ravObj.getAttributes().size()){
							weight ++;
					}
					for(Map.Entry <String, String> entry: MapA.get(m).entrySet()){
						String attrNameA = entry.getKey();
						String attrValueA = entry.getValue();
						for( RavensAttribute attr : ravObj.getAttributes()){
							String attrName = attr.getName();
							if(attrNameA.equalsIgnoreCase(attrName)){
								weight = weight + 4;
								String attrValue = attr.getValue();
								if(attrValueA.equalsIgnoreCase(attrValue))
								{
									weight = weight + 5;
									if(attrNameA.equals("shape"))
										weight = weight + 4;
								}
							}
						}
					}
					weightMap.put(String.valueOf(m), weight);
				}
				
				//select transformation with most weight
				String key  = "";
				int maxValueInMap=(Collections.max(weightMap.values())); 
				List<String> secondBestArray = new ArrayList<String>();
				int countMaxValues = 0;
								
				for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {  
					if (entry.getValue()== maxValueInMap) {
						secondBestArray.add(entry.getKey());
						countMaxValues++;
						key = entry.getKey();
					}
					
					if (countMaxValues > 1) {
						//TODO: Resolve conflict if two shapes have exact same weight
						//update maxValueInMap
						//check objMap
						for (Map.Entry<String, Integer> notMappedObj : weightMap.entrySet()) {  
							if (!mappedObjsList.contains(notMappedObj.getKey())) {
								if(secondBestArray.contains(notMappedObj.getKey())){
									key = notMappedObj.getKey();
								}
							}
						}
					}
				}
        	
				int maxNewInMap = 0;
				List<Integer> newBestArray = new ArrayList<Integer>();
				if(mappedObjsList.contains(key)){
					//pick the other one which is second best match
					for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {  
						if (entry.getValue()== maxValueInMap) {
							
						} else
							newBestArray.add(entry.getValue());
					}
					maxNewInMap = newBestArray.get(newBestArray.indexOf(Collections.max(newBestArray)));
					for (Map.Entry<String, Integer> entry : weightMap.entrySet()) { 
						if (entry.getValue()== maxNewInMap) {
							key = entry.getKey();
						}
					}
				}   
				
				if(mappedObjsList.contains(key)){
					List<Integer> newBestArrayThird = new ArrayList<Integer>();
					//pick the third best match
					for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {  
						if (entry.getValue()== maxValueInMap || entry.getValue() == maxNewInMap) {
							
						} else
							newBestArrayThird.add(entry.getValue());
					}
					if(newBestArrayThird.size() == 0){
						for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {
							if (!mappedObjsList.contains(entry.getKey())) {
									key = entry.getKey();	
							}
						}
					} else {
					maxNewInMap = newBestArrayThird.get(newBestArrayThird.indexOf(Collections.max(newBestArrayThird)));
					for (Map.Entry<String, Integer> entry : weightMap.entrySet()) { 
						if (entry.getValue()== maxNewInMap) {
							key = entry.getKey();
						}
					}
					}
				}
				//A maps to 1, B maps to 2 etc
				updateObjNames.put( objectName, Integer.parseInt(key));

        	
				//Map objectName A to maxKey from objectB
				
					
					
						
						for( RavensAttribute attr : ravObj.getAttributes()){
							//frame.addToFrame(attr.getName(), attr.getValue());
							attrMap.put(attr.getName(), attr.getValue()) ;
						}
						objMap.put(Integer.parseInt(key), attrMap);
						k++;
						//add it to mapped items list
						mappedObjsList.add(objectName);
						
					 			
				          	
			}
		}
			    
	    
		//Update values of attributes as per the updateObjNames Map for the object	
		final Iterator<Map.Entry<Integer, HashMap<String,String>>> obj  = objMap.entrySet().iterator();
	    while (obj.hasNext()) {
	        Map.Entry<Integer, HashMap<String, String>> mapEntry = obj.next();
			HashMap <String, String> attrMap = mapEntry.getValue();
			for(Map.Entry <String, String> entry: attrMap.entrySet()){
				String attrName = entry.getKey();
				String attrValue = entry.getValue();
				
				String sol = "";
				int count = 0;
				for (String val: attrValue.split(",")){
					if(count > 0){
		    			sol = sol.concat(",");
		    		}
		    		if (updateObjNames.containsKey(val)){
		    			sol = sol.concat(String.valueOf(updateObjNames.get(val)));
		    			count ++;
		    		}
		    	}
				if(count > 0)
					objMap.get(mapEntry.getKey()).put(attrName, sol ) ;
				
			}  
		}
       	
		return objMap;
	}
	
	
	
	//FOr A , B & C
	HashMap<String, HashMap<Integer, HashMap<String, String>>> getCorrespondence(RavensProblem problem)
	{
		//return Object
		HashMap <String, HashMap <Integer, HashMap<String, String>>> rawMap = new HashMap <String, HashMap <Integer, HashMap<String, String>>>();
        
		String problemType = problem.getProblemType();
    	String problemName = problem.getName();
    	System.out.println("--------- "+problemName+" ----------");
    	if(problemType.equalsIgnoreCase("2x2 (Image)") || problemType.equalsIgnoreCase("2x1 (Image)") || 
    			problemType.equalsIgnoreCase("2x2") || problemType.equalsIgnoreCase("2x1")){ 
    		Map<String, RavensFigure> hmap = problem.getFigures();
    		
        	RavensFigure rfigA = hmap.get("A"); 
        	ArrayList <RavensObject> listA = rfigA.getObjects();
        	int countObjsA = listA.size();
        	
        	RavensFigure rfigB = hmap.get("B");
        	ArrayList<RavensObject> listB = rfigB.getObjects();
        	int countObjsB = listB.size();
        	
        	RavensFigure rfigC = hmap.get("C");
        	ArrayList<RavensObject> listC = rfigC.getObjects();
        	int countObjsC = listC.size();
        	
        	
        	//case 1: When there is only one object in all the images.
        	if(countObjsA == 1 && countObjsB == 1 && countObjsC == 1){
        			int count = 0;
                	for (int i = 1; i<= 6; i++){
                		RavensFigure rfigOption = hmap.get(String.valueOf(i));
                		ArrayList <RavensObject> listOption = rfigOption.getObjects();
                		if(!listOption.isEmpty()){
                			updateObjNames.put(listOption.get(0).getName(), 1);
                    		if( listOption.size() ==  1){
                    			count ++;
                    		} else 
                    			break;
                		} else 
                			isImageEmpty = 1;
                	}
                	if(count == 6){
                		//All images have same number of objects, no need of establishing correspondence.
                		//Load everything into Map
                		Loader loader = new Loader(problem);
                		return loader.getProblemMap();
                	} else if (count == 5 && isImageEmpty == 1){
                		Loader loader = new Loader(problem);
                		return loader.getProblemMap();
                	}
        	}
        	
        	HashMap <Integer, HashMap<String, String>> objMap = new HashMap <Integer, HashMap<String, String>> ();
        	
        	//case2: A has no objects in it!
        	if(listA.size() == 0){
        		rawMap.put("A", objMap);
        	} 
        	else {        	
        	
        	//case 3: When there are more than one object in the images.
        	//For A
    		
        		for(RavensObject ravObjA : listA){
        			HashMap<String, String> attrMap = new HashMap<String, String> ();
        			for( RavensAttribute attr : ravObjA.getAttributes()){
        				attrMap.put(attr.getName(), attr.getValue()) ;
        			}
        			objMap.put(globalObjectNames.indexOf(ravObjA.getName()), attrMap);
        			updateObjNames.put(ravObjA.getName(), globalObjectNames.indexOf(ravObjA.getName()));
        		}       
        	
        		//Update references as well of Object names in A
        		int counter = 0;
        		counter = objMap.size();
        		for (int m = 1; m <= counter ; m++){
        			for(Map.Entry <String, String> entry: objMap.get(m).entrySet()){
        				String attrName = entry.getKey();
        				String attrValue = entry.getValue();
        				if (updateObjNames.containsKey(attrValue)){
        					//update value
        					objMap.get(m).put(attrName, String.valueOf(updateObjNames.get(attrValue)) ) ;
        				}    			
        			}  
        		}
        		rawMap.put("A", objMap);
        	}
            
            //For B
        	rawMap.put("B", getMatch(listA, listB));            
        	//For C
            rawMap.put("C", getMatch(listA, listC));
            
    		//Update values of attributes as per the updateObjNames Map for the object	
    		final Iterator<Map.Entry<Integer, HashMap<String,String>>> obj  = objMap.entrySet().iterator();
    	    while (obj.hasNext()) {
    	        Map.Entry<Integer, HashMap<String, String>> mapEntry = obj.next();
    			HashMap <String, String> attrMap = mapEntry.getValue();
    			for(Map.Entry <String, String> entry: attrMap.entrySet()){
    				String attrName = entry.getKey();
    				String attrValue = entry.getValue();
    				
    				String sol = "";
    				int count = 0;
    				for (String val: attrValue.split(",")){
    					if(count > 0){
    		    			sol = sol.concat(",");
    		    		}
    		    		if (updateObjNames.containsKey(val)){
    		    			sol = sol.concat(String.valueOf(updateObjNames.get(val)));
    		    			count ++;
    		    		}
    		    	}
    				if(count > 0)
    					objMap.get(mapEntry.getKey()).put(attrName, sol ) ;
    				
    			}  
    		}
            
            //Get match against options using rawMap.
           for(int i = 1; i <= 6 ; i++ ){
            		RavensFigure rfigOption = hmap.get(String.valueOf(i)); 
            		ArrayList <RavensObject> listOption = rfigOption.getObjects();        	
            		//for options
            		//get the object among A, B & C which has maximum number of objects for best comparison
            		if(rawMap.get("A").size() >= rawMap.get("B").size() && rawMap.get("A").size() >= rawMap.get("C").size()){
            			rawMap.put(String.valueOf(i), getMatchOfOptions(rawMap.get("A"), listOption)); 
            		} else if(rawMap.get("B").size() >= rawMap.get("C").size()){
            			rawMap.put(String.valueOf(i), getMatchOfOptions(rawMap.get("B"), listOption)); 
                    }  else if(rawMap.get("C").size() > rawMap.get("B").size()){
            			rawMap.put(String.valueOf(i), getMatchOfOptions(rawMap.get("C"), listOption)); 
                    }
            }
           
    	}     	
    	else if(problemType.equalsIgnoreCase("3x3") || problemType.equalsIgnoreCase("3x3 (Image)")){
    		/*
    		 * --------------------------------------------------
    		 * 3x3 PROBLEMS ONLY, number of object shave increased
    		 * --------------------------------------------------
    		 */
    		Map<String, RavensFigure> hmap = problem.getFigures();
    		
        	RavensFigure rfigA = hmap.get("A"); 
        	ArrayList <RavensObject> listA = rfigA.getObjects();
        	int countObjsA = listA.size();
        	
        	RavensFigure rfigB = hmap.get("B");
        	ArrayList<RavensObject> listB = rfigB.getObjects();
        	int countObjsB = listB.size();
        	
        	RavensFigure rfigC = hmap.get("C");
        	ArrayList<RavensObject> listC = rfigC.getObjects();
        	int countObjsC = listC.size();
        	
        	RavensFigure rfigD = hmap.get("D");
        	ArrayList<RavensObject> listD = rfigD.getObjects();
        	int countObjsD = listD.size();
        	
        	RavensFigure rfigE = hmap.get("E");
        	ArrayList<RavensObject> listE = rfigE.getObjects();
        	int countObjsE = listE.size();
        	
        	RavensFigure rfigF = hmap.get("F");
        	ArrayList<RavensObject> listF = rfigF.getObjects();
        	int countObjsF = listF.size();
        	
        	RavensFigure rfigG = hmap.get("G");
        	ArrayList<RavensObject> listG = rfigG.getObjects();
        	int countObjsG = listG.size();
        	
        	RavensFigure rfigH = hmap.get("H");
        	ArrayList<RavensObject> listH = rfigH.getObjects();
        	int countObjsH = listH.size();
        	
        	
        	
        	//case 1: When there is only one object in all the images.
        	if(countObjsA == 1 && countObjsB == 1 && countObjsC == 1 && countObjsD == 1 && countObjsE == 1 && countObjsF == 1 && countObjsG == 1 && countObjsH == 1){
        			int count = 0;
                	for (int i = 1; i<= 6; i++){
                		RavensFigure rfigOption = hmap.get(String.valueOf(i));
                		ArrayList <RavensObject> listOption = rfigOption.getObjects();
                		if(!listOption.isEmpty()){
                			updateObjNames.put(listOption.get(0).getName(), 1);
                    		if( listOption.size() ==  1){
                    			count ++;
                    		} else 
                    			break;
                		} else 
                			isImageEmpty = 1;
                	}
                	if(count == 6){
                		//All images have same number of objects, no need of establishing correspondence.
                		//Load everything into Map
                		Loader loader = new Loader(problem);
                		return loader.getProblemMap3x3();
                	} else if (count == 5 && isImageEmpty == 1){
                		Loader loader = new Loader(problem);
                		return loader.getProblemMap();
                	}
        	}
        	
        	HashMap <Integer, HashMap<String, String>> objMap = new HashMap <Integer, HashMap<String, String>> ();
        	
        	//case2: A has no objects in it!
        	if(listA.size() == 0){
        		rawMap.put("A", objMap);
        	} 
        	else {        	
        	
        	//case 3: When there are more than one object in the images.
        	//For A
    		
        		for(RavensObject ravObjA : listA){
        			HashMap<String, String> attrMap = new HashMap<String, String> ();
        			for( RavensAttribute attr : ravObjA.getAttributes()){
        				attrMap.put(attr.getName(), attr.getValue()) ;
        			}
        			objMap.put(globalObjectNames.indexOf(ravObjA.getName()), attrMap);
        			updateObjNames.put(ravObjA.getName(), globalObjectNames.indexOf(ravObjA.getName()));
        		}       
        	
        		//Update references as well of Object names in A
        		int counter = 0;
        		counter = objMap.size();
        		for (int m = 1; m <= counter ; m++){
        			for(Map.Entry <String, String> entry: objMap.get(m).entrySet()){
        				String attrName = entry.getKey();
        				String attrValue = entry.getValue();
        				if (updateObjNames.containsKey(attrValue)){
        					//update value
        					objMap.get(m).put(attrName, String.valueOf(updateObjNames.get(attrValue)) ) ;
        				}    			
        			}  
        		}
        		rawMap.put("A", objMap);
        	}
            
            //For B
        	rawMap.put("B", getMatch(listA, listB));            
        	//For C 
            rawMap.put("C", getMatch(listA, listC));
            rawMap.put("D", getMatch(listA, listD));
            rawMap.put("E", getMatch(listA, listE));
            rawMap.put("F", getMatch(listA, listF));
            rawMap.put("G", getMatch(listA, listG));
            rawMap.put("H", getMatch(listA, listH));
            
    		//Update values of attributes as per the updateObjNames Map for the object	
    		final Iterator<Map.Entry<Integer, HashMap<String,String>>> obj  = objMap.entrySet().iterator();
    	    while (obj.hasNext()) {
    	        Map.Entry<Integer, HashMap<String, String>> mapEntry = obj.next();
    			HashMap <String, String> attrMap = mapEntry.getValue();
    			for(Map.Entry <String, String> entry: attrMap.entrySet()){
    				String attrName = entry.getKey();
    				String attrValue = entry.getValue();
    				
    				String sol = "";
    				int count = 0;
    				for (String val: attrValue.split(",")){
    					if(count > 0){
    		    			sol = sol.concat(",");
    		    		}
    		    		if (updateObjNames.containsKey(val)){
    		    			sol = sol.concat(String.valueOf(updateObjNames.get(val)));
    		    			count ++;
    		    		}
    		    	}
    				if(count > 0)
    					objMap.get(mapEntry.getKey()).put(attrName, sol ) ;
    				
    			}  
    		}
            
            //Get match against options using rawMap.
           for(int i = 1; i <= 6 ; i++ ){
            		RavensFigure rfigOption = hmap.get(String.valueOf(i)); 
            		ArrayList <RavensObject> listOption = rfigOption.getObjects();        	
            		//for options
            		//get the object among A, B & C which has maximum number of objects for best comparison
            		if(rawMap.get("A").size() >= rawMap.get("B").size() && rawMap.get("A").size() >= rawMap.get("C").size()){
            			rawMap.put(String.valueOf(i), getMatchOfOptions(rawMap.get("A"), listOption)); 
            		} else if(rawMap.get("B").size() >= rawMap.get("C").size()){
            			rawMap.put(String.valueOf(i), getMatchOfOptions(rawMap.get("B"), listOption)); 
                    }  else if(rawMap.get("C").size() > rawMap.get("B").size()){
            			rawMap.put(String.valueOf(i), getMatchOfOptions(rawMap.get("C"), listOption)); 
                    }
            }
    	}
    	
    	
		return rawMap;
		
	}
	
}
