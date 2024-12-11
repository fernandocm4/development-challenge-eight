package com.jogador.futebol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jogador.futebol.models.Jogador;

public interface JogadorRepository extends JpaRepository<Jogador, Long> {

	List<Jogador> findByNome(String nome);
}
