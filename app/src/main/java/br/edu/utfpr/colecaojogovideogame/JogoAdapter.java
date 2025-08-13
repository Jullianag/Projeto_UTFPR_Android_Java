package br.edu.utfpr.colecaojogovideogame;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class JogoAdapter extends BaseAdapter {

    private Context context;

    private List<Jogo> listaJogos;

    private String[] generos;

    private static class JogoHolder {

        public TextView textViewValorNome;
        public TextView textViewValorAno;
        public TextView textViewValorValorConsole;
        public TextView textViewValorGenero;
        public TextView textViewValorTipoMidia;
    }

    public JogoAdapter(Context context, List<Jogo> listaJogos) {
        this.context = context;
        this.listaJogos = listaJogos;

        generos = context.getResources().getStringArray(R.array.generos);
    }

    @Override
    public int getCount() {
        return listaJogos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaJogos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        JogoHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.linha_lista_jogos, parent, false);

            holder = new JogoHolder();
            holder.textViewValorNome = convertView.findViewById(R.id.textViewValorNome);
            holder.textViewValorAno = convertView.findViewById(R.id.textViewValorAno);
            holder.textViewValorValorConsole = convertView.findViewById(R.id.textViewValorConsole);
            holder.textViewValorGenero = convertView.findViewById(R.id.textViewValorGenero);
            holder.textViewValorTipoMidia = convertView.findViewById(R.id.textViewValorTipoMidia);

            convertView.setTag(holder);
        }
        else {
            holder = (JogoHolder) convertView.getTag();
        }

        Jogo jogo = listaJogos.get(position);

        holder.textViewValorNome.setText(jogo.getNome());
        holder.textViewValorAno.setText(String.valueOf(jogo.getAno()));

        List<String> valorConsoles = new ArrayList<>();

        if (jogo.isPlaystation()) {
            valorConsoles.add(context.getString(R.string.playstation));
        }
        if (jogo.isXbox()) {
            valorConsoles.add(context.getString(R.string.xbox));
        }
        if (jogo.isNintendoSwitch()) {
            valorConsoles.add(context.getString(R.string.nintendo_switch));
        }

        holder.textViewValorValorConsole.setText(TextUtils.join(" ", valorConsoles));


        holder.textViewValorGenero.setText(generos[jogo.getGenero()]);

        switch (jogo.getTipoMidia()) {

            case Fisica:
                holder.textViewValorTipoMidia.setText(R.string.fisica);
                break;

            case Digital:
                holder.textViewValorTipoMidia.setText(R.string.digital);
                break;
        }

        return convertView;
    }
}
