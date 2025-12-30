/*    */ package iniciar;
/*    */ 
/*    */ import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
/*    */ import br.com.sankhya.extensions.actionbutton.ContextoAcao;
/*    */ import br.com.sankhya.jape.EntityFacade;
/*    */ import br.com.sankhya.jape.dao.JdbcWrapper;
/*    */ import br.com.sankhya.modelcore.util.EntityFacadeFactory;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.ResultSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class update
/*    */   implements AcaoRotinaJava
/*    */ {
/*    */   public void doAction(ContextoAcao arg0) throws Exception {
/* 17 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/* 18 */     JdbcWrapper jdbcWrapper = dwf.getJdbcWrapper();
/* 19 */     jdbcWrapper.openSession();
/*    */     
/* 21 */     String sql = "SELECT\r\nLISTAGG(CODDETSIMSIMP,',') AS CODDETSIMSIMP,\r\nCODCID_ANP\r\nFROM (\r\nSELECT \r\nA.CODDETSIMSIMP,\r\nA.NUNOTA,\r\nA.CODINSTALACAO2,\r\nA.CNPJ_PARC,\r\nB.AD_CODINSTALACAO_ANP,\r\n(SELECT CID.AD_CODCID_ANP FROM TSICID CID WHERE CID.CODCID = B.CODCID OR CID.CODCID = C.CODCID) AS CODCID_ANP\r\nFROM AD_DETSIMULARSIMP A\r\nLEFT JOIN TGFPAR B ON A.CODINSTALACAO2 = B.AD_CODINSTALACAO_ANP\r\nLEFT JOIN TGFPAR C ON A.CNPJ_PARC = C.CGC_CPF\r\nWHERE CODSIMSIM = 90 AND CODOPER IN (182,31,32) AND A.NUNOTA IN (SELECT \r\nSIMP.NUNOTA\r\nFROM AD_DETSIMULARSIMP SIMP\r\nLEFT JOIN TGFPAR PAR1 ON SIMP.CODINSTALACAO2 = PAR1.AD_CODINSTALACAO_ANP\r\nLEFT JOIN TGFPAR PAR2 ON SIMP.CNPJ_PARC = PAR2.CGC_CPF\r\nWHERE SIMP.CODSIMSIM = 90 AND SIMP.CODOPER IN (182,31,32) AND SIMP.CODDETSIMSIMP !=A.CODDETSIMSIMP)\r\n)\r\nGROUP BY CODCID_ANP";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 44 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/*    */     
/* 46 */     ResultSet query = pstm.executeQuery();
/*    */     
/* 48 */     while (query.next()) {
/* 49 */       String detCodSimp = query.getString("CODDETSIMSIMP");
/* 50 */       String codCidAnp = query.getString("CODCID_ANP");
/*    */       
/* 52 */       String sql2 = "UPDATE AD_DETSIMULARSIMP SET CODCID_ANP = '" + codCidAnp + "' WHERE CODDETSIMSIMP IN (" + detCodSimp + ") AND CODSIMSIM = 90 AND CODOPER IN (182,31,32)";
/* 53 */       jdbcWrapper.getPreparedStatement(sql2).executeUpdate();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\inicia\\update.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */