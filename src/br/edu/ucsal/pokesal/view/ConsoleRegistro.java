package br.edu.ucsal.pokesal.view;

import br.edu.ucsal.pokesal.service.RegistroBatalha;

/** Exibe os eventos da batalha no console. */
public class ConsoleRegistro implements RegistroBatalha {

  @Override
  public void inicioBatalha(String confronto, String terreno) {
    System.out.println("\n" + confronto + "!");
    System.out.println("Terreno da batalha: " + terreno);
  }

  @Override
  public void inicioRodada(int numero) {
    System.out.println("\n=== Rodada " + numero + " ===");
  }

  @Override
  public void ataque(String atacante, String defensor) {
    System.out.println(atacante + " atacou " + defensor + "!");
  }

  @Override
  public void acertoCritico() {
    System.out.println("Acerto crítico!");
  }

  @Override
  public void dano(String defensor, int dano, int hpAtual, int hpMax) {
    System.out.println(defensor + " sofreu " + dano + " de dano. HP: " + hpAtual + "/" + hpMax);
  }

  @Override
  public void efeitoStatus(String mensagem) {
    System.out.println(mensagem);
  }

  @Override
  public void efeitoTerreno(String mensagem) {
    System.out.println(mensagem);
  }

  @Override
  public void usoItem(String mensagem) {
    System.out.println(mensagem);
  }

  @Override
  public void derrotado(String nome) {
    System.out.println(nome + " foi derrotado!");
  }
}