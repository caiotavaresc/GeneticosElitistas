package algoritmosGeneticos;

/* REQUISITOS BASICOS DO TRABALHO
uso de dois diferentes tamanhos de popula¸c˜ao, de grandezes bem diferentes; 
dois crit´erios de parada;
para a parte I - cromossomo com codifica¸c˜ao bin´aria; -> FEITO E NAO TESTADO
um operador de sele¸c˜ao (preferencialmente roleta); -> FEITO E NAO TESTADO
dois operadores de crossover (crossover de um ponto e outro `a escolha do grupo); -> FEITO E NAO TESTADO
dois operadores de muta¸c˜ao (muta¸c˜ao simples e outro `a escolha do grupo); -> 1/2 E NAO TESTADO
dois crit´erios de troca de popula¸c˜ao;
evolu¸c˜ao sem eletismo e com elitismo.
*/

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
        //Sorteia dois  numeros i,j no intervalo [0, fitness total da populacao]
        //Escolhe os dois individuos no qual o i e j esta em seu intervalo/fatia
        //Retorna um vetor de duas posições, cada uma delas com o indice de um dos individuos escolhidos
        int[] giroDeRoleta()
        {
            int g1 = rand.nextInt((int)fitnessTotal());//Numero da roleta para primeira escolha
            int [] ind;
            int escolhido=0;
            for(int j = 0; g1>0; j++)
            {                
                ind = geracao.get(j);
                g1-=fitness(Utils.binarioPraDecimal(ind, min, max));
                escolhido = j;
            }
            int j;
            do
            {
                int g2 = rand.nextInt((int)fitnessTotal()); //Numero da roleta para segunda escolha
                for(j = 0; g2>0; j++)
                {   
                    ind = geracao.get(j);
                    g2-=fitness(Utils.binarioPraDecimal(ind, min, max));
                    g1 = j;
                }
            }while(g1!=escolhido);//Para garantir que não escolhemos dois iguais
            
            return new int[]{escolhido, g1};
            
        }
        
        //Operador de cruzamento 1 -> cruzamento de um ponto
        //Escolhe dois individuos a e b da populacao
        //Sorteia uma posicao i, pega de 0 a i do a e de i ao final de b e vice-versa
        //Criando dois novos individuos
        List<int[]> crossover1px()
        {
            List<int[]> filhos = new ArrayList();
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
        //O segundo filho é o inverso, [pai2|pai1|pai2]
        //Criando dois novos individuos
        List<int[]> crossover2px()
        {
            List<int[]> filhos = new ArrayList();
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
        
        //Operador de mutação 1 -> Mutação simples
        int [] mutacaoSimples(int indice)
        {
            int[] mutante = geracao.get(indice);
            
            int m = rand.nextInt()%(mutante.length);
            
            mutante[m] = rand.nextInt(2);
            
            return mutante; //Nao tenho certeza da necessidade desse retorno
        }
        
}
