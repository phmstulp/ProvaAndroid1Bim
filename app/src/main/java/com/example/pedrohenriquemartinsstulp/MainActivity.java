package com.example.pedrohenriquemartinsstulp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pedrohenriquemartinsstulp.Model.Atendimento;
import com.example.pedrohenriquemartinsstulp.Model.Empresa;
import com.example.pedrohenriquemartinsstulp.Model.TipoAtendimento;
import com.example.pedrohenriquemartinsstulp.Util.Dados;
import com.example.pedrohenriquemartinsstulp.Util.Mensagem;
import com.example.pedrohenriquemartinsstulp.Util.TipoMensagem;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView tvData, tvEmpresa;
    private EditText etAssunto, etContato, etTelefone, etEmail;
    private Spinner spTipoAtendimento;
    private Button btConsultar, btSalvar, btCancelar;
    private ListView lvAtendimentos;
    private final int LOVEMPRESA = 1;
    private DatePickerDialog datePickerDialog;
    private int day, month, year;
    private Calendar calendar = Calendar.getInstance();
    private Empresa empresaSelecionada = new Empresa();
    private Date dataSelecionada;
    private boolean isEdicao;
    private int indexEdicao;
    private Atendimento atendimentoExcluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(MainActivity.this,
                this, year, month, day);

        loadComponentes();
        loadEventos();

        if ((Dados.empresaList.size() == 0) && (Dados.tipoAtendimentoList.size() == 0))
            carregaDados();

        ArrayAdapter<TipoAtendimento> adapterTipoAtendimento = new ArrayAdapter<TipoAtendimento>(
                MainActivity.this, R.layout.item_adapter, Dados.tipoAtendimentoList);
        spTipoAtendimento.setAdapter(adapterTipoAtendimento);

        carregaLista();

    }

    private void loadComponentes() {
        tvData = findViewById(R.id.tvData);
        tvEmpresa = findViewById(R.id.tvEmpresa);
        etAssunto = findViewById(R.id.etAssunto);
        etContato = findViewById(R.id.etContato);
        etTelefone = findViewById(R.id.etTelefone);
        etEmail = findViewById(R.id.etEmail);
        spTipoAtendimento = findViewById(R.id.spTipoAtendimento);
        btConsultar = findViewById(R.id.btConsultar);
        btSalvar = findViewById(R.id.btSalvar);
        btCancelar = findViewById(R.id.btCancelar);
        lvAtendimentos = findViewById(R.id.lvAtendimentos);
    }

    private void loadEventos() {
        btConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LovEmpresaActivity.class);
                //Inicia uma nova tela esperando um resoltado (Objeto, TipoPrimitivo)
                startActivityForResult(intent, LOVEMPRESA);
            }
        });
        tvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chamar Dialog
                datePickerDialog.show();
            }
        });
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((etAssunto.getText().toString().trim().length() > 0) &&
                        (etContato.getText().toString().trim().length() > 0) &&
                        (etTelefone.getText().toString().trim().length() > 0) &&
                        (etEmail.getText().toString().trim().length() > 0) &&
                        (tvEmpresa.getText().toString() != "Empresa")) {
                    if (!isEdicao) {
                        Atendimento atendimento = new Atendimento();
                        atendimento.setCodigo(getLastIdAtendimento());
                        atendimento.setAssunto(etAssunto.getText().toString().trim());
                        atendimento.setContato(etContato.getText().toString().trim());
                        atendimento.setTelefone(etTelefone.getText().toString().trim());
                        atendimento.setEmail(etEmail.getText().toString().trim());
                        atendimento.setTipoAtendimento((TipoAtendimento) spTipoAtendimento.getSelectedItem());
                        atendimento.setData(dataSelecionada);
                        atendimento.setEmpresa(empresaSelecionada);
                        Dados.atendimentoList.add(atendimento);
                        Mensagem.ExibirMensagem(MainActivity.this, "Atendimento salvo com sucesso", TipoMensagem.SUCESSO);
                        carregaLista();
                        limpaCampos();
                    } else {
                        Dados.atendimentoList.remove(atendimentoExcluir);
                        Atendimento atendimento = new Atendimento();
                        atendimento.setCodigo(getLastIdAtendimento());
                        atendimento.setAssunto(etAssunto.getText().toString().trim());
                        atendimento.setContato(etContato.getText().toString().trim());
                        atendimento.setTelefone(etTelefone.getText().toString().trim());
                        atendimento.setEmail(etEmail.getText().toString().trim());
                        atendimento.setTipoAtendimento((TipoAtendimento) spTipoAtendimento.getSelectedItem());
                        atendimento.setData(dataSelecionada);
                        atendimento.setEmpresa(empresaSelecionada);
                        Dados.atendimentoList.add(atendimento);
                        Mensagem.ExibirMensagem(MainActivity.this, "Atendimento atualizado com sucesso", TipoMensagem.SUCESSO);
                        carregaLista();
                        limpaCampos();
                        btSalvar.setText(R.string.lbSalvar);
                    }
                } else {
                    Mensagem.ExibirMensagem(MainActivity.this, "Favor preencher TODOS os campos!", TipoMensagem.ALERTA);
                }
            }
        });
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpaCampos();
            }
        });
        lvAtendimentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Atendimento atendimento = (Atendimento) lvAtendimentos.getItemAtPosition(position);
                carregarCampos(atendimento);
                isEdicao = true;
                indexEdicao = position;
                btSalvar.setText(R.string.lbEditar);
                atendimentoExcluir = new Atendimento();
                atendimentoExcluir = atendimento;
            }
        });
        lvAtendimentos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Atendimento atendimento = (Atendimento) lvAtendimentos.getItemAtPosition(position);
                atendimentoExcluir = atendimento;
                AlertDialog.Builder alertConfirmacao = new AlertDialog.Builder(MainActivity.this);
                alertConfirmacao.setTitle("Confirmação Exclusão");
                alertConfirmacao.setMessage("Deseja Realmente excluir o Registro ???");
                alertConfirmacao.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dados.atendimentoList.remove(atendimentoExcluir);
                        carregaLista();
                    }
                });
                alertConfirmacao.setNegativeButton(R.string.lbCancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertConfirmacao.show();
                return true;
            }
        });
    }

    private void limpaCampos() {
        tvData.setText(R.string.lbData);
        tvEmpresa.setText(R.string.lbEmpresa);
        etAssunto.setText("");
        etContato.setText("");
        etTelefone.setText("");
        etEmail.setText("");
    }

    private void carregaDados() {
        TipoAtendimento tipoAtendimento = new TipoAtendimento();
        tipoAtendimento.setCodigo(1);
        tipoAtendimento.setDescricao("Erro");
        Dados.tipoAtendimentoList.add(tipoAtendimento);

        tipoAtendimento = new TipoAtendimento();
        tipoAtendimento.setCodigo(2);
        tipoAtendimento.setDescricao("Melhoria");
        Dados.tipoAtendimentoList.add(tipoAtendimento);

        tipoAtendimento = new TipoAtendimento();
        tipoAtendimento.setCodigo(3);
        tipoAtendimento.setDescricao("Manutenção");
        Dados.tipoAtendimentoList.add(tipoAtendimento);


        Empresa empresa = new Empresa();
        empresa.setCodigo(1);
        empresa.setDescricao("Copagril");
        Dados.empresaList.add(empresa);

        empresa = new Empresa();
        empresa.setCodigo(2);
        empresa.setDescricao("Cvale");
        Dados.empresaList.add(empresa);

        empresa = new Empresa();
        empresa.setCodigo(3);
        empresa.setDescricao("Agricola Horizonte");
        Dados.empresaList.add(empresa);

        empresa = new Empresa();
        empresa.setCodigo(4);
        empresa.setDescricao("Copacol");
        Dados.empresaList.add(empresa);

        empresa = new Empresa();
        empresa.setCodigo(5);
        empresa.setDescricao("Frimesa");
        Dados.empresaList.add(empresa);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == LOVEMPRESA) {
            Empresa empresa = (Empresa) data.getExtras().get("EMPRESASEL");
            if (empresa != null) {
                tvEmpresa.setText(empresa.toString());
                empresaSelecionada = (Empresa) data.getExtras().get("EMPRESASEL");
            }
        } else if (resultCode == RESULT_CANCELED && requestCode == LOVEMPRESA) {
            Mensagem.ExibirMensagem(MainActivity.this, "Favor Selecionar uma Empresa", TipoMensagem.ALERTA);
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
            tvData.setText(day + "/" + (month + 1) + "/" + year);
            dataSelecionada = new Date();
            dataSelecionada.setYear(year);
            dataSelecionada.setMonth(month);
            dataSelecionada.setDate(day);
    }

    public static int getLastIdAtendimento() {
        int codigo;
        if (Dados.atendimentoList == null) {
            codigo = 1;
        } else {
            codigo = (Dados.atendimentoList.size()) + 1;
        }
        return codigo;
    }

    private void carregaLista() {
        ArrayAdapter<Atendimento> adapterAtendimento = new ArrayAdapter<Atendimento>(
                MainActivity.this, R.layout.item_adapter, Dados.atendimentoList);
        lvAtendimentos.setAdapter(adapterAtendimento);
    }

    private void carregarCampos(Atendimento atendimento) {
        etAssunto.setText(atendimento.getAssunto());
        etContato.setText(atendimento.getContato());
        etTelefone.setText(atendimento.getTelefone());
        etEmail.setText(atendimento.getEmail());
        String data = atendimento.getData().getDay() + "/" + atendimento.getData().getMonth() + "/" +
                atendimento.getData().getYear();
        tvData.setText(data);
        tvEmpresa.setText(atendimento.getEmpresa().toString());
    }



}
