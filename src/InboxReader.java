import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Vector;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class InboxReader {
	
	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final int SMTP_HOST_PORT = 465;
	public static String SMTP_AUTH_PWD = "";
	static Properties props = System.getProperties();
	static EmailList currentList;
	//Name of config file without .txt on the end (ex. configTest)
	static Config config= new Config("config");
	static Vector<EmailList> lists;// = config.lists;
	//static Vector<String> alsoAddTo = config.alsoAddTo;
	public static String SMTP_AUTH_USER = config.email;
    public static String SMTP_LIST_PROC = config.listProcEmail;
	
	//Constructor
	public InboxReader(){
		//config = new Config("config");
		lists = config.lists;
		//alsoAddTo = config.alsoAddTo;
		SMTP_AUTH_USER = config.email;
		SMTP_LIST_PROC = config.listProcEmail;
	}

	public static void main(String args[]) throws Exception {
		 System.out.println(SMTP_AUTH_USER + " config.email:"+config.email);
		 loadLookAndFeel();
		LoginDialog test = new LoginDialog(config.email);
		SMTP_AUTH_PWD = test.password;

		startConsole();
		System.out.println("Session Closed");
	}



	static void startConsole() throws Exception {
		
        //try {
		//    new Console();
		//} 
		//catch (IOException e) {
	    //	}
		//TODO:extract to startEmailConnection()
		props.setProperty("mail.store.protocol", "imaps");
		
		try {
			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", config.email, SMTP_AUTH_PWD);
			System.out.println(store);
			Folder inbox = store.getFolder("Inbox");
			inbox.open(Folder.READ_WRITE);
			// Message messages[] = inbox.getMessages();
			//TODO:the the below function
			getJoinRequestsAndAdd(inbox);
			String input = "";
			while (!(input.equals("end") || input.equals("exit"))) {
				System.out.println("--------");
				input = getInput();
				if (input.equals("delete")) {
					System.out.println("Email?");
					String email = getInput();
					System.out.println("List?");
					String list = getInput();
					
					deleteFromList(findList(list), email, false);
				} else if (input.equals("deleteAll")) {
					System.out.println("List?");
					String list = getInput();

					deleteAllFromList(findList(list));
				}

				else if (input.equals("add")) {
					System.out.println("Email?");
					String email = getInput();
					System.out.println("List?");
					String list = getInput();
					System.out.println("Name?");
					String name = getInput();
					addToList(findList(list), email, name, false);

				} else if (input.equals("print")) {
					System.out.println("List?");
					String list = getInput();
					printList(findList(list));
				} else if (input.equals("import")) {
					System.out.println("file directory?");
					String dir = getInput();
					System.out.println("List?");
					String list = getInput();
					bulkImport(dir, list);
				} else if (input.equals("gui")) {
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							createAndShowGUI();
						}
					});
				} else if (input.equals("exit")) {
					//TODO Close mail connections
				} 
				else if (input.equals("help"))
					System.out
							.println("Commands are: gui, print, add, delete, deleteAll, import, exit");
				else {
					System.out
							.println("Command not found. Commands are: gui, print, add, delete, deleteAll, import, exit");
				}
			}

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (MessagingException e) {
			e.printStackTrace();
			System.exit(2);
		}
	}
//TODO:test
	static void getJoinRequestsAndAdd(Folder inbox) throws MessagingException,
			IOException, NoSuchProviderException, AddressException {
		Session session;
		Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));// unread messages
		// inbox.open(Folder.READ_WRITE);
		for (Message inboxMessage : messages) {
			// System.out.println("subject:"+inboxMessage.getSubject());
			// http://www.vipan.com/htdocs/javamail.html

			Object o = inboxMessage.getContent();
			if (o instanceof String) {
				try{
				if (inboxMessage.getSubject().equals("Subscription approval request")) {
					String test = inboxMessage.getContent().toString();
					System.out.println("content:" + test);

					String[] tempStr = ((String) inboxMessage.getContent()).split("\\s+");
					addToList(findList(tempStr[20]), tempStr[22], tempStr[23]+ " " + tempStr[24],false);

					// TODO Needs testing
					for (String list2AlsoAdd : config.alsoAddTo) {
						addToList(findList(list2AlsoAdd), tempStr[22],tempStr[23] + " " + tempStr[24],false);
					}

					// String sendContent = "add mamabird-tryouts 4birdtryouts "
					// + tempStr[22] + " " + tempStr[23] + " " + tempStr[24];
					// inbox.open(Folder.READ_WRITE);
					// inbox.getMessage(inboxMessage.getMessageNumber()).getContent();
					// inbox.close(false);

					// sendMail(sendContent);
					}
				}catch(Exception e) {
					//System.out.println("blankEmailSubject");
					
				}
			} 
			else if (o instanceof Multipart) {
				System.out.print("**This is a Multipart Message.  ");
				multiPartMessage(o);
			}

			// inbox.open(Folder.READ_WRITE);
			// inbox.getMessage(inboxMessage.getMessageNumber()).getContent();
			// inbox.close(false);

		}
		inbox.close(false);
	}
//TODO:test
	private static void multiPartMessage(Object o) throws MessagingException,
			IOException {
		Multipart mp = (Multipart) o;
		for (int j = 0; j < mp.getCount(); j++) {
			// Part are numbered starting at 0
			BodyPart b = mp.getBodyPart(j);
			Object o2 = b.getContent();
			if (o2 instanceof String) {
				String[] tempStr = ((String) o2).split("\\s+");
				// System.out.println("THIS IS THE CONTENTS:"+(String) o2);
				addToList(findList(tempStr[20]), tempStr[22], tempStr[23] + " "+ tempStr[24], false);

				for (String list2AlsoAdd : config.alsoAddTo) {
					addToList(findList(list2AlsoAdd), tempStr[22], tempStr[23]+ " " + tempStr[24],false);
				}
			} else if (o2 instanceof Multipart) {
				System.out.print("**This BodyPart is a nested Multipart.  ");
				Multipart mp2 = (Multipart) o2;
				int count2 = mp2.getCount();
				System.out.println("It has " + count2
						+ "further BodyParts in it**");
				multiPartMessage(o2);
			} else if (o2 instanceof InputStream) {
				System.out.println("**This is an InputStream BodyPart**");
			}
		} // End of for
	}
//TODO:test file update
	public static void deleteAllFromList(EmailList list) {
		System.out.println("Are you sure you want delete all members on the list "+ list.name + "?(yes/no)");
		String input = getInput();
		if (input.equals("yes")) {
			System.out.println("list members:" + list.members.size());
			// Hack to convert to array because 2 threads modify the vector otherwise
			Member[] membersArray = (Member[]) list.members.toArray(new Member[0]);
			for (int i = 0; i < membersArray.length; ++i) {
				deleteFromList(list, membersArray[i].email, true);
			}
		}
	}
//TODO:test
	private static void bulkImport(String fileName, String list) {
		// TODO Auto-generated method stub
		String line;
		File file = new File("C:\\Users\\Calvin\\workspace\\GmailImap\\src\\"
				+ fileName + ".txt");
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		try {
			fis = new FileInputStream(file);

			// Here BufferedInputStream is added for fast reading.
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			// dis.available() returns 0 if the file does not have more lines.
			while (dis.available() != 0) {

				// this statement reads the line from the file and print it to
				// the console.
				line = dis.readLine();
				String[] temp = null;
				temp = line.split(" ");
				addToList(findList(list), temp[0], temp[1] + temp[2], true);
			}

			// dispose all the resources after using them.
			fis.close();
			bis.close();
			dis.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
//TODO:Finish implementation
	static void createAndShowGUI() {		
        Object[][] data = getMemberDataForGUI();
        Vector<String> columnNamesVector = new Vector<String>();
        columnNamesVector.add("Name");
        columnNamesVector.add("Email");

        for (EmailList list:InboxReader.config.lists){
        	columnNamesVector.add(list.name);
        }
        String[] columnNames = (String[]) columnNamesVector.toArray(new String[]{});
        
        TableFilterDemo t2 = new TableFilterDemo(columnNames,data);
        t2.createAndShowGUI(columnNames,data);
		
		/*Table2 t = new Table2();
		
		// Create and set up the window.
		JFrame frame = new JFrame("SimpleTableDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		Table newContentPane = new Table(data);
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
		*/
	}

public static Object[][] getMemberDataForGUI() {
	Vector<Member> members = getMemebersList();
	Object[][] data = new Object[members.size()][InboxReader.config.lists.size()+2];
	
	for(int i=0; i < members.size(); i++){
		data[i][0] = members.elementAt(i).name;
		data[i][1] = members.elementAt(1).email;
		for(int j =0; j < members.elementAt(i).inLists.size()-1; j++){
			if(members.elementAt(i).inLists.elementAt(j)!=null){
				System.out.println("members.elementAt(i).inLists.elementAt(j)"+members.elementAt(i).inLists.elementAt(j));
				data[i][j+2] = members.elementAt(i).inLists.elementAt(j);
			}
		}
	}
	return data;
}
//TODO:Test	
	private static Vector<Member> getMemebersList() {
		Vector<Member> members = new Vector<Member>();
		int i=0;
		for(EmailList list:InboxReader.config.lists){
			for(Member listMember:list.members){
				//[0]=index of member // {1]=if member was found
				Object[] indexAndFound =findMember(members, listMember, list.name);
				if((Boolean)indexAndFound[1] ==false){
					members.add(listMember);
					for(int k=0; k < InboxReader.config.lists.size(); k++){						
							members.lastElement().inLists.add(false);
					}
					members.lastElement().inLists.add(i, true);
				}
				else{
					members.elementAt((Integer) indexAndFound[0]).inLists.add(i, true);
					int lastElementIndex = members.elementAt((Integer) indexAndFound[0]).inLists.size()-1;
					members.elementAt((Integer) indexAndFound[0]).inLists.remove(lastElementIndex);
				}
			}
			i++;
		}
		return members;		
	}
	
	private static Object[] findMember(Vector<Member> membersList, Member memberLookingFor, String listName) {
		Boolean found=false;
		int index=0;
		for(Member m:membersList){
			if (m.email.equals(memberLookingFor.email)){
				//m.inLists.add(listName);
				found = true;
				break;
			}
			index++;	
		}

		Object[]  r= {index,found};
	return r;
}
	
	private static void printList(EmailList list) {
		for (Member member : list.members) {
			System.out.println(member.name + "        " + member.email);
		}
	}
//TODO:test file update
	public static void addToList(EmailList list, String email, String name, boolean forceAdd) {
		String content = "add " + list.name + " " + list.password + " " + email + " " + name;
		try {
			if (listDoesNotAlreadyContain(list, email)) {
				sendMail(content, forceAdd);
				Member tempMember = new Member(name, email);
				list.members.add(tempMember);
				addToTextFile(name, email, list);
			} else
				System.out.println("\nList " + list.name
						+ " already contains email " + email);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void addToTextFile(String name, String email, EmailList list) {
		FileWriter myFileWriter = null;

		try {
			myFileWriter = new FileWriter(
					"C:\\Users\\Calvin\\workspace\\GmailImap\\src\\" + list.name + ".txt", true);
			myFileWriter.write('\n');
			myFileWriter.write(email + " " + name);
			myFileWriter.close();
		}

		catch (Exception e) {
			System.out.println("EXCEPTION thrown ");
		}
	}

	private static void deleteFromList(EmailList list, String email, Boolean forceDelete) {
		//TODO deleteFromList is not deleting from file
		String content = "delete " + list.name + " " + list.password + " " + email;
		try {

			Member tempMember = new Member(getNameFromEmail(list, email), email);
			int index = 0;
			Boolean found = false;
			for (Member member : list.members) {
				if (member.email.equals(email)) {
					found = true;
					break;
				}
				index++;

			}
			if (found) {
				sendMail(content, forceDelete);
				list.members.remove(index);
				deleteFromFile(list, email);
			} else
				System.out.println("ERROR:Email not found to delete.");
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//TODO:handle upper and lowercase
	private static String getNameFromEmail(EmailList list, String email) {
		for (Member member : list.members) {
			if (member.email.equals(email)) {
				return member.name;
			}
		}
		return ("Email not found in list " + list.name);
	}
//TODO:test
	static EmailList findList(String list2find) {
		String listNames = "";
		for (EmailList list : config.lists) {
			if (list.name.equals(list2find.toLowerCase())) {
				return list;
			} else
				listNames = listNames + " " + list.name;
		}

		System.out.println("list " + list2find + " does not exist. List names are " + listNames);
		return (findList(getInput()));
	}
//TODO:test, extract openSendMailSession, closeSendMailSession
	private static void sendMail(String sendContent, Boolean force)
			throws NoSuchProviderException, MessagingException,
			AddressException {
		Session session;
		if(!force){
			//TODO don't display sendContent Text in Clear Text
			System.out.println("\nConfirm outgoing mail(y/n):" + sendContent);
			String input = getInput();
			if(input.equals("n"))
				return;
		}
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtps.host", SMTP_HOST_NAME);
		props.put("mail.smtps.auth", "true");

		session = Session.getDefaultInstance(props);
		session.setDebug(false);

		Transport transport = session.getTransport();

		MimeMessage sendMessage = new MimeMessage(session);
		sendMessage.setSubject("");
		sendMessage.setContent(sendContent, "text/plain");

		sendMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(config.listProcEmail));

		transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, config.email,SMTP_AUTH_PWD);

		transport.sendMessage(sendMessage, sendMessage.getRecipients(Message.RecipientType.TO));
		transport.close();
	}
//TODO:test
	private static boolean listDoesNotAlreadyContain(EmailList list, String email) {
		Vector<Member> members = list.members;
		for (Member m : members) {
			if (m.email.equals(email)) {
				return false;
			}
		}
		return true;
	}
//TODO:test
	public static void deleteFromFile(EmailList list, String email) {
		String file = "C:\\Users\\Calvin\\workspace\\GmailImap\\src\\"+ list.name + ".txt";
		String emailToRemove = email;
		try {

			File inFile = new File(file);

			if (!inFile.isFile()) {
				System.out.println("Parameter is not an existing file");
				return;
			}

			// Construct the new file that will later be renamed to the original
			// filename.
			File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;

			// Read from the original file and write to the new
			// unless content matches data to be removed.
			while ((line = br.readLine()) != null) {
				String[] tempStr = line.split("\\s+");
				String emailInTXTdoc = tempStr[0];

				if (!emailInTXTdoc.equals(emailToRemove)) {

					pw.println(line);
					pw.flush();
				}
			}
			pw.close();
			br.close();

			// Delete the original file
			if (!inFile.delete()) {
				System.out.println("Could not delete file");
				return;
			}

			// Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(inFile))
				System.out.println("Could not rename file");

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	static String getInput() {
		BufferedReader brInput = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		try {
			input = brInput.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return input;
	}
	
	private static void loadLookAndFeel() 
	throws ClassNotFoundException, InstantiationException, IllegalAccessException,UnsupportedLookAndFeelException {
		try{
	//infoNode
		/*
		InfoNodeLookAndFeelTheme theme =
	        new InfoNodeLookAndFeelTheme("My Theme",
	                                     new Color(110, 120, 150),
	                                     new Color(0, 170, 0),
	                                     new Color(80, 80, 80),
	                                     Color.WHITE,
	                                     new Color(0, 170, 0),
	                                     Color.WHITE,
	                                     0.8); 
	    UIManager.setLookAndFeel(new InfoNodeLookAndFeel(theme));
	    */
	    
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		//UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        //UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
		}catch (Exception e){
			System.out.println("ERROR:Theme not found");
		}
/*
		try {
			    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			        if ("Nimbus".equals(info.getName())) {
			        	System.out.println("info.getClassName():"+info.getClassName());
			            UIManager.setLookAndFeel(info.getClassName());
			            break;
			        }
			    }
			} catch (Exception e) {
				System.out.println("ERROR:Nimus not found");
				UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			}
			*/
	}
}