package algoritmosGeneticos;
import java.util.*;

//A classe AlgoritmosGeneticos implementará a lógica geral dos algoritmos genéticos
//Será utilizada como superclasse para especialização dos problemas
public abstract class AlgoritmosGeneticos {

	/*-----------------| Espaço dos Atributos |-----------------*/
	
	//geracao -> Lista de elementos da geração atual da população
	//Cada elemento deverá mapear o valor x e o valor y do ponto em binário
	//Cada função fitness terá seu próprio modelo de indivíduo, portanto eles serão mapeados nas classes filhas
	List<int[]> geracao;
	
	//indGeracao -> Indicador da geração em que a população está
	int indGeracao;
	
	Intervalo da funcao
	int min, max;
	
	//rand -> operador aleatório
	Random rand;
	
	/*-----------------| Espaço dos Métodos |-----------------*/	
	
	//Construtor -> Por enquanto só inicializa rand
	protected AlgoritmosGeneticos()
	{
		this.rand = new Random();
	}
	
	//geradorInicial -> Cria a primeira geração - Supõe que o alfabeto é {0,1}
	//Recebe como parâmetro o número de indivíduos que serão criados na geração
	void geradorInicial(int n)
	{
		//Laço que cria todos os indivíduos
		for(int i = 0; i < n; i++)
			//Iterar por cada gene do cromossomo dando um valor aleatório
			for(int j = 0; j < this.geracao.get(i).length; j++)
				this.geracao.get(i)[j] = rand.nextInt(2);
	}
	
	//imprimeGeracao -> imprime os elementos da geração atual
	//Depois podemos definir algo para separar mantissa e expoente na representação decimal.
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
			
			System.out.print("Indivíduo " + i + ": ");
			
			//Imprimir o indivíduo
			for(j = 0; j < temp.length; j++)
				System.out.print(temp[j]);
			
			System.out.println("");
		}
	}
	
	//Fitness é abstrato porque cada filho definirá o seu fitness
	//Receberá como entrada um fenótipo e devolverá uma avaliação
	//O fenótipo de todas as funções é um ponto no plano
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
        //Sorteia um  numero i no intervalo [0, fitness total da populacao]
        //Escolhe o individuo no qual o i esta em seu intervalo/fatia
        int[] giroDeRoleta()
        {
            int i = rand.nextInt((int)fitnessTotal());
            for(int [] ind: geracao)
            {
                while(i>0)
                    i-=fitness(Utils.binarioPraDecimal(ind, min, max));
                return Arrays.copyOf(ind, ind.length);
            }
            
            return null;//Isso precisa ser arrumado
        }
        
        //Operador de cruzamento 1 -> cruzamento de um ponto
        //Escolhe dois individuos a e b da populacao
        //Sorteia uma posicao i, pega de 0 a i do a e de i ao final de b e vice-versa
        //Criando dois novos individuos
        List<int[]> crossover1px()
        {
            List<int[]> filhos = new ArrayList();
            int[] a = giroDeRoleta();
            int[] b = giroDeRoleta();
            
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
}
