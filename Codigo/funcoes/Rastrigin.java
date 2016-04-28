package funcoes;
import Operadores.CrossoverDoisPontos;
import Operadores.CrossoverUmPonto;
import Operadores.MutacaoSimples;
import Operadores.MutacaoTroca;
import algoritmosGeneticos.Utils;

//Classe RASTRIGIN -> Mapeia o algoritmo gen�tico para a fun��o Rastrigin
//Objetivo: Maximiza��o
public class Rastrigin extends algoritmosGeneticos.AlgoritmosGeneticos{

	//M�todo construtor -> Chama o construtor da classe pai
	Rastrigin() 
	{
		super();
	}
	
	//M�todo fitness -> Fun��o de avalia��o
	//O fen�tipo de todas as fun��es � um ponto no plano
	protected double fitness(int[] gen)
	{
		double zx, zy, z, x, y;
                double[] xy = Utils.binarioPraDecimal(gen, min, max);
		//Atribuir valores
		x = xy[0];
		y = xy[1];
		
		//Representa��o da fun��o passada na especifica��o
		zx = Math.pow(x, 2) - 10 * Math.cos(2 * Math.PI * x) + 10;
		zy = Math.pow(y, 2) - 10 * Math.cos(2 * Math.PI * y) + 10;
		
		z = zx + zy;
		z = z * -1;
		
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
     * 10 -> Elitismo - 0 [n�o] 1 [sim]*/
	public static void main(String[] args) {
		
		Rastrigin rastr = new Rastrigin();
		
		//Passar os parametros
		rastr.numGenes = Integer.valueOf(args[0]);
		rastr.numIndividuos = Integer.valueOf(args[1]);
		rastr.critParada = Integer.valueOf(args[2]);
		rastr.numGeracoes = Integer.valueOf(args[3]);
		rastr.numCross = (int)(Double.valueOf(args[4])*rastr.numIndividuos);
		rastr.crossover = Integer.valueOf(args[5]) == 0? new CrossoverUmPonto() : new CrossoverDoisPontos();
		rastr.probCrossover = Double.valueOf(args[6]);
		rastr.mutacao = Integer.valueOf(args[7]) == 0 ? new MutacaoSimples() : new MutacaoTroca();
		rastr.probMutacao = Double.valueOf(args[8]);
		rastr.critTroca = Integer.valueOf(args[9]);
		rastr.elitismo = Boolean.valueOf(args[10]);
                rastr.intervaloImpressao = Integer.parseInt(args[11]);
		
		//Definir o intervalo de otimizacao
		rastr.min = -5;
		rastr.max = 5;
		
		//Definir a natureza do problema
		rastr.tipoFun = MAXIMIZACAO;
		
		//Mandar evoluir
		rastr.evolucao();
	}
}
