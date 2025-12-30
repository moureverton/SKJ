/*    */ package br.com.sattva.lubmaster.simp.erp.dto;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public final class SimpExportResult {
/*    */   private String outPath;
/*    */   private int totalLinhas;
/*    */   private int bytes;
/*    */   private String persistId;
/*    */   private long elapsedMs;
/*    */   private List<String> ops;
/*    */   
/* 13 */   public String getOutPath() { return this.outPath; } public void setOutPath(String outPath) {
/* 14 */     this.outPath = outPath;
/*    */   }
/* 16 */   public int getTotalLinhas() { return this.totalLinhas; } public void setTotalLinhas(int totalLinhas) {
/* 17 */     this.totalLinhas = totalLinhas;
/*    */   }
/* 19 */   public int getBytes() { return this.bytes; } public void setBytes(int bytes) {
/* 20 */     this.bytes = bytes;
/*    */   }
/* 22 */   public String getPersistId() { return this.persistId; } public void setPersistId(String persistId) {
/* 23 */     this.persistId = persistId;
/*    */   }
/* 25 */   public long getElapsedMs() { return this.elapsedMs; } public void setElapsedMs(long elapsedMs) {
/* 26 */     this.elapsedMs = elapsedMs;
/*    */   }
/* 28 */   public List<String> getOps() { return this.ops; } public void setOps(List<String> ops) {
/* 29 */     this.ops = ops;
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\erp\dto\SimpExportResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */