/*    */ package br.com.sattva.lubmaster.simp.util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public final class DebugLog {
/*    */   private final boolean enabled;
/*  9 */   private final List<String> lines = new ArrayList<>();
/* 10 */   private static final Logger LOG = Logger.getLogger("SIMP");
/*    */   public DebugLog(boolean enabled) {
/* 12 */     this.enabled = enabled;
/*    */   }
/*    */   public void log(String fmt, Object... args) {
/* 15 */     if (!this.enabled)
/* 16 */       return;  String msg = String.format(fmt, args);
/* 17 */     this.lines.add(msg);
/* 18 */     LOG.info(msg);
/*    */   }
/*    */   public boolean isEnabled() {
/* 21 */     return this.enabled;
/*    */   }
/*    */   public String dump() {
/* 24 */     StringBuilder sb = new StringBuilder();
/* 25 */     for (int i = 0; i < this.lines.size(); i++) {
/* 26 */       sb.append(this.lines.get(i)).append('\n');
/*    */     }
/* 28 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\sim\\util\DebugLog.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */