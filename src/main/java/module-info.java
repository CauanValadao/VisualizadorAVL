module br.com.arvore {
    // Dependências que seu projeto precisa
    requires javafx.controls;
    requires javafx.graphics;

    // Exporta o pacote da interface gráfica (ui) para que o JavaFX possa iniciar sua classe App
    exports br.com.arvore.ui;
    
    // Abre o pacote da ui para permitir que o JavaFX acesse recursos, como imagens
    opens br.com.arvore.ui to javafx.graphics;

    // Exporta o pacote do seu modelo de dados (boa prática)
    exports br.com.arvore.model;
}
