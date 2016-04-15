package algoritmosGeneticos;


import Operadores.Crossover;
import Operadores.Individuo;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sousa
 */
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
    
    //Fitness e abstrato porque cada filho definira¡ o seu fitness
    //Recebera¡ como entrada um fenotipo e devolvera¡ uma avaliacao
    //O fenotipo de todas as funcoes e um ponto no plano
    protected abstract double fitness(int[] genotipo);
    
    //Calcula o fitness total da populacao
    double fitnessTotal() {
        double total = 0;
        for (Individuo ind : geracao) {
            total += ind.fitness;
        }
        return total;
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
            geracao.add(new Individuo(gen_ind_atual, fitness(gen_ind_atual)));
        }
    }
    
    
    //Adapta a geracao para que os fitness negativos nao impecam o funcionamento da roleta
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
    
    //Fitness total adaptado para nao causar problemas ao operador roleta
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

        maxFitness = minFitness = this.geracao.get(0).fitness;
        maximo = minimo = this.geracao.get(0).getGenotipo();

        for (int i = 1; i < this.numIndividuos; i++) {
            if (this.geracao.get(i).fitness > maxFitness) {
                maxFitness = this.geracao.get(i).fitness;
                maximo = this.geracao.get(i).getGenotipo();
            }

            if (this.geracao.get(i).fitness < minFitness) {
                minFitness = this.geracao.get(i).fitness;
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
        
        String relatorio;
        relatorio = "numGenes,numIndividuos,critParada,numGeracoes,numCross,tipoCrossover,probCrossover,tipoMutacao,probMutacao,critTroca,elitismo\n";
        System.out.println("numGenes\tnumIndividuos\tcritParada\tnumGeracoes\tnumCross\ttipoCrossover\tprobCrossover\ttipoMutacao\tprobMutacao\tcritTroca\telitismo\n"
                +numGenes+"\t"+numIndividuos+"\t"+critParada+"\t"+numGeracoes+"\t"+numCross+"\t"+crossover+"\t"
                +probCrossover+"\t"+mutacao+"\t"+probMutacao+"\t"+critTroca+"\t"+elitismo+"\n");
        relatorio = relatorio+numGenes+","+numIndividuos+","+critParada+","+numGeracoes+","+numCross+","+crossover+","
                +probCrossover+","+mutacao+","+probMutacao+","+critTroca+","+elitismo+"\n\n";
        
        relatorio = relatorio + "numGeracao,fitness da populacao: total,medio,maximo,minimo\n";
        
        System.out.println("numGeracao\tfitness da populacao: total\tmedio\tmaximo\tminimo");
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
            if(indGeracao%intervaloImpressao==1) System.out.println(indGeracao+"\t"+fitnessTotal+"\t"+(fitnessTotal/geracao.size())+"\t"+geracao.get(geracao.size()-1).fitness+"\t"+geracao.get(0).fitness);
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
        System.out.println(indGeracao+"\t"+fitnessTotal+"\t"+(fitnessTotal/geracao.size())+"\t"+geracao.get(geracao.size()-1).fitness+"\t"+geracao.get(0).fitness);
        relatorio = relatorio+ indGeracao+","+fitnessTotal+","+(fitnessTotal/geracao.size())+","+geracao.get(geracao.size()-1).fitness+","+geracao.get(0).fitness+"\n";
        
        imprimirRelatorio(relatorio);
    }
    
    
    public void imprimirRelatorio(String r)
    {
        String nome="";
        
        if( this instanceof funcoes.Gold)
            nome = "Gold";
        else if (this instanceof funcoes.Rastrigin)
            nome = "Rastrigin";
        else
            nome = "Bump2";
            
        
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
