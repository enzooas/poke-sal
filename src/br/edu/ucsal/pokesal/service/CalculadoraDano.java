package br.edu.ucsal.pokesal.service;

import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.enums.Status;
import br.edu.ucsal.pokesal.model.enums.Terreno;

/** Calcula o dano de um ataque combinando status, tipo, terreno e crítico. */
public class CalculadoraDano {

  private static final double MULTIPLICADOR_CRITICO = 1.5;
  private static final int DANO_MINIMO = 1;

  /** Calcula o dano final de um ataque, sempre de pelo menos o dano mínimo. */
  public int calcular(Pokesal atacante, Pokesal defensor, Terreno terreno, boolean critico) {
    double atkEfetivo = atacante.getAtk();
    if (atacante.getStatus() == Status.QUEIMADO) {
      atkEfetivo *= 1.0 - atacante.getStatus().getRedutorAtributo();
    }

    double dano = Math.max(atkEfetivo - defensor.getDef(), DANO_MINIMO);
    dano *= atacante.getTipoElemental().multiplicadorContra(defensor.getTipoElemental());
    dano *= 1.0 + terreno.bonusDeDano(atacante.getTipoElemental());

    if (critico) {
      dano *= MULTIPLICADOR_CRITICO;
    }

    int danoFinal = (int) dano;
    if (danoFinal < DANO_MINIMO) {
      danoFinal = DANO_MINIMO;
    }
    return danoFinal;
  }
}