package com.jogador.futebol.models;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="Jogador")
public class Jogador {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long cod_jogador;
	
	@Column
	String nome;

	@Column
	String email;
	
	@Column
	Date datanasc;
	
	
	public Jogador() {}
	
	public Jogador(String nome, String email, Date datanasc) {
		this.nome = nome;
		this.email=email;
		this.datanasc = datanasc;
	}
	
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDatanasc() {
		return datanasc;
	}

	public void setDatanasc(Date datanasc) {
		this.datanasc = datanasc;
	}

	public Long getCod_jogador() {
		return cod_jogador;
	}
}