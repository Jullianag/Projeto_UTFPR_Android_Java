package br.edu.utfpr.colecaojogovideogame.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.edu.utfpr.colecaojogovideogame.modelo.Jogo;

@Dao
public interface JogoDao {

    @Insert
    long insert(Jogo jogo);

    @Delete
    int delete(Jogo jogo);

    @Update
    int update(Jogo jogo);

    @Query("SELECT * FROM jogo WHERE id=:id")
    Jogo queryForId(long id);

    @Query("SELECT * FROM jogo ORDER BY nome ASC")
    List<Jogo> queryAllAscending();

    @Query("SELECT * FROM jogo ORDER BY nome DESC")
    List<Jogo> queryAllDownward();
}
