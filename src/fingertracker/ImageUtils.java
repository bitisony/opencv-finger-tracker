package fingertracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageUtils {

	/**
	 * Apply the 2D map point-wise to every pixel using channel1 and channel2
	 * from the source Mat <code>src</code>
	 * 
	 * @param map
	 * @param src
	 * @param dst
	 *            Output array
	 */
	public static void map2D(Mat map, Mat src, Mat dst) {
		assert src.channels() == 2;
		assert map.channels() == dst.channels();

		int width = src.width();
		int height = src.height();
		double[] val;
		double[] prob;
		int i, j;
		for (i = 0; i < height; i++) {
			for (j = 0; j < width; j++) {
				val = src.get(i, j);
				prob = map.get((int) val[0], (int) val[1]);
				dst.put(i, j, prob);
			}
		}
	}

	public static void drawHistogram(Mat hist, Mat out, Scalar color, String title) {
		assert hist.rows() == out.cols();
		MinMaxLocResult minMax = Core.minMaxLoc(hist);
		double maxVal = minMax.maxVal;
		int scaleY = out.height() - 1;
		int rows = hist.rows();
		double curr;
		Point pt1, pt2;
		for (int i = 0; i < rows; i++) {
			curr = hist.get(i, 0)[0];
			pt1 = new Point(i, 0);
			pt2 = new Point(i, curr / maxVal*(scaleY));
			Core.rectangle(out, pt1, pt2, color);
		}
		
		Core.flip(out, out, 0);
		Core.putText(out, String.format("%s", title), 
				new Point(10,10), Core.FONT_HERSHEY_SIMPLEX, 0.4, new Scalar(255,255,255));
	}
	
	/**
	 * Overlay the image overlay on out with the blend factor alpha starting at location r0,c0
	 * @param overlay
	 * @param out
	 * @param r0
	 * @param c0
	 * @param alpha
	 */
	public static void overlayImage(Mat overlay, Mat out, int r0, int c0, double alpha) {
		assert overlay.channels() == out.channels(): "Overlay and output must have the same number of channels";
		int rows = overlay.rows();
		int cols = overlay.cols();
		int maxRows = out.rows();
		int maxCols = out.cols();
		double beta = 1 - alpha;
		int r, c;
		double[] overlayVal, outVal;
		for (int i=0; i<rows; i++) {
			for (int j=0; j<cols; j++) {
				r = i+r0;
				c = j+c0;
				if (r >= 0 && r < maxRows && c >=0 && c < maxCols) {
					overlayVal = overlay.get(i,j);
					outVal = out.get(r,c);
					for (int k=0; k<overlayVal.length; k++) {
						outVal[k] = outVal[k] * beta + overlayVal[k] * alpha;
					}
					out.put(r, c, outVal);
				}
			}
		}
	}

	public static void computeChannelHistograms(Mat src, List<Mat> channelHists, int width, List<MatOfFloat> ranges) {
		assert ranges.size() >= channelHists.size(): "Ranges must have the same length as channels";
		List<Mat> channels = new ArrayList<>();
	    Core.split(src, channels);
	    Mat hist;
	    int i = 0;
	    for (Mat channel : channels) {
	    	MatOfFloat range = ranges.get(i);
	    	hist = new Mat();
	    	Imgproc.calcHist(Arrays.asList(channel), new MatOfInt(0), new Mat(), hist, new MatOfInt(width), range);
	    	channelHists.add(hist);
	    	i++;
	    }
	}

	private static final Scalar[] colors = {new Scalar(255,0,0,0), new Scalar(0,255,0,0), new Scalar(0,0,255,0)};
	
	public static void displayHistograms(List<Mat> hists, Mat dst, List<String> titles, int height, double blend) {
		Mat histOverlay;
		int h = height / hists.size();
		int r = 0;
		for (Mat hist : hists) {
		    histOverlay = Mat.zeros(new Size(hist.rows(),h), CvType.CV_8UC3);
		    drawHistogram(hist, histOverlay, colors[r], titles.get(r));
		    
		    overlayImage(histOverlay, dst, r*h, 0, blend);
		    r++;
	    }
	}
	
	public static int largestContourIdx(List<MatOfPoint> contours) {
		double largestArea = 0, area;
		int idxLargest = 0;
		Mat contour;
		for (int i=0; i<contours.size(); i++) {
			contour = contours.get(i);
			area = Imgproc.contourArea(contour);
			if (area > largestArea) {
				largestArea = area;
				idxLargest = i;
			}
		}
		return idxLargest;
	}
}
