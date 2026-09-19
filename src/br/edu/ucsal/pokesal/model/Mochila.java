package br.edu.ucsal.pokesal.model;

import java.util.List;
import br.edu.ucsal.pokesal.model.enums.Itens;
import br.edu.ucsal.pokesal.model.enums.Status;

public class Mochila {
    
    private List<Itens> itens;
    private int qtdUsadaBatalha;
    private static final int LIMITE_MAXIMO = 2;
    
    public Mochila(List<Itens> itens) {
        this.itens = itens;
        this.qtdUsadaBatalha = 0;
    }

    public List<Itens> getItens() {
        return itens;
    }

    public boolean podeUsarItem() {
        return qtdUsadaBatalha < LIMITE_MAXIMO && !itens.isEmpty();
    }

    public boolean validarUsoItem(Itens item, Pokesal pokesal) {
        if (item == Itens.POTION || item == Itens.SUPERPOTION) {
            return pokesal.getHpAtual() < pokesal.getHpMax();
        }
        if (item == Itens.ANTIDOTE) {
            return pokesal.getStatus() == Status.ENVENENADO;
        }
        return true;
    }

    public void registrarUso(Itens item) {
        itens.remove(item);
        qtdUsadaBatalha++;
    }

    public int getQtdUsadaBatalha() {
        return qtdUsadaBatalha;
    }
}