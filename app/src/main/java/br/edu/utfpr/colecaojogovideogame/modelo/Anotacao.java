package br.edu.utfpr.colecaojogovideogame.modelo;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Comparator;

@Entity(foreignKeys = {@ForeignKey(
        entity = Jogo.class,
        parentColumns = "id",
        childColumns = "idJogo",
        onDelete = CASCADE)
})
public class Anotacao {

    public static Comparator<Anotacao> ordenacaoDecrescente = new Comparator<Anotacao>() {

        @Override
        public int compare(Anotacao anotacao1, Anotacao anotacao2) {
            return -1 * anotacao1.getDiaHoraCriacao().compareTo(anotacao2.getDiaHoraCriacao());
        }
    };

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long idJogo;

    @NonNull
    @ColumnInfo(index = true)
    private LocalDateTime diaHoraCriacao;

    @NonNull
    private String texto;

    public Anotacao(long idJogo, @NonNull LocalDateTime diaHoraCriacao, @NonNull String texto) {
        this.idJogo = idJogo;
        this.diaHoraCriacao = diaHoraCriacao;
        this.texto = texto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdJogo() {
        return idJogo;
    }

    public void setIdJogo(long idJogo) {
        this.idJogo = idJogo;
    }

    @NonNull
    public LocalDateTime getDiaHoraCriacao() {
        return diaHoraCriacao;
    }

    public void setDiaHoraCriacao(@NonNull LocalDateTime diaHoraCriacao) {
        this.diaHoraCriacao = diaHoraCriacao;
    }

    @NonNull
    public String getTexto() {
        return texto;
    }

    public void setTexto(@NonNull String texto) {
        this.texto = texto;
    }
}
