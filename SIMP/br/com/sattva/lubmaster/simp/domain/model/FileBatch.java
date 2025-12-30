/*    */ package br.com.sattva.lubmaster.simp.domain.model;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class FileBatch {
/*  7 */   private final List<SimpRecord> records = new ArrayList<>();
/*    */   
/*    */   public void add(SimpRecord r) {
/* 10 */     this.records.add(r);
/*    */   }
/*    */   
/*    */   public void addAll(List<SimpRecord> list) {
/* 14 */     this.records.addAll(list);
/*    */   }
/*    */   
/*    */   public List<SimpRecord> getRecords() {
/* 18 */     return this.records;
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\domain\model\FileBatch.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */