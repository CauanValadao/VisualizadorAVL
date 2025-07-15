package br.com.arvore.model;

public class No{
    private int valor;
    private No esq;
    private No dir;

    public No(){
        this.valor = 0 ;
        this.esq = null;
        this.dir = null;
    }

    public No(int valor) {
        this.valor = valor;
        this.esq = null;
        this.dir = null;
    }

    public No(int valor, No esq, No dir) {
        this.valor = valor;
        this.esq = esq;
        this.dir = dir;
    }

    public int getValor() {
        return valor;
    }

    public No getEsq() {
        return esq;
    }

    public No getDir() {
        return dir;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public void setEsq(No esq) {
        this.esq = esq;
    }

    public void setDir(No dir) {
        this.dir = dir;
    }
}