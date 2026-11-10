# Requisitos Autorais

## Requisito Autoral 1 — Defensor com chance de contra-ataque

### Descrição

Funcionalidade que dá uma probabilidade aleatória de um ataque recebido por um PokéSal virar um contra-ataque instantâneo durante o combate, revidando 50% do dano que seria imposto pelo atacante.

### Regras

* **Chance definida:** 6% de probabilidade por ataque 
* **Caracterização:** É determinado de forma randômica a cada ataque através de um sorteio.
* **Impacto no ataque:** O defensor não recebe o dano que viria do ataque e contra-ataca 50% do dano que iria receber do atacante.
* **Condições e limitações:** Ocorre em qualquer ataque padrão, independentemente de tipo elemental ou terreno e mantém a mesma ordem dos turnos.

### Comportamento esperado

Quando um contra-ataque ocorre, o sistema exibe uma mensagem no console (`"Contra-ataque!"`), multiplica por 0.5 o dano final calculado que o defensor receberia e diminui do HP do Pokésal atacante.
---

## Requisito Autoral 2 — Conquista por sequência de vitórias

### Descrição

Mecânica que dá uma conquista ao jogador de acordo com o número de vitórias seguidas que ele consegue (no modo de jogo normal), além de conseguir visualizar o maior recorde no histórico de batalhas.

### Regras

* **Momento da concessão:** A conquista é concedida imediatamente após o término da batalha que alcança a meta, caso o PokéSal do jogador ganhe uma determinada sequência de batalhas no modo normal.
* **Tipo de conquista:** O treinador recebe um título de conquista: Treinador Exemplar: 3 vitórias seguidas; Super Treinador: 5 vitórias seguidas; Rei do jogo: 10 vitórias seguidas.

* **Condições e limitações:** 

### Comportamento esperado

Após a confirmação da vitória, o sistema exibe uma mensagem (`"Conquista obtida: [Título] por ganhar [nº de vitórias seguidas]"`), atualiza o histórico e retorna ao menu principal para que o usuário possa consultar o histórico ou iniciar novas batalhas. 

---
