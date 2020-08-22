package com.iiht.evaluation.coronokit.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.iiht.evaluation.coronokit.dao.ProductMasterDao;
import com.iiht.evaluation.coronokit.model.ProductMaster; 

@WebServlet({"/admin","/login","/list","/newproduct","/insertproduct","/deleteproduct","/editproduct","/updateproduct","/logout"})
public class AdminController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProductMasterDao productMasterDao;

	public void setProductMasterDao(ProductMasterDao productMasterDao) {
		this.productMasterDao = productMasterDao;
	}

	public void init(ServletConfig config) {
		String jdbcURL = config.getServletContext().getInitParameter("jdbcUrl");
		String jdbcUsername = config.getServletContext().getInitParameter("jdbcUsername");
		String jdbcPassword = config.getServletContext().getInitParameter("jdbcPassword");
		this.productMasterDao = new ProductMasterDao(jdbcURL, jdbcUsername, jdbcPassword);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action =  request.getParameter("action");
		String viewName = "";
		try {
			switch (action) {
			case "login" : 
				viewName = adminLogin(request, response);
				break;
			case "newproduct":
				viewName = showNewProductForm(request, response);
				break;
			case "insertproduct":
				viewName = insertProduct(request, response);
				break;
			case "deleteproduct":
				viewName = deleteProduct(request, response);
				break;
			case "editproduct":
				viewName = showEditProductForm(request, response);
				break;
			case "updateproduct":
				viewName = updateProduct(request, response);
				break;
			case "list":
				viewName = listAllProducts(request, response);
				break;	
			case "logout":
				viewName = adminLogout(request, response);
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

	private String adminLogout(HttpServletRequest request, HttpServletResponse response) {
		return "index.jsp";
	}

	private String listAllProducts(HttpServletRequest request, HttpServletResponse response) {
		String view = "";

		try {
			List<ProductMaster> products = productMasterDao.getAll();
			request.setAttribute("products", products);
			view = "listproducts.jsp";
		} catch (ServletException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errPage.jsp";
		}

		return view;
	}

	private String updateProduct(HttpServletRequest request, HttpServletResponse response) {
		int pid = Integer.parseInt(request.getParameter("pid"));
		String view = "";

		try {
			ProductMaster product = productMasterDao.getById(pid);
			request.setAttribute("product", product);
			view = "editproduct.jsp";
		} catch (ServletException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errPage.jsp";
		}

		return view;
	}

	private String showEditProductForm(HttpServletRequest request, HttpServletResponse response) {
		int pid = Integer.parseInt(request.getParameter("pid"));
		String view = "";
		ProductMaster product = new ProductMaster();
		product.setProductName(request.getParameter("pname"));
		product.setCost(Double.parseDouble(request.getParameter("pcost")));
		product.setProductDescription(request.getParameter("pdesc"));
		product.setId(pid);
		
		try {
			productMasterDao.save(product);
			view = "listproducts.jsp";
		} catch (ServletException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errPage.jsp";
		}

		return view;
	}

	private String deleteProduct(HttpServletRequest request, HttpServletResponse response) {
		int pid = Integer.parseInt(request.getParameter("pid"));
		String view = "";

		try {
			productMasterDao.deleteById(pid);
			view = "listproducts.jsp";
		} catch (ServletException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errPage.jsp";
		}

		return view;
	}

	private String insertProduct(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html");
		request.setAttribute("msg", "<h3> <%= Product added successfully %> </h3>");
		return "newproduct.jsp";
	}

	private String showNewProductForm(HttpServletRequest request, HttpServletResponse response) {
		ProductMaster product = new ProductMaster();

		//product.setId(Integer.parseInt(request.getParameter("pid")));
		product.setProductName(request.getParameter("pname"));
		product.setCost(Double.parseDouble(request.getParameter("pcost")));
		product.setProductDescription(request.getParameter("pdesc"));

		String view = "";
		try {
			productMasterDao.add(product);
			view="newproduct.jsp";
		} catch (ServletException e) {
			request.setAttribute("errMsg", e.getMessage());
			view = "errPage.jsp";
		}
		return view;
	}

	private String adminLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String username = request.getParameter("loginid");
		String pwd = request.getParameter("password");
		String view = "index.jsp";
		if(username.equals("admin") && pwd.equals("admin")) view = "listproducts.jsp";
		return view;
	}

	
}