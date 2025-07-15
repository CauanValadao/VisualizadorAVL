package br.com.arvore.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.arvore.model.ArvoreBalanceada;
import br.com.arvore.model.ArvoreBinaria;
import br.com.arvore.model.ArvoreControle;
import br.com.arvore.model.No;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.Color;

public class ArvoreDesenho extends Application {
    private Pane painelDesenho = new Pane();
    private ArvoreControle controle;
    private final double RAIO_NO = 20;
    private final double H_SPACING = 45; // Espaçamento horizontal ENTRE os nós
    private final double V_SPACING = 70;

    private TextField campoTextoValor;
    private Button botaoAdicionar;
    private Button botaoRemover;
    private Button botaoAleatorio;
    private Button botaoBuscar;
    private Button botaoLimpar;
    private ToggleButton toggleAVL_ABB;
    private Label labelBuscaResultado;

    private Map<Integer, NoVisual> mapaDeNosVisuais = new HashMap<>();

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ArvoreBinaria modelo = new ArvoreBalanceada();
        controle = new ArvoreControle(modelo, this);

        BorderPane layoutPrincipal = new BorderPane();
        layoutPrincipal.setCenter(painelDesenho);

        Label labelInput = new Label("Valor do Nó:");
        campoTextoValor = new TextField();
        campoTextoValor.setPromptText("Digite um número");
        botaoAdicionar = new Button("Adicionar na Árvore");
        botaoRemover = new Button("Remover da Árvore");
        botaoAleatorio = new Button("Árvore Aleatória");
        botaoBuscar = new Button("Buscar");
        botaoLimpar = new Button("Limpar Árvore");
        toggleAVL_ABB = new ToggleButton("Modo AVL");
        labelBuscaResultado = new Label(" ");

        toggleAVL_ABB.setSelected(true); // inicia em AVL

        HBox caixaDeControle = new HBox(10, labelInput, campoTextoValor, botaoAdicionar, botaoRemover, botaoBuscar,
                toggleAVL_ABB, botaoLimpar, botaoAleatorio, labelBuscaResultado);
        caixaDeControle.setAlignment(Pos.CENTER);
        caixaDeControle.setPadding(new Insets(15));
        layoutPrincipal.setTop(caixaDeControle);  // Botoes no topo agora

        botaoAdicionar.setOnAction(e -> adicionarNoPelaUI(campoTextoValor));
        botaoRemover.setOnAction(e -> removerNoPelaUI(campoTextoValor));
        botaoAleatorio.setOnAction(e -> gerarArvoreAleatoria(campoTextoValor));
        botaoBuscar.setOnAction(e -> {
            try {
                String texto = campoTextoValor.getText();
                if (texto.isEmpty()) return;
                int valor = Integer.parseInt(texto);
                List<Integer> caminho = controle.getArvore().caminhoBusca(valor);
                boolean encontrado = /*!caminho.isEmpty() &&*/ caminho.get(caminho.size() - 1) == valor;
                animarCaminhoBusca(caminho, encontrado);
                labelBuscaResultado.setText(encontrado ? "Valor " + valor + " encontrado." : "Valor " + valor + " não encontrado.");
                campoTextoValor.clear();
                campoTextoValor.requestFocus();
            } catch (NumberFormatException ex) {
                System.err.println("Entrada inválida. Por favor, digite um número inteiro.");
                campoTextoValor.clear();
            }
        });
        toggleAVL_ABB.setOnAction(e -> {
            boolean novoModoAVL = toggleAVL_ABB.isSelected();
            toggleAVL_ABB.setText(novoModoAVL ? "Modo AVL" : "Modo ABB");
            controle.trocarModo(novoModoAVL);
            labelBuscaResultado.setText(" ");
        });

        botaoLimpar.setOnAction(e -> {
            controle.limparArvore();
            labelBuscaResultado.setText("Árvore limpa.");
            campoTextoValor.clear();
            campoTextoValor.requestFocus();
        });

        campoTextoValor.setOnAction(e -> adicionarNoPelaUI(campoTextoValor));

        Scene scene = new Scene(layoutPrincipal, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Visualizador de Árvore Interativo");
        stage.show();
    }

    private void adicionarNoPelaUI(TextField campo) {
        try {
            String texto = campo.getText();
            if (texto.isEmpty())
                return;
            int valor = Integer.parseInt(texto);
            controle.adicionarNaArvore(valor);
            campo.clear();
            campo.requestFocus();
            labelBuscaResultado.setText(" ");
        } catch (NumberFormatException e) {
            System.err.println("Entrada inválida. Por favor, digite um número inteiro.");
            campo.clear();
        }
    }

    private void removerNoPelaUI(TextField campo) {
        try {
            String texto = campo.getText();
            if (texto.isEmpty())
                return;
            int valor = Integer.parseInt(texto);
            controle.removerDaArvore(valor);
            campo.clear();
            campo.requestFocus();
            labelBuscaResultado.setText(" ");
        } catch (NumberFormatException e) {
            System.err.println("Entrada inválida. Por favor, digite um número inteiro.");
            campo.clear();
        }
    }

    private void gerarArvoreAleatoria(TextField campo) {
        try {
            String texto = campo.getText();
            if (texto.isEmpty())
                return;
            int valor = Integer.parseInt(texto);
            controle.gerarArvoreAleatoria(valor);
            campo.clear();
            campo.requestFocus();
            labelBuscaResultado.setText(" ");
        } catch (NumberFormatException e) {
            System.err.println("Entrada inválida. Por favor, digite um número inteiro.");
            campo.clear();
        }
    }

    public void alternarEstadoControles(boolean desabilitar) {
        campoTextoValor.setDisable(desabilitar);
        botaoAdicionar.setDisable(desabilitar);
        botaoRemover.setDisable(desabilitar);
        botaoAleatorio.setDisable(desabilitar);
        botaoBuscar.setDisable(desabilitar);
        toggleAVL_ABB.setDisable(desabilitar);
        botaoLimpar.setDisable(desabilitar);
    }

    public void redesenharArvore(ArvoreBinaria arvore) {
        // Remove linhas antigas
        painelDesenho.getChildren().removeIf(node -> node instanceof Line);

        resetarEstilosNos();


        if (arvore.getRaiz() == null) {
            mapaDeNosVisuais.values().forEach(painelDesenho.getChildren()::remove);
            mapaDeNosVisuais.clear();
            return;
        }

        Map<Integer, Point2D> posicoesFinais = new HashMap<>();
        calcularPosicoesInOrder(arvore.getRaiz(), 0, new int[] { 0 }, posicoesFinais);

        double minX = posicoesFinais.values().stream().mapToDouble(Point2D::getX).min().orElse(0);
        double maxX = posicoesFinais.values().stream().mapToDouble(Point2D::getX).max().orElse(0);
        double larguraArvore = maxX - minX;

        double centroDoPainelX = painelDesenho.getWidth() / 2;
        double centroDaArvore = minX + larguraArvore / 2;
        double deslocamentoX = centroDoPainelX - centroDaArvore;

        for (Integer valorNo : posicoesFinais.keySet()) {
            Point2D pontoAntigo = posicoesFinais.get(valorNo);
            Point2D pontoNovo = new Point2D(pontoAntigo.getX() + deslocamentoX, pontoAntigo.getY());
            posicoesFinais.put(valorNo, pontoNovo);
        }

        ParallelTransition animacaoGeral = new ParallelTransition();

        Set<Integer> nosExistentes = posicoesFinais.keySet();

        //remove do mapaDeNosVisuais os elementos que nao estao mais na árvore, e prepara a sua animação para desaparecer.
        mapaDeNosVisuais.entrySet().removeIf(entry -> {
            if (!nosExistentes.contains(entry.getKey())) {
                NoVisual noParaRemover = entry.getValue();
                FadeTransition ft = new FadeTransition(Duration.millis(300), noParaRemover);
                ft.setToValue(0);
                ft.setOnFinished(e -> painelDesenho.getChildren().remove(noParaRemover));
                animacaoGeral.getChildren().add(ft);
                return true;
            }
            return false;
        });

        //atualiza mapaDeNosVisuais e prepara as animacoes dos nós
        posicoesFinais.forEach((valor, posicao) -> {
            NoVisual noVisual = mapaDeNosVisuais.get(valor);
            double targetX = posicao.getX() - RAIO_NO;
            double targetY = posicao.getY() - RAIO_NO;

            //se o nó é novo
            if (noVisual == null) {
                noVisual = new NoVisual(valor, RAIO_NO);
                mapaDeNosVisuais.put(valor, noVisual);
                painelDesenho.getChildren().add(noVisual);

                noVisual.setOpacity(0);
                noVisual.setTranslateX(targetX);
                noVisual.setTranslateY(targetY);

                FadeTransition ft = new FadeTransition(Duration.millis(400), noVisual);
                ft.setToValue(1);
                animacaoGeral.getChildren().add(ft);
            } else {//se o nó ja existia
                TranslateTransition tt = new TranslateTransition(Duration.millis(500), noVisual);
                tt.setToX(targetX);
                tt.setToY(targetY);
                animacaoGeral.getChildren().add(tt);
            }
        });

        animacaoGeral.setOnFinished(e -> {
            desenharLinhas(arvore.getRaiz(), posicoesFinais);
            alternarEstadoControles(false);
        });

        animacaoGeral.play();
    }

    private void calcularPosicoesInOrder(No no, int nivel, int[] proximoIndiceX, Map<Integer, Point2D> posicoes) {
        if (no == null) return;

        calcularPosicoesInOrder(no.getEsq(), nivel + 1, proximoIndiceX, posicoes);

        double x = 50 + (proximoIndiceX[0] * H_SPACING);
        double y = 60 + (nivel * V_SPACING);

        posicoes.put(no.getValor(), new Point2D(x, y));
        proximoIndiceX[0]++;

        calcularPosicoesInOrder(no.getDir(), nivel + 1, proximoIndiceX, posicoes);
    }

    //método para calcular pontos na borda do círculo
    private Point2D calcularPontoBordaCirculo(Point2D centro, Point2D pontoAlvo, double raio) {
        double dx = pontoAlvo.getX() - centro.getX();
        double dy = pontoAlvo.getY() - centro.getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) return centro; // evita divisão por zero
        double ratio = raio / dist;
        double x = centro.getX() + dx * ratio;
        double y = centro.getY() + dy * ratio;
        return new Point2D(x, y);
    }

    private void desenharLinhas(No no, Map<Integer, Point2D> posicoes) {
        if (no == null) return;

        Point2D posPai = posicoes.get(no.getValor());

        if (no.getEsq() != null) {
            Point2D posFilhoEsq = posicoes.get(no.getEsq().getValor());
            Point2D start = calcularPontoBordaCirculo(posPai, posFilhoEsq, RAIO_NO);
            Point2D end = calcularPontoBordaCirculo(posFilhoEsq, posPai, RAIO_NO);
            Line linha = new Line(start.getX(), start.getY(), end.getX(), end.getY());
            painelDesenho.getChildren().add(0, linha);
            desenharLinhas(no.getEsq(), posicoes);
        }

        if (no.getDir() != null) {
            Point2D posFilhoDir = posicoes.get(no.getDir().getValor());
            Point2D start = calcularPontoBordaCirculo(posPai, posFilhoDir, RAIO_NO);
            Point2D end = calcularPontoBordaCirculo(posFilhoDir, posPai, RAIO_NO);
            Line linha = new Line(start.getX(), start.getY(), end.getX(), end.getY());
            painelDesenho.getChildren().add(0, linha);
            desenharLinhas(no.getDir(), posicoes);
        }
    }

    private void resetarEstilosNos() {
        mapaDeNosVisuais.values().forEach(no -> no.resetarEstilo());
    }

    private void animarCaminhoBusca(List<Integer> caminho, boolean encontrado) {
        resetarEstilosNos();

        if (caminho.isEmpty()) {
            // Número não encontrado: piscar todos os nós em vermelho
            //piscarTodosNosVermelho();
        } else {
            animarNo(caminho, 0, encontrado);
        }
    }

    private void animarNo(List<Integer> caminho, int index, boolean encontrado) {
        if (index >= caminho.size()) return;

        NoVisual atual = mapaDeNosVisuais.get(caminho.get(index));
        if (atual == null) {
            animarNo(caminho, index + 1, encontrado);
            return;
        }

        if (index == caminho.size() - 1) {
            if(encontrado){
                // Último nó: piscar verde e fixar verde
                atual.piscar(Color.TRANSPARENT, Color.LIMEGREEN, 3, () -> {
                    atual.destacar(); // fixa verde
                });
            }
            else{
                atual.piscar(Color.TRANSPARENT, Color.RED, 3, () -> {
                    atual.resetarEstilo(); // reseta estilo após piscar
                    piscarTodosNosVermelho(); // pisca todos os nós em vermelho
                });
            }
        } else {
            // Nó do caminho: piscar amarelo, depois resetar e animar próximo
            atual.piscar(Color.TRANSPARENT, Color.GOLD, 3, () -> {
                atual.resetarEstilo();
                animarNo(caminho, index + 1, encontrado);
            });
        }
    }


    private void piscarTodosNosVermelho() {
        final int piscarCount = 6; // número de piscadas
        final int delayMillis = 400;

        Timeline timeline = new Timeline();
        for (int i = 0; i < piscarCount; i++) {
            final boolean ligarVermelho = (i % 2 == 0);
            KeyFrame frame = new KeyFrame(Duration.millis(i * delayMillis), e -> {
                mapaDeNosVisuais.values().forEach(no -> {
                    if (ligarVermelho) {
                        no.setCorFundo(Color.RED);
                    } else {
                        no.resetarEstilo();
                    }
                });
            });
            timeline.getKeyFrames().add(frame);
        }

        timeline.setOnFinished(e -> resetarEstilosNos());
        timeline.play();
    }
}