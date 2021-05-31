package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataAccessObject;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class OrderDAO extends DataAccessObject<Order> {

    private final static String GET_BY_ID = "SELECT c.first_name, c.last_name, c.email, o.order_id, " +
            "o.creation_date, o.total_due, o.status, s.first_name, s.last_name, s.email, ol.quantity, " +
            "p.code, p.name, p.size, p.variety, p.price " +
            "FROM orders o JOIN customer c ON o.customer_id = c.customer_id " +
            "JOIN salesperson s ON o.salesperson_id = s.salesperson_id " +
            "JOIN order_item ol ON ol.order_id = o.order_id " +
            "JOIN product p ON ol.product_id = p.product_id " +
            "WHERE o.order_id = ?";

    public OrderDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Order findById(long id) {
        Order order = new Order();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_BY_ID)){
            statement.setLong(1,id);
            ResultSet rs = statement.executeQuery();
            long orderId = 0;
            List<OrderLine> orderLines = new ArrayList<>();
            while(rs.next()){
                if (orderId == 0){
                    order.setCustomerFirstName(rs.getString(1));
                    order.setCustomerLastName(rs.getString(2));
                    order.setCustomerEmail(rs.getString(3));
                    order.setId(rs.getLong(4));
                    orderId = order.getId();
                    order.setCreationDate(new Date(rs.getDate(5).getTime()));
                    order.setTotalDue(rs.getDouble(6));
                    order.setStatus(rs.getString(7));
                    order.setSalespersonFirstName(rs.getString(8));
                    order.setSalespersonLastName(rs.getString(9));
                    order.setSalespersonEmail(rs.getString(10));
                }
                OrderLine orderLine = new OrderLine();
                orderLine.setQuantity(rs.getInt(11));
                orderLine.setProductCode(rs.getString(12));
                orderLine.setProductName(rs.getString(13));
                orderLine.setProductSize(rs.getInt(14));
                orderLine.setProductVariety(rs.getString(15));
                orderLine.setProductPrice(rs.getDouble(16));
            }
            order.setOrderLines(orderLines);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return order;
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Order update(Order dto) {
        return null;
    }

    @Override
    public Order create(Order dto) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
