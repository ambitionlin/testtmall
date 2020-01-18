package tmall.dao;

import com.sun.org.apache.xpath.internal.operations.Or;
import tmall.bean.Order;
import tmall.bean.OrderItem;
import tmall.bean.Product;
import tmall.bean.User;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {
    public int getTotal(){
        int total = 0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();){
            String sql = "select count(*) from OrderItem";
            ResultSet rs = s.executeQuery(sql);
            while(rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    public void add(OrderItem bean){
        String sql = "insert into OrderItem values(null,?,?,?,?)";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);){
            ps.setInt(1,bean.getNumber());
            ps.setInt(2,bean.getProduct().getId());
            //订单项在创建的时候，是没有带订单信息的
            if(null==bean.getOrder()){
                ps.setInt(3,-1);
            }else{
                ps.setInt(3,bean.getOrder().getId());
            }
            ps.setInt(4,bean.getUser().getId());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
               int id = rs.getInt("id");
               bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void update(OrderItem bean){
        String sql = "update OrderItem set number=?, pid=?, oid=?, uid=? where id = ?";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
            ps.setInt(1,bean.getNumber());
            ps.setInt(2,bean.getProduct().getId());
            if(null==bean.getOrder()){
                ps.setInt(3,-1);
            }else{
                ps.setInt(3,bean.getOrder().getId());
            }
            ps.setInt(4,bean.getUser().getId());
            ps.setInt(5,bean.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void delete(int id){
        try(Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
            String sql = "delete from OrderItem where id ="+id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public OrderItem get(int id){
        OrderItem bean = null;
        try(Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
            String sql = "select * from OrderItem where id ="+id;
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()){
                bean = new OrderItem();
                int number = rs.getInt("number");
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int uid = rs.getInt("uid");
                bean.setNumber(number);
                Product product = new ProductDAO().get(pid);
                bean.setProduct(product);
                if(-1!=oid){
                    Order order = new OrderDAO().get(oid);
                    bean.setOrder(order);
                }
                User user = new UserDAO().get(uid);
                bean.setUser(user);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }
    public List<OrderItem> listByProduct(int pid){
        return listByProduct(pid,0,Short.MAX_VALUE);
    }
    public List<OrderItem> listByProduct(int pid, int start,int count){
        List<OrderItem> beans = new ArrayList<>();
        String sql = "select * from OrderItem where pid = ? order by id desc limit ?,?";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
            ps.setInt(1,pid);
            ps.setInt(2,start);
            ps.setInt(3,count);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                OrderItem bean = new OrderItem();
                int number = rs.getInt("number");
                int oid = rs.getInt("oid");
                int uid = rs.getInt("uid");
                int id = rs.getInt("id");
                bean.setNumber(number);
                Product product = new ProductDAO().get(pid);
                bean.setProduct(product);
                if(oid!=-1){
                    Order order = new OrderDAO().get(oid);
                    bean.setOrder(order);
                }
                User user = new UserDAO().get(uid);
                bean.setUser(user);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
    public List<OrderItem> listByOrder(int oid){
        return listByOrder(oid,0,Short.MAX_VALUE);
    }
    public List<OrderItem> listByOrder(int oid,int start,int count){
        List<OrderItem> beans = new ArrayList<>();
        String sql = "select * from OrderItem where oid = ? order by id desc limit ?,?";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
            ps.setInt(1,oid);
            ps.setInt(2,start);
            ps.setInt(3,count);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                OrderItem bean = new OrderItem();
                int number = rs.getInt("number");
                int pid  = rs.getInt("pid");
                int uid = rs.getInt("uid");
                int id = rs.getInt("id");
                Product product = new ProductDAO().get(pid);
                User user = new UserDAO().get(uid);
                bean.setId(id);
                bean.setNumber(number);
                if(oid!=-1){
                    Order order = new OrderDAO().get(oid);
                    bean.setOrder(order);
                }
                bean.setProduct(product);
                bean.setUser(user);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
    public List<OrderItem> listByUser(int uid){
        return listByUser(uid,0,Short.MAX_VALUE);
    }
    public List<OrderItem> listByUser(int uid,int start,int count){
        List<OrderItem> beans = new ArrayList<>();
        String sql = "select * from OrderItem where uid = ? and oid=-1 order by id desc limit ?,?";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
            ps.setInt(1,uid);
            ps.setInt(2,start);
            ps.setInt(3,count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                OrderItem bean = new OrderItem();
                int number = rs.getInt("number");
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int id = rs.getInt("id");
                Product product = new ProductDAO().get(pid);
                User user = new UserDAO().get(uid);
                if(oid!=-1){
                    Order order = new OrderDAO().get(oid);
                    bean.setOrder(order);
                }
                bean.setNumber(number);
                bean.setUser(user);
                bean.setId(id);
                bean.setProduct(product);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
    public void fill(Order o){
        List<OrderItem> ois = listByOrder(o.getId());
        float total = 0;
        for(OrderItem oi:ois){
            total+=oi.getNumber()*oi.getProduct().getPromotePrice();
        }
        o.setTotal(total);
        o.setOrderItems(ois);
    }
    public void fill(List<Order> os){
        for(Order o:os){
            List<OrderItem> ois = listByOrder(o.getId());
            float total = 0;
            int totalNumber = 0;
            for(OrderItem oi:ois){
                total += oi.getNumber()*oi.getProduct().getPromotePrice();
                totalNumber+=oi.getNumber();
            }
            o.setTotal(total);
            o.setOrderItems(ois);
            o.setTotalNumber(totalNumber);
        }
    }
    public int getSaleCount(int pid) {
        int total = 0;
        try(Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
            String sql = "select sum(number) from OrderItem where pid = "+pid;
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

}
