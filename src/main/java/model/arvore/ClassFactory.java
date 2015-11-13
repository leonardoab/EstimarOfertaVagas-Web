package model.arvore;

import java.util.HashMap;

public class ClassFactory {
	
	private static ClassFactory _instance = null;
	private HashMap<String, Class> map = new HashMap<String, Class>();
	
	protected ClassFactory() {	}
	
	private static ClassFactory getInstance() { 
		if (_instance == null)	_instance = new ClassFactory(); 
		return _instance;
	}
	
	public static Class getClass(String id,boolean limpar,String weight) {
		
		HashMap<String, Class> cfmap = getInstance().map;
		
		
		Class c = cfmap.get(id);
		
		if(c!=null && limpar == true){
			
			cfmap.remove(id);
			c = null;
			
		}
		
		if (c == null) {
			c = new Class(id,Integer.parseInt(weight));
			cfmap.put(id, c);
		}
		
		return c;		
	}
	
	public static void addClass(String id, Class c) {
		getInstance().map.put(id, c);
	}
	
	public static boolean contains(String id) {
		return getInstance().map.containsKey(id);
	}
	
	public static String makeString() {
		String out = "";
		HashMap<String, Class> m = getInstance().map;
		for (String string : m.keySet()) 
			out +=  "("+string + ", " + m.get(string) + ") \n";
		
		return out;
	}
	
}
