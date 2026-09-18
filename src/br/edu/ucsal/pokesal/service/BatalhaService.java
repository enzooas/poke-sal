package br.edu.ucsal.pokesal.service;

import java.util.Random;

import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.Treinador;

public class BatalhaService {

    // TODO: valor temporário para teste, voltar para 0.0625 (6,25%)
    private static final double CHANCE_CRITICO = 0.5;
    private static final double MULTIPLICADOR_CRITICO = 1.5;

    private final Treinador treinadorJogador;
    private final Treinador treinadorCpu;
    private final Pokesal pokesalJogador;
    private final Pokesal pokesalCpu;
    private final Random random;

    public BatalhaService(Treinador treinadorJogador, Treinador treinadorCpu, Random random) {
        this.treinadorJogador = treinadorJogador;
        this.treinadorCpu = treinadorCpu;
        this.pokesalJogador = treinadorJogador.criarPokesal();
        this.pokesalCpu = treinadorCpu.criarPokesal();
        this.random = random;
    }

    public int calcularDano(Pokesal atacante, Pokesal defensor, boolean critico) {
        double dano = atacante.getAtk() - (defensor.getDef() / 2);

        if (critico) {
            dano = dano * MULTIPLICADOR_CRITICO;
        }

        int danoFinal = (int) Math.round(dano);
        if (danoFinal < 1) {
            danoFinal = 1;
        }
        return danoFinal;
    }

    public boolean sortearCritico() {
        return random.nextDouble() < CHANCE_CRITICO;
    }

    public Pokesal definirQuemAtacaPrimeiro() {
        if (pokesalJogador.getSpd() > pokesalCpu.getSpd()) {
            return pokesalJogador;
        }
        if (pokesalCpu.getSpd() > pokesalJogador.getSpd()) {
            return pokesalCpu;
        }
        if (random.nextBoolean()) {
            return pokesalJogador;
        }
        return pokesalCpu;
    }

    public void atacar(Pokesal atacante, Pokesal defensor) {
        boolean critico = sortearCritico();
        if (critico) {
            System.out.println("Acerto crítico!");
        }

        int dano = calcularDano(atacante, defensor, critico);
        int danoSofrido = defensor.receberDano(dano);
        System.out.println(nomeComDono(atacante) + " causou " + danoSofrido + " de dano em "
                + nomeComDono(defensor) + ". HP: " + defensor.getHpAtual() + "/" + defensor.getHpMax());

        if (defensor.estaDerrotado()) {
            System.out.println(nomeComDono(defensor) + " foi derrotado!");
        }
    }

    public void executarRodada() {
        Pokesal primeiro = definirQuemAtacaPrimeiro();
        Pokesal segundo;
        if (primeiro == pokesalJogador) {
            segundo = pokesalCpu;
        } else {
            segundo = pokesalJogador;
        }

        atacar(primeiro, segundo);

        if (!segundo.estaDerrotado()) {
            atacar(segundo, primeiro);
        }
    }

    public Treinador iniciarBatalha() {
        System.out.println("\n" + nomeComDono(pokesalJogador) + " contra " + nomeComDono(pokesalCpu) + "!");
        int rodada = 1;

        while (!pokesalJogador.estaDerrotado() && !pokesalCpu.estaDerrotado()) {
            System.out.println("\n--- Rodada " + rodada + " ---");
            executarRodada();
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

}
