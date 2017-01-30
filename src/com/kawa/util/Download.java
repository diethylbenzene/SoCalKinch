package com.kawa.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JProgressBar;

/**
 * Downloads a file, and outputs its progress to a {@link JProgressBar}.
 * @author ethanvovan
 *
 */
public class Download {

	private File file;

	private URL url;
	private HttpURLConnection connection;
	private double progress;
	private JProgressBar pb;

	public Download(URL url, File file, JProgressBar b) throws MalformedURLException, IOException 
	{
		this.url = url;
		this.file = file;
		this.pb = b;
		connect();
	}
	
	/**
	 * <b>This method is always invoked by the constructor, so there is no need to call it in your project.</b>
	 * <br><br>Opens the {@link HttpURLConnection} required by the {@link URL}.
	 */
	private void connect()
	{
		try
		{
			this.connection = (HttpURLConnection) url.openConnection();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Downloads a file.
	 * @throws IOException
	 */
	public void downloadFile() throws IOException 
	{
		if(connection.getResponseCode() == 200)
		{
			int bufferSize = 1024;
			long total = 0;
			long filesize = connection.getContentLengthLong();
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(fos, 1024);
			byte[] data = new byte[bufferSize];
			int i = 0;
			while ((i = in.read(data, 0, bufferSize)) != -1)
			{
				total += i;
				out.write(data, 0, i);
				progress = total * 100.0 / filesize;
				this.pb.setValue((int) progress);
				this.pb.setString(Util.roundToNearestDecimal(total / 1048576.0, 2) + " MB of " + Util.roundToNearestDecimal(filesize / 1048576.0, 2) + " MB, " + Util.roundToNearestDecimal(progress, 1) + "% completed");
			}
			
			out.close();
			in.close();
		}
		else
		{
			throw new IOException("Error connecting to the server! HTTP reponse code returned: " + connection.getResponseCode()); //HTTP code response was not HTTP_OK
		}
	}
	
	public File getFile() 
	{
		return file;
	}
	
	public void setFile(File file) 
	{
		this.file = file;
	}
	
	public URL getURL() 
	{
		return url;
	}
	
	public void setURL(URL url) 
	{
		this.url = url;
	}
}
