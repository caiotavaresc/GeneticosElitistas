package algoritmosGeneticos;

/* REQUISITOS BASICOS DO TRABALHO
uso de dois diferentes tamanhos de populacao, de grandezes bem diferentes; 
dois criterios de parada;
para a parte I - cromossomo com codificacao binaria; -> FEITO E NAO TESTADO
um operador de selecao (preferencialmente roleta); -> FEITO E NAO TESTADO
dois operadores de crossover (crossover de um ponto e outro a escolha do grupo); -> FEITO E NAO TESTADO
dois operadores de mutacao (mutacao simples e outro a escolha do grupo); -> 1/2 E NAO TESTADO
dois criterios de troca de populacao;
evolucao sem eletismo e com elitismo.
*/

import java.util.*;

//A classe AlgoritmosGeneticos implementara a logica geral dos algoritmos geneticos
//Sera utilizada como superclasse para especializacao dos problemas
public abstract class AlgoritmosGeneticos {

	/*-----------------| Espaco dos Atributos |-----------------*/
	
	//geracao -> Lista de elementos da geracao atual da populacao
	//Cada elemento devera° mapear o valor x e o valor y do ponto em binario
	//Cada funcao fitness tera seu proprio modelo de individuo, portanto eles serao mapeados nas classes filhas
	List<int[]> geracao;
	
	//indGeracao -> Indicador da geracao em que a populacao esta
	int indGeracao;
	
	//Intervalo da funcao
	int min, max;
	
	//rand -> operador aleatorio
	Random rand;
	
	/*-----------------| Atributos da Evolucao |-----------------*/
	//Criterio de Parada
	protected int critParada;
	protected int numGeracoes;
	private static final int CONVERGENCIA = 0;
	private static final int NUM_GERACOES = 1;
	
	//Numero de individuos por geracao
	protected int numIndividuos;
	
	//Numero de genes por individuo
	protected int numGenes;
	
	//Tipo de Cross-Over
	protected int tipoCrossover;
	private static final int CROSS1PONTO = 0;
	private static final int CROSS2PONTOS = 1;
	
	//Probabilidade de ocorrer Crossover
	protected double probCrossover;
	
	//Tipo de Mutacao
	protected int tipoMutacao;
	private static final int MUTACAOTRAD = 0;
	
	//Probabilidade de mutacao
	protected double probMutacao;
	
	//Criterio de Selecao
	protected int critSelecao;
	
	//Elitismo?
	protected boolean elitismo;
	
	/*-----------------| Espaco dos Metodos |-----------------*/	
	
	//Construtor -> Por enquanto so inicializa rand
	protected AlgoritmosGeneticos()
	{
		this.rand = new Random();
	}
	
	//geradorInicial -> Cria a primeira geracao - Supoe que o alfabeto e {0,1}
	//Recebe como parametro o numero de individuos que serao criados na geracao
	void geradorInicial(int n)
	{
		//Laco que cria todos os individuos
		for(int i = 0; i < n; i++)
			//Iterar por cada gene do cromossomo dando um valor aleatorio
			for(int j = 0; j < this.geracao.get(i).length; j++)
				this.geracao.get(i)[j] = rand.nextInt(2);
	}
	
	//imprimeGeracao -> imprime os elementos da geracao atual
	//Depois podemos definir algo para separar mantissa e expoente na representacao decimal.
	void imprimeGeracao()
	{
		Iterator<int[]> i;
		int[] temp;
		int j;
		
		i = this.geracao.iterator();
		
		//Navegar nos elementos
		while(i.hasNext())
		{
			temp = i.next();
			
			System.out.print("Individuo " + i + ": ");
			
			//Imprimir o indiv√≠duo
			for(j = 0; j < temp.length; j++)
				System.out.print(temp[j]);
			
			System.out.println("");
		}
	}
	
	//Fitness e abstrato porque cada filho definira° o seu fitness
	//Recebera° como entrada um fenotipo e devolvera° uma avaliacao
	//O fenotipo de todas as funcoes e um ponto no plano
	protected abstract double fitness(Ponto fenotipo);
	
	//Calcula o fitness total da populacao
        double fitnessTotal()
        {
            double total=0;
            for(int[] ind : geracao)
            {
                total+=fitness(Utils.binarioPraDecimal(ind, min, max));
            }
            return total;
        }
        
        //Metodo de selecao 1 -> giro de roleta
        //Sorteia dois  numeros i,j no intervalo [0, fitness total da populacao]
        //Escolhe os dois individuos no qual o i e j esta em seu intervalo/fatia
        //Retorna um vetor de duas posicoes, cada uma delas com o indice de um dos individuos escolhidos
        int[] giroDeRoleta()
        {
            int g1 = rand.nextInt((int)fitnessTotal());//Numero da roleta para primeira escolha
            int [] ind;
            int escolhido=0;
            
            for(int j = 0; g1>0; j++)
            {                
                ind = geracao.get(j);
                g1 -= fitness(Utils.binarioPraDecimal(ind, min, max));
                escolhido = j;
            }
            
            int j;
            do
            {
                int g2 = rand.nextInt((int)fitnessTotal()); //Numero da roleta para segunda escolha
                for(j = 0; g2>0; j++)
                {   
                    ind = geracao.get(j);
                    g2 -= fitness(Utils.binarioPraDecimal(ind, min, max));
                    g1 = j;
                }
            }while(g1!=escolhido);//Para garantir que nao escolhemos dois iguais
            
            return new int[]{escolhido, g1};
        }
        
        //Operador de cruzamento 1 -> cruzamento de um ponto
        //Escolhe dois individuos a e b da populacao
        //Sorteia uma posicao i, pega de 0 a i do a e de i ao final de b e vice-versa
        //Criando dois novos individuos
        List<int[]> crossover1px()
        {
            List<int[]> filhos = new ArrayList<int[]>();
            int[] escolhidos = giroDeRoleta();
            
            int [] a = geracao.get(escolhidos[0]);
            int [] b = geracao.get(escolhidos[1]);
            
            int p = rand.nextInt()%(a.length-1)+1;
            
            int [] f1 = new int[a.length];
            int [] f2 = new int[a.length];
            
            int i;
            for(i = 0; i < p; i++)
            {
                f1[i] = a[i];
                f2[i] = b[i];
            }
            
            for(i = p; i < f1.length; i++)
            {
                f1[i] = b[i];
                f2[i] = a[i];
            }
            
            filhos.add(a);
            filhos.add(b);
            
            return filhos;
        }
        
        //Operador de cruzamento 2 -> cruzamento de dois ponto
        //Escolhe dois individuos a e b da populacao
        //Sorteia uma posicao p1 e uma p2, garante que p1 seja < p2.
        //Cria dois individuos, 
        //sendo um deles da posicao 0 a p1 igual o pai 1, da p1 ate a p2 igual o pai 2 e da p2 ate o final igual o pai 1
        //O segundo filho e o inverso, [pai2|pai1|pai2]
        //Criando dois novos individuos
        List<int[]> crossover2px()
        {
            List<int[]> filhos = new ArrayList<int[]>();
            int[] escolhidos = giroDeRoleta();
            
            int [] a = geracao.get(escolhidos[0]);
            int [] b = geracao.get(escolhidos[1]);
            
            int p1 = rand.nextInt()%(a.length-1)+1;
            int p2 = rand.nextInt()%(a.length-1)+1;
            while(p2<=p1)
                if(p2 < p1)
                {
                    int aux = p1;
                    p1 = p2;
                    p2 = aux;
                }else if(p2==p1)
                {
                    p1 = rand.nextInt()%(a.length-1)+1;
                    p2 = rand.nextInt()%(a.length-1)+1;
                }
            
            int [] f1 = new int[a.length];
            int [] f2 = new int[a.length];
            
            for(int i = 0; i < p1; i++)
            {
                f1[i] = a[i];
                f2[i] = b[i];
            }
            for(int i = p1; i < p2; i++)
            {
                f1[i] = b[i];
                f2[i] = a[i];
            }
            for(int i = p2; i < a.length; i++)
            {
                f1[i] = a[i];
                f2[i] = b[i];
            }
            
            filhos.add(a);
            filhos.add(b);
            
            return filhos;
        }
        
        //Operador de mutacao 1 -> Mutacao simples
        void mutacaoSimples(List<int[]> proxFilhos)
        {
        	int indice;
        	
        	//Escolher um individuo
        	indice = this.rand.nextInt(proxFilhos.size());
            int[] mutante = proxFilhos.get(indice);
            
            //Escolher um gene
            int m = rand.nextInt()%(mutante.length);
            
            //Escolher um elemento do alfabeto
            mutante[m] = rand.nextInt(2);
        }
        
        //Teste de convergencia - Verifica se todos os individuos tem o mesmo fitness
        boolean convergiu()
        {
        	double firstFitness;
        	firstFitness = this.fitness(Utils.binarioPraDecimal(this.geracao.get(0), this.min, this.max));
        	
        	//Se achou alguem com fitness igual ao do primeiro, retorna false
        	for(int i = 1; i < this.numIndividuos; i++)
        	{
        		if(this.fitness(Utils.binarioPraDecimal(this.geracao.get(i), this.min, this.max)) != firstFitness)
        			return false;
        	}
        	
        	//Se nao achou -> Convergiu - retorna true
        	return true;
        }
        
        //Operador de Selecao 1) Melhores da geracao
        List<int[]> melhoresFilhos(List<int[]> proxFilhos)
        {
        	List<int[]> melhoresFilhos;
        	melhoresFilhos = new ArrayList<int[]>();
        	
        	Set<Double> fitness;
        	Iterator<Double> it;
        	
        	//Criar um mapa ordenado para indexar os filhos por fitness
        	Map<Double, List<int[]>> mapeamento;
        	mapeamento = new TreeMap<Double, List<int[]>>();
        	
        	//Ir adicionando os filhos
        	for(int i = 0; i < proxFilhos.size(); i++)
        	{
        		double fitnessI;
        		fitnessI = this.fitness(Utils.binarioPraDecimal(proxFilhos.get(i), this.min, this.max));
        		
        		//Se o fitness ja estiver no mapeamento, apenas adicionar o filho
        		if(mapeamento.containsKey(fitnessI))
        		{
        			mapeamento.get(fitnessI).add(proxFilhos.get(i));
        		}
        		//Caso contrario, colocar o fitness no mapeamento e criar uma nova lista com o individuo
        		else
        		{
        			List<int[]> mapeador = new ArrayList<int[]>();
        			mapeador.add(proxFilhos.get(i));
        			
        			mapeamento.put(fitnessI, mapeador);
        		}
        	}
        	
        	//Depois de mapear todos os filhos pelo fitness, basta colocar os com melhor fitness na proxima geracao
        	fitness = mapeamento.keySet();
        	it = fitness.iterator();
        	
        	//Todos os filhos foram adicionados em ordem crescente
        	while(it.hasNext())
        		melhoresFilhos.addAll(mapeamento.get(it.next()));
        	
        	//Agora basta retirar os piores (inicio da lista)
        	while(melhoresFilhos.size() > this.numIndividuos)
        		melhoresFilhos.remove(0);
        	
        	return melhoresFilhos;
        }
        
        //Metodo Evolucao: Evoluira o algoritmo guiado pelos parametros
        protected void evolucao()
        {
        	//Variaveis auxiliares
        	int i;
        	List<int[]> proxFilhos;
        	
        	proxFilhos = new ArrayList<int[]>();
        	
        	//1) Criar a primeira geracao
        	this.geracao = new ArrayList<int[]>(this.numIndividuos);
        	
        	for(i = 0; i < this.numIndividuos; i++)
        		this.geracao.add(new int[this.numGenes]);
        	
        	this.geradorInicial(this.numIndividuos);
        	this.indGeracao = 1;
        	
        	// Iniciar a Evolucao
        	// A Evolucao ser· um while true, cujo criterio de parada definira o break
        	while(true)
        	{
        		//A evolucao consiste em:
        		
        		//1) Dada um probabilidade de ocorrer o crossOver
        		if(this.rand.nextDouble() <= this.probCrossover)
        		{
        			//a) Selecionar os pais
        			//b) Efetuar o cruzamento e gerar os filhos
        			if(this.tipoCrossover == CROSS1PONTO)
        				proxFilhos.addAll(this.crossover1px());
        			else if(this.tipoCrossover == CROSS2PONTOS)
        				proxFilhos.addAll(this.crossover2px());
        		}
        		
        		//2) Dada uma probabilidade de ocorrer a mutacao
        		if(this.rand.nextDouble() <= this.probMutacao)
        		{
        			//Efetuar a mutacao
        			if(this.tipoMutacao == MUTACAOTRAD)
        				this.mutacaoSimples(proxFilhos);
        		}
        		
        		//3) Selecionar os melhores filhos para compor a proxima geracao
        		//Necessario implementar os criterios de selecao
        		this.geracao = this.melhoresFilhos(proxFilhos);
        		
        		//Incrementar a geracao
        		this.indGeracao++;
        		
        		//4) Parar a evolucao quando for a hora certa
        		if(this.critParada == CONVERGENCIA)
        		{
        			//Teste de convergencia - Se a geracao convergiu, paramos a evolucao
        			if(this.convergiu())
        				break;
        		}
        		else if(this.critParada == NUM_GERACOES)
        		{
        			//Se a geracao atual corresponder ao numero estabelecido, paramos a evolucao
        			if(this.indGeracao == this.numGeracoes)
        				break;
        		}
        	}
        }      
}