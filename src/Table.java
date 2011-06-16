import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Vector;

public class Table extends JPanel {
    private boolean DEBUG = true;

    public Table(Object[][] data) {
        super(new GridLayout(1,0));
		//System.out.println("Table List?");
		//BufferedReader brInput = new BufferedReader(new InputStreamReader(
		//		System.in));
		//String list = "";
		//try {
		//	list = brInput.readLine();
		//} catch (IOException e1) {
			// TODO Auto-generated catch block
		//	e1.printStackTrace();
		//}
		//EmailList emailList= InboxReader.findList(list);
        

        Vector<String> columnNamesVector = new Vector<String>();
        columnNamesVector.add("Name");
        columnNamesVector.add("Email");

        for (EmailList list:InboxReader.config.lists){
        	columnNamesVector.add(list.name);
        }
        String[] columnNames = (String[]) columnNamesVector.toArray(new String[]{});
/*
        //String[] columnNames = {"First Name", "Last Name", "Email", "ListName1", "ListName2"};
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
*/
        //
        //http://www.java2s.com/Code/Java/Swing-Components/multipleComponentTable2checkbox.htm
        //
        
        final JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(1200, 70));
        table.setFillsViewportHeight(true);

        if (DEBUG) {
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    printDebugData(table);
                }
            });
        }

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(scrollPane);
    }

	private void printDebugData(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();

        System.out.println("Value of data: ");
        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
   
}