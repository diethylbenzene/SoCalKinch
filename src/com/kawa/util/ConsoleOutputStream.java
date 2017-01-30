package com.kawa.util;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

/**
 * A basic output stream for writing the console code to a JTextArea.
 * @author Kawa
 */
public class ConsoleOutputStream extends OutputStream
{
	private JTextArea textArea;
	
	public ConsoleOutputStream(JTextArea area) 
	{
		this.textArea = area;
	}

	@Override
	public void write(int b) throws IOException 
	{
		textArea.append(String.valueOf((char) b));
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}	
}
