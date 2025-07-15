package br.com.arvore.model;

import java.util.ArrayList;
import java.util.List;

public class ArvoreBinaria {
    protected No raiz;

    public boolean isEmpty() {
        return raiz == null;
    }

    public No getRaiz() {
        return raiz;
    }

    public void inserirNo(int valor) {
        raiz = inserirRec(raiz, valor);
    }

    private No inserirRec(No atual, int valor) {
        if (atual == null) {
            return new No(valor);
        }
        if (valor < atual.getValor()) {
            atual.setEsq(inserirRec(atual.getEsq(), valor));
        } else if (valor > atual.getValor()) {
            atual.setDir(inserirRec(atual.getDir(), valor));
        }
        return atual;
    }

    public void removerNo(int valor) {
        raiz = removerRec(raiz, valor);
    }

    private No removerRec(No atual, int valor) {
        if (atual == null) return null;

        if (valor < atual.getValor()) {
            atual.setEsq(removerRec(atual.getEsq(), valor));
        } else if (valor > atual.getValor()) {
            atual.setDir(removerRec(atual.getDir(), valor));
        } else {
            // Nó encontrado: remover
            if (atual.getEsq() == null) return atual.getDir();
            if (atual.getDir() == null) return atual.getEsq();

            No sucessor = getMinimo(atual.getDir());
            atual.setValor(sucessor.getValor());
            atual.setDir(removerRec(atual.getDir(), sucessor.getValor()));
        }
        return atual;
    }

    private No getMinimo(No atual) {
        while (atual != null && atual.getEsq() != null) {
            atual = atual.getEsq();
        }
        return atual;
    }

    public boolean buscar(int valor) {
        return buscarRec(raiz, valor);
    }

    private boolean buscarRec(No atual, int valor) {
        if (atual == null) return false;
        if (atual.getValor() == valor) return true;
        return valor < atual.getValor() ? buscarRec(atual.getEsq(), valor) : buscarRec(atual.getDir(), valor);
    }

    /**
     * Retorna uma lista contendo o caminho dos valores visitados na busca pelo valor.
     * Se o valor não for encontrado, a lista será vazia.
     */
    public List<Integer> caminhoBusca(int valor) {
        List<Integer> caminho = new ArrayList<>();
        buscarComCaminho(raiz, valor, caminho);

        // Se o último valor não for o valor buscado, limpa a lista (valor não encontrado)
        /*if (!caminho.isEmpty() && caminho.get(caminho.size() - 1) != valor) {
            caminho.clear();
        }*/
        return caminho;
    }

    private void buscarComCaminho(No atual, int valor, List<Integer> caminho) {
        if (atual == null) return;

        caminho.add(atual.getValor());
        if (atual.getValor() == valor) return;

        if (valor < atual.getValor()) {
            buscarComCaminho(atual.getEsq(), valor, caminho);
        } else {
            buscarComCaminho(atual.getDir(), valor, caminho);
        }
    }

    /**
     * Remove todos os nós da árvore, limpando-a.
     */
    public void limparArvore() {
        raiz = null;
    }

    /**
     * Clona a árvore atual (cópia profunda).
     */
    public ArvoreBinaria clone() {
        ArvoreBinaria nova = new ArvoreBinaria();
        nova.raiz = cloneRec(raiz);
        return nova;
    }

    protected No cloneRec(No atual) {
        if (atual == null) return null;
        No novoNo = new No(atual.getValor());
        novoNo.setEsq(cloneRec(atual.getEsq()));
        novoNo.setDir(cloneRec(atual.getDir()));
        return novoNo;
    }

    /**
     * Compara se esta árvore é estruturalmente igual à outra.
     */
    public boolean ehIdentica(ArvoreBinaria outra) {
        return compararNos(this.raiz, outra.getRaiz());
    }

    private boolean compararNos(No n1, No n2) {
        if (n1 == null && n2 == null) return true;
        if (n1 == null || n2 == null) return false;
        return n1.getValor() == n2.getValor()
                && compararNos(n1.getEsq(), n2.getEsq())
                && compararNos(n1.getDir(), n2.getDir());
    }

    protected int altura(No atual) {
    if (atual == null) return -1; // altura de árvore vazia = -1, altura de folha = 0
    int altEsq = altura(atual.getEsq());
    int altDir = altura(atual.getDir());
    return Math.max(altEsq, altDir) + 1;
}

}
