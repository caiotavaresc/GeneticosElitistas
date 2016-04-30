
package funcoes;

import Operadores.CrossoverDoisPontos;
import Operadores.CrossoverUmPonto;
import Operadores.MutacaoSimples;
import Operadores.MutacaoTroca;
import algoritmoGenetico.Utils;

public class Bump2 extends algoritmoGenetico.AlgoritmosGeneticos{

    /*-----------------|
    Recebe como parametro o genotipo e retorna o fitness desse individuo
    |-----------------*/
    public double fitness(int [] gen) {
        
        double x, y, z, temp0, temp1, temp2;
        
        //Obtendo o fenotipo
        double xy[] = Utils.binarioPraDecimal(gen, min, max);
        x = xy[0];
        y = xy[1];
        
        if(x*y< 0.75)
            z = 0;
        else if((x+y)>15)
            z = 0;
        else
        {
            temp0 = Math.pow(Math.cos(x), 4)+Math.pow(Math.cos(y), 4);
            temp1 = 2*Math.cos(Math.pow(x, 2))*Math.cos(Math.pow(y, 2));
            temp2 = Math.sqrt(Math.pow(x, 2) +2*Math.pow(y, 2));
            z = Math.abs((temp0-temp1)/temp2);
            z = -z;
        }
        
        return z;
    }
    
    public static void main(String[] args) {
		
		Bump2 bmp = new Bump2();
                
                //Preencher os parametros
		bmp.numGenes = Integer.valueOf(args[0]);
		bmp.numIndividuos = Integer.valueOf(args[1]);
		bmp.critParada = Integer.valueOf(args[2]);
		bmp.numGeracoes = Integer.valueOf(args[3]);
		bmp.numCross = (int)(Double.valueOf(args[4])*bmp.numIndividuos);
		bmp.crossover = Integer.valueOf(args[5]) == 0? new CrossoverUmPonto() : new CrossoverDoisPontos();
		bmp.probCrossover = Double.valueOf(args[6]);
		bmp.mutacao = Integer.valueOf(args[7]) == 0 ? new MutacaoSimples() : new MutacaoTroca();
		bmp.probMutacao = Double.valueOf(args[8]);
		bmp.critTroca = Integer.valueOf(args[9]);
		bmp.elitismo = Boolean.valueOf(args[10]);
                bmp.intervaloImpressao = Integer.parseInt(args[11]);
		
		//Definir o intervalo de otimizacao
		bmp.min = 0;
		bmp.max = 10;
		
		//Definir a natureza do problema
		bmp.tipoFun = MINIMIZACAO;
		
		//Mandar evoluir
		bmp.evolucao();
	}   
}
