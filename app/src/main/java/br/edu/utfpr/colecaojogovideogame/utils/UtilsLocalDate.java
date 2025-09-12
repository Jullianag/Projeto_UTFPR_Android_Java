package br.edu.utfpr.colecaojogovideogame.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public final class UtilsLocalDate {

    private UtilsLocalDate() {

    }

    public static String formatLocalDate(LocalDate date) {

        if (date == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
        return date.format(formatter);
    }

    public static long toMillissegundos(LocalDate date) {

        if (date == null) {
            return 0;
        }

        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static int diferencaEmAnosParaHoje(LocalDate date) {

        return diferencaEmAnos(date, LocalDate.now());
    }

    public static int diferencaEmAnos(LocalDate date1, LocalDate date2) {

        if (date1 == null || date2 == null) {
            return 0;
        }

        Period periodo = Period.between(date1, date2);

        return periodo.getYears();
    }
}
