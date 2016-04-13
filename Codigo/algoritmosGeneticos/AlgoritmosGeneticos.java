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
    //Cada elemento devera¡ mapear o valor x e o valor y do ponto em binario
    //Cada funcao fitness tera seu proprio modelo de individuo, portanto eles serao mapeados nas classes filhas
    List<Individuo> geracao;
    List<Individuo> geracaoRoleta;
    //indGeracao -> Indicador da geracao em que a populacao esta
    int indGeracao;

    //Intervalo da funcao
    protected int min, max;

    //Tipo de Funcao
    protected int tipoFun;
    protected static final int MINIMIZACAO = 0;
    protected static final int MAXIMIZACAO = 1;

    //rand -> operador aleatorio
    Random rand;

    //fitnessTotal - Guarda o fitness total da geracao - para evitar recalculo
    double fitnessTotal;
    double fitnessTotalRoleta;
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

    //Numero de Crossovers que ocorrerao por ciclo evolutivo
    protected int numCross;

    //Probabilidade de ocorrer Crossover
    protected double probCrossover;

    //Tipo de Mutacao
    protected int tipoMutacao;
    private static final int MUTACAOTRAD = 0;

    //Probabilidade de mutacao
    protected double probMutacao;

    //Criterio de Troca
    protected int critTroca;
    private static final int COM_SUBST = 0;
    private static final int SEM_SUBST = 1;

    //Elitismo?
    protected boolean elitismo;

    /*-----------------| Espaco dos Metodos |-----------------*/
    //Construtor -> Por enquanto so inicializa rand
    protected AlgoritmosGeneticos() {
        this.rand = new Random();
    }

    //geradorInicial -> Cria a primeira geracao - Supoe que o alfabeto e {0,1}
    //Recebe como parametro o numero de individuos que serao criados na geracao
    void geradorInicial() {
        //1) Criar a primeira geracao
        this.geracao = new ArrayList(this.numIndividuos);

        //Laco que cria todos os individuos
        for (int i = 0; i < this.numIndividuos; i++) {
            //Iterar por cada gene do cromossomo dando um valor aleatorio
            int[] gen_ind_atual = new int[numGenes];

            for (int j = 0; j < gen_ind_atual.length; j++) {
                gen_ind_atual[j] = rand.nextInt(2);
            }
            geracao.add(new Individuo(gen_ind_atual, fitness(Utils.binarioPraDecimal(gen_ind_atual, min, max))));
        }
    }

    //imprimeGeracao -> imprime os elementos da geracao atual
    //Depois podemos definir algo para separar mantissa e expoente na representacao decimal.
    void imprimeGeracao() {
        Iterator<Individuo> i;
        Individuo temp;
        int j, k;

        i = this.geracao.iterator();
        k = 0;
        System.out.println("GERACAO: "+indGeracao);
        //Navegar nos elementos
        while (i.hasNext()) {
            temp = i.next();

            System.out.print("Individuo " + k++ + ": ");
            int [] genotipo = temp.getGenotipo();
            //Imprimir o individuo
            for (j = 0; j < genotipo.length; j++) {
                System.out.print(genotipo[j]);
            }
            System.out.print(" Fit: "+temp.fitness+"\n");
        }

        System.out.println("");
    }

    //Fitness e abstrato porque cada filho definira¡ o seu fitness
    //Recebera¡ como entrada um fenotipo e devolvera¡ uma avaliacao
    //O fenotipo de todas as funcoes e um ponto no plano
    protected abstract double fitness(Ponto fenotipo);

    //Calcula o fitness total da populacao
    double fitnessTotal() {
        double total = 0;
        for (Individuo ind : geracao) {
            total += fitness(Utils.binarioPraDecimal(ind.getGenotipo(), min, max));
        }
        return total;
    }
    
    void criaGeracaoRoleta()
    {
        geracaoRoleta = new ArrayList<Individuo>();
        double shift = 0;
        fitnessTotalRoleta = 0;
        if(tipoFun == MINIMIZACAO)
        {   
            if(geracao.get(geracao.size()-1).fitness > 0)
                shift = geracao.get(geracao.size()-1).fitness +1;
            for(Individuo ind : geracao)
            {
                //Invertendo os fitness de todos os individuos
                double novoFitness = shift+ind.fitness*-1;
                geracaoRoleta.add(new Individuo(ind.getGenotipo(), novoFitness));
                fitnessTotalRoleta += novoFitness;
            }
        }
        else
        {
            if(geracao.get(0).fitness < 0) 
                shift = geracao.get(0).fitness*-1 +1;
            for(Individuo ind : geracao)
            {
                
                double novoFitness = shift+ind.fitness;
                geracaoRoleta.add(new Individuo(ind.getGenotipo(), novoFitness));
                fitnessTotalRoleta += novoFitness;
            }
        }
        if(fitnessTotalRoleta == 0) fitnessTotalRoleta =1;
        Collections.shuffle(geracaoRoleta);
    }
    
    
    //Metodo de selecao 1 -> giro de roleta
    //Sorteia dois  numeros i,j no intervalo [0, fitness total da populacao]
    //Escolhe os dois individuos no qual o i e j esta em seu intervalo/fatia
    //Retorna um vetor de duas posicoes, cada uma delas com o indice de um dos individuos escolhidos
    Individuo[] roleta() {

        double g1 = (rand.nextDouble());//Numero da roleta para primeira escolha
        Individuo ind1 = null,ind2 = null;
        int escolhido = 0;
        
       
        for (int j = 0; g1 > 0; j++) {
            ind1 = geracaoRoleta.get(j);
            double x =(ind1.fitness) / this.fitnessTotalRoleta; 
            g1 -= x;
            escolhido = j;
        }

        int escolhido2 = 0;

        do {
            g1 = (rand.nextDouble()); //Numero da roleta para segunda escolha
            for (int j = 0; g1 > 0 && j < geracao.size(); j++) {
                ind2 = geracaoRoleta.get(j);
                double x =(ind2.fitness) / this.fitnessTotalRoleta; 
                g1 -= x;
                escolhido2 = j;
            }
        } while (escolhido2 == escolhido);//Para garantir que nao escolhemos dois iguais
        return new Individuo[]{ind1, ind2};
    }

    double fitnessTotalRoleta(int fator, double shift) {
        double total = 0;
        if (shift < 0) {
            shift = -1*shift;
            for (Individuo ind : geracao) {
                double x = fator * ind.fitness + shift+1;
                total += x;
            }
        } else {
            return fitnessTotal();
        }
        return total;
    }

    //Operador de cruzamento 1 -> cruzamento de um ponto
    //Escolhe dois individuos a e b da populacao
    //Sorteia uma posicao i, pega de 0 a i do a e de i ao final de b e vice-versa
    //Criando dois novos individuos
    List<Individuo> crossover1px() {
        List<Individuo> filhos = new ArrayList();
        Individuo[] escolhidos = roleta();

        
        int[] pai1 = escolhidos[0].getGenotipo();
        int[] pai2 = escolhidos[1].getGenotipo();
        
        int p = rand.nextInt(pai1.length - 1)+1;

        int[] f1 = new int[pai1.length];
        int[] f2 = new int[pai1.length];

        int i;
        for (i = 0; i < p; i++) {
            f1[i] = pai1[i];
            f2[i] = pai2[i];
        }

        for (i = p; i < f1.length; i++) {
            f1[i] = pai2[i];
            f2[i] = pai1[i];
        }
        
        filhos.add(new Individuo(f1, fitness(Utils.binarioPraDecimal(f1, min, max))));
        filhos.add(new Individuo(f2, fitness(Utils.binarioPraDecimal(f2, min, max))));

        return filhos;
    }

    //Operador de cruzamento 2 -> cruzamento de dois ponto
    //Escolhe dois individuos a e b da populacao
    //Sorteia uma posicao p1 e uma p2, garante que p1 seja < p2.
    //Cria dois individuos, 
    //sendo um deles da posicao 0 a p1 igual o pai 1, da p1 ate a p2 igual o pai 2 e da p2 ate o final igual o pai 1
    //O segundo filho e o inverso, [pai2|pai1|pai2]
    //Criando dois novos individuos
    List<Individuo> crossover2px() {
        List<Individuo> filhos = new ArrayList();
        Individuo[] escolhidos = roleta();

        int[] pai1 = escolhidos[0].getGenotipo();
        int[] pai2 = escolhidos[1].getGenotipo();

        int p1 = rand.nextInt(pai1.length - 1)+1;
        int p2 = rand.nextInt(pai1.length - 1)+1;
        while (p2 <= p1) {
            if (p2 < p1) {
                int aux = p1;
                p1 = p2;
                p2 = aux;
            } else if (p2 == p1) {
                p1 = rand.nextInt(pai1.length - 1);
                p2 = rand.nextInt(pai1.length - 1);
            }
        }

        int[] f1 = new int[pai1.length];
        int[] f2 = new int[pai1.length];

        for (int i = 0; i < p1; i++) {
            f1[i] = pai1[i];
            f2[i] = pai2[i];
        }
        for (int i = p1; i < p2; i++) {
            f1[i] = pai2[i];
            f2[i] = pai1[i];
        }
        for (int i = p2; i < pai1.length; i++) {
            f1[i] = pai1[i];
            f2[i] = pai2[i];
        }

        filhos.add(new Individuo(f1, fitness(Utils.binarioPraDecimal(f1, min, max))));
        filhos.add(new Individuo(f2, fitness(Utils.binarioPraDecimal(f2, min, max))));

        return filhos;
    }

    //Operador de mutacao 1 -> Mutacao simples
    void mutacaoSimples(List<Individuo> proxFilhos) {
        int indice;

        //Escolher um individuo
        indice = this.rand.nextInt(proxFilhos.size());
        int[] mutante = proxFilhos.get(indice).getGenotipo();

        //Escolher um gene
        int m = rand.nextInt(mutante.length);

        //Escolher um elemento do alfabeto
        mutante[m] = rand.nextInt(2);
        proxFilhos.remove(indice);
        proxFilhos.add(indice, new Individuo(mutante, fitness(Utils.binarioPraDecimal(mutante, min, max))));
    }
    
    //Operador de mutacao 2 -> Mutacao flip
    void mutacaoFlip(List<Individuo> proxFilhos) {
        int indice;

        //Escolher um individuo
        indice = this.rand.nextInt(proxFilhos.size());
        int[] mutante = proxFilhos.get(indice).getGenotipo();
        //Escolher um gene
        int m1 = rand.nextInt(mutante.length);
        int m2 = rand.nextInt(mutante.length);

        //Escolher um elemento do alfabeto
        int aux = mutante[m1];
        mutante[m1] = mutante[m2];
        mutante[m2] = aux;
        proxFilhos.remove(indice);
        proxFilhos.add(indice, new Individuo(mutante, fitness(Utils.binarioPraDecimal(mutante, min, max))));
    }

    //Teste de convergencia - Verifica se todos os individuos tem o mesmo fitness
    boolean convergiu() {
        double firstFitness;
        firstFitness = this.geracao.get(0).fitness;

        //Se achou alguem com fitness igual ao do primeiro, retorna false
        int i=0;
        for (i = 1; i < this.geracao.size(); i++) {
 
            if (this.geracao.get(i).fitness != firstFitness) {
                return false;
            }
        }

        Ponto point = Utils.binarioPraDecimal(this.geracao.get(0).getGenotipo(), this.min, this.max);
        System.out.println("X: " + point.x + " Y: " + point.y + " - Fitness: " + this.fitness(point));

        //Se nao achou -> Convergiu - retorna true
        return true;
    }

    //Operador de troca de populacao Melhores dentre os filhos
    //Esse operador esta funcionando para problemas de maximizacao -> Deve ser adaptado para minimizacao
    List<Individuo> melhoresFilhos(List<Individuo> proxFilhos) {
        List<Individuo> melhoresFilhos;
        Collections.sort(proxFilhos);
        if(tipoFun == MAXIMIZACAO)
            melhoresFilhos = proxFilhos.subList(proxFilhos.size()-numIndividuos, proxFilhos.size());
        else
            melhoresFilhos = proxFilhos.subList(0, numIndividuos);
        
        return melhoresFilhos;
    }

    //Metodo que retorna o individuo mais bem adaptado da geracao atual
    Individuo getBetter() {
        int[] maximo, minimo;
        double maxFitness, minFitness;

        maxFitness = minFitness = this.fitness(Utils.binarioPraDecimal(this.geracao.get(0).getGenotipo(), this.min, this.max));
        maximo = minimo = this.geracao.get(0).getGenotipo();

        for (int i = 1; i < this.numIndividuos; i++) {
            if (this.fitness(Utils.binarioPraDecimal(this.geracao.get(i).getGenotipo(), this.min, this.max)) > maxFitness) {
                maxFitness = this.fitness(Utils.binarioPraDecimal(this.geracao.get(i).getGenotipo(), this.min, this.max));
                maximo = this.geracao.get(i).getGenotipo();
            }

            if (this.fitness(Utils.binarioPraDecimal(this.geracao.get(i).getGenotipo(), this.min, this.max)) < minFitness) {
                minFitness = this.fitness(Utils.binarioPraDecimal(this.geracao.get(i).getGenotipo(), this.min, this.max));
                minimo = this.geracao.get(i).getGenotipo();
            }
        }

        //Se o problema for de maximizacao, retorno o maximo
        //Se o problema for de minimizacao, retorno o minimo
        if (this.tipoFun == MINIMIZACAO) {
            return new Individuo(minimo, minFitness);
        } else {
            return new Individuo(maximo, maxFitness);
        }
    }

    //Metodo Evolucao: Evoluira o algoritmo guiado pelos parametros
    protected void evolucao() {
        //Variaveis auxiliares
        //int i;
        List<Individuo> proxFilhos;
        proxFilhos = new ArrayList();

        this.geradorInicial();
        this.indGeracao = 1;

        //Imprimir a primeira geracao
        this.imprimeGeracao();

        // Iniciar a Evolucao
        // A Evolucao será um while true, cujo criterio de parada definira o break
        while (true) {
            Collections.sort(geracao);
            criaGeracaoRoleta();
            //Armazenar o fitness total para evitar recalculo
            this.fitnessTotal = this.fitnessTotal();

            //A evolucao consiste em:
            //0) Ocorrerão n CrossOvers
            for (int contCross = 0; contCross < this.numCross; contCross++) {

                //1) Dada um probabilidade de ocorrer o crossOver
                if (this.rand.nextDouble() <= this.probCrossover) {
                    //a) Selecionar os pais
                    //b) Efetuar o cruzamento e gerar os filhos
                    if (this.tipoCrossover == CROSS1PONTO) {
                        proxFilhos.addAll(this.crossover1px());
                    } else if (this.tipoCrossover == CROSS2PONTOS) {
                        proxFilhos.addAll(this.crossover2px());
                    }
                }
            }

            //2) Dada uma probabilidade de ocorrer a mutacao
            if (this.rand.nextDouble() <= this.probMutacao && proxFilhos.size() > 0) {
                //Efetuar a mutacao
                if (this.tipoMutacao == MUTACAOTRAD) {
                    this.mutacaoSimples(proxFilhos);
                }
            }

            //3) Selecionar os melhores filhos para compor a proxima geracao
            //Aplicar o criterio de troca de populacao
            if (this.critTroca == SEM_SUBST) {
                proxFilhos.addAll(this.geracao);
            } else //Se for aplicada troca com substituicao de populacao, verificar se vai ser aplicado elitismo
             if (this.elitismo) {
                    proxFilhos.add(this.getBetter());
                }
            //Nota: por uma questao simples, nao se aplica elitismo quando nao ha substituicao de populacao
            List<Individuo> copiaFilhos = new ArrayList<Individuo>();
            copiaFilhos.addAll(proxFilhos);
            this.geracao = this.melhoresFilhos(copiaFilhos);

            //Incrementar a geracao
            this.indGeracao++;
            
            //A cada iteracao imprimir a geracao
            this.imprimeGeracao();
            Collections.sort(geracao);
            System.out.println("FITNESS: U"+geracao.get(geracao.size()-1).fitness);
            System.out.println("FITNESS: P"+geracao.get(0).fitness);
            
            //4) Parar a evolucao quando for a hora certa
            if (this.critParada == CONVERGENCIA) {
                //Teste de convergencia - Se a geracao convergiu, paramos a evolucao
                if (this.convergiu()) {
                    break;
                }
            } else if (this.critParada == NUM_GERACOES) {
                //Se a geracao atual corresponder ao numero estabelecido, paramos a evolucao
                if (this.indGeracao == this.numGeracoes) {
                    break;
                }
            }

            
            //Limpar os objetos utilizados
            proxFilhos.clear();
        }
        
    }
    
    
}
