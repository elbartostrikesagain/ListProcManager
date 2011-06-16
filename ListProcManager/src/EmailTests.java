import java.util.Properties;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;



public class EmailTests extends TestCase {
	InboxReader reader;
	EmailList list;
	EmailList currentList;
	Config config;
    /**
     * setUp() method that initializes common objects
     */
    protected void setUp() throws Exception {
        super.setUp();
        reader = new InboxReader();
    	config = new Config("configTest");
    	list = config.lists.elementAt(0);
    	InboxReader.SMTP_AUTH_PWD = "4mamabird";
    }

    /**
     * tearDown() method that cleanup the common objects
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        reader= null;
        list=null;
    }

    /**
     * Constructor for BookTest.
     * @param name
     */
    public EmailTests(String name) {
        super(name);
    }
    public static Test suite(){
        TestSuite suite = new TestSuite();
        suite.addTest(new EmailTests("testListCreate"));
        suite.addTest(new EmailTests("testConfig"));
        suite.addTest(new EmailTests("IrConstructor"));
        suite.addTest(new EmailTests("addToList"));
        suite.addTest(new EmailTests("deleteAllFromList"));


  	return suite;
  }
    
//EmailList.java
    public void testListCreate(){
        assertTrue(list.members.size()==4);
        assertTrue(list.name.equals("testlist"));
        assertTrue(list.password.equals("testListPassword"));
    }
//Config.java
    public void testConfig(){
    	assertTrue(config.email.equals("mamabirdlist@gmail.com"));
    	assertTrue(config.listProcEmail.equals("mamabirdlist@gmail.com"));
    	assertTrue(config.lists.size()==5);
    	assertTrue(config.lists.elementAt(4).name.equals("test4"));
    	assertTrue(config.alsoAddTo.size()==2);
    }
//InboxReader.java constructor
    public void IrConstructor(){
    	System.out.println("IRCONST "+InboxReader.config.email);
    	assertTrue(InboxReader.config.email.equals(config.email));
    	assertTrue(InboxReader.lists.size() == config.lists.size());
    }
//InboxReader.java addToList
    public void addToList(){
    	InboxReader.addToList(config.lists.elementAt(4), "test@unit.com", "Unit Test", true);
    	assertTrue(config.lists.elementAt(4).members.lastElement().email.equals("test@unit.com"));
    }
//InboxReader.java constructor
    public void deleteAllFromList(){
    	assertFalse(config.lists.elementAt(4).members.size()==0);
    	InboxReader.deleteAllFromList(config.lists.elementAt(4));
    	assertTrue(config.lists.elementAt(4).members.size()==0);
    }
    
}
