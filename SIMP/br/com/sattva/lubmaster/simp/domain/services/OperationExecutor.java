package br.com.sattva.lubmaster.simp.domain.services;

import br.com.sattva.lubmaster.simp.config.SimpSettings;
import br.com.sattva.lubmaster.simp.domain.model.Operation;
import br.com.sattva.lubmaster.simp.domain.model.SimpRecord;
import java.util.List;

public interface OperationExecutor {
  List<SimpRecord> execute(Operation paramOperation, SimpSettings paramSimpSettings) throws Exception;
}


/* Location:              D:\Evertech\05-LubMaster\LubMaster\SIMP\SATTVA\SIMP.jar!\br\com\sattva\lubmaster\simp\domain\services\OperationExecutor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */