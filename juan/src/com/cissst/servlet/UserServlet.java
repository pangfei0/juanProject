package com.cissst.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cissst.entity.Admin;
import com.cissst.entity.CustomAccount;
import com.cissst.service.IAdminService;
import com.cissst.service.ICustomAccountService;
import com.cissst.service.impl.AdminServiceImpl;
import com.cissst.service.impl.CustomAccountServiceImpl;
import com.cissst.util.MD5Util;

public class UserServlet extends HttpServlet{
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String action = request.getParameter("action");
		IAdminService as = new AdminServiceImpl();
		ICustomAccountService cs = new CustomAccountServiceImpl();
		HttpSession session = request.getSession();
		//如果是登录验证֤
				if("login".equals(action)){
					String name = request.getParameter("username");
					String password = MD5Util.encode(request.getParameter("password"));
					String usertype = request.getParameter("usertype");
					if("admin".equals(usertype))//管理员登录
					{
						Admin a = as.findBynp(name, password);
						if(a != null){
							session.setAttribute("admin", a);
						    response.sendRedirect("index.jsp");
							}
						else{
							response.getWriter().write("<script charset='UTF-8'>alert(\'管理员账号或密码不存在！\');" +
		                        "location.href='login.jsp';</script>");
							}				
					}
					else//默认业主登入
					{
						CustomAccount c = cs.findBynp(name, password);
						if(c!=null){
							session.setAttribute("customAccount", c);
							request.getRequestDispatcher("index2.jsp").forward(request, response);
						}else{
						response.getWriter().write("<script charset='UTF-8'>alert(\'业主账号或密码不存在\');" +
		                        "location.href='login.jsp';</script>");
						}
					}
				}
			    //退出登录则session过期
				else if("logout".equals(action)){
						session.invalidate();
						//if (request.getSession(false)==null) System.out.println(123);
						response.sendRedirect("login.jsp");
				}
				//重新登录则session过期
				else if("relogin".equals(action)){
						session.invalidate();
						response.sendRedirect("login.jsp");
				}
				}
	}

		
