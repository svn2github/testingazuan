package it.eng.spagobi.twitter.analysis.servlet;

import it.eng.spagobi.twitter.analysis.dataprocessors.TwitterSearchDataProcessor;
import it.eng.spagobi.twitter.analysis.pojos.TwitterSearchPojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class ActionServlet
 */
public class InitializeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InitializeServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		List<TwitterSearchPojo> search = new ArrayList<TwitterSearchPojo>();

		search = new TwitterSearchDataProcessor()
				.getTwitterSearchList("StreamingAPI");
		Gson gson = new Gson();
		JsonElement element = gson.toJsonTree(search,
				new TypeToken<List<TwitterSearchPojo>>() {
				}.getType());

		JsonArray jsonArray = element.getAsJsonArray();

		System.out.println(jsonArray);
		response.setContentType("application/json");
		response.getWriter().print(jsonArray);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
