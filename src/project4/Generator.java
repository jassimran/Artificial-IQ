/**
 * 
 */
package project4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jassimran
 *
 */
class Generator {

	/*
	 *  --------------------------Generator to generate D--------------------------
	 *  Its a very smart generator and generates the best possible solution for D
	 *  using it knowledge of the transformation from Problem A to Problem B & etc
	 *  --------------------------Generator to generate D--------------------------
	 */
	
	
	//Create Attribute list for fictional solution D for 2x 1 problems
	public Map <String, Map<String, String>>  generateSolution(
			RavensProblem problem, 
			Map <String, HashMap<String, String>> mapObj, 
			Map <String, HashMap<String, String>> deletedAttrsMap,
        	Map <String, HashMap<String, String>> addedAttrsMap, Comparer compObj,
        	List <String> deletedObjList, Map <String, String> deletedAttrList ){
		
		int countObjsMappedC = 0;
		Map <String, Map<String, String>> attrMapD = new HashMap<String, Map<String, String>>();
		
		//Get problem type :"2x1", "2x2", or "3x3
    	String problemType = problem.getProblemType();
    	String problemName = problem.getName();
    	System.out.println("--------- "+problemName+" ----------");
    	if(problemType.equalsIgnoreCase("2x1 (Image)")){
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
        	
    	
		
		for (RavensObject attrObjC : listC){
			HashMap<String,String> matchattrMapC = new HashMap<String,String>();
			int circleFlag = 0;
			int circleAngleFlag = 0;
			int squareFlag = 0;
			int squareAngleFlag = 0;
			int triangleFlag = 0;
			int triangleAngleFlag = 0;
			String objectNameC = attrObjC.getName();
			if(mapObj.containsKey(objectNameC)){
				countObjsMappedC++;
			
			//check if it has circle and angle combination
			for(RavensAttribute attrC : attrObjC.getAttributes()){
				String attrNameC = attrC.getName();
				String attrValueC = attrC.getValue();
				
				//circle check
				if(attrNameC.equals("shape") && attrValueC.equalsIgnoreCase("circle")){
					circleFlag = 1;
				} else if(attrNameC.equals("angle")){
					circleAngleFlag = 1;
				}
				
				//square and 90,180,270,360 angle check
				if(attrNameC.equals("shape") && (attrValueC.equalsIgnoreCase("square")) ){
					squareFlag = 1;
				} else if(attrNameC.equals("angle") && 
						( attrValueC.equalsIgnoreCase("90") || attrValueC.equalsIgnoreCase("180") 
						|| attrValueC.equalsIgnoreCase("270") || attrValueC.equalsIgnoreCase("360"))){
					squareAngleFlag = 1;
				}
				
				//triangle and 120,240,360 angle check
				if(attrNameC.equals("shape") && (attrValueC.equalsIgnoreCase("triangle")) ){
					triangleFlag = 1;
				} else if(attrNameC.equals("angle") && ( attrValueC.equalsIgnoreCase("120") || attrValueC.equalsIgnoreCase("240") || attrValueC.equalsIgnoreCase("360")))
					triangleAngleFlag = 1;
				
				
			}
			int countAttrC = 0;
			int countAttrObj = mapObj.get(objectNameC).size();
			for(RavensAttribute attrC : attrObjC.getAttributes()){
				String attrNameC = attrC.getName();
				String attrValueC = attrC.getValue();
				
				
				if((mapObj.get(objectNameC)).containsKey(attrNameC)) {
					compObj.setFlip(0);
					HashMap <String, String >transD = mapObj.get(objectNameC);
					
					if(attrNameC.equalsIgnoreCase("shape")){
						countAttrC++;
						if(transD.get(attrNameC).equalsIgnoreCase("same"))
							matchattrMapC.put(attrNameC, attrValueC);
						else if (transD.get(attrNameC).equalsIgnoreCase("changed")){
							if(compObj.getShapeDir().containsKey(attrValueC)){
								matchattrMapC.put(attrNameC, compObj.getShapeDir().get(attrValueC));
							} else {
								double rel = 1.0;
								//get the relationship between the two and derive correct answer
								for (Map.Entry<String,String> entry : compObj.getShapeDir().entrySet()) {
									  String shapeorig = entry.getKey();
									  String shapefnl = entry.getValue();
									  if(compObj.getEdgesDir().containsKey(shapeorig) && compObj.getEdgesDir().containsKey(shapefnl)){
										  rel = (double)compObj.getEdgesDir().get(shapeorig)/(double)compObj.getEdgesDir().get(shapefnl);
										  break;
									  }
								}
								//derive correct shape
								if(compObj.getEdgesDir().containsKey(attrValueC)){
									int noOfEdgesD = (int) ((int)compObj.getEdgesDir().get(attrValueC)/rel);
									for (Map.Entry<String,Integer> entry : compObj.getEdgesDir().entrySet()) {
	  								  String shape = entry.getKey();
	  								  Integer shapeEdges = entry.getValue();
	  								  if(noOfEdgesD == shapeEdges){
	  									matchattrMapC.put(attrNameC, shape);
	  									break;
	  								  }
									}
								}
							}
						}
					} else if(attrNameC.equalsIgnoreCase("fill") ){
						countAttrC++;
						if(transD.get(attrNameC).equalsIgnoreCase("same"))
							matchattrMapC.put(attrNameC, attrValueC);
						else if(transD.get(attrNameC).equalsIgnoreCase("filled"))
							matchattrMapC.put(attrNameC, "yes");
						else if (transD.get(attrNameC).equalsIgnoreCase("unfilled"))
	     					matchattrMapC.put(attrNameC, "no");
						else if (transD.get(attrNameC).equalsIgnoreCase("filledno2lh"))
	     					matchattrMapC.put(attrNameC, "left-half");
						else if (transD.get(attrNameC).equalsIgnoreCase("filledno2rh"))
	     					matchattrMapC.put(attrNameC, "right-half");
						else if (transD.get(attrNameC).equalsIgnoreCase("filledno2th"))
	     					matchattrMapC.put(attrNameC, "top-half");
						else if (transD.get(attrNameC).equalsIgnoreCase("filledno2bh"))
	     					matchattrMapC.put(attrNameC, "bottom-half");
						else if(transD.get(attrNameC).equalsIgnoreCase("fillArray")){
							//TODO: Add all cases
							for (String fillVal: attrValueC.split(",")){
				        		if (fillVal.equalsIgnoreCase("top-left")){
				        			compObj.fillArray[0] = compObj.fillArray[0] | 1;
				        		} else if (fillVal.equalsIgnoreCase("top-right")){
				        			compObj.fillArray[1] = compObj.fillArray[0] | 1;;
				        		} else if (fillVal.equalsIgnoreCase("bottom-left")){
				        			compObj.fillArray[2] = compObj.fillArray[0] | 1;;
				        		} else if (fillVal.equalsIgnoreCase("bottom-right")){
				        			compObj.fillArray[3] = compObj.fillArray[0] | 1;;
				        		}
				        	}
							String result = String.valueOf(compObj.fillArray[0])+ "," + 
									String.valueOf(compObj.fillArray[1])+ "," + 
									String.valueOf(compObj.fillArray[2])+ "," +
									String.valueOf(compObj.fillArray[3]);
							//Add output to map
							matchattrMapC.put(attrNameC, result);
							//reset array for get fill map for each choice 1-6
							for(int i= 0; i< 4; i++)
								compObj.fillArray[i] = 0;            					 
						}
						       				
					} 
					else if(attrNameC.equalsIgnoreCase("size")){
						countAttrC++;
						if (transD.get(attrNameC).equalsIgnoreCase("same")){
							matchattrMapC.put(attrNameC, attrValueC);
						} else if(transD.get(attrNameC).equalsIgnoreCase("enlarged")){
							String val = "";
							if(attrValueC.equals("Xsmall"))
								val = "small";
							else if(attrValueC.equals("small"))
								val = "medium";
							else if(attrValueC.equals("medium"))
								val = "large";
							else if(attrValueC.equals("large"))
								val = "Xlarge";
							else if(attrValueC.equals("Xlarge"))
								val = "XXlarge";
						
							
							matchattrMapC.put(attrNameC, val);
						} else if(transD.get(attrNameC).equalsIgnoreCase("reduced")){
							String val = "";
							if(attrValueC.equals("small"))
								val = "Xsmall";
							else if(attrValueC.equals("medium"))
								val = "small";
							else if(attrValueC.equals("large"))
								val = "medium";
							else if(attrValueC.equals("Xlarge"))
								val = "large";
							else if(attrValueC.equals("XXlarge"))
								val = "Xlarge";
							
							matchattrMapC.put(attrNameC, val);
						}
							
						/*
						if (transD.get(attrNameC).equalsIgnoreCase("same"))
							matchattrMapC.put(attrNameC, attrValueC);
						else if(transD.get(attrNameC).equalsIgnoreCase("enlargeds2m"))
							matchattrMapC.put(attrNameC, "medium");
						else if(transD.get(attrNameC).equalsIgnoreCase("reducedm2s"))
							matchattrMapC.put(attrNameC, "small");
						else if(transD.get(attrNameC).equalsIgnoreCase("enlargeds2l"))
							matchattrMapC.put(attrNameC, "large");
						else if(transD.get(attrNameC).equalsIgnoreCase("reducedl2s"))
							matchattrMapC.put(attrNameC, "small");
						else if(transD.get(attrNameC).equalsIgnoreCase("enlargedm2l"))
							matchattrMapC.put(attrNameC, "large");
						else if( transD.get(attrNameC).equalsIgnoreCase("reducedl2m"))
							matchattrMapC.put(attrNameC, "medium");
						else if(transD.get(attrNameC).equalsIgnoreCase("enlargedl2Xl"))
							matchattrMapC.put(attrNameC, "Xlarge");
						else if( transD.get(attrNameC).equalsIgnoreCase("reducedXl2l"))
							matchattrMapC.put(attrNameC, "large");
						else if(transD.get(attrNameC).equalsIgnoreCase("enlargedm2Xl"))
							matchattrMapC.put(attrNameC, "Xlarge");
						else if( transD.get(attrNameC).equalsIgnoreCase("reducedXl2m"))
							matchattrMapC.put(attrNameC, "medium");
						else if(transD.get(attrNameC).equalsIgnoreCase("enlargeds2Xl"))
							matchattrMapC.put(attrNameC, "Xlarge");
						else if( transD.get(attrNameC).equalsIgnoreCase("reducedXl2s"))
							matchattrMapC.put(attrNameC, "small");
						else if(transD.get(attrNameC).equalsIgnoreCase("enlargedXl2XXl"))
							matchattrMapC.put(attrNameC, "XXlarge");
						else if( transD.get(attrNameC).equalsIgnoreCase("reducedXXl2Xl"))
							matchattrMapC.put(attrNameC, "Xlarge");
						else if(transD.get(attrNameC).equalsIgnoreCase("enlargedl2XXl"))
							matchattrMapC.put(attrNameC, "XXlarge");
						else if( transD.get(attrNameC).equalsIgnoreCase("reducedXXl2l"))
							matchattrMapC.put(attrNameC, "large");
						else if(transD.get(attrNameC).equalsIgnoreCase("enlargedm2XXl"))
							matchattrMapC.put(attrNameC, "XXlarge");
						else if( transD.get(attrNameC).equalsIgnoreCase("reducedXXl2m"))
							matchattrMapC.put(attrNameC, "medium");
						else if(transD.get(attrNameC).equalsIgnoreCase("enlargeds2XXl"))
							matchattrMapC.put(attrNameC, "XXlarge");
						else if( transD.get(attrNameC).equalsIgnoreCase("reducedXXl2s"))
							matchattrMapC.put(attrNameC, "small");
						//new Xsmall cases
						else if(transD.get(attrNameC).equalsIgnoreCase("enlargedXs2XXl"))
							matchattrMapC.put(attrNameC, "XXlarge");
						else if( transD.get(attrNameC).equalsIgnoreCase("reducedXXl2Xs"))
							matchattrMapC.put(attrNameC, "Xsmall");
						else if(transD.get(attrNameC).equalsIgnoreCase("enlargedXs2Xl"))
							matchattrMapC.put(attrNameC, "Xlarge");
						else if( transD.get(attrNameC).equalsIgnoreCase("reducedXl2Xs"))
							matchattrMapC.put(attrNameC, "Xsmall");
						else if(transD.get(attrNameC).equalsIgnoreCase("enlargedXs2l"))
							matchattrMapC.put(attrNameC, "large");
						else if( transD.get(attrNameC).equalsIgnoreCase("reducedl2Xs"))
							matchattrMapC.put(attrNameC, "Xsmall");
						else if(transD.get(attrNameC).equalsIgnoreCase("enlargedXs2m"))
							matchattrMapC.put(attrNameC, "medium");
						else if( transD.get(attrNameC).equalsIgnoreCase("reducedm2Xs"))
							matchattrMapC.put(attrNameC, "Xsmall");
						else if(transD.get(attrNameC).equalsIgnoreCase("enlargedXs2s"))
							matchattrMapC.put(attrNameC, "small");
						else if( transD.get(attrNameC).equalsIgnoreCase("reduceds2Xs"))
							matchattrMapC.put(attrNameC, "Xsmall");
						*/
						
						
					} 
					else if(attrNameC.equalsIgnoreCase("angle") && ( (circleFlag==1) && (circleAngleFlag==1) )){
						countAttrC++;
						//do not put angle as key comparison factor in c
					}
					else if(attrNameC.equalsIgnoreCase("angle")){ 
						countAttrC++;
						if(transD.get(attrNameC).equalsIgnoreCase("same"))
							matchattrMapC.put(attrNameC, attrValueC);
						else if(triangleFlag == 1 && transD.containsKey("vertical-flip")){
							matchattrMapC.put(attrNameC,attrValueC );
						}
						else if(transD.get(attrNameC).equalsIgnoreCase("rotated45clockwise") || transD.get(attrNameC).equalsIgnoreCase("rotated45anticlockwise"))
							matchattrMapC.put(attrNameC, "45");
						else if( transD.get(attrNameC).equalsIgnoreCase("rotated180clockwise")){
							
							if((Integer.parseInt(attrValueC) + 180) < 360)
								matchattrMapC.put(attrNameC, String.valueOf(Integer.parseInt(attrValueC) + 180));
							else if((Integer.parseInt(attrValueC) + 180) > 360)
								matchattrMapC.put(attrNameC, String.valueOf(Integer.parseInt(attrValueC) - 180));
							else if((Integer.parseInt(attrValueC) + 180) == 360)
								matchattrMapC.put(attrNameC, "0");
							compObj.setFlip(1);
						}	
						else if(transD.get(attrNameC).equalsIgnoreCase("rotated180anticlockwise"))
							matchattrMapC.put(attrNameC, String.valueOf(Integer.parseInt(attrValueC) - 180));
						
						else {
							if(( (squareFlag==1) && (squareAngleFlag==1) )){
								matchattrMapC.put(attrNameC, attrValueC);
							} else if(( (triangleFlag==1) && (triangleAngleFlag==1) )){
								matchattrMapC.put(attrNameC, attrValueC);
							} 
							int res = Integer.parseInt(attrValueC) + Integer.parseInt(transD.get(attrNameC));
							matchattrMapC.put(attrNameC, String.valueOf(res));
						}
					} 
					else if(attrNameC.equalsIgnoreCase("above") && transD.get(attrNameC).equalsIgnoreCase("same")){
						countAttrC++;
						matchattrMapC.put(attrNameC, attrValueC);
					} else if(attrNameC.equalsIgnoreCase("above") && transD.get(attrNameC).equalsIgnoreCase("notabove")){
						countAttrC++;
						//TODO: Handle condition
						//RavensAttribute attrD = new RavensAttribute(attrNameC,"0");
						//attrListD.add(attrD);
					} else if(attrNameC.equalsIgnoreCase("inside")){ 
						countAttrC++;
						if(transD.get(attrNameC).equalsIgnoreCase("same"))
							matchattrMapC.put(attrNameC,attrValueC );
						else if(transD.get(attrNameC).equalsIgnoreCase("deleted")){
							//it should not be present in the list
							deletedAttrList.put(objectNameC, attrNameC);
							
						}
					} else if(attrNameC.equalsIgnoreCase("left-of") && transD.get(attrNameC).equalsIgnoreCase("same")){
						countAttrC++;
						matchattrMapC.put(attrNameC,attrValueC );
					} else if(attrNameC.equalsIgnoreCase("vertical-flip") && transD.get(attrNameC).equalsIgnoreCase("flipped")){
						countAttrC++;
						matchattrMapC.put(attrNameC,"yes");
					} else if((attrNameC.equalsIgnoreCase("overlaps")|| attrNameC.equalsIgnoreCase("overlap")) && transD.get(attrNameC).equalsIgnoreCase("same")){
						countAttrC++;
						matchattrMapC.put(attrNameC,attrValueC);
					}
				} else {
					//C has a new attribute: add to to the list
					if(compObj.getFlip() == 1){
						if(attrValueC.equals("no"))
							matchattrMapC.put(attrNameC, "yes");
						else
							matchattrMapC.put(attrNameC, "no");
					} else {
						if (deletedAttrsMap.containsKey(objectNameC)){
							if (deletedAttrsMap.get(objectNameC).containsKey(attrNameC)){
								//DO Nothing
							}
						}
						else if (addedAttrsMap.containsKey(objectNameC)){
							if (addedAttrsMap.get(objectNameC).containsKey(attrNameC)){
								if(addedAttrsMap.get(objectNameC).get(attrNameC).equals(attrValueC))
									matchattrMapC.put(attrNameC, attrValueC);
							}
						}
					}
						 
				}
				}
			
			
				
			} else if(!deletedObjList.contains(objectNameC)){
					//it should not be present in the answer.
					deletedObjList.add(objectNameC);
			}
			
			if(matchattrMapC.size()>0)
				attrMapD.put(objectNameC, matchattrMapC);
		}
		if (countObjsC > countObjsMappedC && countObjsC > countObjsA){
			//identify the property in mapObj which is not same in Z object - angle
			String propertyFlag = "";
			for (Map.Entry<String,String> entry : mapObj.get("Z").entrySet()) {
				  String attrName = entry.getKey();
				  String trans = entry.getValue();
				  if(!trans.equals("same")){
					  propertyFlag = propertyFlag.concat(attrName);
					  break;
				  }
			}
			for (RavensObject attrObjC : listC){
	    		HashMap<String,String> matchattrMapC = new HashMap<String,String>();
	    		
	    		if(!attrMapD.containsKey(attrObjC.getName())){
	    			for(RavensAttribute attrC : attrObjC.getAttributes()){
	    				String attrNameC = attrC.getName();
	        			String attrValueC = attrC.getValue();
	        			if(attrNameC.equals(propertyFlag)){
	        				matchattrMapC.put(attrNameC, compObj.angleComparor2x2( attrValueC,  attrMapD.get("Z").get(attrNameC)));
	        			} else 
	        				matchattrMapC.put(attrNameC, attrValueC);
	        			
	    			}
	    			attrMapD.put(attrObjC.getName(), matchattrMapC);
	    		} 
	    		
			}
		}
    	}
		return attrMapD;
    	
	}
	
	/*
	 * ---------------------------------------------------------------------------------------------------------------------------------------
	 */
	
	
	/*
	 */
	//Create Attribute list for fictional solution D for 2x 2 problems
	//
	//
	public HashMap <Integer, HashMap<String, String>>  generateSolution2x2(
			HashMap <String, HashMap <Integer, HashMap<String, String>>> finalMap,
			HashMap <Integer, HashMap<String, String>> mapObjAB, 
			HashMap <Integer, HashMap<String, String>> mapObjAC, 
			HashMap <Integer, HashMap<String, String>> deletedAttrsMap,
			HashMap <Integer, HashMap<String, String>> addedAttrsMap, 
        	Comparer compObj,
        	List <Integer> deletedObjList, 
        	List <Integer> addedObjList ){
		
		HashMap <Integer, HashMap<String, String>> attrMapD = new HashMap<Integer, HashMap<String, String>>();
		
    	System.out.println("--------- Generator ----------");
    	
    	int countAttrA = 0;
		int countObjA = finalMap.get("A").size();
		Integer i;
		for (i = 1; i <= finalMap.get("A").size(); i++){
			HashMap<String,String> matchattrMapD = new HashMap<String,String>();
			int circleFlag = 0;
			int circleAngleFlag = 0;
			int squareFlag = 0;
			int squareAngleFlag = 0;
			int triangleFlag = 0;
			int triangleAngleFlag = 0;
			int righttriangleFlag = 0;
			int righttriangleAngleFlag = 0;
			for (Map.Entry <String,String> attrMapI : finalMap.get("A").get(i).entrySet()) {
			
				//check if it has circle and angle combination
				String attrNameA = attrMapI.getKey();
				String attrValueA = attrMapI.getValue();
				
				if(attrNameA.equals("shape") && attrValueA.equalsIgnoreCase("circle")){
					circleFlag = 1;
				} else if(attrNameA.equals("angle")){
					circleAngleFlag = 1;
				}			
				///square and 90,180,270,360 angle check
				if(attrNameA.equals("shape") && (attrValueA.equalsIgnoreCase("square")) ){
					squareFlag = 1;
				} else if(attrNameA.equals("angle") && 
						( attrValueA.equalsIgnoreCase("90") || attrValueA.equalsIgnoreCase("180") 
							|| attrValueA.equalsIgnoreCase("270") || attrValueA.equalsIgnoreCase("360"))){
					squareAngleFlag = 1;
				}				
				//triangle and 120,240,360 angle check
				if(attrNameA.equals("shape") && (attrValueA.equalsIgnoreCase("triangle")) ){
					triangleFlag = 1;
				} else if(attrNameA.equals("angle") && ( attrValueA.equalsIgnoreCase("120") || attrValueA.equalsIgnoreCase("240") || attrValueA.equalsIgnoreCase("360"))){
					triangleAngleFlag = 1;
				}
				//right - triangle and 0, 360 angle check
				if(attrNameA.equals("shape") && (attrValueA.equalsIgnoreCase("triangle")) ){
					righttriangleFlag = 1;
				} else if(attrNameA.equals("angle") && ( attrValueA.equalsIgnoreCase("0") || attrValueA.equalsIgnoreCase("360"))){
					righttriangleAngleFlag = 1;
				}
			}
			
			for (Map.Entry <String,String> attrMapI : finalMap.get("A").get(i).entrySet()) {
				//check if it has circle and angle combination
				String attrNameA = attrMapI.getKey();
				String attrValueA = attrMapI.getValue();
				
				compObj.setFlip(0);
		
				if(!mapObjAB.containsKey(i) || !mapObjAC.containsKey(i)){
					
						break;
					
				}
					
					if(attrNameA.equalsIgnoreCase("shape")){
						countObjA++;
						if(mapObjAB.containsKey(i) && mapObjAC.containsKey(i)){
						if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") && mapObjAC.get(i).get(attrNameA).endsWith("same"))
							matchattrMapD.put(attrNameA, attrValueA);
						else if ((mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("changed") && mapObjAC.get(i).get(attrNameA).endsWith("same")) ||
								 (mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("changed") && mapObjAB.get(i).get(attrNameA).endsWith("same"))){
							if(compObj.getShapeDir().containsKey(attrValueA)){
								if (compObj.getShapeDir().get(attrValueA).equals("circle")){
									circleFlag = 1;
									circleAngleFlag = 1;
								}
								matchattrMapD.put(attrNameA, compObj.getShapeDir().get(attrValueA));
							} else {
								double rel = 1.0;
								//get the relationship between the two and derive correct answer
								for (Map.Entry<String,String> entry : compObj.getShapeDir().entrySet()) {
									  String shapeorig = entry.getKey();
									  String shapefnl = entry.getValue();
									  if(compObj.getEdgesDir().containsKey(shapeorig) && compObj.getEdgesDir().containsKey(shapefnl)){
										  rel = (double)compObj.getEdgesDir().get(shapeorig)/(double)compObj.getEdgesDir().get(shapefnl);
										  break;
									  }
								}
								//derive correct shape
								if(compObj.getEdgesDir().containsKey(attrValueA)){
									int noOfEdgesD = (int) ((int)compObj.getEdgesDir().get(attrValueA)/rel);
									for (Map.Entry<String,Integer> entry : compObj.getEdgesDir().entrySet()) {
	  								  String shape = entry.getKey();
	  								  Integer shapeEdges = entry.getValue();
	  								  if(noOfEdgesD == shapeEdges){
	  									matchattrMapD.put(attrNameA, shape);
	  									break;
	  								  }
									}
								}
							}
						}
						} else if(mapObjAB.containsKey(i)){
							if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, attrValueA);
							else if ((mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("changed"))){
								if(compObj.getShapeDir().containsKey(attrValueA)){
									if (compObj.getShapeDir().get(attrValueA).equals("circle")){
										circleFlag = 1;
										circleAngleFlag = 1;
									}
									matchattrMapD.put(attrNameA, compObj.getShapeDir().get(attrValueA));
								}
							}
						} else if(mapObjAC.containsKey(i)){
							if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, attrValueA);
							else if ((mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("changed"))){
								if(compObj.getShapeDir().containsKey(attrValueA)){
									if (compObj.getShapeDir().get(attrValueA).equals("circle")){
										circleFlag = 1;
										circleAngleFlag = 1;
									}
									matchattrMapD.put(attrNameA, compObj.getShapeDir().get(attrValueA));
								}
							}
						}
					} else if(attrNameA.equalsIgnoreCase("fill") ){
						countAttrA++;
						if(mapObjAB.containsKey(i) && mapObjAC.containsKey(i)){
						if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same"))
							matchattrMapD.put(attrNameA, attrValueA);
						else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("filled") &&  mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
								mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("filled") &&  mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
							matchattrMapD.put(attrNameA, "yes");
						else if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("unfilled") &&  mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
								 mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("unfilled") &&  mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
	     					matchattrMapD.put(attrNameA, "no");
						else if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("filledno2lh") )
	     					matchattrMapD.put(attrNameA, "left-half");
						else if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("filledno2rh"))
	     					matchattrMapD.put(attrNameA, "right-half");
						else if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("filledno2th"))
	     					matchattrMapD.put(attrNameA, "top-half");
						else if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("filledno2bh"))
	     					matchattrMapD.put(attrNameA, "bottom-half");
						else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("fillArray")){
							//TODO: Add all cases
							for (String fillVal: attrValueA.split(",")){
				        		if (fillVal.equalsIgnoreCase("top-left")){
				        			compObj.fillArray[0] = compObj.fillArray[0] | 1;
				        		} else if (fillVal.equalsIgnoreCase("top-right")){
				        			compObj.fillArray[1] = compObj.fillArray[0] | 1;;
				        		} else if (fillVal.equalsIgnoreCase("bottom-left")){
				        			compObj.fillArray[2] = compObj.fillArray[0] | 1;;
				        		} else if (fillVal.equalsIgnoreCase("bottom-right")){
				        			compObj.fillArray[3] = compObj.fillArray[0] | 1;;
				        		}
				        	}
							String result = String.valueOf(compObj.fillArray[0])+ "," + 
									String.valueOf(compObj.fillArray[1])+ "," + 
									String.valueOf(compObj.fillArray[2])+ "," +
									String.valueOf(compObj.fillArray[3]);
							//Add output to map
							matchattrMapD.put(attrNameA, result);
							//reset array for get fill map for each choice 1-6
							for(int k= 0; k< 4; k++)
								compObj.fillArray[k] = 0;            					 
						}
					} else if(mapObjAB.containsKey(i)){
						if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
							matchattrMapD.put(attrNameA, attrValueA);
						else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("filled"))
							matchattrMapD.put(attrNameA, "yes");
						else if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("unfilled"))
	     					matchattrMapD.put(attrNameA, "no");
						else if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("filledno2lh") )
	     					matchattrMapD.put(attrNameA, "left-half");
						else if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("filledno2rh"))
	     					matchattrMapD.put(attrNameA, "right-half");
						else if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("filledno2th"))
	     					matchattrMapD.put(attrNameA, "top-half");
						else if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("filledno2bh"))
	     					matchattrMapD.put(attrNameA, "bottom-half");
						else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("fillArray")){
							//TODO: Add all cases
							for (String fillVal: attrValueA.split(",")){
				        		if (fillVal.equalsIgnoreCase("top-left")){
				        			compObj.fillArray[0] = compObj.fillArray[0] | 1;
				        		} else if (fillVal.equalsIgnoreCase("top-right")){
				        			compObj.fillArray[1] = compObj.fillArray[0] | 1;;
				        		} else if (fillVal.equalsIgnoreCase("bottom-left")){
				        			compObj.fillArray[2] = compObj.fillArray[0] | 1;;
				        		} else if (fillVal.equalsIgnoreCase("bottom-right")){
				        			compObj.fillArray[3] = compObj.fillArray[0] | 1;;
				        		}
				        	}
							String result = String.valueOf(compObj.fillArray[0])+ "," + 
									String.valueOf(compObj.fillArray[1])+ "," + 
									String.valueOf(compObj.fillArray[2])+ "," +
									String.valueOf(compObj.fillArray[3]);
							//Add output to map
							matchattrMapD.put(attrNameA, result);
							//reset array for get fill map for each choice 1-6
							for(int k= 0; k< 4; k++)
								compObj.fillArray[k] = 0;            					 
						}      				
					} else  if(mapObjAC.containsKey(i)){
					if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same"))
						matchattrMapD.put(attrNameA, attrValueA);
					else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("filled"))
						matchattrMapD.put(attrNameA, "yes");
					else if (mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("unfilled"))
     					matchattrMapD.put(attrNameA, "no");
					else if (mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("filledno2lh") )
     					matchattrMapD.put(attrNameA, "left-half");
					else if (mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("filledno2rh"))
     					matchattrMapD.put(attrNameA, "right-half");
					else if (mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("filledno2th"))
     					matchattrMapD.put(attrNameA, "top-half");
					else if (mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("filledno2bh"))
     					matchattrMapD.put(attrNameA, "bottom-half");
					else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("fillArray")){
						//TODO: Add all cases
						for (String fillVal: attrValueA.split(",")){
			        		if (fillVal.equalsIgnoreCase("top-left")){
			        			compObj.fillArray[0] = compObj.fillArray[0] | 1;
			        		} else if (fillVal.equalsIgnoreCase("top-right")){
			        			compObj.fillArray[1] = compObj.fillArray[0] | 1;;
			        		} else if (fillVal.equalsIgnoreCase("bottom-left")){
			        			compObj.fillArray[2] = compObj.fillArray[0] | 1;;
			        		} else if (fillVal.equalsIgnoreCase("bottom-right")){
			        			compObj.fillArray[3] = compObj.fillArray[0] | 1;;
			        		}
			        	}
						String result = String.valueOf(compObj.fillArray[0])+ "," + 
								String.valueOf(compObj.fillArray[1])+ "," + 
								String.valueOf(compObj.fillArray[2])+ "," +
								String.valueOf(compObj.fillArray[3]);
						//Add output to map
						matchattrMapD.put(attrNameA, result);
						//reset array for get fill map for each choice 1-6
						for(int k= 0; k< 4; k++)
							compObj.fillArray[k] = 0;            					 
					}
					}
					}
					else if(attrNameA.equalsIgnoreCase("size")){
						countAttrA++;
						if(mapObjAB.containsKey(i) && mapObjAC.containsKey(i)){
							
							if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same")){
								matchattrMapD.put(attrNameA, attrValueA);
							} else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlarged") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlarged") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") ){
								String val = "";
								if(attrValueA.equals("Xsmall"))
									val = "small";
								else if(attrValueA.equals("small"))
									val = "medium";
								else if(attrValueA.equals("medium"))
									val = "large";
								else if(attrValueA.equals("large"))
									val = "Xlarge";
								else if(attrValueA.equals("Xlarge"))
									val = "XXlarge";
									matchattrMapD.put(attrNameA, val);
								
							} else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reduced") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reduced") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") ){
								
								String val = "";
								if(attrValueA.equals("small"))
									val = "Xsmall";
								else if(attrValueA.equals("medium"))
									val = "small";
								else if(attrValueA.equals("large"))
									val = "medium";
								else if(attrValueA.equals("Xlarge"))
									val = "large";
								else if(attrValueA.equals("XXlarge"))
									val = "Xlarge";
									matchattrMapD.put(attrNameA, val);
							
							}
								
							
							
							/*if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, attrValueA);
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2m") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2m") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") )
								matchattrMapD.put(attrNameA, "medium");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedm2s") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedm2s") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, "small");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2l") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2l") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, "large");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedl2s") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedl2s") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, "small");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedm2l") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedm2l") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, "large");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedl2m") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedl2m") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, "medium");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedl2Xl") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedl2Xl") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") )
								matchattrMapD.put(attrNameA, "Xlarge");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2l") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2l") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, "large");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedm2Xl") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedm2Xl") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") )
								matchattrMapD.put(attrNameA, "Xlarge");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2m") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2m") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, "medium");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2Xl") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2Xl") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") )
								matchattrMapD.put(attrNameA, "Xlarge");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2s") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2s") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, "small");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXl2XXl") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedXl2XXl") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") )
								matchattrMapD.put(attrNameA, "XXlarge");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2Xl") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2Xl") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, "Xlarge");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedl2XXl") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedl2XXl") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") )
								matchattrMapD.put(attrNameA, "XXlarge");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2l") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2l") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, "large");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedm2XXl") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedm2XXl") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") )
								matchattrMapD.put(attrNameA, "XXlarge");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2m") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2m") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, "medium");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2XXl") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2XXl") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") )
								matchattrMapD.put(attrNameA, "XXlarge");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2s") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2s") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, "small");
							//new XSmall cases
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2s") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2s") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") )
								matchattrMapD.put(attrNameA, "small");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reduceds2Xs") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reduceds2Xs") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, "Xsmall");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2m") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2m") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") )
								matchattrMapD.put(attrNameA, "medium");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedm2Xs") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedm2Xs") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, "Xsmall");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2l") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2l") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") )
								matchattrMapD.put(attrNameA, "large");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedl2Xs") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedl2Xs") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, "Xsmall");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2Xl") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2Xl") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") )
								matchattrMapD.put(attrNameA, "Xlarge");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2Xs") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2Xs") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, "Xsmall");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2XXl") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2XXl") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") )
								matchattrMapD.put(attrNameA, "XXlarge");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2Xs") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
									mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2Xs") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, "Xsmall");
							*/
							
							
						} else if(mapObjAB.containsKey(i)) {
							
							if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, attrValueA);
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlarged")){
								String val = "";
								if(attrValueA.equals("Xsmall"))
									val = "small";
								else if(attrValueA.equals("small"))
									val = "medium";
								else if(attrValueA.equals("medium"))
									val = "large";
								else if(attrValueA.equals("large"))
									val = "Xlarge";
								else if(attrValueA.equals("Xlarge"))
									val = "XXlarge";
									matchattrMapD.put(attrNameA, val);
							} else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reduced")){
								String val = "";
								if(attrValueA.equals("small"))
									val = "Xsmall";
								else if(attrValueA.equals("medium"))
									val = "small";
								else if(attrValueA.equals("large"))
									val = "medium";
								else if(attrValueA.equals("Xlarge"))
									val = "large";
								else if(attrValueA.equals("XXlarge"))
									val = "Xlarge";
									matchattrMapD.put(attrNameA, val);
							}
							
							/*if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, attrValueA);
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2m"))
								matchattrMapD.put(attrNameA, "medium");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedm2s"))
								matchattrMapD.put(attrNameA, "small");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2l"))
								matchattrMapD.put(attrNameA, "large");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedl2s"))
								matchattrMapD.put(attrNameA, "small");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedm2l"))
								matchattrMapD.put(attrNameA, "large");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedl2m"))
								matchattrMapD.put(attrNameA, "medium");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedl2Xl"))
								matchattrMapD.put(attrNameA, "Xlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2l"))
								matchattrMapD.put(attrNameA, "large");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedm2Xl"))
								matchattrMapD.put(attrNameA, "Xlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2m"))
								matchattrMapD.put(attrNameA, "medium");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2Xl"))
								matchattrMapD.put(attrNameA, "Xlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2s"))
								matchattrMapD.put(attrNameA, "small");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXl2XXl"))
								matchattrMapD.put(attrNameA, "XXlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2Xl"))
								matchattrMapD.put(attrNameA, "Xlarge");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedl2XXl"))
								matchattrMapD.put(attrNameA, "XXlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2l"))
								matchattrMapD.put(attrNameA, "large");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedm2XXl"))
								matchattrMapD.put(attrNameA, "XXlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2m"))
								matchattrMapD.put(attrNameA, "medium");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2XXl"))
								matchattrMapD.put(attrNameA, "XXlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2s"))
								matchattrMapD.put(attrNameA, "small");
							//new Xsmall case
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2XXl"))
								matchattrMapD.put(attrNameA, "XXlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2Xs"))
								matchattrMapD.put(attrNameA, "Xsmall");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2Xl"))
								matchattrMapD.put(attrNameA, "Xlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2Xs"))
								matchattrMapD.put(attrNameA, "Xsmall");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2l"))
								matchattrMapD.put(attrNameA, "large");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedl2Xs"))
								matchattrMapD.put(attrNameA, "Xsmall");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2m"))
								matchattrMapD.put(attrNameA, "medium");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedm2Xs"))
								matchattrMapD.put(attrNameA, "Xsmall");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2s"))
								matchattrMapD.put(attrNameA, "small");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reduceds2Xs"))
								matchattrMapD.put(attrNameA, "Xsmall");
								*/
							
							
							
							
						} else if(mapObjAC.containsKey(i)) {
							

							if (mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, attrValueA);
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlarged")){
								String val = "";
								if(attrValueA.equals("Xsmall"))
									val = "small";
								else if(attrValueA.equals("small"))
									val = "medium";
								else if(attrValueA.equals("medium"))
									val = "large";
								else if(attrValueA.equals("large"))
									val = "Xlarge";
								else if(attrValueA.equals("Xlarge"))
									val = "XXlarge";
									matchattrMapD.put(attrNameA, val);
							} else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reduced")){
								String val = "";
								if(attrValueA.equals("small"))
									val = "Xsmall";
								else if(attrValueA.equals("medium"))
									val = "small";
								else if(attrValueA.equals("large"))
									val = "medium";
								else if(attrValueA.equals("Xlarge"))
									val = "large";
								else if(attrValueA.equals("XXlarge"))
									val = "Xlarge";
									matchattrMapD.put(attrNameA, val);
							}
							/*if (mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, attrValueA);
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2m"))
								matchattrMapD.put(attrNameA, "medium");
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedm2s"))
								matchattrMapD.put(attrNameA, "small");
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2l"))
								matchattrMapD.put(attrNameA, "large");
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedl2s"))
								matchattrMapD.put(attrNameA, "small");
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedm2l"))
								matchattrMapD.put(attrNameA, "large");
							else if( mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedl2m"))
								matchattrMapD.put(attrNameA, "medium");
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedl2Xl"))
								matchattrMapD.put(attrNameA, "Xlarge");
							else if( mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2l"))
								matchattrMapD.put(attrNameA, "large");
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedm2Xl"))
								matchattrMapD.put(attrNameA, "Xlarge");
							else if( mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2m"))
								matchattrMapD.put(attrNameA, "medium");
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2Xl"))
								matchattrMapD.put(attrNameA, "Xlarge");
							else if( mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2s"))
								matchattrMapD.put(attrNameA, "small");
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedXl2XXl"))
								matchattrMapD.put(attrNameA, "XXlarge");
							else if( mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2Xl"))
								matchattrMapD.put(attrNameA, "Xlarge");
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedl2XXl"))
								matchattrMapD.put(attrNameA, "XXlarge");
							else if( mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2l"))
								matchattrMapD.put(attrNameA, "large");
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedm2XXl"))
								matchattrMapD.put(attrNameA, "XXlarge");
							else if( mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2m"))
								matchattrMapD.put(attrNameA, "medium");
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2XXl"))
								matchattrMapD.put(attrNameA, "XXlarge");
							else if( mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2s"))
								matchattrMapD.put(attrNameA, "small");
							//new Xsmall case
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2XXl"))
								matchattrMapD.put(attrNameA, "XXlarge");
							else if( mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2Xs"))
								matchattrMapD.put(attrNameA, "Xsmall");
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2Xl"))
								matchattrMapD.put(attrNameA, "Xlarge");
							else if( mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2Xs"))
								matchattrMapD.put(attrNameA, "Xsmall");
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2l"))
								matchattrMapD.put(attrNameA, "large");
							else if( mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedl2Xs"))
								matchattrMapD.put(attrNameA, "Xsmall");
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2m"))
								matchattrMapD.put(attrNameA, "medium");
							else if( mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reducedm2Xs"))
								matchattrMapD.put(attrNameA, "Xsmall");
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2s"))
								matchattrMapD.put(attrNameA, "small");
							else if( mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reduceds2Xs"))
								matchattrMapD.put(attrNameA, "Xsmall");
							*/
							
						}
					} 
					else if(attrNameA.equalsIgnoreCase("angle") && ( (circleFlag==1) && (circleAngleFlag==1) )){
						countAttrA++;
						//do not put angle as key comparison factor in c
						matchattrMapD.put(attrNameA, "0");
					}
					else if(attrNameA.equalsIgnoreCase("angle")){ 
						countAttrA++;
						if(mapObjAB.containsKey(i) && mapObjAC.containsKey(i)){
							
						if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same"))
							matchattrMapD.put(attrNameA, attrValueA);
						else if(mapObjAB.get(i).get(attrNameA).equals("same") && !mapObjAC.get(i).get(attrNameA).equals("same"))
							matchattrMapD.put(attrNameA, mapObjAC.get(i).get(attrNameA));
						else if(mapObjAC.get(i).get(attrNameA).equals("same") && !mapObjAB.get(i).get(attrNameA).equals("same"))
							matchattrMapD.put(attrNameA, mapObjAB.get(i).get(attrNameA));
						else if (mapObjAB.size() == 1 && (Integer.parseInt(mapObjAB.get(i).get(attrNameA)) - Integer.parseInt(mapObjAC.get(i).get(attrNameA)) == 180 ||
								Integer.parseInt(mapObjAB.get(i).get(attrNameA)) - Integer.parseInt(mapObjAC.get(i).get(attrNameA)) == -180)) {
							//its a diagonal flip
							if(Integer.parseInt(attrValueA) >= 180){
								matchattrMapD.put(attrNameA, String.valueOf(Integer.parseInt(attrValueA) - 180));
							}  else if (Integer.parseInt(attrValueA) < 180){
								matchattrMapD.put(attrNameA, String.valueOf(Integer.parseInt(attrValueA) + 180));
							}
						} 
						else {
							if((squareFlag==1) ){
								int res = Integer.parseInt(mapObjAB.get(i).get(attrNameA)) + Integer.parseInt(mapObjAC.get(i).get(attrNameA));
								res = res - Integer.parseInt(attrValueA);
								if(res == 90 || res == 180 || res == 270 || res == 360)
									matchattrMapD.put(attrNameA, attrValueA);
							} else if(( (triangleFlag==1) && (triangleAngleFlag==1) )){
								matchattrMapD.put(attrNameA, attrValueA);
							} else {
							int res = Integer.parseInt(attrValueA) + Integer.parseInt(mapObjAB.get(i).get(attrNameA)) + Integer.parseInt(mapObjAC.get(i).get(attrNameA));
							if (res >= 360) 
								matchattrMapD.put(attrNameA, String.valueOf(res-360));
							else
								matchattrMapD.put(attrNameA, String.valueOf(res));
							}
						}
						}
					} 
					else if(attrNameA.equalsIgnoreCase("above")){
						countAttrA++;
						if(mapObjAB.containsKey(i) && mapObjAC.containsKey(i)){
							if( mapObjAB.get(i).containsKey(attrNameA) && mapObjAC.get(i).containsKey(attrNameA)){
							if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same")){
								matchattrMapD.put(attrNameA, attrValueA);
							} else if((mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("notabove") || mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("notabove"))){
								countAttrA++;
							}
							}
							
						} else if(mapObjAB.containsKey(i)){
							if(mapObjAB.get(i).containsKey(attrNameA)){
							if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same")){
								matchattrMapD.put(attrNameA, attrValueA);
							} else if((mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("notabove"))){
								countAttrA++;
							}
							}
							
						} else if(mapObjAC.containsKey(i)){
							if(mapObjAC.get(i).containsKey(attrNameA)){
							if( mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same")){
								matchattrMapD.put(attrNameA, attrValueA);
							} else if((mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("notabove"))){
								countAttrA++;
							}
							}
						}
					}
						
					else if(attrNameA.equalsIgnoreCase("inside")){ 
						countAttrA++;
						if(mapObjAB.containsKey(i) && mapObjAC.containsKey(i)){
							if(mapObjAB.get(i).containsKey(attrNameA) && mapObjAC.get(i).containsKey(attrNameA)){
								if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same"))
									matchattrMapD.put(attrNameA,attrValueA );
								else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("deleted")){
									//it should not be present in the list
									//deletedAttrList.put(objectNameC, attrNameA);
								}
							}
						} else if(mapObjAB.containsKey(i)){
							if(mapObjAB.get(i).containsKey(attrNameA)){
								if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
									matchattrMapD.put(attrNameA,attrValueA );
							}
						} else if(mapObjAC.containsKey(i)){
							if(mapObjAB.get(i).containsKey(attrNameA)){
								if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same"))
									matchattrMapD.put(attrNameA,attrValueA );							
							}
						}
					} 
					
					else if(attrNameA.equalsIgnoreCase("left-of") ){
						countAttrA++;
						if(mapObjAB.containsKey(i) && mapObjAC.containsKey(i)){
							if(mapObjAB.get(i).containsKey(attrNameA) && mapObjAC.get(i).containsKey(attrNameA)){
								if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same")){
									matchattrMapD.put(attrNameA,attrValueA );
								} else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("changed") && mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ||
										mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("changed") && mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same")){
									if(compObj.getPositionDir().containsKey(attrValueA))
										matchattrMapD.put(attrNameA, compObj.getPositionDir().get(attrValueA));
								}
							} else if(mapObjAB.get(i).containsKey(attrNameA)){
								if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same")) {
									if(deletedAttrsMap.containsKey(i)){
										if(deletedAttrsMap.get(i).containsKey(attrValueA)){
											//DO not add the attribute to matchattrMapD : Delete them
										}
									} else if(addedAttrsMap.containsKey(i)){
										if(addedAttrsMap.get(i).containsKey(attrValueA)){
											//Add the attribute to matchattrMapD : Add them
											matchattrMapD.put(attrNameA,addedAttrsMap.get(i).get(attrNameA) );
										}
									}
									else
										matchattrMapD.put(attrNameA,attrValueA );
								} else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("changed")){
									if(compObj.getPositionDir().containsKey(attrValueA))
										matchattrMapD.put(attrNameA, compObj.getPositionDir().get(attrValueA));
								}
							} else if(mapObjAC.get(i).containsKey(attrNameA)){
								if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same")) {
									if(deletedAttrsMap.containsKey(i)){
										if(deletedAttrsMap.get(i).containsKey(attrValueA)){
											//DO not add the attribute to matchattrMapD : Delete them
										}
									} else if(addedAttrsMap.containsKey(i)){
										if(addedAttrsMap.get(i).containsKey(attrValueA)){
											//Add the attribute to matchattrMapD : Add them
											matchattrMapD.put(attrNameA,addedAttrsMap.get(i).get(attrNameA) );
										}
									}
									else
										matchattrMapD.put(attrNameA,attrValueA );
								} else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("changed")){
									if(compObj.getPositionDir().containsKey(attrValueA))
										matchattrMapD.put(attrNameA, compObj.getPositionDir().get(attrValueA));
								}
							}
						} else if(mapObjAB.containsKey(i)){
							if(mapObjAB.get(i).containsKey(attrNameA)){
							if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") ){
								matchattrMapD.put(attrNameA,attrValueA );
							}
							}
						} else if(mapObjAC.containsKey(i)){
							if(mapObjAC.get(i).containsKey(attrNameA)){
							if( mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same")){
								matchattrMapD.put(attrNameA,attrValueA );
							}
							}
						}
						
						
					} else if(attrNameA.equalsIgnoreCase("vertical-flip")){
						if(mapObjAB.get(i).containsKey(attrNameA) && mapObjAC.get(i).containsKey(attrNameA)){
							if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("flipped") || mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("flipped")){
								countAttrA++;
								matchattrMapD.put(attrNameA,"yes");
							}
						}
					} else if(attrNameA.equalsIgnoreCase("overlaps")) {
							if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") || mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same")){
								countAttrA++;
								matchattrMapD.put(attrNameA,attrValueA);
							}
					}
		}
			

		if(!deletedObjList.contains(i)){
			if (addedAttrsMap.containsKey(i)){
				for(Map.Entry<String, String> addedAttrs : addedAttrsMap.get(i).entrySet()){
						if (!matchattrMapD.containsKey(addedAttrs.getKey())){
							matchattrMapD.put(addedAttrs.getKey(), addedAttrs.getValue());
						}
				}
			} 
			attrMapD.put(i, matchattrMapD);
		}
		
		}
		
		for (int j = 0; j < addedObjList.size(); j ++){
			if(!attrMapD.containsKey(addedObjList.get(j))){
				if (finalMap.get("B").containsKey(addedObjList.get(j))){
					//attrMapD.put(addedObjList.get(j), finalMap.get("B").get(addedObjList.get(j)));
					if(finalMap.get("B").get(addedObjList.get(j)).containsKey("shape")){
						if(finalMap.get("B").size()> 1){
							if(finalMap.get("B").containsKey(1)){
							if(finalMap.get("B").get(addedObjList.get(j)).get("shape").equals(finalMap.get("B").get(1).get("shape"))){
							//if shapes are same, transformation is applicable on it
								if(compObj.getShapeDir().containsKey(finalMap.get("B").get(addedObjList.get(j)).get("shape"))){
									
									HashMap<String, String> matchattrMapD = new HashMap<String, String> ();
									matchattrMapD.put("shape", compObj.getShapeDir().get(finalMap.get("B").get(addedObjList.get(j)).get("shape")));
									
									for(Map.Entry<String, String> attr : finalMap.get("B").get(addedObjList.get(j)).entrySet()){
										if(!attr.getKey().equals("shape")){
											matchattrMapD.put(attr.getKey(), attr.getValue());
										}
									}
									attrMapD.put(addedObjList.get(j), matchattrMapD);
								} else {
									//something else changes: angle??
									if(finalMap.get("B").get(addedObjList.get(j)).get("angle").equals(finalMap.get("B").get(1).get("angle"))){
										//change angle asper transformation
										HashMap<String, String> matchattrMapD = new HashMap<String, String> ();
										matchattrMapD.put("angle", (finalMap.get("C").get(1).get("angle")));
										
										for(Map.Entry<String, String> attr : finalMap.get("B").get(addedObjList.get(j)).entrySet()){
											if(!attr.getKey().equals("angle") && !attr.getKey().equals("left-of") && !attr.getKey().equals("above")){
												matchattrMapD.put(attr.getKey(), attr.getValue());
											}
										}
										attrMapD.put(addedObjList.get(j), matchattrMapD);
									}
								}
							} else {
								attrMapD.put(addedObjList.get(j), finalMap.get("B").get(addedObjList.get(j)));
							} 
							
							}
						} else 
							attrMapD.put(addedObjList.get(j), finalMap.get("B").get(addedObjList.get(j)));
					}
				} else if (finalMap.get("C").containsKey(addedObjList.get(j))){
					if(finalMap.get("C").get(addedObjList.get(j)).containsKey("shape")){
						if(finalMap.get("C").size()> 1){
							if(finalMap.get("C").containsKey(1)){
							if(finalMap.get("C").get(addedObjList.get(j)).get("shape").equals(finalMap.get("C").get(1).get("shape"))){
							//if shapes are same, transformation is applicable on it
								if(compObj.getShapeDir().containsKey(finalMap.get("C").get(addedObjList.get(j)).get("shape"))){
									
									HashMap<String, String> matchattrMapD = new HashMap<String, String> ();
									matchattrMapD.put("shape", compObj.getShapeDir().get(finalMap.get("C").get(addedObjList.get(j)).get("shape")));
									
									for(Map.Entry<String, String> attr : finalMap.get("C").get(addedObjList.get(j)).entrySet()){
										if(!attr.getKey().equals("shape")){
											matchattrMapD.put(attr.getKey(), attr.getValue());
										}
									}
									attrMapD.put(addedObjList.get(j), matchattrMapD);
								} else {
									//something else changes: angle??
									if(finalMap.get("C").get(addedObjList.get(j)).get("angle").equals(finalMap.get("C").get(1).get("angle"))){
										//change angle asper transformation
										HashMap<String, String> matchattrMapD = new HashMap<String, String> ();
										matchattrMapD.put("angle", (finalMap.get("B").get(1).get("angle")));
										
										for(Map.Entry<String, String> attr : finalMap.get("C").get(addedObjList.get(j)).entrySet()){
											if(!attr.getKey().equals("angle") && !attr.getKey().equals("left-of") && !attr.getKey().equals("above")){
												matchattrMapD.put(attr.getKey(), attr.getValue());
											}
										}
										attrMapD.put(addedObjList.get(j), matchattrMapD);
									}
										
								}
							} else {
								attrMapD.put(addedObjList.get(j), finalMap.get("C").get(addedObjList.get(j)));
							}
							}
						} else 
							attrMapD.put(addedObjList.get(j), finalMap.get("C").get(addedObjList.get(j)));
					}
				}
			}
		}
    	
		return attrMapD;
    	
	}
	
	
	
	/*
	 * -------------------------------------------------------------------------------------------------------------------------------------------------------
	 */
	
	/*
	 */
	//Create Attribute list for fictional solution D for 2x 2 problems
	//
	//
	public HashMap <Integer, HashMap<String, String>>  generateSolution2x1_custom(
			HashMap <String, HashMap <Integer, HashMap<String, String>>> finalMap,
			HashMap <Integer, HashMap<String, String>> mapObjAB,
			HashMap <Integer, HashMap<String, String>> deletedAttrsMap,
			HashMap <Integer, HashMap<String, String>> addedAttrsMap, 
        	Comparer compObj,
        	List <Integer> deletedObjList, 
        	List <Integer> addedObjList ){
		
		HashMap <Integer, HashMap<String, String>> attrMapD = new HashMap<Integer, HashMap<String, String>>();
		
    	System.out.println("--------- Generator ----------");
    	
    	int countAttrA = 0;
		int countObjA = finalMap.get("A").size();
		Integer i;
		for (i = 1; i <= finalMap.get("A").size(); i++){
			HashMap<String,String> matchattrMapD = new HashMap<String,String>();
			int circleFlag = 0;
			int circleAngleFlag = 0;
			int squareFlag = 0;
			int squareAngleFlag = 0;
			int triangleFlag = 0;
			int triangleAngleFlag = 0;
			int righttriangleFlag = 0;
			int righttriangleAngleFlag = 0;
			for (Map.Entry <String,String> attrMapI : finalMap.get("A").get(i).entrySet()) {
			
				//check if it has circle and angle combination
				String attrNameA = attrMapI.getKey();
				String attrValueA = attrMapI.getValue();
				
				if(attrNameA.equals("shape") && attrValueA.equalsIgnoreCase("circle")){
					circleFlag = 1;
				} else if(attrNameA.equals("angle")){
					circleAngleFlag = 1;
				}			
				///square and 90,180,270,360 angle check
				if(attrNameA.equals("shape") && (attrValueA.equalsIgnoreCase("square")) ){
					squareFlag = 1;
				} else if(attrNameA.equals("angle") && 
						( attrValueA.equalsIgnoreCase("90") || attrValueA.equalsIgnoreCase("180") 
							|| attrValueA.equalsIgnoreCase("270") || attrValueA.equalsIgnoreCase("360"))){
					squareAngleFlag = 1;
				}				
				//triangle and 120,240,360 angle check
				if(attrNameA.equals("shape") && (attrValueA.equalsIgnoreCase("triangle")) ){
					triangleFlag = 1;
				} else if(attrNameA.equals("angle") && ( attrValueA.equalsIgnoreCase("120") || attrValueA.equalsIgnoreCase("240") || attrValueA.equalsIgnoreCase("360"))){
					triangleAngleFlag = 1;
				}
				//right - triangle and 0, 360 angle check
				if(attrNameA.equals("shape") && (attrValueA.equalsIgnoreCase("triangle")) ){
					righttriangleFlag = 1;
				} else if(attrNameA.equals("angle") && ( attrValueA.equalsIgnoreCase("0") || attrValueA.equalsIgnoreCase("360"))){
					righttriangleAngleFlag = 1;
				}
			}
			if(finalMap.get("C").containsKey(i)){
			  for (Map.Entry <String,String> attrMapI : finalMap.get("C").get(i).entrySet()) {
				//check if it has circle and angle combination
				String attrNameA = attrMapI.getKey();
				String attrValueA = attrMapI.getValue();
				
				compObj.setFlip(0);
		
				if(!mapObjAB.containsKey(i)){
					
						break;
					
				}
					
					if(attrNameA.equalsIgnoreCase("shape")){
						countObjA++;
						if(mapObjAB.containsKey(i)){
						  if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
							matchattrMapD.put(attrNameA, attrValueA);
						  else if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("changed")){
							if(compObj.getShapeDir().containsKey(attrValueA)){
								if (compObj.getShapeDir().get(attrValueA).equals("circle")){
									circleFlag = 1;
									circleAngleFlag = 1;
								}
								matchattrMapD.put(attrNameA, compObj.getShapeDir().get(attrValueA));
							} else {
								double rel = 1.0;
								//get the relationship between the two and derive correct answer
								for (Map.Entry<String,String> entry : compObj.getShapeDir().entrySet()) {
									  String shapeorig = entry.getKey();
									  String shapefnl = entry.getValue();
									  if(compObj.getEdgesDir().containsKey(shapeorig) && compObj.getEdgesDir().containsKey(shapefnl)){
										  rel = (double)compObj.getEdgesDir().get(shapeorig)/(double)compObj.getEdgesDir().get(shapefnl);
										  break;
									  }
								}
								//derive correct shape
								if(compObj.getEdgesDir().containsKey(attrValueA)){
									int noOfEdgesD = (int) ((int)compObj.getEdgesDir().get(attrValueA)/rel);
									for (Map.Entry<String,Integer> entry : compObj.getEdgesDir().entrySet()) {
	  								  String shape = entry.getKey();
	  								  Integer shapeEdges = entry.getValue();
	  								  if(noOfEdgesD == shapeEdges){
	  									matchattrMapD.put(attrNameA, shape);
	  									break;
	  								  }
									}
								}
							}
						  }
						} 
					} else if(attrNameA.equalsIgnoreCase("fill") ){
						countAttrA++;
						if(mapObjAB.containsKey(i)){
						if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
							matchattrMapD.put(attrNameA, attrValueA);
						else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("filled"))
							matchattrMapD.put(attrNameA, "yes");
						else if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("unfilled"))
	     					matchattrMapD.put(attrNameA, "no");
						else if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("filledno2lh") )
	     					matchattrMapD.put(attrNameA, "left-half");
						else if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("filledno2rh"))
	     					matchattrMapD.put(attrNameA, "right-half");
						else if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("filledno2th"))
	     					matchattrMapD.put(attrNameA, "top-half");
						else if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("filledno2bh"))
	     					matchattrMapD.put(attrNameA, "bottom-half");
						else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("fillArray")){
							//TODO: Add all cases
							for (String fillVal: attrValueA.split(",")){
				        		if (fillVal.equalsIgnoreCase("top-left")){
				        			compObj.fillArray[0] = compObj.fillArray[0] | 1;
				        		} else if (fillVal.equalsIgnoreCase("top-right")){
				        			compObj.fillArray[1] = compObj.fillArray[0] | 1;;
				        		} else if (fillVal.equalsIgnoreCase("bottom-left")){
				        			compObj.fillArray[2] = compObj.fillArray[0] | 1;;
				        		} else if (fillVal.equalsIgnoreCase("bottom-right")){
				        			compObj.fillArray[3] = compObj.fillArray[0] | 1;;
				        		}
				        	}
							String result = String.valueOf(compObj.fillArray[0])+ "," + 
									String.valueOf(compObj.fillArray[1])+ "," + 
									String.valueOf(compObj.fillArray[2])+ "," +
									String.valueOf(compObj.fillArray[3]);
							//Add output to map
							matchattrMapD.put(attrNameA, result);
							//reset array for get fill map for each choice 1-6
							for(int k= 0; k< 4; k++)
								compObj.fillArray[k] = 0;            					 
						}
					} 
					}
					else if(attrNameA.equalsIgnoreCase("size")){
						countAttrA++;
						if(mapObjAB.containsKey(i)) {		
							
							if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, attrValueA);
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlarged")){
								String val = "";
								if(attrValueA.equals("Xsmall"))
									val = "small";
								else if(attrValueA.equals("small"))
									val = "medium";
								else if(attrValueA.equals("medium"))
									val = "large";
								else if(attrValueA.equals("large"))
									val = "Xlarge";
								else if(attrValueA.equals("Xlarge"))
									val = "XXlarge";
									matchattrMapD.put(attrNameA, val);
							} else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reduced")){
								String val = "";
								if(attrValueA.equals("small"))
									val = "Xsmall";
								else if(attrValueA.equals("medium"))
									val = "small";
								else if(attrValueA.equals("large"))
									val = "medium";
								else if(attrValueA.equals("Xlarge"))
									val = "large";
								else if(attrValueA.equals("XXlarge"))
									val = "Xlarge";
									matchattrMapD.put(attrNameA, val);
							}

							/*if (mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, attrValueA);
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2m"))
								matchattrMapD.put(attrNameA, "medium");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedm2s"))
								matchattrMapD.put(attrNameA, "small");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2l"))
								matchattrMapD.put(attrNameA, "large");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedl2s"))
								matchattrMapD.put(attrNameA, "small");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedm2l"))
								matchattrMapD.put(attrNameA, "large");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedl2m"))
								matchattrMapD.put(attrNameA, "medium");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedl2Xl"))
								matchattrMapD.put(attrNameA, "Xlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2l"))
								matchattrMapD.put(attrNameA, "large");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedm2Xl"))
								matchattrMapD.put(attrNameA, "Xlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2m"))
								matchattrMapD.put(attrNameA, "medium");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2Xl"))
								matchattrMapD.put(attrNameA, "Xlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2s"))
								matchattrMapD.put(attrNameA, "small");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXl2XXl"))
								matchattrMapD.put(attrNameA, "XXlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2Xl"))
								matchattrMapD.put(attrNameA, "Xlarge");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedl2XXl"))
								matchattrMapD.put(attrNameA, "XXlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2l"))
								matchattrMapD.put(attrNameA, "large");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedm2XXl"))
								matchattrMapD.put(attrNameA, "XXlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2m"))
								matchattrMapD.put(attrNameA, "medium");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargeds2XXl"))
								matchattrMapD.put(attrNameA, "XXlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2s"))
								matchattrMapD.put(attrNameA, "small");
							//new Xsmall case
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2XXl"))
								matchattrMapD.put(attrNameA, "XXlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXXl2Xs"))
								matchattrMapD.put(attrNameA, "Xsmall");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2Xl"))
								matchattrMapD.put(attrNameA, "Xlarge");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedXl2Xs"))
								matchattrMapD.put(attrNameA, "Xsmall");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2l"))
								matchattrMapD.put(attrNameA, "large");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedl2Xs"))
								matchattrMapD.put(attrNameA, "Xsmall");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2m"))
								matchattrMapD.put(attrNameA, "medium");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reducedm2Xs"))
								matchattrMapD.put(attrNameA, "Xsmall");
							else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("enlargedXs2s"))
								matchattrMapD.put(attrNameA, "small");
							else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("reduceds2Xs"))
								matchattrMapD.put(attrNameA, "Xsmall");
								*/
							
						}
					} 
					else if(attrNameA.equalsIgnoreCase("angle") && ( (circleFlag==1) && (circleAngleFlag==1) )){
						countAttrA++;
						//do not put angle as key comparison factor in c
						matchattrMapD.put(attrNameA, "0");
					}
					else if(attrNameA.equalsIgnoreCase("angle")){ 
						countAttrA++;
					  if(mapObjAB.containsKey(i)){
						if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
							matchattrMapD.put(attrNameA, attrValueA);
						else if(triangleFlag == 1 && mapObjAB.containsKey("vertical-flip")){
							matchattrMapD.put(attrNameA,attrValueA );
						}
						else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("rotated45clockwise") || mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("rotated45anticlockwise"))
							matchattrMapD.put(attrNameA, "45");
						else if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("rotated180clockwise")){
							
							if((Double.parseDouble(attrValueA) + 180) < 360)
								matchattrMapD.put(attrNameA, String.valueOf((int)Double.parseDouble(attrValueA) + 180));
							else if((Double.parseDouble(attrValueA) + 180) > 360)
								matchattrMapD.put(attrNameA, String.valueOf((int)Double.parseDouble(attrValueA) - 180));
							else if((Double.parseDouble(attrValueA) + 180) == 360)
								matchattrMapD.put(attrNameA, "0");
							compObj.setFlip(1);
						}	
						else if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("rotated180anticlockwise"))
							matchattrMapD.put(attrNameA, String.valueOf((int)Double.parseDouble(attrValueA) - 180));
						
						else {
							if(( (squareFlag==1) && (squareAngleFlag==1) )){
								matchattrMapD.put(attrNameA, attrValueA);
							} else if(( (triangleFlag==1) && (triangleAngleFlag==1) )){
								matchattrMapD.put(attrNameA, attrValueA);
							} 
							int res =(int) (Double.parseDouble(attrValueA) + Double.parseDouble(mapObjAB.get(i).get(attrNameA)));
							matchattrMapD.put(attrNameA, String.valueOf(res));
						}
					  }
					}
					else if(attrNameA.equalsIgnoreCase("above")){
						countAttrA++;
						if(mapObjAB.containsKey(i)){
							if(mapObjAB.get(i).containsKey(attrNameA)){
							if( mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same")){
								matchattrMapD.put(attrNameA, attrValueA);
							} else if((mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("notabove"))){
								countAttrA++;
							}
							}
							
						}
					}
						
					else if(attrNameA.equalsIgnoreCase("inside")){ 
						countAttrA++;
						if(mapObjAB.containsKey(i)){
							if(mapObjAB.get(i).containsKey(attrNameA)){
								if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same"))
									matchattrMapD.put(attrNameA,attrValueA );
							}
						}
					} 
					
					else if(attrNameA.equalsIgnoreCase("left-of") ){
						countAttrA++;
						if(mapObjAB.containsKey(i)){
							if(mapObjAB.get(i).containsKey(attrNameA)){
								if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same") ){
									matchattrMapD.put(attrNameA,attrValueA );
								}
							}
						} 						
						
					} else if(attrNameA.equalsIgnoreCase("vertical-flip")){
						if(mapObjAB.get(i).containsKey(attrNameA)){
							if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("flipped") ){
								countAttrA++;
								matchattrMapD.put(attrNameA,"yes");
							}
						}
					} else if(attrNameA.equalsIgnoreCase("overlaps")) { 
						if(mapObjAB.get(i).containsKey(attrNameA)){
							if(mapObjAB.get(i).get(attrNameA).equalsIgnoreCase("same")){
								countAttrA++;
								matchattrMapD.put(attrNameA,attrValueA);
							}
						}
					}
		  }
		}
			

		if(!deletedObjList.contains(i)){
			if (addedAttrsMap.containsKey(i)){
				for(Map.Entry<String, String> addedAttrs : addedAttrsMap.get(i).entrySet()){
						if (!matchattrMapD.containsKey(addedAttrs.getKey())){
							matchattrMapD.put(addedAttrs.getKey(), addedAttrs.getValue());
						}
				}
			} 
			attrMapD.put(i, matchattrMapD);
		}
		
		}
		
		for (int j = 0; j < addedObjList.size(); j ++){
			if(!attrMapD.containsKey(addedObjList.get(j))){
				if (finalMap.get("B").containsKey(addedObjList.get(j))){
					//attrMapD.put(addedObjList.get(j), finalMap.get("B").get(addedObjList.get(j)));
					if(finalMap.get("B").get(addedObjList.get(j)).containsKey("shape")){
						if(finalMap.get("B").size()> 1){
							if(finalMap.get("B").containsKey(1)){
							if(finalMap.get("B").get(addedObjList.get(j)).get("shape").equals(finalMap.get("B").get(1).get("shape"))){
							//if shapes are same, transformation is applicable on it
								if(compObj.getShapeDir().containsKey(finalMap.get("B").get(addedObjList.get(j)).get("shape"))){
									
									HashMap<String, String> matchattrMapD = new HashMap<String, String> ();
									matchattrMapD.put("shape", compObj.getShapeDir().get(finalMap.get("B").get(addedObjList.get(j)).get("shape")));
									
									for(Map.Entry<String, String> attr : finalMap.get("B").get(addedObjList.get(j)).entrySet()){
										if(!attr.getKey().equals("shape")){
											matchattrMapD.put(attr.getKey(), attr.getValue());
										}
									}
									attrMapD.put(addedObjList.get(j), matchattrMapD);
								} else {
									//something else changes: angle??
									if(finalMap.get("B").get(addedObjList.get(j)).get("angle").equals(finalMap.get("B").get(1).get("angle"))){
										//change angle asper transformation
										HashMap<String, String> matchattrMapD = new HashMap<String, String> ();
										matchattrMapD.put("angle", (finalMap.get("C").get(1).get("angle")));
										
										for(Map.Entry<String, String> attr : finalMap.get("B").get(addedObjList.get(j)).entrySet()){
											if(!attr.getKey().equals("angle") && !attr.getKey().equals("left-of") && !attr.getKey().equals("above")){
												matchattrMapD.put(attr.getKey(), attr.getValue());
											}
										}
										attrMapD.put(addedObjList.get(j), matchattrMapD);
									}
								}
							} else {
								attrMapD.put(addedObjList.get(j), finalMap.get("B").get(addedObjList.get(j)));
							} 
							
							}
						} else 
							attrMapD.put(addedObjList.get(j), finalMap.get("B").get(addedObjList.get(j)));
					}
				} else if (finalMap.get("C").containsKey(addedObjList.get(j))){
					if(finalMap.get("C").get(addedObjList.get(j)).containsKey("shape")){
						if(finalMap.get("C").size()> 1){
							if(finalMap.get("C").containsKey(1)){
							if(finalMap.get("C").get(addedObjList.get(j)).get("shape").equals(finalMap.get("C").get(1).get("shape"))){
							//if shapes are same, transformation is applicable on it
								if(compObj.getShapeDir().containsKey(finalMap.get("C").get(addedObjList.get(j)).get("shape"))){
									
									HashMap<String, String> matchattrMapD = new HashMap<String, String> ();
									matchattrMapD.put("shape", compObj.getShapeDir().get(finalMap.get("C").get(addedObjList.get(j)).get("shape")));
									
									for(Map.Entry<String, String> attr : finalMap.get("C").get(addedObjList.get(j)).entrySet()){
										if(!attr.getKey().equals("shape")){
											matchattrMapD.put(attr.getKey(), attr.getValue());
										}
									}
									attrMapD.put(addedObjList.get(j), matchattrMapD);
								} else {
									//something else changes: angle??
									if(finalMap.get("C").get(addedObjList.get(j)).get("angle").equals(finalMap.get("C").get(1).get("angle"))){
										//change angle asper transformation
										HashMap<String, String> matchattrMapD = new HashMap<String, String> ();
										matchattrMapD.put("angle", (finalMap.get("B").get(1).get("angle")));
										
										for(Map.Entry<String, String> attr : finalMap.get("C").get(addedObjList.get(j)).entrySet()){
											if(!attr.getKey().equals("angle") && !attr.getKey().equals("left-of") && !attr.getKey().equals("above")){
												matchattrMapD.put(attr.getKey(), attr.getValue());
											}
										}
										attrMapD.put(addedObjList.get(j), matchattrMapD);
									}
										
								}
							} else {
								attrMapD.put(addedObjList.get(j), finalMap.get("C").get(addedObjList.get(j)));
							}
							}
						} else 
							attrMapD.put(addedObjList.get(j), finalMap.get("C").get(addedObjList.get(j)));
					}
				}
			}
		}
    	
		return attrMapD;
    	
	}
	
	
	//3x3
	/*
	 */
	//Create Attribute list for fictional solution D for 3x3 problems
	//
	//
	public HashMap <Integer, HashMap<String, String>>  generateSolution3x3_custom(
			HashMap <String, HashMap <Integer, HashMap<String, String>>> finalMap,
			HashMap <Integer, HashMap<String, String>> mapObjAC,
			HashMap <Integer, HashMap<String, String>> deletedAttrsMap,
			HashMap <Integer, HashMap<String, String>> addedAttrsMap, 
        	Comparer compObj,
        	List <Integer> deletedObjList, 
        	List <Integer> addedObjList ){
		
		HashMap <Integer, HashMap<String, String>> attrMapD = new HashMap<Integer, HashMap<String, String>>();
		
    	System.out.println("--------- Generator ----------");
    	
    	int countAttrA = 0;
		int countObjA = finalMap.get("A").size();
		Integer i;
		for (i = 1; i <= finalMap.get("A").size(); i++){
			HashMap<String,String> matchattrMapD = new HashMap<String,String>();
			int circleFlag = 0;
			int circleAngleFlag = 0;
			int squareFlag = 0;
			int squareAngleFlag = 0;
			int triangleFlag = 0;
			int triangleAngleFlag = 0;
			int righttriangleFlag = 0;
			int righttriangleAngleFlag = 0;
			for (Map.Entry <String,String> attrMapI : finalMap.get("A").get(i).entrySet()) {
			
				//check if it has circle and angle combination
				String attrNameA = attrMapI.getKey();
				String attrValueA = attrMapI.getValue();
				
				if(attrNameA.equals("shape") && attrValueA.equalsIgnoreCase("circle")){
					circleFlag = 1;
				} else if(attrNameA.equals("angle")){
					circleAngleFlag = 1;
				}			
				///square and 90,180,270,360 angle check
				if(attrNameA.equals("shape") && (attrValueA.equalsIgnoreCase("square")) ){
					squareFlag = 1;
				} else if(attrNameA.equals("angle") && 
						( attrValueA.equalsIgnoreCase("90") || attrValueA.equalsIgnoreCase("180") 
							|| attrValueA.equalsIgnoreCase("270") || attrValueA.equalsIgnoreCase("360"))){
					squareAngleFlag = 1;
				}				
				//triangle and 120,240,360 angle check
				if(attrNameA.equals("shape") && (attrValueA.equalsIgnoreCase("triangle")) ){
					triangleFlag = 1;
				} else if(attrNameA.equals("angle") && ( attrValueA.equalsIgnoreCase("120") || attrValueA.equalsIgnoreCase("240") || attrValueA.equalsIgnoreCase("360"))){
					triangleAngleFlag = 1;
				}
				//right - triangle and 0, 360 angle check
				if(attrNameA.equals("shape") && (attrValueA.equalsIgnoreCase("triangle")) ){
					righttriangleFlag = 1;
				} else if(attrNameA.equals("angle") && ( attrValueA.equalsIgnoreCase("0") || attrValueA.equalsIgnoreCase("360"))){
					righttriangleAngleFlag = 1;
				}
			}
			if(finalMap.get("G").containsKey(i)){
			  for (Map.Entry <String,String> attrMapI : finalMap.get("G").get(i).entrySet()) {
				//check if it has circle and angle combination
				String attrNameA = attrMapI.getKey();
				String attrValueA = attrMapI.getValue();
				
				compObj.setFlip(0);
		
				if(!mapObjAC.containsKey(i)){
					
						break;
					
				}
					
					if(attrNameA.equalsIgnoreCase("shape")){
						countObjA++;
						if(mapObjAC.containsKey(i)){
						  if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same"))
							matchattrMapD.put(attrNameA, attrValueA);
						  else if (mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("changed")){
							if(compObj.getShapeDir().containsKey(attrValueA)){
								if (compObj.getShapeDir().get(attrValueA).equals("circle")){
									circleFlag = 1;
									circleAngleFlag = 1;
								}
								matchattrMapD.put(attrNameA, compObj.getShapeDir().get(attrValueA));
							} else {
								double rel = 1.0;
								//get the relationship between the two and derive correct answer
								for (Map.Entry<String,String> entry : compObj.getShapeDir().entrySet()) {
									  String shapeorig = entry.getKey();
									  String shapefnl = entry.getValue();
									  if(compObj.getEdgesDir().containsKey(shapeorig) && compObj.getEdgesDir().containsKey(shapefnl)){
										  rel = (double)compObj.getEdgesDir().get(shapeorig)/(double)compObj.getEdgesDir().get(shapefnl);
										  break;
									  }
								}
								//derive correct shape
								if(compObj.getEdgesDir().containsKey(attrValueA)){
									int noOfEdgesD = (int) ((int)compObj.getEdgesDir().get(attrValueA)/rel);
									for (Map.Entry<String,Integer> entry : compObj.getEdgesDir().entrySet()) {
	  								  String shape = entry.getKey();
	  								  Integer shapeEdges = entry.getValue();
	  								  if(noOfEdgesD == shapeEdges){
	  									matchattrMapD.put(attrNameA, shape);
	  									break;
	  								  }
									}
								}
							}
						  }
						} 
					} else if(attrNameA.equalsIgnoreCase("fill") ){
						countAttrA++;
						if(mapObjAC.containsKey(i)){
						if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same"))
							matchattrMapD.put(attrNameA, attrValueA);
						else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("filled"))
							matchattrMapD.put(attrNameA, "yes");
						else if (mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("unfilled"))
	     					matchattrMapD.put(attrNameA, "no");
						else if (mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("filledno2lh") )
	     					matchattrMapD.put(attrNameA, "left-half");
						else if (mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("filledno2rh"))
	     					matchattrMapD.put(attrNameA, "right-half");
						else if (mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("filledno2th"))
	     					matchattrMapD.put(attrNameA, "top-half");
						else if (mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("filledno2bh"))
	     					matchattrMapD.put(attrNameA, "bottom-half");
						else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("fillArray")){
							//TODO: Add all cases
							for (String fillVal: attrValueA.split(",")){
				        		if (fillVal.equalsIgnoreCase("top-left")){
				        			compObj.fillArray[0] = compObj.fillArray[0] | 1;
				        		} else if (fillVal.equalsIgnoreCase("top-right")){
				        			compObj.fillArray[1] = compObj.fillArray[0] | 1;;
				        		} else if (fillVal.equalsIgnoreCase("bottom-left")){
				        			compObj.fillArray[2] = compObj.fillArray[0] | 1;;
				        		} else if (fillVal.equalsIgnoreCase("bottom-right")){
				        			compObj.fillArray[3] = compObj.fillArray[0] | 1;;
				        		}
				        	}
							String result = String.valueOf(compObj.fillArray[0])+ "," + 
									String.valueOf(compObj.fillArray[1])+ "," + 
									String.valueOf(compObj.fillArray[2])+ "," +
									String.valueOf(compObj.fillArray[3]);
							//Add output to map
							matchattrMapD.put(attrNameA, result);
							//reset array for get fill map for each choice 1-6
							for(int k= 0; k< 4; k++)
								compObj.fillArray[k] = 0;            					 
						}
					} 
					}
					else if(attrNameA.equalsIgnoreCase("size")){
						countAttrA++;
						if(mapObjAC.containsKey(i)) {		
							
							if (mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same"))
								matchattrMapD.put(attrNameA, attrValueA);
							else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("enlarged")){
								String val = "";
								if(attrValueA.equals("Xsmall"))
									val = "small";
								else if(attrValueA.equals("small"))
									val = "medium";
								else if(attrValueA.equals("medium"))
									val = "large";
								else if(attrValueA.equals("large"))
									val = "Xlarge";
								else if(attrValueA.equals("Xlarge"))
									val = "XXlarge";
									matchattrMapD.put(attrNameA, val);
							} else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("reduced")){
								String val = "";
								if(attrValueA.equals("small"))
									val = "Xsmall";
								else if(attrValueA.equals("medium"))
									val = "small";
								else if(attrValueA.equals("large"))
									val = "medium";
								else if(attrValueA.equals("Xlarge"))
									val = "large";
								else if(attrValueA.equals("XXlarge"))
									val = "Xlarge";
									matchattrMapD.put(attrNameA, val);
							}
							
						}
					} 
					else if(attrNameA.equalsIgnoreCase("angle") && ( (circleFlag==1) && (circleAngleFlag==1) )){
						countAttrA++;
						//do not put angle as key comparison factor in c
						matchattrMapD.put(attrNameA, "0");
					}
					else if(attrNameA.equalsIgnoreCase("angle")){ 
						countAttrA++;
					  if(mapObjAC.containsKey(i)){
						if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same"))
							matchattrMapD.put(attrNameA, attrValueA);
						else if(triangleFlag == 1 && mapObjAC.containsKey("vertical-flip")){
							matchattrMapD.put(attrNameA,attrValueA );
						}
						else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("rotated45clockwise") || mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("rotated45anticlockwise"))
							matchattrMapD.put(attrNameA, "45");
						else if( mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("rotated180clockwise")){
							
							if((Double.parseDouble(attrValueA) + 180) < 360)
								matchattrMapD.put(attrNameA, String.valueOf((int)Double.parseDouble(attrValueA) + 180));
							else if((Double.parseDouble(attrValueA) + 180) > 360)
								matchattrMapD.put(attrNameA, String.valueOf((int)Double.parseDouble(attrValueA) - 180));
							else if((Double.parseDouble(attrValueA) + 180) == 360)
								matchattrMapD.put(attrNameA, "0");
							compObj.setFlip(1);
						}	
						else if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("rotated180anticlockwise"))
							matchattrMapD.put(attrNameA, String.valueOf((int)Double.parseDouble(attrValueA) - 180));
						
						else {
							if(( (squareFlag==1) && (squareAngleFlag==1) )){
								matchattrMapD.put(attrNameA, attrValueA);
							} else if(( (triangleFlag==1) && (triangleAngleFlag==1) )){
								matchattrMapD.put(attrNameA, attrValueA);
							} 
							int res =(int) (Double.parseDouble(attrValueA) + Double.parseDouble(mapObjAC.get(i).get(attrNameA)));
							matchattrMapD.put(attrNameA, String.valueOf(res));
						}
					  }
					}
					else if(attrNameA.equalsIgnoreCase("above")){
						countAttrA++;
						if(mapObjAC.containsKey(i)){
							if(mapObjAC.get(i).containsKey(attrNameA)){
							if( mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same")){
								matchattrMapD.put(attrNameA, attrValueA);
							} else if((mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("notabove"))){
								countAttrA++;
							}
							}
							
						}
					}
						
					else if(attrNameA.equalsIgnoreCase("inside")){ 
						countAttrA++;
						if(mapObjAC.containsKey(i)){
							if(mapObjAC.get(i).containsKey(attrNameA)){
								if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same"))
									matchattrMapD.put(attrNameA,attrValueA );
							}
						}
					} 
					
					else if(attrNameA.equalsIgnoreCase("left-of") ){
						countAttrA++;
						if(mapObjAC.containsKey(i)){
							if(mapObjAC.get(i).containsKey(attrNameA)){
								if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same") ){
									matchattrMapD.put(attrNameA,attrValueA );
								}
							}
						} 						
						
					} else if(attrNameA.equalsIgnoreCase("vertical-flip")){
						if(mapObjAC.get(i).containsKey(attrNameA)){
							if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("flipped") ){
								countAttrA++;
								matchattrMapD.put(attrNameA,"yes");
							}
						}
					} else if(attrNameA.equalsIgnoreCase("overlaps")) { 
						if(mapObjAC.get(i).containsKey(attrNameA)){
							if(mapObjAC.get(i).get(attrNameA).equalsIgnoreCase("same")){
								countAttrA++;
								matchattrMapD.put(attrNameA,attrValueA);
							}
						}
					}
			  }
			}
			
			if(!deletedObjList.contains(i)){
				if (addedAttrsMap.containsKey(i)){
					for(Map.Entry<String, String> addedAttrs : addedAttrsMap.get(i).entrySet()){
							if (!matchattrMapD.containsKey(addedAttrs.getKey())){
								matchattrMapD.put(addedAttrs.getKey(), addedAttrs.getValue());
							}
					}
				} 
				attrMapD.put(i, matchattrMapD);
			}
		}
		
    	
		return attrMapD;
    	
	}
	
	
}
