package funcoes;
import Operadores.CrossoverDoisPontos;
import Operadores.CrossoverUmPonto;
import Operadores.MutacaoSimples;
import Operadores.MutacaoTroca;
import algoritmoGenetico.Utils;

/*-----------------|
//Classe Gold2 (z) -> Mapeia o algoritmo genético para a função Gold
//Objetivo: Minimização
|-----------------*/
public class Gold2 extends algoritmoGenetico.AlgoritmosGeneticos{

	Gold2() 
	{
		super();
	}
	
        /*-----------------|
        //Calculo do fitness especifico da funcao gold
        |-----------------*/
	protected double fitness(int[] gen)
	{
		double a, b, z, x, y;
                double[] d = Utils.binarioPraDecimal(gen, min, max);
		//Atribuir valores
		x = d[0];
		y = d[1];
		
		//Representação da função passada na especificação
		a = 1 + Math.pow((x + y + 1), 2) * (19 - 14 * x + 3 * Math.pow(x,2) - 14*y + 6*x*y + 3*Math.pow(y,2));
		b = 30 + Math.pow((2*x - 3*y), 2) * (18 - 32*x + 12*Math.pow(x,2) + 48*y -36*x*y + 27*Math.pow(y,2));
		z = a*b;
		
		return z;
	}
	
    //Metodo main: Executa a evolucao -- Ocorre por subclasse
    
    /*Parametros
     * 0 -> Num de cromossomos por genotipo
     * 1 -> Num Individuos por geracao
     * 2 -> Criterio de Parada - 0 [Convergencia] 1 [Num Geracoes]
     * 3 -> Numero de Geracoes (So sera usado se o criterio de parada for 1)
     * 4 -> Numero de Crossovers que ocorrerao por geracao
     * 5 -> Tipo de Crossover - 0 [Um ponto] 1 [Dois pontos]
     * 6 -> Probabilidade de crossover - entre 0.00 e 1.00
     * 7 -> Tipo de Mutacao
     * 8 -> Probabilidade de Mutacao - entra 0.00 e 1.00
     * 9 -> Criterio de Troca de Populacao - 0 [com troca] 1 [sem troca]
     * 10 -> Elitismo - 0 [não] 1 [sim]*/
	public static void main(String[] args) {
		
		Gold2 gold = new Gold2();
                      
		gold.numGenes = Integer.valueOf(args[0]);
		gold.numIndividuos = Integer.valueOf(args[1]);
		gold.critParada = Integer.valueOf(args[2]);
		gold.numGeracoes = Integer.valueOf(args[3]);
		gold.numCross = (int)(Double.valueOf(args[4])*gold.numIndividuos);
		gold.crossover = Integer.valueOf(args[5]) == 0? new CrossoverUmPonto() : new CrossoverDoisPontos();
		gold.probCrossover = Double.valueOf(args[6]);
		gold.mutacao = Integer.valueOf(args[7]) == 0 ? new MutacaoSimples() : new MutacaoTroca();
		gold.probMutacao = Double.valueOf(args[8]);
		gold.critTroca = Integer.valueOf(args[9]);
		gold.elitismo = Boolean.valueOf(args[10]);
                gold.intervaloImpressao = Integer.parseInt(args[11]);
		
		//Definir o intervalo de otimizacao
		gold.min = -2;
		gold.max = 2;
		
		//Definir a natureza do problema
		gold.tipoFun = MINIMIZACAO;
		
		//Mandar evoluir
		gold.evolucao();
	}
}
