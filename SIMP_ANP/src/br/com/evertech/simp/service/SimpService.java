package br.com.evertech.simp.service;

import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.evertech.simp.util.SimpLayoutManager;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

public class SimpService {

    // Caminho do recurso dentro do JAR
    private static final String LAYOUT_PATH = "/br/com/evertech/simp/layout/v1_11.json";

    public void executarCarga(JdbcWrapper jdbc, BigDecimal codSimp, BigDecimal codEmp, String dtIni, String dtFim) throws Exception {
        // 1. Limpeza
        NativeSql del = new NativeSql(jdbc);
        del.executeUpdate("DELETE FROM AD_DETSIMULARSIMP WHERE CODSIMSIM = " + codSimp);

        // 2. Execução do Script de Carga (Use o SQL que montamos anteriormente)
        // Aqui estou simplificando, mas você deve ler o arquivo .sql dos resources se for grande
        String sqlCarga = "INSERT INTO AD_DETSIMULARSIMP (...) SELECT ... FROM TGFCAB ... WHERE ..."; 
        
        NativeSql ins = new NativeSql(jdbc);
        // ins.setCommand(lerSqlDosResources("/br/com/suaempresa/simp/sql/carga_detalhe.sql"));
        // Configure parametros...
        // ins.executeUpdate();
        
        System.out.println("Carga realizada para SIMP ID: " + codSimp);
    }

    public byte[] gerarArquivoTxt(JdbcWrapper jdbc, BigDecimal codSimp) throws Exception {
        SimpLayoutManager layout = new SimpLayoutManager(LAYOUT_PATH);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        // 1. Busca dados ordenados
        NativeSql sql = new NativeSql(jdbc);
        sql.appendSql("SELECT * FROM AD_DETSIMULARSIMP WHERE CODSIMSIM = ? ORDER BY CODOPER, CODPROD_ANP");
        sql.setBigDecimal(1, codSimp);
        
        ResultSet rs = sql.executeQuery();
        
        // Loop para processar linhas
        while(rs.next()) {
            Map<String, Object> linhaMap = resultSetParaMap(rs);
            
            // Aqui você pode adicionar lógica de "Totalizador" se a operação mudar
            // ...
            
            String linhaTxt = layout.gerarLinhaTxt(linhaMap);
            baos.write(linhaTxt.getBytes("ISO-8859-1")); // ANP exige encoding específico
            baos.write("\r\n".getBytes()); // Quebra de linha Windows
        }
        
        return baos.toByteArray();
    }

    // Utilitário para converter ResultSet em Map para facilitar o uso no LayoutManager
    private Map<String, Object> resultSetParaMap(ResultSet rs) throws Exception {
        Map<String, Object> map = new HashMap<>();
        ResultSetMetaData meta = rs.getMetaData();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            String colName = meta.getColumnName(i).toUpperCase();
            
            // DE-PARA: Mapear nome da COLUNA DO BANCO para nome do CAMPO JSON
            if(colName.equals("CODOPER")) map.put("CODIGO_OPERACAO", rs.getObject(i));
            else if(colName.equals("CODPROD_ANP")) map.put("CODIGO_PRODUTO_OPERADO", rs.getObject(i));
            else if(colName.equals("QTD_PROD_OPERADO_KG")) map.put("QTD_PRODUTO_KG", rs.getObject(i));
            // ... adicione os outros mapeamentos ...
            else map.put(colName, rs.getObject(i));
        }
        return map;
    }
}