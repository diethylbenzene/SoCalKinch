package com.kawa.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class contains any useful {@code static} methods for all other classes to use.
 * @author Kawa
 */
public class Util 
{
	/**
	 * Deletes the database files after use.
	 * @param clearCsv Flags whether or not to clear the previous generated CSV.
	 */
	public static void clean(boolean clearCsv)
	{
		new File("." + File.separator + "WCA_export.tsv.zip").delete();
		new File("." + File.separator + "exports").delete();
		if(clearCsv) new File("." + File.separator + "kinch.csv").delete();
	}
	
	/**
	 * Expands a .zip file. This one will create a directory where the project .jar is located.
	 * @param zipFile The .zip file.
	 * @param dirName The directory name
	 */
	public static void unzipFile(File zipFile, String dirName)
	{
		unzipFile(zipFile, new File("." + File.separator + dirName));
	}

	/**
	 * Expands a .zip file.
	 * @param zipFile the .zip file.
	 * @param outputFolder The output folder.
	 */
	public static void unzipFile(File zipFile, File outputFolder) 
	{
		try 
		{

			if(!outputFolder.exists())
			{
				outputFolder.mkdir();
			}

			ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile));
			ZipEntry entry = zipIn.getNextEntry();
			
			while(entry != null)
			{
				String filePath = outputFolder.getCanonicalPath() + File.separator + entry.getName();
				if(!entry.isDirectory())
				{
					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
			        byte[] bytesIn = new byte[4096];
			        int read = 0;
			        while ((read = zipIn.read(bytesIn)) != -1) 
			        {
			            bos.write(bytesIn, 0, read);
			        }
			        bos.close();
				}
				else
				{
					File dir = new File(filePath);
					dir.mkdir();
				}
				
				entry = zipIn.getNextEntry();
			}

			zipIn.closeEntry();
			zipIn.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Rounds a number to the nearest nth decimal.
	 * @param input The number
	 * @param decimals The amount of decimals
	 * @return The rounded number.
	 */
	public static double roundToNearestDecimal(double input, int decimals)
	{
		return Math.round(input * Math.pow(10.0, decimals)) / Math.pow(10.0, decimals);
	}

	/**
	 * Calculates the KinchRank. 
	 * @param time The result.
	 * @param best The best result.
	 * @return The KinchRank index.
	 */
	public static double calculateKinch(int time, int best)
	{
		if(time == 0)
			return 0.0;
		else
			return (((double) best) / ((double) time)) * 100.0;
	}
	
	/**
	 * Calculates the KinchRank. 
	 * @param time The result.
	 * @param best The best result.
	 * @return The KinchRank index.
	 */
	public static double calculateKinch(double time, double best)
	{
		if(time == 0)
			return 0.0;
		else
			return (best / time) * 100.0;
	}
	
	/**
	 * Calculates the arithmetic mean of all the values in a {@code double} array.
	 * @param array The array
	 * @return The average.
	 */
	public static double calculateAverage(double[] array)
	{
		double sum = 0.0;
		for(double i : array)
			sum += i;
		
		return sum / array.length;
	}
	
	/**
	 * Finds the lowest value in an {@code int} array.
	 * @param input The array
	 * @return The lowest value found.
	 */
	public static int findLowest(int[] input)
	{
		int comparison = Integer.MAX_VALUE;
		for (int i : input) {
			if(i != 0)
				comparison = Math.min(comparison, i);
		}
		return comparison;
	}
	
	/**
	 * Finds the lowest value in an {@code double} array.
	 * @param input The array
	 * @return The lowest value found.
	 */
	public static double findBest(double[] input)
	{
		double comparison = Double.MIN_VALUE;
		for (double i : input) {
			if(i != 0)
				comparison = Math.max(comparison, i);
		}
		return comparison;
	}
	
	/**
	 * Transposes a two-dimensional array.
	 * @param input The array to transpose.
	 * @return The transposed array.
	 */
	public static int[][] trasposeArray(int[][] input)
	{
	    int m = input.length;
	    int n = input[0].length;

	    int[][] trasposedArray = new int[n][m];

	    for(int i = 0; i < n; i++)
	    {
	        for(int j = 0; j < m; j++)
	        {
	            trasposedArray[i][j] = input[j][i];
	        }
	    }

	    return trasposedArray;
	}
	
	/**
	 * Parses an MBLD result for a score. MBLD results are in the form of <code>0DDTTTTTMM</code>, and is decoded like this:<br><br>
	 * 
	 * difference (solved) = 99 - DD<br>
	 * timeInSeconds = TTTTT (99999 means unknown)<br>
	 * missed = MM<br><br>
	 * 
	 * The MBLD score is calculated as (solved) + ProportionOfHourLeft ((1 hour - time) / 1 hour). 
	 * @param input The MBLD result.
	 * @return The score.
	 */
	public static double parseMBLD(int input)
	{
		if(input == 0)
		{
			return 0;
		}
		else
		{
			String in = Integer.toString(input);
			
			int solved = 99 - Integer.parseInt(in.substring(0, 2));
			int time = Integer.parseInt(in.substring(2, 7));
			
			if(solved <= 0)
				return 0;
			else
			{
				double hour = (3600.0 - time) / 3600.0;
				return solved + hour;
			}
		}
	}
	
	/**
	 * Prints out a one-dimensional {@code int} array.
	 * @deprecated This is only used for debug purposes.
	 * @param array
	 */
	@Deprecated
	public static void print1DArray(int[] array)
	{
		for(int i : array)
			System.out.print(i + " ");
	}
	
	/**
	 * Prints out a one-dimensional {@code double} array.
	 * @deprecated This is only used for debug purposes.
	 * @param array
	 */
	@Deprecated
	public static void print1DArray(double[] array)
	{
		for(double i : array)
			System.out.print(i + " ");
	}
	
	/**
	 * Prints out a two-dimensional {@code int} array.
	 * @deprecated This is only used for debug purposes.
	 * @param array
	 */
	@Deprecated
	public static void print2DArray(int[][] array)
	{
		for (int i = 0; i < array.length; i++) {
			for(int j = 0; j < array[i].length; j++)
				System.out.print(array[i][j] + " ");
			System.out.println();
		}
	}
	
	/**
	 * Prints out a two-dimensional {@code double} array.
	 * @deprecated This is only used for debug purposes.
	 * @param array
	 */
	@Deprecated
	public static void print2DArray(double[][] array)
	{
		for (int i = 0; i < array.length; i++) {
			for(int j = 0; j < array[i].length; j++)		
				System.out.print(array[i][j] + " ");
			System.out.println();
		}
	}
}
