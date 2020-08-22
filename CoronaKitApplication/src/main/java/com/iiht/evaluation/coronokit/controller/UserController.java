package com.iiht.evaluation.coronokit.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iiht.evaluation.coronokit.dao.KitDao;
import com.iiht.evaluation.coronokit.dao.ProductMasterDao;
import com.iiht.evaluation.coronokit.model.CoronaKit;
import com.iiht.evaluation.coronokit.model.KitDetail;
import com.iiht.evaluation.coronokit.model.ProductMaster;

@WebServlet({"/user","/newuser","/insertuser","/showproducts","/addnewitem","/deleteitem","/showkit","/placeorder","/saveorder","/ordersummary"})
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private KitDao kitDAO;
	private ProductMasterDao productMasterDao;
	public void setProductMasterDao(ProductMasterDao productMasterDao) {this.productMasterDao = productMasterDao;}

	public void setKitDAO(KitDao kitDao) {this.kitDAO = kitDao;}
	public void init(ServletConfig config) {
		String jdbcURL = config.getServletContext().getInitParameter("jdbcUrl");
		String jdbcUsername = config.getServletContext().getInitParameter("jdbcUsername");
		String jdbcPassword = config. getServletContext().getInitParameter("jdbcPassword");
		this.kitDAO = new KitDao(jdbcURL, jdbcUsername, jdbcPassword);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		
		String viewName = "";
		try {
			switch (action) {
			case "newuser":
				viewName = showNewUserForm(request, response);
				break;
			case "insertuser":
				viewName = insertNewUser(request, response);
				break;
			case "showproducts":
				viewName = showAllProducts(request, response);
				break;	
			case "addnewitem":
				viewName = addNewItemToKit(request, response);
				break;
			case "deleteitem":
				viewName = deleteItemFromKit(request, response);
				break;
			case "showkit":
				viewName = showKitDetails(request, response);
				break;
			case "placeorder":
				viewName = showPlaceOrderForm(request, response);
				break;
			case "saveorder":
				viewName = saveOrderForDelivery(request, response);
				break;	
			case "ordersummary":
				viewName = showOrderSummary(request, response);
				break;	
			default : viewName = "notfound.jsp"; break;	
			}
		} catch (Exception ex) {
			
			throw new ServletException(ex.getMessage());
		}
			RequestDispatcher dispatch = 
					request.getRequestDispatcher(viewName);
			dispatch.forward(request, response);
	
	}

	private String showOrderSummary(HttpServletRequest request, HttpServletResponse response) {
		String view = "";
		try {
			List<String> userset = kitDAO.getUserDetails();
			userset.add(String.valueOf(kitDAO.getTotalPrice()));
			request.setAttribute("userset", userset);
			view = "ordersummary.jsp";
		} catch (ServletException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errPage.jsp";
		}
		
		return view;
	}

	private String saveOrderForDelivery(HttpServletRequest request, HttpServletResponse response) {
		return "placeorder.jsp";
	}

	private String showPlaceOrderForm(HttpServletRequest request, HttpServletResponse response) {
		return "placeorder.jsp";
	}

	private String showKitDetails(HttpServletRequest request, HttpServletResponse response) {
		String view="";
		//String[] quants = request.getParameterValues("pquantity");
		//String[] ids = request.getParameterValues("pid");
		ArrayList<String> pids = new ArrayList<String>(Arrays.asList(request.getParameterValues("pid")));
		ArrayList<String> quantities = new ArrayList<String>(Arrays.asList(request.getParameterValues("pquantity")));
		List<ProductMaster> products = null;
		try {
			kitDAO.updateQuantity(pids, quantities);			
			products = kitDAO.getOrderDetails();
			request.setAttribute("products", products);
			view="showkit.jsp";
		} catch (ServletException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errPage.jsp";
		}
		
		return view;
	}

	private String deleteItemFromKit(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return "";
	}

	private String addNewItemToKit(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return "";
	}

	private String showAllProducts(HttpServletRequest request, HttpServletResponse response) {
		String view = "";

		try {
			List<ProductMaster> products = kitDAO.getProductsList();
			request.setAttribute("products", products);
			view = "showproductstoadd.jsp";
		} catch (ServletException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errPage.jsp";
		}
		return view;
	}

	private String insertNewUser(HttpServletRequest request, HttpServletResponse response) {
		String view = "";
		
		String pname = request.getParameter("Name");
		String pemail = request.getParameter("Email");
		String pcontact = request.getParameter("Contact");
		String paddress = request.getParameter("Address");
		List<ProductMaster> products = null;
		String userdetails[] = {pname, pemail, pcontact, paddress};
		if(userdetails.length==4) {
		try {
			kitDAO.add(userdetails);
			products = kitDAO.getProductsList();
			view = "showproductstoadd.jsp";
		} catch (ServletException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errPage.jsp";
		}}
		request.setAttribute("username", pname);
		request.setAttribute("products", products);
		
		return view;
	}

	private String showNewUserForm(HttpServletRequest request, HttpServletResponse response) {
		return "newuser.jsp";
	}


}