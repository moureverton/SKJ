/*    */ package br.com.sattva.lubmaster.simp.erp.service;
/*    */ 
/*    */ import br.com.sankhya.jape.EntityFacade;
/*    */ import br.com.sankhya.jape.sql.NativeSql;
/*    */ import br.com.sankhya.modelcore.util.EntityFacadeFactory;
/*    */ import br.com.sattva.lubmaster.simp.config.SimpSettings;
/*    */ import br.com.sattva.lubmaster.simp.infra.db.NativeSqlHelper;
/*    */ import java.math.BigDecimal;
/*    */ import java.sql.Date;
/*    */ import java.sql.ResultSet;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Date;
/*    */ import java.util.List;
/*    */ 
/*    */ final class OnlineOperationRepository
/*    */ {
/*    */   private final SimpSettings cfg;
/*    */   
/*    */   static class OpDef
/*    */   {
/*    */     String code;
/*    */     String sql;
/*    */   }
/*    */   
/*    */   OnlineOperationRepository(SimpSettings cfg) {
/* 26 */     this.cfg = cfg;
/*    */   }
/*    */   
/*    */   List<OpDef> loadActiveOps() throws Exception {
/* 30 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/* 31 */     NativeSql ns = new NativeSql(dwf.getJdbcWrapper());
/* 32 */     ns.appendSql("SELECT CODOPER_ANP, CONSULTA FROM AD_OPERACAOANP WHERE ATIVA = 'S' AND CONSULTA IS NOT NULL ORDER BY 1");
/* 33 */     ResultSet rs = ns.executeQuery();
/* 34 */     List<OpDef> list = new ArrayList<>();
/* 35 */     while (rs.next()) {
/* 36 */       OpDef d = new OpDef();
/* 37 */       d.code = rs.getString("CODOPER_ANP");
/* 38 */       d.sql = rs.getString("CONSULTA");
/* 39 */       if (d.code != null && d.sql != null && d.sql.trim().length() > 0) {
/* 40 */         list.add(d);
/*    */       }
/*    */     } 
/* 43 */     rs.close();
/* 44 */     return list;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   ResultSet executeOpSql(NativeSqlHelper helper, String sql, int codEmp, Date dtIni, Date dtFim, int mesRef, int anoRef) throws Exception {
/* 54 */     NativeSql ns = helper.newNativeSql();
/* 55 */     ns.appendSql(sql);
/*    */     
/* 57 */     Date dIni = new Date(dtIni.getTime());
/* 58 */     Date dFim = new Date(dtFim.getTime());
/*    */     
/* 60 */     ns.setNamedParameter("CODEMP", new BigDecimal(codEmp));
/* 61 */     ns.setNamedParameter("DTINI", dIni);
/* 62 */     ns.setNamedParameter("DTFIM", dFim);
/* 63 */     ns.setNamedParameter("MES_REFERENCIA", new BigDecimal(mesRef));
/* 64 */     ns.setNamedParameter("ANO_REFERENTE", new BigDecimal(anoRef));
/* 65 */     ns.setNamedParameter("REFERENCIA", dIni);
/*    */     
/* 67 */     return ns.executeQuery();
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\erp\service\OnlineOperationRepository.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */