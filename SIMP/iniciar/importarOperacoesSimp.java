/*    */ package iniciar;
/*    */ 
/*    */ import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
/*    */ import br.com.sankhya.extensions.actionbutton.ContextoAcao;
/*    */ import br.com.sankhya.extensions.actionbutton.Registro;
/*    */ import br.com.sankhya.jape.EntityFacade;
/*    */ import br.com.sankhya.jape.dao.JdbcWrapper;
/*    */ import br.com.sankhya.jape.vo.DynamicVO;
/*    */ import br.com.sankhya.jape.vo.EntityVO;
/*    */ import br.com.sankhya.modelcore.util.EntityFacadeFactory;
/*    */ import java.math.BigDecimal;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.ResultSet;
/*    */ import java.util.ArrayList;
/*    */ import processamento.FileReaderService;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class importarOperacoesSimp
/*    */   implements AcaoRotinaJava
/*    */ {
/*    */   public void doAction(ContextoAcao arg0) throws Exception {
/* 23 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/* 24 */     JdbcWrapper jdbcWrapper = dwf.getJdbcWrapper();
/* 25 */     jdbcWrapper.openSession();
/*    */     
/* 27 */     Registro[] registros = arg0.getLinhas(); byte b; int i; Registro[] arrayOfRegistro1;
/* 28 */     for (i = (arrayOfRegistro1 = registros).length, b = 0; b < i; ) { Registro registro = arrayOfRegistro1[b];
/* 29 */       BigDecimal codEstSimp = new BigDecimal((String)registro.getCampo("CODESTSIMP"));
/* 30 */       String sql = "SELECT ARQUIVO FROM AD_ESTOQUESIMP WHERE CODESTSIMP = " + codEstSimp;
/* 31 */       PreparedStatement pstm = jdbcWrapper.getPreparedStatement(sql);
/* 32 */       ResultSet rs = pstm.executeQuery();
/* 33 */       while (rs.next()) {
/* 34 */         byte[] arquivo = rs.getBytes("ARQUIVO");
/* 35 */         ArrayList<String> linhas = FileReaderService.getLinas(arquivo, 1);
/* 36 */         for (String linha : linhas) {
/*    */           
/* 38 */           String[] campos = linha.split(";");
/*    */           
/* 40 */           DynamicVO simp = (DynamicVO)dwf.getDefaultValueObjectInstance("AD_OPERACAOANP");
/* 41 */           simp.setProperty("TIPO", campos[0]);
/* 42 */           simp.setProperty("FINALIDADE", campos[1]);
/* 43 */           simp.setProperty("CLASSE", campos[2]);
/* 44 */           simp.setProperty("DESCRICAO", campos[3]);
/* 45 */           simp.setProperty("SEQ", campos[4]);
/* 46 */           simp.setProperty("CODOPER_ANP", campos[5]);
/* 47 */           dwf.createEntity("AD_OPERACAOANP", (EntityVO)simp);
/*    */         } 
/*    */       } 
/*    */       b++; }
/*    */   
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\iniciar\importarOperacoesSimp.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */