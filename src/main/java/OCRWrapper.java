import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import net.sourceforge.lept4j.*;
import net.sourceforge.lept4j.util.LeptUtils;
import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.ITessAPI.TessBaseAPI;
import net.sourceforge.tess4j.util.ImageHelper;

public class OCRWrapper {
	private final String WHITELIST_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "ƒ÷‹‰ˆ¸ﬂ" + "1234567890" + " !?.,-+#*/\\\"$Ä()[]{}<>'=%ß";
	
	private ImageManipulation imgManipulator;
	public int test = 0;
	
	
	
	public OCRWrapper() {
		imgManipulator = new ImageManipulation();
	}

	public static void main(String[] args) {
		OCRWrapper ow = new OCRWrapper();
		ow.scanDirectory("test_images");
		System.out.println("RESULT: " + ow.test);
	}

	private BufferedImage openImg(String path) {
		File f = new File(path);
		BufferedImage img = null;
		try {
			img = ImageIO.read(f);
		} catch (IOException e) {
			// TODO logg to gui here (need a GUI first, heh)
			e.printStackTrace();
		}

		return img;
	}
	
	private void setUpAPIParameters(TessBaseAPI handle) {
		TessAPI1.TessBaseAPISetVariable(handle, "--psm", "6"); // page segmentation mode: assume a single uniform block of code
		TessAPI1.TessBaseAPISetVariable(handle, "--oem", "1"); // engine mode: LSTM neural net mode
		TessAPI1.TessBaseAPISetVariable(handle, "tessedit_char_whitelist", WHITELIST_CHARS); // whitelist...
		TessAPI1.TessBaseAPISetVariable(handle, "enable_new_segsearch", "1"); // enable new segmentation search path
	}

	public void scanDirectory(String path) {
		String dataPath = "tessdata"; // these two could be parameterized in the future for different directories
		String languages = "eng+deu+ita+spa";

		// initialize tesseract instances
		TessBaseAPI handle = TessAPI1.TessBaseAPICreate();
		TessAPI1.TessBaseAPIInit3(handle, dataPath, languages);
		setUpAPIParameters(handle);

		// loop over all files in directory
		File dir = new File(path);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				getTextFromImg(child.getAbsolutePath(), handle);
			}
		} else {
			// TODO: loggimoggi
		}

		TessAPI1.TessBaseAPIEnd(handle); // clean up
	}

	private String getTextFromImg(String imgPath, TessBaseAPI handle) {
		BufferedImage processingImg = openImg(imgPath);

		// image preprocessing (maybe change order a bit)
		processingImg = imgManipulator.toGrayscale(processingImg); //only this = 549
		//processingImg = ImageHelper.convertImageToBinary(processingImg); // this doesn't help much since we will often be facing complex backgrounds
		processingImg = imgManipulator.smoothImg(processingImg);
		processingImg = imgManipulator.addBorder(processingImg, 6);
		//processingImg = imgManipulator.performSWT(processingImg);
//		JOptionPane.showInputDialog(null, "lel", "diss", JOptionPane.QUESTION_MESSAGE, new ImageIcon(processingImg), null, ""); // DEBUG
//		processingImg = imgManipulator.changeContrast(processingImg, 0.1);
//		JOptionPane.showInputDialog(null, "lel", "diss", JOptionPane.QUESTION_MESSAGE, new ImageIcon(processingImg), null, ""); // DEBUG
		
		// finalize the image
		Pix pix = imgManipulator.img2Pix(processingImg);
		pix.xres = processingImg.getHeight(); // converting to pix somehow breaks the resolution somehow
		pix.yres = processingImg.getWidth();

		TessAPI1.TessBaseAPISetImage2(handle, pix); // hand over the processed image to the api
		
		LeptUtils.dispose(pix); // clean up

		int conf = TessAPI1.TessBaseAPIMeanTextConf(handle);
		test += conf;
		String result = TessAPI1.TessBaseAPIGetUTF8Text(handle).getString(0);
		System.out.println("conf: " + conf + "\n" + result); // in the future warn here if confidence is too low

		return result;
	}
}
