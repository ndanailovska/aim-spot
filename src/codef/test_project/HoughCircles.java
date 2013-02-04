/**
 * 
 */
package codef.test_project;

/**
 * @author Natasha
 *
 */

import java.io.File;

import com.googlecode.javacpp.Pointer;
import com.googlecode.javacv.cpp.opencv_imgproc;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

public class HoughCircles {
	
    public static void main(String[] args) {
	IplImage img = cvLoadImage("imgs/photo6.png",1);
	IplImage gray = cvCreateImage( cvSize( img.width(), img.height() ), IPL_DEPTH_8U, 1);
	
	cvCvtColor(img, gray, CV_RGB2GRAY );
	cvSetImageROI(gray, cvRect(0, (int)(img.height()*.15), (int)img.width(), (int)(img.height()-(img.height()*.20))));
	cvSmooth(gray,gray,opencv_imgproc.CV_GAUSSIAN,9,9,2,2);
	
	Pointer circles = CvMemStorage.create();        
	CvSeq seq = cvHoughCircles(gray, circles, CV_HOUGH_GRADIENT, 2.5d, (double)gray.height()/30, 70d, 100d, 0, 80);
	
	for(int j=0; j<seq.total(); j++) {
		CvPoint3D32f point = new CvPoint3D32f(cvGetSeqElem(seq, j));
		
		float xyr[] = {point.x(),point.y(),point.z()};
		CvPoint center = new CvPoint(Math.round(xyr[0]), Math.round(xyr[1]));
		
		int radius = Math.round(xyr[2]);
		cvCircle(gray, center, 3, CvScalar.GREEN, -1, 8, 0);
		cvCircle(gray, center, radius, CvScalar.BLUE, 3, 8, 0);
	}
	
	String path = "result_imgs";
	File photo = new File(path, "photo6_2.jpg");
	if (photo.exists()) {
		photo.delete();
	}
	cvSaveImage("result_imgs/photo6_2.jpg", gray);
   }
   
}
