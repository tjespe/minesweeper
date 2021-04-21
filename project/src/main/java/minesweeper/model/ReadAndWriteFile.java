package minesweeper.model;

import java.io.IOException;

public interface ReadAndWriteFile<T> {

	public T readFromFile() throws IOException;

	public void writeToFile(T obj) throws IOException;

}
