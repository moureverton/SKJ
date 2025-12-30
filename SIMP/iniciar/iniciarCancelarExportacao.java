/*    */ package iniciar;
/*    */ 
/*    */ import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
/*    */ import br.com.sankhya.extensions.actionbutton.ContextoAcao;
/*    */ import processamento.processarCancelarExportacao;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class iniciarCancelarExportacao
/*    */   implements AcaoRotinaJava
/*    */ {
/*    */   public void doAction(ContextoAcao arg0) throws Exception {
/* 16 */     processarCancelarExportacao.processar(arg0);
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\iniciar\iniciarCancelarExportacao.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */