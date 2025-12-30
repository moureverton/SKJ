/*    */ package br.com.sattva.lubmaster.simp.infra.io;
/*    */ 
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class SimpFileWriter {
/*    */   public void write(Path p, List<String> lines, Charset cs) throws Exception {
/* 12 */     BufferedWriter bw = null;
/*    */     try {
/* 14 */       bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(p.toFile()), cs));
/* 15 */       for (int i = 0; i < lines.size(); i++) {
/* 16 */         bw.write(lines.get(i));
/* 17 */         bw.newLine();
/*    */       } 
/*    */     } finally {
/* 20 */       if (bw != null) try { bw.close(); } catch (Exception exception) {} 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\infra\io\SimpFileWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */