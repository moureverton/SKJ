/*    */ package iniciar;
/*    */ 
/*    */ import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
/*    */ import br.com.sankhya.jape.event.PersistenceEvent;
/*    */ import br.com.sankhya.jape.event.TransactionContext;
/*    */ import processamento.processarIncluirTotalizadores;
/*    */ 
/*    */ 
/*    */ public class totalizadorFinalEvento
/*    */   implements EventoProgramavelJava
/*    */ {
/*    */   public void afterDelete(PersistenceEvent arg0) throws Exception {
/* 13 */     afterInsert(arg0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterInsert(PersistenceEvent arg0) throws Exception {
/* 19 */     processarIncluirTotalizadores.estoqueFnal(arg0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterUpdate(PersistenceEvent arg0) throws Exception {
/* 25 */     afterInsert(arg0);
/*    */   }
/*    */   
/*    */   public void beforeCommit(TransactionContext arg0) throws Exception {}
/*    */   
/*    */   public void beforeDelete(PersistenceEvent arg0) throws Exception {}
/*    */   
/*    */   public void beforeInsert(PersistenceEvent arg0) throws Exception {}
/*    */   
/*    */   public void beforeUpdate(PersistenceEvent arg0) throws Exception {}
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\iniciar\totalizadorFinalEvento.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */