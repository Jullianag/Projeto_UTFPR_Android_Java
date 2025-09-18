package br.edu.utfpr.colecaojogovideogame.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.edu.utfpr.colecaojogovideogame.modelo.Anotacao;

@Dao
public interface AnotacaoDao {

    @Insert
    long insert(Anotacao anotacao);

    @Delete
    int delete(Anotacao anotacao);

    @Update
    int update(Anotacao anotacao);

    @Query("SELECT * FROM Anotacao WHERE id = :id")
    Anotacao queryForId(long id);

    @Query("SELECT * FROM Anotacao WHERE idJogo = :idJogo ORDER BY diaHoraCriacao DESC")
    List<Anotacao> queryForIdJogo(long idJogo);

    @Query("SELECT count(*) FROM Anotacao WHERE idJogo = :idJogo")
    int totalIdJogo(long idJogo);
}
