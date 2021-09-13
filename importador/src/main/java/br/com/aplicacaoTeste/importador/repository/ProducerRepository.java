package br.com.aplicacaoTeste.importador.repository;

import br.com.aplicacaoTeste.importador.entidades.MovieProducer;
import br.com.aplicacaoTeste.importador.entidades.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Repository
public class ProducerRepository implements Serializable {

    private EntityManager em;

    @Autowired
    public ProducerRepository(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public Producer salvar(Producer producer) {
        return em.merge(producer);
    }

    @Transactional
    public List<Producer> buscarTodos() {
        String sql = " select * from Producer ";
        Query q = em.createNativeQuery(sql, Producer.class);
        return q.getResultList();
    }

    @Transactional
    public List<MovieProducer> buscarFilmesDosProdutores() {
        String hql = " from MovieProducer mp " +
                " order by mp.producer.name " ;
        Query q = em.createQuery(hql, MovieProducer.class);
        return q.getResultList();
    }

    @Autowired
    public void setEm(EntityManager em) {
        this.em = em;
    }
}
