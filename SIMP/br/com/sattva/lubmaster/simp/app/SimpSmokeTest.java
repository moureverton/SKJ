/*    */ package br.com.sattva.lubmaster.simp.app;
/*    */ 
/*    */ import br.com.sattva.lubmaster.simp.config.SimpSettings;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.Operation;
/*    */ import java.util.Arrays;
/*    */ import java.util.Calendar;
/*    */ import java.util.Date;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public final class SimpSmokeTest
/*    */ {
/*    */   public static void main(String[] args) throws Exception {
/* 14 */     Date fim = new Date();
/* 15 */     Calendar cal = Calendar.getInstance();
/* 16 */     cal.setTime(fim);
/* 17 */     cal.add(5, -7);
/* 18 */     Date ini = cal.getTime();
/*    */     
/* 20 */     SimpSettings cfg = (new SimpSettings.Builder())
/* 21 */       .codEmp(1)
/* 22 */       .period(ini, fim)
/* 23 */       .outDir("C:/tmp")
/* 24 */       .charset("ISO-8859-1")
/* 25 */       .debug(true)
/* 26 */       .matrixVersion("v1_11")
/* 27 */       .offline(true)
/* 28 */       .build();
/*    */     
/* 30 */     List<Operation> ops = Arrays.asList(new Operation[] {
/* 31 */           Operation.OP_1011001
/*    */         });
/*    */ 
/*    */     
/* 35 */     (new SimpExportRunner()).runOps(cfg, ops.<Operation>toArray(new Operation[ops.size()]));
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\app\SimpSmokeTest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */