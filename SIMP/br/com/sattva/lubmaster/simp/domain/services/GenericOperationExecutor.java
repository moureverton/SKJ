/*    */ package br.com.sattva.lubmaster.simp.domain.services;
/*    */ 
/*    */ import br.com.sattva.lubmaster.simp.config.SimpSettings;
/*    */ import br.com.sattva.lubmaster.simp.domain.mapping.MappingRegistry;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.Operation;
/*    */ import br.com.sattva.lubmaster.simp.domain.model.SimpRecord;
/*    */ import br.com.sattva.lubmaster.simp.domain.rules.RuleEngine;
/*    */ import br.com.sattva.lubmaster.simp.infra.db.NativeSqlHelper;
/*    */ import br.com.sattva.lubmaster.simp.infra.db.QueryRepository;
/*    */ import br.com.sattva.lubmaster.simp.infra.db.SqlTemplateEngine;
/*    */ import br.com.sattva.lubmaster.simp.util.DebugLog;
/*    */ import java.sql.ResultSet;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ public final class GenericOperationExecutor
/*    */   implements OperationExecutor
/*    */ {
/*    */   private final QueryRepository queries;
/*    */   private final SqlTemplateEngine templ;
/*    */   private final NativeSqlHelper db;
/*    */   private final MappingRegistry mappings;
/*    */   private final RuleEngine rules;
/*    */   private final DebugLog log;
/*    */   
/*    */   public GenericOperationExecutor(NativeSqlHelper db, QueryRepository queries, SqlTemplateEngine templ, MappingRegistry mappings, RuleEngine rules) {
/* 27 */     this.db = db;
/* 28 */     this.queries = queries;
/* 29 */     this.templ = templ;
/* 30 */     this.mappings = mappings;
/* 31 */     this.rules = rules;
/* 32 */     this.log = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<SimpRecord> execute(final Operation op, SimpSettings cfg) throws Exception {
/* 37 */     if (cfg.isOffline()) {
/* 38 */       List<SimpRecord> mocked = this.mappings.mockRecords(op, cfg);
/* 39 */       this.rules.validate(op, mocked);
/* 40 */       return mocked;
/*    */     } 
/*    */ 
/*    */     
/* 44 */     Map<String, Object> params = this.mappings.defaultParams(cfg);
/* 45 */     String raw = this.queries.load(op);
/* 46 */     String sql = this.templ.render(raw, params);
/*    */     
/* 48 */     final List<SimpRecord> out = this.mappings.emptyList();
/* 49 */     this.db.query(sql, params, new NativeSqlHelper.RowHandler() {
/*    */           public void handle(ResultSet rs) throws Exception {
/* 51 */             out.add(GenericOperationExecutor.this.mappings.mapRow(op, rs));
/*    */           }
/*    */         });
/* 54 */     this.rules.validate(op, out);
/* 55 */     return out;
/*    */   }
/*    */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\domain\services\GenericOperationExecutor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */