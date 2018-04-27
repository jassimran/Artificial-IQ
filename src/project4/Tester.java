/**
 * 
 */
package project4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jassimran
 *
 */
class Tester {
	
	Integer getScore(RavensFigure rfigOption, Map <String, Map<String, String>> attrMapD, 
			Comparer compObj,
			Map <String, HashMap<String, String>> deletedAttrsMap,
			Map <String, HashMap<String, String>> addedAttrsMap,
			List <String> deletedObjList, List <String> addedObjList, 
			Map <String, String> deletedAttrList){
    	
    		
		int totalScore = 0;
        	for(RavensObject itrOptionObj : rfigOption.getObjects()){
    			String objectNameOption = itrOptionObj.getName();
    			int totalAttrsD = 0;
    			
        		if(attrMapD.containsKey(objectNameOption)){ 
        		 
        		  for(RavensAttribute attrOption : itrOptionObj.getAttributes()){
        			String attrNameOption = attrOption.getName();
        			String attrValueOption = attrOption.getValue();
        			
        			//comparison
        			if(attrNameOption.equalsIgnoreCase("shape")){
        				if(attrMapD.get(objectNameOption).containsKey("shape")){
        					totalAttrsD++;
        					String attrValueD = attrMapD.get(objectNameOption).get("shape");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        					}
        				} else
        					totalScore = totalScore -1;
        			} else if(attrNameOption.equalsIgnoreCase("fill")){
        				if(attrMapD.get(objectNameOption).containsKey("fill")){
        					totalAttrsD++;
        					String attrValueD = attrMapD.get(objectNameOption).get("fill");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        					} else {
        						compObj.fillArrayMethod(attrValueOption);
        						String result = String.valueOf(compObj.fillArray[0])+ "," + 
        								String.valueOf(compObj.fillArray[1])+ "," + 
        								String.valueOf(compObj.fillArray[2])+ "," +
        								String.valueOf(compObj.fillArray[3]);
        						if (attrValueD.equals(result))
        							totalScore = totalScore +2;
        						//reset array for next choice 1-6
        						for(int j= 0; j< 4; j++)
        							compObj.fillArray[j] = 0;  
        						
        					}
        				} else
        					totalScore = totalScore -1;           			
        			} else if(attrNameOption.equalsIgnoreCase("size")){
        				if(attrMapD.get(objectNameOption).containsKey("size")){
        					totalAttrsD++;
        					String attrValueD = attrMapD.get(objectNameOption).get("size");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        					}
        				} else
        					totalScore = totalScore -1;            			
        			}  else if(attrNameOption.equalsIgnoreCase("angle")){
        				if(attrMapD.get(objectNameOption).containsKey("angle")){
        					totalAttrsD++;
        					String attrValueD = attrMapD.get(objectNameOption).get("angle");
        					
        					//give higher preference to exact matching angle
        					if (attrValueOption.equals(attrValueD))
        						totalScore = totalScore +5;
        					else if (attrValueOption.equals(String.valueOf(360 - Integer.parseInt(attrValueD))) ||
        							(attrValueOption.equals(String.valueOf(360 + Integer.parseInt(attrValueD)))))
                				totalScore = totalScore +2;
        				} else
        					totalScore = totalScore -1;   	            			
        			} else if(attrNameOption.equalsIgnoreCase("inside")){
        				if(attrMapD.get(objectNameOption).containsKey("inside")){
        					totalAttrsD++;
        					String attrValueD = attrMapD.get(objectNameOption).get("inside");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        					}
        				} else if (addedAttrsMap.containsKey(objectNameOption)){
        					if(addedAttrsMap.get(objectNameOption).containsKey(attrNameOption)){
        						String attrValueD = addedAttrsMap.get(objectNameOption).get("inside");
        						if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        						}
        					}
        				} else if (deletedAttrsMap.containsKey(objectNameOption)){
        					if(deletedAttrsMap.get(objectNameOption).containsKey(attrNameOption)){
        						String attrValueD = deletedAttrsMap.get(objectNameOption).get("inside");
        						if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore - 2;
        						}
        					}
        				} else
        					totalScore = totalScore -1;             			
        			} else if(attrNameOption.equalsIgnoreCase("above")){
        				if(attrMapD.get(objectNameOption).containsKey("above")){
        					totalAttrsD++;
        					String attrValueD = attrMapD.get(objectNameOption).get("above");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        					}
        				} else if (addedAttrsMap.containsKey(objectNameOption)){
        					if(addedAttrsMap.get(objectNameOption).containsKey(attrNameOption)){
        						String attrValueD = addedAttrsMap.get(objectNameOption).get("above");
        						if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        						}
        					}
        				} else if (deletedAttrsMap.containsKey(objectNameOption)){
        					if(deletedAttrsMap.get(objectNameOption).containsKey(attrNameOption)){
        						String attrValueD = deletedAttrsMap.get(objectNameOption).get("above");
        						if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore - 2;
        						}
        					}
        				} else
        					totalScore = totalScore -1;             			
        			} else if(attrNameOption.equalsIgnoreCase("left-of")){
        				if(attrMapD.get(objectNameOption).containsKey("left-of")){
        					totalAttrsD++;
        					String attrValueD = attrMapD.get(objectNameOption).get("left-of");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        					}
        				} else if (addedAttrsMap.containsKey(objectNameOption)){
        					if(addedAttrsMap.get(objectNameOption).containsKey(attrNameOption)){
        						String attrValueD = addedAttrsMap.get(objectNameOption).get("left-of");
        						if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        						}
        					}
        				} else if (deletedAttrsMap.containsKey(objectNameOption)){
        					if(deletedAttrsMap.get(objectNameOption).containsKey(attrNameOption)){
        						String attrValueD = deletedAttrsMap.get(objectNameOption).get("left-of");
        						if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore -2;
        						}
        					}
        				}else
        					totalScore = totalScore -1;             			
        			} else if(attrNameOption.equalsIgnoreCase("overlaps")){
        				if(attrMapD.get(objectNameOption).containsKey("overlaps")){
        					totalAttrsD++;
        					String attrValueD = attrMapD.get(objectNameOption).get("overlaps");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        					}
        				} else if (addedAttrsMap.containsKey(objectNameOption)){
        					if(addedAttrsMap.get(objectNameOption).containsKey(attrNameOption)){
        						String attrValueD = addedAttrsMap.get(objectNameOption).get("overlaps");
        						if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        					}
        					}
        				} else
        					totalScore = totalScore -1;             			
        			} else if(attrNameOption.equalsIgnoreCase("vertical-flip")){
        				if(attrMapD.get(objectNameOption).containsKey("vertical-flip")){
        					totalAttrsD++;
        					totalScore = totalScore +1;
        					String attrValueD = attrMapD.get(objectNameOption).get("vertical-flip");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +1;
        					}
        				} else
        					totalScore = totalScore -1;             			
        			} else if(addedAttrsMap.containsKey(objectNameOption)){
        				if(addedAttrsMap.get(objectNameOption).containsKey(attrNameOption)){
        					String attrValueExpected = addedAttrsMap.get(objectNameOption).get(attrNameOption);
        					if (attrValueOption.equalsIgnoreCase(attrValueExpected)){
        						//Adding scoring mechanism
            					totalScore = totalScore +2;
        					} else {
        						//Adding scoring mechanism
            					totalScore = totalScore +1;
        					}
        				}
        			} else if(deletedAttrsMap.containsKey(objectNameOption)){
        				if(deletedAttrsMap.get(objectNameOption).containsKey(attrNameOption)){
        					String attrValueExpected = deletedAttrsMap.get(objectNameOption).get(attrNameOption);
        					if (attrValueOption.equalsIgnoreCase(attrValueExpected)){
        						//Adding scoring mechanism
            					totalScore = totalScore - 2;
        					} else {
        						//Adding scoring mechanism
            					totalScore = totalScore - 1;
        					}
        				}
        			}
        			
        	    }
        	} else if(deletedObjList.contains(objectNameOption)){
        		totalScore = totalScore -1;
        	} else if(addedObjList.contains(objectNameOption)) {
        		totalScore = totalScore + 1;
        	} 
        	if(attrMapD.containsKey(objectNameOption)){
        		if(totalAttrsD != attrMapD.get(objectNameOption).size()){
            		if(attrMapD.get(objectNameOption).containsKey(deletedAttrList.get(objectNameOption)))
            			totalScore = totalScore -1;
        		}
        	} else 
        		totalScore = totalScore -1;
        	
    	}
        	return totalScore;
	}
	
	
	
	
	
	
	
	
	

	public Integer getScore2x2(HashMap <Integer, HashMap<String, String>> objOptionMap, 
			HashMap <Integer, HashMap<String, String>> attrMapD, 
			Comparer compObj,
			HashMap <Integer, HashMap<String, String>> deletedAttrsMap,
			HashMap <Integer, HashMap<String, String>> addedAttrsMap,
			List <Integer> deletedObjList, List<Integer> addedObjList, 
			Map<String, String> deletedAttrList){
    	
    		
		int totalScore = 0;
        for (int i = 1; i <= attrMapD.size() ; i ++){
        	int totalAttrsD = 0;
        	if (attrMapD.containsKey(i)){
        	if (objOptionMap.containsKey(i)){
        		for (Map.Entry<String,String> entry : objOptionMap.get(i).entrySet()) {
        			String attrNameOption = entry.getKey();
        			String attrValueOption = entry.getValue();
						
        			
        			//comparison
        			if(attrNameOption.equalsIgnoreCase("shape")){
        				if(attrMapD.get(i).containsKey("shape")){
        					totalAttrsD++;
        					String attrValueD = attrMapD.get(i).get("shape");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +5;
        					}
        				} else
        					totalScore = totalScore -1;
        			} else if(attrNameOption.equalsIgnoreCase("fill")){
        				if(attrMapD.get(i).containsKey("fill")){
        					totalAttrsD++;
        					String attrValueD = attrMapD.get(i).get("fill");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +5;
        					} else {
        						compObj.fillArrayMethod(attrValueOption);
        						String result = String.valueOf(compObj.fillArray[0])+ "," + 
        								String.valueOf(compObj.fillArray[1])+ "," + 
        								String.valueOf(compObj.fillArray[2])+ "," +
        								String.valueOf(compObj.fillArray[3]);
        						if (attrValueD.equals(result))
        							totalScore = totalScore +4;
        						//reset array for next choice 1-6
        						for(int j= 0; j< 4; j++)
        							compObj.fillArray[j] = 0;  
        						
        					}
        				} else
        					totalScore = totalScore -1;           			
        			} else if(attrNameOption.equalsIgnoreCase("size")){
        				if(attrMapD.get(i).containsKey("size")){
        					totalAttrsD++;
        					String attrValueD = attrMapD.get(i).get("size");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +4;
        					}
        				} else
        					totalScore = totalScore -1;            			
        			}  else if(attrNameOption.equalsIgnoreCase("angle")){
        				if(attrMapD.get(i).containsKey("angle")){
        					totalAttrsD++;
        					String attrValueD = attrMapD.get(i).get("angle");
        					
        					//give higher preference to exact matching angle
        					if (attrValueOption.equals(attrValueD))
        						totalScore = totalScore +2;
        					else if (attrValueOption.equals(String.valueOf(360 - Integer.parseInt(attrValueD))) ||
        							(attrValueOption.equals(String.valueOf(360 + Integer.parseInt(attrValueD)))))
                				totalScore = totalScore +1;
        				} else
        					totalScore = totalScore -1;   	            			
        			} else if(attrNameOption.equalsIgnoreCase("inside")){
        				if(attrMapD.get(i).containsKey("inside")){
        					totalAttrsD++;
        					String attrValueD = attrMapD.get(i).get("inside");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        					}
        				} else if (addedAttrsMap.containsKey(i)){
        					if(addedAttrsMap.get(i).containsKey(attrNameOption)){
        						String attrValueD = addedAttrsMap.get(i).get("inside");
        						if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        						}
        					}
        				} else if (deletedAttrsMap.containsKey(i)){
        					if(deletedAttrsMap.get(i).containsKey(attrNameOption)){
        						String attrValueD = deletedAttrsMap.get(i).get("inside");
        						if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore - 2;
        						}
        					}
        				} else
        					totalScore = totalScore -1;             			
        			} else if(attrNameOption.equalsIgnoreCase("above")){
        				if(attrMapD.get(i).containsKey("above")){
        					totalAttrsD++;
        					String attrValueD = attrMapD.get(i).get("above");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        					}
        				} else if (addedAttrsMap.containsKey(i)){
        					if(addedAttrsMap.get(i).containsKey(attrNameOption)){
        						String attrValueD = addedAttrsMap.get(i).get("above");
        						if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        						}
        					}
        				} else if (deletedAttrsMap.containsKey(i)){
        					if(deletedAttrsMap.get(i).containsKey(attrNameOption)){
        						String attrValueD = deletedAttrsMap.get(i).get("above");
        						if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore - 2;
        						}
        					}
        				} else
        					totalScore = totalScore -1;             			
        			} else if(attrNameOption.equalsIgnoreCase("left-of")){
        				if(attrMapD.get(i).containsKey("left-of")){
        					totalAttrsD++;
        					String attrValueD = attrMapD.get(i).get("left-of");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        					}
        				} else if (addedAttrsMap.containsKey(i)){
        					if(addedAttrsMap.get(i).containsKey(attrNameOption)){
        						String attrValueD = addedAttrsMap.get(i).get("left-of");
        						if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        						}
        					}
        				} else if (deletedAttrsMap.containsKey(i)){
        					if(deletedAttrsMap.get(i).containsKey(attrNameOption)){
        						String attrValueD = deletedAttrsMap.get(i).get("left-of");
        						if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore -2;
        						}
        					}
        				}else
        					totalScore = totalScore -1;             			
        			} else if(attrNameOption.equalsIgnoreCase("overlaps")){
        				if(attrMapD.get(i).containsKey("overlaps")){
        					totalAttrsD++;
        					String attrValueD = attrMapD.get(i).get("overlaps");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        					}
        				} else if (addedAttrsMap.containsKey(i)){
        					if(addedAttrsMap.get(i).containsKey(attrNameOption)){
        						String attrValueD = addedAttrsMap.get(i).get("overlaps");
        						if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        					}
        					}
        				} else
        					totalScore = totalScore -1;             			
        			} else if(attrNameOption.equalsIgnoreCase("vertical-flip")){
        				if(attrMapD.get(i).containsKey("vertical-flip")){
        					totalAttrsD++;
        					totalScore = totalScore +1;
        					String attrValueD = attrMapD.get(i).get("vertical-flip");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +1;
        					}
        				} else
        					totalScore = totalScore -1;             			
        			} else if(addedAttrsMap.containsKey(i)){
        				if(addedAttrsMap.get(i).containsKey(attrNameOption)){
        					String attrValueExpected = addedAttrsMap.get(i).get(attrNameOption);
        					if (attrValueOption.equalsIgnoreCase(attrValueExpected)){
        						//Adding scoring mechanism
            					totalScore = totalScore +2;
        					} else {
        						//Adding scoring mechanism
            					totalScore = totalScore +1;
        					}
        				}
        			} else if(deletedAttrsMap.containsKey(i)){
        				if(deletedAttrsMap.get(i).containsKey(attrNameOption)){
        					String attrValueExpected = deletedAttrsMap.get(i).get(attrNameOption);
        					if (attrValueOption.equalsIgnoreCase(attrValueExpected)){
        						//Adding scoring mechanism
            					totalScore = totalScore - 2;
        					} else {
        						//Adding scoring mechanism
            					totalScore = totalScore - 1;
        					}
        				}
        			}
        			
        	    }
        	} 
        	} else if(deletedObjList.contains(i)){
        		totalScore = totalScore -1;
        	} else if(addedObjList.contains(i)) {
        		totalScore = totalScore + 1;
        	} 
        	if(attrMapD.containsKey(i)){
        		if(totalAttrsD != attrMapD.get(i).size()){
            		if(attrMapD.get(i).containsKey(deletedAttrList.get(i)))
            			totalScore = totalScore -1;
        		}
        	} else 
        		totalScore = totalScore -1;
        	
    	} 
         
        if(objOptionMap.size() > attrMapD.size()){
        	totalScore = totalScore - (objOptionMap.size() - attrMapD.size());
        }
        
        	return totalScore;
	}
	
	//=================================================
	//CUSTOM TESTER FUNCTION
	//=================================================


	public Integer getScore3x3(HashMap <Integer, HashMap<String, String>> objOptionMap, 
			HashMap <Integer, HashMap<String, String>> attrMapD){
    	
    		
		int totalScore = 0;
        for (int i = 1; i <= attrMapD.size() ; i ++){
        	if (attrMapD.containsKey(i)){
        	if (objOptionMap.containsKey(i)){
        		for (Map.Entry<String,String> entry : objOptionMap.get(i).entrySet()) {
        			String attrNameOption = entry.getKey();
        			String attrValueOption = entry.getValue();
						
        			
        			//comparison
        			if(attrNameOption.equalsIgnoreCase("shape")){
        				if(attrMapD.get(i).containsKey("shape")){
        					String attrValueD = attrMapD.get(i).get("shape");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +5;
        					}
        				} else
        					totalScore = totalScore -1;
        			} else if(attrNameOption.equalsIgnoreCase("fill")){
        				if(attrMapD.get(i).containsKey("fill")){
        					String attrValueD = attrMapD.get(i).get("fill");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +5;
        					} 
        				} else
        					totalScore = totalScore -1;           			
        			} else if(attrNameOption.equalsIgnoreCase("size")){
        				if(attrMapD.get(i).containsKey("size")){
        					String attrValueD = attrMapD.get(i).get("size");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +4;
        					}
        				} else
        					totalScore = totalScore -1;            			
        			}  else if(attrNameOption.equalsIgnoreCase("angle")){
        				if(attrMapD.get(i).containsKey("angle")){
        					String attrValueD = attrMapD.get(i).get("angle");
        					
        					//give higher preference to exact matching angle
        					if (attrValueOption.equals(attrValueD))
        						totalScore = totalScore +2;
        					else if (attrValueOption.equals(String.valueOf(360 - Integer.parseInt(attrValueD))) ||
        							(attrValueOption.equals(String.valueOf(360 + Integer.parseInt(attrValueD)))))
                				totalScore = totalScore +1;
        				} else
        					totalScore = totalScore -1;   	            			
        			} else if(attrNameOption.equalsIgnoreCase("inside")){
        				if(attrMapD.get(i).containsKey("inside")){
        					String attrValueD = attrMapD.get(i).get("inside");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        					}
        				} else
        					totalScore = totalScore -1;             			
        			} else if(attrNameOption.equalsIgnoreCase("above")){
        				if(attrMapD.get(i).containsKey("above")){
        					String attrValueD = attrMapD.get(i).get("above");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        					}
        				} else
        					totalScore = totalScore -1;             			
        			} else if(attrNameOption.equalsIgnoreCase("left-of")){
        				if(attrMapD.get(i).containsKey("left-of")){
        					String attrValueD = attrMapD.get(i).get("left-of");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        					}
        				} else
        					totalScore = totalScore -1;             			
        			} else if(attrNameOption.equalsIgnoreCase("overlaps")){
        				if(attrMapD.get(i).containsKey("overlaps")){
        					String attrValueD = attrMapD.get(i).get("overlaps");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +2;
        					}
        				} else
        					totalScore = totalScore -1;             			
        			} else if(attrNameOption.equalsIgnoreCase("vertical-flip")){
        				if(attrMapD.get(i).containsKey("vertical-flip")){
        					totalScore = totalScore +1;
        					String attrValueD = attrMapD.get(i).get("vertical-flip");
        					if (attrValueOption.equals(attrValueD)){
                					//Adding scoring mechanism
                					totalScore = totalScore +1;
        					}
        				} else
        					totalScore = totalScore -1;             			
        			} 
        			
        	    }
        	} 
        	} 
        	
        	
    	} 
         
        if(objOptionMap.size() > attrMapD.size()){
        	totalScore = totalScore - (objOptionMap.size() - attrMapD.size());
        }
        
        	return totalScore;
	}
	
}
