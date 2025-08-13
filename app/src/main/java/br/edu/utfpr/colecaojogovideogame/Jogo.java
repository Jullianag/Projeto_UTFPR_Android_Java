package br.edu.utfpr.colecaojogovideogame;

public class Jogo {

    private String nome;

    private int ano;

    private boolean playstation;

    private boolean xbox;

    private boolean nintendoSwitch;

    private int genero;

    private TipoMidia tipoMidia;

    public Jogo(String nome, int ano, boolean playstation, boolean xbox, boolean nintendoSwitch, int genero, TipoMidia tipoMidia) {
        this.nome = nome;
        this.ano = ano;
        this.playstation = playstation;
        this.xbox = xbox;
        this.nintendoSwitch = nintendoSwitch;
        this.genero = genero;
        this.tipoMidia = tipoMidia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public boolean isPlaystation() {
        return playstation;
    }

    public void setPlaystation(boolean playstation) {
        this.playstation = playstation;
    }

    public boolean isXbox() {
        return xbox;
    }

    public void setXbox(boolean xbox) {
        this.xbox = xbox;
    }

    public boolean isNintendoSwitch() {
        return nintendoSwitch;
    }

    public void setNintendoSwitch(boolean nintendoSwitch) {
        this.nintendoSwitch = nintendoSwitch;
    }

    public int getGenero() {
        return genero;
    }

    public void setGenero(int genero) {
        this.genero = genero;
    }

    public TipoMidia getTipoMidia() {
        return tipoMidia;
    }

    public void setTipoMidia(TipoMidia tipoMidia) {
        this.tipoMidia = tipoMidia;
    }

    @Override
    public String toString() {
        return nome + "\n" +
                ano + "\n" +
                playstation + "\n" +
                xbox + "\n" +
                nintendoSwitch + "\n" +
                genero + "\n" +
                tipoMidia;
    }
}
