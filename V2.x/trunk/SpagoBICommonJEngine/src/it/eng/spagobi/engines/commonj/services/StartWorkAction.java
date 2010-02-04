package it.eng.spagobi.engines.commonj.services;

import javax.servlet.http.HttpSession;

import commonj.work.Work;

import de.myfoo.commonj.work.FooRemoteWorkItem;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.commonj.runtime.CommonjWorkContainer;
import it.eng.spagobi.engines.commonj.runtime.CommonjWorkListener;
import it.eng.spagobi.utilities.engines.AbstractEngineAction;
import it.eng.spagobi.utilities.threadmanager.WorkManager;


public class StartWorkAction extends AbstractEngineAction {



	@Override
	public void init(SourceBean config) {
		// TODO Auto-generated method stub
		super.init(config);
	}

	@Override
	public void service(SourceBean request, SourceBean response) {
		super.service(request, response);

		HttpSession session=getHttpSession();

		//recover from session
		Object o=session.getAttribute("SBI_PROCESS_"+"lavoro");
		try{
			if(o!=null){

				CommonjWorkContainer container=(CommonjWorkContainer)o;
				WorkManager wm=container.getWm();

				Work work=container.getWork();
				CommonjWorkListener listener=container.getListener();

				FooRemoteWorkItem fooRemoteWorkItem=(FooRemoteWorkItem)wm.runWithReturn(work, listener);
				container.setFooRemoteWorkItem(fooRemoteWorkItem);

				session.setAttribute("SBI_PROCESS_"+"lavoro", container);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}




}
