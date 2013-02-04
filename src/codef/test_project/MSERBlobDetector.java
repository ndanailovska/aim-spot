/**
 * 
 */
package codef.test_project;

import java.io.File;

import com.googlecode.javacv.CanvasFrame;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import com.googlecode.javacv.cpp.opencv_features2d.FeatureDetector;
import com.googlecode.javacv.cpp.opencv_features2d.KeyPoint;

//import cv2
//import math
//import numpy as np

/**
 * @author Natasha
 *
 */
public class MSERBlobDetector {
		
	public static boolean supress(KeyPoint x, KeyPoint keypoints){
		for(int i = 0; i < keypoints.capacity(); i++) {
		    KeyPoint kp = keypoints.position(i);
		    float distx = kp.pt_x() - x.pt_x(); 
		    float disty = kp.pt_y() - x.pt_y(); 
		    float dist = (float) Math.sqrt(distx*distx + disty*disty);
		    if(kp.size() > x.size() && dist < kp.size()/2){
		    	return true;
		    }
		}
		return false;

	}
	
	 public static void main(String[] args) {


		 CvScalar d_red =  new CvScalar(150, 55, 65, 0);
		 CvScalar l_red = new CvScalar(250, 200, 200, 0);

		 IplImage img = cvLoadImage("imgs/mserBlob3.jpg",1);
	     IplImage gray = cvCreateImage( cvSize( img.width(), img.height() ), IPL_DEPTH_8U, 1);
	
	     cvCvtColor(img, gray, CV_RGB2GRAY );
	   
		 //orig = cv2.imread("c.jpg")
		 //img = orig.copy()
		 //img2 = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

	     FeatureDetector detector = FeatureDetector.create("MSER"); 
		 //detector = opencv_core.FeatureDetector_create("MSER");
	     KeyPoint keypoints = new KeyPoint();
	     detector.detect(gray, keypoints, null);
	     
		 // fs.sort(key = lambda x: -x.size)

	    KeyPoint sfs = new KeyPoint();
	 	for(int i = 0; i < keypoints.capacity(); i++) {
		    KeyPoint kp = keypoints.position(i);
		    if(!supress(kp, keypoints))
		    {
		    	sfs.put(kp);
		    }
	 	}
	  
	 	for(int i = 0; i < sfs.capacity(); i++) {
			 KeyPoint f = sfs.position(i);
			 CvPoint center = new CvPoint((int)f.pt_x(), (int)f.pt_y());
			 cvCircle(img, center, (int)f.size()/2, d_red, 2, CV_AA, 0);
		     //cv2.circle(img, (int(f.pt[0]), int(f.pt[1])), int(f.size/2), d_red, 2, CV_AA);
			 cvCircle(img, center, (int)f.size()/2, l_red, 1, CV_AA, 0);
		     //cv2.circle(img, (int(f.pt[0]), int(f.pt[1])), int(f.size/2), l_red, 1, CV_AA);
		}
	 	 //cvCircle(gray, center, radius, CvScalar.BLUE, 3, 8, 0);
		         
		 //int h = img.height();
		 //int w = img.width();
		 //IplImage vis = cvCreateImage( cvSize( h, w*2+5 ), IPL_DEPTH_8U, 1);
	 			//IplImage vis = cvCreateImage( cvSize( gray.width(), gray.height() ), IPL_DEPTH_8U, 3);
		 //int vis = np.zeros((h, w*2+5), np.uint8)
	 			//cvCvtColor(gray, vis, CV_GRAY2RGB);
		 //vis = cv2.cvtColor(vis, cv2.COLOR_GRAY2BGR)
		 
		 //vis[:h, :w] = orig
		 //vis[:h, w+5:w*2+5] = img

	    String path = "result_imgs";
	    File photo=new File(path, "mser_3.jpg");
	
	    if (photo.exists()) 
	    {
	        photo.delete();
	    }
	   cvSaveImage("result_imgs_mser/mser_3.jpg", gray);
		 
		 
	    // create image window named "My Image"
		CanvasFrame canvas = new CanvasFrame("Result Image");

	    // request closing of the application when the image window is closed
	    canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

	    // show image on window
	   canvas.showImage(img);
	 }
}
