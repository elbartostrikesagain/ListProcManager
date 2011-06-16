import java.util.Vector;


public class Member {
	String name;
	String email;
	Vector<Boolean> inLists = new Vector<Boolean>();
	
	public Member(String nameIn, String emailIn) {
		email=emailIn;
		name=nameIn;
	}
	//I don't think is this used anywhere?
	//public void addToList(String listName, String listPassword){
	//	String sendContent="add "+listName+" "+ listPassword+ " " + email + " " + name;				
	//}
	//public void DeleteFromList(String listName, String listPassword){
	//	String sendContent="delete "+listName+" "+ listPassword+ " " + email;
	//}

}
