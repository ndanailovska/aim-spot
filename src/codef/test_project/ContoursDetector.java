/**
 * 
 */
package codef.test_project;

import java.io.File;
import java.util.Random;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvMemStoragePos;
import com.googlecode.javacv.cpp.opencv_imgproc;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvPoint3D32f;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_core.CvContour;

import com.googlecode.javacv.cpp.opencv_features2d.FeatureDetector;
import com.googlecode.javacv.cpp.opencv_features2d.KeyPoint;

/**
 * @author Natasha
 *
 */
public class ContoursDetector {

	 public static void main(String[] args) {
		 IplImage src = cvLoadImage("imgs/cont2.png",1);
		 IplImage gray = cvCreateImage(cvSize(src.width(), src.height()), IPL_DEPTH_8U, 1); 
		 cvCvtColor(src, gray, CV_RGB2GRAY );
		 cvSmooth(gray,gray,opencv_imgproc.CV_GAUSSIAN,9,9,2,2);
		 //cvSmooth(gray, gray, CV_GAUSSIAN, 7, 7); 
	
		 IplImage cc_img = cvCreateImage(cvGetSize(gray), gray.depth(), 3); 
		 cvSetZero(cc_img);
		 //CvScalar(ext_color);
	
		 cvCanny(gray, gray, 10, 30, 3); 
	
		 CvMemStorage storage = cvCreateMemStorage(0);
		 CvSeq circles = cvHoughCircles(gray, storage, CV_HOUGH_GRADIENT, 1, src.height()/6, 100, 50, 0, 80);
		 cvCvtColor(gray, src, CV_GRAY2BGR);
		 for (int i = 0; i < circles.total(); i++)
		 {   
			  CvPoint3D32f point = new CvPoint3D32f(cvGetSeqElem(circles, i));
				
			  float xyr[] = {point.x(),point.y(),point.z()};
		      CvPoint center = new CvPoint(Math.round(xyr[0]), Math.round(xyr[1]));
		
		      int radius = Math.round(xyr[2]);
	
		      // draw the circle center
		      //cvCircle(cc_img, center, 3, CV_RGB(0,255,0), -1, 8, 0 );
	
		      // draw the circle outline
		      cvCircle(cc_img, center, radius+1, CV_RGB(0,0,255), 2, 8, 0 );
	
		 }   
	
		 CvMemStorage mem = CvMemStorage.create();
		 CvSeq contours = new CvSeq();// = new CvSeq();
		 cvCvtColor(cc_img, gray, CV_BGR2GRAY);
		 // Use either this:
		 int n = cvFindContours(gray, mem, contours, Loader.sizeof(CvContour.class), CV_RETR_CCOMP, CV_CHAIN_APPROX_NONE, cvPoint(0,0));
		 // Or this:
		 //int n = cvFindContours(gray, mem, &contours, sizeof(CvContour), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE, cvPoint(0,0));
	
		 Random rand = new Random();
		/* for (int i=0; contours != null && i<contours.total(); i++)
		 {
			 CvScalar ext_color = CV_RGB( rand.nextInt(255), rand.nextInt(255),rand.nextInt(255)); //randomly coloring different contours
		     cvDrawContours(cc_img, contours, ext_color, CV_RGB(0,0,0), -1, CV_FILLED, 8, cvPoint(0,0));
		 }*/
		 for (int i = 0; i < contours.capacity(); i++) {
			 CvScalar ext_color = CV_RGB( rand.nextInt(255), rand.nextInt(255),rand.nextInt(255)); //randomly coloring different contours
			 //CvSeq seq = (CvSeq) cvGetSeqElem(contours, i);
			 cvDrawContours(cc_img, (CvSeq) cvGetSeqElem(contours, i), ext_color, CV_RGB(0,0,0), -1, CV_FILLED, 8, cvPoint(0,0));
				
		 }
		 
		 /*
		 for (; contours != null ; contours = contours.h_next())
		 {
		     CvScalar ext_color = CV_RGB( rand.nextInt(255), rand.nextInt(255),rand.nextInt(255)); //randomly coloring different contours
		     cvDrawContours(cc_img, contours, ext_color, CV_RGB(0,0,0), -1, CV_FILLED, 8, cvPoint(0,0));
		 }
		 */
	
		String path = "result_imgs_cont";
		File photo=new File(path, "cont2_result.jpg");
		
	    if (photo.exists()) 
	    {
	        photo.delete();
	    }
	    cvSaveImage("result_imgs_cont/cont2_result.jpg", cc_img);
	 }
}
