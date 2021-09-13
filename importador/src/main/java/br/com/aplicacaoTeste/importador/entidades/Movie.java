package br.com.aplicacaoTeste.importador.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer year;
    private String title;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "movie")
    private List<MovieStudio> studios;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "movie")
    private List<MovieProducer> producers;
    private Boolean winner;

    public Movie() {
        studios = new ArrayList<>();
        producers = new ArrayList<>();
    }

    public Movie(Integer year, String title, Boolean winner) {
        this.year = year;
        this.title = title;
        this.winner = winner;
        studios = new ArrayList<>();
        producers = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MovieStudio> getStudios() {
        return studios;
    }

    public void setStudios(List<MovieStudio> studios) {
        this.studios = studios;
    }

    public List<MovieProducer> getProducers() {
        return producers;
    }

    public void setProducers(List<MovieProducer> producers) {
        this.producers = producers;
    }

    public Boolean getWinner() {
        return winner;
    }

    public void setWinner(Boolean winner) {
        this.winner = winner;
    }
}
