package com.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.ResultSet;
 

public class DataBase {

	private String projectLocation = "C:\\Users\\talaviad\\eclipse-workspace\\";
	private String url;
	private static int formsCounter = 0;
	
	public DataBase() {
		connect();
	}
	
    public void connect() {
    	    //db parameters
            url = "jdbc:sqlite:"+projectLocation+"WixFormBuilderServer\\src\\main\\java\\com\\database\\forms\\mainpage.db";    
            try {
            	Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
            	e.printStackTrace();
            } 
            Connection conn=null;
            try {
                conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement();
                System.out.println("Connection to SQLite has been established.");
                String sql = "CREATE TABLE IF NOT EXISTS forms (\n"
                        + "	FormId integer PRIMARY KEY,\n"
                        + "	FormName text NOT NULL,\n"
                        + "	Submmisions integer\n"
                        + ");";                
                // create a new table               
                stmt.execute(sql);   
                String query = "SELECT MAX(FormId) FROM forms";
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                formsCounter = Integer.parseInt(rs.getString(1));
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
    
    public String getFormName(String formId) {
    	String formName = "";
    	Connection conn=null;
    	try {
			conn = DriverManager.getConnection(url);
    		String query = "SELECT FormName "
    				+ "FROM forms WHERE FormId = ?";
        	PreparedStatement pstmt  = conn.prepareStatement(query);
            pstmt.setInt(1,Integer.parseInt(formId));
            ResultSet rs  = pstmt.executeQuery();
            rs.next();
            formName = rs.getString("FormName");  
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
    	return formName;
    }
    
    public JSONArray getSubmissionsForm(String formId) {
    	JSONArray array = null;
    	Connection conn=null;
    	try {
			conn = DriverManager.getConnection(url);
    		String query = "SELECT FormId, FormName "
    				+ "FROM forms WHERE FormId = ?";
        	PreparedStatement pstmt  = conn.prepareStatement(query);
            pstmt.setInt(1,Integer.parseInt(formId));
            ResultSet rs  = pstmt.executeQuery();
            if (rs.next() == true) {
            	array = new JSONArray();
            	String formName = rs.getString("FormName");  
            	query = "SELECT * FROM sqlite_master";
        		Statement stmt = conn.createStatement();
                rs = stmt.executeQuery(query);
                ArrayList<String> fetchedTables = new ArrayList<>();
                while (rs.next()) {
                	if (rs.getString(3).contains(formName+"_"))
                		fetchedTables.add(rs.getString(3));
                }
                for (int i=0; i<fetchedTables.size(); i++) {
                	String currentTable = fetchedTables.get(i);
                	JSONObject submmiter = new JSONObject();
                	String currentUsername = currentTable.replace(formName+"_","");
                	submmiter.put("username", currentUsername);
                	query = "SELECT inputname,fieldlabel FROM "+currentTable;
                	stmt = conn.createStatement();
                	rs = stmt.executeQuery(query);
                	int counter = 1;
                	while (rs.next()) {
                		submmiter.put("inputname"+counter, rs.getString("inputname"));
                		submmiter.put("fieldlabel"+counter, rs.getString("fieldlabel"));
                		counter++;
                	}
                	array.put(submmiter);
                }
            }
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
    	return array;  	
    }
    
    public JSONArray getFormsMainPage() {
    	String query = "SELECT FormId, FormName, Submmisions FROM forms";
    	JSONArray array = null;
    	Connection conn=null;
    	try {
    		conn = DriverManager.getConnection(url);    	
    		Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            array = new JSONArray();
            while (rs.next()) {
                JSONObject item = new JSONObject();
                item.put("FormId", rs.getInt("FormId"));
                item.put("FormName", rs.getString("FormName"));
                item.put("Submmisions", rs.getInt("Submmisions"));
                array.put(item);
            }
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
    	return array;
    }
    
    public void insertNewForm(String formName,String newForm) {
    	 JSONArray array = new JSONArray(newForm);
    	 String query = "INSERT INTO forms(FormId,FormName,Submmisions) VALUES(?,?,?)";	 
    	 Connection conn=null;
         try {
        	 conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query);
             pstmt.setInt(1,++formsCounter);
             pstmt.setString(2, formName);
             pstmt.setInt(3,0);
             pstmt.executeUpdate();
             Statement stmt = conn.createStatement();
             String sql = "CREATE TABLE IF NOT EXISTS "+formName+" (\n"
            		 + "	id integer PRIMARY KEY,\n"
                     + "	fieldlabel text,\n"
                     + "	inputname text,\n"
                     + "	inputtype text\n"
                     + ");";
             stmt.execute(sql); 
             //insert the new form
        	 String query2 = "INSERT INTO "+formName+"(id,fieldlabel,inputname,inputtype) VALUES(?,?,?,?)";	 
             for(int i=0;i<array.length();i++) {
            	 JSONObject formItem = array.getJSONObject(i);
            	 PreparedStatement pstmt2 = conn.prepareStatement(query2);
            	 pstmt2.setInt(1, formItem.getInt("id"));
            	 pstmt2.setString(2, formItem.getString("fieldLabel"));
            	 pstmt2.setString(3, formItem.getString("inputname"));
            	 pstmt2.setString(4, formItem.getString("inputtype"));
            	 pstmt2.executeUpdate();
             }
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
    	Connection conn=null;
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
            String query2 = "CREATE TABLE IF NOT EXISTS "+formName+"_"+userName+" (\n"
           		 + "	id integer PRIMARY KEY,\n"
                    + "	fieldlabel text,\n"
                    + "	inputname text,\n"
                    + "	inputtype text\n"
                    + ");";            
            // create a new table
            Statement stmt = conn.createStatement();
            stmt.execute(query2);             
            //insert the new form
       	 	String query3 = "INSERT INTO "+formName+"_"+userName+"(id,fieldlabel,inputname,inputtype) VALUES(?,?,?,?)";	 
            for(int i=0;i<array.length();i++) {
	           	 JSONObject formItem = array.getJSONObject(i);
	           	 PreparedStatement pstmt2 = conn.prepareStatement(query3);
	           	 pstmt2.setInt(1, formItem.getInt("id"));
	           	 pstmt2.setString(2, formItem.getString("fieldlabel"));
	           	 pstmt2.setString(3, formItem.getString("inputname"));
	           	 pstmt2.setString(4, formItem.getString("inputtype"));
	           	 pstmt2.executeUpdate();
            }          
            //update
            String query4 = "UPDATE forms SET Submmisions = ? "
                    + "WHERE FormId = ?";
            PreparedStatement pstmt3 = conn.prepareStatement(query4);
            pstmt3.setInt(1, ++numOfSubmissions);
            pstmt3.setInt(2, Integer.parseInt(formId));
            pstmt3.executeUpdate();
    	} catch (SQLException e) {
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
    	Connection conn=null;
    	try {
			conn = DriverManager.getConnection(url);
    		String query = "SELECT FormId, FormName "
    				+ "FROM forms WHERE FormId = ?";
        	PreparedStatement pstmt  = conn.prepareStatement(query);
            pstmt.setInt(1,Integer.parseInt(formId));
            ResultSet rs  = pstmt.executeQuery();
            if (rs.next() == true) {
            	String formName = rs.getString("FormName");           
            	String query2 = "SELECT * FROM "+formName;
            	PreparedStatement pstmt2  = conn.prepareStatement(query2);
            	ResultSet rs2  = pstmt2.executeQuery();
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