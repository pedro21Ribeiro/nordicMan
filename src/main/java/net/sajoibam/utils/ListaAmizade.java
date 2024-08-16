package net.sajoibam.utils;

import java.util.HashMap;
import java.util.Map;

public class ListaAmizade {
    private Map<String, String> lista;
    private static final ListaAmizade instance = new ListaAmizade();

    private ListaAmizade() {
        lista = new HashMap<String, String>();
    }

    public static ListaAmizade getInstance() {
        return instance;
    }

    public void addLista(final String pessoa1, final String pessoa2) {
        lista.put(pessoa1, pessoa2);
        lista.put(pessoa2, pessoa1);
    }

    public void removeLista(final String perdoador, final String perdoado) {
        if (lista.containsKey(perdoador)) {
            if (lista.get(perdoador).equals(perdoado)) {
                lista.remove(perdoador);
            }
        }
    }

    public boolean jaAdicionado(final String pessoa) {
        return lista.containsKey(pessoa);
    }

    public boolean liberado(final String pessoa1, final String pessoa2) {
        return !(lista.containsKey(pessoa1) && lista.containsKey(pessoa2));
    }
}
