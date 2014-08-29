/*
	The MIT License (MIT)
	
	Copyright (c) [2014] [Melik Noyan Baykal]
	
	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:
	
	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE. 

 */

package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.commons.io.FileUtils;

/**
 * @author M. Noyan Baykal
 * 
 * This is the Model class that has the data and does the copying.
 */
public class Model{
	private File playlist = null;
	private File root = null;
	private File target = null;
	private BufferedReader buffReader;
	private int level;
	private int currentIndex = 0;
	//Songs that failed to be copied will be mentioned when execution stops. Their index will be kept here.
	private String errors = "";
	
	public int done = 0;
	public int total = 0;
	public boolean isRunning = false;
	
	/**
	 * This is called when a play-list is selected through a file browser to determine the songs to be copied.
	 * @param file The play-list that contains the paths of the songs that will be copied
	 * @throws UnsupportedEncodingException File IO related exceptions
	 * @throws FileNotFoundException File IO related exceptions
	 * @throws IOException File IO related exceptions
	 */
	public void m3uSelected(File file) throws UnsupportedEncodingException, FileNotFoundException, IOException{
		playlist = file;
		
		buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		
		//Determine the total number of songs
		int lines = 0;
		while(buffReader.readLine() != null){
			lines++;
		}
		
		total = (lines-1) / 2;
		
		//Reset the input and skip the first line
		buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		buffReader.readLine();
	}
	
	/**
	 * @param folder The folder that has the songs. This is used if "copy contents" option is selected
	 */
	public void rootSelected(File folder){
		root = folder;
	}
	
	/**
	 * @param folder Songs will be copied to this folder
	 */
	public void targetSelected(File folder){
		target = folder;
	}
	
	/**
	 * @return If the user has selected the required settings for the "copy from bottom" option
	 */
	public boolean checkFolderSettings(){
		if(playlist != null && target != null)
			return true;
		else
			return false;
	}
	
	/**
	 * @return If the user has selected the required settings for the "copy contents" option
	 */
	public boolean checkFolderSettingsFull(){
		if(playlist != null && root != null && target != null)
			return true;
		else
			return false;
	}
	
	/**
	 * @param Level The number of folders that will be copied when "copy from bottom" option is used
	 */
	public void setLevel(int Level){
		level = Level;
	}
	
	/**
	 * Gets the next songs path and copies it to the target folder. If there is no space available at the destination
	 * the execution of the program is stopped.
	 * @return Returns null on successful copy and an error message if there was a problem
	 */
	public String copyFile(){
		try {
			//Skip two lines
			String current = buffReader.readLine();
			current = buffReader.readLine();
			
			//Check if we reached the end
			if(current == null){
				isRunning = false;
				return null;
			}
			
			done++;
			
			//Get the current song
			File song = new File(current);
			File newTarget;
			
			//Determine which method to use for copying
			if(level == -1){
				newTarget = createFoldersFromSource(song.getAbsolutePath());
			}else{
				newTarget = createFoldersBottomUp(song.getAbsolutePath());
			}
			
			//Skip songs that are not in the selected root folder structure
			if(newTarget == null){
				return null;
			}
				
			//Copy the file
			FileUtils.copyFileToDirectory(song, newTarget);

		} catch (IOException e) {			
			//Stop immediately if there is no space left in the target device
			if(e.getMessage().contains("not enough space")){
				return "No space";
			}
			
			//Add the index of this file to the errors string that will be shown after execution stops
			errors = errors.concat("Unable to copy song on line: " + currentIndex+"\n");
			return null;
		}
		
		return null;
	}
	
	/**
	 * @return Returns the string that has the index of all files that failed to be copied. If this is empty then all
	 * songs have been successfully copied
	 */
	public String getErrors(){
		return errors;
	}
	
	/**
	 * This function is used to copy songs when the "copy contents" option is selected.
	 * @param song Full path of the song to be copied
	 * @return Returns the path that will be the target to copy this song
	 */
	private File createFoldersFromSource(String song){
		//Get the folders inside the source folder
		int start = song.indexOf(root.getName());
		int end = song.lastIndexOf(File.separatorChar);
		
		//This song is not in the root folder structure
		if(start == -1)
			return null;
		
		//Create the folders if they are missing
		File newTarget = new File(target.getAbsolutePath() +File.separator+ song.substring(start, end)+File.separator);

		return newTarget;
	}
	
	/**
	 * This function is used to copy songs when the "copy from bottom" option is selected.
	 * @param song Full path of the song to be copied
	 * @return Returns the path that will be the target to copy this song
	 */
	private File createFoldersBottomUp(String song){
		//Get all the folders. We don't want to include the last file separator.
		int start = song.indexOf(File.separatorChar);
		int end = song.lastIndexOf(File.separatorChar);
		
		//Determine the number of folders
		String folders = song.substring(start, end);
		int count = 0;
		int index = 0;
		
		for(index = 0; index < folders.length(); index++){
		    if( folders.charAt(index) == File.separatorChar) {
		        count++;
		    } 
		}
		
		//Set the folder depth if needed
		int depth = Math.min(level, count);
		
		//We will only create 'depth' many folders
		for(index = 0; index < count - depth; index++){
			folders = folders.substring(folders.indexOf(File.separatorChar, 1));
		}
		
		//Create the folders if they are missing
		File newTarget = new File(target.getAbsolutePath() + File.separator + folders + File.separator);

		return newTarget;
	}
}
