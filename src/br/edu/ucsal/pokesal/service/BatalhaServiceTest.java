package br.edu.ucsal.pokesal.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import br.edu.ucsal.pokesal.model.Mochila;
import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.model.enums.SalDex;
import br.edu.ucsal.pokesal.model.enums.Status;
import br.edu.ucsal.pokesal.model.enums.Terreno;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BatalhaServiceTest {

  @Test
  public void testVantagemElemental() {
    Pokesal atacanteFogo = new Pokesal(SalDex.CHARSAL);
    Pokesal defensorPlanta = new Pokesal(SalDex.BULBASAL);
    
    Treinador t1 = new Treinador("J1", SalDex.CHARSAL, new Mochila(new ArrayList<>()));
    Treinador t2 = new Treinador("J2", SalDex.BULBASAL, new Mochila(new ArrayList<>()));
    
    BatalhaService batalha = new BatalhaService(t1, t2, new Random(), new Scanner(System.in));
    
    int dano = batalha.calcularDano(atacanteFogo, defensorPlanta, false);
    assertTrue(dano >= 6, "Dano deve refletir vantagem elemental de 2.0");
  }

  @Test
  public void testEfeitoTerrenoEstacionamentoUCSal() {
    Random randomControlado = new Random() {
      @Override
      public int nextInt(int bound) {
        if (bound == Terreno.values().length) return Terreno.ASFALTO_QUENTE.ordinal(); 
        return super.nextInt(bound);
      }
    };

    Treinador t1 = new Treinador("J1", SalDex.CHARSAL, new Mochila(new ArrayList<>()));
    Treinador t2 = new Treinador("J2", SalDex.CHARSAL, new Mochila(new ArrayList<>()));
    
    BatalhaService batalha = new BatalhaService(t1, t2, randomControlado, new Scanner(System.in));
    
    Pokesal atacanteFogo = t1.criarPokesal();
    Pokesal defensorFogo = t2.criarPokesal();
    
    int dano = batalha.calcularDano(atacanteFogo, defensorFogo, false);
    assertEquals(10, dano, "Dano deve subir de 9 para 10 devido ao bônus de 15% do Asfalto Quente");
  }

  @Test
  public void testOrdemDeAtaquePorVelocidade() throws Exception {
    BatalhaService batalha = new BatalhaService(
        new Treinador("J1", SalDex.CHARSAL, new Mochila(new ArrayList<>())),
        new Treinador("J2", SalDex.BULBASAL, new Mochila(new ArrayList<>())),
        new Random(), new Scanner(System.in)
    );
    
    Pokesal rapido = new Pokesal(SalDex.CHARSAL); // Spd 65
    Pokesal lento = new Pokesal(SalDex.BULBASAL); // Spd 45
    
    // Voltando a usar o "Pé de Cabra" (Reflection) já que não podemos mexer no 'private' do jogo original!
    Method getSpdEfetiva = BatalhaService.class.getDeclaredMethod("getSpdEfetiva", Pokesal.class);
    getSpdEfetiva.setAccessible(true);
    
    int spdRapido = (int) getSpdEfetiva.invoke(batalha, rapido);
    int spdLento = (int) getSpdEfetiva.invoke(batalha, lento);
    
    assertTrue(spdRapido > spdLento, "PokéSal com maior SPD base deve ter maior SPD efetiva");
    
    rapido.setStatus(Status.PARALISADO);
    int spdComParalisia = (int) getSpdEfetiva.invoke(batalha, rapido);
    
    assertTrue(spdComParalisia < spdRapido, "Status Paralisado deve reduzir a velocidade base pela metade");
  }

  @Test
  public void testCalculoDanoBoundaryValues() {
    // 1. Boundary do HP em PokéSal (Não passa do maximo nem fica negativo)
    Pokesal p = new Pokesal(SalDex.BULBASAL); // HpMax = 45
    p.receberDano(9999);
    assertEquals(0, p.getHpAtual(), "HP nunca deve ficar negativo");
    
    p.curar(9999);
    assertEquals(p.getHpMax(), p.getHpAtual(), "Cura não deve ultrapassar HP Máximo");
    
    // 2. Boundary do Ataque/Defesa em BatalhaService (Ataque Menor que Defesa)
    Pokesal atacanteFraco = new Pokesal(SalDex.BULBASAL); // ATK 49
    Pokesal defensorForte = new Pokesal(SalDex.SQUIRTSAL); // DEF 65
    
    BatalhaService batalha = new BatalhaService(
        new Treinador("J1", SalDex.BULBASAL, new Mochila(new ArrayList<>())),
        new Treinador("J2", SalDex.SQUIRTSAL, new Mochila(new ArrayList<>())),
        new Random(), new Scanner(System.in)
    );
    
    int danoLimite = batalha.calcularDano(atacanteFraco, defensorForte, false);
    assertTrue(danoLimite > 0, "Mesmo com Defesa maior que Ataque, o dano tem que ser pelo menos 1 (antes de sofrer os mutiplicadores finais)");
  }

  @Test
  public void testDanoCritico() {
    Treinador t1 = new Treinador("J1", SalDex.CHARSAL, new Mochila(new ArrayList<>()));
    Treinador t2 = new Treinador("J2", SalDex.CHARSAL, new Mochila(new ArrayList<>()));
    
    Random randomNeutro = new Random() {
      @Override
      public int nextInt(int bound) {
        if (bound == Terreno.values().length) return Terreno.CANTEIRO_CENTRAL.ordinal(); 
        return super.nextInt(bound);
      }
    };
    
    BatalhaService batalha = new BatalhaService(t1, t2, randomNeutro, new Scanner(System.in));
    
    Pokesal atacante = t1.criarPokesal();
    Pokesal defensor = t2.criarPokesal();
    
    int danoNormal = batalha.calcularDano(atacante, defensor, false);
    int danoCritico = batalha.calcularDano(atacante, defensor, true);
    
    assertEquals(9, danoNormal, "O dano normal entre CharSal e CharSal deve ser 9");
    assertEquals(13, danoCritico, "O Acerto Crítico deve aplicar o multiplicador de 1.5x e resultar em 13");
  }
}
