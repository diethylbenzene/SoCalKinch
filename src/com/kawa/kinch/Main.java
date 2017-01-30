package com.kawa.kinch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import com.kawa.util.ConsoleOutputStream;
import com.kawa.util.Util;
import com.opencsv.CSVWriter;

/**
 * <b>This class is the backbone of the program. This opens a {@link Thread} to calculate the KinchRanks from a list of WCA IDs. The process is as follows:</b><br>
 * <ul>
 * <li>Download the WCA database</li>
 * <li>Collect the WCA IDs listed in a plaintext file</li>
 * <li>Search for each person's best result in the database and input it into a two-dimensional <code>int</code> array</li>
 * <li>Calculate the KinchRanks based on either the best results found, or the WRs</li>  
 * <li>Import the KinchRanks results into a two-dimensional <code>double</code> array</li>
 * <li>Write that to a .CSV file</li>
 * </ul>
 * 
 * <b>The KinchRanks system works as follows:</b><br>
 * 
 * <ul><li>1. For each event, you get a score of WR/PB x 100 (this will be in the range 0-100)</li>
 * <li>2. The average across your 18 scores is the final KinchRank score.</li></ul>
 * The “events” used are:<ul>
 * <li>Averages for 3x3x3, 4x4x4, 5x5x5, 2x2x2, OH, Feet, Megaminx, Pyraminx, Square-1, Clock, Skewb, 6x6x6, 7x7x7.
 * <li>Singles for 3bld, FMC, 4bld, 5bld, MultiBLD</ul>
 * If you haven’t done the event then of course you get 0 for that event.<br>
 * <br>For MultiBLD, your result is adjusted to a single number Points + ProportionOfHourLeft. At the time of writing, 
 * Marcin Kowalkcyk has a score of 41/41 54:14, which equates to 41.0961. Someone with e.g. 11 points in 45:00 would get (11+0.25)/41.0961 = 0.2737. 
 * This calculation ensures that more points always equals a better score, no matter the time spent. Less time spent still gives a better score.<br><br>
 * 
 * CSV 
 * @author Code: Ethan Vovan (Kawa), KinchRanks system developed by Daniel Sheppard (Kinch), All CSV handling done by OpenCSV 3.8.
 */

public class Main implements Runnable
{
	/**
	 * This is the list of WCA events which use an average to calculate. 
	 */
	public static final String[] EVENTS_AVERAGE = {"222", "333", "444", "555", "666", "777", "333oh", "333ft", "minx", "pyram", "sq1", "clock", "skewb"};
	
	/**
	 * This is the list of WCA events which use a single to calculate. <br><br><b>N.B.) </b>Conventionally, 3BLD and FMC can be interchanged between 
	 * average and single, whichever works best for the competitor. However, since there exists more single results for these events than average results, 
	 * it makes more sense for competitors to be ranked using their singles.
	 */
	public static final String[] EVENTS_BESTOF = {"333bf", "333fm", "444bf", "555bf", "333mbf"};
	
	/**
	 * This is a 2D {@code int} array that holds the raw WCA data.
	 */
	private int[][] kinchRaw;
	
	/**
	 * This is a 2D {@code double} array that holds the processed KinchRanks data.
	 */
	private double[][] kinchDone;
	
	/**
	 * This is an {@code int} array that holds the results the KinchRanks are calculated against.
	 */
	private int[] fastestIndex;
	
	/**
	 * MBLD is calculated in a different manner than other events. Each imported result is an {@code int} in the form of <code>0DDTTTTTMM</code>, and is decoded like this:<br><br>
	 * 
	 * difference = 99 - DD<br>
	 * timeInSeconds = TTTTT (99999 means unknown)<br>
	 * missed = MM<br><br>
	 * 
	 * The result for MBLD is calculated separately and stored in this {@code double} for each competitor.
	 */
	private double[] mbld;
	
	/**
	 * This is a {@code double} array that contains the average KinchRank from each event.
	 */
	private double[] finalKinch;
	
	/**
	 * This is the list of WCA IDs from a user-designated {@link File}.
	 */
	private String[] wcaIds;
	
	/**
	 * This points to the {@link File} that contains the WCA IDs. 
	 */
	private File wcaIdFile;
	
	/**
	 * This allows for the code to write to the GUI's {@link JProgressBar}.
	 */
	private JProgressBar bar;
	
	/**
	 * This flags whether or not to delete the previously downloaded database files. 
	 */
	private boolean clean;
	
	/**
	 * This flags whether to use the best results found from the imported data, or from the current WRs. 
	 */
	private boolean relative;
	
	/**
	 * This flags whether or not to use the default location for the output CSV file.
	 */
	private boolean defaultFile;
	
	/**
	 * This is the output CSV {@link File}.
	 */
	private File outputFile;
	
	/**
	 * This constructor is used for when the user checks the {@code defaultFile} check box in the GUI.
	 * @param file The WCA ID {@link File}
	 * @param area The {@link JTextArea} for the console
	 * @param bar The {@link JProgressBar} for the progress bar
	 * @param relative Flags whether to use the best results found from the imported data, or from the current WRs. 
	 * @param clean Flags whether or not to delete the previously downloaded database files. 
	 */
	public Main(String file, JTextArea area, JProgressBar bar, boolean relative, boolean clean)
	{
		this.wcaIdFile = new File(file);
		this.bar = bar;
		this.relative = relative;
		this.clean = clean;
		this.defaultFile = true;
		PrintStream ps = new PrintStream(new ConsoleOutputStream(area));
		System.setOut(ps);
		System.setErr(ps);
	}
	
	/**
	 * This constructor is used for when the user does not check the {@code defaultFile} check box in the GUI.
	 * @param file The WCA ID {@link File}
	 * @param area The {@link JTextArea} for the console
	 * @param bar The {@link JProgressBar} for the progress bar
	 * @param relative Flags whether to use the best results found from the imported data, or from the current WRs. 
	 * @param clean Flags whether or not to delete the previously downloaded database files. 
	 * @param outputFile The output CSV file.
	 */
	public Main(String file, JTextArea area, JProgressBar bar, boolean relative, boolean clean, File outputFile)
	{
		this.wcaIdFile = new File(file);
		this.bar = bar;
		this.relative = relative;
		this.clean = clean;
		this.outputFile = outputFile;
		this.defaultFile = false;
		PrintStream ps = new PrintStream(new ConsoleOutputStream(area));
		System.setOut(ps);
		System.setErr(ps);
	}
	
	/**
	 * This constructor is only used for any non-GUI purposes.
	 * @param file The WCA ID {@link File}
	 * @param relative Flags whether to use the best results found from the imported data, or from the current WRs. 
	 * @param clean Flags whether or not to delete the previously downloaded database files. 
	 * @param outputFile The output CSV file.
	 */
	public Main(String file, boolean relative, boolean clean, File outputFile)
	{
		this.wcaIdFile = new File(file);
		this.relative = relative;
		this.bar = new JProgressBar();
		this.clean = clean;
		this.outputFile = outputFile;
		this.defaultFile = false;
	}
	
	public static void main(String[] args) {
		new Thread(new Main(args[0], Boolean.parseBoolean(args[1]), Boolean.parseBoolean(args[2]), new File(args[3]))).start();
	}

	/**
	 * This method is invoked by the {@link Thread}. It executes the procedure outlined in the class's documentation.<br>
	 * MBLD results are imported and calculated separately, since ranking MBLD requires a separate parsing step.
	 * @throws IOException
	 */
	private void start() throws IOException {
		if(!new File(Window.outputFileLocation).exists())
			throw new IOException("Directory not found!");
		
		System.out.println("> Cleaning");
		Util.clean(true);
		System.out.println("> Downloading database");
		Retriever.retrieveDatabase(bar);
		System.out.println("> Reading from designated file");
		wcaIds = Parser.readFromFile(wcaIdFile);
		kinchRaw = new int[wcaIds.length][18];
		kinchDone = new double[wcaIds.length][18];
		fastestIndex = new int[18];
		mbld = new double[wcaIds.length];
		finalKinch = new double[wcaIds.length];	
		bar.setValue(0);
		bar.setString(null);
		System.out.println("> Finding data");
		fillRawData();	
		System.out.println("> Calculating best result index");
		calculateBest(relative);
		fillKinch();
		System.out.println("> Finding and calculating MBLD results");
		fillKinchWithMBLD(relative);	
		System.out.println("> Calculating the average KinchRanks");
		calculateAverageKinchRank();
		System.out.println("> Writing to file");
		
		if(defaultFile)
			fillCSV();
		else
			fillCSV(outputFile);

		if(clean){ 
			System.out.println("> Cleaning up");
			Util.clean(false);
		}

		System.out.println("> Done!");
	}

	/**
	 * This method fills the {@code kinchRaw} array with the data found from the WCA database file. 
	 * @throws IOException
	 */
	private void fillRawData() throws IOException
	{
		int count = 0;
		for(int i = 0; i < wcaIds.length; i++)
		{
			for(int j = 0; j < EVENTS_AVERAGE.length; j++)
			{
				kinchRaw[i][j] = Parser.parse(Parser.DB_AVERAGE, EVENTS_AVERAGE[j], wcaIds[i]);
			}

			for(int j = 0; j < EVENTS_BESTOF.length; j++)
			{
				kinchRaw[i][j + EVENTS_AVERAGE.length] = Parser.parse(Parser.DB_SINGLE, EVENTS_BESTOF[j], wcaIds[i]);
			}
			count++;
			bar.setValue((int) Math.round((((double)count / (double)Parser.num) * 100.0)));
			System.out.println(">> Found results for ID " + wcaIds[i] + ", " + count + " / " + Parser.num + " found, " + Math.round((((double)count / (double)Parser.num) * 100.0)) + "% done");
		}
	}
	
	/**
	 * This method calculates the best results from either the {@code kinchRaw} array itself, for from the WRs in the WCA database.
	 * @param relative Flags whether to use the best results found from the imported data, or from the current WRs. 
	 */
	private void calculateBest(boolean relative)
	{
		if(relative)
		{
			int[][] kinchTransposed = Util.trasposeArray(kinchRaw);

			for(int i = 0; i < 17; i++)
			{
				fastestIndex[i] = Util.findLowest(kinchTransposed[i]);
			}
		}
		else
		{
			try
			{
				for(int i = 0; i < EVENTS_AVERAGE.length; i++)
				{
					fastestIndex[i] = Parser.parseForWR(Parser.DB_AVERAGE, EVENTS_AVERAGE[i]);
				}

				for(int i = 0; i < EVENTS_BESTOF.length; i++)
				{
					fastestIndex[i + 13] = Parser.parseForWR(Parser.DB_SINGLE, EVENTS_BESTOF[i]);
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method fills the {@code kinchDone} array with all the calculated KinchRanks except for MBLD.
	 */
	private void fillKinch()
	{
		for(int i = 0; i < wcaIds.length; i++)
		{
			for(int j = 0; j < 17; j++)
			{
				kinchDone[i][j] = Util.calculateKinch(kinchRaw[i][j], fastestIndex[j]);
			}
		}
	}

	/**
	 * This method parses the MBLD results in the {@code kinchRaw} array and puts them into the {@code mbld} array. It
	 * then calculates the KinchRanks based on either the fastest result found or the current WR.
	 * @param relative Flags whether to use the best result found from the imported data, or from the current WR. 
	 * @throws IOException 
	 */
	private void fillKinchWithMBLD(boolean relative) throws IOException
	{
		for(int i = 0; i < wcaIds.length; i++)
		{
			mbld[i] = Util.parseMBLD(kinchRaw[i][17]);
		}

		double fastest;
		if(relative)
			fastest = Util.findBest(mbld);
		else
			fastest = Util.parseMBLD(Parser.parseForWR(Parser.DB_SINGLE, "333mbf"));
		
		for(int i = 0; i < wcaIds.length; i++)
		{
			kinchDone[i][17] = (mbld[i] / fastest) * 100.0;
		}
	}

	/**
	 * This method calculates the average KinchRank and outputs it into the {@code finalKinch} array.
	 */
	private void calculateAverageKinchRank()
	{
		for(int i = 0; i < wcaIds.length; i++)
		{
			finalKinch[i] = Util.calculateAverage(kinchDone[i]);
		}
	}
	
	/**
	 * This method takes all the processed data and outputs it into a CSV file to the default location.
	 * @throws IOException
	 */
	private void fillCSV() throws IOException
	{
		CSVWriter writer = new CSVWriter(new FileWriter(new File("." + File.separator + "kinch.csv")), ',');

		String[] header = {"Person", "KinchRank", "222", "333", "444", "555", "666", "777", "333oh", "333ft", "minx", "pyram", "sq1", "clock", "skewb", "333bf", "333fm", "444bf", "555bf", "333mbf"};
		writer.writeNext(header);

		for(int i = 1; i <= wcaIds.length; i++)
		{
			String[] output = new String[20];
			for(int j = 0; j < output.length; j++)
			{
				if(j == 0)
					output[0] = wcaIds[i - 1];
				else if(j == 1)
					output[1] = Double.toString(finalKinch[i - 1]);
				else
				{
					output[j] = Double.toString(kinchDone[i - 1][j - 2]);
				}
			}

			writer.writeNext(output);
		}

		writer.close();
	}
	
	/**
	 * This method takes all the processed data and outputs it into a CSV file.
	 * @param file The file it outputs to.
	 * @throws IOException
	 */
	private void fillCSV(File file) throws IOException
	{
		CSVWriter writer = new CSVWriter(new FileWriter(file));

		String[] header = {"Person", "KinchRank", "222", "333", "444", "555", "666", "777", "333oh", "333ft", "minx", "pyram", "sq1", "clock", "skewb", "333bf", "333fm", "444bf", "555bf", "333mbf"};
		writer.writeNext(header);

		String[][] csvOut = new String[wcaIds.length][20];
		for(int i = 0; i < wcaIds.length; i++)
		{
			for(int j = 0; j < csvOut[0].length; j++)
			{
				if(j == 0)
					csvOut[i][0] = wcaIds[i];
				else if(j == 1)
					csvOut[i][1] = Double.toString(finalKinch[i]);
				else
				{
					csvOut[i][j] = Double.toString(kinchDone[i][j - 2]);
				}
			}
		}
		
		Arrays.sort(csvOut, new Comparator<String[]>() {
			@Override
			public int compare(String[] first, String[] second)
			{
				Double d1 = Double.valueOf(first[1]);
				Double d2 = Double.valueOf(second[1]);
				
				return -d1.compareTo(d2);
			}
		});
		
		for(int i = 0; i < wcaIds.length; i++)
			writer.writeNext(csvOut[i]);

		writer.close();
	}
	
	@Override
	public void run() {
		try {
			start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
