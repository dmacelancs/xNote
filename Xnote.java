import java.lang.*;
import java.io.*;
import java.nio.*;
import java.util.regex.*; 

public class Xnote{

	private String xnoteFilePath = ""; // Enote file path 
	private String endFile = ""; // File to print to HTML at end 
	private static final String ANSI_RESET = "\u001b[0m"; // ANSI RESET
	private static final String ANSI_CYAN = "\u001b[36m"; // ANSI CYAN

	public Xnote(String xnoteFilePath){

		// Initalise 
		this.xnoteFilePath = xnoteFilePath; 

		// Print init message
		sendOutput("enote", "will open file: " + xnoteFilePath);

		// Parse the file and output HTML
		try{

			// Parse the file
			parseFile(); 
			
			// Print status message
			sendOutput("xnote", "file has been parsed: " + xnoteFilePath);

			// Print the HTML
			printHTML(); 

			// Print status message
			sendOutput("xnote", "file has been output: " + xnoteFilePath + ".html");

		} catch(Exception e){

			e.printStackTrace(); 
		}
	}

	/**
		* print output to HTML file
	*/
	public void printHTML() throws Exception{

		String header = "<!DOCTYPE HTML><html><head><link rel='stylesheet' href='http://bootswatch.com/journal/bootstrap.min.css' /><title>eNote Output</title></head><style>img{ max-width: 100%; vertical-align: middle; }</style><body><div class='container'>";
		String footer ="</div></body></html>";

		PrintWriter output = new PrintWriter(xnoteFilePath + ".html");
		output.println(header + endFile + footer); 
		output.close(); 
	}

	/**
		* parse the eNote file
	*/
	public void parseFile() throws Exception{

		// Read the file line by line... 
		BufferedReader fileIn = new BufferedReader(new FileReader(xnoteFilePath));

		String line = ""; 
		int tabCount = 0;
		while((line = fileIn.readLine()) != null){

			// Split line into array
			char[] chars = line.toCharArray(); 

			// Current line tab counter
			int p = 0;	

			// Run though each line char by char 
			if(chars.length > 0){

				//CHECK TABS 
				while(chars[p] == '\t'){
					p++; 
				}

				// ul start?  
				if(tabCount < p){
					endFile += "<ul>"; 
				}

				// ul end? 
				if(tabCount > p){
					for(int t = 0; t < (tabCount - p); t++){
						endFile += "</ul>"; 
					}
				}
			}

			// CHECK FOR HEADNIG 1
			Pattern patH1 = Pattern.compile("##(.{0,})");
			Matcher mH1 = patH1.matcher(line);
			if (mH1.find())
				line = line.replace(mH1.group(0), "<h1>" + mH1.group(1) + "</h1>");

			// CHECK FOR HEADNIG 2
			Pattern patH2 = Pattern.compile("_(.{0,})");
			Matcher mH2 = patH2.matcher(line);
			if (mH2.find())
				line = line.replace(mH2.group(0), "<h2>" + mH2.group(1) + "</h2>");

			// CHECK FOR LIST ITEMS
			Pattern patLI = Pattern.compile("^\t+-(.{0,})");
			Matcher mLI = patLI.matcher(line);
			if (mLI.find())
				line = line.replace(mLI.group(0), "<li>" + mLI.group(1) + "</li>");

			// CHECK FOR IMAGES...
			Pattern pat = Pattern.compile("img:(.{0,});");
			Matcher m = pat.matcher(line);
			if (m.find())
				line = line.replace(m.group(0), "<img src='" + m.group(1) + "' />");

			// Add to end file
			endFile = endFile + line + "\n"; 

			// Set tab count
			tabCount = p; 

			// Print the line to console
			// System.out.println(line);
		}

		// Close file
		fileIn.close(); 
	}

	/**
		* sendOutput
		* prints out message to terminal 
	*/
	public void sendOutput(String header, String content){

		// Print out message
		System.out.println("[" + ANSI_CYAN + header + ANSI_RESET + "]" + " " + content);
	}
}