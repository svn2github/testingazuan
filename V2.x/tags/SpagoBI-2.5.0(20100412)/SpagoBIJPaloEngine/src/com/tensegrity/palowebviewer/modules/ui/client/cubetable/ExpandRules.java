package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import java.util.ArrayList;
import java.util.List;

import com.tensegrity.palowebviewer.modules.ui.client.dimensions.IDimensionModel;
import com.tensegrity.palowebviewer.modules.util.client.taskchain.AbstractChainTask;
import com.tensegrity.palowebviewer.modules.util.client.taskchain.IChainTask;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.ITask;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.TaskQueue;

public class ExpandRules {
	
	private final ICubeTableModel model;
	private int level = 0;
	private final List selectionDimList = new ArrayList();
	private HeaderLevelExpander expander = null;
	private final IDimensionListListener axisDimListListener = new IDimensionListListener() {

		

		public void dimensionAdded(IDimensionList list, IDimensionModel model) {
			if(selectionDimList.contains(model)){
				selectionDimList.remove(model);
				CubeHeaderModel header = getHeader(list);
				expander = new HeaderLevelExpander(header, level, null);
				expander.setExpandOnly(model.getDimension());
			}
		}

		public void dimensionRemoved(IDimensionList list, IDimensionModel model) {}

		public void dimensionMoved(IDimensionList list, IDimensionModel model) {
		
		}
		
	};
	

	public ExpandRules(ICubeTableModel model) {
		this.model = model;
		initDimensionList();
		subscribe();
	}
	
	public void setLevel(int value) {
		level = value;
	}
	
	public IChainTask getChainTask() {
		return new ExpandRulesChainTask();
	}

	protected CubeHeaderModel getHeader(IDimensionList list) {
		CubeHeaderModel result = null;
		if(model.getXDimensions()==list){
			result = model.getXHeaderModel();
		}
		else if(model.getYDimensions()==list){
			result = model.getYHeaderModel();
		}
		return result;
	}

	private void initDimensionList() {
		IDimensionList sliceDimensions = model.getSliceDimensions();
		int size = sliceDimensions.getDimCount();
		for(int i = 0; i< size; i++) {
			IDimensionModel dimModel = sliceDimensions.getDimModel(i);
			selectionDimList.add(dimModel);
		}
	}

	private void subscribe() {
		model.getXDimensions().addListener(axisDimListListener);
		model.getYDimensions().addListener(axisDimListListener);
	}
	
	private class ExpandRulesChainTask extends AbstractChainTask {
		
		private final IExpanderCallback callback = new IExpanderCallback() {

			public void expanded() {
				executeNextTask();
			}
			
		};
		
		private final class ExpandTask implements ITask {

			public void execute() {
				if(expander != null) {
					expander.setCallback(callback);
					expander.expand();
					expander = null;
				}
				else {
					executeNextTask();
				}
			}

			public String getName() {
				return "ExpandTask";
			}
			
		}


		public void execute() {
			TaskQueue.getInstance().add(new ExpandTask());
		}
		
		public String getDescription() {
			return "ExpandRulesChainTask";
		}
		
	};
	
}

