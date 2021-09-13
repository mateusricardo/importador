package br.com.aplicacaoTeste.importador.repository;

import br.com.aplicacaoTeste.importador.entidades.Movie;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Repository
public class MovieRepository implements Serializable {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void salvar(Movie movie) {
        em.persist(movie);
    }

    @Transactional
    public List<Movie> buscarDados() {
        String sql = " select * from Movie ";
        Query q = em.createNativeQuery(sql, Movie.class);
        return q.getResultList();
    }
}
