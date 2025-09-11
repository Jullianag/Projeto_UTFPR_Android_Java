package br.edu.utfpr.colecaojogovideogame.persistencia;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migrar_1_2 extends Migration {

    public Migrar_1_2() {
        super(1, 2);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {

        database.execSQL("CREATE TABLE IF NOT EXISTS `Jogo_provisorio` " +
                "(`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "`nome` TEXT NOT NULL, " +
                "`ano` INTEGER NOT NULL, " +
                "`playstation` INTEGER NOT NULL, " +
                "`xbox` INTEGER NOT NULL, " +
                "`nintendoSwitch` INTEGER NOT NULL, " +
                "`genero` INTEGER NOT NULL, " +
                "`tipoMidia` INTEGER)");

        database.execSQL("INSERT INTO Jogo_provisorio (id, nome, ano, playstation, xbox, nintendoSwitch, genero, tipoMidia) " +
        "SELECT id, nome, ano, playstation, xbox, nintendoSwitch, genero, " +
                "CASE " +
                "WHEN tipoMidia = 'Fisica' THEN 0 " +
                "WHEN tipoMidia = 'Digital' THEN 1 " +
                "ELSE -1 " +
                "END " +
                "FROM Jogo");

        database.execSQL("DROP TABLE Jogo");
        database.execSQL("ALTER TABLE Jogo_provisorio RENAME TO Jogo");

        database.execSQL("CREATE INDEX IF NOT EXISTS `index_Jogo_nome` ON `Jogo` (`nome`)");

    }
}
