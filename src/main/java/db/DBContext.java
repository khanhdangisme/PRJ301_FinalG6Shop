/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author email
 */
public class DBContext {

    private Connection conn;
    private static final String DB_URL = "jdbc:sqlserver://127.0.0.1:1433;databaseName=G6Shop;encrypt=false";
    private static final String DB_USER = "sa";
    private static final String DB_PWD = "040425";

    public DBContext() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.conn = (Connection) DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
    }

    //executeSelect
    public ResultSet executeSelectQuery(String query, Object[] params) throws SQLException {
        PreparedStatement statement = this.getConnection().prepareStatement(query);

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
        }
        return statement.executeQuery();
    }

    //insert, update, delete
    //executeQuery
    public int executeQuery(String query, Object[] params) throws SQLException {

        PreparedStatement statement = this.getConnection().prepareStatement(query);

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
        }
        return statement.executeUpdate();
    }

    public int executeCallableUpdate(String sql, Object[] params) throws SQLException {
        try ( Connection conn = getConnection();  CallableStatement cs = conn.prepareCall(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    if (params[i] == null) {
                        // Xác định kiểu null theo vị trí
                        int sqlType;
                        switch (i) {
                            case 4: // Price
                                sqlType = Types.DECIMAL;
                                break;
                            case 5: // Quantity
                            case 6: // CategoryID
                                sqlType = Types.INTEGER;
                                break;
                            default:
                                sqlType = Types.NVARCHAR;
                                break;
                        }

                        cs.setNull(i + 1, sqlType);
                        System.out.println("Param[" + i + "] = NULL (" + getSqlTypeName(sqlType) + ")");
                    } else {
                        cs.setObject(i + 1, params[i]);
                        System.out.println("Param[" + i + "] = " + params[i]);
                    }
                }
            }

            boolean result = cs.execute();
            System.out.println("Execute result: " + result);
            return 1; // giả định success để check lỗi servlet

        }

    }

    private String getSqlTypeName(int sqlType) {
        switch (sqlType) {
            case Types.INTEGER:
                return "INTEGER";
            case Types.DECIMAL:
                return "DECIMAL";
            case Types.NVARCHAR:
                return "NVARCHAR";
            default:
                return "UNKNOWN(" + sqlType + ")";
        }
    }

}
