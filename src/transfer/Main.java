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

package transfer;

import controllers.Controller;
import views.View;
import models.Model;

/**
 * @author M. Noyan Baykal
 * 
 * This is the main class. It just creates the MVC classes and starts the controller.
 */
public class Main {
	/**
	 * Starts the program.
	 * @param args Command line arguments. We do not need any.
	 */
	public static void main(String[] args) {
		Model model = new Model();
    	View view = new View(); 
        Controller controller = new Controller(model, view);
        
		controller.start();
	}
}
