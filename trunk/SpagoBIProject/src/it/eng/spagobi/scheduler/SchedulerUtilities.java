package it.eng.spagobi.scheduler;

import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SchedulerUtilities {

	public static BIObject.BIObjectSnapshot getNamedHistorySnapshot(List allsnapshots, String namesnap, int hist) 
	       throws Exception {
		Map snapshots = new HashMap();
		List snapDates = new ArrayList();
		Iterator iterAllSnap = allsnapshots.iterator();
		while(iterAllSnap.hasNext()) {
			BIObject.BIObjectSnapshot snap =  (BIObject.BIObjectSnapshot)iterAllSnap.next();
			if(snap.getName().equals(namesnap)){
				Date creationDate = snap.getDateCreation();
				Long creationLong = new Long(creationDate.getTime());
				snapDates.add(creationLong);
				snapshots.put(creationLong, snap);
			}
		}
		// check if history is out of range
		if( (hist<0) || (snapDates.size()-1 > hist) ) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, SchedulerUtilities.class.getName(), 
					            "getNamedHistorySnapshot", "History step out of range");
			throw new Exception("History step out of range");
		}
		// get the right snapshot
		Collections.sort(snapDates);
		Collections.reverse(snapDates);
		Object key = snapDates.get(hist);
		BIObject.BIObjectSnapshot snap = (BIObject.BIObjectSnapshot)snapshots.get(key);
		return snap;
	}
	

	
	public static List getSnapshotsByName(List allsnapshots, String namesnap) throws Exception {
		List snaps = new ArrayList();
		Iterator iterAllSnap = allsnapshots.iterator();
		while(iterAllSnap.hasNext()) {
			BIObject.BIObjectSnapshot snap =  (BIObject.BIObjectSnapshot)iterAllSnap.next();
			if(snap.getName().equals(namesnap)){
				snaps.add(snap);
			}
		}
		return snaps;
	}	

}
