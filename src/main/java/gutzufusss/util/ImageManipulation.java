package gutzufusss.util;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openimaj.image.processing.edges.StrokeWidthTransform;

import gutzufusss.Main;
import net.sourceforge.lept4j.Pix;
import net.sourceforge.lept4j.util.LeptUtils;
import net.sourceforge.tess4j.util.ImageHelper;

public class ImageManipulation {
	private Main controller;
	
	public ImageManipulation(Main controller) {
		this.controller = controller;
		
		// this is needed to use opencv (C:\Program Files\Java\jre1.8.0_221\bin)
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		nu.pattern.OpenCV.loadLibrary();
	}

	// START_MISC_FUNCTIONS
	public Pix img2Pix(BufferedImage img) {
		Pix pix = null;
		try {
			pix = LeptUtils.convertImageToPix(img);
		} catch(IOException e) {
			controller.getLogger().log(Logger.LVL_ERROR, "I/O error: " + e.getMessage());
		}

		return pix;
	}

	private Mat imgToMat(BufferedImage img) { // kinda hacky, but i can't find another way... should be efficient
												// however
		int type = img.getType() == BufferedImage.TYPE_3BYTE_BGR ? CvType.CV_8UC3 : CvType.CV_8UC1;
		Mat mat = new Mat(img.getHeight(), img.getWidth(), type);
		byte[] data = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);

		return mat;
	}

	private BufferedImage matToImg(Mat mat) {
		int type = mat.channels() > 1 ? BufferedImage.TYPE_3BYTE_BGR : BufferedImage.TYPE_BYTE_GRAY;
		byte[] b = new byte[mat.channels() * mat.cols() * mat.rows()];
		mat.get(0, 0, b); // get all the pixels
		BufferedImage img = new BufferedImage(mat.cols(), mat.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);

		return img;
	}
	// END_MISC_FUNCTIONS


	// below are the functions that actually manipulate the images content itself

	public BufferedImage smoothImg(BufferedImage img) {
		Mat imgMat = imgToMat(img);
		Mat newMat = new Mat(imgMat.rows(), imgMat.cols(), imgMat.type());
		Imgproc.bilateralFilter(imgMat, newMat, 24, 32.0, 8.0, 0);

		return matToImg(newMat);
	}
	
	public BufferedImage changeContrast(BufferedImage img, float offset) {
		Mat imgMat = imgToMat(img);
		Mat newMat = new Mat(imgMat.rows(), imgMat.cols(), imgMat.type());
		imgMat.convertTo(newMat, -1, 1 + offset, 0);

		return matToImg(newMat);
	}
	
	public BufferedImage performSWT(BufferedImage img) {
		FImage fImg = ImageUtilities.createFImage(img);
		StrokeWidthTransform swt = new StrokeWidthTransform(false, new CannyEdgeDetector());
		swt.processImage(fImg);
		fImg = StrokeWidthTransform.normaliseImage(fImg);
		DisplayUtilities.display(fImg);
		
		return ImageUtilities.createBufferedImage(fImg);
	}

	public BufferedImage toGrayscale(BufferedImage img) {
		return ImageHelper.convertImageToGrayscale(img);
	}
	
	public BufferedImage addBorder(BufferedImage img, int sz) {
		Mat imgMat = imgToMat(img);
		Mat newMat = new Mat(imgMat.rows(), imgMat.cols(), imgMat.type());
		Imgproc.copyMakeBorder(imgMat, newMat, sz, sz, sz, sz, Imgproc.BORDER_CONSTANT);

		return matToImg(newMat);
	}
}
