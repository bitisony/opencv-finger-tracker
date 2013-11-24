package fingertracker;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

import fingertracker.ui.ImageViewerUI;
import fingertracker.ui.ParamsPanelUI;

public class Main {

	public static void main(String[] args) throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		final Mat probSkin = DataLoader.read2DMatrix(Main.class.getResourceAsStream("/prob_skin.dat"), 181, 256);
		final Mat src = new Mat();
		
		final VideoCapture capture = new VideoCapture(0);
		if (!capture.isOpened()) {
			throw new Exception("Unable to open Video Capture device 0");
		}
		final ImageViewerUI viewer = new ImageViewerUI();
		capture.read(src);
		viewer.setPreferredSize(new Dimension(src.width(), src.height()));
		
		final FingerParams params = new FingerParams(probSkin);
		final FingerFinder finder = new FingerFinder(params, src.size());
		final Mat dst = finder.createDst();
		final JFrame frame = new JFrame();
		
		
		final Timer timer = new Timer(30, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				capture.read(src);
				Core.flip(src, src, 1);
				finder.find(src,dst);
				viewer.setImage(dst);
				frame.repaint();
			}
		});
		
		SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				/*
				params.addListener(new ParamChangedListener() {
					@Override
					public void paramUpdated() {
						finder.find(src,dst);
						viewer.setImage(dst);
						frame.repaint();
					}
				});
				*/
				
				frame.setLayout(new BorderLayout());
				frame.setSize(640, 480);
				frame.add(viewer, BorderLayout.CENTER);
				frame.add(new ParamsPanelUI(params.allParams), BorderLayout.EAST);
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				frame.pack();
				frame.setVisible(true);	
				timer.start();
			}
		});
	}
}
