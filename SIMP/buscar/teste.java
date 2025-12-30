/*    */ package buscar;
/*    */ 
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import processamento.processarExportarArquivo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class teste
/*    */ {
/*    */   public static void main(String[] args) {
/* 12 */     String vlrUnit = "15,15580";
/* 13 */     String vlrunitinteiro = "0";
/* 14 */     String vlrunitdecimal = "0";
/* 15 */     if (vlrUnit.equalsIgnoreCase("0.0")) {
/* 16 */       vlrunitinteiro = "0";
/*    */     } else {
/*    */       
/* 19 */       String[] valores = vlrUnit.split("\\.");
/* 20 */       System.out.println(valores[0]);
/* 21 */       if (valores.length > 0) {
/* 22 */         vlrunitinteiro = valores[0];
/* 23 */         vlrunitdecimal = valores[1];
/*    */       } else {
/* 25 */         vlrunitinteiro = vlrUnit;
/*    */       } 
/* 27 */       System.out.println(vlrunitinteiro);
/* 28 */       System.out.println(vlrunitdecimal);
/*    */       
/* 30 */       vlrunitinteiro = vlrunitinteiro.replace(".", "");
/* 31 */       vlrunitdecimal = vlrunitdecimal.replace(".", "");
/*    */       
/* 33 */       vlrunitinteiro = processarExportarArquivo.limitaString(StringUtils.leftPad(vlrunitinteiro, 3, "0"), 3);
/* 34 */       vlrunitdecimal = processarExportarArquivo.limitaString(StringUtils.rightPad(vlrunitdecimal, 4, "0"), 4);
/* 35 */       System.out.println(processarExportarArquivo.geraLinha(String.valueOf(vlrunitinteiro) + vlrunitdecimal, 7));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\buscar\teste.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */