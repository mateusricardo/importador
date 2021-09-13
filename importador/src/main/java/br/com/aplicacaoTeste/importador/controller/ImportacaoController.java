package br.com.aplicacaoTeste.importador.controller;

import br.com.aplicacaoTeste.importador.entidades.*;
import br.com.aplicacaoTeste.importador.entidadesauxiliares.VoFilme;
import br.com.aplicacaoTeste.importador.service.MovieService;
import br.com.aplicacaoTeste.importador.service.ProducerService;
import br.com.aplicacaoTeste.importador.service.StudioService;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImportacaoController {

    @Autowired
    private ProducerService producerService;
    @Autowired
    private StudioService studioService;
    @Autowired
    private MovieService movieService;

    public ImportacaoController() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @PostConstruct
    private void init() {
        inicializarDados();
    }

    private void inicializarDados() {
        try {
            CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
            CSVReader csvReader = new CSVReaderBuilder(new FileReader(ResourceUtils.getFile("classpath:movielist.csv"))).withCSVParser(csvParser).withSkipLines(1).build();
            List<VoFilme> filmes = new ArrayList<>();
            for (String[] filme : csvReader.readAll()) {
                filmes.add(new VoFilme(Integer.valueOf(filme[0]), filme[1], filme[2], filme[3], !filme[4].isEmpty() && "yes".equals(filme[4])));
            }
            popularDados(filmes);
        } catch (Exception ex) {
            System.out.println("Ocorreu um erro ao importar o arquivo: " + ex.getMessage());
        }
    }

    private void popularDados(List<VoFilme> filmes) {
        List<Studio> studios = new ArrayList<>();
        List<Producer> producers = new ArrayList<>();
        for (VoFilme filme : filmes) {
            String[] produtores = filme.getProducers().replace(" and ", ",").split(",");
            popularProducer(producers, produtores);

            String[] estudios = filme.getStudios().split(",");
            popularStudio(studios, estudios);

            Movie movie = new Movie(filme.getYear(), filme.getTitle(), filme.getWinner());
            adicionarProducersInMovie(producers, produtores, movie);
            adicionarStudiosInMovie(studios, estudios, movie);
            movieService.salvar(movie);
        }
    }

    private void adicionarStudiosInMovie(List<Studio> studios, String[] estudios, Movie movie) {
        for (String estudio : estudios) {
            MovieStudio movieStudio = new MovieStudio(movie, studios.stream().filter(est -> est.getName().trim().equals(estudio.trim())).findFirst().get());
            movie.getStudios().add(movieStudio);
        }
    }

    private void adicionarProducersInMovie(List<Producer> producers, String[] produtores, Movie movie) {
        for (String produtor : produtores) {
            MovieProducer movieProducer = new MovieProducer(movie, producers.stream().filter(prod -> prod.getName().trim().equals(produtor.trim())).findFirst().get());
            movie.getProducers().add(movieProducer);
        }
    }

    private void popularStudio(List<Studio> studios, String[] estudios) {
        for (String estudio : estudios) {
            if (studios.stream().noneMatch(prod -> prod.getName().trim().equals(estudio.trim()))) {
                studios.add(studioService.salvar(new Studio(estudio.trim())));
            }
        }
    }

    private void popularProducer(List<Producer> producers, String[] produtores) {
        for (String produtor : produtores) {
            if (producers.stream().noneMatch(prod -> prod.getName().trim().equals(produtor.trim()))) {
                producers.add(producerService.salvar(new Producer(produtor.trim())));
            }
        }
    }
}
