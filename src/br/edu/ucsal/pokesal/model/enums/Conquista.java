package br.edu.ucsal.pokesal.model.enums;

import java.util.Optional;

/** Títulos concedidos ao jogador por sequências de vitórias seguidas no modo normal. */
public enum Conquista {
  TREINADOR_EXEMPLAR(3, "Treinador Exemplar"),
  SUPER_TREINADOR(5, "Super Treinador"),
  REI_DO_JOGO(10, "Rei do jogo");

  private final int vitoriasNecessarias;
  private final String titulo;

  Conquista(int vitoriasNecessarias, String titulo) {
    this.vitoriasNecessarias = vitoriasNecessarias;
    this.titulo = titulo;
  }

  public int getVitoriasNecessarias() {
    return vitoriasNecessarias;
  }

  public String getTitulo() {
    return titulo;
  }

  /** Procura a conquista cuja meta é exatamente a sequência informada.  */
  public static Optional<Conquista> daSequencia(int sequencia) {
    for (Conquista conquista : values()) {
      if (conquista.vitoriasNecessarias == sequencia) {
        return Optional.of(conquista);
      }
    }
    return Optional.empty();
  }
}
