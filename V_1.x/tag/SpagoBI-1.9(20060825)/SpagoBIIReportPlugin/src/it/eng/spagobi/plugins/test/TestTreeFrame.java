package it.eng.spagobi.plugins.test;

import it.eng.spagobi.plugins.ireport.MyNode;
import it.eng.spagobi.plugins.ireport.PluginResourcesManager;
import it.eng.spagobi.plugins.ireport.gui.TreeFrame;

import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import junit.framework.TestCase;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

public class TestTreeFrame extends TestCase {

	private PluginResourcesManager resourcesManager = null;
	private HashMap nodeMap = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		nodeMap = new HashMap();
		TreeFrame treeFrame = TreeFrame.getMainInstance();
		// create the jtree
		JTree tree = treeFrame.getJTree();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		resourcesManager = PluginResourcesManager.getMainInstance();
		resourcesManager.setOperationMode("test");
		
	}
	
	public void testTreeFrame_1(){
		// setUp
		TreeFrame.getMainInstance().populateTreeFromXML(createXMLDocument(1));
		findNode(TreeFrame.getMainInstance().getRootNode(),"/");
		
		//test 
		assertTrue(myTest("/SpagoBI Radice/SpagoBI/F1/","folder","F1",""));
		assertTrue(myTest("/SpagoBI Radice/SpagoBI/F1/F2/","folder","F2",""));
		assertTrue(myTest("/SpagoBI Radice/SpagoBI/F1/F2/F3/","folder","F3",""));
		assertTrue(myTest("/SpagoBI Radice/SpagoBI/F1/F4/","folder","F4",""));
		
		assertTrue(myTest("/SpagoBI Radice/SpagoBI/O1/","object","O1","O1-path"));
		assertTrue(myTest("/SpagoBI Radice/SpagoBI/F1/O2/","object","O2","O2-path"));
		assertTrue(myTest("/SpagoBI Radice/SpagoBI/F1/F2/O3/","object","O3","O3-path"));
		assertTrue(myTest("/SpagoBI Radice/SpagoBI/F1/F2/F3/O4/","object","O4","O4-path"));
		assertTrue(myTest("/SpagoBI Radice/SpagoBI/F1/F4/O5/","object","O5","O5-path"));
       
	}
	
	/**
	 * This method checks if the node identified by the String node contains the correct information, about
	 * tag (folder, object), name and path (if object) 
	 * @return true if all the information are correct, false otherwise
	 */
	private boolean myTest(String node, String tag, String name, String path){
		
		MyNode mn = (MyNode)((DefaultMutableTreeNode)nodeMap.get(node)).getUserObject();
		
		try {
			if ( (mn.getTag().equals(tag))&&(mn.getName().equals(name))){
				if (tag.equals("folder")) {
					
					return true;	
					
				} else if (tag.equals("object")) {
					
					if (mn.getAttributes().getProperty("path").equals(path)){
						return true;
					} else return false;
									
				} else return false;
			} else return false;
		} catch (Exception e){
			return false;
		}
	
	}
	
	private void findNode(DefaultMutableTreeNode node, String path){
		
		path+=node.toString()+"/";
		System.out.println(path);
		nodeMap.put(path,node);
		
		
		for (Enumeration e=node.children(); e.hasMoreElements(); ) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)e.nextElement();
            findNode(child,path);
         }
	}
	

	
	
	private Document createXMLDocument(int select){
		
		String doc = "";
		switch (select){
		//correct
		case 1: doc= 	"<response>" +
							"<body>" +
								"<object name='O1' path='O1-path'></object>"+
								"<folder name='F1'>" +
									"<object name='O2' path='O2-path'></object>"+
									"<folder name='F2'>" +
										"<object name='O3' path='O3-path'></object>"+
										"<folder name='F3'>" +
											"<object name='O4' path='O4-path'></object>"+
										"</folder>" +
									"</folder>" +
									"<folder name='F4'>" +
										"<object name='O5' path='O5-path'></object>"+
									"</folder>" +
								"</folder>" +
							"</body>" +
						"</response>";
						break;
	
		}; 
		
	Document document = null;
	try {
		
		document = DocumentHelper.parseText(doc);
		
	} catch (DocumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 return document;
			
	}
	
	
	

}
