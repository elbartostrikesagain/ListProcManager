import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;


public class Config {
	String email;
	String listProcEmail;
	Vector<EmailList> lists = new Vector<EmailList>();
	Vector<String> alsoAddTo = new Vector<String>();
	
	public Config(String fileName){

		try{
			    // Open the file that is the first 
			    // command line parameter
			    FileInputStream fstream = new FileInputStream("src/"+fileName+".txt");
			    // Get the object of DataInputStream
			    DataInputStream in = new DataInputStream(fstream);
			        BufferedReader br = new BufferedReader(new InputStreamReader(in));
			    String strLine;
			    //Read File Line By Line
			    while ((strLine = br.readLine()) != null)   {
			      // Print the content on the console
			      handleConfigLine(strLine);
			    }
			    //Close the input stream
			    in.close();
			    }catch (Exception e){//Catch exception if any
			      System.err.println("Error: " + e.getMessage());
			    }
	}
//TODO:add support for auto-add
//TODO:tests
	public void handleConfigLine(String strLine) {
		String[] line=strLine.split("\\s+");
		if(line.length < 1){
			return;
		}
		String command=line[0];
		EmailList list;
		
		if(command.equals("List")){
			if(line.length ==2)
				{list=new EmailList(line[1].toLowerCase(), "");}
			else if(line.length ==3)
				{list=new EmailList(line[1].toLowerCase(),line[2]);}
			else
			{list=new EmailList("error","");}
			
			lists.add(list);
		}
		else if(command.equals("Email")){
			email=line[1];
		}
		else if(command.equals("ListProcEmail")){
			listProcEmail=line[1];
		}
		else if(command.equals("AlsoAddTo")){
			alsoAddTo.add(line[1]);
		}
		else if(command.charAt(0)=='/' && command.charAt(1)=='/'){
			return;
		}
		else{
			System.err.println("Invalid command: " + command + ". Valid commands include \"List \", \"Email \", \"ListProcEmail \", \"AlsoAddTo \" and \"\\\\\" " );
		}
	}
}
