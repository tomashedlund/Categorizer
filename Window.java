import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;


public class Window extends JFrame {
	private static final long serialVersionUID = -5728114595696525310L;
	private JTextPane area = new JTextPane();
	StyledDocument doc = area.getStyledDocument();
	Style style = area.addStyle("I'm a Style", null);
	private JList<String> list;
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JButton addElement = new JButton("Add new quotation");
	private JButton editElement = new JButton("Edit");
	private JButton save = new JButton("Save");
	private JButton load = new JButton("Load");
	List<Element> elements = new ArrayList<Element>();
	JFileChooser chooseFileWindow = new JFileChooser();
	
	public Window(){
		super("Simple categories");
		
		JPanel center = new JPanel();
		center.setLayout(new BorderLayout());
		area.setFont(new Font("Arial",Font.PLAIN,12));
		area.setPreferredSize(new Dimension(600,500));
//		area.setLineWrap(true);
//		area.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		
		center.add(new JLabel("Quotations:"),BorderLayout.NORTH);
		center.add(scroll,BorderLayout.CENTER);
		add(center,BorderLayout.CENTER);
		
		list = new JList<String>(listModel);
		
//		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setSelectionModel(new DefaultListSelectionModel() {
		    private static final long serialVersionUID = 1L;

		    boolean gestureStarted = false;

		    @Override
		    public void setSelectionInterval(int index0, int index1) {
		        if(!gestureStarted){
		            if (isSelectedIndex(index0)) {
		                super.removeSelectionInterval(index0, index1);
		            } else {
		                super.addSelectionInterval(index0, index1);
		            }
		        }
		        gestureStarted = true;
		    }

		    @Override
		    public void setValueIsAdjusting(boolean isAdjusting) {
		        if (isAdjusting == false) {
		            gestureStarted = false;
		        }
		    }

		});
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);

		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(150, 90));
		
		JPanel east = new JPanel();
		east.setLayout(new BorderLayout());
		east.add(new JLabel("Categories:"),BorderLayout.NORTH);
		east.add(listScroller,BorderLayout.CENTER);
		list.addListSelectionListener(new ListSelectionHandler());
		add(east,BorderLayout.EAST);
		
		JPanel south = new JPanel();
		south.setLayout(new FlowLayout());
		south.add(addElement);
		addElement.addActionListener(new AddElementListener());
		south.add(editElement);
		editElement.addActionListener(new EditElementListener());
		south.add(save);
		save.addActionListener(new SaveButtonListener());
		south.add(load);
		load.addActionListener(new LoadButtonListener());
		add(south,BorderLayout.SOUTH);
		
		
		//	INIT. WINDOW
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocation(10,0);
		setVisible(true);
		//	END OF INIT. WINDOW
	}
	public void openNewElement() {
		new AddElement(this);
	}
	public void editElement() {
		int choice;
		String choosen = JOptionPane.showInputDialog(this,"Enter the id:", "Edit quotation", JOptionPane.PLAIN_MESSAGE);
		try {
			choice = Integer.parseInt(choosen);
			if (choice<0 || choice >= elements.size())
				return;
		}catch (NumberFormatException nfe) {
			return;
		}
		new AddElement(this,elements.get(choice));
		updateList();
		updateTextArea();
	}
	public void addElement(Element element) {
		if(element.getId()<elements.size())
			elements.set(element.getId(),element);
		else
			elements.add(element.getId(),element);
		updateList();
		updateTextArea();
	}
	private void updateList(){
        listModel.removeAllElements();
	    for(Element e : elements)
	    	if(e.getCategories() != null)
	    		for(String s : e.getCategories())
		    		if(!listModel.contains(s)){
		    			listModel.addElement(s);
		    		}
	    
	    
	}
	private void updateTextArea(){
		List<String> selected = new ArrayList<String>(); 
		selected = list.getSelectedValuesList();
		area.setText("");
		for(Element e : elements){
			Set<String> elementCategories = e.getCategories();
			int i = 0;
	    	for(String s : selected)
	    		if(elementCategories != null && elementCategories.contains(s)){
	    			 i++;
	    		}
	    	if(i==selected.size()){
	    		Color redColor = new Color(255,220,220); Color blueColor = new Color(220,220,255);
	    		Color greenColor = new Color(220,255,220); Color yellowColor = new Color(255,255,220);
	    		Color cyanColor = new Color(220,255,255);
	            try {
	            	StyleConstants.setForeground(style, Color.BLUE);
	            	doc.insertString(doc.getLength(), e.getId() + ":\n" ,style);
	            	StyleConstants.setFontFamily(style,"Arial");
	            	StyleConstants.setFontSize(style, 14);
	            	StyleConstants.setForeground(style, Color.BLACK);
	            	
	            	if (e.getCategories().contains("intervju 1") || e.getCategories().contains("interview 1")){
	            		StyleConstants.setBackground(style, redColor);
	            	}
	            	else if (e.getCategories().contains("intervju 2") || e.getCategories().contains("interview 2")){
	            		StyleConstants.setBackground(style, blueColor);
	            	}
	            	else if (e.getCategories().contains("intervju 3") || e.getCategories().contains("interview 3")){
	            		StyleConstants.setBackground(style, greenColor);
	            	}
	            	else if (e.getCategories().contains("intervju 4") || e.getCategories().contains("interview 4")){
	            		StyleConstants.setBackground(style, yellowColor);
	            	}
	            	else if (e.getCategories().contains("intervju 5") || e.getCategories().contains("interview 5")){
	            		StyleConstants.setBackground(style, cyanColor);
	            	}
	            	doc.insertString(doc.getLength(), e.getQuotation() + "\n" ,style);
	            	StyleConstants.setBackground(style, Color.WHITE);
	            	StyleConstants.setForeground(style, Color.DARK_GRAY);
	            	StyleConstants.setFontSize(style, 12);
	            	doc.insertString(doc.getLength(), e.getCategories() + "\n\n" ,style);
	            }
	            catch (BadLocationException ble){}
	            
	    	}
//	    		area.append(e.getId() + ": " + e.getQuotation() + "\n" + e.getCategories() + "\n\n");
		}
	}
	class ListSelectionHandler implements ListSelectionListener {
	    public void valueChanged(ListSelectionEvent e) {
			updateTextArea();
		}
	}
	private class AddElementListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			openNewElement();
		}
	}
	private class EditElementListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			editElement();
		}
	}
	private class SaveButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent ave){
			chooseFileWindow.setSelectedFile(new File("Name.mycategories"));
			int status = chooseFileWindow.showSaveDialog(Window.this);
			if (status != JFileChooser.APPROVE_OPTION) 
				return;
			String filename = chooseFileWindow.getSelectedFile().getAbsolutePath();
			try{
				FileOutputStream fos = new FileOutputStream(filename);
				ObjectOutputStream oos =new ObjectOutputStream(fos);
				oos.writeObject(new SaveObjectsCategorizer(elements));
				oos.close();
			}catch (IOException ioe){ 
				System.err.println("Write error:" + ioe);
			}
    	}
	}
	private class LoadButtonListener implements ActionListener{
    	public void actionPerformed(ActionEvent ave){
    		int status = chooseFileWindow.showOpenDialog(Window.this);
    		if (status != JFileChooser.APPROVE_OPTION) 
    			return;
    		String filename = chooseFileWindow.getSelectedFile().getAbsolutePath();
    		try{
    			FileInputStream fis = new FileInputStream(filename); 
    			ObjectInputStream ois = new ObjectInputStream(fis);
    			SaveObjectsCategorizer load = (SaveObjectsCategorizer)ois.readObject();
    			elements = load.getElements();
    			ois.close();
    		}catch (FileNotFoundException fnfe){ 
    			System.err.println("Hittar inte filen!");
    		}catch (ClassNotFoundException cnfe){ 
    			System.err.println("Read error:" + cnfe);
    		}catch (IOException ioe){ 
    			System.err.println("Read error:" + ioe);
    		}
    		Element.setNumberOfElements(elements.size());
    		updateList();
    		updateTextArea();
    	}
    }
	
}
