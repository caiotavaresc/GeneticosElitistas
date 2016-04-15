package Operadores;


import java.util.List;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sousa
 */
public class CrossoverUmPonto extends Crossover {
    
    public CrossoverUmPonto()
    {
        super();
    }
    
    
    @Override
    public String toString()
    {
        return "Crossover de um ponto";
    }
    
    //Operador de cruzamento 1 -> cruzamento de um ponto
    //Escolhe dois individuos a e b da populacao
    //Sorteia uma posicao i, pega de 0 a i do a e de i ao final de b e vice-versa
    //Criando dois novos individuos
    public int[][] executar(int[] pai1, int[] pai2) {
        
        int p = rand.nextInt(pai1.length - 1)+1;

        int[] f1 = new int[pai1.length];
        int[] f2 = new int[pai1.length];

        int i;
        for (i = 0; i < p; i++) {
            f1[i] = pai1[i];
            f2[i] = pai2[i];
        }

        for (i = p; i < f1.length; i++) {
            f1[i] = pai2[i];
            f2[i] = pai1[i];
        }
        
        return new int[][]{f1, f2};
    }
}
