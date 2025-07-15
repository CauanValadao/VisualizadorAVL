package br.com.arvore.model;

public class ArvoreBalanceada extends ArvoreBinaria {

    //Sobrepor o método para garantir balanceamento
    @Override
    public void inserirNo(int valor) {
        super.inserirNo(valor);
        raiz = balancear(raiz);
    }

    public void inserirSemBalancear(int valor){
        super.inserirNo(valor);
    }
    
    // Sobrepor o método para garantir balanceamento na remoção
    @Override
    public void removerNo(int valor) {
        super.removerNo(valor);
        raiz = balancear(raiz);
    }

    public void removerSemBalancear(int valor){
        super.removerNo(valor);
    }

    // Método para balancear o nó atual
    public No balancear(No atual) {
        if(atual == null) return null;
        atual.setEsq(balancear(atual.getEsq()));
        atual.setDir(balancear(atual.getDir()));
        int fb = fatorBalanceamento(atual);

        // Esquerda-Esquerda (LL)
        if (fb < -1 && fatorBalanceamento(atual.getEsq()) <= 0) {
            return rotacaoDireita(atual);
        }

        // Esquerda-Direita (LR)
        if (fb < -1 && fatorBalanceamento(atual.getEsq()) > 0) {
            atual.setEsq(rotacaoEsquerda(atual.getEsq()));
            return rotacaoDireita(atual);
        }

        // Direita-Direita (RR)
        if (fb > 1 && fatorBalanceamento(atual.getDir()) >= 0) {
            return rotacaoEsquerda(atual);
        }

        // Direita-Esquerda (RL)
        if (fb > 1 && fatorBalanceamento(atual.getDir()) < 0) {
            atual.setDir(rotacaoDireita(atual.getDir()));
            return rotacaoEsquerda(atual);
        }

        return atual;
    } 
    // Verificar balanceamento a partir da raiz
    public boolean estaBalanceada() {
        return estaBalanceadaRec(raiz);
    }

    // Função recursiva de verificar balanceamento a partir de um nó
    private boolean estaBalanceadaRec(No atual) {
        if (atual == null) return true;
        
        int fb = fatorBalanceamento(atual);
        if (Math.abs(fb) > 1) return false;

        return estaBalanceadaRec(atual.getEsq()) && estaBalanceadaRec(atual.getDir());
    }

    // Obtém balanceamento de um nó
    private int fatorBalanceamento(No atual) {
        if(atual == null) return 0;
        return altura(atual.getDir()) - altura(atual.getEsq());
    }

    // Rotacionar para a direita
    private No rotacaoDireita(No atual) {
        No filho = atual.getEsq();
        No neto = filho.getDir();

        filho.setDir(atual);
        atual.setEsq(neto);

        return filho;
    }

    // Rotacionar para a esquerda
    private No rotacaoEsquerda(No atual) {
        No filho = atual.getDir();
        No neto = filho.getEsq();

        filho.setEsq(atual);
        atual.setDir(neto);

        return filho;
    }

    @Override
    public ArvoreBalanceada clone() {
        ArvoreBalanceada novaArvore = new ArvoreBalanceada();
        novaArvore.raiz = cloneRec(this.raiz);
        return novaArvore;
    }
}