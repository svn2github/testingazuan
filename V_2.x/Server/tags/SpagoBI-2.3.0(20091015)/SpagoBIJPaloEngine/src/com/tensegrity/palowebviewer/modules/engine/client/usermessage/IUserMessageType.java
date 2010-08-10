package com.tensegrity.palowebviewer.modules.engine.client.usermessage;

public interface IUserMessageType {

	//methods
	public String getName();
	
	public int getId();

	// type consts
	public static final int ERROR_ID = 1;
	public static final int WARN_ID = 2;
	
	
	// interface implementation consts
	public static final IUserMessageType ERROR = new IUserMessageType(){

		public int getId() {
			return ERROR_ID;
		}

		public String getName() {
			return "error";
		}
		
	};
	
	public static final IUserMessageType WARN = new IUserMessageType(){

		public int getId() {
			return WARN_ID;
		}

		public String getName() {
			return "warning";
		}
		
	};
	
	
}
