import java.io.Serializable;
import java.util.*;

public class SaveObjects implements Serializable {
	private static final long serialVersionUID = 3541355699676789921L;
	Set<Element> elements = new HashSet<Element>();
	public SaveObjects(Set<Element> elements){
		this.elements = elements;
	}
	public Set<Element> getElements(){
		return this.elements;
	}
}
