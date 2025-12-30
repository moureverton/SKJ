/*    */ package br.com.sattva.lubmaster.simp.infra.db;
/*    */ 
/*    */ import br.com.sankhya.jape.EntityFacade;
/*    */ import br.com.sankhya.jape.sql.NativeSql;
/*    */ import br.com.sankhya.modelcore.util.EntityFacadeFactory;
/*    */ import br.com.sattva.lubmaster.simp.config.SimpSettings;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import java.util.Map;
/*    */ 
/*    */ public final class NativeSqlHelper
/*    */ {
/*    */   private final SimpSettings cfg;
/*    */   
/*    */   public NativeSqlHelper(SimpSettings cfg) {
/* 16 */     this.cfg = cfg;
/*    */   }
/*    */   public void query(String sql, Map<String, Object> params, RowHandler handler) throws Exception {
/* 19 */     NativeSql ns = null;
/* 20 */     ResultSet rs = null;
/*    */     try {
/* 22 */       ns = new NativeSql(EntityFacadeFactory.getDWFFacade().getJdbcWrapper());
/* 23 */       rs = ns.executeQuery(sql);
/* 24 */       while (rs.next()) {
/* 25 */         handler.handle(rs);
/*    */       }
/*    */     } finally {
/* 28 */       safeClose(rs);
/*    */     } 
/*    */   }
/*    */   private void safeClose(ResultSet rs) {
/* 32 */     if (rs != null) try { rs.close(); } catch (SQLException sQLException) {} 
/*    */   }
/*    */   
/*    */   public NativeSql newNativeSql() throws Exception {
/* 36 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/* 37 */     return new NativeSql(dwf.getJdbcWrapper());
/*    */   }
/*    */   
/*    */   public static interface RowHandler {
/*    */     void handle(ResultSet param1ResultSet) throws Exception;
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\infra\db\NativeSqlHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */