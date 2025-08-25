package br.edu.utfpr.colecaojogovideogame;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class JogoRecyclerViewAdapter extends RecyclerView.Adapter<JogoRecyclerViewAdapter.JogoHolder> {

    private OnItemClickListener onItemClickListener;

    private Context context;

    private List<Jogo> listaJogos;

    private String[] generos;

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public JogoRecyclerViewAdapter(Context context, List<Jogo> listaJogos, OnItemClickListener listener) {
        this.context = context;
        this.listaJogos = listaJogos;
        this.onItemClickListener = listener;

        generos = context.getResources().getStringArray(R.array.generos);
    }

    public class JogoHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView textViewValorNome;
        public TextView textViewValorAno;
        public TextView textViewValorValorConsole;
        public TextView textViewValorGenero;
        public TextView textViewValorTipoMidia;

        public JogoHolder(@NonNull View itemView) {
            super(itemView);

            textViewValorNome = itemView.findViewById(R.id.textViewValorNome);
            textViewValorAno = itemView.findViewById(R.id.textViewValorAno);
            textViewValorValorConsole = itemView.findViewById(R.id.textViewValorConsole);
            textViewValorGenero = itemView.findViewById(R.id.textViewValorGenero);
            textViewValorTipoMidia = itemView.findViewById(R.id.textViewValorTipoMidia);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (onItemClickListener != null) {
                int pos = getAbsoluteAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(v, pos);
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {

            if (onItemClickListener != null) {
                int pos = getAbsoluteAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemLongClick(v, pos);
                    return true;
                }
            }
            return false;
        }
    }

    @NonNull
    @Override
    public JogoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.linha_lista_jogos, parent, false);

        return new JogoHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull JogoHolder holder, int position) {

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

    }

    @Override
    public int getItemCount() {
        return listaJogos.size();
    }
}
