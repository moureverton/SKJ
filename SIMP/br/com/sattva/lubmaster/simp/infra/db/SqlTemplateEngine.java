/*    */ package br.com.sattva.lubmaster.simp.infra.db;
/*    */ 
/*    */ import java.util.Map;
/*    */ 
/*    */ public final class SqlTemplateEngine {
/*    */   public String render(String sql, Map<String, Object> params) {
/*  7 */     if (sql == null) {
/*  8 */       throw new IllegalArgumentException("SQL é nulo (ver QueryRepository.load).");
/*    */     }
/* 10 */     String out = sql;
/* 11 */     if (params == null || params.isEmpty()) {
/* 12 */       return out;
/*    */     }
/* 14 */     for (Map.Entry<String, Object> e : params.entrySet()) {
/* 15 */       String key = ":" + (String)e.getKey();
/* 16 */       String val = valueToSql(e.getValue());
/* 17 */       out = out.replace(key, val);
/*    */     } 
/* 19 */     return out;
/*    */   }
/*    */   
/*    */   private String valueToSql(Object v) {
/* 23 */     if (v == null)
/* 24 */       return "NULL"; 
/* 25 */     if (v instanceof Number)
/* 26 */       return v.toString(); 
/* 27 */     return "'" + v.toString().replace("'", "''") + "'";
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\infra\db\SqlTemplateEngine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */