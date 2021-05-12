package minesweeper.model;

import java.io.IOException;

public interface ReadAndWriteFile<T> {

	public T readFromFile() throws IOException;

	public T readFromFile(String path) throws IOException;

	public void writeToFile(T obj) throws IOException;

	public void writeToFile(T obj, String path) throws IOException;

	static String getAppDataDir() {
		// Windows
		if (System.getenv("AppData") != null)
			return System.getenv("AppData");

		// Linux
		if (System.getenv("XDG_DATA_HOME") != null)
			return System.getenv("XDG_DATA_HOME");

		// Mac
		String os = System.getProperty("os.name", "generic").toLowerCase();
		if ((os.indexOf("mac") >= 0) || (os.indexOf("darwin") >= 0)) {
			return System.getProperty("user.home") + "/Library/Application Support";
		}

		// Fallback
		return System.getProperty("user.home");
	}

}
