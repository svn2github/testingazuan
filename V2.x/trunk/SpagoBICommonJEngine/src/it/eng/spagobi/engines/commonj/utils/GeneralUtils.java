package it.eng.spagobi.engines.commonj.utils;

import commonj.work.WorkEvent;

public class GeneralUtils {

	static final String WORK_COMPLETED="Work completed";
	static final String WORK_STARTED="Work started";
	static final String WORK_ACCEPTED="Work accepted";
	static final String WORK_REJECTED="Work rejected";
	static final String WORK_NOT_STARTED="Work not started";


	static public String getEventMessage(int status){
		if(status==WorkEvent.WORK_COMPLETED){
			return WORK_COMPLETED;
		}
		else if(status==WorkEvent.WORK_STARTED){
			return WORK_STARTED;
		}
		else if(status==WorkEvent.WORK_ACCEPTED){
			return WORK_ACCEPTED;			
		}
		else if(status==WorkEvent.WORK_REJECTED){
			return WORK_REJECTED;			
		}
		else if(status==0){
			return WORK_NOT_STARTED;
		}
		return "";

	}

}
