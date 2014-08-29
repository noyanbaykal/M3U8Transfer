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

package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JTextArea;

import views.View;
import models.Model;

/**
 * @author M. Noyan Baykal
 * 
 * This is the Controller class that connects the Model and the View classes.
 * It implements Runnable so that copying songs will be done on another thread and the GUI will be responsive.
 */
public class Controller implements Runnable{
	private  Model model;
	private View view;
	
	/**
	 * @param model The Model class that has keeps the required data for copying and has the copy functions
	 * @param view The View class that implements the GUI
	 */
	public Controller(Model model, View view){
		this.model = model;
		this.view = view;
	}
	
	/**
	 * This is called by the Main function to start the program by setting the GUI buttons.
	 */
	public void start(){
		setButtons();
	}
	
	/**
	 * Assigns the GUI buttons their functionalities.
	 */
	private void setButtons(){
		view.getPlaylistButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setM3uButton(e);
			}
		});
		
		view.getRootButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setRootButton(e);
			}
		});
		
		view.getTargetButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTargetButton(e);
			}
		});
		
		view.getStartButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setStartButton(e);
			}
		});
	}
	
	/**
	 * This is the function that is called when the setM3u button is pressed. It opens a file browser to select the
	 * playlist file.
	 * @param e The ActionEvent that is passed when the button is clicked
	 */
	private void setM3uButton(ActionEvent e){
		//Open a file chooser
		JFileChooser c = new JFileChooser();
    	int result = c.showOpenDialog(view.getFrame());
    	
    	//Check if a valid selection has been made
    	if(result == JFileChooser.APPROVE_OPTION){
    		try{
    			model.m3uSelected(c.getSelectedFile());
    		}catch(Exception ex){
    			writeError(ex.getMessage());
    		}
    	}
    	
    	if(model.total < 0){
    		writeError("The input file is not valid");
    	}
	}
	
	/**
	 * This is the function that is called when the set source button is pressed. It opens a file browser to select the
	 * source folder.
	 * @param e The ActionEvent that is passed when the button is clicked
	 */
	private void setRootButton(ActionEvent e){
		//Open a file chooser
		JFileChooser c = new JFileChooser();
		c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//Only accept folders
		c.setAcceptAllFileFilterUsed(false);
		
    	int result = c.showOpenDialog(view.getFrame());
    	
    	if(result == JFileChooser.APPROVE_OPTION)
    		model.rootSelected(c.getSelectedFile());
	}
	
	/**
	 * This is the function that is called when the set target button is pressed. It opens a file browser to select the
	 * target folder.
	 * @param e The ActionEvent that is passed when the button is clicked
	 */
	private void setTargetButton(ActionEvent e){
		//Open a file chooser
		JFileChooser c = new JFileChooser();
		c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//Only accept folders
		c.setAcceptAllFileFilterUsed(false);
		
    	int result = c.showOpenDialog(view.getFrame());
    	
    	if(result == JFileChooser.APPROVE_OPTION)
    		model.targetSelected(c.getSelectedFile());
	}
	
	/**
	 * This is the function that is called when the start button is pressed.
	 * It checks which copying option is selected and determines if all required settings has been selected.
	 * @param e The ActionEvent that is passed when the button is clicked
	 */
	private void setStartButton(ActionEvent e){
		//Determine which copying option to use
		if(view.isRecursive()){
			//Check user input
			int input = -1;
			
			try{
				input = Integer.parseInt(view.getRecurseLevel());
			}catch(Exception ex){
				writeError("You must enter an integer between 1 and 9\n");
				return;
			}
			
			if(input < 1)
				writeError("You must enter an integer that is at least 1\n");
			else if(!model.checkFolderSettings())
				writeError("You must set all required settings\n");
			else{
				startCopying(input);
			}
		}else{
			if(model.checkFolderSettingsFull()){
				startCopying(-1);
			}
			else
				writeError("You must set all the required settings\n");
		}
	}
	
	/**
	 * This is called only after the start button is pressed and all settings has been selected.
	 * The input parameter will be -1 if "copy contents" option is selected.
	 * @param input The number of folders we will be copying if "copy from bottom" option is selected
	 */
	private void startCopying(int input){
		model.setLevel(input);
		model.isRunning = true;
		
		//Run in new thread so the copying won't be done in the EDT
        Thread thread = new Thread(this);
        thread.start();
	}
	
	/**
	 * This function is called if we receive a "not enough space on disk" error. We will stop immediately since we
	 * won't be deleting any files and there is no need to keep trying to copy files.
	 */
	private void handleNoSpace(){
		model.isRunning = false;
		writeError("There is not enough space on the disk. Stopping now!");
	}
	
	/**
	 * Prints the given string to the GUI console.
	 * @param message String that will be printed to the GUI console
	 */
	private void writeError(String message){
		if(message!= null){
			JTextArea console = view.getConsole();
			
			console.append(message);
			console.setCaretPosition(console.getText().length() - 1);
		}
	}
	
	/**
	 * This is called after processing each song to let the user know about the progress.
	 */
	private void checkProgress(){
		if(model.isRunning)
			writeError("Processed "+model.done+" of "+model.total+"\n");
		else
			writeError("Stopped. If you see no errors, all songs should be copied.\n");
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * 
	 * This is the actual function that handles copying all the songs in the selected play-list.
	 */
	@Override
	public void run() {
		String output;
		while(model.isRunning){
			output = model.copyFile();
			
			if(output != null){
				//Stop immediately if there is no space on the disk
				if(output.compareTo("No space") == 1){
					handleNoSpace();
					break;
				}else{
					writeError(output);
				}
			}
			
			checkProgress();
		}
		
		//When we have processed all of the songs we will print to the GUI console which songs we couldn't copy.
		//The line numbers of these songs in the play-list file will be printed.
		writeError(model.getErrors());
	}
}
