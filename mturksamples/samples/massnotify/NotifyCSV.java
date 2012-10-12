/*
 * Copyright 2007-2008 Amazon Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://aws.amazon.com/apache2.0
 *
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 



package massnotify;

import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.service.exception.ServiceException;
import com.amazonaws.mturk.util.PropertiesClientConfig;
import com.amazonaws.mturk.requester.HIT;
import au.com.bytecode.opencsv.CSVReader;
import java.io.*;
import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.service.exception.ServiceException;
import com.amazonaws.mturk.util.PropertiesClientConfig;
import com.amazonaws.mturk.requester.HIT;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;
import java.util.ArrayList;



/**
 * The MTurk Hello World sample application creates a simple HIT via the Mechanical Turk 
 * Java SDK. mturk.properties must be found in the current file path.
 */
public class NotifyCSV extends JPanel
    implements ActionListener {

    private RequesterService service;

    // Defining the atributes of the HIT to be created
    static private final String newline = "\n";
    JButton openButton, saveButton;
    JTextArea log;
    JFileChooser fc;
    File loadedfile = new File("null");

    String fname = "";
    String assignid = "";
    String workerid = "";
    Double bonusamt = 0.0;
    String reason   = "";
    String tot      = "";
    int    nbonus   =  0;
    Double totbonus = 0.0;



    /**
     * Constructor
     * 
     */
    public NotifyCSV() {
        super(new BorderLayout());
	service = new RequesterService(new PropertiesClientConfig("../mturk.properties"));

        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(10,85);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        //Create a file chooser
        fc = new JFileChooser();

        //Uncomment one of the following lines to try a different
        //file selection mode.  The first allows just directories
        //to be selected (and, at least in the Java look and feel,
        //shown).  The second allows both files and directories
        //to be selected.  If you leave these lines commented out,
        //then the default mode (FILES_ONLY) will be used.
        //
        //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        openButton = new JButton("Choose a File...");
        openButton.addActionListener(this);

        //Create the save button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).

        saveButton = new JButton("Submit");
        saveButton.addActionListener(this);


        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);

    }


    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(NotifyCSV.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                loadedfile = fc.getSelectedFile();
                //This is where a real application would open the file.
		String subject  = "";
		String body     = "";

		String[] workerids = new String[1];

		try {
		    BufferedReader r = new BufferedReader(new FileReader(loadedfile));
		    CSVReader c = new CSVReader(r);
		    
		    log.append("Opening: " + loadedfile.getName() + "." + newline);

		    String [] nextLine;
		    int counter = 0;
		    while ((nextLine = c.readNext()) != null & counter<10) {
			// nextLine[] is an array of values from the line
			if (subject.length()==0)
			    {
				subject      = nextLine[0];
				body         = nextLine[1];
			    }
			workerids[0] = nextLine[2]; //0.50;
			log.append("\nPREVIEW> Notification:\n");
			log.append("PREVIEW>------------------------------------\n");
			log.append("PREVIEW> Subject: "+subject+"\nPREVIEW> Message: "+body+"\nPREVIEW> Worker: "+nextLine[2]+"\n");
			counter = counter+1;
			//service.notifyWorkers(subject,body,workerids);
		    }
		    if (counter == 10) {
			log.append("\n\n\n>>>>>>>>>>>>> ONLY FIRST 10 RECORDS PREVIEWED <<<<<<<<<<<<<\n\n\n");
			log.append("Please press 'Submit' to notify ALL workers in your file...");
		    }



		} catch (Exception ex) {
		    StringWriter w = new StringWriter();
		    ex.printStackTrace(new PrintWriter(w));
		    log.append(w.toString());
		    log.append("Error running NotifyCSV on " + loadedfile.getName()+"\n");
		    //quit?
		}


            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());

        } 
        else if (e.getSource() == saveButton) {
		String subject  = "";
		String body     = "";

		String[] workerids = new String[1];
		ArrayList<String> failedworkers;
		failedworkers = new ArrayList<String>();

		try {
		    BufferedReader r = new BufferedReader(new FileReader(loadedfile));
		    CSVReader c = new CSVReader(r);		   

		    String [] nextLine;
		    while ((nextLine = c.readNext()) != null) {
			// nextLine[] is an array of values from the line
			if (subject.length()==0)
			    {
				subject      = nextLine[0];
				body         = nextLine[1];
			    }
			workerids[0] = nextLine[2]; //0.50;
			try {
			    service.notifyWorkers(subject,body,workerids);
			    log.append("\nNotified user: "+workerids[0]);
			    System.out.println("Notified user: "+workerids[0]);
			} catch (Exception ex) {
			    failedworkers.add(workerids[0]);
			}
		    }
		    log.append(newline+newline+newline);
		    log.append("Submitted " + loadedfile.getName() + newline);

		} catch (Exception ex) {
		    StringWriter w = new StringWriter();
		    ex.printStackTrace(new PrintWriter(w));
		    log.append(w.toString());
		    log.append("Error running NotifyCSV on " + loadedfile.getName()+"\n");

		    //quit?
		}
		for ( String w : failedworkers  ){
		    log.append("NotifyCSV failed on " + w +"\n");
		}
            log.setCaretPosition(log.getDocument().getLength());
	    

	}
    }



    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = NotifyCSV.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Mechanical Turk: Send Messages in Batch");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
	NotifyCSV app = new NotifyCSV();
        frame.add(app);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }


    /**
     * Check if there are enough funds in your account in order to create the HIT
     * on Mechanical Turk
     * 
     * @return true if there are sufficient funds. False if not.
     */
    public boolean hasEnoughFund() {
	double balance = service.getAccountBalance();
	log.append("Got account balance: " + RequesterService.formatCurrency(balance));
	log.append(newline);
	return balance > 0;
    }


    /**
     * Main method
     * 
     * @param args
     */
    public static void main(String[] args) {

        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE); 
                createAndShowGUI();
            }
        });
    }
}
