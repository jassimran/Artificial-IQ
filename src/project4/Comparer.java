/**
 * 
 */
package project4;

import java.util.HashMap;

/**
 * @author Jassimran
 *
 */
public class Comparer {
	
	HashMap<String,String> shapeDir = new HashMap<String,String>();
	HashMap<String,Integer> edgesDir = new HashMap<String,Integer>();
	Integer fillArray[] = {0,0,0,0};
	HashMap<String,String> positionDir = new HashMap<String,String>();
	
	
	int flip = 0;

	HashMap<String, String> getShapeDir() {
		return shapeDir;
	}

	void setShapeDir(HashMap<String, String> shapeDir) {
		this.shapeDir = shapeDir;
	}

	HashMap<String, Integer> getEdgesDir() {
		return edgesDir;
	}

	void setEdgesDir(HashMap<String, Integer> edgesDir) {
		this.edgesDir = edgesDir;
	}

	Integer[] getFillArray() {
		return fillArray;
	}

	void setFillArray(Integer[] fillArray) {
		this.fillArray = fillArray;
	}

	HashMap<String, String> getPositionDir() {
		return positionDir;
	}

	void setPositionDir(HashMap<String, String> positionDir) {
		this.positionDir = positionDir;
	}

	int getFlip() {
		return flip;
	}

	void setFlip(int flip) {
		this.flip = flip;
	}


	
    
    public void edgesLoader() {
    	
       edgesDir.put("triangle", 3);
       edgesDir.put("square", 4);
       edgesDir.put("pentagon", 5);
       edgesDir.put("hexagon", 6);
       edgesDir.put("haptagon", 7);
       edgesDir.put("octagon", 8);

    }
    
    public void fillArrayMethod(String fill){
    	for (String fillVal: fill.split(",")){
    		if (fillVal.equalsIgnoreCase("top-left")){
    			fillArray[0] = fillArray[0] | 1;
    		} else if (fillVal.equalsIgnoreCase("top-right")){
    			fillArray[1] = fillArray[1] | 1;
    		} else if (fillVal.equalsIgnoreCase("bottom-left")){
    			fillArray[2] = fillArray[2] | 1;
    		} else if (fillVal.equalsIgnoreCase("bottom-right")){
    			fillArray[3] = fillArray[3] | 1;
    		} else if (fillVal.equalsIgnoreCase("yes")){
    			for (int j = 0; j< 4 ;j++)
    				fillArray[j] = 1;
    		}
    	}
    }
	
    
    public String shapeComparor(String origShp, String finalShp) {
    	
        if(origShp.equalsIgnoreCase(finalShp)){
        	return "same";
        } else {
        	String retVal = "changed";
        	//shape directory
        	shapeDir.put(origShp,finalShp);
        	edgesLoader();
        	return retVal;
        }
    }
    
    public String fillComparor(String origFill, String finalFill) {
    	
        if(origFill.equalsIgnoreCase("yes") && finalFill.equalsIgnoreCase("no")){
        	return "unfilled";
        } else if(origFill.equalsIgnoreCase("no") && finalFill.equalsIgnoreCase("yes")) {
        	return "filled";
        } else if(origFill.equalsIgnoreCase("no") && finalFill.equalsIgnoreCase("left-half")) {
        	return "filledno2lh";
        } else if(origFill.equalsIgnoreCase("left-half") && finalFill.equalsIgnoreCase("no")) {
        	return "filledlh2no";
        } else if(origFill.equalsIgnoreCase("no") && finalFill.equalsIgnoreCase("right-half")) {
        	return "filledno2rh";
        } else if(origFill.equalsIgnoreCase("right-half") && finalFill.equalsIgnoreCase("no")) {
        	return "filledrh2no";
        } else if(origFill.equalsIgnoreCase("no") && finalFill.equalsIgnoreCase("top-half")) {
        	return "filledno2th";
        } else if(origFill.equalsIgnoreCase("top-half") && finalFill.equalsIgnoreCase("no")) {
        	return "filledth2no";
        } else if(origFill.equalsIgnoreCase("no") && finalFill.equalsIgnoreCase("bottom-half")) {
        	return "filledno2bh";
        } else if(origFill.equalsIgnoreCase("bottom-half") && finalFill.equalsIgnoreCase("no")) {
        	return "filledbh2no";
        } else if(origFill.equalsIgnoreCase("yes") && finalFill.equalsIgnoreCase("left-half")) {
        	return "filledyes2lh";
        } else if(origFill.equalsIgnoreCase("left-half") && finalFill.equalsIgnoreCase("yes")) {
        	return "filledlh2yes";
        } else if(origFill.equalsIgnoreCase("yes") && finalFill.equalsIgnoreCase("right-half")) {
        	return "filledyes2rh";
        } else if(origFill.equalsIgnoreCase("right-half") && finalFill.equalsIgnoreCase("yes")) {
        	return "filledrh2yes";
        } else if(origFill.equalsIgnoreCase("yes") && finalFill.equalsIgnoreCase("top-half")) {
        	return "filledyes2th";
        } else if(origFill.equalsIgnoreCase("top-half") && finalFill.equalsIgnoreCase("yes")) {
        	return "filledth2yes";
        } else if(origFill.equalsIgnoreCase("yes") && finalFill.equalsIgnoreCase("bottom-half")) {
        	return "filledyes2bh";
        } else if(origFill.equalsIgnoreCase("bottom-half") && finalFill.equalsIgnoreCase("yes")) {
        	return "filledbh2yes";
        } else if (!finalFill.equals(origFill)){
        	fillArrayMethod(origFill);  
        	fillArrayMethod(finalFill);  
        	return "fillArray";
        } else
        	return "same";
    }
    
    public String sizeComparor(String origSize, String finalSize) {
    	//only 3 cases - small ,large, medium
    	
    	if(origSize.equalsIgnoreCase("Xsmall") && 
    			(finalSize.equalsIgnoreCase("small")|| finalSize.equalsIgnoreCase("medium")	|| finalSize.equalsIgnoreCase("large") 
    			|| finalSize.equalsIgnoreCase("Xlarge")	|| finalSize.equalsIgnoreCase("XXlarge")) 
    			||
    		origSize.equalsIgnoreCase("small") && 
    			(finalSize.equalsIgnoreCase("medium")|| finalSize.equalsIgnoreCase("large")
    			|| finalSize.equalsIgnoreCase("Xlarge") || finalSize.equalsIgnoreCase("XXlarge"))
    			||
    		origSize.equalsIgnoreCase("medium") && 
    			(finalSize.equalsIgnoreCase("large") || finalSize.equalsIgnoreCase("Xlarge") || finalSize.equalsIgnoreCase("XXlarge"))
    			||
   			origSize.equalsIgnoreCase("large") && 
    			(finalSize.equalsIgnoreCase("Xlarge") || finalSize.equalsIgnoreCase("XXlarge")) 
    			||
    		origSize.equalsIgnoreCase("Xlarge") && 
				(finalSize.equalsIgnoreCase("XXlarge"))){
        	return "enlarged";
        } else if(origSize.equalsIgnoreCase("XXlarge") && 
    			(finalSize.equalsIgnoreCase("small")|| finalSize.equalsIgnoreCase("medium")
    			|| finalSize.equalsIgnoreCase("large") || finalSize.equalsIgnoreCase("Xlarge")
    			|| finalSize.equalsIgnoreCase("Xsmall")) 
    			||
    		origSize.equalsIgnoreCase("Xlarge") && 
    			(finalSize.equalsIgnoreCase("medium")|| finalSize.equalsIgnoreCase("large")
    			|| finalSize.equalsIgnoreCase("small") || finalSize.equalsIgnoreCase("Xsmall"))
    			||
    		origSize.equalsIgnoreCase("large") && 
    			(finalSize.equalsIgnoreCase("medium") || finalSize.equalsIgnoreCase("Xsmall")
    			|| finalSize.equalsIgnoreCase("small"))
    			||
   			origSize.equalsIgnoreCase("medium") && 
    			(finalSize.equalsIgnoreCase("small") || finalSize.equalsIgnoreCase("Xsmall")) 
    			||
    		origSize.equalsIgnoreCase("small") && 
				(finalSize.equalsIgnoreCase("Xsmall"))){
        	return "reduced";
        } else
        	return "same";
    	
        /*if(origSize.equalsIgnoreCase("small") && finalSize.equalsIgnoreCase("large")){
        	return "enlargeds2l";
        }
        else if(origSize.equalsIgnoreCase("large") && finalSize.equalsIgnoreCase("small")) {
        	return "reducedl2s";
        } 
        else if(origSize.equalsIgnoreCase("small") && finalSize.equalsIgnoreCase("medium")){
        	return "enlargeds2m";
        }
        else if(origSize.equalsIgnoreCase("medium") && finalSize.equalsIgnoreCase("small")) {
        	return "reducedm2s";
        } 
        else if(origSize.equalsIgnoreCase("medium") && finalSize.equalsIgnoreCase("large")){
        	return "enlargedm2l";
        }
        else if(origSize.equalsIgnoreCase("large") && finalSize.equalsIgnoreCase("medium")) {
        	return "reducedl2m";
        } 
        else if(origSize.equalsIgnoreCase("large") && finalSize.equalsIgnoreCase("Xlarge")) {
        	return "enlargedl2Xl";
        } 
        else if(origSize.equalsIgnoreCase("small") && finalSize.equalsIgnoreCase("Xlarge")) {
        	return "enlargeds2Xl";
        } 
        else if(origSize.equalsIgnoreCase("medium") && finalSize.equalsIgnoreCase("Xlarge")) {
        	return "enlargedm2Xl";
        } 
        else if(origSize.equalsIgnoreCase("Xlarge") && finalSize.equalsIgnoreCase("large")) {
        	return "reducedXl2l";
        } 
        else if(origSize.equalsIgnoreCase("Xlarge") && finalSize.equalsIgnoreCase("medium")) {
        	return "reducedXl2m";
        }
        else if(origSize.equalsIgnoreCase("Xlarge") && finalSize.equalsIgnoreCase("small")) {
        	return "reducedXl2s";
        }
        else if(origSize.equalsIgnoreCase("Xlarge") && finalSize.equalsIgnoreCase("XXlarge")) {
        	return "enlargedXl2XXl";
        } 
        else if(origSize.equalsIgnoreCase("large") && finalSize.equalsIgnoreCase("XXlarge")) {
        	return "enlargedl2XXl";
        } 
        else if(origSize.equalsIgnoreCase("medium") && finalSize.equalsIgnoreCase("XXlarge")) {
        	return "enlargedm2XXl";
        } 
        else if(origSize.equalsIgnoreCase("small") && finalSize.equalsIgnoreCase("XXlarge")) {
        	return "enlargeds2XXl";
        } 
        else if(origSize.equalsIgnoreCase("XXlarge") && finalSize.equalsIgnoreCase("Xlarge")) {
        	return "reducedXXl2Xl";
        }
        else if(origSize.equalsIgnoreCase("XXlarge") && finalSize.equalsIgnoreCase("large")) {
        	return "reducedXXl2l";
        }
        else if(origSize.equalsIgnoreCase("XXlarge") && finalSize.equalsIgnoreCase("medium")) {
        	return "reducedXXl2m";
        }
        else if(origSize.equalsIgnoreCase("XXlarge") && finalSize.equalsIgnoreCase("small")) {
        	return "reducedXXl2s";
        }
        else if(origSize.equalsIgnoreCase("Xsmall") && finalSize.equalsIgnoreCase("small")){
        	return "enlargedXs2s";
        } 
        else if(origSize.equalsIgnoreCase("Xsmall") && finalSize.equalsIgnoreCase("medium")){
        	return "enlargedXs2m";
        }
        else if(origSize.equalsIgnoreCase("Xsmall") && finalSize.equalsIgnoreCase("large")){
        	return "enlargedXs2l";
        }
        else if(origSize.equalsIgnoreCase("Xsmall") && finalSize.equalsIgnoreCase("Xlarge")){
        	return "enlargedXs2Xl";
        }
        else if(origSize.equalsIgnoreCase("Xsmall") && finalSize.equalsIgnoreCase("XXlarge")){
        	return "enlargedXs2XXl";
        }
        else if(origSize.equalsIgnoreCase("XXlarge") && finalSize.equalsIgnoreCase("Xsmall")) {
        	return "reducedXXl2Xs";
        }
        else if(origSize.equalsIgnoreCase("Xlarge") && finalSize.equalsIgnoreCase("Xsmall")) {
        	return "reducedXl2Xs";
        }
        else if(origSize.equalsIgnoreCase("large") && finalSize.equalsIgnoreCase("Xsmall")) {
        	return "reducedl2Xs";
        }
        else if(origSize.equalsIgnoreCase("medium") && finalSize.equalsIgnoreCase("Xsmall")) {
        	return "reducedm2Xs";
        }
        else if(origSize.equalsIgnoreCase("small") && finalSize.equalsIgnoreCase("Xsmall")) {
        	return "reduceds2Xs";
        }
        else
        	return "same";
        	*/
    }
    
    /*public String angleComparor(String origAngle, String finalAngle) {
    	
        if(origAngle.equalsIgnoreCase("0") && (finalAngle.equalsIgnoreCase("45") || finalAngle.equalsIgnoreCase("315"))){
        	return "rotated45clockwise";
        } else if((origAngle.equalsIgnoreCase("45") || origAngle.equalsIgnoreCase("315")) && finalAngle.equalsIgnoreCase("0")) {
        	return "rotated45anticlockwise";
        } else if(Integer.parseInt(finalAngle) - Integer.parseInt(origAngle) == 180){
        	if(origAngle.equals("90") || origAngle.equals("270"))
        		flip = 1;
        	return "rotated180clockwise";
        } else if(Integer.parseInt(origAngle) - Integer.parseInt(finalAngle) == 180) {
        	return "rotated180anticlockwise";
        } else if(!origAngle.equals(finalAngle)) {
        	return String.valueOf((Integer.parseInt(finalAngle) - Integer.parseInt(origAngle)));
        } else
        	return "same";
    }
    */
    
    
    public String angleComparor2x2(String origAngle, String finalAngle) {
    	
        if(!origAngle.equals(finalAngle)) {
        	if(Double.parseDouble(finalAngle) - Double.parseDouble(origAngle) == 180 || Double.parseDouble(origAngle) - Double.parseDouble(finalAngle) == 180 )
        		flip = 1;
        	return String.valueOf((int)((Double.parseDouble(finalAngle) - Double.parseDouble(origAngle))));
        } else
        	return "same";
    }
    
    public String aboveComparor(String orig, String fnl) {
    	
        if(orig.equalsIgnoreCase(fnl)){
        	return "same";
        } else
        	return "notabove";
    }
    
    
    //TODO: Improve it
    public String insideComparor(String orig, String fnl) {
    	
    	Integer insideOrigArray[] = {0,0,0,0,0};
    	Integer insideFnlArray[] = {0,0,0,0,0};
    	
    	for (String fillVal: orig.split(",")){
    		if (fillVal.equalsIgnoreCase("1")){
    			insideOrigArray[0] = 1;
    		}
    		else if (fillVal.equalsIgnoreCase("2")){
    			insideOrigArray[1] = 2; 
    		}
    		else if (fillVal.equalsIgnoreCase("3")){
        			insideOrigArray[2] = 3; 
    		}
    		else if (fillVal.equalsIgnoreCase("4")){
    			insideOrigArray[3] = 4;
    		}
    		else if (fillVal.equalsIgnoreCase("2")){
    			insideOrigArray[4] = 5; 
    		}
    	}
    	
    	String origInside = "";
    	for(int i : insideOrigArray){
    		if(i !=0)
    			origInside = origInside.concat(String.valueOf(i)+",");
    	}
    	
    	for (String fillVal: fnl.split(",")){
    		if (fillVal.equalsIgnoreCase("1")){
    			insideFnlArray[0] = 1;
    		}
    		else if (fillVal.equalsIgnoreCase("2")){
    			insideFnlArray[1] = 2; 
    		}
    		else if (fillVal.equalsIgnoreCase("3")){
    			insideFnlArray[2] = 3; 
    		}
    		else if (fillVal.equalsIgnoreCase("4")){
    			insideFnlArray[3] = 4;
    		}
    		else if (fillVal.equalsIgnoreCase("2")){
    			insideFnlArray[4] = 5; 
    		}
    	}
    	
    	String finalInside = "";
    	for(int i : insideFnlArray){
    		if(i !=0)
    			finalInside = finalInside.concat(String.valueOf(i)+",");
    	}
    			
        if(origInside.equalsIgnoreCase(finalInside))
        	return "same";
        else
        	return "notinside";
    }
    
    public String overlapComparor(String orig, String fnl) {
    	
        if(orig.equalsIgnoreCase("Z") && fnl.equalsIgnoreCase("Z")){
        	return "sameZ";
        } else  if(orig.equalsIgnoreCase("Y") && fnl.equalsIgnoreCase("Y")){
        	return "sameY";
        } else  if(orig.equalsIgnoreCase(fnl)){
        	return "same";
        } else
        	return "nolap";
    }
    
    public String leftOfComparor(String orig, String fnl) {
    	
        if(orig.equalsIgnoreCase(fnl)){
        	return "same";
        } else {
        	positionDir.put(orig, fnl);
        	return "changed";
        }
        	
    }
    
    public String flipComparor(String orig, String fnl) {
    	
        if(orig.equalsIgnoreCase(fnl)){
        	return "same";
        } else {
        	//String res = orig.concat("-").concat(fnl);
        	return "flipped";
        }
        	
    }
}
