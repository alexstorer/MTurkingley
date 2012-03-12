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
import java.io.*;

public class PayBonusCSV {

  /**
   * Main method
   *
   */
  public static void main(String[] args) {

	RequesterService service = new RequesterService(new PropertiesClientConfig("../mturk.properties"));

	// parse the input or something
	System.out.println("Opened requester.");

	String fname = args[0];
	String assignid = "";
	String workerid = "";
	Double bonusamt = 0.0;
	String reason   = "";
	String tot      = "";
	int    nbonus   =  0;
	Double totbonus = 0.0;


	try {
	    BufferedReader r = new BufferedReader(new FileReader(fname));
	    CSVReader c = new CSVReader(r);

	    String [] nextLine;
	    while ((nextLine = c.readNext()) != null) {
		// nextLine[] is an array of values from the line
		assignid = nextLine[0];//"2C21QP6AUC26ERQ7ZEO3Y0FK0QG81X";
		workerid = nextLine[1];//"A27ANNY9E0URA2";
		bonusamt = Double.parseDouble(nextLine[2]); //0.50;
		reason   = nextLine[3]; //"because i'm awesome that's why";
		System.out.println("Trying to construct a grant bonus request:");
		System.out.println("Assignment: "+assignid+"\nWorker: "+workerid+"\nBonus: "+nextLine[2]+"\nReason: "+reason);
		service.grantBonus(workerid,bonusamt,assignid,reason);
		System.out.println(".............................................Complete.");
		totbonus += bonusamt;
		nbonus++;
	    }
	    System.out.println("Awarded a total of $"+totbonus+" to "+nbonus+" workers.");

	} catch (Exception e) {
	    System.err.println("Error running PayBonusCSV on " + fname);
	    System.exit(1);
	}
  }
}
