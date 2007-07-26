/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.qbe.utility;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;

/**
 * This class is taken by Hibernate wiki.
 * 
 * This class provides pagination for displaying results from a large result set
 * over a number of pages (i.e. with a given number of results per page).
 * 
 * Taken from http://blog.hibernate.org/cgi-bin/blosxom.cgi/2004/08/14#fn.html.
 * 
 * @author Gavin King
 * @author Eric Broyles
 */
public class HibernatePage {

	private List results;

	private int pageSize;

	private int page;

	private ScrollableResults scrollableResults;

	private int totalResults = 0;

	/**
	 * Construct a new Page. Page numbers are zero-based, so the first page is
	 * page 0.
	 * 
	 * @param query
	 *            the Hibernate Query
	 * @param page
	 *            the page number (zero-based)
	 * @param pageSize
	 *            the number of results to display on the page
	 */

	public HibernatePage(Query query, int page, int pageSize) throws HibernateException {
		this.page = page;
		this.pageSize = pageSize;
		try {
			scrollableResults = query.scroll();
			/*
			 * We set the max results to one more than the specfied pageSize to
			 * determine if any more results exist (i.e. if there is a next page
			 * to display). The result set is trimmed down to just the pageSize
			 * before being displayed later (in getList()).
			 */
			results = query.setFirstResult(page * pageSize).setMaxResults(
					pageSize + 1).list();
		} catch (HibernateException e) {
            e.printStackTrace();
            Logger.error(HibernatePage.class,
					"Failed to get paginated results: " + e.getMessage());
            throw e;
		}

	}

	public boolean isFirstPage() {
		return page == 0;
	}

	public boolean isLastPage() {
		return page >= getLastPageNumber();
	}

	public boolean hasNextPage() {
		return results.size() > pageSize;
	}

	public boolean hasPreviousPage() {
		return page > 0;
	}

	public int getLastPageNumber() {
		/*
		 * We use the Math.floor() method because page numbers are zero-based
		 * (i.e. the first page is page 0).
		 */
		double totalResults = new Integer(getTotalResults()).doubleValue();
		return new Double(Math.floor(totalResults / pageSize)).intValue();
	}

	public List getList() {
		/*
		 * Since we retrieved one more than the specified pageSize when the
		 * class was constructed, we now trim it down to the pageSize if a next
		 * page exists.
		 */
		return hasNextPage() ? results.subList(0, pageSize) : results;
	}

	
	public int getTotalResults() {
		try {
			getScrollableResults().last();
			totalResults = getScrollableResults().getRowNumber();
		} catch (HibernateException e) {
			Logger.error(HibernatePage.class, 
						 "Failed to get last row number from scollable results: "
						  + e.getMessage());
		}
		return totalResults;
	}

	public int getFirstResultNumber() {
		return page * pageSize + 1;
	}

	public int getLastResultNumber() {
		int fullPage = getFirstResultNumber() + pageSize - 1;
		return getTotalResults() < fullPage ? getTotalResults() : fullPage;
	}

	public int getNextPageNumber() {
		return page + 1;
	}

	public int getPreviousPageNumber() {
		return page - 1;
	}

	protected ScrollableResults getScrollableResults() {
		return scrollableResults;
	}

}

