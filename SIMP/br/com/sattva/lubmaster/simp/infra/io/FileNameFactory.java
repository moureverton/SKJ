/*    */ package br.com.sattva.lubmaster.simp.infra.io;
/*    */ import br.com.sattva.lubmaster.simp.config.SimpSettings;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ 
/*    */ public final class FileNameFactory {
/*    */   private final SimpSettings cfg;
/*    */   
/*    */   public FileNameFactory(SimpSettings cfg) {
/* 12 */     this.cfg = cfg;
/*    */   } public Path buildPath() {
/* 14 */     String ts = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date());
/* 15 */     String name = "SIMP_" + this.cfg.getCodEmp() + "_" + ts + ".txt";
/* 16 */     return Paths.get(this.cfg.getOutDir(), new String[] { name });
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\infra\io\FileNameFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */