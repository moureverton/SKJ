/*    */ package br.com.sattva.simp.actions;
/*    */ 
/*    */ import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
/*    */ import br.com.sankhya.extensions.actionbutton.ContextoAcao;
/*    */ import br.com.sattva.simp.service.ActExportarArquivoService;
/*    */ 
/*    */ public class ActExportarArquivo
/*    */   implements AcaoRotinaJava
/*    */ {
/*    */   public void doAction(ContextoAcao ctx) throws Exception {
/*    */     try {
/* 12 */       ActExportarArquivoService simp = new ActExportarArquivoService();
/* 13 */       ActExportarArquivoService.processar(ctx);
/*    */ 
/*    */ 
/*    */     
/*    */     }
/* 18 */     catch (Throwable t) {
/*    */       
/* 20 */       Throwable root = t;
/* 21 */       while (root.getCause() != null && root.getCause() != root) {
/* 22 */         root = root.getCause();
/*    */       }
/*    */ 
/*    */       
/* 26 */       String msg = "Falha na exportação SIMP: " + ((root.getMessage() != null) ? root.getMessage() : root.toString());
/*    */ 
/*    */       
/*    */       try {
/* 30 */         ctx.mostraErro(msg);
/* 31 */       } catch (Throwable ignore) {
/*    */         
/* 33 */         System.err.println("[SIMP] " + msg);
/*    */       } 
/*    */ 
/*    */       
/* 37 */       t.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMPSATTVA.jar!\br\com\sattva\simp\actions\ActExportarArquivo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */