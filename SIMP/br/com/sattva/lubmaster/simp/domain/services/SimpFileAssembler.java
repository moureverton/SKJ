/*    */ package br.com.sattva.lubmaster.simp.domain.services;
/*    */ 
/*    */ import br.com.sattva.lubmaster.simp.config.SimpSettings;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.FieldSpec;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.FileBatch;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.MatrixSpec;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.SimpField;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.SimpRecord;
/*    */ import br.com.sattva.lubmaster.simp.domain.rules.ValueFormatters;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SimpFileAssembler
/*    */ {
/*    */   private final SimpSettings cfg;
/* 19 */   private final ValueFormatters fmt = new ValueFormatters();
/*    */   public SimpFileAssembler(SimpSettings cfg) {
/* 21 */     this.cfg = cfg;
/*    */   }
/*    */   public List<String> toLines(FileBatch batch, MatrixSpec matrix) {
/* 24 */     List<String> lines = new ArrayList<>();
/* 25 */     for (int i = 0; i < batch.getRecords().size(); i++) {
/* 26 */       SimpRecord rec = batch.getRecords().get(i);
/*    */ 
/*    */       
/* 29 */       Map<SimpField, FieldSpec> specs = ValueFormatters.specMapFor(matrix, rec.opCode());
/*    */       
/* 31 */       StringBuilder sb = new StringBuilder();
/* 32 */       for (int j = 0; j < matrix.fieldsOrder.size(); j++) {
/* 33 */         SimpField f = matrix.fieldsOrder.get(j);
/* 34 */         FieldSpec fs = specs.get(f);
/* 35 */         String s = this.fmt.format(fs, rec.get(f));
/* 36 */         sb.append(s);
/*    */       } 
/* 38 */       lines.add(sb.toString());
/*    */     } 
/* 40 */     return lines;
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\domain\services\SimpFileAssembler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */