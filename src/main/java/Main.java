
public class Main {
	private OCRWrapper ocrWrapper;
	
	public Main() {
		ocrWrapper = new OCRWrapper();
		//ocrWrapper.scanDirectory("test_images");
	}

	public static void main(String[] args) {
		new Main();
	}

	public OCRWrapper getOCR() { return ocrWrapper; }
}
