package com.example.pedrohenriquemartinsstulp.Model;

import java.io.Serializable;

public class Empresa implements Serializable {
    private int codigo;
    private String descricao;

    public Empresa() {
    }

    public Empresa(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
