/*    */ package br.com.sattva.lubmaster.simp.validation;
/*    */ 
/*    */ import br.com.sattva.lubmaster.simp.config.SimpSettings;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.FieldRule;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.FieldSpec;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.FileBatch;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.MatrixSpec;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.SimpField;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.SimpRecord;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ public final class SimpSchemaValidator
/*    */ {
/*    */   public SimpSchemaValidator(SimpSettings cfg) {
/* 17 */     this.cfg = cfg;
/*    */   }
/*    */   
/*    */   private final SimpSettings cfg;
/*    */   
/*    */   public void validate(FileBatch batch) {
/* 23 */     if (batch == null || batch.getRecords().isEmpty())
/*    */       return; 
/*    */   }
/*    */   
/*    */   public void validateRecord(MatrixSpec matrix, SimpRecord rec) {
/* 28 */     List<FieldSpec> specs = (List<FieldSpec>)matrix.rulesByOperation.get(rec.opCode());
/* 29 */     if (specs == null) {
/* 30 */       throw new IllegalStateException("Sem spec para operação " + rec.opCode() + 
/* 31 */           " (versão matriz=" + matrix.version + ")");
/*    */     }
/* 33 */     Map<SimpField, FieldSpec> specMap = toMap(specs);
/* 34 */     for (int i = 0; i < specs.size(); i++) {
/* 35 */       FieldSpec fs = specs.get(i);
/* 36 */       Object val = rec.get(fs.field);
/* 37 */       if (fs.rule == FieldRule.OBRIGATORIO && val == null) {
/* 38 */         throw new IllegalStateException("Campo obrigatório vazio: " + fs.field + 
/* 39 */             " op=" + rec.opCode());
/*    */       }
/*    */       
/* 42 */       if (val instanceof String && ((String)val).length() > fs.length) {
/* 43 */         throw new IllegalStateException("Campo excedeu comprimento: " + fs.field + 
/* 44 */             " op=" + rec.opCode() + " len=" + ((String)val).length() + 
/* 45 */             " max=" + fs.length);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private Map<SimpField, FieldSpec> toMap(List<FieldSpec> specs) {
/* 51 */     Map<SimpField, FieldSpec> map = new HashMap<>();
/* 52 */     for (int i = 0; i < specs.size(); i++) {
/* 53 */       FieldSpec fs = specs.get(i);
/* 54 */       map.put(fs.field, fs);
/*    */     } 
/* 56 */     return map;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validateFixedWidth(MatrixSpec matrix, String line, String opCode) {
/* 61 */     List<SimpField> order = matrix.fieldsOrder;
/* 62 */     List<FieldSpec> specs = (List<FieldSpec>)matrix.rulesByOperation.get(opCode);
/* 63 */     if (specs == null)
/*    */       return; 
/* 65 */     int pos = 0;
/* 66 */     for (int i = 0; i < order.size(); i++) {
/* 67 */       SimpField f = order.get(i);
/* 68 */       FieldSpec fs = find(specs, f);
/* 69 */       if (fs != null) {
/* 70 */         int end = pos + fs.length;
/* 71 */         if (end > line.length()) {
/* 72 */           throw new IllegalStateException("Linha curta: campo " + f + " op=" + opCode + 
/* 73 */               " esperado len=" + fs.length + " pos=" + pos + ".." + (end - 1) + " mas linha=" + line.length());
/*    */         }
/* 75 */         String slice = line.substring(pos, end);
/*    */ 
/*    */         
/* 78 */         if ("N".equals(fs.type)) {
/*    */           
/* 80 */           if (!slice.matches("[0-9]+")) {
/* 81 */             throw new IllegalStateException("Campo numérico inválido (deve conter só dígitos): " + f + " valor='" + slice + "'");
/*    */           }
/* 83 */         } else if ("D".equals(fs.type)) {
/*    */           
/* 85 */           if (!slice.matches("[0-9]{8}")) {
/* 86 */             throw new IllegalStateException("Campo data inválido: " + f + " valor='" + slice + "' (esperado ddMMyyyy)");
/*    */           }
/*    */         } 
/* 89 */         pos = end;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   private FieldSpec find(List<FieldSpec> specs, SimpField f) {
/* 94 */     for (int i = 0; i < specs.size(); i++) {
/* 95 */       if (((FieldSpec)specs.get(i)).field == f) return specs.get(i); 
/*    */     } 
/* 97 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\validation\SimpSchemaValidator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */