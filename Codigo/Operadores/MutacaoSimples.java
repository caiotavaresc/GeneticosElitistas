package Operadores;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sousa
 */
public class MutacaoSimples extends Mutacao{
    
    public MutacaoSimples()
    {
        super();
    }
    
    public String toString()
    {
        return "0";
    }

    @Override
    public int[] executar(int[] mutante) {

        //Escolher um gene
        int m = rand.nextInt(mutante.length);
        //Escolher um elemento do alfabeto
        mutante[m] = rand.nextInt(2);
        //retorna o genotipo pos mutacao
        return mutante;
    }
    
    
}
