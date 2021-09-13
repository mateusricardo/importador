package br.com.aplicacaoTeste.importador.entidadesauxiliares;

import br.com.aplicacaoTeste.importador.entidades.Movie;
import br.com.aplicacaoTeste.importador.entidades.Producer;

public class VoFilmePorProdutor {
    private Producer producer;
    private Movie filmeMaisAntigo;
    private Movie filmeMaisNovo;

    public VoFilmePorProdutor(Producer producer, Movie filmeMaisAntigo, Movie filmeMaisNovo) {
        this.producer = producer;
        this.filmeMaisAntigo = filmeMaisAntigo;
        this.filmeMaisNovo = filmeMaisNovo;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public Movie getFilmeMaisAntigo() {
        return filmeMaisAntigo;
    }

    public void setFilmeMaisAntigo(Movie filmeMaisAntigo) {
        this.filmeMaisAntigo = filmeMaisAntigo;
    }

    public Movie getFilmeMaisNovo() {
        return filmeMaisNovo;
    }

    public void setFilmeMaisNovo(Movie filmeMaisNovo) {
        this.filmeMaisNovo = filmeMaisNovo;
    }
}
