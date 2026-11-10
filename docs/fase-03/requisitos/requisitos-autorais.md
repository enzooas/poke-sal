# Requisitos Autorais da Fase 03

## Requisito Autoral 1: Defensor com chance de contra-ataque

### Descrição

Funcionalidade que dá ao PokéSal defensor uma probabilidade aleatória de bloquear um
ataque recebido e revidar instantaneamente, causando ao atacante metade do dano que
ele mesmo causaria.

### Regras

* **Chance definida:** 6% por ataque.
* **Sorteio:** feito a cada ataque que acerta o alvo, por meio do `GeradorAleatorio`. Se o golpe errar pela precisão, não há sorteio de contra-ataque.
* **Ordem dos sorteios no ataque:** acerto (precisão), contra-ataque, crítico e status. O contra-ataque encerra o ataque: crítico e status não são sorteados.
* **Impacto no ataque:** o defensor não sofre nenhum dano do golpe bloqueado.
* **Dano do contra-ataque:** 50% do dano que o golpe do atacante causaria ao defensor (calculado pela mesma `CalculadoraDano`, com tipo elemental e terreno, sem crítico), truncado para int e com mínimo de 1. O dano é aplicado ao HP do atacante.
* **Condições e limitações:**
  * ocorre em qualquer ataque padrão que acerte, independentemente do tipo elemental ou do terreno;
  * o contra-ataque não pode ser crítico, não aplica status e não gera outro contra-ataque;
  * não consome o turno do defensor: a ordem dos turnos permanece a mesma;
  * se o contra-ataque derrotar o atacante, a batalha termina.

### Colaborador externo (mockável)

`GeradorAleatorio`, que decide se o contra-ataque ocorre.

### Comportamento esperado

Quando o contra-ataque ocorre, o sistema exibe a mensagem "Contra-ataque!" por meio do `RegistroBatalha`, calcula metade do dano que o defensor receberia, aplica esse dano ao PokéSal atacante e registra o HP restante. O defensor permanece com o HP inalterado.

---

## Requisito Autoral 2: Conquista por sequência de vitórias

### Descrição

Mecânica que concede ao jogador um título de conquista conforme o número de vitórias seguidas no modo de jogo normal, além de permitir consultar no histórico o maior recorde de vitórias seguidas.

### Regras

* **Escopo:** conta apenas batalhas do modo normal. Batalhas e resultados de torneio não alteram a sequência.
* **Momento da concessão:** imediatamente após o término da batalha em que a sequência atinge exatamente uma das metas.
* **Títulos:**

| Vitórias seguidas | Título |
|---|---|
| 3 | Treinador Exemplar |
| 5 | Super Treinador |
| 10 | Rei do jogo |

* **Derrota:** zera a sequência atual, mas preserva o recorde (melhor sequência).
* **Repetição:** cada título é concedido apenas uma vez por execução do programa. Sequências intermediárias (4, 6, 11...) não concedem título. Após uma derrota, se o jogador atingir novamente uma meta cujo título já foi obtido, a conquista não é concedida de novo (a sequência e o recorde continuam sendo atualizados normalmente).
* **Registro:** a sequência atual, a melhor sequência e os títulos já obtidos são acumulados em memória durante toda a execução do programa; ao encerrar, nada é persistido.

### Colaborador externo (mockável)

`NotificadorConquista`, que recebe a conquista obtida e exibe a mensagem.

### Comportamento esperado

Após a confirmação da vitória, o sistema exibe a mensagem "Conquista obtida: [Título] por ganhar [nº] vitórias seguidas", atualiza o histórico e retorna ao menu principal, onde o jogador pode consultar o histórico (sequência atual e melhor sequência) ou iniciar novas batalhas.