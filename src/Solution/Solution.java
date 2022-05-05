package Solution;

import Operateur.InsertionPair;
import instance.Instance;
import instance.network.Altruist;
import instance.network.Pair;
import io.InstanceReader;
import io.SolutionWriter;
import io.exception.ReaderException;

import java.util.LinkedList;
import java.util.Map;

public class Solution {

    private int gainMedTotal;
    private final Instance instance;
    private final LinkedList<Cycle> cycles;
    private final LinkedList<Chain> chains;

    public Solution(Instance instance) {
        this.gainMedTotal = 0;
        this.instance = instance;
        this.cycles = new LinkedList<>();
        this.chains = new LinkedList<>();
    }

    public Instance getInstance() {
        return instance;
    }
    public int getGainMedTotal() {
        return gainMedTotal;
    }
    public LinkedList<Cycle> getCycles() {
        return cycles;
    }
    public LinkedList<Chain> getChains() {
        return chains;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "\n\tgainMedTotal=" + gainMedTotal +
                ", \n\tinstance=" + instance +
                ", \n\tcycles=" + cycles +
                ", \n\tchains=" + chains +
                "\n}";
    }

    /**
     * Ajoute une pair dans un nouveau cycle
     * @param pair la paire à ajouter
     * @return true/false si l'ajout est réussi ou non
     */
    public boolean addPairNewCycle(Pair pair) {
        Cycle c = new Cycle();
        if(c.addPairToCycleEnd(pair)) {
            this.cycles.addLast(c);
            this.gainMedTotal += c.getGainMedSequence();
            return true;
        }
        return false;
    }

    /**
     * Ajoute une pair dans un cycle existant
     * @param pair la paire à ajouter
     * @return true/false si l'ajout est réussi ou non
     */
    public boolean addPairExistingCycle(Pair pair) {
        int deltaGain;
        for(Cycle c : this.cycles) {
            deltaGain = c.getGainMedSequence();
            if(c.addPairToCycleEnd(pair)) {
                this.gainMedTotal += (c.getGainMedSequence() - deltaGain);
                return true;
            }
        }

        return false;
    }

    public boolean check() {
        return checkCycles() && checkChains() && checkGainMedicalTotal() &&
                checkPresenceUniqueAltruists() && checkPresenceUniquePairs();
    }

    /**
     * Vérifie que chaque cycle est valide (via la fonction check() de Cycle.java)
     * @return
     */
    private boolean checkCycles() {
        for(Cycle cycle : this.cycles) {
            if(!cycle.check(this.instance.getMaxSizeCycle())) {
                System.out.println("Check Cycles False");
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie que chaque chaine est valide (via la fonction check() de Chain.java)
     * @return
     */
    private boolean checkChains() {
        for(Chain chain : this.chains) {
            if(!chain.check(this.instance.getMaxSizeChain())) {
                System.out.println("Check Chains False");
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie si tous les ALTRUISTS ne sont présents qu'une fois dans l'ensemble des chaines
     * @return
     */
    private boolean checkPresenceUniqueAltruists() {
        for(Altruist altruist : this.instance.getAltruists()) {
            if(!isAltruistUnique(altruist)) {
                System.out.println("Check PresenceUniqueAltruists False (altruiste : " + altruist + ")");
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie si toutes les PAIRES ne sont présentes qu'une fois dans l'ensemble des cycles/chaines
     * @return
     */
    private boolean checkPresenceUniquePairs() {
        for(Pair pair: this.instance.getPairs()) {
            if(!isPairUnique(pair)) {
                System.out.println("Check PresenceUniquePairs False (paire : " + pair + ")");
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie que le gain médical de la solution est égal à la somme des gain méd de chaque cycle & chaine
     * @return
     */
    private boolean checkGainMedicalTotal() {
        int gainTotalCalc = 0;
        for(Cycle cycle : this.cycles) {
            gainTotalCalc += cycle.getGainMedSequence();
        }
        for(Chain chain : this.chains) {
            gainTotalCalc += chain.getGainMedSequence();
        }
        if(gainTotalCalc != this.gainMedTotal) {
            System.out.println("Check GainMedical False <" + gainTotalCalc + ">" );
            return false;
        }
        return true;
    }

    public boolean isPairUnique(Pair pair) {
        int nbTot = 0;
        for(Chain c : this.chains) {
            for(int i=1; i<c.getSequence().size(); i++) {
                Pair p = (Pair) c.getSequence().get(i);
                if(p.equals(pair)) {
                    nbTot++;
                    if(nbTot>1) return false;
                }
            }
        }
        for(Cycle c : this.cycles) {
            for(Object objP : c.getSequence()) {
                Pair p = (Pair) objP;
                if(p.equals(pair)) {
                    nbTot++;
                    if(nbTot>1) return false;
                }
            }
        }
        return true;
    }

    public boolean isAltruistUnique(Altruist altruist) {
        int nbTot = 0;
        for(Chain c : this.chains) {
            if(c.getSequence().size() >= 1) {
                Altruist alt = (Altruist) c.getSequence().get(0);
                if(alt.equals(altruist)) {
                    nbTot++;
                    if(nbTot>1) return false;
                }
            }
        }
        return true;
    }

    public void calculGainSolution() {
        for(Cycle cycle : this.cycles) {
            this.gainMedTotal += cycle.getGainMedSequence();
        }
        for(Chain chain : this.chains) {
            this.gainMedTotal += chain.getGainMedSequence();
        }
    }

    public void deleteSequenceNotUsed() {
        LinkedList<Cycle> copyCycle = new LinkedList<>(this.cycles);
        LinkedList<Chain> copyChain = new LinkedList<>(this.chains);
        for(Cycle cycle : copyCycle) {
            if(cycle.getSequence().size() < 2)
                this.cycles.remove(cycle);
        }
        for(Chain chain : copyChain) {
            if(chain.getSequence().size() < 2)
                this.chains.remove(chain);
        }
    }

    public void createChainsWithAltruists(Instance i) {
        for(Altruist altruistToAdd : i.getAltruists()) {
            Chain ch = new Chain(altruistToAdd);
            this.getChains().addLast(ch);
        }
    }

    public void addPairsIntoChains(Instance i) {
        boolean status;
        for(Pair pairToAdd : i.getPairs()) {
            status = false;
            for(Chain chain : this.getChains()) {
                if(chain.getSequence().size() < i.getMaxSizeChain())
                    status = chain.addPairToChain(pairToAdd);
                if(status) break;
            }
        }
    }

    public void addPairsIntoCycles(Instance i) {
        boolean status;
        for(Pair pairToAdd : i.getPairs()) {
            status = false;
            if(!isUsedInChain(pairToAdd)) {
                for(Cycle cycle : this.getCycles()) {
                    if(cycle.getSequence().size() < i.getMaxSizeCycle()) {
                        status = cycle.addPairToCycleEnd(pairToAdd);
                        if(status) break;
                    }
                }
                if(!status) {
                    this.addPairNewCycle(pairToAdd);
                }
            }
        }
    }

    public boolean isUsedInChain(Pair pair) {
        for(Chain c : this.getChains()) {
            if(c.getSequence().contains(pair)) {
                return true;
            }
        }
        return false;
    }

    public InsertionPair getMeilleureInsertion(Pair pairToInsert) {
        InsertionPair insMeilleur = new InsertionPair();
        if(pairToInsert == null) return insMeilleur;

        if(this.chains.size() < this.getInstance().getNbAltruists())
            this.createChainsWithAltruists(this.getInstance());

        LinkedList<Sequence> sequenceChainCycle = new LinkedList<>();
        for(Sequence chain : this.chains) {
            sequenceChainCycle.add(chain);
        }
        for(Sequence cycle : this.cycles) {
            sequenceChainCycle.add(cycle);
        }

        InsertionPair insActu;
        for(Sequence seq : sequenceChainCycle) {
            insActu = seq.getMeilleureInsertion(pairToInsert);
            if(insActu.isBest(insMeilleur))
                insMeilleur = insActu;
        }

/*
        for(Sequence cycle : this.cycles) {
            insActu = cycle.getMeilleureInsertion(pairToInsert);
            if(insActu.isBest(insMeilleur))
                insMeilleur = insActu;
        }

 */
        return insMeilleur;
    }

    public boolean doInsertion(InsertionPair infos) {
        if(infos == null) return false;
        if(!this.cycles.contains(infos.getProcessedSequence()) && !this.chains.contains(infos.getProcessedSequence())) return false;
        if(!infos.doMouvementIfRealisable()) return false;

        this.gainMedTotal += infos.getDeltaCout();
        return true;
    }


    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/testInstance.txt");
            Instance i = reader.readInstance();
            System.out.println("Instance lue avec success !");

            Solution s = new Solution(i);
            System.out.println(s.getInstance().getPairs());
            //s.solutionInstanceWithCycles();
            //s.solutionInstanceWithChain();
            //s.SolutionInstance();
            System.out.println(s);
            System.out.println("Etat du check : " + s.check());

            /*
            Solution sCheck = new Solution(i);
            Chain c1 = new Chain(i.getAltruists().get(0));
            Chain c2 = new Chain(i.getAltruists().get(0));
            Chain c3 = new Chain(i.getAltruists().get(1));

            sCheck.chains.addLast(c1);
            sCheck.chains.addLast(c2);
            sCheck.chains.addLast(c3);

            Pair p1 = i.getPairs().get(2);
            Pair p2 = i.getPairs().get(3);
            Pair p3 = i.getPairs().get(3);

            Cycle cycle1 = new Cycle();
            cycle1.sequence.addLast(p1);
            cycle1.sequence.addLast(p2);
            cycle1.sequence.addLast(p3);
            sCheck.cycles.addLast(cycle1);

            System.out.println(sCheck);

            System.out.println("-> checkChains : " + sCheck.checkChains());
            System.out.println("-> checkCycles : " + sCheck.checkCycles());
            System.out.println("-> checkGainMedicalTotal : " + sCheck.checkGainMedicalTotal());
            System.out.println("-> checkPresenceUniqueAltruists : " + sCheck.checkPresenceUniqueAltruists());
            System.out.println("-> checkPresenceUniquePairs : " + sCheck.checkPresenceUniquePairs());*/
            SolutionWriter sw = new SolutionWriter(s, "testSolution");

        }
        catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }

}

