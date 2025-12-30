/*    */ package iniciar;
/*    */ 
/*    */ import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
/*    */ import br.com.sankhya.jape.EntityFacade;
/*    */ import br.com.sankhya.jape.dao.JdbcWrapper;
/*    */ import br.com.sankhya.jape.event.PersistenceEvent;
/*    */ import br.com.sankhya.jape.event.TransactionContext;
/*    */ import br.com.sankhya.jape.vo.DynamicVO;
/*    */ import br.com.sankhya.modelcore.util.EntityFacadeFactory;
/*    */ import java.math.BigDecimal;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.ResultSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class iniciarValidacaoSimulaSimp
/*    */   implements EventoProgramavelJava
/*    */ {
/*    */   public void afterDelete(PersistenceEvent arg0) throws Exception {}
/*    */   
/*    */   public void afterInsert(PersistenceEvent arg0) throws Exception {
/* 26 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/* 27 */     JdbcWrapper jdbcWrapper = dwf.getJdbcWrapper();
/* 28 */     jdbcWrapper.openSession();
/*    */     
/* 30 */     DynamicVO dados = (DynamicVO)arg0.getVo();
/*    */     
/* 32 */     BigDecimal codSimSimp = dados.asBigDecimalOrZero("CODSIMSIM");
/*    */     
/* 34 */     String sql = "SELECT COALESCE(STATUS,'0') AS STATUS  FROM AD_SIMULARSIMP WHERE CODSIMSIM =" + codSimSimp;
/* 35 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/* 36 */     ResultSet query = pstm.executeQuery();
/*    */     
/* 38 */     if (query.next()) {
/* 39 */       String status = query.getString("STATUS");
/* 40 */       if (status.equalsIgnoreCase("2") || status.equalsIgnoreCase("3")) {
/* 41 */         throw new Exception("S� pode ser alterado registros com status \"Simula��o\"");
/*    */       }
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
/*    */   public void beforeCommit(TransactionContext arg0) throws Exception {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void beforeDelete(PersistenceEvent arg0) throws Exception {
/* 61 */     afterInsert(arg0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void beforeInsert(PersistenceEvent arg0) throws Exception {
/* 67 */     afterInsert(arg0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void beforeUpdate(PersistenceEvent arg0) throws Exception {
/* 73 */     afterInsert(arg0);
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\iniciar\iniciarValidacaoSimulaSimp.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */