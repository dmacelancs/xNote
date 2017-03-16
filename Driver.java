public class Driver{

	public static void main(String args[]){

		try{

			Xnote xnote = new Xnote("sample.xnote"); 
		} catch(Exception e){

			e.printStackTrace(); // Print the exception
		}
	}
}