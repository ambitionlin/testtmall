package tmall.servlet;

import tmall.dao.CategoryDAO;
import tmall.dao.PropertyDAO;
import tmall.dao.UserDAO;
import tmall.util.Page;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseBackServlet extends HttpServlet {
    public abstract String add(HttpServletRequest request, HttpServletResponse response, Page page);
    public abstract String delete(HttpServletRequest request, HttpServletResponse response, Page page);
    public abstract String edit(HttpServletRequest request, HttpServletResponse response, Page page);
    public abstract String update(HttpServletRequest request, HttpServletResponse response, Page page);
    public abstract String list(HttpServletRequest request, HttpServletResponse response, Page page);

    protected CategoryDAO categoryDAO= new CategoryDAO();
    protected UserDAO userDAO = new UserDAO();
    protected PropertyDAO propertyDAO = new PropertyDAO();

}
