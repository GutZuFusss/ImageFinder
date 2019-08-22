
public class Main {
	private OCRWrapper ocrWrapper;
	private ImageManipulation imgManipulator;
	
	public Main() {
		ocrWrapper = new OCRWrapper(this);
		imgManipulator = new ImageManipulation();
		//ocrWrapper.scanDirectory("test_images");
	}

	public static void main(String[] args) {
		new Main();
	}

	public OCRWrapper getOCR() { return ocrWrapper; }
	
	public ImageManipulation getIMGManipulator() { return imgManipulator; }
}
