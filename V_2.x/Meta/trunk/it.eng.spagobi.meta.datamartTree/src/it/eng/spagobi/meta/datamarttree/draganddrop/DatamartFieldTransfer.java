package it.eng.spagobi.meta.datamarttree.draganddrop;


import it.eng.spagobi.meta.datamarttree.bo.DatamartField;

import java.io.*;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;


public class DatamartFieldTransfer  extends ByteArrayTransfer {
	   private static DatamartFieldTransfer instance = new DatamartFieldTransfer();
	   private static final String TYPE_NAME = "gadget-transfer-format";
	   private static final int TYPEID = registerType(TYPE_NAME);

	   /**
	    * Returns the singleton gadget transfer instance.
	    */
	   public static DatamartFieldTransfer getInstance() {
	      return instance;
	   }
	   /**
	    * Avoid explicit instantiation
	    */
	   private DatamartFieldTransfer() {
	   }
	   protected DatamartField[] fromByteArray(byte[] bytes) {
	      DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));

	      try {
	         /* read number of gadgets */
	         int n = in.readInt();
	         /* read gadgets */
	         DatamartField[] gadgets = new DatamartField[n];
	         for (int i = 0; i < n; i++) {
	        	 DatamartField gadget = readDatamartField(null, in);
	            if (gadget == null) {
	               return null;
	            }
	            gadgets[i] = gadget;
	         }
	         return gadgets;
	      } catch (IOException e) {
	         return null;
	      }
	   }
	   /*
	    * Method declared on Transfer.
	    */
	   protected int[] getTypeIds() {
	      return new int[] { TYPEID };
	   }
	   /*
	    * Method declared on Transfer.
	    */
	   protected String[] getTypeNames() {
	      return new String[] { TYPE_NAME };
	   }
	   /*
	    * Method declared on Transfer.
	    */
	   protected void javaToNative(Object object, TransferData transferData) {
	      byte[] bytes = toByteArray((DatamartField[])object);
	      if (bytes != null)
	         super.javaToNative(bytes, transferData);
	   }
	   /*
	    * Method declared on Transfer.
	    */
	   protected Object nativeToJava(TransferData transferData) {
	      byte[] bytes = (byte[])super.nativeToJava(transferData);
	      return fromByteArray(bytes);
	   }
	   /**
	    * Reads and returns a single gadget from the given stream.
	    */
	   private DatamartField readDatamartField(DatamartField parent, DataInputStream dataIn) throws IOException {
	      /**
	       * Gadget serialization format is as follows:
	       * (String) name of gadget
	       * (int) number of child gadgets
	       * (Gadget) child 1
	       * ... repeat for each child
	       */
	      String name = dataIn.readUTF();
	      int n = dataIn.readInt();
	      DatamartField newParent = new DatamartField(parent, name);
	      for (int i = 0; i < n; i++) {
	         readDatamartField(newParent, dataIn);
	      }
	      return newParent;
	   }
	   protected byte[] toByteArray(DatamartField[] gadgets) {
	      /**
	       * Transfer data is an array of gadgets.  Serialized version is:
	       * (int) number of gadgets
	       * (Gadget) gadget 1
	       * (Gadget) gadget 2
	       * ... repeat for each subsequent gadget
	       * see writeGadget for the (Gadget) format.
	       */
	      ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	      DataOutputStream out = new DataOutputStream(byteOut);

	      byte[] bytes = null;

	      try {
	         /* write number of markers */
	         out.writeInt(gadgets.length);

	         /* write markers */
	         for (int i = 0; i < gadgets.length; i++) {
	            writeDatamartField((DatamartField)gadgets[i], out);
	         }
	         out.close();
	         bytes = byteOut.toByteArray();
	      } catch (IOException e) {
	         //when in doubt send nothing
	      }
	      return bytes;
	   }
	   /**
	    * Writes the given gadget to the stream.
	    */
	   private void writeDatamartField(DatamartField gadget, DataOutputStream dataOut) throws IOException {
	      /**
	       * Gadget serialization format is as follows:
	       * (String) name of gadget
	       * (int) number of child gadgets
	       * (Gadget) child 1
	       * ... repeat for each child
	       */
	      dataOut.writeUTF(gadget.getName());
	      DatamartField[] children = gadget.getChildren();
	      dataOut.writeInt(children.length);
	      for (int i = 0; i < children.length; i++) {
	         writeDatamartField(children[i], dataOut);
	      }
	   }
	}