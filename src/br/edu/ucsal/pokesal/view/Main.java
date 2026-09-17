package br.edu.ucsal.pokesal.view;

import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.enums.SalDex;


public class Main {
    public static void main(String[] args) {
        Pokesal charsal = new Pokesal(SalDex.CHARSAL);
        System.out.println(charsal.getTipoElemental());
    }
}
