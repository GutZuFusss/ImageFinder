
public class Main {

	public static void main(String[] args) {
		OCRWrapper ow = new OCRWrapper();
		ow.scanDirectory("test_images");
		System.out.println("RESULT: " + ow.test);
	}

}
