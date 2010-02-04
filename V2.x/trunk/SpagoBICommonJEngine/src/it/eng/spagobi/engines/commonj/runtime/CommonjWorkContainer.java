package it.eng.spagobi.engines.commonj.runtime;

import it.eng.spagobi.utilities.threadmanager.WorkManager;

import javax.servlet.http.HttpSession;

import commonj.work.Work;
import commonj.work.WorkListener;
import de.myfoo.commonj.work.FooRemoteWorkItem;

public class CommonjWorkContainer {

	Work work=null;
	
	CommonjWorkListener listener;
	
	String name=null;

	WorkManager wm=null;
	
	FooRemoteWorkItem fooRemoteWorkItem=null;
		
	
	public FooRemoteWorkItem getFooRemoteWorkItem() {
		return fooRemoteWorkItem;
	}

	public void setFooRemoteWorkItem(FooRemoteWorkItem fooRemoteWorkItem) {
		this.fooRemoteWorkItem = fooRemoteWorkItem;
	}

	public WorkManager getWm() {
		return wm;
	}

	public void setWm(WorkManager wm) {
		this.wm = wm;
	}

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}

	public CommonjWorkListener getListener() {
		return listener;
	}

	public void setListener(CommonjWorkListener listener) {
		this.listener = listener;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public void setInSession(HttpSession session){
		session.setAttribute("SBI_PROCESS_"+name, this);
	}
	
	
}
