package gutzufusss.wrapper;

import java.awt.image.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

import gutzufusss.ImageDBController;
import gutzufusss.Main;
import gutzufusss.util.Logger;
import net.sourceforge.lept4j.*;
import net.sourceforge.lept4j.util.LeptUtils;
import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.ITessAPI.TessBaseAPI;

public class OCRWrapper {
	private final String WHITELIST_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "ƒ÷‹‰ˆ¸ﬂ" + "1234567890" + " !?.,-+#*/\\\"$Ä()[]{}<>=%ß";
	
	private final int CRITICAL_CONF = 55;
	
	private Logger logger;
	private Main controller;
	private ImageDBController imgDB;
	
	private String[] allowedExtensions = {"jpg", "png", "tiff", "bmp", "pnm", "gif", "ps", "pdf", "webp"};
	
	public OCRWrapper(Logger logger, Main controller, ImageDBController imgDB) {
		this.logger = logger;
		this.controller = controller;
		this.imgDB = imgDB;
	}

	private BufferedImage openImg(String path) {
		File f = new File(path);
		BufferedImage img = null;
		try {
			img = ImageIO.read(f);
		} catch(IOException e) {
			logger.log(Logger.LVL_ERROR, "I/O error: " + e.getMessage());
		}

		return img;
	}
	
	private void setUpAPIParameters(TessBaseAPI handle) {
		TessAPI1.TessBaseAPISetVariable(handle, "--psm", "6"); // page segmentation mode: assume a single uniform block of code ; TODO: play with this value, could be good
		TessAPI1.TessBaseAPISetVariable(handle, "--oem", "1"); // engine mode: LSTM neural net mode
		TessAPI1.TessBaseAPISetVariable(handle, "tessedit_char_whitelist", WHITELIST_CHARS); // whitelist...
		TessAPI1.TessBaseAPISetVariable(handle, "enable_new_segsearch", "1"); // enable new segmentation search path
	}

	public void scanDirectory(String path) {
		String dataPath = "tessdata"; // these two could be parameterized in the future for different directories
		String languages = "eng+deu+ita+spa";
		
		logger.log(Logger.LVL_INFO, "Starting scanning process... Languages: " + languages + ", data path: " + dataPath);

		// initialize tesseract instances
		TessBaseAPI handle = TessAPI1.TessBaseAPICreate();
		TessAPI1.TessBaseAPIInit3(handle, dataPath, languages);
		setUpAPIParameters(handle);

		// loop over all files in directory
		File[] directoryListing = getImagesInDir(new File(path));
		if(directoryListing != null && directoryListing.length != 0) {
			for(File child : directoryListing)
				getTextFromImg(child.getAbsolutePath(), handle);
		}
		else {
			logger.log(Logger.LVL_ERROR, "I/O error: The directory seems to contain no image files!");
		}
		
		logger.log(Logger.LVL_INFO, "Done scanning the directory '" + path + "'.");

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

		// do some post processing &save result into the database
		File fileInfo = new File(imgPath);
		int conf = TessAPI1.TessBaseAPIMeanTextConf(handle);
		String result = TessAPI1.TessBaseAPIGetUTF8Text(handle).getString(0);
		result = result.replaceAll("\\r\\n|\\r|\\n", " "); // screw linebreaks, srsly
		if(result.length() > imgDB.MAX_IMG_TEXT_LEN) { // i don't think it's possible to overflow varchar anyways, but i am not too sure anymore
			result = result.substring(0, imgDB.MAX_IMG_TEXT_LEN);
			logger.log(Logger.LVL_WARN, "Result was longer than " + imgDB.MAX_IMG_TEXT_LEN + ", theirfore it has been trimmed to that length.");
		}
		imgDB.execSQL("INSERT INTO " + imgDB.TABLE_IMG + " (name, abs_path, ocr_data, confidence) VALUES (" + // TODO: move this to the ImageDBController
					"'" + fileInfo.getName()			+ "', " +
					"'" + fileInfo.getAbsolutePath()	+ "', " +
					"'" + result						+ "', " +
						  conf							+ ");");

		if(conf < CRITICAL_CONF)
			logger.log(Logger.LVL_WARN, "Processed '" + imgPath + 
					"'. However, the confidence score was lower than " + CRITICAL_CONF + " (" + conf + 
					") that's why you are seeing this warning.");

		logger.log(Logger.LVL_INFO, "'" + imgPath + "' done, confidence was " + conf + ".");
		logger.log(Logger.LVL_INFO, "Result: " + result);

		return result;
	}
	
	private File[] getImagesInDir(File dir) {
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
		    	for(String ext : allowedExtensions)
		    		if(name.endsWith(ext))
		    			return true;
		    	return false;
		    }
		};

		return dir.listFiles(filter);
	}
}
