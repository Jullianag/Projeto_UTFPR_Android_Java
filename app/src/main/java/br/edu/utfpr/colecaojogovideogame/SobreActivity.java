package br.edu.utfpr.colecaojogovideogame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        setTitle(R.string.sobre);
    }

    public void abrirSiteAutoria(View view) {

        abrirSite("https://www.linkedin.com/in/julliana-gnecco/");
    }

    private void abrirSite(String endereco) {

        Uri uri = Uri.parse(endereco);

        Intent intentAbertura = new Intent(Intent.ACTION_VIEW, uri);

        try {
            startActivity(Intent.createChooser(intentAbertura, getString(R.string.abrir_link_com)));

        } catch (Exception e) {
            Toast.makeText(this,
                    R.string.nenhum_aplicativo_disponivel_para_abrir_paginas_web,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void enviarEmailAutor(View view) {

        enviarEmail(new String[]{"jullianagnecco@gmail.com"}, getString(R.string.contato_pelo_aplicativo));
    }

    private void enviarEmail(String[] enderecos, String assunto) {

        Intent intentAbertura = new Intent(Intent.ACTION_SEND);
        intentAbertura.setType("message/rfc822");

        intentAbertura.putExtra(Intent.EXTRA_EMAIL, enderecos);
        intentAbertura.putExtra(Intent.EXTRA_SUBJECT, assunto);

        try {
            startActivity(Intent.createChooser(intentAbertura, getString(R.string.escolha_o_app_de_e_mail)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,
                    R.string.nenhum_aplicativo_para_enviar_um_email,
                    Toast.LENGTH_LONG).show();
        }
    }
}