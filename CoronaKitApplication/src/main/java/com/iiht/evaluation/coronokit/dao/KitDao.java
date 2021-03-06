package com.iiht.evaluation.coronokit.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import com.iiht.evaluation.coronokit.model.CoronaKit;
import com.iiht.evaluation.coronokit.model.ProductMaster;



public class KitDao {

	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection jdbcConnection;

	public KitDao(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

	protected void connect() throws SQLException {
		if (jdbcConnection == null || jdbcConnection.isClosed()) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				//Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				throw new SQLException(e);
			}
			jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		}
	}

	protected void disconnect() throws SQLException {
		if (jdbcConnection != null && !jdbcConnection.isClosed()) {
			jdbcConnection.close();
		}
	}

	public Connection getJdbcConnection() {
		try {
			connect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jdbcConnection;
	}
	
	public static final String GET_ALL_PRODS_QRY = "SELECT pid,pname,pcost,pquantity FROM orderdetails order by pid asc";
	public static final String INSERT_NEWUSER_QRY = "INSERT INTO userdetails(uname,uemail,ucontact, uaddress) VALUES(?,?,?,?)";
	public static final String UPD_QTY_QRY = "UPDATE orderdetails SET pquantity = ? where pid =?";
	public static final String PID_LIST_QRY = "SELECT pid FROM orderdetails order by pid asc";
	public static final String GET_ALL_ORDERS_QRY = "SELECT pid, pname, pcost, pquantity FROM orderdetails where pquantity > 0";
	public static final String GET_USER_DETAILS_QRY = "SELECT uname, uemail, ucontact, uaddress FROM userdetails where uid IN(SELECT max(uid) from userdetails)";
	public static final String GET_TOT_PRICE_QRY = "Select sum(pcost * pquantity) FROM orderdetails";
	
	public List<ProductMaster> getOrderDetails() throws ServletException{
		List<ProductMaster> products = new ArrayList<>();
		try {
			PreparedStatement pst = getJdbcConnection().prepareStatement(GET_ALL_ORDERS_QRY);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				ProductMaster product = new ProductMaster();
				product.setId(rs.getInt(1));
				product.setProductName(rs.getString(2));
				product.setCost(rs.getDouble(3));
				product.setProductDescription(rs.getString(4));
				
				products.add(product);
			}
		} catch (SQLException e) {
			throw new ServletException("Fetching order details failed!");
		}
		return products;
	}
	public double getTotalPrice() throws ServletException{
		double totPrice = 0;
		try {
			PreparedStatement pst = getJdbcConnection().prepareStatement(GET_TOT_PRICE_QRY);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				totPrice = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new ServletException("Calculating total purchase cost failed!");
		}
		return totPrice;
	}
	public List<String> getUserDetails() throws ServletException{
		List<String> userdetails = new ArrayList<>();
		try {
			PreparedStatement pst = getJdbcConnection().prepareStatement(GET_USER_DETAILS_QRY);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				userdetails.add(rs.getString(1));
				userdetails.add(rs.getString(2));
				userdetails.add(rs.getString(3));
				userdetails.add(rs.getString(4));
			}
		} catch (SQLException e) {
			throw new ServletException("Getting user details failed!");
		}
		return userdetails;
	}
	public void updateQuantity(List<String> pids, List<String> quantities) throws ServletException{
			for (int i=0; i<quantities.size(); i++) {
				try {
					PreparedStatement pst = getJdbcConnection().prepareStatement(UPD_QTY_QRY);
					pst.setString(1, quantities.get(i));
					pst.setString(2, pids.get(i));
					System.out.println("quantities value is " + quantities.get(i));
					System.out.println("pid used for quantities is " + pids.get(i));
					pst.executeUpdate();
				} catch (SQLException e) {
					throw new ServletException("Update quantities failed!");
				}
			}
	}
	
	
	
	public void add(String[] userdetails) throws ServletException {
		if (userdetails != null) {
			try (PreparedStatement pst = getJdbcConnection().prepareStatement(INSERT_NEWUSER_QRY);) {

				pst.setString(1, userdetails[0]);
				pst.setString(2, userdetails[1]);
				pst.setString(3, userdetails[2]);
				pst.setString(4, userdetails[3]);

				pst.executeUpdate();
			} catch (SQLException exp) {
				throw new ServletException("Adding user failed!");
			}
		}
		
	}
	
	public List<ProductMaster> getProductsList() throws ServletException {
		List<ProductMaster> products = new ArrayList<>();
		
		try (PreparedStatement pst = getJdbcConnection().prepareStatement(GET_ALL_PRODS_QRY);) {
			
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				ProductMaster product = new ProductMaster();
				product.setId(rs.getInt(1));
				product.setProductName(rs.getString(2));
				product.setCost(rs.getDouble(3));
				product.setProductDescription(rs.getString(4));
				
				products.add(product);
			}
			
			if(products.isEmpty()) {
				products=null;
			}

		} catch (SQLException exp) {
			throw new ServletException("Feteching products failed!");
		}
		
		return products;
	}
}