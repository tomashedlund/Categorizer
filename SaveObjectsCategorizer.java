import java.io.Serializable;
import java.util.*;

public class SaveObjectsCategorizer implements Serializable {
	private static final long serialVersionUID = 3541355699676789921L;
	List<Element> elements = new ArrayList<Element>();
	public SaveObjectsCategorizer(List<Element> elements){
		this.elements = elements;
	}
	public List<Element> getElements(){
		return this.elements;
	}
}
