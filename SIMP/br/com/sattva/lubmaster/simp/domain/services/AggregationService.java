/*    */ package br.com.sattva.lubmaster.simp.domain.services;
/*    */ import br.com.sattva.lubmaster.simp.config.OperationCatalog;
/*    */ import br.com.sattva.lubmaster.simp.config.SimpSettings;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.SimpField;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.SimpRecord;
/*    */ import java.math.BigDecimal;
/*    */ import java.util.Calendar;
/*    */ import java.util.Date;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ public final class AggregationService {
/*    */   private final OperationCatalog catalog;
/*    */   
/*    */   public AggregationService(OperationCatalog catalog, SimpSettings cfg) {
/* 17 */     this.catalog = catalog; this.cfg = cfg;
/*    */   }
/*    */   private final SimpSettings cfg;
/*    */   public List<SimpRecord> buildAggregators(List<SimpRecord> directRecords) {
/* 21 */     Map<String, List<SimpRecord>> byOp = groupByOp(directRecords);
/*    */ 
/*    */     
/* 24 */     Set<String> candidateAggOps = new HashSet<>();
/* 25 */     for (OperationCatalog.OpInfo info : this.catalog.all()) {
/* 26 */       if (this.catalog.isAggregator(info.code)) candidateAggOps.add(info.code);
/*    */     
/*    */     } 
/* 29 */     List<SimpRecord> out = new ArrayList<>();
/* 30 */     for (String aggCode : candidateAggOps) {
/* 31 */       String group = this.catalog.groupOf(aggCode);
/* 32 */       if (group == null) {
/*    */         continue;
/*    */       }
/* 35 */       List<String> sources = this.catalog.nonAggregatorsInGroup(group);
/* 36 */       BigDecimal sumKg = BigDecimal.ZERO;
/* 37 */       boolean hasAny = false;
/*    */       
/* 39 */       for (int i = 0; i < sources.size(); i++) {
/* 40 */         String src = sources.get(i);
/* 41 */         List<SimpRecord> recs = byOp.get(src);
/* 42 */         if (recs != null) {
/* 43 */           for (int j = 0; j < recs.size(); j++) {
/* 44 */             SimpRecord r = recs.get(j);
/* 45 */             Object v = r.get(SimpField.QTD_PRODUTO_KG);
/* 46 */             if (v instanceof BigDecimal) {
/* 47 */               sumKg = sumKg.add((BigDecimal)v);
/* 48 */               hasAny = true;
/*    */             } 
/*    */           } 
/*    */         }
/*    */       } 
/* 53 */       if (hasAny) {
/* 54 */         SimpRecord agg = new SimpRecord(aggCode);
/* 55 */         agg.put(SimpField.CODIGO_OPERACAO, aggCode);
/* 56 */         agg.put(SimpField.MES_REFERENCIA_MMAAAA, monthYear(this.cfg));
/* 57 */         agg.put(SimpField.DATA_NF, endOfMonth(this.cfg));
/* 58 */         agg.put(SimpField.QTD_PRODUTO_KG, sumKg);
/*    */         
/* 60 */         out.add(agg);
/*    */       } 
/*    */     } 
/* 63 */     return out;
/*    */   }
/*    */   
/*    */   private Map<String, List<SimpRecord>> groupByOp(List<SimpRecord> records) {
/* 67 */     Map<String, List<SimpRecord>> map = new HashMap<>();
/* 68 */     for (int i = 0; i < records.size(); i++) {
/* 69 */       SimpRecord r = records.get(i);
/* 70 */       String op = r.opCode();
/* 71 */       List<SimpRecord> list = map.get(op);
/* 72 */       if (list == null) { list = new ArrayList<>(); map.put(op, list); }
/* 73 */        list.add(r);
/*    */     } 
/* 75 */     return map;
/*    */   }
/*    */   
/*    */   private String monthYear(SimpSettings cfg) {
/* 79 */     SimpleDateFormat mmYYYY = new SimpleDateFormat("MMyyyy");
/* 80 */     return mmYYYY.format((cfg.getDtFim() != null) ? cfg.getDtFim() : new Date());
/*    */   }
/*    */   
/*    */   private Date endOfMonth(SimpSettings cfg) {
/* 84 */     Calendar cal = Calendar.getInstance();
/* 85 */     cal.setTime((cfg.getDtFim() != null) ? cfg.getDtFim() : new Date());
/* 86 */     cal.set(5, cal.getActualMaximum(5));
/* 87 */     cal.set(11, 0); cal.set(12, 0); cal.set(13, 0); cal.set(14, 0);
/* 88 */     return cal.getTime();
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\domain\services\AggregationService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */