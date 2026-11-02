package br.edu.ucsal.pokesal.service;

import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.model.enums.Itens;

/** Concede ao treinador um item sorteado como recompensa de vitória. */
public class RecompensaService {

  private final GeradorAleatorio gerador;

  /** Cria o serviço com a fonte de sorteio usada na escolha do item. */
  public RecompensaService(GeradorAleatorio gerador) {
    this.gerador = gerador;
  }

  /** Sorteia um item, guarda na mochila do treinador e devolve o item concedido. */
  public Itens concederRecompensa(Treinador treinador) {
    Itens[] itens = Itens.values();
    Itens sorteado = itens[gerador.proximoInteiro(itens.length)];
    treinador.getMochila().adicionarItem(sorteado);
    return sorteado;
  }
}