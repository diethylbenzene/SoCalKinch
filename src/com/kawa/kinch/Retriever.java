package com.kawa.kinch;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JProgressBar;

import com.kawa.util.Download;
import com.kawa.util.Util;

/**
 * Downloads the database .zip file.
 * @author Kawa
 */
public class Retriever {
	
	private static URL databaseUrl;
	private static File databaseFile = new File("." + File.separator + "WCA_export.tsv.zip");
	
	static {
		try {
			databaseUrl = new URL("https://www.worldcubeassociation.org/results/misc/WCA_export.tsv.zip"); 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Downloads the database.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static void retrieveDatabase(JProgressBar b) throws MalformedURLException, IOException
	{
		Download databaseDownload = new Download(databaseUrl, databaseFile, b);
		databaseDownload.downloadFile();
		
		Util.unzipFile(databaseDownload.getFile(), "exports");
	}
}
