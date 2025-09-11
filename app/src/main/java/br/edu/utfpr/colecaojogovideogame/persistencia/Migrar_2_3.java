package br.edu.utfpr.colecaojogovideogame.persistencia;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migrar_2_3 extends Migration {


    public Migrar_2_3() {
        super(1, 2);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {

        database.execSQL("ALTER TABLE Jogo ADD COLUMN dataLancamento INTEGER");

    }
}
