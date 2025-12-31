package br.com.sankhya.jape.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;

// Agora ele assina o contrato de ser um EntityVO
public class DynamicVO implements EntityVO {
    
    // Implementação da Interface
    public Object getProperty(String name) { return null; }
    public void setProperty(String name, Object value) {}
    public Object getID() { return null; }

    // Métodos extras úteis que já tínhamos ou costumam ser usados
    public BigDecimal asBigDecimal(String name) { return null; }
    public String asString(String name) { return null; }
    public int asInt(String name) { return 0; }
    public Timestamp asTimestamp(String name) { return null; }
    public java.util.Date asDate(String name) { return null; }
}