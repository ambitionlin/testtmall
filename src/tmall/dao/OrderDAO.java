package tmall.dao;

import tmall.bean.Order;
import tmall.bean.User;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class OrderDAO {
    public int getTotal(){
        int total = 0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();){
            String sql = "select count(*) from Order_";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    public void add(Order bean){
        String sql = "insert into Order_ values(null,?,?,?,?,?,?,?,?,?,?,?,?)";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
            ps.setString(1,bean.getOrderCode());
            ps.setString(2,bean.getAddress());
            ps.setString(3,bean.getPost());
            ps.setString(4,bean.getReceiver());
            ps.setString(5,bean.getMobile());
            ps.setString(6,bean.getUserMessage());
            ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
            ps.setTimestamp(8,DateUtil.d2t(bean.getPayDate()));
            ps.setTimestamp(9,DateUtil.d2t(bean.getDeliveryDate()));
            ps.setTimestamp(10,DateUtil.d2t(bean.getConfirmDate()));
            ps.setInt(11,bean.getUser().getId());
            ps.setString(12,bean.getStatus());
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
    public void update(Order bean){
        String sql = "update Order_ set orderCode=?,address=?,post=?,receiver=?,mobile=?,userMessage=?," +
                "createDate=?,payDate=?,deliveryDate=?,confirmDate=?,uid=? ,status=? where id = ?";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
            ps.setString(1,bean.getOrderCode());
            ps.setString(2,bean.getAddress());
            ps.setString(3,bean.getPost());
            ps.setString(4,bean.getReceiver());
            ps.setString(5,bean.getMobile());
            ps.setString(6,bean.getUserMessage());
            ps.setTimestamp(7,DateUtil.d2t(bean.getCreateDate()));
            ps.setTimestamp(8,DateUtil.d2t(bean.getPayDate()));
            ps.setTimestamp(9,DateUtil.d2t(bean.getDeliveryDate()));
            ps.setTimestamp(10,DateUtil.d2t(bean.getConfirmDate()));
            ps.setInt(11,bean.getUser().getId());
            ps.setString(12,bean.getStatus());
            ps.setInt(13,bean.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void delete(int id){
        try(Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
            String sql = "delete from Order_ where id = "+id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Order get(int id) {
        Order bean = new Order();
        try(Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
            String sql = "select * from Order_ where id ="+id;
            ResultSet rs = s.executeQuery(sql);
            if(rs.next()){
                String orderCode = rs.getString("orderOrde");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));
                int uid = rs.getInt("uid");
                String status = rs.getString("status");

                User user = new UserDAO().get(uid);
                bean.setUser(user);
                bean.setOrderCode(orderCode);
                bean.setAddress(address);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setMobile(mobile);
                bean.setUserMessage(userMessage);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                bean.setStatus(status);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }
    public List<Order> list(){
        return list(0,Short.MAX_VALUE);
    }
    public List<Order> list(int start,int count){
        List<Order> beans = new ArrayList<>();
        String sql = "select * from Order_ order by id desc limit ?,?";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
            ps.setInt(1,start);
            ps.setInt(2,count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Order bean = new Order();
                String orderCode = rs.getString("orderOrde");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));
                int uid = rs.getInt("uid");
                String status = rs.getString("status");
                int id = rs.getInt("id");
                User user = new UserDAO().get(uid);
                bean.setUser(user);
                bean.setOrderCode(orderCode);
                bean.setAddress(address);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setMobile(mobile);
                bean.setUserMessage(userMessage);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                bean.setStatus(status);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
    public List<Order> list(int uid,String excludedStatus){
        return list(uid,excludedStatus,0,Short.MAX_VALUE);
    }
    public List<Order> list(int uid,String excludedStatus,int start, int count){
        List<Order> beans = new ArrayList<>();
        String sql = "select * from Order_ where uid=? and status !=? order by id desc limit ?,?";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
            ps.setInt(1,uid);
            ps.setString(2,excludedStatus);
            ps.setInt(3,start);
            ps.setInt(4,count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Order bean = new Order();
                String orderCode = rs.getString("orderOrde");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                String status = rs.getString("status");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));
                int id = rs.getInt("id");

                User user = new UserDAO().get(uid);
                bean.setUser(user);
                bean.setOrderCode(orderCode);
                bean.setAddress(address);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setMobile(mobile);
                bean.setUserMessage(userMessage);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                bean.setStatus(status);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
}
