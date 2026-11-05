package br.edu.ucsal.pokesal.repository;

import br.edu.ucsal.pokesal.model.ResultadoBatalha;
import java.util.ArrayList;
import java.util.List;

public class RepositorioResultadosMemoria implements RepositorioResultados {

  private final List<ResultadoBatalha> resultados = new ArrayList<>();

  @Override
  public void salvar(ResultadoBatalha resultado) {
    resultados.add(resultado);
  }

  @Override
  public List<ResultadoBatalha> listar() {
    return List.copyOf(resultados);
  }
}
