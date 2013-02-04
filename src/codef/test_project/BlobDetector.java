/**
 * 
 */
package codef.test_project;

import static com.googlecode.javacpp.Loader.sizeof;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.awt.Color;
import java.util.Random;

import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

/**
 * @author Natasha
 *
 */
public class BlobDetector {
	static String sourcePath = "imgs/blob1.jpg";
	static String targetPath = "result_imgs_blob/blob1_result.jpg";
	
	public static void main (String args[]){
	    IplImage image = cvLoadImage(sourcePath);
	    IplImage grayImage = cvCreateImage(cvSize( image.width(), image.height() ), IPL_DEPTH_8U, 1);
	    cvCvtColor(image, grayImage, CV_BGR2GRAY);
	    cvSaveImage("result_imgs_blob/blob1_gray.jpg", grayImage);
	
	    CvMemStorage mem;
	    CvSeq contours = new CvSeq();
	    CvSeq ptr = new CvSeq();
	    cvThreshold(grayImage, grayImage, 150, 255, CV_THRESH_BINARY);
	    mem = cvCreateMemStorage(0);
	
	    cvFindContours(grayImage, mem, contours, sizeof(CvContour.class) , CV_RETR_CCOMP, CV_CHAIN_APPROX_SIMPLE, cvPoint(0,0));
	
	    Random rand = new Random();
	    for (ptr = contours; ptr != null; ptr = ptr.h_next()) {
	       Color randomColor = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
	       CvScalar color = CV_RGB( randomColor.getRed(), randomColor.getGreen(), randomColor.getBlue());
	       cvDrawContours(grayImage, ptr, color, color, -1, CV_FILLED, 8, cvPoint(0,0));
	       // cvDrawContours(grayImage, ptr, CvScalar.RED, CvScalar.GREEN, -1, CV_FILLED, 8, cvPoint(0,0));
	    }
	    cvSaveImage(targetPath, grayImage);
	}	
}
