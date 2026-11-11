package br.edu.ucsal.pokesal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.edu.ucsal.pokesal.model.Mochila;
import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.model.enums.SalDex;
import br.edu.ucsal.pokesal.model.enums.Terreno;
import br.edu.ucsal.pokesal.repository.RepositorioResultados;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testes de precisão, crítico e contra-ataque na batalha.
 *
 * <p>O gerador, o registro, o serviço de terreno e o repositório são mocks. O {@code
 * BatalhaService} é montado à mão, e não com {@code @InjectMocks}, porque o construtor recebe dois
 * {@code Treinador} reais (o Mockito não sabe qual vai em cada posição) e uma {@code
 * CalculadoraDano} real, que é a regra que queremos exercitar junto.
 *
 * <p>Matchers x valores exatos: usamos valor exato quando o argumento é o que o teste prova (o
 * limite 100 do sorteio de precisão, o dano 4 do contra-ataque). Usamos {@code any()} e {@code
 * anyString()} quando o argumento não importa para a regra testada (nomes formatados para a tela,
 * HP restante). Quando um argumento usa matcher, todos precisam usar, por isso aparece {@code
 * eq(4)} misturado com {@code anyString()}.
 */
@ExtendWith(MockitoExtension.class)
public class BatalhaServiceCombateTest {

  /** Limite passado ao gerador no sorteio da precisão (devolve de 0 a 99). */
  private static final int LIMITE_PRECISAO = 100;

  @Mock
  private GeradorAleatorio gerador;

  @Mock
  private RegistroBatalha registro;

  @Mock
  private ServicoTerreno servicoTerreno;

  @Mock
  private RepositorioResultados repositorio;

  /** Monta a batalha com os mocks, liberando só o Canteiro Central (sem bônus de dano). */
  private BatalhaService criarBatalha(SalDex jogador, SalDex cpu) {
    when(servicoTerreno.terrenosDisponiveis()).thenReturn(List.of(Terreno.CANTEIRO_CENTRAL));
    // com um único terreno disponível, o sorteio é proximoInteiro(1), que só pode dar 0
    when(gerador.proximoInteiro(1)).thenReturn(0);
    EscolhaDeAcao semEscolha = (treinador, pokesal) -> null;
    return new BatalhaService(
        new Treinador("J1", jogador, new Mochila(new ArrayList<>())),
        new Treinador("J2", cpu, new Mochila(new ArrayList<>())),
        gerador, new CalculadoraDano(), new Iniciativa(gerador),
        semEscolha, semEscolha, registro, repositorio, servicoTerreno);
  }

  private BatalhaService criarBatalha() {
    return criarBatalha(SalDex.CHARSAL, SalDex.CHARSAL);
  }

  @Test
  public void testGolpeErraQuandoPrecisaoFalha() {
    BatalhaService batalha = criarBatalha();
    // 99 erra qualquer golpe da SalDex (a maior precisão é 95)
    when(gerador.proximoInteiro(LIMITE_PRECISAO)).thenReturn(99);
    Pokesal atacante = new Pokesal(SalDex.CHARSAL);
    Pokesal defensor = new Pokesal(SalDex.CHARSAL);

    batalha.atacar(atacante, defensor);

    assertEquals(defensor.getHpMax(), defensor.getHpAtual(), "HP do alvo continua inalterado");
    assertEquals(atacante.getHpMax(), atacante.getHpAtual(), "Atacante não sofre dano");
    verify(registro).golpeErrou(anyString(), anyString());
    verify(registro, never()).dano(anyString(), anyInt(), anyInt(), anyInt());
    // golpe que erra encerra o ataque: contra-ataque, crítico e status nem são sorteados
    verify(gerador, never()).proximoDecimal();
  }

  @Test
  public void testAcertoCriticoComGeradorMockado() {
    BatalhaService batalha = criarBatalha();
    when(gerador.proximoInteiro(LIMITE_PRECISAO)).thenReturn(0);
    // retornos em sequência: contra-ataque 0.5 (não ativa), crítico 0.0 (dentro dos 6,25%),
    // status 0.99 (não aplica)
    when(gerador.proximoDecimal()).thenReturn(0.5, 0.0, 0.99);
    Pokesal atacante = new Pokesal(SalDex.CHARSAL);
    Pokesal defensor = new Pokesal(SalDex.CHARSAL);
    ArgumentCaptor<Integer> danoEnviado = ArgumentCaptor.forClass(Integer.class);

    batalha.atacar(atacante, defensor);

    verify(registro).acertoCritico();
    verify(registro).dano(anyString(), danoEnviado.capture(), anyInt(), anyInt());
    assertEquals(13, danoEnviado.getValue(),
        "Crítico multiplica o dano por 1.5 (9 x 1,5 = 13,5, truncado para 13)");
    assertEquals(defensor.getHpMax() - 13, defensor.getHpAtual());
    verify(gerador, times(3)).proximoDecimal();
    verify(servicoTerreno, atLeastOnce()).terrenosDisponiveis();
  }

  /** Teste 1 do Contra-Ataque (ata): o defensor bloqueia e devolve 50% do dano. */
  @Test
  public void testContraAtaqueBloqueiaGolpeEDevolveMetadeDoDano() {
    BatalhaService batalha = criarBatalha();
    when(gerador.proximoInteiro(LIMITE_PRECISAO)).thenReturn(0);
    when(gerador.proximoDecimal()).thenReturn(0.0);
    Pokesal atacante = new Pokesal(SalDex.CHARSAL);
    Pokesal defensor = new Pokesal(SalDex.CHARSAL);

    batalha.atacar(atacante, defensor);

    assertEquals(defensor.getHpMax(), defensor.getHpAtual(), "Defensor bloqueia e não sofre dano");
    assertEquals(atacante.getHpMax() - 4, atacante.getHpAtual(),
        "Atacante sofre 50% do golpe que o defensor receberia (9 / 2 = 4)");

    InOrder ordem = inOrder(registro);
    ordem.verify(registro).ataque(anyString(), anyString());
    ordem.verify(registro).contraAtaque(anyString(), anyString());
    ordem.verify(registro).dano(anyString(), eq(4), anyInt(), anyInt());
    verify(registro, never()).acertoCritico();
    verify(registro, never()).golpeErrou(any(), any());
  }

  /** Teste 2 do Contra-Ataque (ata): o revide nunca fica abaixo de 1. */
  @Test
  public void testContraAtaqueTemDanoMinimoDeUm() {
    // CharSal (fogo, ATK 52) contra SquirtSal (água, DEF 65): dano 1, metade seria 0
    BatalhaService batalha = criarBatalha(SalDex.CHARSAL, SalDex.SQUIRTSAL);

    assertEquals(1, batalha.calcularDanoContraAtaque(
        new Pokesal(SalDex.CHARSAL), new Pokesal(SalDex.SQUIRTSAL)),
        "Contra-ataque nunca causa menos que 1 de dano");
  }
}
