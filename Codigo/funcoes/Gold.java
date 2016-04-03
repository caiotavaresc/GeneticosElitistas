package funcoes;
import algoritmosGeneticos.Ponto;

//Classe GOLD -> Mapeia o algoritmo genético para a função Gold
//Objetivo: Minimização
public class Gold extends algoritmosGeneticos.AlgoritmosGeneticos{

	//Método construtor -> Chama o construtor da classe pai
	Gold() 
	{
		super();
	}
	
	//Método fitness -> Função de avaliação
	//O fenótipo de todas as funções é um ponto no plano
	//Na função GOLD temos que mapear os domínios positivo e negativo. A função resultará apenas no domínio positivo
	//O domínio negativo (-z) deve ser tratado no código
	protected double fitness(Ponto fenotipo)
	{
		double a, b, z, x, y;
	
		//Atribuir valores
		x = fenotipo.x;
		y = fenotipo.y;
		
		//Representação da função passada na especificação
		a = 1 + Math.pow((x + y + 1), 2) * (19-14*Math.pow(x,2) - 14*y + 6*x*y + 3*Math.pow(y,2));
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
     * 4 -> Tipo de Crossover - 0 [Um ponto] 1 [Dois pontos]
     * 5 -> Probabilidade de crossover - entre 0.00 e 1.00
     * 6 -> Tipo de Mutacao
     * 7 -> Probabilidade de Mutacao - entra 0.00 e 1.00
     * 8 -> Criterio de Selecao
     * 9 -> Elitismo - 0 [não] 1 [sim]*/
	public static void main(String[] args) {
		
		Gold gold = new Gold();
		
		//Passar os parametros
		gold.numGenes = Integer.valueOf(args[0]);
		gold.numIndividuos = Integer.valueOf(args[1]);
		gold.critParada = Integer.valueOf(args[2]);
		gold.numGeracoes = Integer.valueOf(args[3]);
		gold.tipoCrossover = Integer.valueOf(args[4]);
		gold.probCrossover = Double.valueOf(args[5]);
		gold.tipoMutacao = Integer.valueOf(args[6]);
		gold.probMutacao = Double.valueOf(args[7]);
		gold.critSelecao = Integer.valueOf(args[8]);
		gold.elitismo = Boolean.valueOf(args[9]);
		
		//Mandar evoluir
		gold.evolucao();
	}
}
