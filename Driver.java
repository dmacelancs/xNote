public class Driver{

	public static void main(String args[]){

		try{

			Xnote xnote = new Xnote(args[0]); 
		} catch(Exception e){

			e.printStackTrace(); // Print the exception
		}
	}
}