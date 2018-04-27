package project4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CustomAgent {

	//Solve methd
	public String CustomSolve(RavensProblem problem) {
    	
    	//Get problem type :"2x1 (Image)", "2x2 (Image)", or "3x3 (Image)
    	String problemType = problem.getProblemType();
    	String problemName = problem.getName();
    	System.out.println("--------- "+problemName+" ----------");
    	if(problemType.equalsIgnoreCase("2x1 (Image)") || problemType.equalsIgnoreCase("2x1")){
    		
    		//Establish correspondence and update the map.
    		Corresponder crspObj = new Corresponder();
    		HashMap <String, HashMap <Integer, HashMap<String, String>>> finalMap = crspObj.getCorrespondence(problem); 
    		
    		Comparer compObj = new Comparer();
        	
        	compObj.setFlip(0);
        	
        	//Map for objects
        	HashMap <Integer, HashMap<String, String>> mapObjAB = new HashMap <Integer, HashMap<String, String>> ();
        	//Map for deleted attributes for objects from A
        	HashMap <Integer, HashMap<String, String>> deletedAttrsMap = new HashMap <Integer, HashMap<String, String>> (); 
        	//Map for added attributes for objects to B
        	HashMap <Integer, HashMap<String, String>> addedAttrsMap = new HashMap <Integer, HashMap<String, String>> ();
        	
        	//List for deleted objects from A
        	List <Integer> deletedObjList = new ArrayList <Integer> ();
        	//List for added objects to B
        	List <Integer> addedObjList = new ArrayList <Integer> ();
        	
        	int countObjsA = finalMap.get("A").size();
        	int countObjsB = finalMap.get("B").size();
        	int counter = 0;
        	if(countObjsA >= countObjsB)
        		counter = countObjsA;
        	else 
        		counter = countObjsB;
        	
        	
        	
        	
    	    //if(countObjsA >=countObjsB){
        	for (int i = 1; i <= finalMap.get("A").size(); i++ ){  
        	  if(finalMap.get("A").containsKey(i) && finalMap.get("B").containsKey(i)){        		
        		HashMap<String,String> matchattrMap = new HashMap<String,String>();
        		int attrCountA = finalMap.get("A").get(i).entrySet().size();
        		int attrCountB = finalMap.get("B").get(i).entrySet().size();
        		int countA = 0;
        		int countB = 0;
        		for (Map.Entry <String,String> entryA : finalMap.get("A").get(i).entrySet()) {
				  String attrNameA = entryA.getKey();
				  String attrValueA = entryA.getValue(); 
				 
				  for (Map.Entry <String,String> entryB : finalMap.get("B").get(i).entrySet()) {
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
					for (Map.Entry <String,String> attrMapI : finalMap.get("A").get(i).entrySet()) {
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
					for (Map.Entry <String,String> attrMapI : finalMap.get("B").get(i).entrySet()) {
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
				mapObjAB.put(i, matchattrMap );
        	} else if(!finalMap.get("A").containsKey(i)){
        		if(finalMap.get("B").containsKey(i))
        			addedObjList.add((i));
        	} else if (!finalMap.get("B").containsKey(i)){
        		deletedObjList.add((i));
        	}
        	}
    	     
    	    if (countObjsA <countObjsB){
    	    	if(countObjsA == 0){
    	    		//add all objects in B in addedList.
    	    		Iterator<Map.Entry<Integer, HashMap<String,String>>> objsB  = finalMap.get("B").entrySet().iterator();
    	    	    while (objsB.hasNext()) {
    	    	    	Map.Entry<Integer, HashMap<String, String>> obj = objsB.next();
    	    	    	addedObjList.add(obj.getKey()); 
    	     			
    	    	    }
    	    	} else {
    	    		Iterator<Map.Entry<Integer, HashMap<String,String>>> objsB  = finalMap.get("B").entrySet().iterator();
    	    	    while (objsB.hasNext()) {
    	    	    	Map.Entry<Integer, HashMap<String, String>> obj = objsB.next();
    	        		if(!mapObjAB.containsKey(obj.getKey()))
    	        			addedObjList.add(obj.getKey());
    	        	}
    	        	
    	    	}
    	    }
        	
        	mapObjAB.size();
        	
        	
        	
        		
        	/*
        	 *  --------------------------Generator to generate D--------------------------
        	 *  Its a very smart generator and generates the best possible solution for D
        	 *  using it knowledge of the transformation from Problem A to Problem B & etc
        	 *  --------------------------Generator to generate D--------------------------
        	 */
        	
        	
        	//Generator genObj = new Generator();
        	//Map <String, Map<String, String>> attrMapD= genObj.generateSolution(problem, mapObj,  deletedAttrsMap, addedAttrsMap, compObj, deletedObjList, deletedAttrList);
        	
        	//Generate
    		Generator gen = new Generator();
    		HashMap <Integer, HashMap<String, String>> solutionAB = gen.generateSolution2x1_custom(finalMap, mapObjAB, deletedAttrsMap, addedAttrsMap, compObj, deletedObjList, addedObjList);
        	//---------------------------
    		//Options scoring matrix
        	List<Integer> score = new ArrayList<Integer>();
        	/*
        	 * -----------------Comparing with each of the options: Tester--------------------
        	 */        	
        	Tester testerObj = new Tester();
        	for (int i = 1; i<= 6; i++){
        		//Map for deleted attributes corresponding to an object
            	Map <String, String> deletedAttrList = new HashMap <String,String> ();
            	       
        		Integer totalScore = testerObj.getScore2x2(finalMap.get(String.valueOf(i)), solutionAB, compObj, 
        				deletedAttrsMap, addedAttrsMap, deletedObjList, addedObjList, deletedAttrList);
        		//Reset Total score for next Option
    			score.add(totalScore); 
        	}

        	//get the least score
        	for(int j = 0; j<6; j++)
        	System.out.print("["+score.get(j)+"],  ");
        	int answer = score.indexOf(Collections.max(score));        	
        	//if answers are ambigious, take a guess: 
        	//: choose second option among the highest rated
        	
        	List<Integer> sameScoresArray = new ArrayList<Integer>();
        	int varcount = 0;
        	for(int k = 0; k<6; k++){
        		if (score.get(answer) == score.get(k)){
        			sameScoresArray.add(k);
        			varcount ++;
        		}
        	}
        	if (varcount > 1)
        		answer = sameScoresArray.get(1);
        	answer = answer +1;
        	System.out.println("Answer is: " + answer);	
        	score.clear();
        	compObj.getShapeDir().clear();
        	deletedObjList.clear();
        	addedObjList.clear();
        	deletedAttrsMap.clear();
        	addedAttrsMap.clear();
        	return String.valueOf(answer);
        	
        	
        	
        	
        	
    	} else if(problemType.equalsIgnoreCase("2x2 (Image)") || problemType.equalsIgnoreCase("2x2")){
    		/*
    		 * -----------------------------------------
    		 * CASE 2 for 2x2 problems overhall done
    		 * ------------------------------------------
    		 */
    		System.out.println(" -------------------2x2 Problems -------------------");
    		
    		//Establish correspondence and update the map.
    		Corresponder crspObj = new Corresponder();
    		HashMap <String, HashMap <Integer, HashMap<String, String>>> finalMap = crspObj.getCorrespondence(problem); 
    		
        	//Create compaper Object:
        	Comparer compObj = new Comparer();
        	
        	compObj.setFlip(0);
        	
        	//Map for objects
        	HashMap <Integer, HashMap<String, String>> mapObjAB = new HashMap <Integer, HashMap<String, String>> ();
        	//Map for objects
        	HashMap <Integer, HashMap<String, String>> mapObjAC = new HashMap <Integer, HashMap<String, String>> ();
        	//Map for deleted attributes for objects from A
        	HashMap <Integer, HashMap<String, String>> deletedAttrsMap = new HashMap <Integer, HashMap<String, String>> (); 
        	//Map for added attributes for objects to B
        	HashMap <Integer, HashMap<String, String>> addedAttrsMap = new HashMap <Integer, HashMap<String, String>> ();
        	
        	//List for deleted objects from A
        	List <Integer> deletedObjList = new ArrayList <Integer> ();
        	//List for added objects to B
        	List <Integer> addedObjList = new ArrayList <Integer> ();
        	
        	int countObjsA = finalMap.get("A").size();
        	int countObjsB = finalMap.get("B").size();
        	int counter = 0;
        	if(countObjsA >= countObjsB)
        		counter = countObjsA;
        	else 
        		counter = countObjsB;
        	
        	
        	
        	
    	    //if(countObjsA >=countObjsB){
        	for (int i = 1; i <= finalMap.get("A").size(); i++ ){  
        	  if(finalMap.get("A").containsKey(i) && finalMap.get("B").containsKey(i)){        		
        		HashMap<String,String> matchattrMap = new HashMap<String,String>();
        		int attrCountA = finalMap.get("A").get(i).entrySet().size();
        		int attrCountB = finalMap.get("B").get(i).entrySet().size();
        		int countA = 0;
        		int countB = 0;
        		for (Map.Entry <String,String> entryA : finalMap.get("A").get(i).entrySet()) {
				  String attrNameA = entryA.getKey();
				  String attrValueA = entryA.getValue(); 
				 
				  for (Map.Entry <String,String> entryB : finalMap.get("B").get(i).entrySet()) {
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
					for (Map.Entry <String,String> attrMapI : finalMap.get("A").get(i).entrySet()) {
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
					for (Map.Entry <String,String> attrMapI : finalMap.get("B").get(i).entrySet()) {
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
				mapObjAB.put(i, matchattrMap );
        	} else if(!finalMap.get("A").containsKey(i)){
        		if(finalMap.get("B").containsKey(i))
        			addedObjList.add((i));
        	} else if (!finalMap.get("B").containsKey(i)){
        		deletedObjList.add((i));
        	}
        	}
    	     
    	    if (countObjsA <countObjsB){
    	    	if(countObjsA == 0){
    	    		//add all objects in B in addedList.
    	    		Iterator<Map.Entry<Integer, HashMap<String,String>>> objsB  = finalMap.get("B").entrySet().iterator();
    	    	    while (objsB.hasNext()) {
    	    	    	Map.Entry<Integer, HashMap<String, String>> obj = objsB.next();
    	    	    	addedObjList.add(obj.getKey()); 
    	     			
    	    	    }
    	    	} else {
    	    		Iterator<Map.Entry<Integer, HashMap<String,String>>> objsB  = finalMap.get("B").entrySet().iterator();
    	    	    while (objsB.hasNext()) {
    	    	    	Map.Entry<Integer, HashMap<String, String>> obj = objsB.next();
    	        		if(!mapObjAB.containsKey(obj.getKey()))
    	        			addedObjList.add(obj.getKey());
    	        	}
    	        	
    	    	}
    	    }
        	
        	mapObjAB.size();
        	
        	//A-C transformation
        	
        	int countObjsC = finalMap.get("C").size();
        	counter = 0;
        	if(countObjsA >= countObjsC)
        		counter = countObjsA;
        	else 
        		counter = countObjsC;
        	
        	for (int i = 1; i <= finalMap.get("A").size(); i++ ){  
        	  if(finalMap.get("A").containsKey(i) && finalMap.get("C").containsKey(i)){        		
        		HashMap<String,String> matchattrMap = new HashMap<String,String>();
        		int attrCountA = finalMap.get("A").get(i).entrySet().size();
        		int attrCountC = finalMap.get("C").get(i).entrySet().size();
        		int countA = 0;
        		int countC = 0;
        		for (Map.Entry <String,String> entryA : finalMap.get("A").get(i).entrySet()) {
				  String attrNameA = entryA.getKey();
				  String attrValueA = entryA.getValue(); 
				 
				  for (Map.Entry <String,String> entryC : finalMap.get("C").get(i).entrySet()) {
					  String attrNameC = entryC.getKey();
					  String attrValueC = entryC.getValue();
					  
					  //comparison
					  if(attrNameA.equalsIgnoreCase(attrNameC)){
						  countA++;
						  countC++;
						  
              			//check if values are equal or not
              			if(attrNameC.equalsIgnoreCase("shape"))
              					matchattrMap.put(attrNameA, compObj.shapeComparor(attrValueA, attrValueC));
              			else if(attrNameC.equalsIgnoreCase("fill"))
              					matchattrMap.put(attrNameA, compObj.fillComparor(attrValueA, attrValueC));
              			else if(attrNameC.equalsIgnoreCase("size"))
              					matchattrMap.put(attrNameA, compObj.sizeComparor(attrValueA, attrValueC));
              			else if(attrNameC.equalsIgnoreCase("angle")){
              					matchattrMap.put(attrNameA, compObj.angleComparor2x2(attrValueA, attrValueC));
              					if(compObj.getFlip()==1)
              						matchattrMap.put("vertical-flip", "flipped");
              					compObj.setFlip(0);
              			}
              			else if(attrNameC.equalsIgnoreCase("above"))
              					matchattrMap.put(attrNameA, compObj.aboveComparor(attrValueA, attrValueC));
              			else if(attrNameC.equalsIgnoreCase("left-of"))
              					matchattrMap.put(attrNameA, compObj.leftOfComparor(attrValueA, attrValueC));
              			else if(attrNameC.equalsIgnoreCase("inside"))
              					matchattrMap.put(attrNameA, compObj.insideComparor(attrValueA, attrValueC));
              			else if(attrNameC.equalsIgnoreCase("overlaps"))
              					matchattrMap.put(attrNameA, compObj.overlapComparor(attrValueA, attrValueC));
              			break;
						  
					  }
	        	
				  }
        		}
        	
        		if ((countA != attrCountA)){
					HashMap<String,String> tempMapA = new HashMap<String,String>();
					for (Map.Entry <String,String> attrMapI : finalMap.get("A").get(i).entrySet()) {
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
				if ((countC != attrCountC)){
					//non-matching attributes exist.
					HashMap<String,String> tempMapC = new HashMap<String,String>();
					for (Map.Entry <String,String> attrMapI : finalMap.get("C").get(i).entrySet()) {
						if(!matchattrMap.containsKey(attrMapI.getKey())){
							//if it does not contain key - add it!
							
							tempMapC.put(attrMapI.getKey(), attrMapI.getValue());
							countC++;
							if(countC == attrCountC)
								break;
						}
					}
					addedAttrsMap.put((i), tempMapC);
				}
				mapObjAC.put(i, matchattrMap );
        	} else if(!finalMap.get("A").containsKey(i)){
        		addedObjList.add((i));
        	} else if (!finalMap.get("C").containsKey(i)){
        		deletedObjList.add((i));
        	}
        	}
        	
   	     
        	if (countObjsA <countObjsC){
        		if(countObjsA == 0){
        			//add all objects in B in addedList.
        			Iterator<Map.Entry<Integer, HashMap<String,String>>> objsC  = finalMap.get("C").entrySet().iterator();
        			while (objsC.hasNext()) {
        				Map.Entry<Integer, HashMap<String, String>> obj = objsC.next();
        				addedObjList.add(obj.getKey()); 
   	     			}
        		} else {
        			Iterator<Map.Entry<Integer, HashMap<String,String>>> objsC  = finalMap.get("C").entrySet().iterator();
        			while (objsC.hasNext()) {
        				Map.Entry<Integer, HashMap<String, String>> obj = objsC.next();
        				if(!mapObjAC.containsKey(obj.getKey()))
        					addedObjList.add(obj.getKey());
        			}
        		}
        	}
        	
        	mapObjAC.size();
        	
        	
    		//Generate
    		Generator gen = new Generator();
    		HashMap <Integer, HashMap<String, String>> solutionABD = gen.generateSolution2x2(finalMap, mapObjAB, mapObjAC, deletedAttrsMap, addedAttrsMap, compObj, deletedObjList, addedObjList);
    		
    		
    		
    		
    		//Test
    		//Options scoring matrix
        	List<Integer> score = new ArrayList<Integer>();
        	/*
        	 * -----------------Comparing with each of the options: Tester--------------------
        	 */
        	
        	Tester testerObj = new Tester();
        	for (int i = 1; i<= 6; i++){
        		//Map for deleted attributes corresponding to an object
            	Map <String, String> deletedAttrList = new HashMap <String,String> ();
            	           	
            	
        		Integer totalScore = testerObj.getScore2x2(finalMap.get(String.valueOf(i)), solutionABD, compObj, 
        				deletedAttrsMap, addedAttrsMap, deletedObjList, addedObjList, deletedAttrList);
        		//Reset Total score for next Option
    			score.add(totalScore); 
        	}

        	//get the least score
        	for(int j = 0; j<6; j++)
        	System.out.print("["+score.get(j)+"],  ");
        	int answer = score.indexOf(Collections.max(score));
        	
        	//if answers are ambigious, take a guess: 
        	//: choose second option among the highest rated
        	
        	List<Integer> sameScoresArray = new ArrayList<Integer>();
        	int varcount = 0;
        	for(int k = 0; k<6; k++){
        		if (score.get(answer) == score.get(k)){
        			sameScoresArray.add(k);
        			varcount ++;
        		}
        	}
        	if (varcount > 1)
        		answer = sameScoresArray.get(1);
        	answer = answer +1;
        	System.out.println("Answer is: " + answer);	
        	score.clear();
        	compObj.getShapeDir().clear();
        	deletedObjList.clear();
        	addedObjList.clear();
        	deletedAttrsMap.clear();
        	addedAttrsMap.clear();
        	return String.valueOf(answer);
        	
    		
    	} else if(problemType.equalsIgnoreCase("3x3 (Image)") || problemType.equalsIgnoreCase("3x3")){
    		System.out.println(" 3x3 Problems");
    		Corresponder crspObj = new Corresponder();
    		HashMap <String, HashMap <Integer, HashMap<String, String>>> finalMap = crspObj.getCorrespondence(problem); 
    		
    		TransformationRecorder transRec = new TransformationRecorder();
    		
    		//Map for objects
    		HashMap <Integer, HashMap<String, String>> mapObjAB = transRec.getTransformations(finalMap.get("A"),finalMap.get("B"));        	
        	HashMap <Integer, HashMap<String, String>> mapObjAC = transRec.getTransformations(finalMap.get("A"),finalMap.get("C"));
        	HashMap <Integer, HashMap<String, String>> mapObjAD = transRec.getTransformations(finalMap.get("A"),finalMap.get("D"));        	
        	HashMap <Integer, HashMap<String, String>> mapObjAE = transRec.getTransformations(finalMap.get("A"),finalMap.get("E"));
        	HashMap <Integer, HashMap<String, String>> mapObjAF = transRec.getTransformations(finalMap.get("A"),finalMap.get("F"));        	
        	HashMap <Integer, HashMap<String, String>> mapObjAG = transRec.getTransformations(finalMap.get("A"),finalMap.get("G"));           	
        	HashMap <Integer, HashMap<String, String>> mapObjAH = transRec.getTransformations(finalMap.get("A"),finalMap.get("H"));
        	
        	HashMap <Integer, HashMap<String, String>> mapObjBC = transRec.getTransformations(finalMap.get("B"),finalMap.get("C"));        	
        	HashMap <Integer, HashMap<String, String>> mapObjBD = transRec.getTransformations(finalMap.get("B"),finalMap.get("D"));       	
        	HashMap <Integer, HashMap<String, String>> mapObjBE = transRec.getTransformations(finalMap.get("B"),finalMap.get("E"));
        	HashMap <Integer, HashMap<String, String>> mapObjBF = transRec.getTransformations(finalMap.get("B"),finalMap.get("F"));        	
        	HashMap <Integer, HashMap<String, String>> mapObjBG = transRec.getTransformations(finalMap.get("B"),finalMap.get("G"));           	
        	HashMap <Integer, HashMap<String, String>> mapObjBH = transRec.getTransformations(finalMap.get("B"),finalMap.get("H"));
        	      	
        	HashMap <Integer, HashMap<String, String>> mapObjCD = transRec.getTransformations(finalMap.get("C"),finalMap.get("D"));       	
        	HashMap <Integer, HashMap<String, String>> mapObjCE = transRec.getTransformations(finalMap.get("C"),finalMap.get("E"));
        	HashMap <Integer, HashMap<String, String>> mapObjCF = transRec.getTransformations(finalMap.get("C"),finalMap.get("F"));        	
        	HashMap <Integer, HashMap<String, String>> mapObjCG = transRec.getTransformations(finalMap.get("C"),finalMap.get("G"));           	
        	HashMap <Integer, HashMap<String, String>> mapObjCH = transRec.getTransformations(finalMap.get("C"),finalMap.get("H"));        	
          	
        	HashMap <Integer, HashMap<String, String>> mapObjDE = transRec.getTransformations(finalMap.get("D"),finalMap.get("E"));
        	HashMap <Integer, HashMap<String, String>> mapObjDF = transRec.getTransformations(finalMap.get("D"),finalMap.get("F"));        	
        	HashMap <Integer, HashMap<String, String>> mapObjDG = transRec.getTransformations(finalMap.get("D"),finalMap.get("G"));           	
        	HashMap <Integer, HashMap<String, String>> mapObjDH = transRec.getTransformations(finalMap.get("D"),finalMap.get("H"));
        	
        	HashMap <Integer, HashMap<String, String>> mapObjEF = transRec.getTransformations(finalMap.get("E"),finalMap.get("F"));        	
        	HashMap <Integer, HashMap<String, String>> mapObjEG = transRec.getTransformations(finalMap.get("E"),finalMap.get("G"));           	
        	HashMap <Integer, HashMap<String, String>> mapObjEH = transRec.getTransformations(finalMap.get("E"),finalMap.get("H"));
        	
        	HashMap <Integer, HashMap<String, String>> mapObjFG = transRec.getTransformations(finalMap.get("F"),finalMap.get("G"));        	
        	HashMap <Integer, HashMap<String, String>> mapObjFH = transRec.getTransformations(finalMap.get("F"),finalMap.get("H"));     
        	
        	HashMap <Integer, HashMap<String, String>> mapObjGH = transRec.getTransformations(finalMap.get("G"),finalMap.get("H"));
        		
        	HashMap <Integer, HashMap<String, String>> solutionI = new HashMap <Integer, HashMap<String, String>>();
        		
        	List<Integer> score = new ArrayList<Integer>();
        	// ANALYZE CASES 
        	if (finalMap.get("A").size() == 1 && finalMap.get("B").size() == 1 && finalMap.get("C").size() == 1 
        			&& finalMap.get("D").size() == 1 && finalMap.get("E").size() == 1 && finalMap.get("F").size() ==1 
        			&& finalMap.get("G").size() == 1 && finalMap.get("H").size() == 1 ){
        		
        		//CASE 1 :Check if they are all same
        		if(similarityCheckOneImage(mapObjAB).equals("same") && similarityCheckOneImage(mapObjAC).equals("same")
        				&& similarityCheckOneImage(mapObjAD).equals("same") && similarityCheckOneImage(mapObjAE).equals("same")
        				&& similarityCheckOneImage(mapObjAF).equals("same") && similarityCheckOneImage(mapObjAG).equals("same")
        				&& similarityCheckOneImage(mapObjAH).equals("same")){
        			//TODOD:Add logic
        			//Generate
            		
            		solutionI = finalMap.get("A");
            		           		
        		}
        		
        		//CASE 2 :Check if they are all same horizontally
        		else if(similarityCheckOneImage(mapObjAB).equals("same") && similarityCheckOneImage(mapObjAC).equals("same")
        				&& similarityCheckOneImage(mapObjDE).equals("same") && similarityCheckOneImage(mapObjDF).equals("same")
        				&& similarityCheckOneImage(mapObjGH).equals("same")){
        			//TODOD:Add logic
        			//Generate
            		
            		solutionI = finalMap.get("G");      
            		
        		}
        		
        		//CASE 3 :Check if they are all same vertically
        		else if(similarityCheckOneImage(mapObjAD).equals("same") && similarityCheckOneImage(mapObjAG).equals("same")
        				&& similarityCheckOneImage(mapObjBE).equals("same") && similarityCheckOneImage(mapObjBH).equals("same")
        				&& similarityCheckOneImage(mapObjCF).equals("same")){
        			//TODOD:Add logic
        			//Generate
            		
            		solutionI = finalMap.get("C"); 
            		
        		}
        		
        		//CASE 4 :Check if they are all same diagonally!
        		else if(similarityCheckOneImage(mapObjBF).equals("same") && similarityCheckOneImage(mapObjBG).equals("same")
        				&& similarityCheckOneImage(mapObjCH).equals("same") && similarityCheckOneImage(mapObjCD).equals("same")
        				&& similarityCheckOneImage(mapObjAE).equals("same")){
        			//TODOD:Add logic
        			//Generate
            		
            		solutionI = finalMap.get("A");   
            		
        		}
        		//CASE 5 :Check if they are all same diagonally other!
        		else if(similarityCheckOneImage(mapObjBD).equals("same") && similarityCheckOneImage(mapObjFH).equals("same")
        				&& similarityCheckOneImage(mapObjCE).equals("same") && similarityCheckOneImage(mapObjCG).equals("same")
        				&& similarityCheckOneImage(mapObjAF).equals("same")){
        			//TODOD:Add logic
        			//Generate
            		
            		solutionI = finalMap.get("B");   
            		
        		}
        		
        		//CASE 4 :Check if there is only update in one property per image like angle or size?
        		else if((similarityCheckOneImageOneProperty(mapObjAB).equals("angle") || similarityCheckOneImage(mapObjAB).equals("same")) 
        				&& (similarityCheckOneImageOneProperty(mapObjBC).equals("angle") || similarityCheckOneImage(mapObjBC).equals("same"))
        				&& (similarityCheckOneImageOneProperty(mapObjAD).equals("angle") || similarityCheckOneImage(mapObjAD).equals("same"))
        				&& (similarityCheckOneImageOneProperty(mapObjDE).equals("angle") || similarityCheckOneImage(mapObjDE).equals("same"))
        				&& (similarityCheckOneImageOneProperty(mapObjEF).equals("angle") || similarityCheckOneImage(mapObjEF).equals("same"))
        				&& (similarityCheckOneImageOneProperty(mapObjAG).equals("angle") || similarityCheckOneImage(mapObjAG).equals("same"))
        				&& (similarityCheckOneImageOneProperty(mapObjAH).equals("angle") || similarityCheckOneImage(mapObjAH).equals("same"))){
        			
        			
        			
            		int diffAngleAB = Integer.parseInt(finalMap.get("A").get(1).get("angle"))- Integer.parseInt(finalMap.get("B").get(1).get("angle"));
            		int diffAngleBC = Integer.parseInt(finalMap.get("B").get(1).get("angle"))- Integer.parseInt(finalMap.get("C").get(1).get("angle"));
            		int diffAngleDE = Integer.parseInt(finalMap.get("D").get(1).get("angle"))- Integer.parseInt(finalMap.get("E").get(1).get("angle"));
            		int diffAngleEF = Integer.parseInt(finalMap.get("E").get(1).get("angle"))- Integer.parseInt(finalMap.get("F").get(1).get("angle"));
            		int diffAngleGH = Integer.parseInt(finalMap.get("G").get(1).get("angle"))- Integer.parseInt(finalMap.get("H").get(1).get("angle"));
            		
            		int sumAngleAB = Integer.parseInt(finalMap.get("A").get(1).get("angle"))+ Integer.parseInt(finalMap.get("B").get(1).get("angle"));
            		int sumAngleAD = Integer.parseInt(finalMap.get("A").get(1).get("angle"))+ Integer.parseInt(finalMap.get("D").get(1).get("angle"));
            		int sumAngleDE = Integer.parseInt(finalMap.get("D").get(1).get("angle"))+ Integer.parseInt(finalMap.get("E").get(1).get("angle"));
            		int sumAngleBE = Integer.parseInt(finalMap.get("E").get(1).get("angle"))+ Integer.parseInt(finalMap.get("F").get(1).get("angle"));
            		int sumAngleGH = Integer.parseInt(finalMap.get("G").get(1).get("angle"))+ Integer.parseInt(finalMap.get("H").get(1).get("angle"));
            		int sumAngleCF = Integer.parseInt(finalMap.get("C").get(1).get("angle"))+ Integer.parseInt(finalMap.get("F").get(1).get("angle"));
            		
            		if(diffAngleAB == diffAngleBC && diffAngleDE == diffAngleEF && diffAngleDE == diffAngleGH){
            			                		
                			int angle ;
                			if(diffAngleDE < 0){
                				 angle = Integer.parseInt(finalMap.get("H").get(1).get("angle")) - diffAngleGH;
                			} else{
                				 angle = Integer.parseInt(finalMap.get("H").get(1).get("angle")) + diffAngleGH; 
                			}
                			if (angle == 360)
                				angle = 0;
                			finalMap.get("H").get(1).put("angle", String.valueOf(angle));     
                			solutionI = finalMap.get("H");
                		
            		} else if(sumAngleAB == Integer.parseInt(finalMap.get("C").get(1).get("angle")) 
            				&& sumAngleDE == Integer.parseInt(finalMap.get("F").get(1).get("angle"))){
            			//Horizontally
            			int angle = sumAngleGH;
            			if(angle ==360)
            				angle = 0;
            			
            			finalMap.get("H").get(1).put("angle", String.valueOf(angle));     
            			solutionI = finalMap.get("H");
            		} else if(sumAngleAD == Integer.parseInt(finalMap.get("G").get(1).get("angle")) 
            				&& sumAngleBE == Integer.parseInt(finalMap.get("H").get(1).get("angle"))){
            			//Vertically
            			int angle = sumAngleCF;
            			if(angle ==360)
            				angle = 0;
            			
            			finalMap.get("H").get(1).put("angle", String.valueOf(angle));     
            			solutionI = finalMap.get("H");
            		}
        			      		
                 	
            		
        		} //CASE 6 :Check if there is only update in one property per image like angle or size?
        		else if((similarityCheckOneImageOneProperty(mapObjAB).equals("size") || similarityCheckOneImage(mapObjAB).equals("same")) 
        				&& (similarityCheckOneImageOneProperty(mapObjAC).equals("size") || similarityCheckOneImage(mapObjAC).equals("same"))
        				&& (similarityCheckOneImageOneProperty(mapObjAD).equals("size") || similarityCheckOneImage(mapObjAD).equals("same"))
        				&& (similarityCheckOneImageOneProperty(mapObjAE).equals("size") || similarityCheckOneImage(mapObjAE).equals("same"))
        				&& (similarityCheckOneImageOneProperty(mapObjAF).equals("size") || similarityCheckOneImage(mapObjAF).equals("same"))
        				&& (similarityCheckOneImageOneProperty(mapObjAG).equals("size") || similarityCheckOneImage(mapObjAG).equals("same"))
        				&& (similarityCheckOneImageOneProperty(mapObjAH).equals("size") || similarityCheckOneImage(mapObjAH).equals("same"))){
        			
        			if(mapObjBF.get(1).get("size").equals(mapObjBG.get(1).get("size")) && mapObjBF.get(1).get("size").equals("same")
        					&& mapObjDH.get(1).get("size").equals(mapObjCD.get(1).get("size")) && mapObjCD.get(1).get("size").equals("same")
        					&& mapObjAE.get(1).get("size").equals("same")){
        				
        				     
            			solutionI = finalMap.get("A");
        			} else if(mapObjAB.get(1).get("size").equals(mapObjBC.get(1).get("size")) 
        					&& mapObjDE.get(1).get("size").equals(mapObjEF.get(1).get("size")) 
        					&& mapObjAD.get(1).get("size").equals(mapObjDG.get(1).get("size")) 
        					&& mapObjBE.get(1).get("size").equals(mapObjEH.get(1).get("size")) ){
        				//Horizontally & vertically progressive size change - increase/ decrease
        				String size = finalMap.get("H").get(1).get("size");
        				String finalSize="";
        				
        				//is size increasing or decreasing
        				if (mapObjAB.get(1).get("size").equals("enlarged")){
        					
        					
							if(size.equals("Xsmall"))
								finalSize = "small";
							else if(size.equals("small"))
								finalSize = "medium";
							else if(size.equals("medium"))
								finalSize = "large";
							else if(size.equals("large"))
								finalSize = "Xlarge";
							else if(size.equals("Xlarge"))
								finalSize = "XXlarge";
        				} else if(mapObjAB.get(1).get("size").equals("reduced")){
        					
							if(size.equals("small"))
								finalSize = "Xsmall";
							else if(size.equals("medium"))
								finalSize = "small";
							else if(size.equals("large"))
								finalSize = "medium";
							else if(size.equals("Xlarge"))
								finalSize = "large";
							else if(size.equals("XXlarge"))
								finalSize = "Xlarge";
        				}
        				
        				finalMap.get("H").get(1).put("size", (finalSize));     
        				solutionI = finalMap.get("H");
        			}
        			
        		} //CASE 6 :Check if there are two updates in an object per image like shape & size? Diagonally one direction
        		else if(finalMap.get("B").get(1).get("shape").equals(finalMap.get("D").get(1).get("shape"))
        				&& finalMap.get("H").get(1).get("shape").equals(finalMap.get("F").get(1).get("shape"))
        				&& finalMap.get("C").get(1).get("shape").equals(finalMap.get("E").get(1).get("shape"))
        				&& finalMap.get("C").get(1).get("shape").equals(finalMap.get("G").get(1).get("shape"))
        				&& !finalMap.get("A").get(1).get("shape").equals(finalMap.get("E").get(1).get("shape"))){
        			
        			String shape = finalMap.get("B").get(1).get("shape");
        			
        			//SUBCASE 1 -- size diagonally same - other direction
        			if(finalMap.get("B").get(1).get("size").equals(finalMap.get("F").get(1).get("size"))
            				&& finalMap.get("D").get(1).get("size").equals(finalMap.get("H").get(1).get("size"))
            				&& finalMap.get("A").get(1).get("size").equals(finalMap.get("E").get(1).get("size"))
            				&& !finalMap.get("C").get(1).get("size").equals(finalMap.get("G").get(1).get("size"))
            				&& !finalMap.get("C").get(1).get("size").equals(finalMap.get("E").get(1).get("size"))){
        				
        				String size = finalMap.get("A").get(1).get("size");     				
        				
        				finalMap.get("A").get(1).put("shape", shape);
            			solutionI = finalMap.get("A");            			
        			} 
        			//SUBCASE 2 -- size diagonally same - same direction
        			else if(finalMap.get("B").get(1).get("size").equals(finalMap.get("D").get(1).get("size"))
                				&& finalMap.get("H").get(1).get("size").equals(finalMap.get("F").get(1).get("size"))
                				&& !finalMap.get("A").get(1).get("size").equals(finalMap.get("E").get(1).get("size"))
                				&& finalMap.get("C").get(1).get("size").equals(finalMap.get("G").get(1).get("size"))
                				&& finalMap.get("C").get(1).get("size").equals(finalMap.get("E").get(1).get("size"))){
            				
            				String size = finalMap.get("B").get(1).get("size");     				
            				
            				finalMap.get("B").get(1).put("shape", shape);
                			solutionI = finalMap.get("B");            			
            		}
        			//SUBCASE 3 -- size horizonatally same 
        			else if(finalMap.get("A").get(1).get("size").equals(finalMap.get("B").get(1).get("size"))
                				&& finalMap.get("A").get(1).get("size").equals(finalMap.get("C").get(1).get("size"))
                				&& finalMap.get("D").get(1).get("size").equals(finalMap.get("E").get(1).get("size"))
                				&& finalMap.get("D").get(1).get("size").equals(finalMap.get("F").get(1).get("size"))
                				&& finalMap.get("G").get(1).get("size").equals(finalMap.get("H").get(1).get("size"))){     				
            				
            				finalMap.get("H").get(1).put("shape", shape);
                			solutionI = finalMap.get("H");            			
            		}
        			//SUBCASE 4 -- size vertically same
        			else if(finalMap.get("B").get(1).get("size").equals(finalMap.get("E").get(1).get("size"))
                				&& finalMap.get("B").get(1).get("size").equals(finalMap.get("H").get(1).get("size"))
                				&& finalMap.get("A").get(1).get("size").equals(finalMap.get("D").get(1).get("size"))
                				&& finalMap.get("A").get(1).get("size").equals(finalMap.get("G").get(1).get("size"))
                				&& finalMap.get("C").get(1).get("size").equals(finalMap.get("F").get(1).get("size"))){
            				
            				
            				finalMap.get("F").get(1).put("shape", shape);
                			solutionI = finalMap.get("F");            			
            		}
        			
        		} //CASE 7 :Check if there are two updates in an object per image like shape & size? Diagonally other direction
        		else if(finalMap.get("B").get(1).get("shape").equals(finalMap.get("F").get(1).get("shape"))
        				&& finalMap.get("D").get(1).get("shape").equals(finalMap.get("H").get(1).get("shape"))
        				&& !finalMap.get("C").get(1).get("shape").equals(finalMap.get("E").get(1).get("shape"))
        				&& !finalMap.get("C").get(1).get("shape").equals(finalMap.get("G").get(1).get("shape"))
        				&& finalMap.get("A").get(1).get("shape").equals(finalMap.get("E").get(1).get("shape"))){
        			
        			String shape = finalMap.get("A").get(1).get("shape");
        			
      
        			//SUBCASE 1 -- size diagonally same - other direction
        			if(finalMap.get("B").get(1).get("size").equals(finalMap.get("F").get(1).get("size"))
            				&& finalMap.get("D").get(1).get("size").equals(finalMap.get("H").get(1).get("size"))
            				&& finalMap.get("A").get(1).get("size").equals(finalMap.get("E").get(1).get("size"))
            				&& !finalMap.get("C").get(1).get("size").equals(finalMap.get("G").get(1).get("size"))
            				&& !finalMap.get("C").get(1).get("size").equals(finalMap.get("E").get(1).get("size"))){
        				
        				String size = finalMap.get("A").get(1).get("size");     				
        				
        				finalMap.get("A").get(1).put("shape", shape);
            			solutionI = finalMap.get("A");            			
        			} 
        			//SUBCASE 2 -- size diagonally same - same direction
        			else if(finalMap.get("B").get(1).get("size").equals(finalMap.get("D").get(1).get("size"))
                				&& finalMap.get("H").get(1).get("size").equals(finalMap.get("F").get(1).get("size"))
                				&& !finalMap.get("A").get(1).get("size").equals(finalMap.get("E").get(1).get("size"))
                				&& finalMap.get("C").get(1).get("size").equals(finalMap.get("G").get(1).get("size"))
                				&& finalMap.get("C").get(1).get("size").equals(finalMap.get("E").get(1).get("size"))){
            				
            				String size = finalMap.get("B").get(1).get("size");     				
            				
            				finalMap.get("B").get(1).put("shape", shape);
                			solutionI = finalMap.get("B");            			
            		}
        			//SUBCASE 3 -- size horizonatally same 
        			else if(finalMap.get("A").get(1).get("size").equals(finalMap.get("B").get(1).get("size"))
                				&& finalMap.get("A").get(1).get("size").equals(finalMap.get("C").get(1).get("size"))
                				&& finalMap.get("D").get(1).get("size").equals(finalMap.get("E").get(1).get("size"))
                				&& finalMap.get("D").get(1).get("size").equals(finalMap.get("F").get(1).get("size"))
                				&& finalMap.get("G").get(1).get("size").equals(finalMap.get("H").get(1).get("size"))){     				
            				
            				finalMap.get("H").get(1).put("shape", shape);
                			solutionI = finalMap.get("H");            			
            		}
        			//SUBCASE 4 -- size vertically same
        			else if(finalMap.get("B").get(1).get("size").equals(finalMap.get("E").get(1).get("size"))
                				&& finalMap.get("B").get(1).get("size").equals(finalMap.get("H").get(1).get("size"))
                				&& finalMap.get("A").get(1).get("size").equals(finalMap.get("D").get(1).get("size"))
                				&& finalMap.get("A").get(1).get("size").equals(finalMap.get("G").get(1).get("size"))
                				&& finalMap.get("C").get(1).get("size").equals(finalMap.get("F").get(1).get("size"))){
            				
            				
            				finalMap.get("F").get(1).put("shape", shape);
                			solutionI = finalMap.get("F");            			
            		}
        			
        		}
        		
        	} 
        	//2 objects each
        	else if(finalMap.get("A").size() == 2 && finalMap.get("B").size() == 2 && finalMap.get("C").size() == 2
        			&& finalMap.get("D").size() == 2 && finalMap.get("E").size() == 2 && finalMap.get("F").size() == 2
        			&& finalMap.get("G").size() == 2 && finalMap.get("H").size() == 2){
        		
        		//SUB CASE 1: Outer same horizonatlly
        		if(similarityCheckGivenImage(mapObjAB.get(1)).equals("same") && similarityCheckGivenImage(mapObjAC.get(1)).equals("same")
        				&& similarityCheckGivenImage(mapObjDE.get(1)).equals("same") && similarityCheckGivenImage(mapObjDF.get(1)).equals("same")
        				&& similarityCheckGivenImage(mapObjGH.get(1)).equals("same")){
        			//INNER CASES:
        			
        			solutionI.put(1, finalMap.get("H").get(1));
        			
        			//CASE1: Inner same vertically
        			if(similarityCheckGivenImage(mapObjAD.get(2)).equals("same") && similarityCheckGivenImage(mapObjAG.get(2)).equals("same")
            				&& similarityCheckGivenImage(mapObjBE.get(2)).equals("same") && similarityCheckGivenImage(mapObjBH.get(2)).equals("same")
            				&& similarityCheckGivenImage(mapObjCF.get(2)).equals("same")){
        				solutionI.put(2, finalMap.get("F").get(2));
        			}
        			//CASE2: Inner same horizontally
        			else if(similarityCheckGivenImage(mapObjAB.get(2)).equals("same") && similarityCheckGivenImage(mapObjAC.get(2)).equals("same")
                				&& similarityCheckGivenImage(mapObjDE.get(2)).equals("same") && similarityCheckGivenImage(mapObjDF.get(2)).equals("same")
                				&& similarityCheckGivenImage(mapObjGH.get(2)).equals("same")){
            				solutionI.put(2, finalMap.get("H").get(2));
            		}
        			//CASE3: Inner same diagonally one
        			else if(similarityCheckGivenImage(mapObjBF.get(2)).equals("same") && similarityCheckGivenImage(mapObjBG.get(2)).equals("same")
                				&& similarityCheckGivenImage(mapObjCD.get(2)).equals("same") && similarityCheckGivenImage(mapObjDH.get(2)).equals("same")
                				&& similarityCheckGivenImage(mapObjAE.get(2)).equals("same") 
                				&& similarityCheckGivenImage(mapObjCE.get(2)).equals("diff") && similarityCheckGivenImage(mapObjEG.get(2)).equals("diff")){
            				solutionI.put(2, finalMap.get("A").get(2));
            		}
        			//CASE4: Inner same diagonally other
        			else if(similarityCheckGivenImage(mapObjBD.get(2)).equals("same") && similarityCheckGivenImage(mapObjFH.get(2)).equals("same")
            				&& similarityCheckGivenImage(mapObjAE.get(2)).equals("diff") 
            				&& similarityCheckGivenImage(mapObjCE.get(2)).equals("same") && similarityCheckGivenImage(mapObjEG.get(2)).equals("same")){
        				solutionI.put(2, finalMap.get("B").get(2));
        			}
        		}
        		//SUB CASE 2: Outer same vertically
        		else if(similarityCheckGivenImage(mapObjAD.get(1)).equals("same") && similarityCheckGivenImage(mapObjAG.get(1)).equals("same")
        				&& similarityCheckGivenImage(mapObjBE.get(1)).equals("same") && similarityCheckGivenImage(mapObjBH.get(1)).equals("same")
        				&& similarityCheckGivenImage(mapObjCF.get(1)).equals("same")){
        			//INNER CASES:
        			
        			solutionI.put(1, finalMap.get("F").get(1));
        			
        			//CASE1: Inner same vertically
        			if(similarityCheckGivenImage(mapObjAD.get(2)).equals("same") && similarityCheckGivenImage(mapObjAG.get(2)).equals("same")
            				&& similarityCheckGivenImage(mapObjBE.get(2)).equals("same") && similarityCheckGivenImage(mapObjBH.get(2)).equals("same")
            				&& similarityCheckGivenImage(mapObjCF.get(2)).equals("same")){
        				solutionI.put(2, finalMap.get("F").get(2));
        			}
        			//CASE2: Inner same horizontally
        			else if(similarityCheckGivenImage(mapObjAB.get(2)).equals("same") && similarityCheckGivenImage(mapObjAC.get(2)).equals("same")
                				&& similarityCheckGivenImage(mapObjDE.get(2)).equals("same") && similarityCheckGivenImage(mapObjDF.get(2)).equals("same")
                				&& similarityCheckGivenImage(mapObjGH.get(2)).equals("same")){
            				solutionI.put(2, finalMap.get("H").get(2));
            		}
        			//CASE3: Inner same diagonally one
        			else if(similarityCheckGivenImage(mapObjBF.get(2)).equals("same") && similarityCheckGivenImage(mapObjBG.get(2)).equals("same")
                				&& similarityCheckGivenImage(mapObjCD.get(2)).equals("same") && similarityCheckGivenImage(mapObjDH.get(2)).equals("same")
                				&& similarityCheckGivenImage(mapObjAE.get(2)).equals("same") 
                				&& similarityCheckGivenImage(mapObjCE.get(2)).equals("diff") && similarityCheckGivenImage(mapObjEG.get(2)).equals("diff")){
            				solutionI.put(2, finalMap.get("A").get(2));
            		}
        			//CASE4: Inner same diagonally other
        			else if(similarityCheckGivenImage(mapObjBD.get(2)).equals("same") && similarityCheckGivenImage(mapObjFH.get(2)).equals("same")
            				&& similarityCheckGivenImage(mapObjAE.get(2)).equals("diff") 
            				&& similarityCheckGivenImage(mapObjCE.get(2)).equals("same") && similarityCheckGivenImage(mapObjEG.get(2)).equals("same")){
        				solutionI.put(2, finalMap.get("B").get(2));
        			}
        		}
        		//SUB CASE 3: Outer same diagonally one
        		else if(similarityCheckGivenImage(mapObjBF.get(1)).equals("same") && similarityCheckGivenImage(mapObjBG.get(1)).equals("same")
        				&& similarityCheckGivenImage(mapObjCD.get(1)).equals("same") && similarityCheckGivenImage(mapObjDH.get(1)).equals("same")
        				&& similarityCheckGivenImage(mapObjAE.get(1)).equals("same")
        				&& similarityCheckGivenImage(mapObjCE.get(1)).equals("diff") && similarityCheckGivenImage(mapObjCG.get(1)).equals("diff")){
        			//INNER CASES:
        			
        			solutionI.put(1, finalMap.get("A").get(1));
        			
        			//CASE1: Inner same vertically
        			if(similarityCheckGivenImage(mapObjAD.get(2)).equals("same") && similarityCheckGivenImage(mapObjAG.get(2)).equals("same")
            				&& similarityCheckGivenImage(mapObjBE.get(2)).equals("same") && similarityCheckGivenImage(mapObjBH.get(2)).equals("same")
            				&& similarityCheckGivenImage(mapObjCF.get(2)).equals("same")){
        				solutionI.put(2, finalMap.get("F").get(2));
        			}
        			//CASE2: Inner same horizontally
        			else if(similarityCheckGivenImage(mapObjAB.get(2)).equals("same") && similarityCheckGivenImage(mapObjAC.get(2)).equals("same")
                				&& similarityCheckGivenImage(mapObjDE.get(2)).equals("same") && similarityCheckGivenImage(mapObjDF.get(2)).equals("same")
                				&& similarityCheckGivenImage(mapObjGH.get(2)).equals("same")){
            				solutionI.put(2, finalMap.get("H").get(2));
            		}
        			//CASE3: Inner same diagonally one
        			else if(similarityCheckGivenImage(mapObjBF.get(2)).equals("same") && similarityCheckGivenImage(mapObjBG.get(2)).equals("same")
                				&& similarityCheckGivenImage(mapObjCD.get(2)).equals("same") && similarityCheckGivenImage(mapObjDH.get(2)).equals("same")
                				&& similarityCheckGivenImage(mapObjAE.get(2)).equals("same") 
                				&& similarityCheckGivenImage(mapObjCE.get(2)).equals("diff") && similarityCheckGivenImage(mapObjEG.get(2)).equals("diff")){
            				solutionI.put(2, finalMap.get("A").get(2));
            		}
        			//CASE4: Inner same diagonally other
        			else if(similarityCheckGivenImage(mapObjBD.get(2)).equals("same") && similarityCheckGivenImage(mapObjFH.get(2)).equals("same")
            				&& similarityCheckGivenImage(mapObjAE.get(2)).equals("diff") 
            				&& similarityCheckGivenImage(mapObjCE.get(2)).equals("same") && similarityCheckGivenImage(mapObjEG.get(2)).equals("same")){
        				solutionI.put(2, finalMap.get("B").get(2));
        			}
        		}
        		//SUB CASE 4: Outer same diagonally other
        		else if(similarityCheckGivenImage(mapObjBD.get(1)).equals("same") && similarityCheckGivenImage(mapObjFH.get(1)).equals("same")
        				&& similarityCheckGivenImage(mapObjCE.get(1)).equals("same") && similarityCheckGivenImage(mapObjCG.get(1)).equals("same")
        				&& similarityCheckGivenImage(mapObjAE.get(1)).equals("diff")){
        			//INNER CASES:
        			
        			solutionI.put(1, finalMap.get("B").get(1));
        			
        			//CASE1: Inner same vertically
        			if(similarityCheckGivenImage(mapObjAD.get(2)).equals("same") && similarityCheckGivenImage(mapObjAG.get(2)).equals("same")
            				&& similarityCheckGivenImage(mapObjBE.get(2)).equals("same") && similarityCheckGivenImage(mapObjBH.get(2)).equals("same")
            				&& similarityCheckGivenImage(mapObjCF.get(2)).equals("same")){
        				solutionI.put(2, finalMap.get("F").get(2));
        			}
        			//CASE2: Inner same horizontally
        			else if(similarityCheckGivenImage(mapObjAB.get(2)).equals("same") && similarityCheckGivenImage(mapObjAC.get(2)).equals("same")
                				&& similarityCheckGivenImage(mapObjDE.get(2)).equals("same") && similarityCheckGivenImage(mapObjDF.get(2)).equals("same")
                				&& similarityCheckGivenImage(mapObjGH.get(2)).equals("same")){
            				solutionI.put(2, finalMap.get("H").get(2));
            		}
        			//CASE3: Inner same diagonally one
        			else if(similarityCheckGivenImage(mapObjBF.get(2)).equals("same") && similarityCheckGivenImage(mapObjBG.get(2)).equals("same")
                				&& similarityCheckGivenImage(mapObjCD.get(2)).equals("same") && similarityCheckGivenImage(mapObjDH.get(2)).equals("same")
                				&& similarityCheckGivenImage(mapObjAE.get(2)).equals("same") 
                				&& similarityCheckGivenImage(mapObjCE.get(2)).equals("diff") && similarityCheckGivenImage(mapObjEG.get(2)).equals("diff")){
            				solutionI.put(2, finalMap.get("A").get(2));
            		}
        			//CASE4: Inner same diagonally other
        			else if(similarityCheckGivenImage(mapObjBD.get(2)).equals("same") && similarityCheckGivenImage(mapObjFH.get(2)).equals("same")
            				&& similarityCheckGivenImage(mapObjAE.get(2)).equals("diff") 
            				&& similarityCheckGivenImage(mapObjCE.get(2)).equals("same") && similarityCheckGivenImage(mapObjEG.get(2)).equals("same")){
        				solutionI.put(2, finalMap.get("B").get(2));
        			}
        		}
        		
        	}
        	
        	
        	
        	//airthmetic progression objects:
        	else if((finalMap.get("B").size()-finalMap.get("A").size()) == (finalMap.get("C").size()-finalMap.get("B").size())
        			&& (finalMap.get("E").size()-finalMap.get("D").size()) == (finalMap.get("F").size()-finalMap.get("E").size())
        			&& (finalMap.get("D").size()-finalMap.get("A").size()) == (finalMap.get("G").size()-finalMap.get("D").size())
        			&& (finalMap.get("E").size()-finalMap.get("B").size()) == (finalMap.get("H").size()-finalMap.get("E").size())
        			&& finalMap.get("B").size()-finalMap.get("A").size() != 0){
        		
        		//CHECKING EACH PROPERTY OF EACH OBECT
        		String shape = finalMap.get("A").get(1).get("shape");
        		String angle = finalMap.get("A").get(1).get("angle");
        		String size = finalMap.get("A").get(1).get("size");
        		String fill = finalMap.get("A").get(1).get("fill");
        		int shapeDiff = 0;
        		int fillDiff = 0;
        		int sizeDiff = 0;
        		int angleDiff = 0;

        		for (Entry<Integer, HashMap<String, String>> entry : finalMap.get("A").entrySet()) { 
        			HashMap<String,String> compMap = entry.getValue();
        			if(!compMap.get("shape").equals(shape))
        				shapeDiff = 1;
        			else if(!compMap.get("fill").equals(fill))
        				fillDiff = 1;
        			else if(!compMap.get("size").equals(size))
        				sizeDiff = 1;
        			else if(!compMap.get("angle").equals(angle))
        				angleDiff = 1;        			
        		}
        		for (Entry<Integer, HashMap<String, String>> entry : finalMap.get("B").entrySet()) { 
        			HashMap<String,String> compMap = entry.getValue();
        			if(!compMap.get("shape").equals(shape))
        				shapeDiff = 1;
        			else if(!compMap.get("fill").equals(fill))
        				fillDiff = 1;
        			else if(!compMap.get("size").equals(size))
        				sizeDiff = 1;
        			else if(!compMap.get("angle").equals(angle))
        				angleDiff = 1;
        		}
        		for (Entry<Integer, HashMap<String, String>> entry : finalMap.get("C").entrySet()) { 
        			HashMap<String,String> compMap = entry.getValue();
        			if(!compMap.get("shape").equals(shape))
        				shapeDiff = 1;
        			else if(!compMap.get("fill").equals(fill))
        				fillDiff = 1;
        			else if(!compMap.get("size").equals(size))
        				sizeDiff = 1;
        			else if(!compMap.get("angle").equals(angle))
        				angleDiff = 1;
        		}
        		for (Entry<Integer, HashMap<String, String>> entry : finalMap.get("D").entrySet()) { 
        			HashMap<String,String> compMap = entry.getValue();
        			if(!compMap.get("shape").equals(shape))
        				shapeDiff = 1;
        			else if(!compMap.get("fill").equals(fill))
        				fillDiff = 1;
        			else if(!compMap.get("size").equals(size))
        				sizeDiff = 1;
        			else if(!compMap.get("angle").equals(angle))
        				angleDiff = 1;
        		}
        		for (Entry<Integer, HashMap<String, String>> entry : finalMap.get("E").entrySet()) { 
        			HashMap<String,String> compMap = entry.getValue();
        			if(!compMap.get("shape").equals(shape))
        				shapeDiff = 1;
        			else if(!compMap.get("fill").equals(fill))
        				fillDiff = 1;
        			else if(!compMap.get("size").equals(size))
        				sizeDiff = 1;
        			else if(!compMap.get("angle").equals(angle))
        				angleDiff = 1;
        		}
        		for (Entry<Integer, HashMap<String, String>> entry : finalMap.get("F").entrySet()) { 
        			HashMap<String,String> compMap = entry.getValue();
        			if(!compMap.get("shape").equals(shape))
        				shapeDiff = 1;
        			else if(!compMap.get("fill").equals(fill))
        				fillDiff = 1;
        			else if(!compMap.get("size").equals(size))
        				sizeDiff = 1;
        			else if(!compMap.get("angle").equals(angle))
        				angleDiff = 1;
        		}
        		for (Entry<Integer, HashMap<String, String>> entry : finalMap.get("G").entrySet()) { 
        			HashMap<String,String> compMap = entry.getValue();
        			if(!compMap.get("shape").equals(shape))
        				shapeDiff = 1;
        			else if(!compMap.get("fill").equals(fill))
        				fillDiff = 1;
        			else if(!compMap.get("size").equals(size))
        				sizeDiff = 1;
        			else if(!compMap.get("angle").equals(angle))
        				angleDiff = 1;
        		}
        		for (Entry<Integer, HashMap<String, String>> entry : finalMap.get("H").entrySet()) { 
        			HashMap<String,String> compMap = entry.getValue();
        			if(!compMap.get("shape").equals(shape))
        				shapeDiff = 1;
        			else if(!compMap.get("fill").equals(fill))
        				fillDiff = 1;
        			else if(!compMap.get("size").equals(size))
        				sizeDiff = 1;
        			else if(!compMap.get("angle").equals(angle))
        				angleDiff = 1;
        		}
        		
        		//if all the shapes, sizes, fills and angles are same
            	if(shapeDiff == 0 && fillDiff == 0 && sizeDiff == 0){
            		int count = finalMap.get("H").size();
            		int difference = finalMap.get("H").size()- finalMap.get("G").size();
            		
            		HashMap<Integer, HashMap<String, String>> objMap = new HashMap <Integer, HashMap<String, String>> ();
            		HashMap <String,String> map = new HashMap <String,String>();
            		
            		if(finalMap.get("H").size() == 1 && difference == -1)
            			solutionI = objMap;
            		else {
            		
            		
            		
            		for (Entry<Integer, HashMap<String, String>> entry : finalMap.get("H").entrySet()) { 
            			HashMap<String,String> compMap = entry.getValue();
            			map.put("shape", compMap.get("shape"));
                		map.put("fill", compMap.get("fill"));
                		map.put("size", compMap.get("size"));
                		map.put("angle", compMap.get("angle"));
                		break;
            		}
            		
            		
            		for(int m = 1; m <= difference; m++)
            			objMap.put(count+m, map);
            		
            		for (Entry<Integer, HashMap<String, String>> entry : finalMap.get("H").entrySet()) { 
            			objMap.put(entry.getKey(), entry.getValue());
            		}
            		
            		solutionI = objMap;
            		}
            		
            		
            	} //if all the shapes, sizes, fills are same
            	//ANGLE NOT SAME
            	else if(shapeDiff == 0 && fillDiff == 0 && sizeDiff == 0 && angleDiff == 1){
            		
            	} //if all the shapes, sizes, angles are same
            	//FILL NOT SAME
            	else if(shapeDiff == 0 && fillDiff == 1 && sizeDiff == 0 && angleDiff == 0){
        		
            	} //if all the shapes, angles, fills are same
            	//SIZE NOT SAME
            	else if(shapeDiff == 0 && fillDiff == 0 && sizeDiff == 1 && angleDiff == 0){
        		
            	} //if all the sizes, angles, fills are same
            	//SHAPE NOT SAME
            	else if(shapeDiff == 1 && fillDiff == 0 && sizeDiff == 0){
            		
            		String shapeValue = "";
            		
            		int count = finalMap.get("H").size();
            		int difference = finalMap.get("H").size()- finalMap.get("G").size();
            		
            		HashMap<Integer, HashMap<String, String>> objMap = new HashMap <Integer, HashMap<String, String>> ();
            		HashMap <String,String> map = new HashMap <String,String>();
            		
            		if(finalMap.get("A").get(1).get("shape").equals(finalMap.get("B").get(1).get("shape"))
            			&& finalMap.get("A").get(1).get("shape").equals(finalMap.get("C").get(1).get("shape"))
            			&& finalMap.get("D").get(1).get("shape").equals(finalMap.get("E").get(1).get("shape"))
            			&& finalMap.get("D").get(1).get("shape").equals(finalMap.get("F").get(1).get("shape"))
            			&& finalMap.get("G").get(1).get("shape").equals(finalMap.get("H").get(1).get("shape"))){
            				//horizontally same
            			shapeValue = finalMap.get("H").get(1).get("shape");
            			map.put("shape", shapeValue);
                		map.put("fill", finalMap.get("H").get(1).get("fill"));
                		map.put("size", finalMap.get("H").get(1).get("size"));
                		map.put("angle", finalMap.get("H").get(1).get("angle"));
                		for (Entry<Integer, HashMap<String, String>> entry : finalMap.get("H").entrySet()) { 
                			objMap.put(entry.getKey(), entry.getValue());
                		}
            			
            		} else if(finalMap.get("A").get(1).get("shape").equals(finalMap.get("D").get(1).get("shape"))
                			&& finalMap.get("A").get(1).get("shape").equals(finalMap.get("G").get(1).get("shape"))
                			&& finalMap.get("B").get(1).get("shape").equals(finalMap.get("E").get(1).get("shape"))
                			&& finalMap.get("B").get(1).get("shape").equals(finalMap.get("H").get(1).get("shape"))){
        				//vertically same
            			shapeValue = finalMap.get("F").get(1).get("shape");
            			map.put("shape", shapeValue);
                		map.put("fill", finalMap.get("F").get(1).get("fill"));
                		map.put("size", finalMap.get("F").get(1).get("size"));
                		map.put("angle", finalMap.get("F").get(1).get("angle"));
                		for (Entry<Integer, HashMap<String, String>> entry : finalMap.get("F").entrySet()) { 
                			objMap.put(entry.getKey(), entry.getValue());
                		}
            		}
            		
            		for(int m = 1; m <= difference; m++)
            			objMap.put(count+m, map);
            		
            		
            		
            		solutionI = objMap;
        		
            	} //if more than two prop are different
            	//SHAPE NOT SAME
            	else if(shapeDiff == 1 && fillDiff == 1 && sizeDiff == 1 && angleDiff == 1){
        		
            	} 
        	}
        	//ALL OTHER CASES
        	else {
        		//problem 18
        		if(similarityCheckMultipleImage(mapObjBF).equals("same") && similarityCheckMultipleImage(mapObjBG).equals("same")  
        				&& similarityCheckMultipleImage(mapObjCD).equals("same") && similarityCheckMultipleImage(mapObjDH).equals("same")
        				&& similarityCheckMultipleImage(mapObjAE).equals("same")){
        			solutionI = finalMap.get("A");
        		}
        		//problem 20
        		else {
        			//a->c: apply the transformation on G to get solution I
        			
        			HashMap <Integer, HashMap<String, String>> deletedAttrsMap = new HashMap <Integer, HashMap<String, String>> (); 
                	HashMap <Integer, HashMap<String, String>> addedAttrsMap = new HashMap <Integer, HashMap<String, String>> ();
                	
        			Comparer compObj = new Comparer();
        			//if(countObjsA >=countObjsB){
                	for (int i = 1; i <= finalMap.get("A").size(); i++ ){  
                	  if(finalMap.get("A").containsKey(i) && finalMap.get("C").containsKey(i)){        		
                		HashMap<String,String> matchattrMap = new HashMap<String,String>();
                		int attrCountA = finalMap.get("A").get(i).entrySet().size();
                		int attrCountC = finalMap.get("C").get(i).entrySet().size();
                		int countA = 0;
                		int countC = 0;
                		for (Map.Entry <String,String> entryA : finalMap.get("A").get(i).entrySet()) {
        				  String attrNameA = entryA.getKey();
        				  String attrValueA = entryA.getValue(); 
        				 
        				  for (Map.Entry <String,String> entryC : finalMap.get("C").get(i).entrySet()) {
        					  String attrNameC = entryC.getKey();
        					  String attrValueC = entryC.getValue();
        					  
        					  //comparison
        					  if(attrNameA.equalsIgnoreCase(attrNameC)){
        						  countA++;
        						  countC++;
        						  
                      			//check if values are equal or not
                      			if(attrNameC.equalsIgnoreCase("shape"))
                      					matchattrMap.put(attrNameA, compObj.shapeComparor(attrValueA, attrValueC));
                      			else if(attrNameC.equalsIgnoreCase("fill"))
                      					matchattrMap.put(attrNameA, compObj.fillComparor(attrValueA, attrValueC));
                      			else if(attrNameC.equalsIgnoreCase("size"))
                      					matchattrMap.put(attrNameA, compObj.sizeComparor(attrValueA, attrValueC));
                      			else if(attrNameC.equalsIgnoreCase("angle")){
                      					matchattrMap.put(attrNameA, compObj.angleComparor2x2(attrValueA, attrValueC));
                      					if(compObj.getFlip()==1)
                      						matchattrMap.put("vertical-flip", "flipped");
                      					compObj.setFlip(0);
                      			}
                      			else if(attrNameC.equalsIgnoreCase("above")){
                      					//matchattrMap.put(attrNameA, compObj.aboveComparor(attrValueA, attrValueC));
                      			} else if(attrNameC.equalsIgnoreCase("left-of")){
                      					//matchattrMap.put(attrNameA, compObj.leftOfComparor(attrValueA, attrValueC));
                      			} else if(attrNameC.equalsIgnoreCase("inside"))
                      					matchattrMap.put(attrNameA, compObj.insideComparor(attrValueA, attrValueC));
                      			else if(attrNameC.equalsIgnoreCase("overlap") || attrNameC.equalsIgnoreCase("overlaps"))
                      					matchattrMap.put(attrNameA, compObj.overlapComparor(attrValueA, attrValueC));
                      			break;
        						  
        					  }
        	        	
        				  }
                		}
                	
                		if ((countA != attrCountA)){
        					HashMap<String,String> tempMapA = new HashMap<String,String>();
        					for (Map.Entry <String,String> attrMapI : finalMap.get("A").get(i).entrySet()) {
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
        				if ((countC != attrCountC)){
        					//non-matching attributes exist.
        					HashMap<String,String> tempMapC = new HashMap<String,String>();
        					for (Map.Entry <String,String> attrMapI : finalMap.get("C").get(i).entrySet()) {
        						if(!matchattrMap.containsKey(attrMapI.getKey())){
        							//if it does not contain key - add it!
        							
        							tempMapC.put(attrMapI.getKey(), attrMapI.getValue());
        							countC++;
        							if(countC == attrCountC)
        								break;
        						}
        					}
        					addedAttrsMap.put((i), tempMapC);
        				}
        				mapObjAC.put(i, matchattrMap );
                	} 
                	}
            	     
                	
                	mapObjAC.size();
                	

                	//List for deleted objects from A
                	List <Integer> deletedObjList = new ArrayList <Integer> ();
                	//List for added objects to B
                	List <Integer> addedObjList = new ArrayList <Integer> ();
                	
                	//Generate
            		Generator gen = new Generator();
            		solutionI = gen.generateSolution3x3_custom(finalMap, mapObjAC, deletedAttrsMap, addedAttrsMap, compObj, deletedObjList, addedObjList);
                	
                	
        			
        		}
        		
        	}
        	
        	
        	
        	/*
        	 * -----------------Comparing with each of the options: Tester--------------------
        	 */
        	//Test
    		//Options scoring matrix  
        	//----------------------------------
        /*	for (int i = 1; i<= 6; i++){
            	           	
            	Integer totalScore; 
            	HashMap <Integer, HashMap<String,String>> mapObjIOpt = transRec.getTransformations(solutionI,finalMap.get(String.valueOf(i)));     	
            	if(similarityCheckOneImage(mapObjIOpt).equals("same")){
            		totalScore = 1;                    		
            	} else 
            		totalScore = 0;
            	score.add(totalScore); 
        	}
        	
        	if(score.size() != 0){
        	for(int j = 0; j<6; j++)
        	System.out.print("["+score.get(j)+"],  ");
        	int answer = score.indexOf(Collections.max(score));
        	
        	
        	answer = answer +1;
        	
        	System.out.println("Answer is: " + answer);	
        	score.clear();
        	return String.valueOf(answer);
        	} else 
        		return "7";
        	
        	*/
        	//-------------------------------------------------
        	
        	Tester testerObj = new Tester();
        	for (int i = 1; i<= 6; i++){
        		//Map for deleted attributes corresponding to an object            	           	
            	
        		Integer totalScore = testerObj.getScore3x3(finalMap.get(String.valueOf(i)), solutionI);
        		//Reset Total score for next Option
    			score.add(totalScore); 
        	}

        	//get the least score
        	for(int j = 0; j<6; j++)
        	System.out.print("["+score.get(j)+"],  ");
        	int answer = score.indexOf(Collections.max(score));
        	
        	//if answers are ambigious, take a guess: 
        	//: choose second option among the highest rated
        	
        	List<Integer> sameScoresArray = new ArrayList<Integer>();
        	int varcount = 0;
        	for(int k = 0; k<6; k++){
        		if (score.get(answer) == score.get(k)){
        			sameScoresArray.add(k);
        			varcount ++;
        		}
        	}
        	if (varcount > 1)
        		answer = sameScoresArray.get(0);
        	answer = answer +1;
        	System.out.println("Answer is: " + answer);	
        	score.clear();
        	
        	return String.valueOf(answer);
        	
        	
    	} else 
    		System.out.println(" Matrix size not known!");
    	
        return "7";
        
    }
	
	public String similarityCheckOneImage(HashMap <Integer, HashMap<String, String>> mapObj){
		
		int count = 0;
		for (Map.Entry <String,String> obj : mapObj.get(1).entrySet()) {
			
				if(!obj.getValue().equals("same")){
					count++;
				}
			
		}
		
		if(count == 0){
			return "same";
		} else
			return "diff";
	}
	
	public String similarityCheckGivenImage(HashMap<String, String> mapObj){
		
		int count = 0;
		for (Map.Entry <String,String> obj : mapObj.entrySet()) {
			
				if(!obj.getValue().equals("same")){
					count++;
				}
			
		}
		
		if(count == 0){
			return "same";
		} else
			return "diff";
	}
	
	public String similarityCheckMultipleImage(HashMap <Integer, HashMap<String, String>> mapObj){
		
		int count = 0;
		
		for (Entry<Integer, HashMap<String, String>> entry : mapObj.entrySet()) { 		
			for (Map.Entry <String,String> obj : entry.getValue().entrySet()) {			
				if(!obj.getValue().equals("same"))
					count++;
			}
		}
		
		if(count == 0){
			return "same";
		} else
			return "diff";
	}
	
	
	public String similarityCheckOneImageOneProperty(HashMap <Integer, HashMap<String, String>> mapObj){
		
		int countSame = 0;
		int countDiff= 0;
		String changedProperty = "";
		for (Map.Entry <String,String> obj : mapObj.get(1).entrySet()) {
			if(!obj.getKey().equals("vertical-flip")){
				if(!obj.getValue().equals("same")){
					countDiff++;
					changedProperty = obj.getKey();
				} else {
					countSame++;
				}
			}
		}
		
		if(countDiff == 1 && countSame >=1)
			return changedProperty;
		else 
			return "diff";
	}
	
	
}
