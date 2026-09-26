package br.edu.ucsal.pokesal.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import br.edu.ucsal.pokesal.model.enums.Itens;

import java.util.ArrayList;
import java.util.List;

public class MochilaTest {

  @Test
  public void testUsoLimiteDeItensExcedido() {
    List<Itens> itens = new ArrayList<>();
    itens.add(Itens.POTION);
    itens.add(Itens.ANTIDOTE);
    itens.add(Itens.SUPERPOTION);
    
    Mochila mochila = new Mochila(itens);

    mochila.registrarUso(Itens.POTION);
    mochila.registrarUso(Itens.ANTIDOTE);

    assertThrows(IllegalStateException.class, () -> {
      mochila.registrarUso(Itens.SUPERPOTION);
    },"O sistema deve lançar um erro impedindo o 3º item na batalha");
  }

}
