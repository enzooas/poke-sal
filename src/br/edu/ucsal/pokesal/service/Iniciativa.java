package br.edu.ucsal.pokesal.service;

import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.enums.Status;

/** Decide a ordem de ataque do turno pela velocidade efetiva dos PokéSal. */
public class Iniciativa {

  private final GeradorAleatorio gerador;

  /** Cria a iniciativa com a fonte de sorteio usada nos empates. */
  public Iniciativa(GeradorAleatorio gerador) {
    this.gerador = gerador;
  }

  /** Velocidade considerando a redução causada pelo status. */
  public int velocidadeEfetiva(Pokesal pokesal) {
    double spd = pokesal.getSpd();
    if (pokesal.getStatus() == Status.PARALISADO) {
      spd *= 1.0 - pokesal.getStatus().getRedutorAtributo();
    }
    return (int) spd;
  }

  /** Indica se o primeiro PokéSal ataca antes; empates são decididos por sorteio. */
  public boolean primeiroAtacaAntes(Pokesal primeiro, Pokesal segundo) {
    int spdPrimeiro = velocidadeEfetiva(primeiro);
    int spdSegundo = velocidadeEfetiva(segundo);

    if (spdPrimeiro == spdSegundo) {
      return gerador.proximoInteiro(2) == 0;
    }
    return spdPrimeiro > spdSegundo;
  }

  /** Verifica se os dois PokéSal têm a mesma SPD efetiva, e a ordem sai de sorteio. */
  public boolean haEmpate(Pokesal primeiro, Pokesal segundo) {
    return velocidadeEfetiva(primeiro) == velocidadeEfetiva(segundo);
  }
}