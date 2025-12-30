/*    */ package iniciar;
/*    */ 
/*    */ import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
/*    */ import br.com.sankhya.jape.event.PersistenceEvent;
/*    */ import br.com.sankhya.jape.event.TransactionContext;
/*    */ import processamento.processarIncluirTotalizadores;
/*    */ 
/*    */ public class totalizadorGeralEvento
/*    */   implements EventoProgramavelJava {
/*    */   public void afterDelete(PersistenceEvent arg0) throws Exception {
/* 11 */     afterInsert(arg0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterInsert(PersistenceEvent arg0) throws Exception {
/* 17 */     processarIncluirTotalizadores.totalizadoresGerais(arg0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterUpdate(PersistenceEvent arg0) throws Exception {
/* 23 */     afterInsert(arg0);
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


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\iniciar\totalizadorGeralEvento.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */