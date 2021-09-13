package br.com.aplicacaoTeste.importador.service;

import br.com.aplicacaoTeste.importador.entidades.Studio;
import br.com.aplicacaoTeste.importador.repository.StudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class StudioService implements Serializable {

    @Autowired
    private StudioRepository repository;

    public Studio salvar(Studio studio) {
        return repository.salvar(studio);
    }
}
