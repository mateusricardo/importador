package br.com.aplicacaoTeste.importador.restcontroller;

import br.com.aplicacaoTeste.importador.service.ProducerService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;

@RestController
public class MovieRestController {

    @Autowired
    private ProducerService producerService;

    @Autowired
    public MovieRestController(EntityManager em) {
        producerService = new ProducerService(em);
    }

    @RequestMapping(value = "/buscar-intervalo-premios", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<String> buscarDados() {
        JSONObject maxMin = producerService.buscarDados();
        return new ResponseEntity(maxMin.toString(), HttpStatus.OK);
    }
}
