package br.com.arvore.model;

import br.com.arvore.ui.ArvoreDesenho;
import java.util.ArrayList;
import java.util.List;

public class ArvoreControle {
    private ArvoreBinaria arvoreAtual;
    private final ArvoreBinaria arvoreOriginal;
    private final ArvoreDesenho uiRef;

    public ArvoreControle(ArvoreBinaria modeloInicial, ArvoreDesenho uiRef) {
        this.arvoreAtual = modeloInicial; //inicia como AVL
        this.arvoreOriginal = new ArvoreBinaria(); // ABB sempre
        this.uiRef = uiRef;
    }

    public ArvoreBinaria getArvore() {
        return arvoreAtual;
    }

    public void adicionarNaArvore(int valor) {
        uiRef.alternarEstadoControles(true);
        arvoreOriginal.inserirNo(valor);
        arvoreAtual.inserirNo(valor);
        uiRef.redesenharArvore(arvoreAtual);
    }

    public void removerDaArvore(int valor) {
        uiRef.alternarEstadoControles(true);
        arvoreOriginal.removerNo(valor);
        arvoreAtual.removerNo(valor);
        uiRef.redesenharArvore(arvoreAtual);
    }

    public void limparArvore() {
        arvoreOriginal.limparArvore();
        arvoreAtual.limparArvore();
        uiRef.redesenharArvore(arvoreAtual);
    }

    public void gerarArvoreAleatoria(int quantidade) {
        if(quantidade <= 0) quantidade = 0;
        else uiRef.alternarEstadoControles(true);
        if(quantidade > 100) quantidade = 100; // Limite para evitar muitos nós
        limparArvore();
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < quantidade; i++) {
            int valor = rand.nextInt(100);
            if (!arvoreOriginal.buscar(valor)) {
                arvoreOriginal.inserirNo(valor);
                arvoreAtual.inserirNo(valor);
            }
        }
        uiRef.redesenharArvore(arvoreAtual);
    }

    public void trocarModo(boolean usarAVL) {
        uiRef.alternarEstadoControles(true);
        List<Integer> valores = new ArrayList<>();
        copiarValoresPreOrdem(arvoreOriginal.getRaiz(), valores);

        if (usarAVL) {
            arvoreAtual = new ArvoreBalanceada();
        } else {
            arvoreAtual = new ArvoreBinaria();
        }

        for (int valor : valores) {
            arvoreAtual.inserirNo(valor);
        }

        uiRef.redesenharArvore(arvoreAtual);
        uiRef.alternarEstadoControles(false);
    }

    private void copiarValoresPreOrdem(No atual, List<Integer> lista) {
        if (atual == null) return;
        lista.add(atual.getValor());
        copiarValoresPreOrdem(atual.getEsq(), lista);
        copiarValoresPreOrdem(atual.getDir(), lista);
    }
}
