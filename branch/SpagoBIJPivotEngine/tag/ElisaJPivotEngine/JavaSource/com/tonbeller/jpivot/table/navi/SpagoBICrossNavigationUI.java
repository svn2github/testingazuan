/*
 * ====================================================================
 * This software is subject to the terms of the Common Public License
 * Agreement, available at the following URL:
 *   http://www.opensource.org/licenses/cpl.html .
 * Copyright (C) 2003-2004 TONBELLER AG.
 * All Rights Reserved.
 * You must accept the terms of that agreement to use this software.
 * ====================================================================
 *
 *
 */
package com.tonbeller.jpivot.table.navi;

import it.eng.spagobi.jpivotaddins.crossnavigation.SpagoBICrossNavigationConfig;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Element;

import com.tonbeller.jpivot.core.ModelChangeEvent;
import com.tonbeller.jpivot.core.ModelChangeListener;
import com.tonbeller.jpivot.mondrian.SpagoBICrossNavigation;
import com.tonbeller.jpivot.olap.model.Cell;
import com.tonbeller.jpivot.olap.model.OlapModel;
import com.tonbeller.jpivot.table.CellBuilder;
import com.tonbeller.jpivot.table.CellBuilderDecorator;
import com.tonbeller.jpivot.table.TableComponent;
import com.tonbeller.jpivot.table.TableComponentExtensionSupport;
import com.tonbeller.wcf.component.RendererParameters;
import com.tonbeller.wcf.controller.Dispatcher;
import com.tonbeller.wcf.controller.DispatcherSupport;
import com.tonbeller.wcf.controller.RequestContext;
import com.tonbeller.wcf.controller.RequestListener;
import com.tonbeller.wcf.table.EditableTableComponent;
import com.tonbeller.wcf.table.EmptyTableModel;
import com.tonbeller.wcf.table.ITableComponent;
import com.tonbeller.wcf.table.TableColumn;
import com.tonbeller.wcf.table.TableModel;
import com.tonbeller.wcf.table.TableModelDecorator;
import com.tonbeller.wcf.utils.DomUtils;


public class SpagoBICrossNavigationUI extends TableComponentExtensionSupport implements ModelChangeListener {

  boolean available;
  boolean renderActions;
  Dispatcher dispatcher = new DispatcherSupport();
  SpagoBICrossNavigation extension;

  TableModelDecorator tableModel = new TableModelDecorator(EmptyTableModel.instance());

  public static final String ID = "crossNavigation";
  public String getId() {
    return ID;
  }

  public void initialize(RequestContext context, TableComponent table) throws Exception {
    super.initialize(context, table);
    table.getOlapModel().addModelChangeListener(this);

    // does the underlying data model support drill?
    if (!initializeExtension()) {
      available = false;
      return;
    }
    available = true;

    // extend the controller
    table.getDispatcher().addRequestListener(null, null, dispatcher);

    // add some decorators via table.get/setRenderer
    CellBuilder cb = table.getCellBuilder();
    DomDecorator cr = new DomDecorator(table.getCellBuilder());
    table.setCellBuilder(cr);

  }

  public void startBuild(RequestContext context) {
    super.startBuild(context);
    renderActions = RendererParameters.isRenderActions(context);
    if (renderActions)
      dispatcher.clear();
  }

  class DomDecorator extends CellBuilderDecorator {

    DomDecorator(CellBuilder delegate) {
      super(delegate);
    }

    public Element build(Cell cell, boolean even) {
      Element parent = super.build(cell, even);

      if (!enabled || !renderActions || extension == null)
        return parent;

      String id = DomUtils.randomId();
      // add a drill through child node to cell element
      Element elem = table.insert("cross-navigation", parent);
      elem.setAttribute("id", id);
      elem.setAttribute("title", "Cross navigation");
      dispatcher.addRequestListener(id, null, new CrossNavigationHandler(cell));

      return parent;
    }
  }

  class CrossNavigationHandler implements RequestListener {
    Cell cell;
    CrossNavigationHandler(Cell cell) {
      this.cell = cell;
    }
    public void request(RequestContext context) throws Exception {
        HttpSession session = context.getSession();
        SpagoBICrossNavigationConfig cninfo = (SpagoBICrossNavigationConfig) session.getAttribute("cross_navigation_config");
        final String drillTableRef = table.getOlapModel().getID() + ".crossnavigationtable";
        ITableComponent tc =
          (ITableComponent) session.getAttribute(drillTableRef);
        // get a new drill through table model
        TableModel tm = crossNavigation(cninfo);
        tc.setModel(tm);
        tc.setVisible(true);
        TableColumn[] tableColumns = null;
        if (tc instanceof EditableTableComponent) {
          tableColumns =
              ((EditableTableComponent) tc).getTableComp().getTableColumns();
        } else if (tc instanceof com.tonbeller.wcf.table.TableComponent) {
          tableColumns = ((com.tonbeller.wcf.table.TableComponent) tc).getTableColumns();
        }
        if (tableColumns != null) {
          for (int i = 0; i < tableColumns.length; i++) {
            TableColumn tableColumn = tableColumns[i];
            tableColumn.setHidden(false);
          }
        }
    }
	private TableModel crossNavigation(SpagoBICrossNavigationConfig cninfo) {
		return extension.crossNavigation((Cell) cell.getRootDecoree(), cninfo);
	}
  }

  /** @return true if extension is available */
  protected boolean initializeExtension() {
    OlapModel om = table.getOlapModel();
    extension = (SpagoBICrossNavigation) om.getExtension(SpagoBICrossNavigation.ID);
    return extension != null;
  }

  public boolean isAvailable() {
    return available;
  }

  public void modelChanged(ModelChangeEvent e) {
  }

  public void structureChanged(ModelChangeEvent e) {
    initializeExtension();
    dispatcher.clear();
  }

  public TableModel getTableModel() {
    return tableModel;
  }
}
