package tmall.servlet;

import com.sun.xml.ws.policy.privateutil.PolicyUtils;
import org.omg.PortableInterceptor.INACTIVE;
import tmall.bean.Category;
import tmall.bean.Property;
import tmall.dao.PropertyDAO;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.PreparedStatement;
import java.util.List;

public class PropertyServlet extends BaseBackServlet{
    public String add(HttpServletRequest request, HttpServletResponse response, Page page){
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);
        String name = request.getParameter("name");
        Property p = new Property();
        p.setCategory(c);
        p.setName(name);
        propertyDAO.add(p);
        return "@admin_property_list?cid="+cid;
    }

    public String delete (HttpServletRequest request,HttpServletResponse response,Page page){
        int id = Integer.parseInt(request.getParameter("id"));
        Property p = propertyDAO.get(id);
        propertyDAO.delete(id);
        return "@admin_property_list?cid="+p.getCategory().getId();
    }

    public String edit(HttpServletRequest request,HttpServletResponse response,Page page){
        int id = Integer.parseInt(request.getParameter("id"));
        Property p = propertyDAO.get(id);
        request.setAttribute("p",p);
        return "admin/editProperty.jsp";
    }

    public String update(HttpServletRequest request,HttpServletResponse response, Page page){
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        Property p = new Property();
        p.setCategory(c);
        p.setId(id);
        p.setName(name);
        propertyDAO.update(p);
        return "@admin_property_list?cid="+p.getCategory().getId();
    }

    public String list(HttpServletRequest request,HttpServletResponse response,Page page){
        int cid = Integer.parseInt(request.getParameter("cid"));//获取分类cid
        Category c = categoryDAO.get(cid);
        //基于cid,获取当前分类下的属性集合
        List<Property> ps = propertyDAO.list(cid,page.getStart(),page.getCount());
        //获取当前分类下的属性总数，并且设置给分页page对象
        int total = propertyDAO.getTotal(cid);
        page.setTotal(total);
        //拼接字符串"&cid="+c.getId(),设置给page对象的param值。因为属性分页都是基于当前分类下的分页，所以分页的时候需要传递这个cid
        page.setParam("&cid="+c.getId());
        //把属性集合设置到request的"ps"属性上
        request.setAttribute("ps",ps);
        //把分类对象设置到request的"c"属性上
        request.setAttribute("c",c);
        //把分页对象设置到request的"page"对象上
        request.setAttribute("page",page);
        //服务端跳转到“admin/listProperty.jsp”
        return "admin/listProperty.jsp";
        //在listProperty.jsp页面上使用c:forEach 遍历ps集合，并显示
    }
}
