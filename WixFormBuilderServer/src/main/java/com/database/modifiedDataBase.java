package com.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
 

public class DataBase {

	private String url;
	private static int formsCounter = 0;
	
	public DataBase() {
		connect();
	}
	
	private void createTable(String query) {
		Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            // create a new table
            stmt.execute(query);                
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
        	try {
        		if (conn != null) {
        			conn.close();
        		}
        	} catch (SQLException ex) {
        		System.out.println(ex.getMessage());
        	}
        }
	}
	private ResultSet select(String query) {
    	Connection conn = null;
    	ResultSet rs = null;
    	try {
    		conn = DriverManager.getConnection(url);    	
    		Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
	       	try {
	       		if (conn != null) {
	       			conn.close();
	       		}
	       	} catch (SQLException ex) {
	       		System.out.println(ex.getMessage());
	       	}
        }   
    	return rs;
	}
	
    public void connect() {
            // path to database
            url = "jdbc:sqlite:C:\\Users\\talgever\\eclipse-workspace\\WixFormBuilderServer\\src\\main\\java\\com\\database\\forms\\mainpage.db";                   
            try {
            	Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
            	// TODO Auto-generated catch block
            	e.printStackTrace();
            } 
            String query = "CREATE TABLE IF NOT EXISTS forms (\n"
                    + "	FormId integer PRIMARY KEY,\n"
                    + "	FormName text NOT NULL,\n"
                    + "	Submmisions integer\n"
                    + ");";
            createTable(query);
            System.out.println("Connection to SQLite has been established.");
    }
    
    public JSONArray getFormsMainPage() {
    	String query = "SELECT FormId, FormName, Submmisions FROM forms";
    	JSONArray array = null;
    	ResultSet rs = select(query);
        array = new JSONArray();
             
        // loop through the result set
        try {
	        while (rs.next()) {
	            System.out.println(rs.getInt("FormId") +  "\t" + 
	                                  rs.getString("FormName") + "\t" +
	                                  rs.getInt("Submmisions"));
	            JSONObject item = new JSONObject();
	            item.put("FormId", rs.getInt("FormId"));
	            item.put("FormName", rs.getString("FormName"));
	            item.put("Submmisions", rs.getInt("Submmisions"));
	            array.put(item);
	        }   
        } catch (SQLException e) {
        	e.printStackTrace();
        }
    	return array;
    }
    
    public void insertNewForm(String formName,String newForm) {
    	
    	 JSONArray array = new JSONArray(newForm);
    	 String query = "INSERT INTO forms(FormId,FormName,Submmisions) VALUES(?,?,?)";	
    	 Connection conn = null;
         try {
        	 conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query);
             pstmt.setInt(1,++formsCounter);
             pstmt.setString(2, formName);
             pstmt.setInt(3,0);
             pstmt.executeUpdate();
             
             
             ///////////////////////////
             System.out.println("here1");
             Statement stmt = conn.createStatement();
             
             String sql = "CREATE TABLE IF NOT EXISTS "+formName+" (\n"
            		 + "	id integer PRIMARY KEY,\n"
                     + "	fieldlabel text,\n"
                     + "	inputname text,\n"
                     + "	inputtype text\n"
                     + ");";
             
             // create a new table
             createTable(sql);
             
             //insert the new form
        	 String query2 = "INSERT INTO "+formName+"(id,fieldlabel,inputname,inputtype) VALUES(?,?,?,?)";	 

            // PreparedStatement pstmt2 = conn.prepareStatement(query2);
             for(int i=0;i<array.length();i++) {
            	 JSONObject formItem = array.getJSONObject(i);
            	 System.out.println("formItem "+formItem);
            	 PreparedStatement pstmt2 = conn.prepareStatement(query2);
            	 pstmt2.setInt(1, formItem.getInt("id"));
            	 pstmt2.setString(2, formItem.getString("fieldLabel"));
            	 pstmt2.setString(3, formItem.getString("inputname"));
            	 pstmt2.setString(4, formItem.getString("inputtype"));
            	 pstmt2.executeUpdate();
             }
             //////////////////////
             
             
         } catch (SQLException e) {
             System.out.println(e.getMessage());
         }
         
         finally {
        	try {
        		if (conn != null) {
        			conn.close();
        		}
        	} catch (SQLException ex) {
        		System.out.println(ex.getMessage());
        	}
         }
    }
    
    public void submitUserForm(String formId, String userName,String newForm) {
    	JSONArray array = new JSONArray(newForm);
    	Connection conn = null;
    	try {
			conn = DriverManager.getConnection(url);
    		String query = "SELECT FormId, FormName, Submmisions "
    				+ "FROM forms WHERE FormId = ?";
        	PreparedStatement pstmt  = conn.prepareStatement(query);
            pstmt.setInt(1,Integer.parseInt(formId));
            ResultSet rs  = pstmt.executeQuery();
            rs.next();
            String formName = rs.getString("FormName");
            int numOfSubmissions = rs.getInt("Submmisions");
            System.out.println("formName: "+formName);
            
            String query2 = "CREATE TABLE IF NOT EXISTS "+formName+"_"+userName+" (\n"
           		 + "	id integer PRIMARY KEY,\n"
                    + "	fieldlabel text,\n"
                    + "	inputname text,\n"
                    + "	inputtype text\n"
                    + ");";
            
            createTable(query2);
            //insert the new form
       	 	String query3 = "INSERT INTO "+formName+"_"+userName+"(id,fieldlabel,inputname,inputtype) VALUES(?,?,?,?)";	 

            for(int i=0;i<array.length();i++) {
	           	 JSONObject formItem = array.getJSONObject(i);
	           	 System.out.println("formItem "+formItem);
	           	 PreparedStatement pstmt2 = conn.prepareStatement(query3);
	           	 pstmt2.setInt(1, formItem.getInt("id"));
	           	 pstmt2.setString(2, formItem.getString("fieldlabel"));
	           	 pstmt2.setString(3, formItem.getString("inputname"));
	           	 pstmt2.setString(4, formItem.getString("inputtype"));
	           	 pstmt2.executeUpdate();
            }
            
            //now to update
            String query4 = "UPDATE forms SET Submmisions = ? "
                    + "WHERE FormId = ?";

            PreparedStatement pstmt3 = conn.prepareStatement(query4);
            	 
            // set the corresponding param
            pstmt3.setInt(1, ++numOfSubmissions);
            pstmt3.setInt(2, Integer.parseInt(formId));
            pstmt3.executeUpdate();
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}	
    	finally {
    		try {
    			if (conn != null) {
    				conn.close();
    			}
    		} catch (SQLException ex) {
    			System.out.println(ex.getMessage());
    		}
    	}
    }
    
    public JSONArray searchIfFormExist(String formId) {
    	JSONArray array = null;
    	Connection conn = null;
    	try {
			conn = DriverManager.getConnection(url);
    		String query = "SELECT FormId, FormName "
    				+ "FROM forms WHERE FormId = ?";
        	PreparedStatement pstmt  = conn.prepareStatement(query);
            
            pstmt.setInt(1,Integer.parseInt(formId));
            //
            ResultSet rs  = pstmt.executeQuery();
            if (rs.next() == true) {
            	String formName = rs.getString("FormName");           
            	String query2 = "SELECT * FROM "+formName;
            	ResultSet rs2  = select(query2);
            	array = new JSONArray();
            	while(rs2.next()) {            		
                    JSONObject item = new JSONObject();
                    item.put("id", rs2.getInt("id"));
                    item.put("fieldlabel", rs2.getString("fieldLabel"));
                    item.put("inputname", rs2.getString("inputname"));
                    item.put("inputtype", rs2.getString("inputtype"));
                    array.put(item);            		
            	}           	
            }  		   		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
        finally {
	       	try {
	       		if (conn != null) {
	       			conn.close();
	       		}
	       	} catch (SQLException ex) {
	       		System.out.println(ex.getMessage());
	       	}
        }
    	return array;   	
    }    
}