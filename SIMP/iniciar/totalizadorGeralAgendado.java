/*    */ package iniciar;
/*    */ 
/*    */ import org.cuckoo.core.ScheduledAction;
/*    */ import org.cuckoo.core.ScheduledActionContext;
/*    */ import processamento.processarIncluirTotalizadores;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class totalizadorGeralAgendado
/*    */   implements ScheduledAction
/*    */ {
/*    */   public void onTime(ScheduledActionContext arg0) {
/*    */     try {
/* 14 */       processarIncluirTotalizadores.totalizadoresGeraisAgendado();
/* 15 */     } catch (Exception e) {
/*    */       
/* 17 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\iniciar\totalizadorGeralAgendado.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */