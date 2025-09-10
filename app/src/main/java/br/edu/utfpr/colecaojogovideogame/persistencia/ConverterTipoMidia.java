package br.edu.utfpr.colecaojogovideogame.persistencia;

import androidx.room.TypeConverter;

import br.edu.utfpr.colecaojogovideogame.modelo.TipoMidia;

public class ConverterTipoMidia {


    public static TipoMidia[] tipoMidias = TipoMidia.values();

    @TypeConverter
    public static int fromEnumToInt(TipoMidia tipoMidia) {

        if (tipoMidia == null) {
            return -1;
        }

        return tipoMidia.ordinal();
    }

    @TypeConverter
    public static TipoMidia fromIntToEnum(int ordinal) {

        if (ordinal < 0) {
            return null;
        }

        return tipoMidias[ordinal];
    }
}
