package br.com.aplicacaoTeste.importador.repository;

import br.com.aplicacaoTeste.importador.entidades.Studio;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@Repository
public class StudioRepository implements Serializable {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Studio salvar(Studio studio) {
        return em.merge(studio);
    }
}
