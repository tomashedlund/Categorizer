import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;



public class AddElement extends JFrame {
	private static final long serialVersionUID = -8255319694373975038L;
	private JTextArea area = new JTextArea(4,40);
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JList<String> list = new JList<String>(listModel);
	private JButton removeCategory = new JButton("Remove");
	private JButton addCategory = new JButton("Add Category");
	private JButton addElement = new JButton("Add this quotation");
	private JTextField categoryName = new JTextField(11);
	private Window parentWindow;
	private Element element = null;
	
	public AddElement(Window parentWindow){
		super("Add quotation id " + Element.getNumberOfElements());
		
		this.parentWindow = parentWindow;
		window();
	}
	public AddElement(Window parentWindow, Element element){
		super("Edit quotation id: " + element.getId());
		
		this.parentWindow = parentWindow;
		this.element = element;
		for (String s : element.getCategories())
			listModel.addElement(s);
		area.setText(element.getQuotation());
		addElement.setText("Save changes");
		window();
	}
	private void window(){
		JPanel center = new JPanel();
		center.setLayout(new BorderLayout());
		area.setFont(new Font("Arial",Font.PLAIN,12));
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		center.add(new JLabel("Quotations:"),BorderLayout.NORTH);
		center.add(scroll,BorderLayout.CENTER);
		add(center,BorderLayout.CENTER);
		
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);

		list = new JList<String>(listModel);
		
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(150, 90));
		
		JPanel east = new JPanel();
		east.setLayout(new BorderLayout());
		east.add(new JLabel("Categories:"),BorderLayout.NORTH);
		east.add(listScroller,BorderLayout.CENTER);
		east.add(removeCategory,BorderLayout.SOUTH);
		removeCategory.addActionListener(new RemoveCategoryListener());
		if (listModel.getSize() == 0) 
	        removeCategory.setEnabled(false);
		add(east,BorderLayout.EAST);
		
		JPanel south = new JPanel();
		south.setLayout(new FlowLayout());
		south.add(addElement);
		addElement.addActionListener(new AddElementListener());
		south.add(Box.createRigidArea(new Dimension(100,0)));
		south.add(addCategory);
		south.add(categoryName);
		addCategory.addActionListener(new AddCategoryListener());
		add(south,BorderLayout.SOUTH);
		
		
		//	INIT. WINDOW
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		this.addWindowListener(new ListenOnQuit());
		setLocation(100,50);
		setVisible(true);
		//	END OF INIT. WINDOW
	}
	private class RemoveCategoryListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
		    int index = list.getSelectedIndex();
		    int size = listModel.getSize();
		    
		    if(index >= 0 && index <= size)
		    	listModel.remove(index);
		    else
		    	return;
		    
		    size = listModel.getSize();
		    
		    if (size == 0) 
		        removeCategory.setEnabled(false);
		    else { //Select an index.
		        if (index == listModel.getSize()) {
		            //removed item in last position
		            index--;
		        }
	
		        list.setSelectedIndex(index);
		        list.ensureIndexIsVisible(index);
		    }
		}
	}
	private class AddCategoryListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
		    String name = categoryName.getText();
	
		    //User did not type in a unique name...
		    if (name.equals("")) {
		        Toolkit.getDefaultToolkit().beep();
		        categoryName.requestFocusInWindow();
		        categoryName.selectAll();
		        return;
		    }
	
		    int index = list.getSelectedIndex(); //get selected index
		    if (index == -1) { //no selection, so insert at beginning
		        index = 0;
		    } else {           //add after the selected item
		        index++;
		    }
	
		    listModel.insertElementAt(categoryName.getText(), index);
	
		    //Reset the text field.
		    categoryName.requestFocusInWindow();
		    categoryName.setText("");
	
		    //Select the new item and make it visible.
		    list.setSelectedIndex(index);
		    list.ensureIndexIsVisible(index);
		    removeCategory.setEnabled(true);
		}
	}
	private class AddElementListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			Set<String> categories = new HashSet<String>();
			for(int i=0; i < listModel.getSize(); i++)
			     categories.add(listModel.getElementAt(i));  
			if(element == null)
				element = new Element(area.getText(), categories);
			else{
				element.setQuotation(area.getText());
				element.setCategories(categories);
			}
			parentWindow.addElement(element);
			dispose();
		}
	}
	private class ListenOnQuit extends WindowAdapter {
		public void windowClosing(WindowEvent e){
			dispose();
		}
	}
}
