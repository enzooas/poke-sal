package br.edu.ucsal.pokesal.model;

/** Resultado de uma batalha, guardado ao seu término. */
public class ResultadoBatalha {

  private final String vencedor;
  private final String perdedor;
  private final int turnos;
  private final String pokesalVencedor;
  private final String pokesalPerdedor;

  /** Cria o resultado com os dados finais da batalha. */
  public ResultadoBatalha(String vencedor, String perdedor, int turnos,
      String pokesalVencedor, String pokesalPerdedor) {
    this.vencedor = vencedor;
    this.perdedor = perdedor;
    this.turnos = turnos;
    this.pokesalVencedor = pokesalVencedor;
    this.pokesalPerdedor = pokesalPerdedor;
  }

  public String getVencedor() {
	return vencedor;
  }

  public String getPerdedor() {
	return perdedor;
  }

  public int getTurnos() {
	return turnos;
  }

  public String getPokesalVencedor() {
	return pokesalVencedor;
  }

  public String getPokesalPerdedor() {
	return pokesalPerdedor;
  }
}