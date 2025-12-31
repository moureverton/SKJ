package br.com.sankhya.jape.sql;

import br.com.sankhya.jape.dao.JdbcWrapper;
import java.math.BigDecimal;
import java.sql.ResultSet;

public class NativeSql {
    
    public NativeSql(JdbcWrapper jdbc) {}
    
    // --- VERSÃO LEGACY (Tudo retorna VOID) ---
    
    public void appendSql(String sql) {}
    
    public void setCommand(String sql) {}
    
    // Parâmetros (Bind Variables) - Tudo VOID
    public void setBigDecimal(int pos, BigDecimal val) {}
    public void setString(int pos, String val) {}
    public void setTimestamp(int pos, java.sql.Timestamp val) {}
    public void setDate(int pos, java.sql.Date val) {}
    public void setInt(int pos, int val) {}
    public void setDouble(int pos, double val) {}
    
    // Parâmetros Nomeados
    public void setNamedParameter(String name, Object value) {}
    
    // --- EXECUÇÃO (Mantendo a compatibilidade dupla) ---
    
    // 1. Versão que aceita String (Seu código usa esta)
    public int executeUpdate(String sql) throws Exception { return 0; } 
    public ResultSet executeQuery(String sql) throws Exception { return null; }

    // 2. Versão padrão do Jape (Sem parâmetros)
    public int executeUpdate() throws Exception { return 0; }
    public ResultSet executeQuery() throws Exception { return null; }
    
    public boolean next() { return false; } 

    public void cleanParameters() {}
    public void resetSql() {}
    
    // Getters
    public BigDecimal getBigDecimal(String campo) throws Exception { return null; }
    public String getString(String campo) throws Exception { return null; }
    public int getInt(String campo) throws Exception { return 0; }
}