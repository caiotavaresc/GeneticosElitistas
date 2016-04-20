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
public class MutacaoTroca extends Mutacao{
    
    public MutacaoTroca()
    {
        super();
    }
    
    public String toString()
    {
        return "1";
    }

    @Override
    public int[] executar(int[] mutante) {

        //Escolher dois genes
        int m1 = rand.nextInt(mutante.length);
        int m2 = rand.nextInt(mutante.length);

        //Troca os dois de posicao
        int aux = mutante[m1];
        mutante[m1] = mutante[m2];
        mutante[m2] = aux;
        
        return mutante;
    }
    
}
