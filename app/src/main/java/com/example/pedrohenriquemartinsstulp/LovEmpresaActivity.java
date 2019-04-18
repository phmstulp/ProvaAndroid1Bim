package com.example.pedrohenriquemartinsstulp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pedrohenriquemartinsstulp.Model.Empresa;
import com.example.pedrohenriquemartinsstulp.Util.Dados;

import java.util.ArrayList;
import java.util.List;

public class LovEmpresaActivity extends AppCompatActivity {

    private ListView lvEmpresas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lov_empresa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvEmpresas = findViewById(R.id.lvEmpresas);


        final ArrayAdapter<Empresa> adapterEmpresa = new ArrayAdapter<Empresa>(
                LovEmpresaActivity.this, R.layout.item_adapter, Dados.empresaList);
        lvEmpresas.setAdapter(adapterEmpresa);

        lvEmpresas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Empresa empresaSelecionada = adapterEmpresa.getItem(position);
                Intent output = new Intent();
                output.putExtra("EMPRESASEL", empresaSelecionada);
                setResult(RESULT_OK, output);
                finish();
            }
        });
    }
}
