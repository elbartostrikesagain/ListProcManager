import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventObject;
import java.util.Hashtable;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * @version 1.0 11/09/98
 */
class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {

  CheckBoxRenderer() {
    setHorizontalAlignment(JLabel.CENTER);
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    if (isSelected) {
      setForeground(table.getSelectionForeground());
      //super.setBackground(table.getSelectionBackground());
      setBackground(table.getSelectionBackground());
    } else {
      setForeground(table.getForeground());
      setBackground(table.getBackground());
    }
    setSelected((value != null && ((Boolean) value).booleanValue()));
    return this;
  }
}

public class MultiComponentTable2 extends JFrame {

  public MultiComponentTable2() {
    super("MultiComponent Table");

    DefaultTableModel dm = new DefaultTableModel() {
      public boolean isCellEditable(int row, int column) {
        if (column == 0 || column == 1) {
          return true;
        }
        return false;
      }
    };
    dm.setDataVector(new Object[][] {
        { new Boolean(true), new Boolean(true), "JLabel", "JComboBox" },
        { new Boolean(true), "String", "JLabel", "JComboBox" },
        { new Boolean(true), "Boolean", "JCheckBox", "JCheckBox" },
        { new Boolean(false), "Boolean", "JCheckBox", "JCheckBox" },
        { new Boolean(true), "String", "JLabel", "JTextField" },
        { "test", "String", "JLabel", "JTextField" } }, new Object[] {
        "Component", "Data", "Renderer", "Editor" });

    CheckBoxRenderer checkBoxRenderer = new CheckBoxRenderer();
    EachRowRenderer rowRenderer = new EachRowRenderer();
    rowRenderer.add(1, checkBoxRenderer);
    rowRenderer.add(2, checkBoxRenderer);
    rowRenderer.add(3, checkBoxRenderer);
    JComboBox comboBox = new JComboBox();
    comboBox.addItem("true");
    comboBox.addItem("false");
    JCheckBox checkBox = new JCheckBox();
    checkBox.setHorizontalAlignment(JLabel.CENTER);
    DefaultCellEditor comboBoxEditor = new DefaultCellEditor(comboBox);
    DefaultCellEditor checkBoxEditor = new DefaultCellEditor(checkBox);
    JTable table = new JTable(dm);

// modified by java2s.com

    EachRowEditor rowEditor = new EachRowEditor(table);
    rowEditor.setEditorAt(0, checkBoxEditor);
    rowEditor.setEditorAt(1, checkBoxEditor);
    rowEditor.setEditorAt(2, checkBoxEditor);
    rowEditor.setEditorAt(3, checkBoxEditor);

// end
    
    table.getColumn("Component").setCellRenderer(rowRenderer);
    table.getColumn("Component").setCellEditor(rowEditor);

    JScrollPane scroll = new JScrollPane(table);
    getContentPane().add(scroll);
    setSize(400, 160);
    setVisible(true);
  }

  public static void main(String[] args) {
    MultiComponentTable2 frame = new MultiComponentTable2();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
  }
}

/**
 * @version 1.0 11/09/98
 */

class EachRowRenderer implements TableCellRenderer {
  protected Hashtable renderers;

  protected TableCellRenderer renderer, defaultRenderer;

  public EachRowRenderer() {
    renderers = new Hashtable();
    defaultRenderer = new DefaultTableCellRenderer();
  }

  public void add(int row, TableCellRenderer renderer) {
    renderers.put(new Integer(row), renderer);
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    renderer = (TableCellRenderer) renderers.get(new Integer(row));
    if (renderer == null) {
      renderer = defaultRenderer;
    }
    return renderer.getTableCellRendererComponent(table, value, isSelected,
        hasFocus, row, column);
  }
}

/**
 * each row TableCellEditor
 * 
 * @version 1.1 09/09/99
 * @author Nobuo Tamemasa
 */

class EachRowEditor implements TableCellEditor {
  protected Hashtable editors;

  protected TableCellEditor editor, defaultEditor;

  JTable table;

  /**
   * Constructs a EachRowEditor. create default editor
   * 
   * @see TableCellEditor
   * @see DefaultCellEditor
   */
  public EachRowEditor(JTable table) {
    this.table = table;
    editors = new Hashtable();
    //defaultEditor = new DefaultCellEditor(new JTextField());
    defaultEditor = new DefaultCellEditor(new JCheckBox());

    
  }

  /**
   * @param row
   *            table row
   * @param editor
   *            table cell editor
   */
  public void setEditorAt(int row, TableCellEditor editor) {
    editors.put(new Integer(row), editor);
  }

  public Component getTableCellEditorComponent(JTable table, Object value,
      boolean isSelected, int row, int column) {
    //editor = (TableCellEditor)editors.get(new Integer(row));
    //if (editor == null) {
    //  editor = defaultEditor;
    //}
    return editor.getTableCellEditorComponent(table, value, isSelected,
        row, column);
  }

  public Object getCellEditorValue() {
    return editor.getCellEditorValue();
  }

  public boolean stopCellEditing() {
    return editor.stopCellEditing();
  }

  public void cancelCellEditing() {
    editor.cancelCellEditing();
  }

  public boolean isCellEditable(EventObject anEvent) {
    selectEditor((MouseEvent) anEvent);
    return editor.isCellEditable(anEvent);
  }

  public void addCellEditorListener(CellEditorListener l) {
    editor.addCellEditorListener(l);
  }

  public void removeCellEditorListener(CellEditorListener l) {
    editor.removeCellEditorListener(l);
  }

  public boolean shouldSelectCell(EventObject anEvent) {
    selectEditor((MouseEvent) anEvent);
    return editor.shouldSelectCell(anEvent);
  }

  protected void selectEditor(MouseEvent e) {
    int row;
    if (e == null) {
      row = table.getSelectionModel().getAnchorSelectionIndex();
    } else {
      row = table.rowAtPoint(e.getPoint());
    }
    editor = (TableCellEditor) editors.get(new Integer(row));
    if (editor == null) {
      editor = defaultEditor;
    }
  }
}