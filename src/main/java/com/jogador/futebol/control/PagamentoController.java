package com.jogador.futebol.control;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jogador.futebol.models.Jogador;
import com.jogador.futebol.models.Pagamento;
import com.jogador.futebol.repository.JogadorRepository;
import com.jogador.futebol.repository.PagamentoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;


@RestController
@RequestMapping("/api")
public class PagamentoController {

	@Autowired
	PagamentoRepository pagrepo;

	@Autowired
	JogadorRepository jogrepo;

	//cria um novo pagamento para um jogador
	@Operation(summary = "Adicionar um pagamento", description = "Adiciona um novo pagamento para o jogador com id informado")
	@PostMapping(path="/jogador/{id}/pagamento")
	public ResponseEntity<Pagamento> createPagamento(@Parameter(description = "Id do jogador que está efetuando novo pagamento") @PathVariable("id") long id, @RequestBody Pagamento pagamento) {
		// procura jogador
		Optional<Jogador> jogadordata = jogrepo.findById(id);
		if (jogadordata.isPresent()) {
			try {
				//cria um pagamento
				Pagamento _pagamento = pagrepo.save(
						new Pagamento(pagamento.getAno(), pagamento.getMes(), pagamento.getValor(), jogadordata.get()));
				return new ResponseEntity<>(_pagamento, HttpStatus.CREATED);
			} catch (Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			//jogador nao encontrado
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	//Lista de pagamentos de um jogador
	@Operation(summary = "Buscar pagamentos", description = "Retorna uma lista de pagamentos de um jagador pelo seu id")
	@GetMapping(path="/jogador/{id}/pagamento") // id do jogador
	public ResponseEntity<List<Pagamento>> getPagamentoByJogadoId(@Parameter(description = "Id do jogador cuja lista de pagamentos será retornada") @PathVariable("id") long id)
	{
		// procura jogador
		Optional<Jogador> jogadordata = jogrepo.findById(id);
		if (jogadordata.isPresent()) {
			// cria uma lista de pagamentos feitos ao jogador
			List<Pagamento> pagamentos = pagrepo.findByJogador(jogadordata.get());
			if (pagamentos.isEmpty() || jogadordata.get() == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			// retorna lista de pagamentos do jogador
			return new ResponseEntity<>(pagamentos, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}
	
	//deleta um pagamento por idp = id do pagamento
	@Operation(summary = "Deletar um pagamento", description = "Deleta um pagamento efetuado por um jogador pelo seu id")
	@DeleteMapping(path="/jogador/{id}/pagamento/{idp}")
    public ResponseEntity<HttpStatus> deleteJogador(@Parameter(description = "Id do jogador", required=false) @PathVariable("id") long id) {
        try {
            pagrepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	//deleta todos os pagamentos de um jogador com id=id
	@Operation(summary = "Deletar pagamentos de um jogador", description = "Deleta todos os pagamentos efetuados por um jogador")
	@DeleteMapping(path="/jogador/{id}/pagamento")
    public ResponseEntity<HttpStatus> deleteAllPagamentos(@Parameter(description = "Id do jogador") @PathVariable("id") long id) {
		// procura jogador
				Optional<Jogador> jogadordata = jogrepo.findById(id);
				if (jogadordata.isPresent()) {	
					try {
						//deleta pagamentos de um jogador
			            pagrepo.deleteByJogador(jogadordata.get());
			            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			        } catch (Exception e) {
			        	System.out.println("erro: "+ e);
			            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			        }
				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
    }

}
