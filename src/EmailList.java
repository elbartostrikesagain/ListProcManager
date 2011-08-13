import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;


public class EmailList {
	String name;
	String password;
	Vector<Member> members = new Vector<Member>();
	
	public EmailList(String listName, String listPassword) {
		name=listName;
		password=listPassword;
		addMembers(name);
	}

	private void addMembers(String name) {
		if(name.equals("error")){
			System.out.println("list error from config");
			return;
		}
		Boolean foundList = false;
		File listsDir = new File("src/lists");
		File files[] = listsDir.listFiles();
		for (File f : files) {
			String fileName= f.getName().toLowerCase();
			
			if(fileName.equals(name+".txt"))
				{
				foundList = true;
				break;
				}
		}
		//.svn hack
		if (name.equals(".svn"))
			return;
		if(!foundList){
			System.out.println("WARNING:list "+name+" not found. Creating list "+name+".txt");
			File file = new File("src/lists/"+name+".txt");
			boolean blnCreated = false;
		     try
		     {
		       blnCreated = file.createNewFile();
		     }
		     catch(IOException ioe)
		     {
		       System.out.println("Error while creating a new empty file :" + ioe);
		     }		 
		}
		else
		{
			try {
			    BufferedReader in = new BufferedReader(new FileReader("src/lists/"+name+".txt"));
			    String str;
			    while ((str = in.readLine()) != null) {
			    	String [] tempStr=str.split("\\s+");
			    	String tempName = "";
			    	for(int i=1; i<tempStr.length; ++i){
			    		if((i+1)==tempStr.length)
			    			tempName=tempName+tempStr[i];
			    		else
			    			tempName=tempName+tempStr[i]+ " ";
			    	}
			    		Member tempMember= new Member(tempName,tempStr[0]);
			    		//tempMember.inLists.add(name);
			    	members.add(tempMember);
			    }
			    in.close();
			} catch (IOException e) {
			}
		}
	}
}
