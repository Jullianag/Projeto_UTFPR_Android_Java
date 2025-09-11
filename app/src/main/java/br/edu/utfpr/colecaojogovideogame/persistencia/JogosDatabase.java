package br.edu.utfpr.colecaojogovideogame.persistencia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import br.edu.utfpr.colecaojogovideogame.modelo.Jogo;

@Database(entities = {Jogo.class}, version = 3)
@TypeConverters({ConverterTipoMidia.class, ConverterLocalDate.class})
public abstract class JogosDatabase extends RoomDatabase {

    public abstract JogoDao getJogoDao();

    private static JogosDatabase INSTANCE;

    public static JogosDatabase getInstance(final Context context) {

        if (INSTANCE == null) {

            synchronized (JogosDatabase.class) {

                if (INSTANCE == null) {

                    Builder builder = Room.databaseBuilder(context, JogosDatabase.class, "jogos.db");

                    builder.allowMainThreadQueries();

                    builder.addMigrations(new Migrar_1_2());
                    builder.addMigrations(new Migrar_2_3());

                    // builder.fallbackToDestructiveMigration();

                    INSTANCE = (JogosDatabase) builder.build();
                }
            }
        }

        return INSTANCE;
    }
}
