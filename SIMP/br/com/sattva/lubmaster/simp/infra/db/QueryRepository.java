/*    */ package br.com.sattva.lubmaster.simp.infra.db;
/*    */ 
/*    */ import br.com.sattva.lubmaster.simp.config.ResourceLoader;
/*    */ import br.com.sattva.lubmaster.simp.config.SimpSettings;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.Operation;
/*    */ 
/*    */ public final class QueryRepository {
/*    */   public QueryRepository(SimpSettings cfg) {
/*  9 */     this.loader = new ResourceLoader(cfg);
/*    */   } private final ResourceLoader loader;
/*    */   public String load(Operation op) {
/* 12 */     String path = "./sql/" + op.code() + ".sql";
/* 13 */     String sql = this.loader.readText(path);
/* 14 */     if (sql == null || sql.trim().isEmpty()) {
/* 15 */       throw new IllegalStateException("SQL não encontrado para a operação " + 
/* 16 */           op.code() + ". Esperado em: " + path + 
/* 17 */           " (verifique se o arquivo está no classpath: src/main/resources/sql/" + 
/* 18 */           op.code() + ".sql ou informe caminho absoluto).");
/*    */     }
/* 20 */     return sql;
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\infra\db\QueryRepository.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */