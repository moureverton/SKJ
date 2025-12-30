/*     */ package br.com.sattva.lubmaster.simp.app;
/*     */ 
/*     */ import br.com.sattva.lubmaster.simp.config.OperationCatalog;
/*     */ import br.com.sattva.lubmaster.simp.config.ResourceLoader;
/*     */ import br.com.sattva.lubmaster.simp.config.SimpSettings;
/*     */ import br.com.sattva.lubmaster.simp.domain.mapping.MappingRegistry;
/*     */ import br.com.sattva.lubmaster.simp.domain.model.FileBatch;
/*     */ import br.com.sattva.lubmaster.simp.domain.model.OpResolver;
/*     */ import br.com.sattva.lubmaster.simp.domain.model.Operation;
/*     */ import br.com.sattva.lubmaster.simp.domain.model.SimpRecord;
/*     */ import br.com.sattva.lubmaster.simp.domain.rules.RuleEngine;
/*     */ import br.com.sattva.lubmaster.simp.domain.services.AggregationService;
/*     */ import br.com.sattva.lubmaster.simp.domain.services.GenericOperationExecutor;
/*     */ import br.com.sattva.lubmaster.simp.domain.services.SimpFileAssembler;
/*     */ import br.com.sattva.lubmaster.simp.infra.db.NativeSqlHelper;
/*     */ import br.com.sattva.lubmaster.simp.infra.db.QueryRepository;
/*     */ import br.com.sattva.lubmaster.simp.infra.db.SqlTemplateEngine;
/*     */ import br.com.sattva.lubmaster.simp.infra.io.FileNameFactory;
/*     */ import br.com.sattva.lubmaster.simp.infra.io.SimpFileWriter;
/*     */ import br.com.sattva.lubmaster.simp.validation.SimpSchemaValidator;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SimpExportRunner
/*     */ {
/*     */   public void runOps(SimpSettings cfg, Operation... ops) throws Exception {
/*  31 */     List<String> codes = new ArrayList<>();
/*  32 */     if (ops != null)
/*  33 */       for (int i = 0; i < ops.length; i++) {
/*  34 */         if (ops[i] != null) codes.add(ops[i].code());
/*     */       
/*     */       }  
/*  37 */     run(cfg, codes);
/*     */   }
/*     */   
/*     */   public void run(SimpSettings cfg, List<String> opCodesOrNull) throws Exception {
/*     */     List<String> allCodes;
/*  42 */     ResourceLoader rl = new ResourceLoader(cfg);
/*  43 */     OperationCatalog catalog = OperationCatalog.load(rl, null);
/*     */     
/*  45 */     RuleEngine rules = new RuleEngine(cfg);
/*  46 */     GenericOperationExecutor exec = buildExecutor(cfg, rules);
/*     */ 
/*     */     
/*  49 */     if (opCodesOrNull != null && !opCodesOrNull.isEmpty()) {
/*  50 */       allCodes = opCodesOrNull;
/*     */     } else {
/*  52 */       allCodes = new ArrayList<>();
/*  53 */       for (OperationCatalog.OpInfo opInfo : catalog.all()) allCodes.add(opInfo.code);
/*     */     
/*     */     } 
/*  56 */     List<String> direct = new ArrayList<>();
/*  57 */     for (int i = 0; i < allCodes.size(); i++) {
/*  58 */       String c = allCodes.get(i);
/*  59 */       if (!catalog.isAggregator(c)) direct.add(c);
/*     */     
/*     */     } 
/*     */     
/*  63 */     FileBatch batch = 
/*  64 */       new FileBatch();
/*     */     
/*  66 */     for (int j = 0; j < direct.size(); j++) {
/*  67 */       String code = direct.get(j);
/*  68 */       Operation op = OpResolver.tryResolve(code);
/*  69 */       if (op == null) {
/*  70 */         if (cfg.isDebug()) System.out.println("[SIMP] Ignorando op não mapeada no enum: " + code);
/*     */       
/*     */       } else {
/*  73 */         List<SimpRecord> recs = exec.execute(op, cfg);
/*  74 */         if (recs != null && !recs.isEmpty()) {
/*  75 */           for (int n = 0; n < recs.size(); ) { batch.add(recs.get(n)); n++; }
/*     */         
/*     */         }
/*     */       } 
/*     */     } 
/*  80 */     AggregationService agg = new AggregationService(catalog, cfg);
/*  81 */     List<SimpRecord> totals = agg.buildAggregators(batch.getRecords());
/*  82 */     for (int k = 0; k < totals.size(); ) { batch.add(totals.get(k)); k++; }
/*     */ 
/*     */     
/*  85 */     List<String> lines = (new SimpFileAssembler(cfg)).toLines(batch, rules.getMatrix());
/*  86 */     SimpSchemaValidator validator = new SimpSchemaValidator(cfg);
/*  87 */     for (int m = 0; m < lines.size(); m++) {
/*  88 */       String op = ((SimpRecord)batch.getRecords().get(m)).opCode();
/*  89 */       validator.validateFixedWidth(rules.getMatrix(), lines.get(m), op);
/*     */     } 
/*  91 */     Path out = (new FileNameFactory(cfg)).buildPath();
/*  92 */     (new SimpFileWriter()).write(out, lines, Charset.forName(cfg.getCharset()));
/*     */   }
/*     */ 
/*     */   
/*     */   private GenericOperationExecutor buildExecutor(SimpSettings cfg, RuleEngine rules) {
/*  97 */     return new GenericOperationExecutor(
/*  98 */         new NativeSqlHelper(cfg), 
/*  99 */         new QueryRepository(cfg), 
/* 100 */         new SqlTemplateEngine(), 
/* 101 */         new MappingRegistry(cfg), 
/* 102 */         rules);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/* 107 */     SimpSettings cfg = SimpSettings.fromArgs(args);
/* 108 */     (new SimpExportRunner()).runOps(cfg, new Operation[] { Operation.OP_1011001 });
/*     */   }
/*     */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\app\SimpExportRunner.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */