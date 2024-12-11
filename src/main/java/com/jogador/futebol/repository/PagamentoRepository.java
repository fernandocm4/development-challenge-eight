package com.jogador.futebol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.jogador.futebol.models.Jogador;
import com.jogador.futebol.models.Pagamento;

public interface PagamentoRepository  extends JpaRepository<Pagamento, Long>{

	//Acha pagamento pela sua FK
	List<Pagamento> findByJogador(Jogador jogador);

	@Transactional
	void deleteByJogador(Jogador jogador);
}
