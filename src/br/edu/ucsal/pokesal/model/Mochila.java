package br.edu.ucsal.pokesal.model;

import br.edu.ucsal.pokesal.model.enums.Itens;
import br.edu.ucsal.pokesal.model.enums.Status;
import java.util.ArrayList;
import java.util.List;

/** Guarda os itens do treinador e controla o limite de uso por batalha. */
public class Mochila {

  public static final int LIMITE_MAXIMO = 2;
  private List<Itens> itens;
  private int qtdUsadaBatalha;

  /** Cria a mochila com a lista de itens informada e zera o contador de usos. */
  public Mochila(List<Itens> itens) {
    this.itens = new ArrayList<>(itens);
    this.qtdUsadaBatalha = 0;
  }

  public List<Itens> getItens() {
    return itens;
  }

  /** Indica se ainda resta uso de item na batalha atual e se há itens na mochila. */
  public boolean podeUsarItem() {
    return qtdUsadaBatalha < LIMITE_MAXIMO && !itens.isEmpty();
  }

  /** Verifica se o item faz sentido para a condição atual do PokéSal informado. */
  public boolean validarUsoItem(Itens item, Pokesal pokesal) {
    if (item == Itens.POTION || item == Itens.SUPERPOTION) {
      return pokesal.getHpAtual() < pokesal.getHpMax();
    }
    if (item == Itens.ANTIDOTE) {
      return pokesal.getStatus() == Status.ENVENENADO;
    }
    return true;
  }

  /** Remove o item da mochila e contabiliza mais um uso na batalha. */
  public void registrarUso(Itens item) {
    if (!podeUsarItem()) {
      throw new IllegalStateException(
        "Limite de " + LIMITE_MAXIMO + " itens por batalha atingido ou mochila vazia");
    }
    itens.remove(item);
    qtdUsadaBatalha++;
  }

  /** Zera o contador de itens usados para o início de uma nova batalha. */
  public void resetarUsoBatalha() {
    this.qtdUsadaBatalha = 0;
  }

  public int getQtdUsadaBatalha() {
    return qtdUsadaBatalha;
  }
}
