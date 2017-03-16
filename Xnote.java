import java.lang.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.*;
import java.util.regex.*;
import org.apache.poi.*;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

public class Xnote{

	private String xnoteFilePath = ""; // XNote file path 
	private String outputFilePath = ""; // XNote file output path
	private String endFile = ""; // File to print to HTML at end 
	private static final String ANSI_RESET = "\u001b[0m"; // ANSI RESET \ Both for console
	private static final String ANSI_CYAN = "\u001b[36m"; // ANSI CYAN  / output colours

	private XWPFDocument outputDocX = new XWPFDocument(new FileInputStream("style-template.docx")); // Word document to output

	public Xnote(String xnoteFilePath, String outputFilePath) throws Exception{

		// Initalise 
		this.xnoteFilePath = xnoteFilePath; 
		this.outputFilePath = outputFilePath; 

		// Print init message
		sendOutput("xnote", "will open file: " + xnoteFilePath);

		// Parse the file
		parseFile();

		// Clean file?
		outputDocX.removeBodyElement(0); // Remove 1st para - stuck from template...

        // Ouput WORD
        outputWord();
	}

	/*
		* output a word document
	*/
	public void outputWord() throws Exception{

        // Write the document to the filesystem
        FileOutputStream out = new FileOutputStream(new File(outputFilePath));
        outputDocX.write(out);
        out.close();

        // Print status message
        sendOutput("xnote", "written word document: " + outputFilePath);
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

			// Create a new paragraph
			XWPFParagraph para = outputDocX.createParagraph();
			XWPFRun run = para.createRun();

			// Pattern Matching
			Pattern pat;
			Matcher match;

			// CHECK FOR HEADING 1
			pat = Pattern.compile("^##(.{0,})");
			match = pat.matcher(line);
			if (match.find()){
				line = line.replace(match.group(0), match.group(1));
				para.setStyle("Heading1");
			}

			// CHECK FOR HEADNIG 2
			pat = Pattern.compile("^_(.{0,})");
			match = pat.matcher(line);
			if (match.find()) {
				line = line.replace(match.group(0), match.group(1));
				para.setStyle("Heading2");
			}

			// CHECK FOR LIST ITEMS
			pat = Pattern.compile("^\t+- (.{0,})");
			match = pat.matcher(line);
			if (match.find()) {

				// CHECK TABS FROM LEFT...
				char[] chars = line.toCharArray();
				int p = 0;
				while(chars[p] == '\t')
					p++;

				line = line.replace(match.group(0), match.group(1));

				para.setStyle("ListParagraph"); // Set style to list paragraph
				para.setNumID(BigInteger.valueOf(4)); // Set NumID to bullets ?
				if(p>1)
					para.getCTP().getPPr().getNumPr().addNewIlvl().setVal(BigInteger.valueOf((p-1))); // Set level?

			}

			// Set run text
			run.setText(line);
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