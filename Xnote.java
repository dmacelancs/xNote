import java.lang.*;
import java.io.*;
import java.nio.*;
import java.util.regex.*; 

public class Xnote{

	private String xnoteFilePath = ""; // Enote file path 
	private String endFile = ""; // File to print to HTML at end 
	private static final String ANSI_RESET = "\u001b[0m"; // ANSI RESET \ Both for console
	private static final String ANSI_CYAN = "\u001b[36m"; // ANSI CYAN  / output colours 

	public Xnote(String xnoteFilePath) throws Exception{

		// Initalise 
		this.xnoteFilePath = xnoteFilePath; 

		// Print init message
		sendOutput("xnote", "will open file: " + xnoteFilePath);

		// Parse the file
		parseFile(); 

		// Print the HTML
		printHTML(); 
	}

	/**
		* print output to HTML file
	*/
	public void printHTML() throws Exception{

		// Header and footer of HTML file (CSS ETC.)
		String header = "<!DOCTYPE HTML><html><head><link rel='stylesheet' href='http://bootswatch.com/journal/bootstrap.min.css' /><title>eNote Output</title></head><style>img{ max-width: 100%; vertical-align: middle; }</style><body><div class='container'>";
		String footer ="</div></body></html>";

		// Place compiled xNote file into HTML file
		PrintWriter output = new PrintWriter(xnoteFilePath + ".html");
		output.println(header + endFile + footer); 
		output.close(); 

		// Print status message
		sendOutput("xnote", "file has been output: " + xnoteFilePath + ".html");
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
				while(chars[p] == '\t')
					p++; 

				// ul start?  
				if(tabCount < p)
					endFile += "<ul>"; 

				// ul end? 
				if(tabCount > p)
					for(int t = 0; t < (tabCount - p); t++)
						endFile += "</ul>"; 
			}

			// Pattern Matching 
			Pattern pat; 
			Matcher match; 

			// CHECK FOR HEADNIG 1
			pat = Pattern.compile("##(.{0,})");
			match = pat.matcher(line);
			if (match.find())
				line = line.replace(match.group(0), "<h1>" + match.group(1) + "</h1>");

			// CHECK FOR HEADNIG 2
			pat = Pattern.compile("_(.{0,})");
			match = pat.matcher(line);
			if (match.find())
				line = line.replace(match.group(0), "<h2>" + match.group(1) + "</h2>");

			// CHECK FOR LIST ITEMS
			pat = Pattern.compile("^\t+-(.{0,})");
			match = pat.matcher(line);
			if (match.find())
				line = line.replace(match.group(0), "<li>" + match.group(1) + "</li>");

			// CHECK FOR IMAGES...
			pat = Pattern.compile("img:(.{0,});");
			match = pat.matcher(line);
			if (match.find())
				line = line.replace(match.group(0), "<img src='" + match.group(1) + "' />");

			// Add to end file output
			endFile = endFile + line + "\n"; 

			// Set tab count
			tabCount = p; 
		}

		// Close file
		fileIn.close(); 

		// Print status message
		sendOutput("xnote", "file has been parsed: " + xnoteFilePath);
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