/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.utility;

// TODO: Auto-generated Javadoc
/**
 * The Class PageNavigatorUtils.
 * 
 * @author Andrea Gioia
 */
public class PageNavigatorUtils {
	
	/**
	 * Gets the page window.
	 * 
	 * @param currentPage the current page
	 * @param lastPage the last page
	 * @param windowSize the window size
	 * 
	 * @return the page window
	 */
	public static int[] getPageWindow(int currentPage, int lastPage, int windowSize) {
		int[] window;
		
		
		window = new int[Math.min(windowSize, lastPage)];
		int pageOnTheLeft = windowSize/2;
		int pageOnTheRight = windowSize - (pageOnTheLeft + 1);
		if((pageOnTheRight + currentPage) > lastPage) {
			int i = (pageOnTheRight + currentPage) - lastPage;
			pageOnTheRight -= i;
			pageOnTheLeft += i;
		}
		int firstPageInWindow = currentPage-pageOnTheLeft;
		if(firstPageInWindow < 1) firstPageInWindow = 1;
		for(int i = 0; i < window.length; i++) {
			window[i] = firstPageInWindow + i;
		}		
		
		return window;
	}
	
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		int currentPage = 5;
		int lastPage = 5;
		int windowSize = 10;
		
		int[] window = getPageWindow(currentPage, lastPage, windowSize);
		for(int i = 0; i < window.length; i++) {
			if(window[i] == currentPage) {
				System.out.print("  [" + window[i] + "]");
			} else {
				System.out.print("  " + window[i] + "");
			}
		}	
	}
}
