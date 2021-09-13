package br.com.aplicacaoTeste.importador;

import br.com.aplicacaoTeste.importador.restcontroller.MovieRestController;
import br.com.aplicacaoTeste.importador.util.JPAUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

@SpringBootTest
class StartApplicationTests {

    @Test
    void contextLoads() throws JSONException {
        EntityManager em = JPAUtil.getEntityManager();
        MovieRestController movieRestController = new MovieRestController(em);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("min", new JSONArray());
        jsonObject.put("max", new JSONArray());
        JSONAssert.assertEquals(movieRestController.buscarDados().getBody(), jsonObject.toString(), false);
    }
}
