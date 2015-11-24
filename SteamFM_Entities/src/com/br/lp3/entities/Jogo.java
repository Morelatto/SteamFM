package com.br.lp3.entities;

import java.util.List;

/**
 *
 * @author Leandro Meneguzzi - 3144893-3
 * @author Lucas Gianfrancesco - 3147173-0
 * @author Pedro Morelatto - 3142463-5
 */
public class Jogo {

    private int steamID;
    private String nome;
    private List<String> listaGeneroJogo;
    private List<Object> listaRecomendacao;

    public Jogo() {
    }

    public Jogo(int steamID, String nome, List<String> listaGeneroJogo) {
        this.steamID = steamID;
        this.nome = nome;
        this.listaGeneroJogo = listaGeneroJogo;
    }

    public int getSteamID() {
        return steamID;
    }

    public void setSteamID(int steamID) {
        this.steamID = steamID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getListaGeneroJogo() {
        return listaGeneroJogo;
    }

    public void setListaGeneroJogo(List<String> listaGeneroJogo) {
        this.listaGeneroJogo = listaGeneroJogo;
    }

    public List<Object> getListaRecomendacao() {
        return listaRecomendacao;
    }

    public void setListaRecomendacao(List<Object> listaRecomendacao) {
        this.listaRecomendacao = listaRecomendacao;
    }

}
