package ar.edu.unlu.Hanabi.ModeloNew;

public class CartaRepresentacion {
    private final ColorCarta color;
    private final int valor;
    private final boolean visible;

    public CartaRepresentacion(ColorCarta color, int valor, boolean visible) {
        this.color = color;
        this.valor = valor;
        this.visible = visible;
    }

    public ColorCarta getColor() {
        return color;
    }

    public int getValor() {
        return valor;
    }

    public boolean isVisible() {
        return visible;
    }

}
