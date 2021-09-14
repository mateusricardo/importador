package br.com.aplicacaoTeste.importador.service;

import br.com.aplicacaoTeste.importador.entidades.Movie;
import br.com.aplicacaoTeste.importador.entidades.MovieProducer;
import br.com.aplicacaoTeste.importador.entidades.Producer;
import br.com.aplicacaoTeste.importador.entidadesauxiliares.VoFilmePorProdutor;
import br.com.aplicacaoTeste.importador.repository.ProducerRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.*;

@Service
public class ProducerService {

    @Autowired
    private ProducerRepository repository;

    @Autowired
    public ProducerService(EntityManager em) {
        repository = new ProducerRepository(em);
    }

    public JSONObject buscarDados() {
        HashMap<Producer, List<Movie>> mapFilmesPorProdutor = new HashMap<>();
        for (MovieProducer movieProducer : buscarFilmesDosProdutores()) {
            List<Movie> filmes = new ArrayList<>();
            if (mapFilmesPorProdutor.get(movieProducer.getProducer()) != null) {
                filmes.addAll(mapFilmesPorProdutor.get(movieProducer.getProducer()));
            }
            filmes.add(movieProducer.getMovie());
            Collections.sort(filmes, new Comparator<Movie>() {
                @Override
                public int compare(Movie o1, Movie o2) {
                    return o1 != null && o2 != null ? o1.getYear().compareTo(o2.getYear()) : 0;
                }
            });
            mapFilmesPorProdutor.put(movieProducer.getProducer(), filmes);
        }
        return popularJson(mapFilmesPorProdutor);
    }

    private JSONObject popularJson(HashMap<Producer, List<Movie>> mapFilmesPorProdutor) {
        List<VoFilmePorProdutor> filmesPorProdutor = popularFilmesPorProdutor(mapFilmesPorProdutor);
        List<VoFilmePorProdutor> menoresTempos = popularMenoresTempos(filmesPorProdutor);
        List<VoFilmePorProdutor> maioresTempos = popularMaioresTempos(filmesPorProdutor);
        JSONObject maxMin = new JSONObject();
        JSONArray menoresTemposJson = new JSONArray();
        for (VoFilmePorProdutor menorTempo : menoresTempos) {
            montarJson(menoresTemposJson, menorTempo);
        }
        maxMin.put("min", menoresTemposJson);
        JSONArray maioresTemposJson = new JSONArray();
        for (VoFilmePorProdutor maiorTempo : maioresTempos) {
            montarJson(maioresTemposJson, maiorTempo);
        }
        maxMin.put("max", maioresTemposJson);
        return maxMin;
    }

    private void montarJson(JSONArray maioresOrMenoresTemposJson, VoFilmePorProdutor maiorOrMenorTempo) {
        JSONObject max = new JSONObject();
        max.put("producer", maiorOrMenorTempo.getProducer().getName());
        max.put("interval", maiorOrMenorTempo.getFilmeMaisNovo().getYear() - maiorOrMenorTempo.getFilmeMaisAntigo().getYear());
        max.put("previousWin", maiorOrMenorTempo.getFilmeMaisAntigo().getYear());
        max.put("followingWin", maiorOrMenorTempo.getFilmeMaisNovo().getYear());
        maioresOrMenoresTemposJson.put(max);
    }

    private List<VoFilmePorProdutor> popularMaioresTempos(List<VoFilmePorProdutor> filmesPorProdutor) {
        Integer maiorTempo = 0;
        for (VoFilmePorProdutor voFilmePorProdutor : filmesPorProdutor) {
            if (voFilmePorProdutor.getFilmeMaisAntigo() != null && voFilmePorProdutor.getFilmeMaisNovo() != null && (!voFilmePorProdutor.getFilmeMaisNovo().equals(voFilmePorProdutor.getFilmeMaisAntigo()) && voFilmePorProdutor.getFilmeMaisNovo().getYear() - voFilmePorProdutor.getFilmeMaisAntigo().getYear() > maiorTempo)) {
                maiorTempo = voFilmePorProdutor.getFilmeMaisNovo().getYear() - voFilmePorProdutor.getFilmeMaisAntigo().getYear();
            }
        }
        return buscarMaiorOrMenorTempo(filmesPorProdutor, maiorTempo);
    }

    private List<VoFilmePorProdutor> buscarMaiorOrMenorTempo(List<VoFilmePorProdutor> filmesPorProdutor, Integer maiorOrMenorTempo) {
        List<VoFilmePorProdutor> maioresTempos = new ArrayList<>();
        for (VoFilmePorProdutor voFilmePorProdutor : filmesPorProdutor) {
            if (voFilmePorProdutor.getFilmeMaisAntigo() != null && voFilmePorProdutor.getFilmeMaisNovo() != null && !voFilmePorProdutor.getFilmeMaisNovo().equals(voFilmePorProdutor.getFilmeMaisAntigo()) && voFilmePorProdutor.getFilmeMaisNovo().getYear() - voFilmePorProdutor.getFilmeMaisAntigo().getYear() == maiorOrMenorTempo) {
                maioresTempos.add(voFilmePorProdutor);
            }
        }
        return maioresTempos;
    }

    private List<VoFilmePorProdutor> popularMenoresTempos(List<VoFilmePorProdutor> filmesPorProdutor) {
        Integer menorTempo = 9999;
        for (VoFilmePorProdutor voFilmePorProdutor : filmesPorProdutor) {
            if (voFilmePorProdutor.getFilmeMaisAntigo() != null && voFilmePorProdutor.getFilmeMaisNovo() != null && (!voFilmePorProdutor.getFilmeMaisNovo().equals(voFilmePorProdutor.getFilmeMaisAntigo()) && voFilmePorProdutor.getFilmeMaisNovo().getYear() - voFilmePorProdutor.getFilmeMaisAntigo().getYear() < menorTempo)) {
                menorTempo = voFilmePorProdutor.getFilmeMaisNovo().getYear() - voFilmePorProdutor.getFilmeMaisAntigo().getYear();
            }
        }
        return buscarMaiorOrMenorTempo(filmesPorProdutor, menorTempo);
    }

    private List<VoFilmePorProdutor> popularFilmesPorProdutor(HashMap<Producer, List<Movie>> mapFilmesPorProdutor) {
        List<VoFilmePorProdutor> filmesPorProdutor = new ArrayList<>();
        for (Map.Entry<Producer, List<Movie>> producerEntry : mapFilmesPorProdutor.entrySet()) {
            Movie maisAntigo = null;
            Movie maisNovo = null;
            Movie movieAnterior = null;
            Integer diferenca = 0;
            for (Movie movie : producerEntry.getValue()) {
                if (movie.getWinner()) {
                    if (maisNovo == null || (!movie.equals(maisNovo) && movie.getYear().compareTo(maisNovo.getYear()) > 0)) {
                        maisNovo = movie;
                    }
                    if (movieAnterior != null && maisNovo.getYear() - movieAnterior.getYear() > diferenca) {
                        maisAntigo = movieAnterior;
                    } else if (maisAntigo == null) {
                        maisAntigo = movie;
                    }
                    diferenca = maisNovo.getYear() - maisAntigo.getYear();
                    movieAnterior = movie;
                }
            }
            filmesPorProdutor.add(new VoFilmePorProdutor(producerEntry.getKey(), maisAntigo, maisNovo));
        }
        return filmesPorProdutor;
    }

    public Producer salvar(Producer producer) {
        return repository.salvar(producer);
    }

    private List<MovieProducer> buscarFilmesDosProdutores() {
        return repository.buscarFilmesDosProdutores();
    }
}
