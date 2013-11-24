package fingertracker.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.opencv.core.Mat;

public class ImageViewerUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	
	public ImageViewerUI() {
		super();
	}

	public static BufferedImage matToBufferedImage(Mat matrix) {
		int cols = matrix.cols();
		int rows = matrix.rows();
		int elemSize = (int) matrix.elemSize();
		byte[] data = new byte[cols * rows * elemSize];
		int type;
		matrix.get(0, 0, data);
		switch (matrix.channels()) {
		case 1:
			type = BufferedImage.TYPE_BYTE_GRAY;
			break;
		case 3:
			type = BufferedImage.TYPE_3BYTE_BGR;
			byte b;
			for (int i = 0; i < data.length; i = i + 3) {
				b = data[i];
				data[i] = data[i + 2];
				data[i + 2] = b;
			}
			break;
		default:
			return null;
		}
		BufferedImage image = new BufferedImage(cols, rows, type);
		image.getRaster().setDataElements(0, 0, cols, rows, data);
		return image;
	}
	
	public void setImage(Mat mat) {
		this.image = matToBufferedImage(mat);
		this.setPreferredSize(new Dimension(mat.width(), mat.height()));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(image,0,0,null);
	}
}
