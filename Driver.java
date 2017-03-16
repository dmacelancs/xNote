public class Driver{

	public static void main(String args[]){

		try{

			Xnote xnote; 

			// Check for mode..
			if(args.length == 1)
				xnote = new Xnote(args[0], args[0] + ".html"); 
			else if(args.length == 2)
			 	xnote = new Xnote(args[0], args[1]); 

		} catch(Exception e){

			e.printStackTrace(); // Print the exception
		}
	}
}