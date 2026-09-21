# Requisitos Autorais

## Requisito Autoral 1 — Ataques com chance de acerto crítico

### Descrição

Funcionalidade que dá uma probabilidade aleatória de um ataque feito por um PokéSal causar dano crítico extra durante o combate, aumentando a efetividade do golpe.

### Regras

* **Chance definida:** 6,25% de probabilidade por ataque (1 em 16) 
* **Caracterização:** É determinado de forma randômica a cada ataque através de um sorteio.
* **Impacto no dano:** O dano calculado do ataque é multiplicado pelo fator de crítico (1.5x).
* **Condições e limitações:** Ocorre em qualquer ataque padrão, independentemente de tipo elemental ou terreno.

### Comportamento esperado

Quando um acerto crítico ocorre, o sistema exibe uma mensagem no console (`"Acerto crítico!"`), multiplica por 1.5x o dano final calculado e diminui no HP do PokéSal defensor.

---

## Requisito Autoral 2 — Recompensas por vitória

### Descrição

Mecânica que recompensa o jogador com itens de batalha aleatórios sempre que ele sai ganha uma batalha contra a CPU.

### Regras

* **Momento da concessão:** A recompensa é concedida imediatamente após o término da batalha, caso o PokéSal do jogador ganhe da CPU.
* **Tipo de recompensa:** Um item de batalha é sorteado de forma aleatória a partir do conjunto de opções disponíveis no enum de itens (`Itens.values()`, podendo ser Potion, SuperPotion ou Antidote).
* **Condições e limitações:** O item sorteado é inserido de forma automática na mochila do treinador.

### Comportamento esperado

Após a confirmação da vitória, o sistema exibe uma mensagem (`"Recompensa obtida: [Nome do Item] adicionado à mochila!"`), atualiza a lista de itens do jogador e retorna ao menu principal para que o usuário possa consultar a mochila ou iniciar novas batalhas.

---

## Requisito Autoral 3 — Histórico de batalhas

### Descrição

Sistema de histórico de batalhas que armazena o desempenho geral do jogador ao longo das partidas disputadas durante a execução da aplicação.

### Regras

* **Resultados registrados:** São contabilizados e armazenados o total de jogos disputados, a quantidade de vitórias e a quantidade de derrotas.
* **Momento do registro:** O histórico é atualizado logo ao término de cada batalha, somando +1 no contador geral e somando vitórias ou derrotas dependendo do resultado da batalha.
* **Persistência:** O histórico é atualizado durante toda a execução atual do programa, exibindo inclusive o histórico final no momento em que o usuário decide sair da aplicação.

### Comportamento esperado

Quando o usuário seleciona a opção de consultar o histórico no menu principal, o sistema exibe a quantidade total de batalhas realizadas, o número de vitórias e de derrotas acumuladas, ou informa caso nenhuma batalha tenha sido registrada até o momento.