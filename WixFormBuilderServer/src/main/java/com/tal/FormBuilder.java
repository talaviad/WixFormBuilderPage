package com.tal;

import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import com.database.DataBase;


public class FormBuilder extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private DataBase database;
	
       
    public FormBuilder() {
        super();
        database = new DataBase();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String clientParameter = request.getParameter("forms");
		setAccessControlAllow(response);
		if (clientParameter!=null) {
			String formId;
			JSONArray databaseResponse;
			switch(clientParameter) {
				case "mainpage":
					databaseResponse = database.getFormsMainPage();
				    response.getWriter().write(databaseResponse.toString());
				    break;
				    
				case "findform": 
					formId = request.getParameter("formnumber");
					databaseResponse = database.searchIfFormExist(formId); //should return the form details
					if (databaseResponse != null) {
						response.getWriter().write(databaseResponse.toString());
					}
					else {
						response.getWriter().write("false");
					}
					break;
					
				case "getsubmmiters":
					formId = request.getParameter("formnumber");
					JSONArray array = database.getSubmissionsForm(formId);
					if (array != null) 
						response.getWriter().write(array.toString());
					else
						response.getWriter().write("false");
					break;
					
				case "getformname":
					formId = request.getParameter("formnumber");
					response.getWriter().write(database.getFormName(formId));
					break;	
					
				default: break;
			}
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String clientParameter = request.getParameter("forms");
		String requestData;
		switch (clientParameter) {
			case "newform":
				String formName = request.getParameter("formname");
				requestData = request.getReader().lines().collect(Collectors.joining());
				database.insertNewForm(formName,requestData);
				break;
			case "submitform":
				String formId = request.getParameter("formnumber");
				String userName = request.getParameter("username");
				requestData = request.getReader().lines().collect(Collectors.joining());
				database.submitUserForm(formId,userName,requestData);
				break;
			default: break;
		}
	}
	
	public void setAccessControlAllow(HttpServletResponse response) {
	    response.addHeader("Access-Control-Allow-Origin", "*");
	    response.addHeader("Access-Control-Allow-Methods", "GET,POST");
	    response.addHeader("Access-Control-Allow-Headers", "Content-Type");
	    response.addHeader("Access-Control-Max-Age", "86400");				
	}
}
