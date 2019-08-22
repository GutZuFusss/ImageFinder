package gutzufusss.wrapper;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gutzufusss.Main;
import gutzufusss.util.Logger;
import net.sourceforge.lept4j.*;
import net.sourceforge.lept4j.util.LeptUtils;
import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.ITessAPI.TessBaseAPI;

public class OCRWrapper {
	private final String WHITELIST_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "ƒ÷‹‰ˆ¸ﬂ" + "1234567890" + " !?.,-+#*/\\\"$Ä()[]{}<>=%ß";
	
	private Main controller;
	
	public OCRWrapper(Main controller) {
		this.controller = controller;
	}

	private BufferedImage openImg(String path) {
		File f = new File(path);
		BufferedImage img = null;
		try {
			img = ImageIO.read(f);
		} catch(IOException e) {
			controller.getLogger().log(Logger.LVL_ERROR, "I/O error: " + e.getMessage());
		}

		return img;
	}
	
	private void setUpAPIParameters(TessBaseAPI handle) {
		TessAPI1.TessBaseAPISetVariable(handle, "--psm", "7"); // page segmentation mode: treat image like a single line of text ; TODO: play with this value, could be good
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
		if(directoryListing != null) {
			for(File child : directoryListing)
				getTextFromImg(child.getAbsolutePath(), handle);
		}
		else {
			controller.getLogger().log(Logger.LVL_ERROR, "I/O error: The directory seems to be empty!");
		}
		
		controller.getLogger().log(Logger.LVL_INFO, "Done scanning the directory '" + path + "'.");

		TessAPI1.TessBaseAPIEnd(handle); // clean up
	}

	private String getTextFromImg(String imgPath, TessBaseAPI handle) {
		BufferedImage processingImg = openImg(imgPath);

		// image preprocessing (maybe change order a bit)
		processingImg = controller.getIMGManipulator().toGrayscale(processingImg); //only this = 549
		//processingImg = ImageHelper.convertImageToBinary(processingImg); // this doesn't help much since we will often be facing complex backgrounds
		processingImg = controller.getIMGManipulator().smoothImg(processingImg);
		processingImg = controller.getIMGManipulator().addBorder(processingImg, 6);
		//processingImg = manager.getIMGManipulator().performSWT(processingImg);
//		JOptionPane.showInputDialog(null, "lel", "diss", JOptionPane.QUESTION_MESSAGE, new ImageIcon(processingImg), null, ""); // DEBUG
//		processingImg = manager.getIMGManipulator().changeContrast(processingImg, 0.1f);
//		JOptionPane.showInputDialog(null, "lel", "diss", JOptionPane.QUESTION_MESSAGE, new ImageIcon(processingImg), null, ""); // DEBUG
		
		// finalize the image
		Pix pix = controller.getIMGManipulator().img2Pix(processingImg);
		pix.xres = processingImg.getHeight(); // converting to pix somehow breaks the resolution
		pix.yres = processingImg.getWidth();

		TessAPI1.TessBaseAPISetImage2(handle, pix); // hand over the processed image to the api
		
		LeptUtils.dispose(pix); // clean up
		
		// save result into the database
		File fileInfo = new File(imgPath);
		int conf = TessAPI1.TessBaseAPIMeanTextConf(handle);
		String result = TessAPI1.TessBaseAPIGetUTF8Text(handle).getString(0);
		if(result.length() > SQLWrapper.MAX_IMG_TEXT_LEN) { // i don't think it's possible to overflow varchar anyways, but i am not sure anymore
			result = result.substring(0, SQLWrapper.MAX_IMG_TEXT_LEN);
			controller.getLogger().log(Logger.LVL_WARN, "Result was longer than " + SQLWrapper.MAX_IMG_TEXT_LEN + ", theirfore it has been trimmed to that length.");
		}
		SQLWrapper.execSQL("INSERT INTO " + SQLWrapper.TABLE_IMG + " (name, abs_path, ocr_data, confidence) VALUES (" +
					"'" + fileInfo.getName()			+ "', " +
					"'" + fileInfo.getAbsolutePath()	+ "', " +
					"'" + result						+ "', " +
						  conf							+ ");");

		if(conf < 50)
			controller.getLogger().log(Logger.LVL_WARN, "Processed '" + imgPath + 
					"'. However, the confidence score was lower than 50 (" + conf + 
					") that's why you are seeing this warning.");
		
		controller.getLogger().log(Logger.LVL_INFO, "'" + imgPath + "' done, confidence was " + conf + ".");
		controller.getLogger().log(Logger.LVL_INFO, "Result: " + result);

		return result;
	}
}
