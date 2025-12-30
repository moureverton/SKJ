/*     */ package br.com.sattva.lubmaster.simp.erp.service;
/*     */ 
/*     */ import br.com.sankhya.extensions.actionbutton.ContextoAcao;
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
/*     */ import br.com.sattva.lubmaster.simp.erp.dto.SimpExportResult;
/*     */ import br.com.sattva.lubmaster.simp.erp.store.SimpFileStore;
/*     */ import br.com.sattva.lubmaster.simp.infra.db.NativeSqlHelper;
/*     */ import br.com.sattva.lubmaster.simp.infra.db.QueryRepository;
/*     */ import br.com.sattva.lubmaster.simp.infra.db.SqlTemplateEngine;
/*     */ import br.com.sattva.lubmaster.simp.infra.io.FileNameFactory;
/*     */ import br.com.sattva.lubmaster.simp.infra.io.SimpFileWriter;
/*     */ import br.com.sattva.lubmaster.simp.validation.SimpSchemaValidator;
/*     */ import java.math.BigDecimal;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SimpExportService
/*     */ {
/*     */   public SimpExportResult executar(ContextoAcao ctx, int codEmp, Date dtIni, Date dtFim, List<String> opCodes, String charset, boolean somenteCSV, boolean gravarNoBanco, BigDecimal codSimSimp) throws Exception {
/*  48 */     long t0 = System.currentTimeMillis();
/*     */     
/*  50 */     SimpSettings cfg = (new SimpSettings.Builder())
/*  51 */       .codEmp(codEmp)
/*  52 */       .period(dtIni, dtFim)
/*  53 */       .outDir(System.getProperty("java.io.tmpdir"))
/*  54 */       .charset((charset != null) ? charset : "ISO-8859-1")
/*  55 */       .debug(false)
/*  56 */       .matrixVersion("v1_11")
/*  57 */       .offline(false)
/*  58 */       .build();
/*     */     
/*  60 */     ResourceLoader rl = new ResourceLoader(cfg);
/*  61 */     OperationCatalog catalog = OperationCatalog.load(rl, null);
/*  62 */     RuleEngine rules = new RuleEngine(cfg);
/*     */     
/*  64 */     GenericOperationExecutor exec = new GenericOperationExecutor(
/*  65 */         new NativeSqlHelper(cfg), 
/*  66 */         new QueryRepository(cfg), 
/*  67 */         new SqlTemplateEngine(), 
/*  68 */         new MappingRegistry(cfg), 
/*  69 */         rules);
/*     */ 
/*     */     
/*  72 */     List<String> allCodes = (opCodes != null && !opCodes.isEmpty()) ? 
/*  73 */       opCodes : 
/*  74 */       new ArrayList<>();
/*  75 */     if (allCodes.isEmpty()) {
/*  76 */       Collection<OperationCatalog.OpInfo> infos = catalog.all();
/*  77 */       Iterator<OperationCatalog.OpInfo> it = infos.iterator();
/*  78 */       while (it.hasNext()) {
/*  79 */         OperationCatalog.OpInfo opInfo = it.next();
/*  80 */         allCodes.add(opInfo.code);
/*     */       } 
/*     */     } 
/*  83 */     List<String> direct = new ArrayList<>();
/*  84 */     for (int i = 0; i < allCodes.size(); i++) {
/*  85 */       String c = allCodes.get(i);
/*  86 */       if (!catalog.isAggregator(c)) direct.add(c);
/*     */     
/*     */     } 
/*  89 */     FileBatch batch = new FileBatch();
/*  90 */     for (int j = 0; j < direct.size(); j++) {
/*  91 */       String code = direct.get(j);
/*  92 */       Operation op = OpResolver.tryResolve(code);
/*  93 */       if (op != null) {
/*  94 */         List<SimpRecord> recs = exec.execute(op, cfg);
/*  95 */         if (recs != null && !recs.isEmpty())
/*  96 */           for (int n = 0; n < recs.size(); ) { batch.add(recs.get(n)); n++; }
/*     */            
/*     */       } 
/*     */     } 
/* 100 */     AggregationService agg = new AggregationService(catalog, cfg);
/* 101 */     List<SimpRecord> totals = agg.buildAggregators(batch.getRecords());
/* 102 */     for (int k = 0; k < totals.size(); ) { batch.add(totals.get(k)); k++; }
/*     */     
/* 104 */     List<String> lines = (new SimpFileAssembler(cfg)).toLines(batch, rules.getMatrix());
/* 105 */     SimpSchemaValidator validator = new SimpSchemaValidator(cfg);
/* 106 */     for (int m = 0; m < lines.size(); m++) {
/* 107 */       String opCode = ((SimpRecord)batch.getRecords().get(m)).opCode();
/* 108 */       validator.validateFixedWidth(rules.getMatrix(), lines.get(m), opCode);
/*     */     } 
/*     */     
/* 111 */     Path out = (new FileNameFactory(cfg)).buildPath();
/* 112 */     (new SimpFileWriter()).write(out, lines, Charset.forName(cfg.getCharset()));
/*     */     
/* 114 */     String persistId = null;
/* 115 */     if (gravarNoBanco) {
/*     */       
/* 117 */       Calendar cal = Calendar.getInstance();
/* 118 */       cal.setTime((dtFim != null) ? dtFim : new Date());
/* 119 */       String mesReferente = String.valueOf(cal.get(2) + 1);
/* 120 */       BigDecimal anoReferente = new BigDecimal(cal.get(1));
/*     */       
/* 122 */       byte[] data = joinWithNewline(lines, cfg.getCharset());
/*     */ 
/*     */       
/* 125 */       persistId = (new SimpFileStore()).persistViaLegado(
/* 126 */           ctx, 
/* 127 */           out.getFileName().toString(), 
/* 128 */           data, 
/* 129 */           "text/plain", 
/* 130 */           codSimSimp, 
/* 131 */           mesReferente, 
/* 132 */           anoReferente);
/*     */     } 
/*     */ 
/*     */     
/* 136 */     long ms = System.currentTimeMillis() - t0;
/* 137 */     SimpExportResult r = new SimpExportResult();
/* 138 */     r.setOutPath(out.toString());
/* 139 */     r.setTotalLinhas(lines.size());
/* 140 */     r.setBytes((int)Files.size(out));
/* 141 */     r.setPersistId(persistId);
/* 142 */     r.setElapsedMs(ms);
/* 143 */     r.setOps(direct);
/* 144 */     return r;
/*     */   }
/*     */   
/*     */   private static byte[] joinWithNewline(List<String> lines, String charset) throws Exception {
/* 148 */     StringBuilder sb = new StringBuilder();
/* 149 */     for (int i = 0; i < lines.size(); i++) {
/* 150 */       if (i > 0) sb.append('\n'); 
/* 151 */       sb.append(lines.get(i));
/*     */     } 
/* 153 */     return sb.toString().getBytes((charset != null) ? charset : "ISO-8859-1");
/*     */   }
/*     */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\erp\service\SimpExportService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */