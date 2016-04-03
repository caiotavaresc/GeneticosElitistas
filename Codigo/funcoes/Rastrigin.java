package funcoes;
import algoritmosGeneticos.Ponto;

//Classe GOLD -> Mapeia o algoritmo genético para a função Rastrigin
//Objetivo: Minimização
public class Rastrigin extends algoritmosGeneticos.AlgoritmosGeneticos{

	//Método construtor -> Chama o construtor da classe pai
	Rastrigin() 
	{
		super();
	}
	
	//Método fitness -> Função de avaliação
	//O fenótipo de todas as funções é um ponto no plano
	protected double fitness(Ponto fenotipo)
	{
		double zx, zy, z, x, y;
	
		//Atribuir valores
		x = fenotipo.x;
		y = fenotipo.y;
		
		//Representação da função passada na especificação
		zx = Math.pow(x, 2) - 10 * Math.cos(2 * Math.PI * x) + 10;
		zy = Math.pow(y, 2) - 10 * Math.cos(2 * Math.PI * y) + 10;
		
		z = zx + zy;
		
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
		
		Rastrigin rastr = new Rastrigin();
		
		//Passar os parametros
		rastr.numGenes = Integer.valueOf(args[0]);
		rastr.numIndividuos = Integer.valueOf(args[1]);
		rastr.critParada = Integer.valueOf(args[2]);
		rastr.numGeracoes = Integer.valueOf(args[3]);
		rastr.numCross = Integer.valueOf(args[4]);
		rastr.tipoCrossover = Integer.valueOf(args[5]);
		rastr.probCrossover = Double.valueOf(args[6]);
		rastr.tipoMutacao = Integer.valueOf(args[7]);
		rastr.probMutacao = Double.valueOf(args[8]);
		rastr.critTroca = Integer.valueOf(args[9]);
		rastr.elitismo = Boolean.valueOf(args[10]);
		
		//Definir o intervalo de otimizacao
		rastr.min = -5;
		rastr.max = 5;
		
		//Definir a natureza do problema
		rastr.tipoFun = MAXIMIZACAO;
		
		//Mandar evoluir
		rastr.evolucao();
	}
}
