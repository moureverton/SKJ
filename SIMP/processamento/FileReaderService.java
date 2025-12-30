/*    */ package processamento;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileReaderService
/*    */ {
/*    */   public static ArrayList<String> getLinas(byte[] csv, int qtdLinhasPular) throws Exception {
/* 12 */     ArrayList<String> linhas = new ArrayList<>();
/*    */     
/* 14 */     ByteArrayInputStream bais = new ByteArrayInputStream(csv);
/*    */     
/* 16 */     Exception exception1 = null, exception2 = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/*    */     
/*    */     } finally {
/* 35 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*    */     
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\processamento\FileReaderService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */