package com.stt.portfolio.quotes.portfoliofiles;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
 
public class Tail {
 
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		
	}
 
	public static String tail(String fileName) {
		Vector<String> lines =  tail(fileName, 1);
		if (lines != null && lines.size() > 0) {
			return lines.get(0);
		}
		return "";
	}
	
	public static Vector<String> tail(String fileName, int lineCount)
	{
		return tail(fileName, lineCount, 1000);
	}
 
	/**
	 * Given a byte array this method:
	 * a. creates a String out of it
	 * b. reverses the string
	 * c. extracts the lines
	 * d. characters in extracted line will be in reverse order, 
	 *    so it reverses the line just before storing in Vector.
	 *     
	 *  On extracting required numer of lines, this method returns TRUE, 
	 *  Else it returns FALSE.
	 *   
	 * @param bytearray
	 * @param lineCount
	 * @param lastNlines
	 * @return
	 */
	private static boolean parseLinesFromLast(byte[] bytearray, int lineCount, Vector<String> lastNlines)
	{
		String lastNChars = new String (bytearray);
		StringBuffer sb = new StringBuffer(lastNChars);
		lastNChars = sb.reverse().toString();
		StringTokenizer tokens= new StringTokenizer(lastNChars,"\n");
		while(tokens.hasMoreTokens())
		{
			StringBuffer sbLine = new StringBuffer((String)tokens.nextToken());			
			lastNlines.add(sbLine.reverse().toString());
			if(lastNlines.size() == lineCount)
			{
				return true;//indicates we got 'lineCount' lines
			}
		}
		return false; //indicates didn't read 'lineCount' lines
	}
	
	/**
	 * Reads last N lines from the given file. File reading is done in chunks.
	 * 
	 * Constraints:
	 * 1 Minimize the number of file reads -- Avoid reading the complete file
	 * to get last few lines.
	 * 2 Minimize the JVM in-memory usage -- Avoid storing the complete file 
	 * info in in-memory.
	 *
	 * Approach: Read a chunk of characters from end of file. One chunk should
	 * contain multiple lines. Reverse this chunk and extract the lines. 
	 * Repeat this until you get required number of last N lines. In this way 
	 * we read and store only the required part of the file.
	 * 
	 * 1 Create a RandomAccessFile.
	 * 2 Get the position of last character using (i.e length-1). Let this be curPos.
	 * 3 Move the cursor to fromPos = (curPos - chunkSize). Use seek().
	 * 4 If fromPos is less than or equal to ZERO then go to step-5. Else go to step-6
	 * 5 Read characters from beginning of file to curPos. Go to step-9.
	 * 6 Read 'chunksize' characters from fromPos.
	 * 7 Extract the lines. On reading required N lines go to step-9.
	 * 8 Repeat step 3 to 7 until 
	 *			a. N lines are read.
	 *		OR
	 *			b. All lines are read when num of lines in file is less than N. 
	 * Last line may be a incomplete, so discard it. Modify curPos appropriately.
	 * 9 Exit. Got N lines or less than that.
	 *
	 * @param fileName
	 * @param lineCount
	 * @param chunkSize
	 * @return
	 */
	public static Vector<String> tail(String fileName, int lineCount, int chunkSize)
	{
		try
		{
			RandomAccessFile raf = new RandomAccessFile(fileName,"r");
			Vector<String> lastNlines = new Vector<String>();			
			int delta=0;
			long curPos = raf.length() - 1;
			long fromPos;
			byte[] bytearray;
			if (curPos > 0) {
			while(true)
			{				
				fromPos = curPos - chunkSize;
				//System.out.println(curPos);
				//System.out.println(fromPos);				
				if(fromPos <= 0)
				{
					raf.seek(0);
					bytearray = new byte[(int)curPos];
					raf.readFully(bytearray);
					parseLinesFromLast(bytearray, lineCount, lastNlines);
					break;
				}
				else
				{					
					raf.seek(fromPos);
					bytearray = new byte[chunkSize];
					raf.readFully(bytearray);
					if(parseLinesFromLast(bytearray, lineCount, lastNlines))
					{
						break;
					}
					delta = ((String)lastNlines.get(lastNlines.size()-1)).length();
					lastNlines.remove(lastNlines.size()-1);
					curPos = fromPos + delta;	
				}
			}
			}
			/*
			Enumeration e = lastNlines.elements();
			while(e.hasMoreElements())
			{
				System.out.println(e.nextElement());
			}
			*/
			
			raf.close();
			
			return lastNlines;
		}
		catch (FileNotFoundException ex) {
	 		File file = new File(fileName);
	 	    
	        // Create file if it does not exist
	        try {
				file.createNewFile();
			} catch (IOException e) {
				
				e.printStackTrace();
			}

	 	}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		return null;
	}	
	
	public static void removeLastLine(String filename, int len) {
		 try{
             RandomAccessFile raf = new RandomAccessFile(filename, "rw");
             long length = raf.length();
            
             raf.setLength(length - len);
            
             raf.close();
             }
		 	
		 		catch(Exception ex){
             ex.printStackTrace();
             }
	}
}


