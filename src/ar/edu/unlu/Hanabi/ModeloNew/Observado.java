package ar.edu.unlu.Hanabi.ModeloNew;


import java.util.Observable;
import java.util.Observer;

public interface Observado {
    void agregarObservador(Observador observador);
    void notificarObservador(Eventos evento);
    void notificarObservador(Eventos evento, Object dato);
}
