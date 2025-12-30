/*     */ package processamento;
/*     */ 
/*     */ import br.com.sankhya.extensions.actionbutton.ContextoAcao;
/*     */ import br.com.sankhya.extensions.actionbutton.Registro;
/*     */ import br.com.sankhya.jape.EntityFacade;
/*     */ import br.com.sankhya.jape.dao.JdbcWrapper;
/*     */ import br.com.sankhya.jape.vo.DynamicVO;
/*     */ import br.com.sankhya.jape.vo.EntityVO;
/*     */ import br.com.sankhya.modelcore.util.EntityFacadeFactory;
/*     */ import buscar.buscarEstoque;
/*     */ import java.math.BigDecimal;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.SimpleDateFormat;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class processarFiltrarDados
/*     */ {
/*     */   public static void processar(ContextoAcao arg0) throws Exception {
/*  24 */     EntityFacade dwf = EntityFacadeFactory.getDWFFacade();
/*  25 */     JdbcWrapper jdbcWrapper = dwf.getJdbcWrapper();
/*  26 */     jdbcWrapper.openSession();
/*     */ 
/*     */     
/*  29 */     Object object1 = arg0.getParam("REFERENCIA");
/*  30 */     Object object2 = arg0.getParam("EMPRESA");
/*     */ 
/*     */     
/*  33 */     object1 = formataData((String)object1);
/*     */     
/*  35 */     Registro[] registros = arg0.getLinhas();
/*  36 */     if (registros.length > 1)
/*  37 */       arg0.mostraErro("S� pode ser simulado um arquivo por vez!");  byte b;
/*     */     int i;
/*     */     Registro[] arrayOfRegistro1;
/*  40 */     for (i = (arrayOfRegistro1 = registros).length, b = 0; b < i; ) { Registro registro = arrayOfRegistro1[b];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  46 */       BigDecimal codSimulaSimp = new BigDecimal((String)registro.getCampo("CODSIMSIM"));
/*  47 */       ResultSet estoqueInicial = buscarEstoque.estoqueInicial(jdbcWrapper, object1.substring(3), (String)object2);
/*  48 */       while (estoqueInicial.next()) {
/*  49 */         EntityFacade dwf2 = EntityFacadeFactory.getDWFFacade();
/*  50 */         JdbcWrapper jdbcWrapper2 = dwf2.getJdbcWrapper();
/*  51 */         jdbcWrapper2.openSession();
/*     */         
/*  53 */         String codEmpAnp = estoqueInicial.getString("AD_CODEMP_ANP");
/*  54 */         String referenciaArq = geraLinha(object1.replace("-", "").substring(2), 6);
/*  55 */         String codInstalacao1 = estoqueInicial.getString("COD_INST_EMP");
/*  56 */         String codInstalacao2 = estoqueInicial.getString("COD_INST_PAR");
/*  57 */         String codProdAnp = estoqueInicial.getString("CODPROD_ANP");
/*  58 */         BigDecimal qtdUnPadrao = estoqueInicial.getBigDecimal("QTD_UN_PADRAO");
/*  59 */         BigDecimal qtdKg = estoqueInicial.getBigDecimal("QTD_CONVERTIDO_KG");
/*  60 */         String codModal = estoqueInicial.getString("CODMODAL_SIMP");
/*  61 */         String codOleoduto = estoqueInicial.getString("COD_OLEODUTO");
/*  62 */         String cgc_cpf = estoqueInicial.getString("CNPJ_PARC");
/*  63 */         String codCidAnp = estoqueInicial.getString("CODCID_ANP");
/*  64 */         String codCnaeAnpParc = estoqueInicial.getString("AD_CNAE_ANP");
/*  65 */         String codPaisAnp = estoqueInicial.getString("COD_PAIS_ANP");
/*  66 */         String licencaImportacao = estoqueInicial.getString("LICENCA_IMPORTACAO_LI");
/*  67 */         String declaracaoImportacao = estoqueInicial.getString("NUM_DELCARACAO_IMPORTACAO_DI");
/*  68 */         String nroNFOper = estoqueInicial.getString("NRONF");
/*  69 */         String codSerieNfAnp = estoqueInicial.getString("COD_SERIE_NF");
/*  70 */         String datOperComercial = estoqueInicial.getString("DTENTSAI");
/*  71 */         String codTipTarifaServ = estoqueInicial.getString("COD_TIP_TAR_SERV");
/*  72 */         String caracteristicaInativo = estoqueInicial.getString("CARACTERISTICA_INATIVO");
/*  73 */         String metodoInativo = estoqueInicial.getString("METODO_INATIVO");
/*  74 */         String modalidadeFrete = estoqueInicial.getString("MODALIDADE_FRETE");
/*  75 */         String nroDocQualidade = estoqueInicial.getString("VALOR_CARACTERISTICA");
/*     */         
/*  77 */         String codProdResultante = estoqueInicial.getString("CODPROD_RESULTANTE_ANP");
/*  78 */         String vlrUnit = estoqueInicial.getString("VLRUNIT");
/*  79 */         String recepienteGlp = estoqueInicial.getString("RECIPIENTE_GLP");
/*  80 */         String chaveNfeCte = estoqueInicial.getString("CHAVE_NFE_CTE");
/*     */         
/*  82 */         BigDecimal operacao = estoqueInicial.getBigDecimal("OPERACAO");
/*     */         
/*  84 */         BigDecimal codOperTotalizador = estoqueInicial.getBigDecimal("OPER_TOTALIZADOR");
/*     */ 
/*     */         
/*  87 */         saveAD_DETSIMULARSIMP(dwf2, 
/*  88 */             codSimulaSimp, 
/*  89 */             codEmpAnp, 
/*  90 */             referenciaArq, 
/*  91 */             codInstalacao1, 
/*  92 */             codInstalacao2, 
/*  93 */             codProdAnp, (
/*  94 */             new StringBuilder(String.valueOf(qtdUnPadrao.intValue()))).toString(), (
/*  95 */             new StringBuilder(String.valueOf(qtdKg.intValue()))).toString(), 
/*  96 */             codModal, 
/*  97 */             codOleoduto, 
/*  98 */             cgc_cpf, 
/*  99 */             codCidAnp, 
/* 100 */             codCnaeAnpParc, 
/* 101 */             codPaisAnp, 
/* 102 */             licencaImportacao, 
/* 103 */             declaracaoImportacao, 
/* 104 */             nroNFOper, 
/* 105 */             codSerieNfAnp, 
/* 106 */             datOperComercial, 
/* 107 */             codTipTarifaServ, 
/* 108 */             caracteristicaInativo, 
/* 109 */             metodoInativo, 
/* 110 */             modalidadeFrete, 
/* 111 */             nroDocQualidade, 
/* 112 */             codProdResultante, 
/* 113 */             vlrUnit, 
/* 114 */             recepienteGlp, 
/* 115 */             chaveNfeCte, 
/* 116 */             codOperTotalizador, 
/* 117 */             operacao, 
/* 118 */             null, 
/* 119 */             null, 
/* 120 */             null, 
/* 121 */             null);
/* 122 */         jdbcWrapper2.closeSession();
/*     */       } 
/* 124 */       estoqueInicial.close();
/*     */       
/* 126 */       ResultSet estoqueInicialProdMov = buscarEstoque.estoqueInicialProdMovimentados(jdbcWrapper, object1.substring(3), (String)object2);
/* 127 */       while (estoqueInicialProdMov.next()) {
/*     */         
/* 129 */         EntityFacade dwf2 = EntityFacadeFactory.getDWFFacade();
/* 130 */         JdbcWrapper jdbcWrapper2 = dwf2.getJdbcWrapper();
/* 131 */         jdbcWrapper2.openSession();
/*     */         
/* 133 */         String codEmpAnp = estoqueInicialProdMov.getString("AD_CODEMP_ANP");
/* 134 */         String referenciaArq = geraLinha(object1.replace("-", "").substring(2), 6);
/* 135 */         String codInstalacao1 = estoqueInicialProdMov.getString("COD_INST_EMP");
/* 136 */         String codInstalacao2 = estoqueInicialProdMov.getString("COD_INST_PAR");
/* 137 */         String codProdAnp = estoqueInicialProdMov.getString("CODPROD_ANP");
/* 138 */         BigDecimal qtdUnPadrao = estoqueInicialProdMov.getBigDecimal("QTD_UN_PADRAO");
/* 139 */         BigDecimal qtdKg = estoqueInicialProdMov.getBigDecimal("QTD_CONVERTIDO_KG");
/* 140 */         String codModal = estoqueInicialProdMov.getString("CODMODAL_SIMP");
/* 141 */         String codOleoduto = estoqueInicialProdMov.getString("COD_OLEODUTO");
/* 142 */         String cgc_cpf = estoqueInicialProdMov.getString("CNPJ_PARC");
/* 143 */         String codCidAnp = estoqueInicialProdMov.getString("CODCID_ANP");
/* 144 */         String codCnaeAnpParc = estoqueInicialProdMov.getString("AD_CNAE_ANP");
/* 145 */         String codPaisAnp = estoqueInicialProdMov.getString("COD_PAIS_ANP");
/* 146 */         String licencaImportacao = estoqueInicialProdMov.getString("LICENCA_IMPORTACAO_LI");
/* 147 */         String declaracaoImportacao = estoqueInicialProdMov.getString("NUM_DELCARACAO_IMPORTACAO_DI");
/* 148 */         String nroNFOper = estoqueInicialProdMov.getString("NRONF");
/* 149 */         String codSerieNfAnp = estoqueInicialProdMov.getString("COD_SERIE_NF");
/* 150 */         String datOperComercial = estoqueInicialProdMov.getString("DTENTSAI");
/* 151 */         String codTipTarifaServ = estoqueInicialProdMov.getString("COD_TIP_TAR_SERV");
/* 152 */         String caracteristicaInativo = estoqueInicialProdMov.getString("CARACTERISTICA_INATIVO");
/* 153 */         String metodoInativo = estoqueInicialProdMov.getString("METODO_INATIVO");
/* 154 */         String modalidadeFrete = estoqueInicialProdMov.getString("MODALIDADE_FRETE");
/* 155 */         String nroDocQualidade = estoqueInicialProdMov.getString("VALOR_CARACTERISTICA");
/*     */         
/* 157 */         String codProdResultante = estoqueInicialProdMov.getString("CODPROD_RESULTANTE_ANP");
/* 158 */         String vlrUnit = estoqueInicialProdMov.getString("VLRUNIT");
/* 159 */         String recepienteGlp = estoqueInicialProdMov.getString("RECIPIENTE_GLP");
/* 160 */         String chaveNfeCte = estoqueInicialProdMov.getString("CHAVE_NFE_CTE");
/*     */         
/* 162 */         BigDecimal operacao = estoqueInicialProdMov.getBigDecimal("OPERACAO");
/*     */         
/* 164 */         BigDecimal codOperTotalizador = estoqueInicialProdMov.getBigDecimal("OPER_TOTALIZADOR");
/*     */ 
/*     */         
/* 167 */         saveAD_DETSIMULARSIMP(dwf2, 
/* 168 */             codSimulaSimp, 
/* 169 */             codEmpAnp, 
/* 170 */             referenciaArq, 
/* 171 */             codInstalacao1, 
/* 172 */             codInstalacao2, 
/* 173 */             codProdAnp, (
/* 174 */             new StringBuilder(String.valueOf(qtdUnPadrao.intValue()))).toString(), (
/* 175 */             new StringBuilder(String.valueOf(qtdKg.intValue()))).toString(), 
/* 176 */             codModal, 
/* 177 */             codOleoduto, 
/* 178 */             cgc_cpf, 
/* 179 */             codCidAnp, 
/* 180 */             codCnaeAnpParc, 
/* 181 */             codPaisAnp, 
/* 182 */             licencaImportacao, 
/* 183 */             declaracaoImportacao, 
/* 184 */             nroNFOper, 
/* 185 */             codSerieNfAnp, 
/* 186 */             datOperComercial, 
/* 187 */             codTipTarifaServ, 
/* 188 */             caracteristicaInativo, 
/* 189 */             metodoInativo, 
/* 190 */             modalidadeFrete, 
/* 191 */             nroDocQualidade, 
/* 192 */             codProdResultante, 
/* 193 */             vlrUnit, 
/* 194 */             recepienteGlp, 
/* 195 */             chaveNfeCte, 
/* 196 */             codOperTotalizador, 
/* 197 */             operacao, 
/* 198 */             null, 
/* 199 */             null, 
/* 200 */             null, 
/* 201 */             null);
/* 202 */         jdbcWrapper2.closeSession();
/*     */       } 
/* 204 */       estoqueInicialProdMov.close();
/*     */       
/* 206 */       ResultSet movimentacoes = buscarEstoque.movimentacoes(jdbcWrapper, object1.substring(3), (String)object2);
/* 207 */       while (movimentacoes.next()) {
/* 208 */         EntityFacade dwf2 = EntityFacadeFactory.getDWFFacade();
/* 209 */         JdbcWrapper jdbcWrapper2 = dwf2.getJdbcWrapper();
/* 210 */         jdbcWrapper2.openSession();
/*     */         
/* 212 */         BigDecimal nunota = movimentacoes.getBigDecimal("NUNOTA");
/* 213 */         String seq = movimentacoes.getString("SEQUENCIA");
/* 214 */         String codProd = movimentacoes.getString("CODPROD");
/* 215 */         String codEmpAnp = movimentacoes.getString("AD_CODEMP_ANP");
/* 216 */         String referenciaArq = geraLinha(object1.replace("-", "").substring(2), 6);
/* 217 */         String codInstalacao1 = movimentacoes.getString("COD_INST_EMP");
/* 218 */         String codInstalacao2 = movimentacoes.getString("COD_INST_PAR");
/* 219 */         String codProdAnp = movimentacoes.getString("CODPROD_ANP");
/* 220 */         BigDecimal qtdUnPadrao = movimentacoes.getBigDecimal("QTD_UN_PADRAO");
/* 221 */         BigDecimal qtdKg = movimentacoes.getBigDecimal("QTD_CONVERTIDO_KG");
/* 222 */         String codModal = movimentacoes.getString("CODMODAL_SIMP");
/* 223 */         String codOleoduto = movimentacoes.getString("COD_OLEODUTO");
/* 224 */         String cgc_cpf = movimentacoes.getString("CNPJ_PARC");
/* 225 */         String codCidAnp = movimentacoes.getString("CODCID_ANP");
/* 226 */         String codCnaeAnpParc = movimentacoes.getString("AD_CNAE_ANP");
/* 227 */         String codPaisAnp = movimentacoes.getString("COD_PAIS_ANP");
/* 228 */         String licencaImportacao = movimentacoes.getString("LICENCA_IMPORTACAO_LI").replace("-", "").replace("/", "");
/* 229 */         String declaracaoImportacao = movimentacoes.getString("NUM_DELCARACAO_IMPORTACAO_DI").replace("-", "").replace("/", "");
/* 230 */         String nroNFOper = movimentacoes.getString("NRONF");
/* 231 */         String codSerieNfAnp = movimentacoes.getString("COD_SERIE_NF");
/* 232 */         String datOperComercial = movimentacoes.getString("DTENTSAI");
/* 233 */         String codTipTarifaServ = movimentacoes.getString("COD_TIP_TAR_SERV");
/* 234 */         String caracteristicaInativo = movimentacoes.getString("CARACTERISTICA_INATIVO");
/* 235 */         String metodoInativo = movimentacoes.getString("METODO_INATIVO");
/* 236 */         String modalidadeFrete = movimentacoes.getString("MODALIDADE_FRETE");
/* 237 */         String nroDocQualidade = movimentacoes.getString("VALOR_CARACTERISTICA");
/*     */         
/* 239 */         String codProdResultante = movimentacoes.getString("CODPROD_RESULTANTE_ANP");
/* 240 */         String vlrUnit = movimentacoes.getString("VLRUNIT");
/* 241 */         String recepienteGlp = movimentacoes.getString("RECIPIENTE_GLP");
/* 242 */         String chaveNfeCte = movimentacoes.getString("CHAVE_NFE_CTE");
/*     */         
/* 244 */         BigDecimal operacao = movimentacoes.getBigDecimal("OPERACAO");
/*     */         
/* 246 */         BigDecimal codOperTotalizador = movimentacoes.getBigDecimal("OPER_TOTALIZADOR");
/*     */         
/* 248 */         String coleta = movimentacoes.getString("AD_TEM_COLETA");
/* 249 */         String classeOper = movimentacoes.getString("CLASS_OPERACAO");
/* 250 */         BigDecimal operPadDispColeta = movimentacoes.getBigDecimal("OPER_PAD_DISP_COLETA");
/*     */ 
/*     */         
/* 253 */         saveAD_DETSIMULARSIMP(dwf2, 
/* 254 */             codSimulaSimp, 
/* 255 */             codEmpAnp, 
/* 256 */             referenciaArq, 
/* 257 */             codInstalacao1, 
/* 258 */             codInstalacao2, 
/* 259 */             codProdAnp, (
/* 260 */             new StringBuilder(String.valueOf(qtdUnPadrao.intValue()))).toString(), (
/* 261 */             new StringBuilder(String.valueOf(qtdKg.intValue()))).toString(), 
/* 262 */             codModal, 
/* 263 */             codOleoduto, 
/* 264 */             cgc_cpf, 
/* 265 */             cgc_cpf.equals("0") ? "0" : codCidAnp, 
/* 266 */             codCnaeAnpParc, 
/* 267 */             codPaisAnp, 
/* 268 */             licencaImportacao, 
/* 269 */             declaracaoImportacao, 
/* 270 */             nroNFOper, 
/* 271 */             codSerieNfAnp, 
/* 272 */             datOperComercial, 
/* 273 */             codTipTarifaServ, 
/* 274 */             caracteristicaInativo, 
/* 275 */             metodoInativo, 
/* 276 */             modalidadeFrete, 
/* 277 */             nroDocQualidade, 
/* 278 */             codProdResultante, 
/* 279 */             vlrUnit, 
/* 280 */             recepienteGlp, 
/* 281 */             chaveNfeCte, 
/* 282 */             codOperTotalizador, 
/* 283 */             operacao, 
/* 284 */             nunota, 
/* 285 */             seq, 
/* 286 */             codProd, 
/* 287 */             null);
/*     */         
/* 289 */         if (coleta.equalsIgnoreCase("S") && classeOper.equalsIgnoreCase("2")) {
/* 290 */           saveAD_DETSIMULARSIMP(dwf2, 
/* 291 */               codSimulaSimp, 
/* 292 */               codEmpAnp, 
/* 293 */               referenciaArq, 
/* 294 */               codInstalacao1, 
/* 295 */               codInstalacao2, 
/* 296 */               codProdAnp, (
/* 297 */               new StringBuilder(String.valueOf(qtdUnPadrao.intValue()))).toString(), (
/* 298 */               new StringBuilder(String.valueOf(qtdKg.intValue()))).toString(), 
/* 299 */               codModal, 
/* 300 */               codOleoduto, 
/* 301 */               cgc_cpf, 
/* 302 */               codCidAnp, 
/* 303 */               codCnaeAnpParc, 
/* 304 */               codPaisAnp, 
/* 305 */               licencaImportacao, 
/* 306 */               declaracaoImportacao, 
/* 307 */               nroNFOper, 
/* 308 */               codSerieNfAnp, 
/* 309 */               datOperComercial, 
/* 310 */               codTipTarifaServ, 
/* 311 */               caracteristicaInativo, 
/* 312 */               metodoInativo, 
/* 313 */               "0", 
/* 314 */               nroDocQualidade, 
/* 315 */               codProdResultante, 
/* 316 */               vlrUnit, 
/* 317 */               recepienteGlp, 
/* 318 */               chaveNfeCte, 
/* 319 */               null, 
/* 320 */               operPadDispColeta, 
/* 321 */               nunota, 
/* 322 */               seq, 
/* 323 */               codProd, 
/* 324 */               null);
/*     */         }
/* 326 */         jdbcWrapper2.closeSession();
/*     */       } 
/* 328 */       movimentacoes.close();
/*     */       
/* 330 */       ResultSet movimentacoesPROD = buscarEstoque.movimentacoesPRODNEWS(jdbcWrapper, object1.substring(3), (String)object2);
/* 331 */       while (movimentacoesPROD.next()) {
/* 332 */         EntityFacade dwf2 = EntityFacadeFactory.getDWFFacade();
/* 333 */         JdbcWrapper jdbcWrapper2 = dwf2.getJdbcWrapper();
/* 334 */         jdbcWrapper2.openSession();
/*     */ 
/*     */ 
/*     */         
/* 338 */         BigDecimal IDIPROC = movimentacoesPROD.getBigDecimal("IDIPROC");
/*     */         
/* 340 */         BigDecimal nunota = movimentacoesPROD.getBigDecimal("NUNOTA");
/* 341 */         String seq = movimentacoesPROD.getString("SEQUENCIA");
/* 342 */         String codProd = movimentacoesPROD.getString("CODPROD");
/* 343 */         String codEmpAnp = movimentacoesPROD.getString("AD_CODEMP_ANP");
/* 344 */         String referenciaArq = geraLinha(object1.replace("-", "").substring(2), 6);
/* 345 */         String codInstalacao1 = movimentacoesPROD.getString("COD_INST_EMP");
/* 346 */         String codInstalacao2 = movimentacoesPROD.getString("COD_INST_PAR");
/* 347 */         String codProdAnp = movimentacoesPROD.getString("CODPROD_ANP");
/* 348 */         BigDecimal qtdUnPadrao = movimentacoesPROD.getBigDecimal("QTD_UN_PADRAO");
/* 349 */         BigDecimal qtdKg = movimentacoesPROD.getBigDecimal("QTD_CONVERTIDO_KG");
/* 350 */         String codModal = movimentacoesPROD.getString("CODMODAL_SIMP");
/* 351 */         String codOleoduto = movimentacoesPROD.getString("COD_OLEODUTO");
/* 352 */         String cgc_cpf = movimentacoesPROD.getString("CNPJ_PARC");
/* 353 */         String codCidAnp = movimentacoesPROD.getString("CODCID_ANP");
/* 354 */         String codCnaeAnpParc = movimentacoesPROD.getString("AD_CNAE_ANP");
/* 355 */         String codPaisAnp = movimentacoesPROD.getString("COD_PAIS_ANP");
/* 356 */         String licencaImportacao = movimentacoesPROD.getString("LICENCA_IMPORTACAO_LI").replace("-", "").replace("/", "");
/* 357 */         String declaracaoImportacao = movimentacoesPROD.getString("NUM_DELCARACAO_IMPORTACAO_DI").replace("-", "").replace("/", "");
/* 358 */         String nroNFOper = movimentacoesPROD.getString("NRONF");
/* 359 */         String codSerieNfAnp = movimentacoesPROD.getString("COD_SERIE_NF");
/* 360 */         String datOperComercial = movimentacoesPROD.getString("DTENTSAI");
/* 361 */         String codTipTarifaServ = movimentacoesPROD.getString("COD_TIP_TAR_SERV");
/* 362 */         String caracteristicaInativo = movimentacoesPROD.getString("CARACTERISTICA_INATIVO");
/* 363 */         String metodoInativo = movimentacoesPROD.getString("METODO_INATIVO");
/* 364 */         String modalidadeFrete = movimentacoesPROD.getString("MODALIDADE_FRETE");
/* 365 */         String nroDocQualidade = movimentacoesPROD.getString("VALOR_CARACTERISTICA");
/*     */         
/* 367 */         String codProdResultante = movimentacoesPROD.getString("CODPROD_RESULTANTE_ANP");
/* 368 */         String vlrUnit = movimentacoesPROD.getString("VLRUNIT");
/* 369 */         String recepienteGlp = movimentacoesPROD.getString("RECIPIENTE_GLP");
/* 370 */         String chaveNfeCte = movimentacoesPROD.getString("CHAVE_NFE_CTE");
/*     */         
/* 372 */         BigDecimal operacao = movimentacoesPROD.getBigDecimal("OPERACAO");
/*     */         
/* 374 */         BigDecimal codOperTotalizador = movimentacoesPROD.getBigDecimal("OPER_TOTALIZADOR");
/*     */         
/* 376 */         String coleta = movimentacoesPROD.getString("AD_TEM_COLETA");
/* 377 */         String classeOper = movimentacoesPROD.getString("CLASS_OPERACAO");
/* 378 */         BigDecimal operPadDispColeta = movimentacoesPROD.getBigDecimal("OPER_PAD_DISP_COLETA");
/*     */ 
/*     */         
/* 381 */         saveAD_DETSIMULARSIMP(dwf2, 
/* 382 */             codSimulaSimp, 
/* 383 */             codEmpAnp, 
/* 384 */             referenciaArq, 
/* 385 */             codInstalacao1, 
/* 386 */             codInstalacao2, 
/* 387 */             codProdAnp, (
/* 388 */             new StringBuilder(String.valueOf(qtdUnPadrao.intValue()))).toString(), (
/* 389 */             new StringBuilder(String.valueOf(qtdKg.intValue()))).toString(), 
/* 390 */             codModal, 
/* 391 */             codOleoduto, 
/* 392 */             cgc_cpf, 
/* 393 */             codCidAnp, 
/* 394 */             codCnaeAnpParc, 
/* 395 */             codPaisAnp, 
/* 396 */             licencaImportacao, 
/* 397 */             declaracaoImportacao, 
/* 398 */             nroNFOper, 
/* 399 */             codSerieNfAnp, 
/* 400 */             datOperComercial, 
/* 401 */             codTipTarifaServ, 
/* 402 */             caracteristicaInativo, 
/* 403 */             metodoInativo, 
/* 404 */             modalidadeFrete, 
/* 405 */             nroDocQualidade, 
/* 406 */             codProdResultante, 
/* 407 */             vlrUnit, 
/* 408 */             recepienteGlp, 
/* 409 */             chaveNfeCte, 
/* 410 */             codOperTotalizador, 
/* 411 */             operacao, 
/* 412 */             nunota, 
/* 413 */             seq, 
/* 414 */             codProd, 
/* 415 */             (String)IDIPROC);
/*     */         
/* 417 */         if (coleta.equalsIgnoreCase("S") && classeOper.equalsIgnoreCase("2")) {
/* 418 */           saveAD_DETSIMULARSIMP(dwf2, 
/* 419 */               codSimulaSimp, 
/* 420 */               codEmpAnp, 
/* 421 */               referenciaArq, 
/* 422 */               codInstalacao1, 
/* 423 */               codInstalacao2, 
/* 424 */               codProdAnp, (
/* 425 */               new StringBuilder(String.valueOf(qtdUnPadrao.intValue()))).toString(), (
/* 426 */               new StringBuilder(String.valueOf(qtdKg.intValue()))).toString(), 
/* 427 */               codModal, 
/* 428 */               codOleoduto, 
/* 429 */               cgc_cpf, 
/* 430 */               codCidAnp, 
/* 431 */               codCnaeAnpParc, 
/* 432 */               codPaisAnp, 
/* 433 */               licencaImportacao, 
/* 434 */               declaracaoImportacao, 
/* 435 */               nroNFOper, 
/* 436 */               codSerieNfAnp, 
/* 437 */               datOperComercial, 
/* 438 */               codTipTarifaServ, 
/* 439 */               caracteristicaInativo, 
/* 440 */               metodoInativo, 
/* 441 */               "0", 
/* 442 */               nroDocQualidade, 
/* 443 */               codProdResultante, 
/* 444 */               vlrUnit, 
/* 445 */               recepienteGlp, 
/* 446 */               chaveNfeCte, 
/* 447 */               null, 
/* 448 */               operPadDispColeta, 
/* 449 */               nunota, 
/* 450 */               seq, 
/* 451 */               codProd, 
/* 452 */               null);
/*     */         }
/* 454 */         jdbcWrapper2.closeSession();
/*     */       } 
/* 456 */       movimentacoesPROD.close();
/*     */ 
/*     */       
/* 459 */       ResultSet totalizador1021 = buscarEstoque.totalizar1021(jdbcWrapper, codSimulaSimp);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 468 */       registro.setCampo("MES_REFERENCIA", Integer.parseInt(object1.split("-")[1]));
/* 469 */       registro.setCampo("ANO_REFRENCIA", object1.split("-")[2]);
/* 470 */       registro.setCampo("STATUS", "0");
/* 471 */       registro.save();
/* 472 */       estoqueInicial.close();
/* 473 */       estoqueInicial.close();
/*     */       b++; }
/*     */     
/* 476 */     jdbcWrapper.closeSession();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String geraLinha(String texto, int size) {
/* 481 */     StringBuilder linha = new StringBuilder("");
/* 482 */     return limitaString(StringUtils.leftPad(texto, size, "0"), size);
/*     */   }
/*     */   
/*     */   public static String limitaString(String texto, int maximo) {
/* 486 */     if (texto.length() <= maximo) {
/* 487 */       return texto;
/*     */     }
/* 489 */     return texto.substring(0, maximo);
/*     */   }
/*     */   
/*     */   public static String formataData(String date) {
/* 493 */     date = date.replace("T", " ");
/* 494 */     Timestamp hoje = Timestamp.valueOf(date);
/* 495 */     SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
/* 496 */     return dateFormat.format(hoje);
/*     */   }
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
/*     */   public static void saveAD_DETSIMULARSIMP(EntityFacade dwf, BigDecimal codSimulaSimp, String codEmpAnp, String referenciaArq, String codInstalacao1, String codInstalacao2, String codProdAnp, String qtdUnPadrao, String qtdKg, String codModal, String codOleoduto, String cgc_cpf, String codCidAnp, String codCnaeAnpParc, String codPaisAnp, String licencaImportacao, String declaracaoImportacao, String nroNFOper, String codSerieNfAnp, String datOperComercial, String codTipTarifaServ, String caracteristicaInativo, String metodoInativo, String modalidadeFrete, String nroDocQualidade, String codProdResultante, String vlrUnit, String recepienteGlp, String chaveNfeCte, BigDecimal codOperTotalizador, BigDecimal operacao, BigDecimal nunota, String seq, String codProd, String idiProc) throws Exception {
/* 534 */     DynamicVO simp = (DynamicVO)dwf.getDefaultValueObjectInstance("AD_DETSIMULARSIMP");
/* 535 */     simp.setProperty("CODSIMSIM", codSimulaSimp);
/* 536 */     simp.setProperty("CODEMP_ANP", codEmpAnp);
/* 537 */     simp.setProperty("MESREFERENCIA", referenciaArq);
/* 538 */     simp.setProperty("CODOPER", operacao);
/* 539 */     simp.setProperty("CODINSTALACAO1", codInstalacao1);
/* 540 */     simp.setProperty("CODINSTALACAO2", codInstalacao2);
/* 541 */     simp.setProperty("CODPROD_ANP", codProdAnp);
/* 542 */     simp.setProperty("QTD_PROD_UN_ANP", qtdUnPadrao);
/* 543 */     simp.setProperty("QTD_PROD_OPERADO_KG", qtdKg);
/* 544 */     simp.setProperty("COD_MODAL_MOVIMENTACAO", codModal);
/* 545 */     simp.setProperty("COD_OLEODUTO_TRANSPORTE", codOleoduto);
/* 546 */     simp.setProperty("CNPJ_PARC", cgc_cpf);
/* 547 */     simp.setProperty("CODCID_ANP", codCidAnp);
/* 548 */     simp.setProperty("COD_ATIV_PARC_ANP", codCnaeAnpParc);
/* 549 */     simp.setProperty("CODPAIS_ANP", codPaisAnp);
/* 550 */     simp.setProperty("NUM_LICEN_IMPORT", licencaImportacao);
/* 551 */     simp.setProperty("NUM_DECLARACAO_IMPORT", declaracaoImportacao);
/* 552 */     simp.setProperty("NUMNF", nroNFOper);
/* 553 */     simp.setProperty("SERIE_NF", codSerieNfAnp);
/* 554 */     simp.setProperty("DAT_OPER_COMERCIAL", datOperComercial);
/* 555 */     simp.setProperty("COD_TIP_TARIFA_SERVICO", codTipTarifaServ);
/* 556 */     simp.setProperty("CARACTERISTICA", caracteristicaInativo);
/* 557 */     simp.setProperty("METODO", metodoInativo);
/* 558 */     simp.setProperty("MODALIDADE_FRETE", modalidadeFrete);
/* 559 */     simp.setProperty("NUM_DOC_QUALIDADE", nroDocQualidade);
/* 560 */     simp.setProperty("COD_OPER_RESULTANTE", codProdResultante);
/* 561 */     simp.setProperty("VLRUNIT_NF", vlrUnit);
/* 562 */     simp.setProperty("RECEPIENTE_GLP", recepienteGlp);
/* 563 */     simp.setProperty("CAHVE_NFE_CTE", chaveNfeCte);
/* 564 */     simp.setProperty("OPERTOTALIZADOR", codOperTotalizador);
/* 565 */     simp.setProperty("NUNOTA", nunota);
/* 566 */     simp.setProperty("SEQUENCIA", seq);
/* 567 */     simp.setProperty("CODPROD", codProd);
/* 568 */     simp.setProperty("IDIPROC", idiProc);
/* 569 */     dwf.createEntity("AD_DETSIMULARSIMP", (EntityVO)simp);
/*     */   }
/*     */ }


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\processamento\processarFiltrarDados.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */