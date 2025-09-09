package br.edu.utfpr.colecaojogovideogame.modelo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Comparator;

@Entity
public class Jogo implements Cloneable {

    public static Comparator<Jogo> ordenacaoCrescente = new Comparator<Jogo>() {

        @Override
        public int compare(Jogo jogo1, Jogo jogo2) {

            return jogo1.getNome().compareToIgnoreCase(jogo2.getNome());
        }
    };

    public static Comparator<Jogo> ordenacaoDecrescente = new Comparator<Jogo>() {

        @Override
        public int compare(Jogo jogo1, Jogo jogo2) {

            return -1 * jogo1.getNome().compareToIgnoreCase(jogo2.getNome());
        }
    };

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(index = true)
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {

        /* Como esta classe só tem atributos primitivos ou imutáveis.
           O método clone() da plasse pai já resolve.
         */

        return super.clone();
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
