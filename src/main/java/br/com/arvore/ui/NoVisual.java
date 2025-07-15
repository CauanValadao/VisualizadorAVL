package br.com.arvore.ui;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class NoVisual extends StackPane {
    private Circle circulo;
    private Text texto;
    private int valor;

    // Cores padrões
    private final Color COR_PADRAO = Color.WHITE;
    private final Color COR_BORDA = Color.BLACK;
    private final Color COR_DESTAQUE = Color.LIMEGREEN;
    private final Color COR_PISCAR_AMARELO = Color.GOLD;

    public NoVisual(int valor, double raio) {
        this.valor = valor;
        this.circulo = new Circle(raio, COR_PADRAO);
        this.circulo.setStroke(COR_BORDA);

        this.texto = new Text(String.valueOf(valor));

        getChildren().addAll(circulo, texto);
        setAlignment(Pos.CENTER);
    }

    public int getValor() {
        return this.valor;
    }

    // Muda a cor de fundo do círculo
    public void setCorFundo(Color cor) {
        circulo.setFill(cor);
    }

    // Reseta a cor para padrão branco e borda preta, texto normal
    public void resetarEstilo() {
        setCorFundo(COR_PADRAO);
        texto.setFill(Color.BLACK);
        texto.setStyle("");
    }

    // Destaca o nó encontrado em verde e deixa o texto em negrito
    public void destacar() {
        setCorFundo(COR_DESTAQUE);
        texto.setFill(Color.BLACK);
        texto.setStyle("-fx-font-weight: bold;");
    }

    // Pisca amarelo (usado no caminho da busca antes do nó final)
    public void destacarBuscaAmarelo() {
        setCorFundo(COR_PISCAR_AMARELO);
        texto.setFill(Color.BLACK);
        texto.setStyle("");
    }

    // Animação simples para piscar (não obrigatória, mas pode ser útil)
    public void piscar(Color cor1, Color cor2, int vezes, Runnable aoTerminar) {
        FadeTransition ft1 = new FadeTransition(Duration.millis(300), this);
        FadeTransition ft2 = new FadeTransition(Duration.millis(300), this);

        ft1.setFromValue(1.0);
        ft1.setToValue(0.3);
        ft2.setFromValue(0.3);
        ft2.setToValue(1.0);

        ft1.setOnFinished(e -> {
            setCorFundo(cor1);
            ft2.play();
        });
        ft2.setOnFinished(e -> {
            setCorFundo(cor2);
            if (vezes > 1) {
                piscar(cor1, cor2, vezes - 1, aoTerminar);
            } else {
                if (aoTerminar != null) aoTerminar.run();
            }
        });

        ft1.play();
    }
}
