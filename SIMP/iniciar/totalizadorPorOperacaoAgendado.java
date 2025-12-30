/*    */ package iniciar;
/*    */ 
/*    */ import org.cuckoo.core.ScheduledAction;
/*    */ import org.cuckoo.core.ScheduledActionContext;
/*    */ import processamento.processarIncluirTotalizadores;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class totalizadorPorOperacaoAgendado
/*    */   implements ScheduledAction
/*    */ {
/*    */   public void onTime(ScheduledActionContext arg0) {
/*    */     try {
/* 14 */       processarIncluirTotalizadores.totalizadorPorOperacaoAgendado();
/* 15 */     } catch (Exception e) {
/*    */       
/* 17 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\iniciar\totalizadorPorOperacaoAgendado.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */