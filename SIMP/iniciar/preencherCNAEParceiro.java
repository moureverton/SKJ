/*    */ package iniciar;
/*    */ 
/*    */ import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
/*    */ import br.com.sankhya.jape.EntityFacade;
/*    */ import br.com.sankhya.jape.dao.JdbcWrapper;
/*    */ import br.com.sankhya.jape.event.PersistenceEvent;
/*    */ import br.com.sankhya.jape.event.TransactionContext;
/*    */ import br.com.sankhya.jape.vo.DynamicVO;
/*    */ import br.com.sankhya.modelcore.util.EntityFacadeFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class preencherCNAEParceiro
/*    */   implements EventoProgramavelJava
/*    */ {
/*    */   public void afterDelete(PersistenceEvent arg0) throws Exception {}
/*    */   
/*    */   public void afterInsert(PersistenceEvent arg0) throws Exception {
/* 22 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/* 23 */     JdbcWrapper jdbcWrapper = dwf.getJdbcWrapper();
/* 24 */     jdbcWrapper.openSession();
/*    */     
/* 26 */     DynamicVO dados = (DynamicVO)arg0.getVo();
/* 27 */     String cnae = dados.asString("CNAE");
/* 28 */     String ad_cnae = dados.asString("AD_CNAE_ANP");
/* 29 */     if ((!cnae.equalsIgnoreCase("null") || ad_cnae.equalsIgnoreCase("null")) && 
/* 30 */       cnae.length() >= 5) {
/* 31 */       dados.setProperty("AD_CNAE_ANP", cnae.substring(0, 5));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterUpdate(PersistenceEvent arg0) throws Exception {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void beforeCommit(TransactionContext arg0) throws Exception {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void beforeDelete(PersistenceEvent arg0) throws Exception {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void beforeInsert(PersistenceEvent arg0) throws Exception {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void beforeUpdate(PersistenceEvent arg0) throws Exception {
/* 63 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/* 64 */     JdbcWrapper jdbcWrapper = dwf.getJdbcWrapper();
/* 65 */     jdbcWrapper.openSession();
/*    */     
/* 67 */     DynamicVO dados = (DynamicVO)arg0.getVo();
/* 68 */     String cnae = dados.asString("CNAE");
/* 69 */     String ad_cnae = dados.asString("AD_CNAE_ANP");
/* 70 */     if (!cnae.equalsIgnoreCase("null") && (arg0.getModifingFields().isModifing("CNAE") || arg0.getModifingFields().isModifing("AD_CNAE_ANP")) && 
/* 71 */       cnae.length() >= 5)
/* 72 */       dados.setProperty("AD_CNAE_ANP", cnae.substring(0, 5)); 
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\iniciar\preencherCNAEParceiro.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */