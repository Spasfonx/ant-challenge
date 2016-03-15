import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Logger {
	
	private static FileWriter writer = null;
	private final static boolean isEnabled = true;
	
	private static FileWriter getWriter() {
		try {
			if (writer == null) {
				writer = new FileWriter(new File("log.txt"), false);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return writer;
	}
	
	public static void writeLog(String message) {
		if (isEnabled) {
			try {
				getWriter().write(message + "\n");
				getWriter().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static <T> void writeLog(List<T> liste) {		
		for (T el : liste) {
			writeLog(el.toString());
		}
	} 

}
