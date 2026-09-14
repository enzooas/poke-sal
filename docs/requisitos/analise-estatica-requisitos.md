# Relatório de Inspeção e Revisão dos Requisitos


## Objetivo da Inspeção

A equipe analisou os requisitos antes da implementação do projeto, a fim de identificar possíveis ambiguidades, omissões ou contradições que viessem a comprometer a codificação do sistema.

## Resultados da Inspeção

| ID | Requisito | Trecho analisado | Classificação | Problema identificado |
|---|---|---|---|---|
| RQ-01 | Seleção do Inicial | "...cada treinador deve escolher exclusivamente um pokesal inicial (BulbaSal, CharSal ou SquirtSal) ou (ChikoSal, CyndaSal ou TotoSal)." | Ambiguidade | Não fica claro se o treinador deve escolher 1 entre os 6, ou se primeiro escolhe um dos dois grupos, e depois escolhe entre os 3 pokesal. |
| RQ-02 | Efeito de Terreno | "Mecânica do Estacionamento da UCSal (Efeito de Terreno)" | Omissão | Não especifica como e quando o terreno é escolhido e se podem coexistir simultaneamente. |
| RQ-03 | Precisão ou Dano | "Poça de Chuva / Piso Escorregadio: Golpes de Água aplicam 10% adicionais de precisão ou dano." | Ambiguidade | Não fica claro como é escolhido o aumento de precisão ou dano. |
| RQ-04 | Fim de um turno | "Canteiro Central: pokesal do tipo Planta recuperam 5% do HP máximo ao final de cada turno." | Omissão | Não especifica como se dá a definição e o fim de um turno. |
| RQ-05 | Regra de Desempate | "A ordem de ataque do turno é determinada estritamente pelo atributo SPD (Velocidade)." | Omissão | Não especifica o que acontece em caso de mesmo valor de SPD (Velocidade). |
| RQ-06 | Efeitos de Status | "Aplicação de Efeitos de Status no final do turno (ex: Queimado reduz HP e ATK; Envenenado tira dano progressivo; Paralisado reduz SPD)." | Omissão | Não especifica o quanto cada efeito reduz os atributos. |
| RQ-07 | Itens de Batalha | "Cada treinador pode usar no máximo 2 itens por batalha (ex: Potion, Super Potion, Antidote)." | Omissão | Não especifica o impacto que cada item tem na batalha. |
| RQ-08 | Consumo de turno | "Usar um item consome o turno do treinador." | Ambiguidade | Não fica claro de que forma o uso do item consome o turno do treinador. |


## Conclusão

A análise estática realizada identificou ambiguidades e omissões nos requisitos do projeto que podem dificultar a implementação e testes do sistema. Os principais pontos identificados estão relacionados às regras de seleção dos Pokésal, aos efeitos de terreno, à ordem dos turnos, aos efeitos de status e ao funcionamento dos itens de batalha.