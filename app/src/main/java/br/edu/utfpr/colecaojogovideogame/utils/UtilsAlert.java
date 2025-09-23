package br.edu.utfpr.colecaojogovideogame.utils;


import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import br.edu.utfpr.colecaojogovideogame.R;

public final class UtilsAlert {

    private UtilsAlert() {
    }

    public static void mostrarAviso(Context context, int idMensagem) {

        mostrarAviso(context, context.getString(idMensagem), null);
    }


    public static void mostrarAviso(Context context, int idMenssagem,
                                    DialogInterface.OnClickListener listener) {

        mostrarAviso(context, context.getString(idMenssagem), listener);
    }

    public static void mostrarAviso(Context context, String mensagem,
                                    DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.aviso);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage(mensagem);

        builder.setNeutralButton(R.string.ok, listener);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void confirmarAcao(Context context,
                                     int idMensagem,
                                     DialogInterface.OnClickListener listenerSim,
                                     DialogInterface.OnClickListener listenerNao) {

        confirmarAcao(context, context.getString(idMensagem), listenerSim, listenerNao);

    }

    public static void confirmarAcao(Context context,
                                     String mensagem,
                                     DialogInterface.OnClickListener listenerSim,
                                     DialogInterface.OnClickListener listenerNao) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.confirmacao);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setMessage(mensagem);

        builder.setPositiveButton(R.string.sim, listenerSim);
        builder.setNegativeButton(R.string.nao, listenerNao);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public interface OnTextEnteredListener {

        void onTextEntered(String texto);
    }

    public static void lerTexto(Context context,
                                int idTitulo,
                                int idLayout,
                                int idEditText,
                                String textoInicial,
                                final OnTextEnteredListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(idTitulo);
        builder.setIcon(android.R.drawable.ic_input_get);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(idLayout, null);

        final EditText editText = view.findViewById(idEditText);

        editText.setText(textoInicial);

        builder.setView(view);

        builder.setPositiveButton(R.string.salvar, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String texto = editText.getText().toString();
                listener.onTextEntered(texto);
            }
        });

        builder.setNegativeButton(R.string.cancelar, null);

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                editText.requestFocus();
                editText.setSelection(editText.getText().toString().length());
            }
        });

        dialog.show();
    }
}
