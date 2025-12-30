/*    */ package br.com.sattva.lubmaster.simp.domain.rules;
/*    */ 
/*    */ import br.com.sattva.lubmaster.simp.config.ResourceLoader;
/*    */ import br.com.sattva.lubmaster.simp.config.SimpSettings;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.FieldRule;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.FieldSpec;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.MatrixSpec;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.Operation;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.SimpRecord;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class RuleEngine {
/*    */   private final MatrixSpec matrix;
/*    */   
/*    */   public RuleEngine(SimpSettings cfg) {
/* 16 */     ResourceLoader rl = new ResourceLoader(cfg);
/* 17 */     this.matrix = rl.readMatrix("/config/matrix/" + cfg.getMatrixVersion() + ".json");
/*    */   }
/*    */   public MatrixSpec getMatrix() {
/* 20 */     return this.matrix;
/*    */   }
/*    */   public void validate(Operation op, List<SimpRecord> list) {
/* 23 */     List<FieldSpec> specs = specsFor(op.code());
/* 24 */     if (specs == null)
/* 25 */       return;  for (int i = 0; i < list.size(); i++) {
/* 26 */       SimpRecord r = list.get(i);
/* 27 */       for (int j = 0; j < specs.size(); j++) {
/* 28 */         FieldSpec fs = specs.get(j);
/* 29 */         if (fs.rule == FieldRule.OBRIGATORIO && r.get(fs.field) == null) {
/* 30 */           throw new IllegalStateException("Campo obrigatório vazio: " + fs.field + " op=" + op.code());
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public List<FieldSpec> specsFor(String opCode) {
/* 37 */     List<FieldSpec> specs = (List<FieldSpec>)this.matrix.rulesByOperation.get(opCode);
/* 38 */     if (specs == null || specs.isEmpty()) {
/* 39 */       specs = (List<FieldSpec>)this.matrix.rulesByOperation.get("DEFAULT");
/*    */     }
/*    */ 
/*    */     
/* 43 */     if (specs != null && isInherit(specs)) {
/* 44 */       String parent = parentName(specs);
/* 45 */       List<FieldSpec> p = (List<FieldSpec>)this.matrix.rulesByOperation.get(parent);
/* 46 */       if (p != null) return p; 
/*    */     } 
/* 48 */     return specs;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean isInherit(List<FieldSpec> specs) {
/* 55 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private String parentName(List<FieldSpec> specs) {
/* 61 */     return "DEFAULT";
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\domain\rules\RuleEngine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */