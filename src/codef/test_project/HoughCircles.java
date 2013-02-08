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
		for(int i = 0; i < args.length; i++)
		{
			  File[] files = new File(args[i]).listFiles();
			  showFiles(files);
		}
	}

	public static void showFiles(File[] files) {
	    for (File file : files) {
	        if (file.isDirectory()) {
	            System.out.println("Directory: " + file.getName());
	            showFiles(file.listFiles()); // Calls same method again.
	        } else {
	            System.out.println("File: " + file.getName());
	            detectBalls(file);
	        }
	    }
	}
	
    public static void detectBalls(File img_file) {
		IplImage img = cvLoadImage(img_file.getAbsolutePath(),1);
	    IplImage gray = cvCreateImage( cvSize( img.width(), img.height() ), IPL_DEPTH_8U, 1);
	
	    cvCvtColor(img, gray, CV_RGB2GRAY );
	    cvSetImageROI(gray, cvRect(0, (int)(img.height()*.15), (int)img.width(), (int)(img.height()-(img.height()*.20))));
	    cvSmooth(gray,gray,opencv_imgproc.CV_GAUSSIAN,9,9,2,2);
	
	    Pointer circles = CvMemStorage.create();        
	    CvSeq seq = cvHoughCircles(gray, circles, CV_HOUGH_GRADIENT, 2.5d, (double)gray.height()/30, 70d, 100d, 20, 100);
	
	    for(int j=0; j<seq.total(); j++){
	        CvPoint3D32f point = new CvPoint3D32f(cvGetSeqElem(seq, j));
	
	        float xyr[] = {point.x(),point.y(),point.z()};
	        CvPoint center = new CvPoint(Math.round(xyr[0]), Math.round(xyr[1]));
	
	        int radius = Math.round(xyr[2]);
	        cvCircle(gray, center, 3, CvScalar.GREEN, -1, 8, 0);
	        cvCircle(gray, center, radius, CvScalar.BLUE, 3, 8, 0);
	    }
	    
	    
	    String path = img_file.getParent();
	    File photo=new File(path + "\\results\\", img_file.getName() + "_result.jpg");
	
	    if (photo.exists()) 
	    {
	        photo.delete();
	    }
	   cvSaveImage(path + "\\results\\" + img_file.getName() + "_result.jpg", gray);
	   System.out.println("Path: " + path + "\\results\\" + img_file.getName() + "_result.jpg");
   }
}
