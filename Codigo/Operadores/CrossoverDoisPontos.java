package Operadores;


import java.util.ArrayList;
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
public class CrossoverDoisPontos extends Crossover {
    
    public CrossoverDoisPontos()
    {
        super();
    }
    
    
    @Override
    public String toString()
    {
        return "Crossover de dois pontos";
    }
    
    //Operador de cruzamento 2 -> cruzamento de dois ponto
    //Escolhe dois individuos a e b da populacao
    //Sorteia uma posicao p1 e uma p2, garante que p1 seja < p2.
    //Cria dois individuos, 
    //sendo um deles da posicao 0 a p1 igual o pai 1, da p1 ate a p2 igual o pai 2 e da p2 ate o final igual o pai 1
    //O segundo filho e o inverso, [pai2|pai1|pai2]
    //Criando dois novos individuos
    public int[][] executar(int[] pai1, int[] pai2) {

        int p1 = rand.nextInt(pai1.length - 1)+1;
        int p2 = rand.nextInt(pai1.length - 1)+1;
        while (p2 <= p1) {
            if (p2 < p1) {
                int aux = p1;
                p1 = p2;
                p2 = aux;
            } else if (p2 == p1) {
                p1 = rand.nextInt(pai1.length - 1);
                p2 = rand.nextInt(pai1.length - 1);
            }
        }

        int[] f1 = new int[pai1.length];
        int[] f2 = new int[pai1.length];

        for (int i = 0; i < p1; i++) {
            f1[i] = pai1[i];
            f2[i] = pai2[i];
        }
        for (int i = p1; i < p2; i++) {
            f1[i] = pai2[i];
            f2[i] = pai1[i];
        }
        for (int i = p2; i < pai1.length; i++) {
            f1[i] = pai1[i];
            f2[i] = pai2[i];
        }

        return new int[][]{f1, f2};
    }
    
}
