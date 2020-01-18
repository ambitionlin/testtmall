package tmall.dao;

import tmall.bean.Product;
import tmall.bean.Review;
import tmall.bean.User;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
public class ReviewDAO {
    public int getTotal(){
        int total = 0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();){
            String sql = "select count(*) from Review";
            ResultSet rs = s.executeQuery(sql);
            while(rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    public int getTotal(int pid){
        int total = 0;
        try(Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
            String sql = "select count(*) from Review where pid ="+pid;
            ResultSet rs = s.executeQuery(sql);
            while(rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    public void add(Review bean){
        String sql = "insert into Review values(null,?,?,?,?)";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);){
            ps.setString(1,bean.getContent());
            ps.setInt(2,bean.getUser().getId());
            ps.setInt(3,bean.getProduct().getId());
            ps.setTimestamp(4, DateUtil.d2t(bean.getCreateDate()));
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
    public void update(Review bean){
        String sql = "update Review set content =?,uid=?,pid=? and createDate = ? where id = ?";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
            ps.setString(1,bean.getContent());
            ps.setInt(2,bean.getUser().getId());
            ps.setInt(3,bean.getProduct().getId());
            ps.setTimestamp(4,DateUtil.d2t(bean.getCreateDate()));
            ps.setInt(5,bean.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void delete(int id){
        try(Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
            String sql = "delete from Review where id ="+id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Review get(int id){
        Review bean = null;
        try(Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
            String sql = "select * from Review where id = "+id;
            ResultSet rs = s.executeQuery(sql);
            if(rs.next()){
                bean = new Review();
                String content = rs.getString("content");
                int uid = rs.getInt("uid");
                int pid = rs.getInt("pid");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                User user = new UserDAO().get(uid);
                Product product = new ProductDAO().get(pid);
                bean.setId(id);
                bean.setContent(content);
                bean.setUser(user);
                bean.setProduct(product);
                bean.setCreateDate(createDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }
    //getCount()与上面的getTotal()功能一样,可以删除不写
    public int getCount(int pid) {
        int total = 0;
        String sql = "select count(*) from Review where pid = ?";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
            ps.setInt(1,pid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    public List<Review> list(int pid){
        return list(pid,0,Short.MAX_VALUE);
    }
    public List<Review> list(int pid,int start,int count){
        List<Review> beans = new ArrayList<>();
        String sql = "select * from Review where pid = ? order by id desc limit ?,?";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
            ps.setInt(1,pid);
            ps.setInt(2,start);
            ps.setInt(3,count);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Review bean = new Review();
                int id = rs.getInt("id");
                String content = rs.getString("content");
                int uid = rs.getInt("uid");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                User user = new UserDAO().get(uid);
                Product product = new ProductDAO().get(pid);
                bean.setId(id);
                bean.setContent(content);
                bean.setUser(user);
                bean.setProduct(product);
                bean.setCreateDate(createDate);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
    public boolean isExist(String content,int pid){
        String sql  = "select * from Review where content=? and pid = ?";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
            ps.setString(1,content);
            ps.setInt(2,pid);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
