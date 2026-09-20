package br.edu.ucsal.pokesal.service;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.model.enums.Itens;
import br.edu.ucsal.pokesal.model.enums.Status;
import br.edu.ucsal.pokesal.model.enums.Terreno;
import br.edu.ucsal.pokesal.model.enums.TipoElemental;

public class BatalhaService {

	// TODO: valor temporário para teste, voltar para 0.0625 (6,25%)
	private static final double CHANCE_CRITICO = 0.5;
	private static final double MULTIPLICADOR_CRITICO = 1.5;

	private final Treinador treinadorJogador;
	private final Treinador treinadorCpu;
	private final Pokesal pokesalJogador;
	private final Pokesal pokesalCpu;
	private final Random random;
	private final Scanner scanner;
	private Terreno terrenoAtual;

	public BatalhaService(Treinador treinadorJogador, Treinador treinadorCpu, Random random, Scanner scanner) {
		this.treinadorJogador = treinadorJogador;
		this.treinadorCpu = treinadorCpu;
		this.pokesalJogador = treinadorJogador.criarPokesal();
		this.pokesalCpu = treinadorCpu.criarPokesal();
		this.random = random;
		this.scanner = scanner;
		Terreno[] terrenos = Terreno.values();
	    this.terrenoAtual = terrenos[random.nextInt(terrenos.length)];
	}

	public int calcularDano(Pokesal atacante, Pokesal defensor, boolean critico) {
		double danoBase = atacante.getAtk() - (defensor.getDef());

		if (danoBase < 1) {
			danoBase = 1;
		}
		
		double danoCalculado = danoBase;
		
		double multiElemental = 1.0;
		
		if (atacante.getTipoElemental() == TipoElemental.FOGO && defensor.getTipoElemental() == TipoElemental.PLANTA) {
			multiElemental = 2.0;
		} else if (atacante.getTipoElemental() == TipoElemental.FOGO && defensor.getTipoElemental() == TipoElemental.AGUA) {
			multiElemental = 0.5;
		} else if (atacante.getTipoElemental() == TipoElemental.AGUA && defensor.getTipoElemental() == TipoElemental.FOGO) {
			multiElemental = 2.0;
		} else if (atacante.getTipoElemental() == TipoElemental.AGUA && defensor.getTipoElemental() == TipoElemental.PLANTA) {
			multiElemental = 0.5;
		} else if (atacante.getTipoElemental() == TipoElemental.PLANTA && defensor.getTipoElemental() == TipoElemental.AGUA) {
			multiElemental = 2.0;
		} else if (atacante.getTipoElemental() == TipoElemental.PLANTA && defensor.getTipoElemental() == TipoElemental.FOGO) {
			multiElemental = 0.5;
		}
		danoCalculado *= multiElemental;
		
		if (terrenoAtual != null && atacante.getTipoElemental() != TipoElemental.PLANTA) {
		    if (terrenoAtual.getTipoElemental() == atacante.getTipoElemental()) {
		        danoCalculado *= (1.0 + terrenoAtual.getPorcentagem());
		    }
		}
		
		if (critico) {
			danoCalculado *= MULTIPLICADOR_CRITICO;
		}

		int danoFinal = (int) danoCalculado;
		
		if (danoFinal < 1) {
		    danoFinal = 1;
		}
		
		return danoFinal;
	}

	public boolean sortearCritico() {
		return random.nextDouble() < CHANCE_CRITICO;
	}

	public void atacar(Pokesal atacante, Pokesal defensor) {
		boolean critico = sortearCritico();

		if (critico) {
			System.out.println("Acerto crítico!");
		}

		int dano = calcularDano(atacante, defensor, critico);
		int danoSofrido = defensor.receberDano(dano);
		System.out.println(nomeComDono(atacante) + " atacou e causou " + danoSofrido + " de dano em "
				+ nomeComDono(defensor) + ". HP: " + defensor.getHpAtual() + "/" + defensor.getHpMax());

		if (!defensor.estaDerrotado() && defensor.getStatus() == Status.NORMAL && random.nextDouble() < 0.15) {
			if (atacante.getTipoElemental() == TipoElemental.FOGO) {
				defensor.setStatus(Status.QUEIMADO);
				System.out.println(nomeComDono(defensor) + " foi queimado!");
			} else if (atacante.getTipoElemental() == TipoElemental.PLANTA) {
				defensor.setStatus(Status.ENVENENADO);
				System.out.println(nomeComDono(defensor) + " foi envenenado!");
			} else if (atacante.getTipoElemental() == TipoElemental.AGUA) {
				defensor.setStatus(Status.PARALISADO);
				System.out.println(nomeComDono(defensor) + " foi paralisado!");
			}
		}
		
		if (defensor.estaDerrotado()) {
			System.out.println(nomeComDono(defensor) + " foi derrotado!");
		}
	}

	public void executarTurnoJogador() {
	    boolean acaoConcluida = false;

	    while (!acaoConcluida) {
	        System.out.println("\n--- Seu Turno ---");
	        System.out.println("1 - Atacar");
	        System.out.println("2 - Usar Item");

	        int acao = lerOpcao(1, 2);

	        if (acao == 1) {
	            System.out.println(nomeComDono(pokesalJogador) + " escolheu atacar!");
	            atacar(pokesalJogador, pokesalCpu);
	            acaoConcluida = true; 
	        } else {
	            if (!treinadorJogador.getMochila().podeUsarItem()) {
	                System.out.println("Limite de 2 itens por batalha atingido ou mochila vazia! Escolha outra ação.");
	                continue; 
	            }

	            Itens itemEscolhido = escolherItemNaMochila();
	            if (itemEscolhido != null) {
	                aplicarItem(treinadorJogador, pokesalJogador, itemEscolhido);
	                acaoConcluida = true; 
	            } else {
	                System.out.println("Retornando ao menu do turno...");
	            }
	        }
	    }
	}

	public void executarTurnoCpu() {
		System.out.println("\n--- Turno da CPU ---");
		System.out.println(nomeComDono(pokesalCpu) + " escolheu atacar!");
		atacar(pokesalCpu, pokesalJogador);
	}

	private Itens escolherItemNaMochila() {
		List<Itens> itens = treinadorJogador.getMochila().getItens();
		
		System.out.println("\n===== ITENS NA MOCHILA =====");
		for (int i = 0; i < itens.size(); i++) {
			System.out.println((i + 1) + " - " + itens.get(i).getNome());
		}
		System.out.println("0 - Voltar/Atacar");

		int escolha = lerOpcao(0, itens.size());
		if (escolha == 0)
			return null;

		Itens itemSelecionado = itens.get(escolha - 1);

		if (!treinadorJogador.getMochila().validarUsoItem(itemSelecionado, pokesalJogador)) {
			System.out.println("Condição inválida para usar este item! Tente outra ação.");
			return null;
		}

		return itemSelecionado;
	}

	private void aplicarItem(Treinador treinador, Pokesal pokesal, Itens item) {
		treinador.getMochila().registrarUso(item);
		System.out.println(treinador.getNome() + " usou " + item.getNome() + "!");

		if (item == Itens.POTION || item == Itens.SUPERPOTION) {
			int curaEfetiva = pokesal.curar(item.getValorCura());
			System.out.println(pokesal.getNomePK() + " recuperou " + curaEfetiva + " de HP! HP atual: "
					+ pokesal.getHpAtual() + "/" + pokesal.getHpMax());
		} else if (item == Itens.ANTIDOTE) {
			pokesal.setStatus(Status.NORMAL);
			System.out.println(pokesal.getNomePK() + " foi curado do envenenamento!");
		}
	}
	
	private void processarFimDeTurno(Pokesal pokesal) {
	    if (pokesal.estaDerrotado()) return;

	    if (terrenoAtual != null && pokesal.getTipoElemental() == TipoElemental.PLANTA 
	        && terrenoAtual == Terreno.CANTEIRO_CENTRAL) {
	        
	        int curaTerreno = (int) (pokesal.getHpMax() * terrenoAtual.getPorcentagem());
	        int curado = pokesal.curar(curaTerreno);
	        if (curado > 0) {
	            System.out.println(nomeComDono(pokesal) + " recuperou " + curado + " de HP pelo Canteiro Central!");
	        }
	    }

	    Status statusAtual = pokesal.getStatus();
	    if (statusAtual.getDanoPorTurno() > 0.0) {
	        
	        int danoStatus = (int) (pokesal.getHpMax() * statusAtual.getDanoPorTurno());
	        
	        if (danoStatus < 1) {
	            danoStatus = 1;
	        }

	        pokesal.receberDano(danoStatus);
	        System.out.println(nomeComDono(pokesal) + " sofreu " + danoStatus + " de dano devido ao status " + statusAtual + "!");
	    }
	}

	public Treinador iniciarBatalha() {
		treinadorJogador.getMochila().resetarUsoBatalha();
	    treinadorCpu.getMochila().resetarUsoBatalha();
		
		System.out.println("\n" + nomeComDono(pokesalJogador) + " contra " + nomeComDono(pokesalCpu) + "!");
		
		if (terrenoAtual != null) {
			System.out.println("Terreno da batalha: " + terrenoAtual);
		}
		
		int rodada = 1;

		while (!pokesalJogador.estaDerrotado() && !pokesalCpu.estaDerrotado()) {
			System.out.println("\n=== Rodada " + rodada + " ===");

			boolean jogadorComeca = pokesalJogador.getSpd() >= pokesalCpu.getSpd();
			if (pokesalJogador.getSpd() == pokesalCpu.getSpd()) {
				jogadorComeca = random.nextBoolean();
			}

			if (jogadorComeca) {
				executarTurnoJogador();
				if (pokesalCpu.estaDerrotado()) {
					break;
				}	
				executarTurnoCpu();
			} else {
				executarTurnoCpu();
				if (pokesalJogador.estaDerrotado()) {
					break;
				}
				executarTurnoJogador();
			}
			
			processarFimDeTurno(pokesalJogador);
			if (pokesalCpu.estaDerrotado() || pokesalJogador.estaDerrotado()) {
				break;
			}
			
			processarFimDeTurno(pokesalCpu);
			if (pokesalCpu.estaDerrotado() || pokesalJogador.estaDerrotado()) {
				break;
			}

			rodada++;
		}

		if (pokesalJogador.estaDerrotado()) {
			return treinadorCpu;
		}
		return treinadorJogador;
	}

	private String nomeComDono(Pokesal pokesal) {
		Treinador dono;
        if (pokesal == pokesalJogador) {
            dono = treinadorJogador;
        } else {
            dono = treinadorCpu;
        }
        return pokesal.getNomePK() + " (" + dono.getNome() + ")";
	}

	private int lerOpcao(int min, int max) {
		while (true) {
			System.out.print("Escolha uma opção (" + min + " a " + max + "): ");
			if (scanner.hasNextInt()) {
				int num = scanner.nextInt();
				if (num >= min && num <= max) {
					return num;
				}
			} else {
				scanner.next();
			}
			System.out.println("Opção inválida.");
		}
	}
}