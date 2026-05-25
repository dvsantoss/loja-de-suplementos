package br.ufrn.eaj.lojasuplementos.repository;

import br.ufrn.eaj.lojasuplementos.domain.Suplementos;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SuplementosRepository extends JpaRepository<Suplementos, Long> {
    // O Spring Data cria o comndo sql sozinho baseado no nome deste método
    List<Suplementos> findByIsDeletedIsNull();
}
