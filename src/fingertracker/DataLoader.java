package fingertracker;

import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class DataLoader {
	
	public static Mat read2DMatrix(InputStream input, int dim1Size, int dim2Size) throws Exception {		
		double[] arr = new double[dim1Size*dim2Size];
		DataInputStream in = new DataInputStream(input);
		int n = 0;
		while (in.available() > 0 && n < (dim1Size * dim2Size)) {
			byte[] bytes = new byte[8];
			for (int i=0; i<8; i++) {
				bytes[i] = in.readByte();
			}
			arr[n] = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN ).getDouble();
			n++;
		}
		in.close();
		Mat mat = new Mat(dim1Size, dim2Size, CvType.CV_32FC1);
		mat.put(0,0,arr);
		return mat;
	}
}
