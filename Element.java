import java.io.Serializable;
import java.util.*;


public class Element implements Serializable {
	private static final long serialVersionUID = 6865200257546414674L;
	private Set<String> categories;
	private String quotation;
	private static int numberOfElements;
	private int id;
	
	public Element(String quotation, Set<String> categories){
		this.quotation = quotation;
		this.categories = categories;
		this.id = Element.numberOfElements;
		Element.numberOfElements++;
	}
	public void addCategory(String category){
		categories.add(category);
	}
	public void removeCategory(String category){
		categories.remove(category);
	}
	public void setQuotation(String quotation){
		this.quotation = quotation;
	}
	public Set<String> getCategories(){
		return this.categories;
	}
	public void setCategories(Set<String> categories){
		this.categories = categories;
	}
	public String getQuotation(){
		return this.quotation;
	}
	public int getId(){
		return this.id;
	}
	public static int getNumberOfElements(){
		return Element.numberOfElements;
	}
	public static void setNumberOfElements(int number){
		Element.numberOfElements = number;
	}
}
