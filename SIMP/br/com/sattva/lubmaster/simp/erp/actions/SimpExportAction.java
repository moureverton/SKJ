/*    */ package br.com.sattva.lubmaster.simp.erp.actions;
/*    */ 
/*    */ import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
/*    */ import br.com.sankhya.extensions.actionbutton.ContextoAcao;
/*    */ import br.com.sattva.lubmaster.simp.erp.service.SimpExportServiceOnline;
/*    */ import java.math.BigDecimal;
/*    */ import java.sql.Timestamp;
/*    */ import java.text.ParseException;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Calendar;
/*    */ import java.util.Date;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpExportAction
/*    */   implements AcaoRotinaJava
/*    */ {
/*    */   private static final String DF_UI = "dd/MM/yyyy";
/*    */   private static final String DF_REF = "MM/yyyy";
/*    */   
/*    */   public void doAction(ContextoAcao ctx) throws Exception {
/* 24 */     long t0 = System.currentTimeMillis();
/*    */     
/*    */     try {
/* 27 */       int codEmp = parseInt((String)ctx.getParam("CODEMP"), 1);
/* 28 */       Timestamp referencia = (Timestamp)ctx.getParam("REFERENCIA");
/* 29 */       String ref = formatMonthYear(referencia);
/* 30 */       Date[] range = resolvePeriodo(ref.trim());
/* 31 */       Date dtIni = range[0];
/* 32 */       Date dtFim = range[1];
/* 33 */       List<String> opCodes = new ArrayList<>();
/* 34 */       BigDecimal codSimSimp = (BigDecimal)ctx.getLinhas()[0].getCampo("CODSIMSIM");
/* 35 */       SimpExportServiceOnline svc = new SimpExportServiceOnline();
/* 36 */       svc.executar(ctx, codEmp, dtIni, dtFim, opCodes, null, false, true, codSimSimp);
/* 37 */       long ms = System.currentTimeMillis() - t0;
/* 38 */       StringBuilder sb = new StringBuilder();
/* 39 */       sb.append("Exportação SIMP concluída com sucesso.\n");
/* 40 */       sb.append("Empresa: ").append(codEmp).append("\n");
/* 41 */       sb.append("Período: ").append(fmt(dtIni)).append(" a ").append(fmt(dtFim)).append("\n");
/* 42 */       sb.append("Operações: carregadas de AD_OPERACAOANP (ATIVA='S').\n");
/* 43 */       sb.append("Tempo total: ").append(ms).append(" ms");
/* 44 */       ctx.setMensagemRetorno(sb.toString());
/* 45 */     } catch (Exception var17) {
/* 46 */       var17.printStackTrace();
/* 47 */       ctx.mostraErro("Falha na exportação SIMP: " + var17.getMessage());
/* 48 */       throw var17;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static String formatMonthYear(Timestamp timestamp) {
/* 53 */     if (timestamp == null) {
/* 54 */       return null;
/*    */     }
/* 56 */     SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
/* 57 */     return sdf.format(timestamp);
/*    */   }
/*    */ 
/*    */   
/*    */   private static int parseInt(String s, int def) {
/*    */     try {
/* 63 */       String v = (s != null) ? s.trim() : "";
/* 64 */       return v.isEmpty() ? def : (new BigDecimal(v)).intValue();
/* 65 */     } catch (Exception var3) {
/* 66 */       return def;
/*    */     } 
/*    */   }
/*    */   
/*    */   private static Date[] resolvePeriodo(String refMmYyyy) throws ParseException {
/* 71 */     SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
/* 72 */     sdf.setLenient(false);
/* 73 */     Date base = sdf.parse(refMmYyyy);
/* 74 */     Calendar c = Calendar.getInstance();
/* 75 */     c.setTime(base);
/* 76 */     c.set(5, 1);
/* 77 */     c.set(11, 0);
/* 78 */     c.set(12, 0);
/* 79 */     c.set(13, 0);
/* 80 */     c.set(14, 0);
/* 81 */     Date ini = c.getTime();
/* 82 */     c.set(5, c.getActualMaximum(5));
/* 83 */     c.set(11, 23);
/* 84 */     c.set(12, 59);
/* 85 */     c.set(13, 59);
/* 86 */     c.set(14, 999);
/* 87 */     Date fim = c.getTime();
/* 88 */     return new Date[] { ini, fim };
/*    */   }
/*    */   
/*    */   private static String fmt(Date d) {
/* 92 */     return (new SimpleDateFormat("dd/MM/yyyy")).format(d);
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\erp\actions\SimpExportAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */