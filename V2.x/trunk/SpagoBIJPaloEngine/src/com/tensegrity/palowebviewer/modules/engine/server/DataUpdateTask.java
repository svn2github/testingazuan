/**
 * 
 */
package com.tensegrity.palowebviewer.modules.engine.server;

import java.util.Locale;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IXPoint;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;

class DataUpdateTask extends DbTask {
	 private XPath cube;
	 private IXPoint point;
	 private IResultElement value;
	 private Locale locale;

	 public void task()  throws InvalidObjectPathException{
		 getAccessor().update(getCube(), getPoint(), getValue(), getConnection(), getLocale());
	 }
	 public XPath getCube() {
		 return cube;
	 }
	 public void setCube(XPath cube) {
		 this.cube = cube;
	 }
	 public IXPoint getPoint() {
		 return point;
	 }
	 public void setPoint(IXPoint point) {
		 this.point = point;
	 }
	 public IResultElement getValue() {
		 return value;
	 }
	 public void setValue(IResultElement value) {
		 this.value = value;
	 }
	 protected String getServer() {
		 return getCube().getServer().getName();
	 }
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
 }