package com.kawa.kinch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.CSVReader;

/**
 * This class will read the WCA database files that was retrieved from {@link Retriever} and find the entries with OpenCSV.
 * @author ethanvovan, OpenCSV 3.8
 *
 */
public class Parser
{
	/**
	 * The location of the Average database.
	 */
	public static final File DB_AVERAGE = new File("." + File.separator + "exports" + File.separator + "WCA_export_RanksAverage.tsv");
	
	/**
	 * The location of the Single database.
	 */
	public static final File DB_SINGLE = new File("." + File.separator + "exports" + File.separator + "WCA_export_RanksSingle.tsv");
	
	public static int num = 0;
	
	/**
	 * Looks through the WCA database file that it's been given and based on the event and WCA ID it's been given it returns the result. If the result
	 * doesn't exist, it returns 0.
	 * @param file The WCA database file from which to look from. 
	 * @param event The WCA event.
	 * @param wcaId The WCA ID.
	 * @return The result.
	 * @throws IOException
	 */
	public static int parse(File file, String event, String wcaId) throws IOException
	{
		int result = 0;
		CSVReader readerAverage = new CSVReader(new FileReader(file), '\t');
		
		String[] nextLine;
		while((nextLine = readerAverage.readNext()) != null)
		{
			if(nextLine[0].equals(wcaId) && nextLine[1].equals(event))
			{
				result = Integer.parseInt(nextLine[2]);
			}
		}
		
		readerAverage.close();
		
		return result;
	}
	
	/**
	 * Looks through the WCA database file that it's been given and based on the event it returns the world record result.
	 * @param file The WCA database file from which to look from. 
	 * @param event The WCA event.
	 * @return The WR result.
	 * @throws IOException
	 */
	public static int parseForWR(File file, String event) throws IOException
	{
		int wr = 0;
		CSVReader readerAverage = new CSVReader(new FileReader(file), '\t');
		
		String[] nextLine;
		while((nextLine = readerAverage.readNext()) != null)
		{
			if(nextLine[3].equals("1") && nextLine[1].equals(event))
			{
				wr = Integer.parseInt(nextLine[2]);
			}
		}
		
		readerAverage.close();
		
		return wr;
	}
	
	/**
	 * Reads a file. This is used for reading the user-defined WCA IDs file.
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String[] readFromFile(File file) throws IOException
	{
		ArrayList<String> read = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	read.add(line);
		    	num++;
		    }
		}
		
		String[] output = new String[read.size()];
		
		for(int i = 0; i < output.length; i++)
			output[i] = read.get(i);
		
		return output;
	}
	
}
