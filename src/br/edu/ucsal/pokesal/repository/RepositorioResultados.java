package br.edu.ucsal.pokesal.repository;

import br.edu.ucsal.pokesal.model.ResultadoBatalha;
import java.util.List;

/** Guarda os resultados das batalhas. */
public interface RepositorioResultados {

  /** Salva o resultado de uma batalha. */
  void salvar(ResultadoBatalha resultado);

  /** Devolve todos os resultados salvos, do mais antigo ao mais recente. */
  List<ResultadoBatalha> listar();
}