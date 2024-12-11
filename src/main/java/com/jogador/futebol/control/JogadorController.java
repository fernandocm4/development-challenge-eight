package com.jogador.futebol.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jogador.futebol.models.Jogador;
import com.jogador.futebol.repository.JogadorRepository;
import com.jogador.futebol.repository.PagamentoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;




@RestController
@RequestMapping("/api")
public class JogadorController {

	@Autowired
    JogadorRepository jogadorrepo;
	
	@Autowired
	PagamentoRepository pagrepo;
	
	@Operation(summary = "Busca jogador", description = "Retorna lista de jogadores, pode retornar uma lista de jogadores com o mesmo nome")
	@GetMapping(path="/jogador")
    public ResponseEntity<List<Jogador>> getAllJogadores(@Parameter(description = "Nome que está sendo buscado") @RequestParam(required = false) String nome) 
	{
        try {
            List<Jogador> jogadores = new ArrayList<Jogador>();
 
            if (nome == null) {
                jogadorrepo.findAll().forEach(jogadores::add);
            } else {
                jogadorrepo.findByNome(nome).forEach(jogadores::add);
            }
 
            if (jogadores.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(jogadores, HttpStatus.OK);
 
 
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@Operation(summary = "Buscar jogador por id", description = "Mostra o jogador com o id informado")
	@GetMapping(path="/jogador/{id}")
    public ResponseEntity<Jogador> getJogadorById(@Parameter(description = "Id do jogador") @PathVariable("id") long id) 
	{
        Optional<Jogador> jogador = jogadorrepo.findById(id);
 
        if (jogador.isPresent()) {
            return new ResponseEntity<>(jogador.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	
	@Operation(summary = "Cadastrar novo jogador", description = "cadastra um novo jogador")
	@PostMapping(path="/jogador")
    public ResponseEntity<Jogador> createJogador(@RequestBody Jogador jogador) 
	{
        try {
            Jogador _jogador = jogadorrepo
            		.save(new Jogador(jogador.getNome(), jogador.getEmail(), jogador.getDatanasc()));
            return new ResponseEntity<>(_jogador, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@Operation(summary = "Atualizar jogador", description = "Atualiza os dados do jogador do id fornecido")
	@PutMapping(path="/jogador/{id}")
    public ResponseEntity<Jogador> updateJogador(@Parameter(description = "Id do jogador que está sendo atualizado") @PathVariable("id") long id, @RequestBody Jogador jogador)
	{
        Optional<Jogador> jogadord = jogadorrepo.findById(id);
 
        if (jogadord.isPresent()) {
            Jogador _jogador = jogadord.get();
            _jogador.setNome(jogador.getNome());
            _jogador.setEmail(jogador.getEmail());
            _jogador.setDatanasc(jogador.getDatanasc());
            //sobrescreve o jogador antigo(id se mantem)
            return new ResponseEntity<>(jogadorrepo.save(_jogador), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	
	@Operation(summary = "Deletar jogador", description = "Deleta o jogador do id informado")
	 @DeleteMapping(path="/jogador/{id}")
	    public ResponseEntity<HttpStatus> deleteJogador(@Parameter(description = "Id do jogador que será deletado") @PathVariable("id") long id) {
		Optional<Jogador> jogadordata = jogadorrepo.findById(id);
		if(jogadordata.isPresent())
		{
		try {
			//deleta pagamentos de um jogador(pois contém FK de jogador)
            pagrepo.deleteByJogador(jogadordata.get()); /*jogadorrepo.findbyid retorna um optional. 
            deleteByJogador não aceita optional, por isso deve criar um optional antes e depois 
            extrair o objeto Jogador*/
            
            //depois de deletar os pagamentos, deleta o jogador
	        jogadorrepo.deleteById(id);
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	        } catch (Exception e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	    }
	 
	@Operation(summary = "Deletr todos jogadores", description = "Deleta todos os jogadores")
	    @DeleteMapping(path="/jogador")
	    public ResponseEntity<HttpStatus> deleteAllJogadores() {
	        try {
	        	pagrepo.deleteAll();
	            jogadorrepo.deleteAll();
	            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	        } catch (Exception e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

}
