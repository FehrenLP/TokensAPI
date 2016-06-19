package eu.burrow.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {
	
	public static Connection con;
	String host;
	int port;
	String user;
	String password;
	String database;
	
	public MySQL(String host, int port, String user, String pw, String db){
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = pw;
		this.database = db;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.user, this.password);
			System.out.println("[TokensAPI] MySQL Connected!");
		} catch (SQLException e) {
			System.err.println("[TokensAPI] MySQL Connection Failed!");
			con = null;
		}
		
	}
	
	public static Connection getConnection(){
		return con;
	}
	
	public static void close() {
		
		if(con != null){
			try {
				con.close();
				System.out.println("[TokensAPI] MySQL Closed!");
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("[TokensAPI] MySQL Close Failed!");
			}
		}
		
	}
	
	public static void closeRessources(ResultSet rs, PreparedStatement st){
		if(rs != null){
			try{
				rs.close();
			} catch(SQLException e){
				e.printStackTrace();
				System.err.println("[TokensAPI] close ResultSet in closeRessources Failed!");
			}
		}
		
		if(st != null){
			try{
				st.close();
			} catch (SQLException e){
				e.printStackTrace();
				System.err.println("[TokensAPI] close PreparedStatement in closeRessources Failed!");
			}
		}
	}
	
	public static void Update(String qry){
		
		try{
			Statement stmt = con.createStatement();
			stmt.executeUpdate(qry);
		} catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	
	public static ResultSet Query(String qry){
		ResultSet rs = null;
		
		try {
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(qry);
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("[TokensAPI] Send ResultSet Query Failed!");
		}
		
		return rs;
	}
	
}
