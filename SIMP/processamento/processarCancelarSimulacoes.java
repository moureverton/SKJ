/*    */ package processamento;
/*    */ 
/*    */ import br.com.sankhya.jape.EntityFacade;
/*    */ import br.com.sankhya.jape.dao.JdbcWrapper;
/*    */ import br.com.sankhya.modelcore.util.EntityFacadeFactory;
/*    */ import java.sql.PreparedStatement;
/*    */ 
/*    */ 
/*    */ public class processarCancelarSimulacoes
/*    */ {
/*    */   public static void processar() throws Exception {
/* 12 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/* 13 */     JdbcWrapper jdbcWrapper = dwf.getJdbcWrapper();
/* 14 */     jdbcWrapper.openSession();
/*    */     
/* 16 */     String sql = "UPDATE AD_SIMULARSIMP SET STATUS = '3' WHERE CODSIMSIM IN (SELECT \r\nCODSIMSIM\r\nFROM AD_SIMULARSIMP \r\nWHERE STATUS = '0'\r\nAND DATINC < (SYSDATE - INTERVAL '18' HOUR))";
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 21 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/* 22 */     pstm.executeUpdate();
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\processamento\processarCancelarSimulacoes.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */