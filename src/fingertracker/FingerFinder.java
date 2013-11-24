package fingertracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class FingerFinder {
	private final FingerParams params;
	private final Mat mask;
	private final Mat hierarchy;
	private final Size size;
	
	public FingerFinder(FingerParams params, Size size) {
		this.params = params;
		this.size = size;
		this.mask = new Mat(size, CvType.CV_8UC1);
		this.hierarchy = new Mat(size, CvType.CV_8UC1);
	}
	
	public void findYCrCb(Mat src, Mat dst) {
		List<Mat> channelHists = new ArrayList<>();
		List<MatOfPoint> contours = new ArrayList<>();
		List<MatOfFloat> channelRanges = Arrays.asList(new MatOfFloat(0,255), new MatOfFloat(0,255), new MatOfFloat(0,255));
		int scale = params.downsample.getValue().intValue();
		Scalar lower = new Scalar(0,params.crLow.getValue(),params.cbLow.getValue(),0);
		Scalar upper = new Scalar(255,params.crHigh.getValue(),params.cbHigh.getValue(),255);
		double maskBlend = params.maskBlend.getValue();
		
		// Down sample mask for performance
		Imgproc.resize(src, mask, new Size(src.width()/scale, src.height()/scale));
		Imgproc.GaussianBlur(mask, mask, new Size(13,13), params.gaussSigma.getValue());
		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2YCrCb);
		Scalar mean = Core.mean(mask);
		if (params.overlayBlend.getValue() > 0) {
			ImageUtils.computeChannelHistograms(mask, channelHists, 256, channelRanges);
		}
		Core.inRange(mask, lower, upper, mask);
		Imgproc.morphologyEx(mask, mask,Imgproc.MORPH_CLOSE, Mat.ones(5, 5, CvType.CV_8U));
		// Up sample mask for display
		Imgproc.resize(mask, mask, dst.size());
		
		
		// Find the largest contour
		Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		int idx = ImageUtils.largestContourIdx(contours);
		
		src.copyTo(dst);
		Imgproc.drawContours(dst, contours, idx, new Scalar(255,0,255), -1); // Area
		Imgproc.drawContours(dst, contours, idx, new Scalar(255,255,255), 1); // Outline
		
		Core.addWeighted(src, 1-maskBlend, dst, maskBlend, 0, dst);
		
		if (params.overlayBlend.getValue() > 0) {
			List<String> titles = Arrays.asList(
					String.format("Y [%.1f]", mean.val[0]), 
					String.format("Cr [%.1f]", mean.val[1]),
					String.format("Cb [%.1f]", mean.val[2])); 
		    ImageUtils.displayHistograms(channelHists, dst, titles, (int)(0.3*dst.height()), params.overlayBlend.getValue());
		}
	}
	
	
	public void find(Mat src, Mat dst) {
		if (params.enableFilters.getValue()) {
			findYCrCb(src, dst);
		} else {
			src.copyTo(dst);
		}
	}

	public Mat createDst() {
		return Mat.zeros(size, CvType.CV_8UC3);
	}
}
/*
Core.merge(channels, mask);

Mat hist = new Mat();
MatOfInt histSize = new MatOfInt(30,32);
MatOfFloat ranges = new MatOfFloat(0,180,0,255);
Imgproc.calcHist(Arrays.asList(mask), new MatOfInt(0,1), new Mat(), hist, histSize, ranges);
Core.inRange(mask,
			new Scalar(params.hLow.getValue(),params.sLow.getValue(),params.vLow.getValue(),0), // lower bound 
			new Scalar(params.hHigh.getValue(),params.sHigh.getValue(),params.vHigh.getValue(),255), // upper bound 
			mask);
	Imgproc.morphologyEx(mask, mask,Imgproc.MORPH_OPEN, Mat.ones(5, 5, CvType.CV_8U));
*/