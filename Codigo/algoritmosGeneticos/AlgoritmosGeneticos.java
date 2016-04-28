package algoritmosGeneticos;


import Operadores.Crossover;
import Operadores.Mutacao;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class AlgoritmosGeneticos {
    /*-----------------| Espaco dos Atributos |-----------------*/
    //geracao -> Lista de elementos da geracao atual da populacao
    //Cada elemento devera mapear o valor x e o valor y do ponto em binario
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

    //Operador de crossover
    protected Crossover crossover;

    //Numero de Crossovers que ocorrerao por ciclo evolutivo
    protected int numCross;

    //Probabilidade de ocorrer Crossover
    protected double probCrossover;

    //Operador mutacao
    protected Mutacao mutacao;

    //Probabilidade de mutacao
    protected double probMutacao;

    //Criterio de Troca
    protected int critTroca;
    private static final int COM_SUBST = 0;
    private static final int SEM_SUBST = 1;

    //Elitismo?
    protected boolean elitismo;
    
    //Intervalo de impressao
    protected int intervaloImpressao;
    
    
    /*-----------------| Espaco dos Metodos |-----------------*/
    //Construtor -> Por enquanto so inicializa rand
    protected AlgoritmosGeneticos() {
        this.rand = new Random();
    }
    
    
    /*-----------------|
    Fitness e abstrato porque cada filho definira o seu fitness
    Recebera como entrada um genotipo e devolvera uma avaliacao
    O genotipo eh convertido em fenotipo dentro do metodo
    |-----------------*/
    protected abstract double fitness(int[] genotipo);
    
    /*-----------------|
    Calcula o fitness de toda a populacao
    |-----------------*/
    double fitnessTotal() {
        double total = 0;
        //Percorre todos os individuos da populacao
        for (Individuo ind : geracao) {
            total += ind.fitness; //Acrescenta a total o fitness do individuo vigente
        }
        return total; //Retorna o total de fitness
    }
    
    /*-----------------|
    geradorInicial -> Cria a primeira geracao baseado nos parametros
    Supoe que o alfabeto e {0,1}
    |-----------------*/
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
            //Cria um individuo com aquele cromossomo e adiciona na geracao
            geracao.add(new Individuo(gen_ind_atual, fitness(gen_ind_atual)));
        }
    }
    
    /*-----------------|
    Cria uma copia da geracao atual adaptando o fitness dos individuos de forma que
    todos os valores sejam positivos e garantam o funcionamento da roleta
    |-----------------*/
    void criaGeracaoRoleta()
    {
        geracaoRoleta = new ArrayList<Individuo>(); //Geracao adaptada
        double shift = 0; //O shift e para garantir que nenhum valor seja negativo
        fitnessTotalRoleta = 0; //Fitness total adaptado
        if(tipoFun == MINIMIZACAO)//Caso a funcao seja de minizacao
        {   
            //Se o maior valor for positivo, como ele virara negativo ao multiplicar por -1, ele sera o shift
            if(geracao.get(geracao.size()-1).fitness > 0)
                shift = geracao.get(geracao.size()-1).fitness +1;
            
            for(Individuo ind : geracao)//Para cada individuo da geracao
            {
                //Invertendo os fitness de todos os individuos
                double novoFitness = shift+ind.fitness*-1; //Multiplicar o fitness por -1 e adicionar o shift
                geracaoRoleta.add(new Individuo(ind.getGenotipo(), novoFitness)); //Adicionar a geracaoRoleta o adaptado
                fitnessTotalRoleta += novoFitness;//Calculando o fitness total adaptado
            }
        }
        else //Se for maximizacao
        {
            if(geracao.get(0).fitness < 0) //Se o menor valor for negativo
                shift = geracao.get(0).fitness*-1 +1; //O shift deve ser esse valor (positivo) e somar a 1
            for(Individuo ind : geracao) //Percorre a geracao
            {    
                double novoFitness = shift+ind.fitness; //Da o shift no fitness adaptado
                geracaoRoleta.add(new Individuo(ind.getGenotipo(), novoFitness)); //Adiciona a geracao adaptada
                fitnessTotalRoleta += novoFitness; // Calcula o novo fitness
            }
        }
        if(fitnessTotalRoleta == 0) fitnessTotalRoleta =1; //Para nao acontecer divisao por 0
        Collections.shuffle(geracaoRoleta); //Desordena a geracao roleta para aumentar a aletoriedade
    }
    
    
    /*-----------------|
    Metodo de selecao: Roleta
    Sorteia dois  numeros i,j no intervalo [0, 100]
    Escolhe os dois individuos no qual o i e j esta em seu intervalo/fatia
    Retorna um vetor de duas posicoes, cada uma delas com um dos individuos escolhidos
    |-----------------*/
    Individuo[] roleta() {

        double g1 = (rand.nextDouble());//Numero da roleta para primeira escolha
        Individuo ind1 = null,ind2 = null;
        int escolhido = 0;
       
        for (int j = 0; g1 > 0; j++) {//Enquanto o numero for maior que 0, se ele for menor ou igual, para pois esse eh o individuo escolhido
            ind1 = geracaoRoleta.get(j);
            double x =(ind1.fitness) / this.fitnessTotalRoleta; 
            g1 -= x;//Desconta a % que o fitness do individuo representa
            escolhido = j;
        }

        //Faz a mesma coisa para escolher o segundo individuo
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
    
    /*-----------------|
    Criterio de parada: Convergiu
    Verifica se todos os individuos da geracao tem o mesmo fitness
    |-----------------*/
    boolean convergiu() {
        double firstFitness;
        firstFitness = this.geracao.get(0).fitness;

        //Se achou alguem com fitness diferente ao do primeiro, retorna false
        int i=0;
        for (i = 1; i < this.geracao.size(); i++) {
 
            if (this.geracao.get(i).fitness != firstFitness) {
                return false;
            }
        }
        //Se nao achou -> Convergiu - retorna true
        return true;
    }
    
    /*-----------------|
    Troca dos individuos
    Recebe a lista de filhos e retorna uma sublista dessa com os filhos melhores avaliados
    |-----------------*/
    List<Individuo> melhoresFilhos(List<Individuo> proxFilhos) {
        List<Individuo> melhoresFilhos;//Cria a lista de melhores filhos
        Collections.sort(proxFilhos); //Ordena o vetor de filhos (parametro)
        if(tipoFun == MAXIMIZACAO)//Se for maximizacao
            if(proxFilhos.size() < numIndividuos){ //Se o numero de filhos nao suprir o numero de individuos por geracao parametrizado
                melhoresFilhos = proxFilhos; //Os "melhores filhos" serao todos os filhos gerados
                melhoresFilhos.addAll(geracao.subList(geracao.size() - (numIndividuos - proxFilhos.size()), geracao.size()));
                //Completados pelos melhores da geracao pai
            }
            else //Caso o numero de filhos seja maior ou igual ao parametrizado: Escolhe os numIndividuos(inteiro dado como parametro) melhores filhos
                melhoresFilhos = proxFilhos.subList(proxFilhos.size()-numIndividuos, proxFilhos.size());
        else{ // Caso seja minizacao, eh analogo a maximizacao, mas escolhe os filhos com os menores valores
            if(proxFilhos.size() < numIndividuos){
                melhoresFilhos = proxFilhos;
                melhoresFilhos.addAll(geracao.subList(0, numIndividuos - proxFilhos.size()+1));
            }
            else
                melhoresFilhos = proxFilhos.subList(0, numIndividuos);
        }
        
        return melhoresFilhos;
    }
    
    /*-----------------|
    Metodo que retorna o individuo mais bem adaptado (com melhor fitness) da geracao atual
    |-----------------*/
    Individuo getBetter() {
        //Se o problema for de maximizacao, retorno o maximo
        //Se o problema for de minimizacao, retorno o minimo
        if (this.tipoFun == MINIMIZACAO) {
            return geracao.get(0);
        } else {
            return geracao.get(geracao.size()-1);
        }
    }
    
    //Metodo Evolucao: Evoluira o algoritmo guiado pelos parametros
    protected void evolucao() {
        boolean convergiu = true;
        String relatorio ="";
        relatorio = relatorio + "numGenes,numIndividuos,critParada,numGeracoes,numCross,tipoCrossover,probCrossover,tipoMutacao,probMutacao,critTroca,elitismo\n";
        //System.out.println("numGenes\tnumIndividuos\tcritParada\tnumGeracoes\tnumCross\ttipoCrossover\tprobCrossover\ttipoMutacao\tprobMutacao\tcritTroca\telitismo\n"
        //        +numGenes+"\t"+numIndividuos+"\t"+critParada+"\t"+numGeracoes+"\t"+numCross+"\t"+crossover+"\t"
        //        +probCrossover+"\t"+mutacao+"\t"+probMutacao+"\t"+critTroca+"\t"+elitismo+"\n");
        relatorio = relatorio + ((this instanceof funcoes.Gold1)? "Gold-," : (this instanceof funcoes.Bump2)? "Bump2,": (this instanceof funcoes.Gold2)? "Gold+,": "Rastrigin")+",";
        relatorio = relatorio+numGenes+","+numIndividuos+","+critParada+","+numGeracoes+","+numCross+","+crossover+","
                +probCrossover+","+mutacao+","+probMutacao+","+critTroca+","+elitismo+"\n\n";
        
        relatorio = relatorio + "numGeracao,fitness da populacao: total,medio,maximo,minimo\n";
        
        //System.out.println("numGeracao\tfitness da populacao: total\tmedio\tmaximo\tminimo");
        //Variaveis auxiliares
        List<Individuo> proxFilhos;
        proxFilhos = new ArrayList();

        this.geradorInicial();
        this.indGeracao = 1;

        //Imprimir a primeira geracao
        //this.imprimeGeracao();

        // Iniciar a Evolucao
        // A Evolucao será um while true, cujo criterio de parada definira o break
        while (true) {
            Collections.sort(geracao);
            criaGeracaoRoleta();
            //Armazenar o fitness total para evitar recalculo
            this.fitnessTotal = this.fitnessTotal();
            
            relatorio = relatorio+ indGeracao+","+fitnessTotal+","+(fitnessTotal/geracao.size())+","+geracao.get(geracao.size()-1).fitness+","+geracao.get(0).fitness+"\n";
           // if(indGeracao%intervaloImpressao==1) System.out.println(indGeracao+"\t"+fitnessTotal+"\t"+(fitnessTotal/geracao.size())+"\t"+geracao.get(geracao.size()-1).fitness+"\t"+geracao.get(0).fitness);
            //A evolucao consiste em:
            //0) Ocorrerão n CrossOvers
            for (int contCross = 0; contCross < this.numCross; contCross++) {

                //1) Dada um probabilidade de ocorrer o crossOver
                if (this.rand.nextDouble() <= this.probCrossover) {
                    //a) Selecionar os pais
                    Individuo[] pais = roleta();
                    //b) Efetuar o cruzamento e gerar os filhos
                    int[][] filhos = crossover.executar(pais[0].getGenotipo(), pais[1].getGenotipo());
                    
                    proxFilhos.add(new Individuo(filhos[0], fitness(filhos[0])));
                    proxFilhos.add(new Individuo(filhos[1], fitness(filhos[1])));
                }
            }

            //2) Dada uma probabilidade de ocorrer a mutacao
            if (this.rand.nextDouble() <= this.probMutacao && proxFilhos.size() > 0) {
               
                int indice = rand.nextInt(proxFilhos.size());
                //Efetuar a mutacao
                int[] mutante = mutacao.executar(proxFilhos.get(indice).getGenotipo());
                proxFilhos.remove(indice);
                proxFilhos.add(indice, new Individuo(mutante, fitness(mutante)));
                
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
            if(indGeracao==10000)
            {
                convergiu = false;
                System.out.println("NAO CONVERGIU");
                break;
            }
            
            //A cada iteracao imprimir a geracao
            //this.imprimeGeracao();
            Collections.sort(geracao);
            
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
        //System.out.println(indGeracao+"\t"+fitnessTotal+"\t"+(fitnessTotal/geracao.size())+"\t"+geracao.get(geracao.size()-1).fitness+"\t"+geracao.get(0).fitness);
        relatorio = relatorio+ indGeracao+","+fitnessTotal+","+(fitnessTotal/geracao.size())+","+geracao.get(geracao.size()-1).fitness+","+geracao.get(0).fitness+"\n";
        if(!convergiu) relatorio = relatorio+"NAO CONVERGIU";
        imprimirRelatorio(relatorio);
    }
    
    
    public void imprimirRelatorio(String r)
    {
        String nome="";
        
        if( this instanceof funcoes.Gold1)
            nome = "Gold-";
        else if (this instanceof funcoes.Rastrigin)
            nome = "Rastrigin";
        else if(this instanceof funcoes.Bump2)
            nome = "Bump2";
        else
            nome = "Gold+";
           
        try {
            BufferedWriter w = new BufferedWriter(new FileWriter(nome+","+numGenes+","+numIndividuos+","+critParada+","+numGeracoes+","+numCross+","+crossover+","
                    +probCrossover+","+mutacao+","+probMutacao+","+critTroca+","+elitismo+".csv"));
            
            w.append(r);
            w.close();
        } catch (IOException ex) {
            Logger.getLogger(AlgoritmosGeneticos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
