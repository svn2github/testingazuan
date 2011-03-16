package it.eng.spagobi.meta.datamarttree.draganddrop;


import it.eng.qbe.model.structure.AbstractModelNode;
import org.eclipse.swt.dnd.ByteArrayTransfer;



public class DatamartFieldTransfer  extends ByteArrayTransfer {
	   private static DatamartFieldTransfer instance = new DatamartFieldTransfer();
	   private static final String TYPE_NAME = "datamartfield-transfer-format";
	   private static final int TYPEID = registerType(TYPE_NAME);

	   /**
	    * Returns the singleton transfer instance.
	    */
	   public static DatamartFieldTransfer getInstance() {
	      return instance;
	   }
	   /**
	    * Avoid explicit instantiation
	    */
	   private DatamartFieldTransfer() {
	   }
	   protected AbstractModelNode[] fromByteArray(byte[] bytes) {
	      return new AbstractModelNode[0];
	   }
	   
	   protected byte[] toByteArray(AbstractModelNode[] bytes) {
		   return new byte[0];
	   }

	   protected int[] getTypeIds() {
	      return new int[] { TYPEID };
	   }

	   protected String[] getTypeNames() {
	      return new String[] { TYPE_NAME };
	   }


	}