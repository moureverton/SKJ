/*    */ package processamento;
/*    */ 
/*    */ import br.com.sankhya.extensions.actionbutton.ContextoAcao;
/*    */ import br.com.sankhya.extensions.actionbutton.Registro;
/*    */ import br.com.sankhya.jape.EntityFacade;
/*    */ import br.com.sankhya.jape.dao.JdbcWrapper;
/*    */ import br.com.sankhya.modelcore.util.EntityFacadeFactory;
/*    */ import buscar.buscarEstoque;
/*    */ import java.math.BigDecimal;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class processarCancelarExportacao
/*    */ {
/*    */   public static void processar(ContextoAcao arg0) throws Exception {
/* 16 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/* 17 */     JdbcWrapper jdbcWrapper = dwf.getJdbcWrapper();
/* 18 */     jdbcWrapper.openSession();
/*    */     
/* 20 */     Registro[] registros = arg0.getLinhas();
/* 21 */     if (registros.length > 1)
/* 22 */       arg0.mostraErro("S� pode ser cancelado um arquivo por vez!");  byte b;
/*    */     int i;
/*    */     Registro[] arrayOfRegistro1;
/* 25 */     for (i = (arrayOfRegistro1 = registros).length, b = 0; b < i; ) { Registro registro = arrayOfRegistro1[b];
/* 26 */       BigDecimal codSimulacao = new BigDecimal((String)registro.getCampo("CODSIMSIM"));
/*    */       
/* 28 */       String update = "DELETE FROM AD_IMPORTADORESTOQUESIMPDET WHERE CODIMPDOCCAB IN (SELECT CODIMPDOCCAB FROM AD_IMPORTADORESTOQUESIMPCAB WHERE CODSIMSIM = " + codSimulacao + ")";
/* 29 */       buscarEstoque.update(jdbcWrapper, update);
/*    */       
/* 31 */       update = "DELETE FROM AD_IMPORTADORESTOQUESIMPCAB WHERE CODSIMSIM =" + codSimulacao;
/* 32 */       buscarEstoque.update(jdbcWrapper, update);
/*    */       
/* 34 */       update = "UPDATE AD_SIMULARSIMP SET STATUS = '3' WHERE CODSIMSIM =" + codSimulacao;
/* 35 */       buscarEstoque.update(jdbcWrapper, update);
/*    */       
/* 37 */       update = "UPDATE AD_ESTOQUESIMP SET STATUS = '2' WHERE CODSIMSIM =" + codSimulacao;
/* 38 */       buscarEstoque.update(jdbcWrapper, update);
/*    */       b++; }
/*    */   
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\processamento\processarCancelarExportacao.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */