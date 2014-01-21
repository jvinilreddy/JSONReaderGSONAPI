package com.goeuro.json.apiquery;

import java.io.FileWriter;
import java.io.IOException;

import com.goeuro.json.model.Position;

public final class GoEuroCsvWriter {
	// Commented for future purposes. Specific Directory location needs
	/*
	 * public static void main(String [] args) throws IOException{
	 * System.out.println(System.currentTimeMillis()); String currentDir = new
	 * File(".").getCanonicalPath(); System.out.println(currentDir); }
	 */
	/**
	 * @author Vinil J
	 * @param customModel
	 *            It contains the structure of Position class and it is of type
	 *            Position
	 * @param csvFileName
	 *            Name of the CSV file to be created based on current System
	 *            Time
	 * @return Returns 0 if the CSV file is created successfully else returns 1
	 */
	public int createCsvFile(Position customModel, String csvFileName) {
		try {
			FileWriter writer = new FileWriter(csvFileName, true);
			// String data = customModel.toString();
			writer.append(customModel.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.err
					.println("An IO Error occured in writing the data to file");
			return 1;
		}
		return 0;
	}
}