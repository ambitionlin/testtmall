package tmall.dao;

import org.apache.commons.lang.time.DateUtils;
import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ProductDAO {
    public int getTotal(int cid){
        int total = 0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();){
            String sql = "select count(*) from Product where cid = "+cid;
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    public void add(Product bean){
        String sql = "insert into Product values(null,?,?,?,?,?,?,?)";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);){
            ps.setString(1,bean.getName());
            ps.setString(2,bean.getSubTitle());
            ps.setFloat(3,bean.getOrignalPrice());
            ps.setFloat(4,bean.getPromotePrice());
            ps.setInt(5,bean.getStock());
            ps.setInt(6,bean.getCategory().getId());
            ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
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
    public void update(Product bean){
        String sql ="update Product set name=?,subTitle=?,orignalPrice=?,promotePrice=?,stock=?,cid=?,createDate=? where id=?";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
            ps.setString(1,bean.getName());
            ps.setString(2,bean.getSubTitle());
            ps.setFloat(3,bean.getOrignalPrice());
            ps.setFloat(4,bean.getPromotePrice());
            ps.setInt(5,bean.getStock());
            ps.setInt(6,bean.getCategory().getId());
            ps.setTimestamp(7,DateUtil.d2t(bean.getCreateDate()));
            ps.setInt(8,bean.getId());
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void delete(int id){
        try(Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
            String sql = "delete  from Product where id = "+id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Product get(int id) {
        Product bean = null;
        try(Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
            String sql = "select * from Product where id = "+id;
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()){
                bean = new Product();
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float orignalPrice = rs.getFloat("orignalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                int cid = rs.getInt("cid");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOrignalPrice(orignalPrice);
                bean.setPromotePrice(promotePrice);
                bean.setStock(stock);
                Category category = new CategoryDAO().get(cid);
                bean.setCategory(category);
                bean.setCreateDate(createDate);
                bean.setId(id);
                setFirstProductImage(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }
    public List<Product> list(){
        return list(0,Short.MAX_VALUE);
    }
    public List<Product> list(int start,int count){
        List<Product> beans = new ArrayList<>();
        String sql = "select * from Product order by id desc limit ?,?";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
            ps.setInt(1,start);
            ps.setInt(2,count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Product bean = new Product();
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float orignalPrice = rs.getFloat("orignalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                int cid = rs.getInt("cid");
                int id = rs.getInt("id");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOrignalPrice(orignalPrice);
                bean.setPromotePrice(promotePrice);
                bean.setStock(stock);
                Category category = new CategoryDAO().get(cid);
                bean.setCategory(category);
                bean.setCreateDate(createDate);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
    public List<Product> list(int cid){
        return list(cid,0,Short.MAX_VALUE);
    }
    public List<Product> list(int cid,int start,int count){
        List<Product> beans = new ArrayList<>();
        String sql = "select * from Product where cid = ? order by id desc limit ?,?";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
            ps.setInt(1,cid);
            ps.setInt(2,start);
            ps.setInt(3,count);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Product bean = new Product();
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float orignalPrice = rs.getFloat("orignalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                int id = rs.getInt("id");
                bean.setId(id);
                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOrignalPrice(orignalPrice);
                bean.setPromotePrice(promotePrice);
                bean.setStock(stock);
                Category category = new CategoryDAO().get(cid);
                bean.setCategory(category);
                bean.setCreateDate(createDate);
                setFirstProductImage(bean);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public void fill(Category c){
        List<Product> ps = this.list(c.getId());
        c.setProducts(ps);
    }
    public void fill(List<Category> cs){
        for(Category c:cs){
            fill(c);
        }
    }
    public void fillByRow(List<Category> cs){
        int productNumberEachRow  = 8;
        for(Category c:cs){
            List<Product> products = c.getProducts();
            List<List<Product>> productsByRow  = new ArrayList<>();
            for(int i=0;i<products.size();i+=productNumberEachRow){
                int size = i+productNumberEachRow;
                size = size>products.size()?products.size():size;
                List<Product> productsOfEachRow = products.subList(i,size);
                productsByRow.add(productsOfEachRow);
            }
            c.setProductsByRow(productsByRow);
        }
    }
    public List<Product> search(String keyword,int start,int count){
        List<Product> beans = new ArrayList<>();
        if(keyword==null||0==keyword.trim().length()){
            return beans;
        }
        String sql = "select * from Product where name like ? limit ?,?";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);){
            Product bean = null;
            ps.setString(1,"%"+keyword.trim()+"%");
            ps.setInt(2,start);
            ps.setInt(3,count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                bean = new Product();
                String name = rs.getString("name");
                int id  = rs.getInt("id");
                int cid = rs.getInt("cid");
                String subTitle = rs.getString("subTitle");
                float orignalPrice = rs.getFloat("orignalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                bean.setName(name);
                bean.setId(id);
                bean.setSubTitle(subTitle);
                bean.setOrignalPrice(orignalPrice);
                bean.setPromotePrice(promotePrice);
                bean.setStock(stock);
                bean.setCreateDate(createDate);
                Category category = new CategoryDAO().get(cid);
                bean.setCategory(category);
                setFirstProductImage(bean);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
    public void setFirstProductImage(Product p){
        List<ProductImage> pis = new ProductImageDAO().list(p,ProductImageDAO.type_single);
        if(!pis.isEmpty()){
            p.setFirstProductImage(pis.get(0));
        }
    }
    public void setSaleAndReviewNumber(List<Product> products){
        for(Product p:products){
            setSaleAndReviewNumber(p);
        }
    }
    public void setSaleAndReviewNumber(Product p){
        int saleCount = new OrderItemDAO().getSaleCount(p.getId());
        p.setSaleCount(saleCount);
        int reviewCount = new ReviewDAO().getCount(p.getId());
        p.setReviewCount(reviewCount);
    }
}
