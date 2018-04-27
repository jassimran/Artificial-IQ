package project4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;



import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

/**
 * Your Agent for solving Raven's Progressive Matrices. You MUST modify this
 * file.
 * 
 * You may also create and submit new files in addition to modifying this file.
 * 
 * Make sure your file retains methods with the signatures:
 * public Agent()
 * public char Solve(VisualRavensProblem problem)
 * 
 * These methods will be necessary for the project's main method to run.
 * 
 */
public class Agent {
    /**
     * The default constructor for your Agent. Make sure to execute any
     * processing necessary before your Agent starts solving problems here.
     * 
     * Do not add any variables to this signature; they will not be used by
     * main().
     * 
     */
    public Agent() {
        
    }
    
	DrawImage dImg;
	int asciiCounter = 65;
	
	public double roundOffAngle(double angle){
		if(angle <50 && angle >40)
			return 45;
		else if((angle > 350 && angle < 10) || (angle >-10 && angle <10 ))
			return 0; //or 360
		else if(angle > -50 && angle < -40)
			return -45;
		else if(angle > 80 && angle < 100)
			return 90;
		else if(angle < -80 && angle > -100)
			return -90;
		else if(angle < -170 && angle > -190)
			return -180;
		else if(angle > 170 && angle < 190)
			return 180;
		else if(angle > 260 && angle < 280)
			return 270;
		else if(angle < -260 && angle > -280)
			return -270;
		else
			return angle;
	}
	
	public float calcDistance(Point p1, Point p2) {
        return (float)Math.sqrt(Math.pow((p1.x-p2.x),2) + Math.pow((p1.y-p2.y),2));
    }
	
	public int findcalcAnglePolygon(Point[] vertices)
    {
        int minAngle = 400; //(some random large number which is a bound)
        ArrayList<Integer> calcAngles = new ArrayList<Integer>();//np.array([])
        for (Point x: vertices)
        {
            ArrayList<PointPair<Point>> dist = new ArrayList<PointPair<Point>>();
            //x = np.array(x_arr[0])
            
            // we have to find the closest two which corresponds to two neighbors
            for (Point y: vertices)
            {
                if (x.equals(y))
                {
                    continue;
                }
                dist.add(new PointPair(calcDistance(x,y),y));
            }
            Object[] dist_array = (Object [])dist.toArray();
            Arrays.sort(dist_array);
            calcAngles.add(calcAngle(x,((PointPair<Point>)dist_array[0]).getY()));
            calcAngles.add(calcAngle(x,((PointPair<Point>)dist_array[1]).getY()));
        }
        Object [] array = (Object [])calcAngles.toArray();
        Arrays.sort(array);
        return ((Integer)array[0]);
    }

    public int calcAngle(Point p1, Point p2){
        return (int)(Math.round(Math.toDegrees(Math.atan2(p1.x - p2.x, p1.y - p2.y))*0.2)/0.2+360)%360;
    }
    
    //A = (1, 0.5), B = (0.5, 1)
    //cosine(theta) = (0.5 + 0.5) / sqrt(5/4) sqrt(5/4) = 4/5
    public int calcCosTheta(Point[] vertices, Point p1, Point p2){
        return (int)(Math.round(Math.toDegrees(Math.atan2(p1.x - p2.x, p1.y - p2.y))*0.2)/0.2+360)%360;
    }

	
	public 	RavensFigure recognizeImage(Mat image, String figName){
		int CircleFlag = 0;
		int SquareFlag = 0;
		int triangleFlag = 0;
		double triangleArea = 0;
		RavensFigure currentFigure = new RavensFigure(figName);
		RavensObject currentObject = null;
		Size imageSize = image.size();
		String firstCentroidObj = "";
		Point firstCentroid = new Point();

   	 	//CREATE A LEFT-OF MAP
   	 	Map<String, Point> centroidMap = new HashMap<String, Point>();
   	 
		//Mat destination_erode = new Mat(image.rows(),image.cols(),image.type());
		//destination_erode = image;
        //int erosion_size = 5;
        
        //Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(2*erosion_size + 1, 2*erosion_size+1));
        //Imgproc.erode(image, destination_erode, element);
        //Mat destination = new Mat(destination_erode.rows(),destination_erode.cols(),destination_erode.type());
        //Imgproc.GaussianBlur(destination_erode, destination,new Size(45,45), 0);
		//Imgproc.threshold(destination, destination, 100, 128, Imgproc.THRESH_BINARY_INV);
		Imgproc.threshold(image, image, 100, 128, Imgproc.THRESH_BINARY_INV);
    	
    	//finding the contours
    	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    	final Mat hierarchy = new Mat();
    	Mat mA= new Mat();
    	Mat mC= new Mat();
    	
    	
    	Imgproc.findContours(image, contours, hierarchy, Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE);
    	
		 
    	for(int i=0; i< contours.size();i++){
    		 //Convert contours(i) from MatOfPoint to MatOfPoint2f
    		 MatOfPoint2f mMOP2f1 = new MatOfPoint2f();
    		 contours.get(i).convertTo(mMOP2f1, CvType.CV_32FC2);
    		 //Processing on mMOP2f1 which is in type MatOfPoint2f
    		 MatOfPoint2f mMOP2f2 = new MatOfPoint2f();
    		
    		 Imgproc.approxPolyDP(mMOP2f1, mMOP2f2, 2, true); 
    		 //Imgproc.approxPolyDP(mMOP2f1, mMOP2f2, 0.01 * Imgproc.arcLength(mMOP2f1, true), true); 
    		 //int angle =  
    		 //Convert back to MatOfPoint and put the new values back into the contours list
    		 mMOP2f2.convertTo(contours.get(i), CvType.CV_32S);
    		
    			
    			 String shape = "circle";
    			 String angle = "0";
    			 String size = "large";
    			 String fill = "no";
    			 String inside = "";
    			 String vertical_flip= "";
    			 Point centroid = new Point();
    		
    		 int iBuff[] = new int[ (int) (hierarchy.total() * hierarchy.channels()) ]; 
			 // [ Contour0 (next sibling num, previous sibling num, 1st child num, parent num), Contour1(...), ...
			 hierarchy.get(0, 0, iBuff); 
			
			
			 Mat mask = Mat.zeros(image.size(), CvType.CV_8UC1);
			 dImg = new DrawImage();
			 Imgproc.drawContours(mask, contours, i, new Scalar(255));
			 String title = "Countour".concat(figName);
    		//dImg.ShowImage(mask, title);
			 Moments moments = Imgproc.moments(contours.get(i));
			 centroid.x = moments.get_m10() / moments.get_m00();
			 centroid.y = moments.get_m01() / moments.get_m00();
			 if(firstCentroid.x == 0 && firstCentroid.y == 0){
				 firstCentroid.x = centroid.x;
				 firstCentroid.y = centroid.y;
				 firstCentroidObj = Character.toString((char)asciiCounter);
			 }
			 
    		 if(!Imgproc.isContourConvex(contours.get(i))){
    			 
    			 if(mMOP2f2.size().height == 12){
    				 if((iBuff[2+ 4*i] != -1 && hierarchy.total() >= 2 && iBuff[3+ 4*i] == -1)){
        				 continue;        					 
        			 }
    				 
    				 if(iBuff[2+ 4*i] != -1 && iBuff[3+ 4*i] != -1 && iBuff[3+ 4*(i-1)] != -1){
    					 continue;
    				 }
    				 
    				 shape = "plus";
    				 
    				 //Checking FILL
    				 if((iBuff[3 +4*i] != -1) ){
    					fill= "no";
    				}	
    				else{
    					fill = "yes";
    				 }
    				 
    				//Checking SIZE
    				 double area = Imgproc.contourArea(contours.get(i));
    				 	//Checking SIZE 
      				 RotatedRect rotRect = Imgproc.minAreaRect(mMOP2f2);
      				 System.out.println("Area = "+ rotRect.size.area());
    				 if (rotRect.size.area() >=20000)
    	    			 size = "XXlarge";
    				 else if (rotRect.size.area() >=13000)
    	    			 size = "Xlarge";
    				 else if (rotRect.size.area() >=8000)
    	    			 size = "large";
    				 else if(rotRect.size.area() >= 5000)
    					 size = "medium";
    				 else if (rotRect.size.area() >= 1700)
    					 size = "small";
    				 else if (rotRect.size.area() <1700)
    	    			 size = "Xsmall";
    				 
    				//Checking rotation
    				 angle = String.valueOf((int)rotRect.angle);
    				 double final_angle = roundOffAngle(rotRect.angle);
    				 angle = String.valueOf((int)final_angle);
     				 
    			 } else if(mMOP2f2.size().height == 5){
    				 if((iBuff[2+ 4*i] != -1 && hierarchy.total() >= 2 && iBuff[3+ 4*i] == -1)){
        				 continue;        					 
        			 }
    				 
    				 if(iBuff[2+ 4*i] != -1 && iBuff[3+ 4*i] != -1 && iBuff[3+ 4*(i-1)] != -1){
    					 continue;
    				 }
    				  shape = "half-arrow";
    				 
    				 //Checking FILL
    				 if((iBuff[3 +4*i] != -1) ){
    					fill= "no";
    				}	
    				else{
    					fill = "yes";
    				 }
    				 
    				
    				 double area = Imgproc.contourArea(contours.get(i));
    				 //Checking SIZE 
      				 RotatedRect rotRect = Imgproc.minAreaRect(mMOP2f2);
      				 System.out.println("Area = "+ rotRect.size.area());
    				 if (rotRect.size.area() >=20000)
    	    			 size = "XXlarge";
    				 else if (rotRect.size.area() >=13000)
    	    			 size = "Xlarge";
    				 else if (rotRect.size.area() >=8000)
    	    			 size = "large";
    				 else if(rotRect.size.area() >= 5000)
    					 size = "medium";
    				 else if (rotRect.size.area() >= 1700)
    					 size = "small";
    				 else if (rotRect.size.area() <1700)
    	    			 size = "Xsmall";
    				 
    				//Checking rotation
    				 angle = String.valueOf((int)rotRect.angle);
    				 double final_angle = roundOffAngle(rotRect.angle);
    				 angle = String.valueOf((int)final_angle);

    				 MatOfInt convexHullMatOfInt = new MatOfInt();
    				 Imgproc.convexHull( contours.get(i), convexHullMatOfInt, false );
    				 MatOfInt4 mConvexityDefectsMatOfInt4 = new MatOfInt4();
    				 Imgproc.convexityDefects(contours.get(i), convexHullMatOfInt, mConvexityDefectsMatOfInt4);
    				 
    				 ArrayList<Point> convexHullPointArrayList = new ArrayList<Point>();
    				  
    				 //ArrayList<Point> foldPts = new ArrayList<Point>();
    				 //List<Integer> cdList = mConvexityDefectsMatOfInt4.toList();
    				 Rect rect = Imgproc.boundingRect(contours.get(i));
     				 
    				  for(int k=0; k < convexHullMatOfInt.toList().size(); k++)
    			        {
    					  convexHullPointArrayList.add(contours.get(0).toList().get(convexHullMatOfInt.toList().get(k)));
    				    
    					 //foldPts.add(contours.get(0).toList().get(cdList.get(2)));
    			        }
    				 
    				  Core.rectangle(mask, rect.tl(), rect.br(), new Scalar(255, 0, 0),1, 8,0);
     				 int b = rect.width;
     				 int h = rect.height;
     				 int countPtXRights=0;
     				 int countPtXLefts = 0;
     				 int countPtYsTop=0;
     				 int countPtYsBot= 0;
     				 
    				 for (Point c : convexHullPointArrayList){
    					 if(b>h)
    					 {
    						 if (c.x > b/2){
    							 countPtXRights++;
    							 //pointing right
    						 } else if (c.x < b/2){
    							 countPtXLefts++;
    							 //pointing right
    						 } 
    						 
    						 if(c.y == 0 && c.x > b/4 && c.x < (b-b/4)){
    							 countPtYsTop++;
    						 } else if(c.y == h && c.x > b/4 && c.x < (b-b/4)){
    							 countPtYsBot++;
    						 }
    						 if(countPtXRights >=3 && countPtYsTop ==1){
    							 angle = String.valueOf(0);
    							 vertical_flip = "no";
    						 } else if(countPtXRights >=3 && countPtYsBot ==1){
    							 angle = String.valueOf(0);
    							 vertical_flip = "yes";
    						 } else if(countPtXLefts >=3 && countPtYsTop ==1){
    							 angle = String.valueOf(180);
    							 vertical_flip = "yes";
    						 } else if(countPtXLefts >=3 && countPtYsBot ==1){
    							 angle = String.valueOf(180);
    							 vertical_flip = "no";
    						 }
    					 }
    					 Core.circle(mask, c, 5, new Scalar(255,0,0));
    				 }
    		        
    				 
    				 dImg = new DrawImage();
    				 /*
    				 Point pt_top_left = new Point(0,0);
    				 Point pt_top_right = new Point(b,0);
    				 Point pt_bot_left = new Point(0,h);
    				 Point pt_bot_right = new Point(b,h);
    				 
    				
    				 double dist_top_left = Imgproc.pointPolygonTest(mMOP2f2,pt_top_left,false);
    				 double dist_top_right = Imgproc.pointPolygonTest(mMOP2f2,pt_top_right,false);
    				 
    				 double dist_bot_left = Imgproc.pointPolygonTest(mMOP2f2,pt_bot_left,false);
    				 double dist_bot_right = Imgproc.pointPolygonTest(mMOP2f2,pt_bot_right,false);
    				 
    				 if(dist_top_left== 1 && dist_top_right ==1){
    					 
    				 }
    				 */
    				 //System.out.println("The point is" + dist);
    				 Imgproc.drawContours(mask, contours, i, new Scalar(255));
    				 title = "Countour".concat(figName);
    	    		 //dImg.ShowImage(mask, title);
    				 Mat dst = new Mat();
    				  
    			 } else if(mMOP2f2.size().height == 7){
    				 //FULL ARROW
    				 if((iBuff[2+ 4*i] != -1 && hierarchy.total() >= 2 && iBuff[3+ 4*i] == -1)){
        				 continue;        					 
        			 }
    				 
    				 if(iBuff[2+ 4*i] != -1 && iBuff[3+ 4*i] != -1 && iBuff[3+ 4*(i-1)] != -1){
    					 continue;
    				 }
    				 shape = "arrow";
    				 
    				 //Checking FILL
    				 if((iBuff[3 +4*i] != -1) ){
    					fill= "no";
    				}	
    				else{
    					fill = "yes";
    				 }
    				 
    				 if(asciiCounter == 65){
    					 mA = mMOP2f2;
    				 }
    				 
    				 if(asciiCounter == 67){
    					 mC = mMOP2f2;
    				 }
    				 
    				//Checking SIZE
    				 double area = Imgproc.contourArea(contours.get(i));
    				 Rect rect = Imgproc.boundingRect(contours.get(i));
    				
    				
    				 	//Checking SIZE 
      				 RotatedRect rotRect = Imgproc.minAreaRect(mMOP2f2);
      				 System.out.println("Area = "+ rotRect.size.area());
    				 if (rotRect.size.area() >=20000)
    	    			 size = "XXlarge";
    				 else if (rotRect.size.area() >=13000)
    	    			 size = "Xlarge";
    				 else if (rotRect.size.area() >=8000)
    	    			 size = "large";
    				 else if(rotRect.size.area() >= 5000)
    					 size = "medium";
    				 else if (rotRect.size.area() >= 1700)
    					 size = "small";
    				 else if (rotRect.size.area() <1700)
    	    			 size = "Xsmall";
    				 

     				//Checking rotation
     				 angle = String.valueOf((int)rotRect.angle);
     				 double final_angle = roundOffAngle(rotRect.angle);
     				 angle = String.valueOf((int)final_angle);
     				 
     				 Core.rectangle(mask, rect.tl(), rect.br(), new Scalar(0, 255, 0),1, 8,0);
     				 int b = rect.width/10;
     				 int h = rect.height/2 + rect.height/10;
     				 dImg = new DrawImage();
     				 Point pt = new Point(b,h);
     				 
     				 double dist = Imgproc.pointPolygonTest(mMOP2f2,pt,false);
     				 Imgproc.drawContours(mask, contours, i, new Scalar(255), -1);
     				 title = "Countour".concat(figName);
     	    		 //dImg.ShowImage(mask, title);
     				 Mat dst = new Mat();
     				 
     				 if(dist == 1){
     					 angle = String.valueOf(0);
     				 } else if(dist == -1){
     					 angle = String.valueOf(180);
     				 }
     				 
      				
    				 
    				/*
    				 double dist_top_left = Imgproc.pointPolygonTest(mMOP2f2,pt_top_left,false);
    				 double dist_top_right = Imgproc.pointPolygonTest(mMOP2f2,pt_top_right,false);
    				 
    				 double dist_bot_left = Imgproc.pointPolygonTest(mMOP2f2,pt_bot_left,false);
    				 double dist_bot_right = Imgproc.pointPolygonTest(mMOP2f2,pt_bot_right,false);
    				 
    				 if(dist_top_left== 1 && dist_top_right ==1){
    					 
    				 }
    				 */   				 
    				
    				 //Core.flip(mA, dst, -1);
    				 /*double ret = Imgproc.matchShapes(mMOP2f2, dst, 1, 0.0);
    				 if(ret == 0.0){
    					 angle = String.valueOf(180);
    				 }
    				 
    				 Core.flip(mA, dst, 0);
    				 ret = Imgproc.matchShapes(mMOP2f2, dst, 1, 0.0);
    				 if(ret == 0.0){
    					 angle = String.valueOf(180);
    				 }
    				 
    				 mm
    				 
    				 Mat M_180plus = Imgproc.getRotationMatrix2D(centroid, 180, 1);
    				 Imgproc.warpAffine(mA, dst, M_180plus, contours.get(i).size());
    				 double ret = Imgproc.matchShapes(mMOP2f2, dst, 1, 0.0);
    				 if(ret == 0.0){
    					 angle = String.valueOf(180);
    				 }
    				 */
    				 
     				 
    			 } else {
    				 //PAC-MAN
    				 if((iBuff[2+ 4*i] != -1 && hierarchy.total() >= 2 && iBuff[3+ 4*i] == -1)){
        				 continue;        					 
        			 }
    				 
    				 if(iBuff[2+ 4*i] != -1 && iBuff[3+ 4*i] != -1 && iBuff[3+ 4*(i-1)] != -1){
    					 continue;
    				 }
    				 
    				 shape ="pac-man";
    				 
    				 //Checking FILL
    				 if((iBuff[3 +4*i] != -1) ){
    					fill= "no";
    				}	
    				else{
    					 fill = "yes";
    				 }
    				 
    				 
    				 
    				 	//Checking SIZE 
      				 RotatedRect rotRect = Imgproc.minAreaRect(mMOP2f2);
      				 System.out.println("Area = "+ rotRect.size.area());
    				 if (rotRect.size.area() >=20000)
    	    			 size = "XXlarge";
    				 else if (rotRect.size.area() >=12500)
    	    			 size = "Xlarge";
    				 else if (rotRect.size.area() >=8000)
    	    			 size = "large";
    				 else if(rotRect.size.area() >= 5000)
    					 size = "medium";
    				 else if (rotRect.size.area() >= 1700)
    					 size = "small";
    				 else if (rotRect.size.area() <1700)
    	    			 size = "Xsmall";
    				 
     				
    				 Point center = new Point();
    				 float[] radius = null;
     				 Imgproc.minEnclosingCircle(mMOP2f2, center, radius);
     	    		 Rect rect = Imgproc.boundingRect(contours.get(i));
     	    		 Core.rectangle(mask, rect.tl(), rect.br(), new Scalar(255, 0, 0),1, 8,0);
    				 
     	    		 int h = (int)center.y;
     	    		 int w = (int)center.x;
     	    		 
     	    		 Point pt_top_left_1 = new Point(w-5,h-10);
     	    		 Point pt_top_left_2 = new Point(w-10,h-5);
     				 Point pt_top_right_1 = new Point(w+5,h-10);
     				 Point pt_top_right_2 = new Point(w+10,h-5);
    				 Point pt_bot_left_1 =  new Point(w-5,h+10);
    				 Point pt_bot_left_2 =  new Point(w-10,h+5);
    				 Point pt_bot_right_1 = new Point(w+5,h+10);    				 
    				 Point pt_bot_right_2 = new Point(w+10,h+5); 
    				 Core.circle(mask, center, 5, new Scalar(255,0,0));
    				 Core.circle(mask, pt_top_left_1, 5, new Scalar(255,0,0));
    				 Core.circle(mask, pt_top_left_2, 5, new Scalar(255,0,0));
    				 Core.circle(mask, pt_top_right_1, 5, new Scalar(255,0,0));
    				 Core.circle(mask, pt_top_right_2, 5, new Scalar(255,0,0));
    				 Core.circle(mask, pt_bot_left_1, 5, new Scalar(255,0,0));
    				 Core.circle(mask, pt_bot_left_2, 5, new Scalar(255,0,0));
    				 Core.circle(mask, pt_bot_right_1, 5, new Scalar(255,0,0));
    				 Core.circle(mask, pt_bot_right_2, 5, new Scalar(255,0,0));    				 
    				 Imgproc.drawContours(mask, contours, i, new Scalar(255));
     				 title = "Countour".concat(figName);
     	    		 //dImg.ShowImage(mask, title);
    				 double dist_top_left_1 = Imgproc.pointPolygonTest(mMOP2f2,pt_top_left_1,false);
    				 double dist_top_left_2 = Imgproc.pointPolygonTest(mMOP2f2,pt_top_left_2,false);
    				 double dist_top_right_1 = Imgproc.pointPolygonTest(mMOP2f2,pt_top_right_1,false);
    				 double dist_top_right_2 = Imgproc.pointPolygonTest(mMOP2f2,pt_top_right_2,false);
    				 double dist_bot_left_1 = Imgproc.pointPolygonTest(mMOP2f2,pt_bot_left_1,false);
    				 double dist_bot_left_2 = Imgproc.pointPolygonTest(mMOP2f2,pt_bot_left_2,false);
    				 double dist_bot_right_1 = Imgproc.pointPolygonTest(mMOP2f2,pt_bot_right_1,false);
    				 double dist_bot_right_2 = Imgproc.pointPolygonTest(mMOP2f2,pt_bot_right_2,false);    				 
    				 
     	    		 if(dist_top_left_1 < 0 && dist_top_left_2 < 0){
     	    			 angle = String.valueOf(225);
     	    		 } else if(dist_top_right_1 < 0 && dist_top_right_2 <0){
     	    			 angle = String.valueOf(315);
     	    		 } else if(dist_bot_left_1 < 0 && dist_bot_left_2 <0){
     	    			 angle = String.valueOf(135);
     	    		 } else if(dist_bot_right_1 < 0 && dist_bot_right_2 <0){
     	    			 angle = String.valueOf(45);
     	    		 } else if(dist_top_left_2 < 0 && dist_bot_left_2 < 0 ){
     	    			 angle = String.valueOf(180);
     	    		 } else if(dist_top_right_2 < 0 && dist_bot_right_2 < 0 ){
     	    			 angle = String.valueOf(0);
     	    		 } else if(dist_top_left_1 <= 0 && dist_top_right_1 <= 0 ){
     	    			 angle = String.valueOf(270);
     	    		 } else if(dist_bot_left_1 <= 0 && dist_bot_right_1 <= 0 ){
     	    			 angle = String.valueOf(90);
     	    		 } else
     	    			 angle = String.valueOf(0);
     	    		 
     	    		/*if(dist_top_left_1 < 0 && dist_top_left_2 < 0){
    	    			 angle = String.valueOf(225);
    	    		 } else if(dist_top_right_1 < 0 && dist_top_right_2 <0){
    	    			 angle = String.valueOf(315);
    	    		 } else if(dist_bot_left_1 < 0 && dist_bot_left_2 <0){
    	    			 angle = String.valueOf(135);
    	    		 } else if(dist_bot_right_1 < 0 && dist_bot_right_2 <0){
    	    			 angle = String.valueOf(45);
    	    		 } else if(dist_top_left_1 < 0 && dist_bot_left_1 < 0 ){
    	    			 angle = String.valueOf(180);
    	    		 } else if(dist_top_right_1 < 0 && dist_bot_right_1 < 0 ){
    	    			 angle = String.valueOf(0);
    	    		 } else if(dist_top_left_2 <= 0 && dist_top_right_2 <= 0 ){
    	    			 angle = String.valueOf(270);
    	    		 } else if(dist_bot_left_2 <= 0 && dist_bot_right_2 <= 0 ){
    	    			 angle = String.valueOf(90);
    	    		 } else
    	    			 angle = String.valueOf(0);
    				 */
    			 }
    		 } else if(mMOP2f2.size().height == 4){
    			  
    			 Imgproc.approxPolyDP(mMOP2f1, mMOP2f2, 0.02 * Imgproc.arcLength(mMOP2f1, true), true); 
        		 if(mMOP2f2.size().height == 3){
        			//-------------------------------------------------------------//
        			//TRIANGLE
        			 if((iBuff[2+ 4*i] != -1 && hierarchy.total() >= 2 && iBuff[3+ 4*i] == -1)){
        				 triangleFlag = 1;
        				 triangleArea = Imgproc.contourArea(contours.get(i)); 
        				 continue;        					 
        			 }
    				 
    				 if(iBuff[2+ 4*i] != -1 && iBuff[3+ 4*i] != -1 && iBuff[3+ 4*(i-1)] != -1){
    					 triangleFlag = 1;
    					 triangleArea = Imgproc.contourArea(contours.get(i)); 
    					 continue;
    				 }
        			 
        			 shape= "triangle";
        			 
        			
        			 //Checking FILL
    				 if((iBuff[3 +4*i] != -1) ){
    					fill= "no";
    				 }	
    				 else{
    					 fill = "yes";
    				 }
    				 
    				//Check if its a rectangle	

    				 double area = Imgproc.contourArea(contours.get(i)); 
    				 Rect rect = Imgproc.boundingRect(contours.get(i));
    				 
    				 if(triangleFlag == 1 && triangleArea >= (2*area - 800) && triangleArea <= (2*area + 800)){
    					 if(Math.abs(1- ((double)rect.width/rect.height))> 0.02){
    					 //its a rect
    					 //if height/width is close to 1/3
    						 if(rect.height > rect.width){
    							 if(Math.abs(rect.height - 3*rect.width) >= 0 && Math.abs(rect.height - 3*rect.width) <20){
        						 //Its a rectangle
        						 
    							 } else if(Math.abs(rect.height - rect.width) > 40 ){
    	    						 //Its a rectangle
    	    						 if(!size.equals("small") && contours.size() <=2 && i == 1 ){
    	    							 //Determining orientation
    	    							 if(rect.height > rect.width){
    	    								 if(centroid.x > 100){
    	    									 fill = "top-left,bottom-left";
    	    								 } else if(centroid.x < 80){
    	    									 fill = "top-right,bottom-right";
    	    								 }
    	    							 }
    	    					 }
    						 } else if(rect.width > rect.height){
        						 if(Math.abs(3*rect.height - rect.width) >= 0 && Math.abs(3*rect.height - rect.width) <20){
            						 //Its a rectangle
            						 
        						 } else if(Math.abs(rect.height - rect.width) > 40 ){
            						 //Its a rectangle
            						 if(!size.equals("small") && contours.size() <=2 && i == 1 ){
            							 //Determining orientation
            							 if(rect.height > rect.width){
            								 if(centroid.x > 100){
            									 fill = "top-left,bottom-left";
            								 } else if(centroid.x < 80){
            									 fill = "top-right,bottom-right";
            								 }
            							 }
            					 }
        					 }
    						 
    					 }
    				 }
    				 }
    				 }
    				 
    				//Checking inside
    				/* if(iBuff[3+4*i] == 2){
    					 inside = String.valueOf((char)(asciiCounter-1));
    				 }
    				 if(iBuff[3+4*i] == 4){
    					 inside = inside.concat("," +String.valueOf((char)(asciiCounter-1)));
    				 }
    				 */
    				 
    				//Checking inside
    				 if(iBuff[3+4*i] == 2){
    					 if((firstCentroid.x - centroid.x) <=10  && (firstCentroid.y - centroid.y) <= 10){
    						 if(String.valueOf((char)(asciiCounter-1)).equals(firstCentroidObj))
    							 inside = firstCentroidObj;
    					 }
    				 }
    				 if(iBuff[3+4*i] == 4){
    					 if(inside.isEmpty()){
    						 inside = String.valueOf((char)(asciiCounter-1));
    					 } else
    						 inside = inside.concat("," +String.valueOf((char)(asciiCounter-1)));
    				 }
        			 
    				 	//Checking SIZE 
      				 RotatedRect rotRect = Imgproc.minAreaRect(mMOP2f2);
      				 System.out.println("Area = "+ rotRect.size.area());
    				 if (rotRect.size.area() >=20000)
    	    			 size = "XXlarge";
    				 else if (rotRect.size.area() >=13000)
    	    			 size = "Xlarge";
    				 else if (rotRect.size.area() >=8000)
    	    			 size = "large";
    				 else if(rotRect.size.area() >= 5000)
    					 size = "medium";
    				 else if (rotRect.size.area() >= 1700)
    					 size = "small";
    				 else if (rotRect.size.area() <1700)
    	    			 size = "Xsmall";
    				 
    				 //Checking ANGLE
    				 //System.out.println("ANgle: "+ rotRect.angle);
    				 //Rect rect = Imgproc.boundingRect(contours.get(i));
    				 
        			 double rightTriArea = 0.5 * rect.height * rect.width ;
        			 if(rightTriArea == area || (Math.abs(area-rightTriArea) < 300) ){
        				 if(Math.abs(rect.height- rect.width) <10){
        					 shape = "right-triangle";
        					 
        					 Point pt_top_right = new Point(image.width(), 0);
        					 Point pt_top_left = new Point(0,0);
        					 Point pt_bot_right = new Point(image.width(), image.height());
        					 Point pt_bot_left = new Point(0, image.height());
        					 
        					 double dist_top_right = Imgproc.pointPolygonTest(mMOP2f2,pt_top_right,false);
        					 double dist_top_left = Imgproc.pointPolygonTest(mMOP2f2,pt_top_left,false);
        					 double dist_bot_right = Imgproc.pointPolygonTest(mMOP2f2,pt_bot_right,false);
        					 double dist_bot_left = Imgproc.pointPolygonTest(mMOP2f2,pt_bot_left,false);
        					 
        					 
        					 Core.circle(mask, centroid, 5, new Scalar(255,0,0));
        					 Imgproc.drawContours(mask, contours, i, new Scalar(255));
             				 title = "Countour".concat(figName);
             	    		 //dImg.ShowImage(mask, title);
            				 //check centroid is in which quarter
        					 if(centroid.x > image.width()/2 && centroid.y < image.height()/2 && dist_bot_left < 0)
        						 angle = String.valueOf(180);
        					 else if(centroid.x < image.width()/2 && centroid.y < image.height()/2 && dist_bot_right < 0)
        						 angle = String.valueOf(90);
        					 else if(centroid.x > image.width()/2 && centroid.y > image.height()/2 && dist_top_left < 0)
        						 angle = String.valueOf(270);
        					 else if(centroid.x < image.width()/2 && centroid.y > image.height()/2 && dist_top_right < 0)
        						 angle = String.valueOf(0);
        					 
            				
            				
        					
            				 
        				 } else if(Math.abs(rect.width -rect.height) > 20 && rect.width > rect.height  ){
        					 shape = "right-triangle";
        					 Core.circle(mask, centroid, 5, new Scalar(255,0,0));
        					 Imgproc.drawContours(mask, contours, i, new Scalar(255));
             				 title = "Countour".concat(figName);
             	    		 //dImg.ShowImage(mask, title);
        				 	 //check centroid is in close to center or not
        					 if(centroid.x > image.width()/2-5 && centroid.x < image.width()/2+5 && centroid.y < image.height()/2-5 && centroid.y > 5)
        						 angle = String.valueOf(135);
        					 else if(centroid.x > image.width()/2-5 && centroid.x < image.width()/2+5 && centroid.y < image.height()-5 && centroid.y > image.height()/2+5)
        						 angle = String.valueOf(315);
        					 
    					 
        					 
        				 }  else if(Math.abs(rect.width -rect.height) > 20 && rect.width < rect.height  ){
        					 shape = "right-triangle";
        					 Core.circle(mask, centroid, 5, new Scalar(255,0,0));
        					 Imgproc.drawContours(mask, contours, i, new Scalar(255));
             				 title = "Countour".concat(figName);
             	    		 //dImg.ShowImage(mask, title);
        					//check centroid is in close to center or not
        					 if(centroid.x > image.width()/2+5 && centroid.x < image.width()-5 && centroid.y > image.height()/2-5 && centroid.y < image.height()/2+5)
        						 angle = String.valueOf(225);
        					 else if(centroid.x < image.width()/2-5 && centroid.x > 5 && centroid.y > image.height()/2-5 && centroid.y < image.height()/2+5)
        						 angle = String.valueOf(45);
        					 
        					 
        				 }
        			 }    	
        			 //-------------------------------------------------------------//
        		 } else {
        			 if((iBuff[2+ 4*i] != -1 && hierarchy.total() >= 2 && iBuff[3+ 4*i] == -1)){
    					 SquareFlag = 1;
        				 continue;  
        			 }
    				 
    				 if(SquareFlag != 1){
    					 if(iBuff[2+ 4*i] != -1 && iBuff[3+ 4*i] != -1 && iBuff[3+ 4*(i-1)] != -1){
    						 SquareFlag = 1;
    						 continue;
    					 }
    				 }
    	    			
    				 dImg = new DrawImage();
    				 Imgproc.drawContours(mask, contours, i, new Scalar(255));
     				 title = "Countour".concat(figName);
     	    		 //dImg.ShowImage(mask, title); 
     				 // draw the contours as a solid blob				
     				 shape= "square";
    			 
    			 
    			 
     				 //Checking FILL
     				 if((iBuff[3 +4*i] != -1) ){
     					 fill= "no";
     				 }	
     				 else{
     					 fill = "yes";
     				 }
				 
				//Checking SIZE for square
				 double area = Imgproc.contourArea(contours.get(i)); 
				 RotatedRect rotRect = Imgproc.minAreaRect(mMOP2f2); 
				 System.out.println("Area = "+ rotRect.size.area());
				 if (rotRect.size.area() >=20000)
	    			 size = "XXlarge";
				 else if (rotRect.size.area() >=13000)
	    			 size = "Xlarge";
				 else if (rotRect.size.area() >=8000)
	    			 size = "large";
				 else if(rotRect.size.area() >= 5000)
					 size = "medium";
				 else if (rotRect.size.area() >= 1700)
					 size = "small";
				 else if (rotRect.size.area() <1700)
	    			 size = "Xsmall";
				 
				 
				 /*if (rotRect.size.area() >=15000)
	    			 size = "large";
				 else if(rotRect.size.area() < 5000)
					 size = "small";
				 else if (rotRect.size.area() < 15000 && rotRect.size.area() > 5000)
					 size = "medium";
					 */
				 
				 //Check if its a rectangle				
				 Rect rect = Imgproc.boundingRect(contours.get(i));
				 if(Math.abs(1- ((double)rect.width/rect.height))> 0.02){
					 //its a rect
					 //if area become half
					 
					 if(rect.height > rect.width){
						 if(Math.abs(rect.height - 3*rect.width) >= 0 && Math.abs(rect.height - 3*rect.width) <20){
						 //Its a rectangle
						 
						 } else if(Math.abs(rect.height - rect.width) > 40 ){
						 //Its a rectangle
						 if(!size.equals("small")){
							 //Determining orientation
							 if(rect.height > rect.width){
								 if(centroid.x > 100){
									 fill = "top-left,bottom-left";
								 } else if(centroid.x < 80){
									 fill = "top-right,bottom-right";
								 }
							 } else if(rect.width > rect.height){
								 if(centroid.y > 100){
									 fill = "top-left,top-right";
								 } else if(centroid.y < 80){
									 fill = "bottom-left,bottom-right";
								 }
							 }
						 }
					 }
					 } else if(rect.width > rect.height){
						 if(Math.abs(3*rect.height - rect.width) >= 0 && Math.abs(3*rect.height - rect.width) <20){
    						 //Its a rectangle
    						 
						 }  else if(Math.abs(rect.height - rect.width) > 40 ){
							 //Its a rectangle
							 if(!size.equals("small")){
								 //Determining orientation
								 if(rect.height > rect.width){
									 if(centroid.x > 100){
										 fill = "top-left,bottom-left";
									 } else if(centroid.x < 80){
										 fill = "top-right,bottom-right";
									 }
								 } else if(rect.width > rect.height){
									 if(centroid.y > 100){
										 fill = "top-left,top-right";
									 } else if(centroid.y < 80){
										 fill = "bottom-left,bottom-right";
									 }
								 }
							 }
						 }
					 }
				 }
				 
				 
				 if(Math.abs(1- ((double)rect.width/rect.height))> 0.02){
					 //its a rect
					 //if height/width is close to 1/3
						 if(rect.height > rect.width){
							 if(Math.abs(rect.height - 3*rect.width) >= 0 && Math.abs(rect.height - 3*rect.width) <20){
    						 //Its a rectangle
    						 
							 } else if(Math.abs(rect.height - rect.width) > 40 ){
	    						 //Its a rectangle
	    						 if(!size.equals("small") && contours.size() <=2 && i == 1 ){
	    							 //Determining orientation
	    							 if(rect.height > rect.width){
	    								 if(centroid.x > 100){
	    									 fill = "top-left,bottom-left";
	    								 } else if(centroid.x < 80){
	    									 fill = "top-right,bottom-right";
	    								 }
	    							 }
	    					 }
						 } else if(rect.width > rect.height){
    						 if(Math.abs(3*rect.height - rect.width) >= 0 && Math.abs(3*rect.height - rect.width) <20){
        						 //Its a rectangle
        						 
    						 } else if(Math.abs(rect.height - rect.width) > 40 ){
        						 //Its a rectangle
        						 if(!size.equals("small") && contours.size() <=2 && i == 1 ){
        							 //Determining orientation
        							 if(rect.height > rect.width){
        								 if(centroid.x > 100){
        									 fill = "top-left,bottom-left";
        								 } else if(centroid.x < 80){
        									 fill = "top-right,bottom-right";
        								 }
        							 }
        					 }
    					 }
						 
					 }
				 }
				 }
				 
				 
				 //if only square and not centred in center
				 if(contours.size() <=2 && i == 1){
					 if(centroid.x < 60 && centroid.y < 60){
						 fill = "top-right,bottom-right,bottom-left";
					 } else if(centroid.x < 60 && centroid.y > 120){
						 fill = "top-right,bottom-right,top-left";
					 } if(centroid.x > 120 && centroid.y < 60){
						 fill = "top-left,,bottom-left,bottom-right";
					 } if(centroid.x > 120 && centroid.y > 120){
						 fill = "top-left,top-right,bottom-left";
					 } 
				 }
				 
				//Checking inside
				 if(iBuff[3+4*i] == 2){
					 if((firstCentroid.x - centroid.x) <=10  && (firstCentroid.y - centroid.y) <= 10){
						 if(String.valueOf((char)(asciiCounter-1)).equals(firstCentroidObj))
							 inside = firstCentroidObj;
					 }
				 }
				 if(iBuff[3+4*i] == 4){
					 if(inside.isEmpty()){
						 inside = String.valueOf((char)(asciiCounter-1));
					 } else
						 inside = inside.concat("," +String.valueOf((char)(asciiCounter-1)));
				 }
				 
				 
				//Checking ANGLE
    			 int rot_angle = findcalcAnglePolygon(mMOP2f2.toArray());
    			 double final_angle = roundOffAngle(rot_angle);
				 angle=  String.valueOf((int)final_angle);
				 
				 SquareFlag = 0;
        		 }
    		 } else if (mMOP2f2.size().height == 3){
    			 //TRIANGLE
    			 if((iBuff[2+ 4*i] != -1 && hierarchy.total() >= 2 && iBuff[3+ 4*i] == -1)){
    				 continue;        					 
    			 }
				 
				 if(iBuff[2+ 4*i] != -1 && iBuff[3+ 4*i] != -1 && iBuff[3+ 4*(i-1)] != -1){
					 continue;
				 }
    			 
    			 shape= "triangle";
    			 
    			
    			 //Checking FILL
				 if((iBuff[3 +4*i] != -1) ){
					fill= "no";
				 }	
				 else{
					 fill = "yes";
				 }
				 
				//Check if its a rectangle	

				 double area = Imgproc.contourArea(contours.get(i)); 
				 Rect rect = Imgproc.boundingRect(contours.get(i));
				 
				 if(triangleFlag == 1 && triangleArea >= (2*area - 800) && triangleArea <= (2*area + 800)){
				 if(Math.abs(1- ((double)rect.width/rect.height))> 0.02){
					 //its a rect
					 //if area become half
					 if(Math.abs(rect.height - rect.width) > 40 ){
						 //Its a rectangle
						 if(!size.equals("small") && contours.size() <=2 && i == 1 ){
							 //Determining orientation
							 if(rect.height > rect.width){
								 if(centroid.x > 100){
									 fill = "top-left,bottom-left";
								 } else if(centroid.x < 80){
									 fill = "top-right,bottom-right";
								 }
							 }
						 }
					 }
				 }
				 }
				 
				 
				//Checking inside
				 if(iBuff[3+4*i] == 2){
					 if((firstCentroid.x - centroid.x) <=10  && (firstCentroid.y - centroid.y) <= 10){
						 if(String.valueOf((char)(asciiCounter-1)).equals(firstCentroidObj))
							 inside = firstCentroidObj;
					 }
				 }
				 if(iBuff[3+4*i] == 4){
					 if(inside.isEmpty()){
						 inside = String.valueOf((char)(asciiCounter-1));
					 } else
						 inside = inside.concat("," +String.valueOf((char)(asciiCounter-1)));
				 }
    			 
				 //Checking SIZE
				 RotatedRect rotRect = Imgproc.minAreaRect(mMOP2f2);
  				 System.out.println("Area = "+ rotRect.size.area());
				 if (rotRect.size.area() >=20000)
	    			 size = "XXlarge";
				 else if (rotRect.size.area() >=13000)
	    			 size = "Xlarge";
				 else if (rotRect.size.area() >=8000)
	    			 size = "large";
				 else if(rotRect.size.area() >= 5000)
					 size = "medium";
				 else if (rotRect.size.area() >= 1700)
					 size = "small";
				 else if (rotRect.size.area() <1700)
	    			 size = "Xsmall";
				 
				 //Checking ANGLE
				 //System.out.println("ANgle: "+ rotRect.angle);
				
				 
    			 double rightTriArea = 0.5 * rect.height * rect.width ;
    			 if(rightTriArea == area || (Math.abs(area-rightTriArea) < 300) ){
    				 if(Math.abs(rect.height- rect.width) <10){
    					 shape = "right-triangle";
    					 
    					 Point pt_top_right = new Point(image.width(), 0);
    					 Point pt_top_left = new Point(0,0);
    					 Point pt_bot_right = new Point(image.width(), image.height());
    					 Point pt_bot_left = new Point(0, image.height());
    					 
    					 double dist_top_right = Imgproc.pointPolygonTest(mMOP2f2,pt_top_right,false);
    					 double dist_top_left = Imgproc.pointPolygonTest(mMOP2f2,pt_top_left,false);
    					 double dist_bot_right = Imgproc.pointPolygonTest(mMOP2f2,pt_bot_right,false);
    					 double dist_bot_left = Imgproc.pointPolygonTest(mMOP2f2,pt_bot_left,false);
    					 
    					 
    					 Core.circle(mask, centroid, 5, new Scalar(255,0,0));
    					 Imgproc.drawContours(mask, contours, i, new Scalar(255));
         				 title = "Countour".concat(figName);
         	    		 //dImg.ShowImage(mask, title);
        				 //check centroid is in which quarter
    					 if(centroid.x > image.width()/2 && centroid.y < image.height()/2 && dist_bot_left < 0)
    						 angle = String.valueOf(180);
    					 else if(centroid.x < image.width()/2 && centroid.y < image.height()/2 && dist_bot_right < 0)
    						 angle = String.valueOf(90);
    					 else if(centroid.x > image.width()/2 && centroid.y > image.height()/2 && dist_top_left < 0)
    						 angle = String.valueOf(270);
    					 else if(centroid.x < image.width()/2 && centroid.y > image.height()/2 && dist_top_right < 0)
    						 angle = String.valueOf(0);
    					 
        				
        				
    					
        				 
    				 } else if(Math.abs(rect.width -rect.height) > 20 && rect.width > rect.height  ){
    					 shape = "right-triangle";
    					 Core.circle(mask, centroid, 5, new Scalar(255,0,0));
    					 Imgproc.drawContours(mask, contours, i, new Scalar(255));
         				 title = "Countour".concat(figName);
         	    		 //dImg.ShowImage(mask, title);
    				 	 //check centroid is in close to center or not
    					 if(centroid.x > image.width()/2-5 && centroid.x < image.width()/2+5 && centroid.y < image.height()/2-5 && centroid.y > 5)
    						 angle = String.valueOf(135);
    					 else if(centroid.x > image.width()/2-5 && centroid.x < image.width()/2+5 && centroid.y < image.height()-5 && centroid.y > image.height()/2+5)
    						 angle = String.valueOf(315);
    					 
					 
    					 
    				 }  else if(Math.abs(rect.width -rect.height) > 20 && rect.width < rect.height  ){
    					 shape = "right-triangle";
    					 Core.circle(mask, centroid, 5, new Scalar(255,0,0));
    					 Imgproc.drawContours(mask, contours, i, new Scalar(255));
         				 title = "Countour".concat(figName);
         	    		 //dImg.ShowImage(mask, title);
    					//check centroid is in close to center or not
    					 if(centroid.x > image.width()/2+5 && centroid.x < image.width()-5 && centroid.y > image.height()/2-5 && centroid.y < image.height()/2+5)
    						 angle = String.valueOf(225);
    					 else if(centroid.x < image.width()/2-5 && centroid.x > 5 && centroid.y > image.height()/2-5 && centroid.y < image.height()/2+5)
    						 angle = String.valueOf(45);
    					 
    					 
    				 }
    			 }    			   
				 
    			 
    		 } else if (mMOP2f2.size().height < 8){
    			 Imgproc.approxPolyDP(mMOP2f1, mMOP2f2, 0.01 * Imgproc.arcLength(mMOP2f1, true), true); 
    			 if(mMOP2f2.size().height == 3){
    				 
    				//TRIANGLE
        			 if((iBuff[2+ 4*i] != -1 && hierarchy.total() >= 2 && iBuff[3+ 4*i] == -1)){
        				 continue;        					 
        			 }
    				 
    				 if(iBuff[2+ 4*i] != -1 && iBuff[3+ 4*i] != -1 && iBuff[3+ 4*(i-1)] != -1){
    					 continue;
    				 }
        			 
        			 shape= "triangle";
        			 
        			
        			 //Checking FILL
    				 if((iBuff[3 +4*i] != -1) ){
    					fill= "no";
    				 }	
    				 else{
    					 fill = "yes";
    				 }
    				 
    				//Check if its a rectangle				
    				 Rect rect = Imgproc.boundingRect(contours.get(i));
    				 if(Math.abs(1- ((double)rect.width/rect.height))> 0.02){
    					 //its a rect
    					 //if area become half
    					 if(Math.abs(rect.height - rect.width) > 40 ){
    						 //Its a rectangle
    						 if(!size.equals("small") && contours.size() <=2 && i == 1 ){
    							 //Determining orientation
    							 if(rect.height > rect.width){
    								 if(centroid.x > 100){
    									 fill = "top-left,bottom-left";
    								 } else if(centroid.x < 80){
    									 fill = "top-right,bottom-right";
    								 }
    							 }
    						 }
    					 }
    				 }
    				 
    				 
    				//Checking inside
    				 if(iBuff[3+4*i] == 2){
    					 if((firstCentroid.x - centroid.x) <=10  && (firstCentroid.y - centroid.y) <= 10){
    						 if(String.valueOf((char)(asciiCounter-1)).equals(firstCentroidObj))
    							 inside = firstCentroidObj;
    					 }
    				 }
    				 if(iBuff[3+4*i] == 4){
    					 if(inside.isEmpty()){
    						 inside = String.valueOf((char)(asciiCounter-1));
    					 } else
    						 inside = inside.concat("," +String.valueOf((char)(asciiCounter-1)));
    				 }
        			 
    				 //Checking SIZE
    				 double area = Imgproc.contourArea(contours.get(i)); 
    				 	//Checking SIZE 
      				 RotatedRect rotRect = Imgproc.minAreaRect(mMOP2f2);
      				 System.out.println("Area = "+ rotRect.size.area());
    				 if (rotRect.size.area() >=20000)
    	    			 size = "XXlarge";
    				 else if (rotRect.size.area() >=13000)
    	    			 size = "Xlarge";
    				 else if (rotRect.size.area() >=8000)
    	    			 size = "large";
    				 else if(rotRect.size.area() >= 5000)
    					 size = "medium";
    				 else if (rotRect.size.area() >= 1700)
    					 size = "small";
    				 else if (rotRect.size.area() <1700)
    	    			 size = "Xsmall";
    				 
    				 /*RotatedRect rotRect = Imgproc.minAreaRect(mMOP2f2); 
    				 if (rotRect.size.area() >=15000)
    	    			 size= "large";
    				 else if(rotRect.size.area() < 4000)
    					 size= "small";
    				 else if (rotRect.size.area() < 15000 && rotRect.size.area() > 4000)
    					 size= "medium";
    					 */
    				 
    				 //Checking ANGLE
    				 //System.out.println("ANgle: "+ rotRect.angle);
    				
    				 
        			 double rightTriArea = 0.5 * rect.height * rect.width ;
        			 if(rightTriArea == area || (Math.abs(area-rightTriArea) < 300) ){
        				 if(Math.abs(rect.height- rect.width) <10){
        					 shape = "right-triangle";
        					 
        					 Point pt_top_right = new Point(image.width(), 0);
        					 Point pt_top_left = new Point(0,0);
        					 Point pt_bot_right = new Point(image.width(), image.height());
        					 Point pt_bot_left = new Point(0, image.height());
        					 
        					 double dist_top_right = Imgproc.pointPolygonTest(mMOP2f2,pt_top_right,false);
        					 double dist_top_left = Imgproc.pointPolygonTest(mMOP2f2,pt_top_left,false);
        					 double dist_bot_right = Imgproc.pointPolygonTest(mMOP2f2,pt_bot_right,false);
        					 double dist_bot_left = Imgproc.pointPolygonTest(mMOP2f2,pt_bot_left,false);
        					 
        					 
        					 Core.circle(mask, centroid, 5, new Scalar(255,0,0));
        					 Imgproc.drawContours(mask, contours, i, new Scalar(255));
             				 title = "Countour".concat(figName);
             	    		 //dImg.ShowImage(mask, title);
            				 //check centroid is in which quarter
        					 if(centroid.x > image.width()/2 && centroid.y < image.height()/2 && dist_bot_left < 0)
        						 angle = String.valueOf(180);
        					 else if(centroid.x < image.width()/2 && centroid.y < image.height()/2 && dist_bot_right < 0)
        						 angle = String.valueOf(90);
        					 else if(centroid.x > image.width()/2 && centroid.y > image.height()/2 && dist_top_left < 0)
        						 angle = String.valueOf(270);
        					 else if(centroid.x < image.width()/2 && centroid.y > image.height()/2 && dist_top_right < 0)
        						 angle = String.valueOf(0);
        					 
            				
            				
        					
            				 
        				 } else if(Math.abs(rect.width -rect.height) > 20 && rect.width > rect.height  ){
        					 shape = "right-triangle";
        					 Core.circle(mask, centroid, 5, new Scalar(255,0,0));
        					 Imgproc.drawContours(mask, contours, i, new Scalar(255));
             				 title = "Countour".concat(figName);
             	    		 //dImg.ShowImage(mask, title);
        				 	 //check centroid is in close to center or not
        					 if(centroid.x > image.width()/2-5 && centroid.x < image.width()/2+5 && centroid.y < image.height()/2-5 && centroid.y > 5)
        						 angle = String.valueOf(135);
        					 else if(centroid.x > image.width()/2-5 && centroid.x < image.width()/2+5 && centroid.y < image.height()-5 && centroid.y > image.height()/2+5)
        						 angle = String.valueOf(315);
        					 
    					 
        					 
        				 }  else if(Math.abs(rect.width -rect.height) > 20 && rect.width < rect.height  ){
        					 shape = "right-triangle";
        					 Core.circle(mask, centroid, 5, new Scalar(255,0,0));
        					 Imgproc.drawContours(mask, contours, i, new Scalar(255));
             				 title = "Countour".concat(figName);
             	    		 //dImg.ShowImage(mask, title);
        					//check centroid is in close to center or not
        					 if(centroid.x > image.width()/2+5 && centroid.x < image.width()-5 && centroid.y > image.height()/2-5 && centroid.y < image.height()/2+5)
        						 angle = String.valueOf(225);
        					 else if(centroid.x < image.width()/2-5 && centroid.x > 5 && centroid.y > image.height()/2-5 && centroid.y < image.height()/2+5)
        						 angle = String.valueOf(45);
        					 
        					 
        				 }
        			 }    		
    				 
    			 } else if(mMOP2f2.size().height >= 4 && mMOP2f2.size().height <=5){
    				 
    				 if((iBuff[2+ 4*i] != -1 && hierarchy.total() >= 2 && iBuff[3+ 4*i] == -1)){
    					 SquareFlag = 1;
        				 continue;  
        			 }
    				 
    				 if(SquareFlag != 1){
    					 if(iBuff[2+ 4*i] != -1 && iBuff[3+ 4*i] != -1 && iBuff[3+ 4*(i-1)] != -1){
    						 SquareFlag = 1;
    						 continue;
    					 }
    				 }
    	    			
    				 dImg = new DrawImage();
    				 Imgproc.drawContours(mask, contours, i, new Scalar(255));
     				 title = "Countour".concat(figName);
     	    		 //dImg.ShowImage(mask, title); 
     				 // draw the contours as a solid blob				
     				 shape= "square";
    			 
    			 
    			 
     				 //Checking FILL
     				 if((iBuff[3 +4*i] != -1) ){
     					 fill= "no";
     				 }	
     				 else{
     					 fill = "yes";
     				 }
				 
				//Checking SIZE for square
				 double area = Imgproc.contourArea(contours.get(i)); 
				 RotatedRect rotRect = Imgproc.minAreaRect(mMOP2f2); 
				 System.out.println("Area = "+ rotRect.size.area());
				 if (rotRect.size.area() >=20000)
	    			 size = "XXlarge";
				 else if (rotRect.size.area() >=13000)
	    			 size = "Xlarge";
				 else if (rotRect.size.area() >=8000)
	    			 size = "large";
				 else if(rotRect.size.area() >= 5000)
					 size = "medium";
				 else if (rotRect.size.area() >= 1700)
					 size = "small";
				 else if (rotRect.size.area() <1700)
	    			 size = "Xsmall";
				 
				 
				 /*if (rotRect.size.area() >=15000)
	    			 size = "large";
				 else if(rotRect.size.area() < 5000)
					 size = "small";
				 else if (rotRect.size.area() < 15000 && rotRect.size.area() > 5000)
					 size = "medium";
					 */
				 
				 //Check if its a rectangle				
				 Rect rect = Imgproc.boundingRect(contours.get(i));
				 if(Math.abs(1- ((double)rect.width/rect.height))> 0.02){
					 //its a rect
					 //if area become half
					 
					 if(rect.height > rect.width){
						 if(Math.abs(rect.height - 3*rect.width) >= 0 && Math.abs(rect.height - 3*rect.width) <20){
						 //Its a rectangle
						 
						 } else if(Math.abs(rect.height - rect.width) > 40 ){
						 //Its a rectangle
						 if(!size.equals("small")){
							 //Determining orientation
							 if(rect.height > rect.width){
								 if(centroid.x > 100){
									 fill = "top-left,bottom-left";
								 } else if(centroid.x < 80){
									 fill = "top-right,bottom-right";
								 }
							 } else if(rect.width > rect.height){
								 if(centroid.y > 100){
									 fill = "top-left,top-right";
								 } else if(centroid.y < 80){
									 fill = "bottom-left,bottom-right";
								 }
							 }
						 }
					 }
					 } else if(rect.width > rect.height){
						 if(Math.abs(3*rect.height - rect.width) >= 0 && Math.abs(3*rect.height - rect.width) <20){
    						 //Its a rectangle
    						 
						 }  else if(Math.abs(rect.height - rect.width) > 40 ){
							 //Its a rectangle
							 if(!size.equals("small")){
								 //Determining orientation
								 if(rect.height > rect.width){
									 if(centroid.x > 100){
										 fill = "top-left,bottom-left";
									 } else if(centroid.x < 80){
										 fill = "top-right,bottom-right";
									 }
								 } else if(rect.width > rect.height){
									 if(centroid.y > 100){
										 fill = "top-left,top-right";
									 } else if(centroid.y < 80){
										 fill = "bottom-left,bottom-right";
									 }
								 }
							 }
						 }
					 }
				 }
				 
				 
				 if(Math.abs(1- ((double)rect.width/rect.height))> 0.02){
					 //its a rect
					 //if height/width is close to 1/3
						 if(rect.height > rect.width){
							 if(Math.abs(rect.height - 3*rect.width) >= 0 && Math.abs(rect.height - 3*rect.width) <20){
    						 //Its a rectangle
    						 
							 } else if(Math.abs(rect.height - rect.width) > 40 ){
	    						 //Its a rectangle
	    						 if(!size.equals("small") && contours.size() <=2 && i == 1 ){
	    							 //Determining orientation
	    							 if(rect.height > rect.width){
	    								 if(centroid.x > 100){
	    									 fill = "top-left,bottom-left";
	    								 } else if(centroid.x < 80){
	    									 fill = "top-right,bottom-right";
	    								 }
	    							 }
	    					 }
						 } else if(rect.width > rect.height){
    						 if(Math.abs(3*rect.height - rect.width) >= 0 && Math.abs(3*rect.height - rect.width) <20){
        						 //Its a rectangle
        						 
    						 } else if(Math.abs(rect.height - rect.width) > 40 ){
        						 //Its a rectangle
        						 if(!size.equals("small") && contours.size() <=2 && i == 1 ){
        							 //Determining orientation
        							 if(rect.height > rect.width){
        								 if(centroid.x > 100){
        									 fill = "top-left,bottom-left";
        								 } else if(centroid.x < 80){
        									 fill = "top-right,bottom-right";
        								 }
        							 }
        					 }
    					 }
						 
					 }
				 }
				 }
				 
				 
				 //if only square and not centred in center
				 if(contours.size() <=2 && i == 1){
					 if(centroid.x < 60 && centroid.y < 60){
						 fill = "top-right,bottom-right,bottom-left";
					 } else if(centroid.x < 60 && centroid.y > 120){
						 fill = "top-right,bottom-right,top-left";
					 } if(centroid.x > 120 && centroid.y < 60){
						 fill = "top-left,,bottom-left,bottom-right";
					 } if(centroid.x > 120 && centroid.y > 120){
						 fill = "top-left,top-right,bottom-left";
					 } 
				 }
				 
				//Checking inside
				 if(iBuff[3+4*i] == 2){
					 if((firstCentroid.x - centroid.x) <=10  && (firstCentroid.y - centroid.y) <= 10){
						 if(String.valueOf((char)(asciiCounter-1)).equals(firstCentroidObj))
							 inside = firstCentroidObj;
					 }
				 }
				 if(iBuff[3+4*i] == 4){
					 if(inside.isEmpty()){
						 inside = String.valueOf((char)(asciiCounter-1));
					 } else
						 inside = inside.concat("," +String.valueOf((char)(asciiCounter-1)));
				 }
				 
				 
				//Checking ANGLE
    			 int rot_angle = findcalcAnglePolygon(mMOP2f2.toArray());
    			 double final_angle = roundOffAngle(rot_angle);
				 angle=  String.valueOf((int)final_angle);
				 
				 SquareFlag = 0;
    				 
    			 }  else if(mMOP2f2.size().height >= 10){
    				 //circle
    				 if((iBuff[2+ 4*i] != -1 && hierarchy.total() >= 2 && iBuff[3+ 4*i] == -1)){
       					 CircleFlag = 1;
           				 continue;  
           			 }
       				 
       				 if(CircleFlag != 1){
       					 if(iBuff[2+ 4*i] != -1 && iBuff[3+ 4*i] != -1 && iBuff[3+ 4*(i-1)] != -1){
       						 CircleFlag = 1;
       						 continue;
       					 }
       				 }
       				 
       				 //Checking FILL
       				 if((iBuff[3 +4*i] != -1) ){
      				  		fill= "no";
      				 } else{
      				  		fill = "yes";
      				 }
       				 
       				 //Checking inside
    				 if(iBuff[3+4*i] == 2){
    					 if((firstCentroid.x - centroid.x) <=10  && (firstCentroid.y - centroid.y) <= 10){
    						 if(String.valueOf((char)(asciiCounter-1)).equals(firstCentroidObj))
    							 inside = firstCentroidObj;
    					 }
    				 }
    				 if(iBuff[3+4*i] == 4){
    					 if(inside.isEmpty()){
    						 inside = String.valueOf((char)(asciiCounter-1));
    					 } else
    						 inside = inside.concat("," +String.valueOf((char)(asciiCounter-1)));
    				 }
      				 
      				  	//Checking SIZE 
      				 RotatedRect rotRect = Imgproc.minAreaRect(mMOP2f2);
      				 System.out.println("Area = "+ rotRect.size.area());
    				 if (rotRect.size.area() >=20000)
    	    			 size = "XXlarge";
    				 else if (rotRect.size.area() >=13000)
    	    			 size = "Xlarge";
    				 else if (rotRect.size.area() >=8000)
    	    			 size = "large";
    				 else if(rotRect.size.area() >= 5000)
    					 size = "medium";
    				 else if (rotRect.size.area() >= 1700)
    					 size = "small";
    				 else if (rotRect.size.area() <1700)
    	    			 size = "Xsmall";
    				 
    				
   				 CircleFlag = 0;
    				 
    		
    			 }	 
    			 
    		 } else if (mMOP2f2.size().height >= 8){
    			 Imgproc.approxPolyDP(mMOP2f1, mMOP2f2, 0.01 * Imgproc.arcLength(mMOP2f1, true), true); 
    			 
    			 if(mMOP2f2.size().height ==8){
    				 //its an octagon
    				 
    				 shape = "octagon";
    				double area = Imgproc.contourArea(contours.get(i));
          			Rect rect = Imgproc.boundingRect(contours.get(i));
          			
          			if((iBuff[2+ 4*i] != -1 && hierarchy.total() >= 2 && iBuff[3+ 4*i] == -1)){
      					 CircleFlag = 1;
          				 continue;  
          			 }
      				 
      				 if(CircleFlag != 1){
      					 if(iBuff[2+ 4*i] != -1 && iBuff[3+ 4*i] != -1 && iBuff[3+ 4*(i-1)] != -1){
      						 CircleFlag = 1;
      						 continue;
      					 }
      				 }
      				 
      				 //Checking FILL
       				 if((iBuff[3 +4*i] != -1) ){
      				  		fill= "no";
      				  	 }	
      				   else{
      				  		fill = "yes";
      				  }
       				 
       			//Checking inside
    				 if(iBuff[3+4*i] == 2){
    					 if((firstCentroid.x - centroid.x) <=10  && (firstCentroid.y - centroid.y) <= 10){
    						 if(String.valueOf((char)(asciiCounter-1)).equals(firstCentroidObj))
    							 inside = firstCentroidObj;
    					 }
    				 }
    				 if(iBuff[3+4*i] == 4){
    					 if(inside.isEmpty()){
    						 inside = String.valueOf((char)(asciiCounter-1));
    					 } else
    						 inside = inside.concat("," +String.valueOf((char)(asciiCounter-1)));
    				 }
      				 
      				  	//Checking SIZE 
      				 RotatedRect rotRect = Imgproc.minAreaRect(mMOP2f2);
      				 System.out.println("Area = "+ rotRect.size.area());
    				 if (rotRect.size.area() >=20000)
    	    			 size = "XXlarge";
    				 else if (rotRect.size.area() >=13000)
    	    			 size = "Xlarge";
    				 else if (rotRect.size.area() >=8000)
    	    			 size = "large";
    				 else if(rotRect.size.area() >= 5000)
    					 size = "medium";
    				 else if (rotRect.size.area() >= 1700)
    					 size = "small";
    				 else if (rotRect.size.area() <1700)
    	    			 size = "Xsmall";
    				 
    				 CircleFlag = 0;
    			 }
    			 
    			 else if (mMOP2f2.size().height >=9){
        			double area = Imgproc.contourArea(contours.get(i));
         			Rect rect = Imgproc.boundingRect(contours.get(i));
         		
         			int radius = (rect.width)/2;
         			if (Math.abs(1.0 - ((double)rect.width/rect.height)) <= 0.2
           				 && Math.abs(1.0 - (area/(Math.PI * radius * radius))) <= 0.2){
           				    				
           				if(mMOP2f2.size().height >= 9){  				
           				
           				 if((iBuff[2+ 4*i] != -1 && hierarchy.total() >= 2 && iBuff[3+ 4*i] == -1)){
           					 CircleFlag = 1;
               				 continue;  
               			 }
           				 
           				 if(CircleFlag != 1){
           					 if(iBuff[2+ 4*i] != -1 && iBuff[3+ 4*i] != -1 && iBuff[3+ 4*(i-1)] != -1){
           						 CircleFlag = 1;
           						 continue;
           					 }
           				 }
           	    			
           				 //check if its filled or not?
           				 // Mat mask = Mat.zeros(image.size(), CvType.CV_8UC1);
           				 Imgproc.drawContours(mask, contours, i, new Scalar(255));
         				 title = "Countour".concat(figName);
         	    		// dImg.ShowImage(mask, title);
           				 
           				 
           				 //Checking FILL
           				 if((iBuff[3 +4*i] != -1) ){
          				  		fill= "no";
          				  	 }	
          				   else{
          				  		fill = "yes";
          				  }
           				 
           				 //Checking inside
        				 if(iBuff[3+4*i] == 2){
        					 if((firstCentroid.x - centroid.x) <=10  && (firstCentroid.y - centroid.y) <= 10){
        						 if(String.valueOf((char)(asciiCounter-1)).equals(firstCentroidObj))
        							 inside = firstCentroidObj;
        					 }
        				 }
        				 if(iBuff[3+4*i] == 4){
        					 if(inside.isEmpty()){
        						 inside = String.valueOf((char)(asciiCounter-1));
        					 } else
        						 inside = inside.concat("," +String.valueOf((char)(asciiCounter-1)));
        				 }
          				 
          				  	//Checking SIZE 
          				 RotatedRect rotRect = Imgproc.minAreaRect(mMOP2f2);
          				 System.out.println("Area = "+ rotRect.size.area());
        				 if (rotRect.size.area() >=20000)
        	    			 size = "XXlarge";
        				 else if (rotRect.size.area() >=13000)
        	    			 size = "Xlarge";
        				 else if (rotRect.size.area() >=8000)
        	    			 size = "large";
        				 else if(rotRect.size.area() >= 5000)
        					 size = "medium";
        				 else if (rotRect.size.area() >= 1700)
        					 size = "small";
        				 else if (rotRect.size.area() <1700)
        	    			 size = "Xsmall";
        				 
        				 /*if (rotRect.size.area() >=14000)
       					 	size= "large";
       				 	else if(rotRect.size.area() < 5000)
       					 	size= "small";
       				 	else if (rotRect.size.area() < 14000 && rotRect.size.area() > 4000)
       					 	size= "medium";
       					 */
       				 
       					 
           				 // output, locations of non-zero pixels
           				 //List<MatOfPoint> all_pixels = new ArrayList<MatOfPoint>();
           				 //Core.findNonZero(mask, rect.s);
           				 //Core.extractChannel(contours.get(i), dst, 1);
           				 //int channels = mMOP2f2.channels();
           				 //int chan2 = contours.get(i).channels();
           				 //int totalNoOfPixels = contours.get(i).rows() * contours.get(i).cols();
           				 //int zeroPixels = totalNoOfPixels- Core.countNonZero(mask);
           				 //Core.findNonZero(src, idx);
       				 
       				 CircleFlag = 0;
           			 }
           		   } 
        			 
        		 }
    			
    		 }
    		 
    		 
    		 char character = (char)asciiCounter;    		 
			 int flagCenter = 0;
			 RavensObject newObject = new RavensObject(Character.toString(character));
			
			 String above = "";
			 String left_of = "";
			 
			 if(firstCentroid.x > 85 && firstCentroid.x< 110  && firstCentroid.y >85 && firstCentroid.y < 120){
    			 flagCenter =1;
    		 }
	    	 if(centroid.x >85 && centroid.x < 110 && centroid.y >85 && centroid.y < 120){
				 //no need of adding above or left-of: the figure is in center
	    			    		 
			 } else if(centroid.x < 75 && centroid.y <75){
				//top-left
				if((firstCentroid.x - centroid.x) >=40  && (firstCentroid.y - centroid.y) >= 40){
	    			 if(flagCenter == 0){
	    				 above = firstCentroidObj;
	    			 }
	    		 }
			 } else if(centroid.x < 75 && centroid.y >125){
				//bottom left
				if((firstCentroid.x - centroid.x) >=40  && (centroid.y - firstCentroid.y) >= 40){
	    			 if(flagCenter == 0){
	    				 above = firstCentroidObj;
	    			 }
	    		}
			 } else if(centroid.x > 125 && centroid.y <75){
					//top-right
				 if((centroid.x - firstCentroid.x) >=40  && (firstCentroid.y - centroid.y) >= 40){
	    			 if(flagCenter == 0){
	    				 above = firstCentroidObj;
	    			 }
	    		}
			 } else if(centroid.x > 125 && centroid.y >125){
					//bottom right
				 if((centroid.x - firstCentroid.x) >=40  && (centroid.y - firstCentroid.y) >= 40){
	    			 if(flagCenter == 0){
	    				 above = firstCentroidObj;
	    			 }
	    		}
			 }
	    	 
	    	 
	    	 
	    	 //CHECKING LEFT-OF
	    	 if(centroidMap.size()>0){
	    		 left_of = "";
	    		 above = "";
	    		 inside = "";
	    		 for (Map.Entry<String, Point> entry : centroidMap.entrySet()) { 
	    			 String objName = entry.getKey();
	    			 Point cent = entry.getValue();
	    			 String res = "";
	    			 //CHECK LEFT_OF
	    			 if(Math.abs(centroid.x -cent.x) > 40){
	    				 if(cent.x > centroid.x){
	    	    			 res= objName;
	    	    			 if(!left_of.isEmpty())
	    	    				 left_of = left_of.concat(","+res);
	    	    			 else
	    	    				 left_of = res;
	    				 } else if(cent.x < centroid.x){ 
	    					 res = String.valueOf((char)(asciiCounter));
	    					 
	    							 for(RavensObject obj: currentFigure.getObjects()){
	    								 if(obj.getName().equals(objName)){
	    									 
	    									 	int found = 0;
	    										for(RavensAttribute attr : obj.getAttributes()){
	    												 if(attr.getName().equals("left-of")){
	    													 found = 1;
	    													 if(!attr.getValue().isEmpty()){
	    														 attr.setValue(attr.getValue().concat(","+res));
	    													 } else {
	    														 attr.setValue(res);
	    													 }	    													 
	    												 }
	    										}
	    										if(found == 0)
	    											 obj.getAttributes().add(new RavensAttribute("left-of", res));	    										 
	    								 }
	    							 }    					 
	    				 }
	    			 }
	    			 //CHECK ABOVE
	    			 if(Math.abs(centroid.y -cent.y) > 40){
	    				 if(cent.y > centroid.y){
	    	    			 res= objName;
	    	    			 if(!above.isEmpty())
	    	    				 above = above.concat(","+res);
	    	    			 else
	    	    				 above = res;
	    				 } else if(cent.y < centroid.y){ 
	    					 res = String.valueOf((char)(asciiCounter));
	    					
	    							 for(RavensObject obj: currentFigure.getObjects()){
	    								 if(obj.getName().equals(objName)){
	    									 
	    									 
	    									 	int found = 0;
	    											 for(RavensAttribute attr : obj.getAttributes()){
	    												 if(attr.getName().equals("above")){
	    													 found = 1;
	    													 if(!attr.getValue().isEmpty()){
	    														 attr.setValue(attr.getValue().concat(","+res));
	    													 } else {
	    														 attr.setValue(res);
	    													 }	    													 
	    												 }
	    											 }
	    										  if(found == 0) {
	    											 obj.getAttributes().add(new RavensAttribute("above", res));
	    										 }
	    										 
	    								 }
	    							 }	    					 
	    				 }
	    			 }
	    			 
	    			 if(!shape.equals("triangle") && !shape.equals("right-triangle")){
	    			 	if(Math.abs(centroid.y -cent.y) < 10 && Math.abs(centroid.x -cent.x) < 10){
	    	    			 res= objName;
	    	    			 if(!inside.isEmpty())
	    	    				 inside = inside.concat(","+res);
	    	    			 else
	    	    				 inside = res;
	    			 	}
	    			 } else {
	    				 if(Math.abs(centroid.y -cent.y) < 30 && Math.abs(centroid.x -cent.x) < 30){
	    					 res= objName;
	    	    			 if(!inside.isEmpty())
	    	    				 inside = inside.concat(","+res);
	    	    			 else
	    	    				 inside = res;
	    				 }
	    			 }
	    			 
	    		 }
	    	 }
					
	    	 centroidMap.put(String.valueOf((char)asciiCounter), centroid);
	    	 
	    	 /*if(Math.abs(firstCentroid.y -centroid.y) > 40){
	    		 if(firstCentroid.y > centroid.y)
	    			 above = firstCentroidObj;
	    		 else if(centroid.y > firstCentroid.y)
	    			 above = String.valueOf((char)asciiCounter);
	    	 }
	    	 */
	    	 
	    	 if(left_of.length()>0){
	    		 newObject.getAttributes().add(new RavensAttribute("left-of", left_of));
	    	 }
	    	 
	    	 //CHECKING VERTICAL FLIP
	    	 if(vertical_flip.length()>0){
	    		 newObject.getAttributes().add( new RavensAttribute("vertical-flip", vertical_flip));
	    	 }
	    	 
	    	 //GETTING ABOVE
	    	 if(flagCenter == 0 && !shape.equals("pac-man") && above.length() >0)
	    		 newObject.getAttributes().add( new RavensAttribute("above", above));
	    	 
	    	 //GETTING INSIDE
	    	    	 
	    	 /*if(!inside.isEmpty()){
	    		 if((firstCentroid.x - centroid.x) <=10  && (firstCentroid.y - centroid.y) <= 10){
	    			 if(left_of.isEmpty() && above.isEmpty()){
	    				 inside = firstCentroidObj;
	    				 newObject.getAttributes().add( new RavensAttribute("inside", inside));
	    			 }
	    			
	    		 }
	    		 
	    	 }
	    	 */
	    	 
	    	 if(!inside.isEmpty()){
	    		 newObject.getAttributes().add( new RavensAttribute("inside", inside));
	    	 }
    		 if(!shape.equals(null))
    			 newObject.getAttributes().add( new RavensAttribute("shape", shape));
    		 if(!fill.equals(null))
    			 newObject.getAttributes().add( new RavensAttribute("fill", fill));
    		 if(!size.equals(null))
    			 newObject.getAttributes().add( new RavensAttribute("size", size));
    		 if(!angle.equals(null))
    			 newObject.getAttributes().add( new RavensAttribute("angle", angle));
    		 	
    			 currentFigure.getObjects().add(newObject);
    			 currentObject = newObject;
    			 if(asciiCounter >= 90)
    				 asciiCounter = 76;
    			 asciiCounter++;
    			 flagCenter = 0;
    	 }
    	
    	
    	return currentFigure;
	}
	
    /**
     * The primary method for solving incoming Raven's Progressive Matrices.
     * For each problem, your Agent's Solve() method will be called. At the
     * conclusion of Solve(), your Agent should return a String representing its
     * answer to the question: "1", "2", "3", "4", "5", or "6". These Strings
     * are also the Names of the individual RavensFigures, obtained through
     * RavensFigure.getName().
     * 
     * In addition to returning your answer at the end of the method, your Agent
     * may also call problem.checkAnswer(String givenAnswer). The parameter
     * passed to checkAnswer should be your Agent's current guess for the
     * problem; checkAnswer will return the correct answer to the problem. This
     * allows your Agent to check its answer. Note, however, that after your
     * agent has called checkAnswer, it will *not* be able to change its answer.
     * checkAnswer is used to allow your Agent to learn from its incorrect
     * answers; however, your Agent cannot change the answer to a question it
     * has already answered.
     * 
     * If your Agent calls checkAnswer during execution of Solve, the answer it
     * returns will be ignored; otherwise, the answer returned at the end of
     * Solve will be taken as your Agent's answer to this problem.
     * 
     * @param problem the VisualRavensProblem your agent should solve
     * @return your Agent's answer to this problem
     */
    public String Solve(VisualRavensProblem problem) {
    	
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    	if(problem.getProblemType().equals("3x3") || problem.getProblemType().equals("3x3 (Image)")){
    		String pathA = problem.getFigures().get("A").getPath();
        	String pathB = problem.getFigures().get("B").getPath();
        	String pathC = problem.getFigures().get("C").getPath();
        	String pathD = problem.getFigures().get("D").getPath();
        	String pathE = problem.getFigures().get("E").getPath();
        	String pathF = problem.getFigures().get("F").getPath();
        	String pathG = problem.getFigures().get("G").getPath();
        	String pathH = problem.getFigures().get("H").getPath();
        	
        	
        	Mat imageA = Highgui.imread(pathA, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        	Mat imageB = Highgui.imread(pathB, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        	Mat imageC = Highgui.imread(pathC, Highgui.CV_LOAD_IMAGE_GRAYSCALE);        	
        	Mat imageD = Highgui.imread(pathD, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        	Mat imageE = Highgui.imread(pathE, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        	Mat imageF = Highgui.imread(pathF, Highgui.CV_LOAD_IMAGE_GRAYSCALE);        	
        	Mat imageG = Highgui.imread(pathG, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        	Mat imageH = Highgui.imread(pathH, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        	
        	RavensProblem newProblem = new RavensProblem(problem.getName(), problem.getProblemType(), "1");
    		//dImg = new DrawImage();
    		
    		RavensFigure mapA= recognizeImage(imageA, "A");
    		for(RavensObject obj: mapA.getObjects()){
    		System.out.print(obj.getName());
    		for(RavensAttribute attr: obj.getAttributes()){
    			System.out.println(attr.getName() + " : " +attr.getValue());
    		}    		
    		}
    		System.out.println("==================");
    		
    		RavensFigure mapB= recognizeImage(imageB, "B");  
    		for(RavensObject obj: mapB.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
        		
    		System.out.println("==================");
    		RavensFigure mapC= recognizeImage(imageC, "C");
    		for(RavensObject obj: mapC.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
        		
    		System.out.println("==================");
    		RavensFigure mapD= recognizeImage(imageD, "D");  
    		for(RavensObject obj: mapD.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
        		
    		System.out.println("==================");
    		RavensFigure mapE= recognizeImage(imageE, "E");
    		for(RavensObject obj: mapE.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
        		
    		System.out.println("==================");
    		RavensFigure mapF= recognizeImage(imageF, "F");   
    		for(RavensObject obj: mapF.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
    		System.out.println("==================");
        		
    		RavensFigure mapG= recognizeImage(imageG, "G");
    		for(RavensObject obj: mapG.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
    		System.out.println("==================");
        		
    		RavensFigure mapH= recognizeImage(imageH, "H");
    		for(RavensObject obj: mapH.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
    		System.out.println("==================");
    		System.out.println("==OPTIONS=========");
    		System.out.println("==================");
        		
    		
    		RavensFigure map1= recognizeImage(Highgui.imread(problem.getFigures().get("1").getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE), "1");
    		for(RavensObject obj: map1.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
    		System.out.println("==================");
    		RavensFigure map2= recognizeImage(Highgui.imread(problem.getFigures().get("2").getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE), "2");
    		for(RavensObject obj: map2.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
    		System.out.println("==================");
    		RavensFigure map3= recognizeImage(Highgui.imread(problem.getFigures().get("3").getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE), "3");
    		for(RavensObject obj: map3.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
    		System.out.println("==================");
    		RavensFigure map4= recognizeImage(Highgui.imread(problem.getFigures().get("4").getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE), "4");
    		for(RavensObject obj: map4.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
    		System.out.println("==================");
    		RavensFigure map5= recognizeImage(Highgui.imread(problem.getFigures().get("5").getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE), "5");
    		for(RavensObject obj: map5.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
    		System.out.println("==================");
    		RavensFigure map6= recognizeImage(Highgui.imread(problem.getFigures().get("6").getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE), "6");
    		for(RavensObject obj: map6.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
    		System.out.println("==================");
    		newProblem.getFigures().put(mapA.getName(), mapA);
    		newProblem.getFigures().put(mapB.getName(), mapB);
    		newProblem.getFigures().put(mapC.getName(), mapC);
    		newProblem.getFigures().put(mapD.getName(), mapD);
    		newProblem.getFigures().put(mapE.getName(), mapE);
    		newProblem.getFigures().put(mapF.getName(), mapF);
    		newProblem.getFigures().put(mapG.getName(), mapG);
    		newProblem.getFigures().put(mapH.getName(), mapH);
    		
    		newProblem.getFigures().put(map1.getName(), map1);
    		newProblem.getFigures().put(map2.getName(), map2);
    		newProblem.getFigures().put(map3.getName(), map3);
    		newProblem.getFigures().put(map4.getName(), map4);
    		newProblem.getFigures().put(map5.getName(), map5);
    		newProblem.getFigures().put(map6.getName(), map6);
    	
    		CustomAgent customAgent = new CustomAgent();
    		String answer = customAgent.CustomSolve(newProblem);
    	
    		//reset ascii to 65 again
    		asciiCounter = 65;
    		return answer;
    
    	} else {
    		String pathA = problem.getFigures().get("A").getPath();
    		String pathB = problem.getFigures().get("B").getPath();
    		String pathC = problem.getFigures().get("C").getPath();
    	
    	
    		Mat imageA = Highgui.imread(pathA, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
    		Mat imageB = Highgui.imread(pathB, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
    		Mat imageC = Highgui.imread(pathC, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
    	
    		RavensProblem newProblem = new RavensProblem(problem.getName(), problem.getProblemType(), "1");
    		//dImg = new DrawImage();
    		asciiCounter = 65;
    	
    		RavensFigure mapA= recognizeImage(imageA, "A");
    		for(RavensObject obj: mapA.getObjects()){
    		System.out.print(obj.getName());
    		for(RavensAttribute attr: obj.getAttributes()){
    			System.out.println(attr.getName() + " : " +attr.getValue());
    		}    		
    		}
    		 
    		RavensFigure mapB= recognizeImage(imageB, "B");
    		for(RavensObject obj: mapB.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
    		RavensFigure mapC= recognizeImage(imageC, "C");
    		for(RavensObject obj: mapC.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
    	
    		RavensFigure map1= recognizeImage(Highgui.imread(problem.getFigures().get("1").getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE), "1");
    		for(RavensObject obj: map1.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
    		RavensFigure map2= recognizeImage(Highgui.imread(problem.getFigures().get("2").getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE), "2");
    		for(RavensObject obj: map2.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
    		RavensFigure map3= recognizeImage(Highgui.imread(problem.getFigures().get("3").getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE), "3");
    		for(RavensObject obj: map3.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
    		RavensFigure map4= recognizeImage(Highgui.imread(problem.getFigures().get("4").getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE), "4");
    		for(RavensObject obj: map4.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
    		RavensFigure map5= recognizeImage(Highgui.imread(problem.getFigures().get("5").getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE), "5");
    		for(RavensObject obj: map5.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
    		RavensFigure map6= recognizeImage(Highgui.imread(problem.getFigures().get("6").getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE), "6");
    		for(RavensObject obj: map6.getObjects()){
        		System.out.print(obj.getName());
        		for(RavensAttribute attr: obj.getAttributes()){
        			System.out.println(attr.getName() + " : " +attr.getValue());
        		}    		
        		}
    	
    		newProblem.getFigures().put(mapA.getName(), mapA);
    		newProblem.getFigures().put(mapB.getName(), mapB);
    		newProblem.getFigures().put(mapC.getName(), mapC);
    		newProblem.getFigures().put(map1.getName(), map1);
    		newProblem.getFigures().put(map2.getName(), map2);
    		newProblem.getFigures().put(map3.getName(), map3);
    		newProblem.getFigures().put(map4.getName(), map4);
    		newProblem.getFigures().put(map5.getName(), map5);
    		newProblem.getFigures().put(map6.getName(), map6);
    	
    		CustomAgent customAgent = new CustomAgent();
    		String answer = customAgent.CustomSolve(newProblem);
    	
    		//reset ascii to 65 again
    		asciiCounter = 65;
    		return answer;
    		}
    	}
}
