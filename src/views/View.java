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

package views;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Container;

/**
 * @author M. Noyan Baykal
 * 
 * This is the View class that handles the Swing GUI.
 */
public class View {
	private JFrame frame;
    private JButton playlistButton;
    private JButton rootButton;
    private JButton targetButton;
    private JButton startButton;
    private JRadioButton sourceButton;
    private JRadioButton indexButton;
    private JTextField recurseLevelLabel;
    JTextArea console;
	JScrollPane consolePane;
    
    /**
     * The constructor sets up the JFrame and calls addContent to add the interactive elements.
     */
    public View(){
    	//Set the frame
    	frame = new JFrame("M3U Foldered Transfer");
	    frame.getContentPane().setLayout(new BorderLayout(5, 5));
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(600, 200);
	    frame.setVisible(true);
	    
	    //Set all the buttons
        addContent(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Adds all the buttons that are present in the GUI.
     * @param pane contains all the buttons
     */
    private void addContent(Container pane){
	    //Add folder structure options
	    addRadioAndRecurseLabel(pane);
    	
    	//M3U button
    	playlistButton = new JButton("Select M3U8 File");
    	pane.add(playlistButton, BorderLayout.LINE_START);
    	playlistButton.setToolTipText("Select the M3U8 file to determine songs to copy");
    	
    	//Source button
	    rootButton = new JButton("Select Source Folder");
	    pane.add(rootButton, BorderLayout.CENTER);
	    rootButton.setToolTipText("Select the source folder where the music files are");
	
	    //Target button
	    targetButton = new JButton("Select Target Folder");
	    pane.add(targetButton, BorderLayout.LINE_END);
	    targetButton.setToolTipText("Select where everything will be copied");
	    
	    //Add start button and the console
	    addStartAndConsole(pane);
    }
    
    /**
     * Adds the radio buttons.
     * @param pane contains the radio buttons and the recurse level label
     */
    private void addRadioAndRecurseLabel(Container pane){
    	JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
	    
	    //Source button that selects to copy from this folder
    	sourceButton = new JRadioButton("Copy contents");
        sourceButton.setSelected(true);
        sourceButton.setToolTipText("Select this to copy all folders inside the source folder");
        
        //Index button that enables copying folders from the bottom
        indexButton = new JRadioButton("Copy from bottom");
        indexButton.setToolTipText("Select this to only copy given levels of folders above each file");
        
        ButtonGroup group = new ButtonGroup();
        group.add(sourceButton);
        group.add(indexButton);
        
        //This label is used to get the desired number of folders to be copied when copying from bottom
        recurseLevelLabel = new JTextField();
        recurseLevelLabel.setText("2");
        recurseLevelLabel.setToolTipText("This many folders above each file will be copied");
        
        panel.add(sourceButton);
        panel.add(indexButton);
        panel.add(recurseLevelLabel);

	    pane.add(panel, BorderLayout.PAGE_START);
    }
    
    /**
     * Adds the start button and the output console.
     * @param pane contains the start button and the output console
     */
    private void addStartAndConsole(Container pane){
    	JPanel lastPanel = new JPanel();
	    lastPanel.setLayout(new BoxLayout(lastPanel, BoxLayout.Y_AXIS));
	    
	    //Start button
	    startButton = new JButton("Start copying");
	    startButton.setToolTipText("Make sure you have chosen the desired folders and folder structure option");
    	
	    //Set the console area
    	console = new javax.swing.JTextArea();
    	console.setColumns(30);
    	console.setRows(15);

    	console.setText("Select options!\n");

    	consolePane = new javax.swing.JScrollPane(console);

	    lastPanel.add(startButton);
	    lastPanel.add(consolePane);

	    pane.add(lastPanel, BorderLayout.PAGE_END);
    }
    
    /**
     * @return The frame that contains everything.
     */
    public JFrame getFrame(){
    	return frame;
    }
    
    /**
     * @return The select play-list button that browses for the play-list file
     */
    public JButton getPlaylistButton(){
    	return playlistButton;
    }
    
    /**
     * @return The select root folder button that browses for the root folder which will be used as the top level
     * folder if the "copy contents" option is selected
     */
    public JButton getRootButton(){
    	return rootButton;
    }
    
    /**
     * @return The select target folder button that browsers for the folder where the songs will be copied to
     */
    public JButton getTargetButton(){
    	return targetButton;
    }
    
    /**
     * @return The start button used to start copying files if all necessary settings have been set
     */
    public JButton getStartButton(){
    	return startButton;
    }
    
    /**
     * @return The label that has the level that will be used to determine how many fodlers to copy if
     * "copy from bottom" option is selected
     */
    public String getRecurseLevel(){
    	return recurseLevelLabel.getText();
    }
    
    /**
     * @return Whether "copy from bottom" option is selected or not
     */
    public boolean isRecursive(){
    	if(indexButton.isSelected())
    		return true;
    	else
    		return false;
    }
    
    /**
     * @return The console that displays output messages
     */
    public JTextArea getConsole(){
    	return console;
    }
}
