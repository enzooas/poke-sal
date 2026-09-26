package br.edu.ucsal.pokesal.view;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import br.edu.ucsal.pokesal.model.enums.Itens;
import br.edu.ucsal.pokesal.view.Main;

import java.lang.reflect.Method;
import java.util.Random;

public class MainTest {

  @Test
  public void testSistemaRecompensa() throws Exception {
    Method sortearRecompensa = Main.class.getDeclaredMethod("sortearItemRecompensa", Random.class);
    sortearRecompensa.setAccessible(true);

    Random randomControlado = new Random() {
      @Override
      public int nextInt(int bound) {
        return 1;
      }
    };
    
    Itens premio = (Itens) sortearRecompensa.invoke(null, randomControlado);
    assertEquals(Itens.SUPERPOTION, premio, "O sistema de recompensa deve mapear os valores e sortear a SuperPotion");
  }

}
