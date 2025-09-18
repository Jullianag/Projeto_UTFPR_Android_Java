package br.edu.utfpr.colecaojogovideogame.modelo;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(foreignKeys = {@ForeignKey(
        entity = Jogo.class,
        parentColumns = "id",
        childColumns = "idJogo",
        onDelete = CASCADE)
})
public class Anotacao {

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
