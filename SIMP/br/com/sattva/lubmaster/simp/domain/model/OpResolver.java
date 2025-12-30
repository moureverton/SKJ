/*    */ package br.com.sattva.lubmaster.simp.domain.model;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class OpResolver
/*    */ {
/*    */   public static Operation tryResolve(String opCode) {
/*  8 */     if (opCode == null) return null;  try {
/*    */       byte b; int i;
/*    */       Operation[] arrayOfOperation;
/* 11 */       for (i = (arrayOfOperation = Operation.values()).length, b = 0; b < i; ) { Operation o = arrayOfOperation[b];
/* 12 */         if (opCode.equals(o.code())) return o;  b++; }
/*    */     
/* 14 */     } catch (Throwable throwable) {}
/* 15 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\domain\model\OpResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */