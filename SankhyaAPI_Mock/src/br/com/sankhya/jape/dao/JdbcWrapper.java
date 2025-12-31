package br.com.sankhya.jape.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;

public class JdbcWrapper {
    // Como é CLASSE, os métodos precisam de corpo, mesmo que vazios
    public void openSession() {}
    
    public void closeSession() {}
    
    public PreparedStatement getPreparedStatement(String sql) throws Exception { return null; }
    
    public Statement getStatement() throws Exception { return null; }
    
    public Connection getConnection() throws Exception { return null; }
    
    // Método auxiliar que às vezes é pedido quando é classe
    public ResultSet executeQuery(String sql) throws Exception { return null; }
    public int executeUpdate(String sql) throws Exception { return 0; }
}