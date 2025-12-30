/*    */ package br.com.sattva.lubmaster.simp.domain.model;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public final class SimpRecord {
/*    */   private final String opCode;
/*  7 */   private final Map<SimpField, Object> values = new LinkedHashMap<>();
/*  8 */   public SimpRecord(String opCode) { this.opCode = opCode; }
/*  9 */   public void put(SimpField f, Object v) { this.values.put(f, v); }
/* 10 */   public Object get(SimpField f) { return this.values.get(f); }
/* 11 */   public String opCode() { return this.opCode; } public Map<SimpField, Object> all() {
/* 12 */     return this.values;
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\domain\model\SimpRecord.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */