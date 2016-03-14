import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Logger {
	
	private static FileWriter writer = null;
	
	private static FileWriter getWriter() {
		try {
			if (writer == null) {
				writer = new FileWriter(new File("log.txt"), true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return writer;
	}
	
	public static void writeLog(String message) {		
		try {
			getWriter().write(message + "\n");			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static <T> void writeLog(List<T> liste) {		
		for (T el : liste) {
			writeLog(el.toString());
		}
	} 

}
