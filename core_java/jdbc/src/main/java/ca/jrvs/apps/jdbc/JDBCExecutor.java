package ca.jrvs.apps.jdbc;

import java.sql.*;
public class JDBCExecutor {

    public static void main(String[] args) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost","hplussport","postgres" ,"password");
        try{
            Connection connection = dcm.getConnection();
            CustomerDAO customerDAO = new CustomerDAO(connection);

            OrderDAO orderDAO = new OrderDAO(connection);
            Order order = orderDAO.findById(1000);
            System.out.println(order);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
