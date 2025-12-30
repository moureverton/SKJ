/*     */ package processamento;
/*     */ 
/*     */ import br.com.sankhya.jape.EntityFacade;
/*     */ import br.com.sankhya.jape.dao.JdbcWrapper;
/*     */ import br.com.sankhya.jape.event.PersistenceEvent;
/*     */ import br.com.sankhya.jape.vo.DynamicVO;
/*     */ import br.com.sankhya.modelcore.util.EntityFacadeFactory;
/*     */ import buscar.buscarEstoque;
/*     */ import buscar.buscarTotalizadores;
/*     */ import java.math.BigDecimal;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class processarIncluirTotalizadores
/*     */ {
/*     */   public static void totalizadorPorOperacao(PersistenceEvent arg0) throws Exception {
/*  64 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/*  65 */     JdbcWrapper jdbcWrapper = dwf.getJdbcWrapper();
/*  66 */     jdbcWrapper.openSession();
/*     */     
/*  68 */     DynamicVO dados = (DynamicVO)arg0.getVo();
/*     */     
/*  70 */     BigDecimal codSimSimp = dados.asBigDecimalOrZero("CODSIMSIM");
/*  71 */     String codProdAnp = dados.asString("CODPROD_ANP");
/*  72 */     String codEmpAnp = dados.asString("CODEMP_ANP");
/*  73 */     String mesReferencia = dados.asString("MESREFERENCIA");
/*  74 */     String instalacao = dados.asString("CODINSTALACAO1");
/*     */     
/*  76 */     ResultSet query = buscarTotalizadores.totalizadorPorOperacao(jdbcWrapper, codSimSimp, codEmpAnp, mesReferencia, 
/*  77 */         instalacao, codProdAnp);
/*  78 */     if (query.next()) {
/*  79 */       BigDecimal operTotalizador = query.getBigDecimal("OPER_TOT");
/*  80 */       String qtdUnPadrao = query.getString("QTD_PROD_UN_ANP");
/*  81 */       String qtdKg = query.getString("QTD_PROD_OPERADO_KG");
/*  82 */       String VLR_PROD_UN_ANP = query.getString("VLR_PROD_UN_ANP");
/*     */ 
/*     */       
/*  85 */       ResultSet validacao = buscarTotalizadores.validarcaoTotalizadorExiste(jdbcWrapper, codSimSimp, codEmpAnp, 
/*  86 */           mesReferencia, instalacao, codProdAnp, operTotalizador);
/*     */       
/*  88 */       if (validacao.next()) {
/*  89 */         String update = "UPDATE AD_DETSIMULARSIMP SET VLRUNIT_NF='" + VLR_PROD_UN_ANP + "',QTD_PROD_UN_ANP = '" + qtdUnPadrao + 
/*  90 */           "',QTD_PROD_OPERADO_KG = '" + qtdKg + "' " + "WHERE CODSIMSIM = " + codSimSimp + "\r\n" + 
/*  91 */           "AND CODEMP_ANP = '" + codEmpAnp + "'\r\n" + "AND MESREFERENCIA = '" + mesReferencia + "'\r\n" + 
/*  92 */           "AND CODINSTALACAO1 = '" + instalacao + "'\r\n" + "AND CODPROD_ANP = '" + codProdAnp + "'\r\n" + 
/*  93 */           "AND CODOPER = " + operTotalizador;
/*  94 */         buscarEstoque.update(jdbcWrapper, update);
/*     */       
/*     */       }
/*  97 */       else if (operTotalizador.intValue() > 0) {
/*  98 */         saveAD_DETSIMULARSIMP(jdbcWrapper, codSimSimp, codEmpAnp, mesReferencia, instalacao, codProdAnp, 
/*  99 */             qtdUnPadrao, qtdKg, operTotalizador, VLR_PROD_UN_ANP);
/*     */       } 
/*     */       
/* 102 */       validacao.close();
/*     */     } 
/* 104 */     query.close();
/* 105 */     jdbcWrapper.closeSession();
/*     */   }
/*     */   
/*     */   public static void totalizadorPorOperacaoAgendado() throws Exception {
/* 109 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/* 110 */     JdbcWrapper jdbcWrapper = dwf.getJdbcWrapper();
/* 111 */     jdbcWrapper.openSession();
/*     */     
/* 113 */     ResultSet query = buscarTotalizadores.totalizadorPorOperacaoAgendado(jdbcWrapper);
/* 114 */     while (query.next()) {
/* 115 */       BigDecimal codSimSimp = query.getBigDecimal("CODSIMSIM");
/* 116 */       String codEmpAnp = query.getString("CODEMP_ANP");
/* 117 */       String mesReferencia = query.getString("MESREFERENCIA");
/* 118 */       String instalacao = query.getString("CODINSTALACAO1");
/* 119 */       String codProdAnp = query.getString("CODPROD_ANP");
/* 120 */       BigDecimal operTotalizador = query.getBigDecimal("OPER_TOT");
/* 121 */       String qtdUnPadrao = query.getString("QTD_PROD_UN_ANP");
/* 122 */       String VLR_PROD_UN_ANP = query.getString("VLR_PROD_UN_ANP");
/* 123 */       String qtdKg = query.getString("QTD_PROD_OPERADO_KG");
/*     */       
/* 125 */       ResultSet validacao = buscarTotalizadores.validarcaoTotalizadorExiste(jdbcWrapper, codSimSimp, codEmpAnp, 
/* 126 */           mesReferencia, instalacao, codProdAnp, operTotalizador);
/*     */       
/* 128 */       if (validacao.next()) {
/* 129 */         String update = "UPDATE AD_DETSIMULARSIMP SET VLRUNIT_NF='" + VLR_PROD_UN_ANP + "',QTD_PROD_UN_ANP = '" + qtdUnPadrao + 
/* 130 */           "',QTD_PROD_OPERADO_KG = '" + qtdKg + "' " + "WHERE CODSIMSIM = " + codSimSimp + "\r\n" + 
/* 131 */           "AND CODEMP_ANP = '" + codEmpAnp + "'\r\n" + "AND MESREFERENCIA = '" + mesReferencia + "'\r\n" + 
/* 132 */           "AND CODINSTALACAO1 = '" + instalacao + "'\r\n" + "AND CODPROD_ANP = '" + codProdAnp + "'\r\n" + 
/* 133 */           "AND CODOPER = " + operTotalizador;
/* 134 */         buscarEstoque.update(jdbcWrapper, update);
/*     */       
/*     */       }
/* 137 */       else if (operTotalizador.intValue() > 0) {
/* 138 */         saveAD_DETSIMULARSIMP(jdbcWrapper, codSimSimp, codEmpAnp, mesReferencia, instalacao, codProdAnp, 
/* 139 */             qtdUnPadrao, qtdKg, operTotalizador, VLR_PROD_UN_ANP);
/*     */       } 
/*     */       
/* 142 */       validacao.close();
/*     */     } 
/* 144 */     query.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void totalizadoresGerais(PersistenceEvent arg0) throws Exception {
/* 149 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/* 150 */     JdbcWrapper jdbcWrapper = dwf.getJdbcWrapper();
/* 151 */     jdbcWrapper.openSession();
/*     */     
/* 153 */     DynamicVO dados = (DynamicVO)arg0.getVo();
/*     */     
/* 155 */     BigDecimal codSimSimp = dados.asBigDecimalOrZero("CODSIMSIM");
/* 156 */     String codProdAnp = dados.asString("CODPROD_ANP");
/* 157 */     String codEmpAnp = dados.asString("CODEMP_ANP");
/* 158 */     String mesReferencia = dados.asString("MESREFERENCIA");
/* 159 */     String instalacao = dados.asString("CODINSTALACAO1");
/*     */     
/* 161 */     ResultSet query = buscarTotalizadores.totalizadorGeral(jdbcWrapper, codSimSimp, codEmpAnp, mesReferencia, 
/* 162 */         instalacao, codProdAnp);
/* 163 */     while (query.next()) {
/* 164 */       BigDecimal operTotalizador = query.getBigDecimal("OPER_TOT");
/* 165 */       String qtdUnPadrao = query.getString("QTD_PROD_UN_ANP");
/* 166 */       String qtdKg = query.getString("QTD_PROD_OPERADO_KG");
/* 167 */       String VLR_PROD_UN_ANP = query.getString("VLR_PROD_UN_ANP");
/*     */ 
/*     */ 
/*     */       
/* 171 */       ResultSet validacao = buscarTotalizadores.validarcaoTotalizadorExiste(jdbcWrapper, codSimSimp, codEmpAnp, 
/* 172 */           mesReferencia, instalacao, codProdAnp, operTotalizador);
/*     */       
/* 174 */       if (validacao.next()) {
/* 175 */         String update = "UPDATE AD_DETSIMULARSIMP SET VLRUNIT_NF='" + VLR_PROD_UN_ANP + "',QTD_PROD_UN_ANP = '" + qtdUnPadrao + 
/* 176 */           "',QTD_PROD_OPERADO_KG = '" + qtdKg + "' " + "WHERE CODSIMSIM = " + codSimSimp + "\r\n" + 
/* 177 */           "AND CODEMP_ANP = '" + codEmpAnp + "'\r\n" + "AND MESREFERENCIA = '" + mesReferencia + "'\r\n" + 
/* 178 */           "AND CODINSTALACAO1 = '" + instalacao + "'\r\n" + "AND CODPROD_ANP = '" + codProdAnp + "'\r\n" + 
/* 179 */           "AND CODOPER = " + operTotalizador;
/* 180 */         buscarEstoque.update(jdbcWrapper, update);
/*     */       
/*     */       }
/* 183 */       else if (operTotalizador.intValue() > 0) {
/* 184 */         saveAD_DETSIMULARSIMP(jdbcWrapper, codSimSimp, codEmpAnp, mesReferencia, instalacao, codProdAnp, 
/* 185 */             qtdUnPadrao, qtdKg, operTotalizador, VLR_PROD_UN_ANP);
/*     */       } 
/*     */       
/* 188 */       validacao.close();
/*     */     } 
/*     */     
/* 191 */     query.close();
/* 192 */     jdbcWrapper.closeSession();
/*     */   }
/*     */   
/*     */   public static void totalizadoresGeraisAgendado() throws Exception {
/* 196 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/* 197 */     JdbcWrapper jdbcWrapper = dwf.getJdbcWrapper();
/* 198 */     jdbcWrapper.openSession();
/*     */     
/* 200 */     ResultSet query = buscarTotalizadores.totalizadorGeralAgendado(jdbcWrapper);
/* 201 */     while (query.next()) {
/* 202 */       BigDecimal codSimSimp = query.getBigDecimal("CODSIMSIM");
/* 203 */       String codEmpAnp = query.getString("CODEMP_ANP");
/* 204 */       String mesReferencia = query.getString("MESREFERENCIA");
/* 205 */       String instalacao = query.getString("CODINSTALACAO1");
/* 206 */       String codProdAnp = query.getString("CODPROD_ANP");
/* 207 */       BigDecimal operTotalizador = query.getBigDecimal("OPER_TOT");
/* 208 */       String qtdUnPadrao = query.getString("QTD_PROD_UN_ANP");
/* 209 */       String qtdKg = query.getString("QTD_PROD_OPERADO_KG");
/* 210 */       String VLR_PROD_UN_ANP = query.getString("VLR_PROD_UN_ANP");
/*     */ 
/*     */       
/* 213 */       ResultSet validacao = buscarTotalizadores.validarcaoTotalizadorExiste(jdbcWrapper, codSimSimp, codEmpAnp, 
/* 214 */           mesReferencia, instalacao, codProdAnp, operTotalizador);
/*     */       
/* 216 */       if (validacao.next()) {
/* 217 */         String update = "UPDATE AD_DETSIMULARSIMP SET VLRUNIT_NF='" + VLR_PROD_UN_ANP + "',QTD_PROD_UN_ANP = '" + qtdUnPadrao + 
/* 218 */           "',QTD_PROD_OPERADO_KG = '" + qtdKg + "' " + "WHERE CODSIMSIM = " + codSimSimp + "\r\n" + 
/* 219 */           "AND CODEMP_ANP = '" + codEmpAnp + "'\r\n" + "AND MESREFERENCIA = '" + mesReferencia + "'\r\n" + 
/* 220 */           "AND CODINSTALACAO1 = '" + instalacao + "'\r\n" + "AND CODPROD_ANP = '" + codProdAnp + "'\r\n" + 
/* 221 */           "AND CODOPER = " + operTotalizador;
/* 222 */         buscarEstoque.update(jdbcWrapper, update);
/*     */       
/*     */       }
/* 225 */       else if (operTotalizador.intValue() > 0) {
/* 226 */         saveAD_DETSIMULARSIMP(jdbcWrapper, codSimSimp, codEmpAnp, mesReferencia, instalacao, codProdAnp, 
/* 227 */             qtdUnPadrao, qtdKg, operTotalizador, VLR_PROD_UN_ANP);
/*     */       } 
/*     */       
/* 230 */       validacao.close();
/*     */     } 
/*     */     
/* 233 */     query.close();
/* 234 */     jdbcWrapper.closeSession();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void estoqueFnal(PersistenceEvent arg0) throws Exception {
/* 239 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/* 240 */     JdbcWrapper jdbcWrapper = dwf.getJdbcWrapper();
/* 241 */     jdbcWrapper.openSession();
/*     */     
/* 243 */     DynamicVO dados = (DynamicVO)arg0.getVo();
/*     */     
/* 245 */     BigDecimal codSimSimp = dados.asBigDecimalOrZero("CODSIMSIM");
/* 246 */     String codProdAnp = dados.asString("CODPROD_ANP");
/* 247 */     String codEmpAnp = dados.asString("CODEMP_ANP");
/* 248 */     String mesReferencia = dados.asString("MESREFERENCIA");
/* 249 */     String instalacao = dados.asString("CODINSTALACAO1");
/*     */     
/* 251 */     ResultSet query = buscarTotalizadores.buscarEstoqueInicial(jdbcWrapper, codSimSimp, codEmpAnp, mesReferencia, 
/* 252 */         instalacao, codProdAnp);
/* 253 */     if (query.next()) {
/* 254 */       BigDecimal operTotalizador = query.getBigDecimal("OPER_TOT");
/*     */       
/* 256 */       BigDecimal qtdUnPadrao = query.getBigDecimal("QTD_PROD_UN_ANP");
/* 257 */       BigDecimal qtdKg = query.getBigDecimal("QTD_PROD_OPERADO_KG");
/* 258 */       BigDecimal VLR_PROD_UN_ANP = BigDecimal.ZERO;
/*     */       
/* 260 */       ResultSet entradas = buscarTotalizadores.entradasaida(jdbcWrapper, codSimSimp, codEmpAnp, mesReferencia, 
/* 261 */           instalacao, codProdAnp, "1");
/* 262 */       while (entradas.next()) {
/* 263 */         qtdUnPadrao = qtdUnPadrao.add(entradas.getBigDecimal("QTD_PROD_UN_ANP"));
/* 264 */         qtdKg = qtdKg.add(entradas.getBigDecimal("QTD_PROD_OPERADO_KG"));
/* 265 */         VLR_PROD_UN_ANP = VLR_PROD_UN_ANP.add(entradas.getBigDecimal("VLR_PROD_UN_ANP"));
/*     */       } 
/*     */ 
/*     */       
/* 269 */       entradas.close();
/*     */       
/* 271 */       ResultSet saida = buscarTotalizadores.entradasaida(jdbcWrapper, codSimSimp, codEmpAnp, mesReferencia, 
/* 272 */           instalacao, codProdAnp, "2");
/* 273 */       while (saida.next()) {
/* 274 */         qtdUnPadrao = qtdUnPadrao.subtract(saida.getBigDecimal("QTD_PROD_UN_ANP"));
/* 275 */         qtdKg = qtdKg.subtract(saida.getBigDecimal("QTD_PROD_OPERADO_KG"));
/* 276 */         VLR_PROD_UN_ANP = VLR_PROD_UN_ANP.add(saida.getBigDecimal("VLR_PROD_UN_ANP"));
/*     */       } 
/*     */       
/* 279 */       saida.close();
/*     */       
/* 281 */       ResultSet validacao = buscarTotalizadores.validarcaoTotalizadorExiste(jdbcWrapper, codSimSimp, codEmpAnp, 
/* 282 */           mesReferencia, instalacao, codProdAnp, operTotalizador);
/* 283 */       if (validacao.next()) {
/* 284 */         String update = "UPDATE AD_DETSIMULARSIMP SET VLRUNIT_NF=" + VLR_PROD_UN_ANP + ",QTD_PROD_UN_ANP = '" + qtdUnPadrao + 
/* 285 */           "',QTD_PROD_OPERADO_KG = '" + qtdKg + "' " + "WHERE CODSIMSIM = " + codSimSimp + "\r\n" + 
/* 286 */           "AND CODEMP_ANP = '" + codEmpAnp + "'\r\n" + "AND MESREFERENCIA = '" + mesReferencia + "'\r\n" + 
/* 287 */           "AND CODINSTALACAO1 = '" + instalacao + "'\r\n" + "AND CODPROD_ANP = '" + codProdAnp + "'\r\n" + 
/* 288 */           "AND CODOPER = " + operTotalizador;
/* 289 */         buscarEstoque.update(jdbcWrapper, update);
/*     */       
/*     */       }
/* 292 */       else if (operTotalizador.intValue() > 0) {
/* 293 */         saveAD_DETSIMULARSIMP(jdbcWrapper, codSimSimp, codEmpAnp, mesReferencia, instalacao, codProdAnp, 
/* 294 */             (String)qtdUnPadrao, (String)qtdKg, operTotalizador, (String)VLR_PROD_UN_ANP);
/*     */       } 
/*     */       
/* 297 */       validacao.close();
/*     */     } 
/* 299 */     query.close();
/* 300 */     jdbcWrapper.closeSession();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void estoqueFnalAgendado() throws Exception {
/* 305 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/* 306 */     JdbcWrapper jdbcWrapper = dwf.getJdbcWrapper();
/* 307 */     jdbcWrapper.openSession();
/*     */ 
/*     */ 
/*     */     
/* 311 */     ResultSet query = buscarTotalizadores.buscarEstoqueInicialAgendado(jdbcWrapper);
/*     */     
/* 313 */     while (query.next()) {
/* 314 */       BigDecimal codSimSimp = query.getBigDecimal("CODSIMSIM");
/* 315 */       String codEmpAnp = query.getString("CODEMP_ANP");
/* 316 */       String mesReferencia = query.getString("MESREFERENCIA");
/* 317 */       String instalacao = query.getString("CODINSTALACAO1");
/* 318 */       String codProdAnp = query.getString("CODPROD_ANP");
/* 319 */       BigDecimal operTotalizador = query.getBigDecimal("OPER_TOT");
/* 320 */       BigDecimal VLR_PROD_UN_ANP = BigDecimal.ZERO;
/*     */       
/* 322 */       BigDecimal qtdUnPadrao = query.getBigDecimal("QTD_PROD_UN_ANP");
/* 323 */       BigDecimal qtdKg = query.getBigDecimal("QTD_PROD_OPERADO_KG");
/*     */       
/* 325 */       ResultSet entradas = buscarTotalizadores.entradasaida(jdbcWrapper, codSimSimp, codEmpAnp, mesReferencia, 
/* 326 */           instalacao, codProdAnp, "1");
/* 327 */       while (entradas.next()) {
/* 328 */         qtdUnPadrao = qtdUnPadrao.add(entradas.getBigDecimal("QTD_PROD_UN_ANP"));
/* 329 */         qtdKg = qtdKg.add(entradas.getBigDecimal("QTD_PROD_OPERADO_KG"));
/* 330 */         VLR_PROD_UN_ANP = VLR_PROD_UN_ANP.add(entradas.getBigDecimal("VLR_PROD_UN_ANP"));
/*     */       } 
/*     */       
/* 333 */       entradas.close();
/*     */       
/* 335 */       ResultSet saida = buscarTotalizadores.entradasaida(jdbcWrapper, codSimSimp, codEmpAnp, mesReferencia, 
/* 336 */           instalacao, codProdAnp, "2");
/* 337 */       while (saida.next()) {
/* 338 */         qtdUnPadrao = qtdUnPadrao.subtract(saida.getBigDecimal("QTD_PROD_UN_ANP"));
/* 339 */         qtdKg = qtdKg.subtract(saida.getBigDecimal("QTD_PROD_OPERADO_KG"));
/* 340 */         VLR_PROD_UN_ANP = VLR_PROD_UN_ANP.add(saida.getBigDecimal("VLR_PROD_UN_ANP"));
/*     */       } 
/*     */       
/* 343 */       saida.close();
/*     */       
/* 345 */       ResultSet validacao = buscarTotalizadores.validarcaoTotalizadorExiste(jdbcWrapper, codSimSimp, codEmpAnp, 
/* 346 */           mesReferencia, instalacao, codProdAnp, operTotalizador);
/* 347 */       if (validacao.next()) {
/* 348 */         String update = "UPDATE AD_DETSIMULARSIMP SET VLRUNIT_NF='" + VLR_PROD_UN_ANP + "',QTD_PROD_UN_ANP = '" + qtdUnPadrao + 
/* 349 */           "',QTD_PROD_OPERADO_KG = '" + qtdKg + "' " + "WHERE CODSIMSIM = " + codSimSimp + "\r\n" + 
/* 350 */           "AND CODEMP_ANP = '" + codEmpAnp + "'\r\n" + "AND MESREFERENCIA = '" + mesReferencia + "'\r\n" + 
/* 351 */           "AND CODINSTALACAO1 = '" + instalacao + "'\r\n" + "AND CODPROD_ANP = '" + codProdAnp + "'\r\n" + 
/* 352 */           "AND CODOPER = " + operTotalizador;
/* 353 */         buscarEstoque.update(jdbcWrapper, update);
/*     */       
/*     */       }
/* 356 */       else if (operTotalizador.intValue() > 0) {
/* 357 */         saveAD_DETSIMULARSIMP(jdbcWrapper, codSimSimp, codEmpAnp, mesReferencia, instalacao, codProdAnp, 
/* 358 */             (String)qtdUnPadrao, (String)qtdKg, operTotalizador, (String)VLR_PROD_UN_ANP);
/*     */       } 
/*     */       
/* 361 */       validacao.close();
/*     */     } 
/* 363 */     query.close();
/* 364 */     jdbcWrapper.closeSession();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void saveAD_DETSIMULARSIMP(JdbcWrapper jdbcWrapper, BigDecimal codSimulaSimp, String codEmpAnp, String referenciaArq, String codInstalacao1, String codProdAnp, String qtdUnPadrao, String qtdKg, BigDecimal operacao, String VLRUNIT_NF) throws Exception {
/* 372 */     String insert = "INSERT INTO AD_DETSIMULARSIMP (CODDETSIMSIMP,CODSIMSIM,CODEMP_ANP,MESREFERENCIA,CODINSTALACAO1,CODPROD_ANP,QTD_PROD_UN_ANP,QTD_PROD_OPERADO_KG,CODOPER,VLRUNIT_NF) VALUES((SELECT COALESCE(MAX(CODDETSIMSIMP),0)+1 FROM AD_DETSIMULARSIMP WHERE CODSIMSIM = " + 
/*     */       
/* 374 */       codSimulaSimp + ") ," + codSimulaSimp + ",'" + codEmpAnp + "','" + referenciaArq + "','" + 
/* 375 */       codInstalacao1 + "','" + codProdAnp + "','" + qtdUnPadrao + "','" + qtdKg + "'," + operacao + ",'" + VLRUNIT_NF + "')";
/* 376 */     PreparedStatement pstm = jdbcWrapper.getPreparedStatement(insert);
/* 377 */     pstm.execute();
/* 378 */     pstm.close();
/*     */   }
/*     */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\processamento\processarIncluirTotalizadores.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */