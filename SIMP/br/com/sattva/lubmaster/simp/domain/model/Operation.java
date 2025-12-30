/*   */ package br.com.sattva.lubmaster.simp.domain.model;public enum Operation {
/*   */   private final String description;
/* 3 */   OP_1011001("1011001", "COMPRA DE AGENTE REGULADO"); private final String code;
/*   */   Operation(String c, String d) {
/* 5 */     this.code = c; this.description = d; }
/* 6 */   public String code() { return this.code; } public String description() {
/* 7 */     return this.description;
/*   */   }
/*   */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\domain\model\Operation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */