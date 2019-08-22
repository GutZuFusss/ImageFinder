
public class Main {
	private OCRWrapper ocrWrapper;
	private ImageManipulation imgManipulator;
	
	public Main() {
		ocrWrapper = new OCRWrapper(this);
		imgManipulator = new ImageManipulation();
		//ocrWrapper.scanDirectory("test_images");
	}

	public OCRWrapper getOCR() { return ocrWrapper; }
	
	public ImageManipulation getIMGManipulator() { return imgManipulator; }
	
	public static void main(String[] args) { // entry point
		new Main();
	}
}
