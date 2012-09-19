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


package massnotify;

import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.service.exception.ServiceException;
import com.amazonaws.mturk.util.PropertiesClientConfig;
import com.amazonaws.mturk.requester.HIT;
import au.com.bytecode.opencsv.CSVReader;
import com.amazonaws.mturk.requester.*;
import java.io.*;

public class QualifyCSV_text {

  /**
   * Main method
   *
   */
  public static void main(String[] args) {

	RequesterService service = new RequesterService(new PropertiesClientConfig("../mturk.properties"));

	// parse the input or something
	System.out.println("Opened requester.");

	String fname = args[0];
	String qual = args[1];
	String qt_id = "";
	String[] workerids = new String[1];

	try {
	    BufferedReader r = new BufferedReader(new FileReader(fname));
	    CSVReader c = new CSVReader(r);

	    //QualificationType qt=service.createQualificationType("QualTestName","qkw1, qkw2, qkw3","Do you speak spanish?",QualificationTypeStatus.Active, 300l, questionForm, "", 300l, false, null);

	    QualificationType qt=service.createQualificationType(qual,"keywords","Custom",QualificationTypeStatus.Active, null, null, null, null, null, null);
	    qt_id = qt.getQualificationTypeId();
	    String [] nextLine;
	    while ((nextLine = c.readNext()) != null) {
		// nextLine[] is an array of values from the line
		workerids[0] = nextLine[0];
		System.out.println("Trying to qualify a worker:");
		service.assignQualification(qt_id,nextLine[0],null,null);
		System.out.println("Qualification: "+qual+"\nWorker: "+nextLine[0]);
		System.out.println(".............................................Complete.");
	    }
	    //System.out.println("Awarded a total of $"+totbonus+" to "+nbonus+" workers.");

	} catch (Exception e) {
	    System.err.println(e);
	    System.err.println("Error running PayBonusCSV on " + fname);
	    System.exit(1);
	}
  }
}
