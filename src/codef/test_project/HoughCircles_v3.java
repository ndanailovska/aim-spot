package codef.test_project;


import java.io.File;

import com.googlecode.javacpp.Pointer;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvPoint3D32f;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

public class HoughCircles_v3 {
	
	static int hi_switch_value[];
	static int hiInt = 0;
	static int lo_switch_value[];
	static int loInt = 0;

	public static void main(String[] args) {
		for(int i = 0; i < args.length; i++)
		{
			  //File[] files = new File(args[i]).listFiles();
			  //showFiles(files);
			detectBalls(new File(args[i]));
		}
	}

	public static void showFiles(File[] files) {
	    for (File file : files) {
	        if (file.isDirectory()) {
	            System.out.println("Directory: " + file.getName());
	            // showFiles(file.listFiles()); // Calls same method again.
	        } else {
	            System.out.println("File: " + file.getName());
	            detectBalls(file);
	        }
	    }
	}
	
    public static void detectBalls(File img_file) {
		//IplImage img = cvLoadImage(img_file.getAbsolutePath(),1);
	    //IplImage gray = cvCreateImage( cvSize( img.width(), img.height() ), IPL_DEPTH_8U, 1);

	    IplImage orig = cvLoadImage(img_file.getAbsolutePath(),1);
	    int key = 0;
        
        cvNamedWindow("circles", CV_WINDOW_NORMAL | CV_WINDOW_KEEPRATIO);
        cvNamedWindow("snooker", CV_WINDOW_NORMAL | CV_WINDOW_KEEPRATIO);
        
        double hi = 255; 
        double lo = 255;
         
        CvTrackbarCallback hi_callback = new CvTrackbarCallback() {
        	 @Override public void call( int position ){
         		hiInt = position;
         	 }
        };
        /*
    	public void switch_callback_hi( int position ){
    		hiInt = position;
    	} */
        
        CvTrackbarCallback lo_callback = new CvTrackbarCallback() {
        	 @Override public void call( int position ){
          		hiInt = position;
          	 }
            /*
        	public void switch_callback_lo( int position ){
        		loInt = position;
        	}
        	*/
        };

        cvCreateTrackbar( "hi", "circles", hi_switch_value, 4, hi_callback);
    	cvCreateTrackbar( "lo", "circles", lo_switch_value, 4, lo_callback );
	   // createTrackbar("lo", "circles", &lo, 255);

    	do
    	{
	        // update display and snooker, so we can play with them
	        IplImage display = orig.clone();//cvCreateImage( cvSize( orig.width(), orig.height() ), IPL_DEPTH_8U, 1);
			
	        IplImage snooker = cvCreateImage( cvSize( orig.width(), orig.height() ), IPL_DEPTH_8U, 1);
	        cvCvtColor(orig, snooker, CV_RGB2GRAY);

	        Pointer circles = CvMemStorage.create();
	        //cvHoughCircles(gray, circles, CV_HOUGH_GRADIENT, 2.5d, (double)gray.height()/30, 70d, 100d, 0, 80);
	        // also preventing crash with hi, lo threshold here...
		    CvSeq seq = cvHoughCircles(snooker, circles, CV_HOUGH_GRADIENT, 2, 32.0, hi > 0 ? hi : 1, lo > 0 ? lo : 1, 0, 80);
		    
		    for( int i = 0; i < seq.total(); i++ )
	        {
		    	 CvPoint3D32f point = new CvPoint3D32f(cvGetSeqElem(seq, i));
		  		
		         float xyr[] = {point.x(),point.y(),point.z()};
	             CvPoint center = new CvPoint(Math.round(xyr[0]), Math.round(xyr[1]));
	             int radius = Math.round(xyr[2]);

	             // draw the green circle center
	             cvCircle( display, center, 3, CV_RGB(0,255,0), -1, 8, 0 );

	             // draw the blue circle outline
	             cvCircle( display, center, radius, CV_RGB(255,0,0), 3, 8, 0 );
	        }
		    
		    cvShowImage( "circles", display );
		    cvShowImage("snooker", snooker);
		    key = cvWaitKey(33);
	   } while((char)key != 27);
	   return;
	}
}
