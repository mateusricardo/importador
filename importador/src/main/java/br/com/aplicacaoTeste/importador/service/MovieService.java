package br.com.aplicacaoTeste.importador.service;

import br.com.aplicacaoTeste.importador.entidades.Movie;
import br.com.aplicacaoTeste.importador.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class MovieService implements Serializable {

    @Autowired
    private MovieRepository repository;

    public void salvar(Movie movie) {
        repository.salvar(movie);
    }
}
