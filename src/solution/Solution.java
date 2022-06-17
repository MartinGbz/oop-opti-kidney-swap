package solution;

import instance.network.Base;
import operateur.CycleNotValide;
import operateur.InsertionPair;
import operateur.ReplacementPair;
import instance.Instance;
import instance.network.Altruist;
import instance.network.Pair;
import io.InstanceReader;
import io.SolutionWriter;
import io.exception.ReaderException;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Classe représentant une solution générée pour une instance
 */
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

    public Solution(Solution s) {
        this.gainMedTotal = s.getGainMedTotal();
        this.instance = s.getInstance();
        this.cycles = new LinkedList<>(s.getCycles());
        this.chains = new LinkedList<>(s.getChains());
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

    /**
     * Checker de la solution
     * @return état de succès du checker
     */
    public boolean check() {
        return checkCycles() && checkChains() && checkGainMedicalTotal() &&
                checkPresenceUniqueAltruists() && checkPresenceUniquePairs();
    }

    /**
     * Vérifie que chaque cycle est valide (via la fonction check() de Cycle.java)
     * @return état du checker pour les cycles (boolean)
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
     * @return état du checker pour les chaines (boolean)
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
     * @return état du checker (boolean)
     */
    private boolean checkPresenceUniqueAltruists() {
        ArrayList<Altruist> altruists = new ArrayList<>(this.instance.getAltruists().values());
        for(Altruist altruist : altruists) {
            if(!isAltruistUnique(altruist)) {
                System.out.println("Check PresenceUniqueAltruists False (altruiste : " + altruist + ")");
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie si toutes les PAIRES ne sont présentes qu'une fois dans l'ensemble des cycles/chaines
     * @return état du checker (boolean)
     */
    private boolean checkPresenceUniquePairs() {
        ArrayList<Pair> pairs = new ArrayList<>(this.instance.getPairs().values());
        for(Pair pair: pairs) {
            if(!isPairUnique(pair)) {
                System.out.println("Check PresenceUniquePairs False (paire : " + pair + ")");
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie que le gain médical de la solution est égal à la somme des gain méd de chaque cycle & chaine
     * @return état du checker gain recalculé vs gain de la solution (boolean)
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

    /**
     * Vérifie si une paire est présente une seule fois dans l'ensnemble cycle+chaine de la solution
     * @param pair Paire à vérifier
     * @return état du checker (boolean)
     */
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
    /**
     * Vérifie si un altruiste est présent qu'une seule fois dans les chaines de la solution
     * @param altruist Altruist à vérifier
     * @return état du checker (boolean)
     */
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

    /**
     * Remise à 0 du gainMedical de la solution
     * Et parcours les cycles et chaines de la solution pour ajouter leur gain à la solution
     */
    public void calculGainSolution() {
        this.gainMedTotal = 0;
        for(Cycle cycle : this.cycles) {
            this.gainMedTotal += cycle.getGainMedSequence();
        }
        for(Chain chain : this.chains) {
            this.gainMedTotal += chain.getGainMedSequence();
        }
    }

    /**
     * Supprime les séquences non utilisées
     */
    public void deleteSequenceNotUsed() {
        LinkedList<Cycle> copyCycle = new LinkedList<>(this.getCycles());
        LinkedList<Chain> copyChain = new LinkedList<>(this.getChains());
        for(Cycle cycle : copyCycle) {
            if(cycle.getSequence().size() < 2 || cycle.getSequence().size() > this.getInstance().getMaxSizeCycle() ||
                    !cycle.getSequence().getLast().isCompatible(cycle.getSequence().getFirst()))
                this.cycles.remove(cycle);
        }
        for(Chain chain : copyChain) {
            if(chain.getSequence().size() < 2)
                this.chains.remove(chain);
        }
    }

    /**
     *  Récupérer les cycles de tailles 1
     * @return
     */
    public LinkedList<Pair> recoverCyclesOfOne() {
        LinkedList<Cycle> copyCycle = new LinkedList<>(this.cycles);
        LinkedList<Pair> pairsToBeReused = new LinkedList<>();

        for(Sequence seq : copyCycle) {
            if(seq.getSequence().size() < 2) {
                pairsToBeReused.add((Pair) seq.getSequence().getFirst());
                this.cycles.remove(seq);
            }
        }
        return pairsToBeReused;
    }

    /**
     * Créé et ajoute des chaines à partir de la liste des altruistes dans l'instance
     */
    public void createChainsWithAltruists() {
        ArrayList<Altruist> altruists = new ArrayList<>(this.instance.getAltruists().values());
        for(Altruist altruistToAdd : altruists) {
            Chain ch = new Chain(altruistToAdd);
            this.getChains().addLast(ch);
        }
    }

    /**
     *
     * @param altruists
     */
    public void createChainsWithSpecificAltruists(LinkedList<Altruist> altruists) {
        for(Altruist altruistToAdd : altruists) {
            Chain ch = new Chain(altruistToAdd);
            this.getChains().addLast(ch);
        }
    }

    /**
     *
     */
    public void addPairsIntoChains() {
        boolean status;
        ArrayList<Pair> pairs = new ArrayList<>(this.instance.getPairs().values());
        for(Pair pairToAdd : pairs) {
            status = false;
            for(Chain chain : this.getChains()) {
                if(chain.getSequence().size() < this.instance.getMaxSizeChain())
                    status = chain.addPairToChain(pairToAdd);
                if(status) break;
            }
        }
    }

    public void addPairsIntoCycles() {
        boolean status;
        ArrayList<Pair> pairs = new ArrayList<>(this.instance.getPairs().values());
        for(Pair pairToAdd : pairs) {
            status = false;
            if(!isUsedInChain(pairToAdd)) {
                for(Cycle cycle : this.getCycles()) {
                    if(cycle.getSequence().size() < this.instance.getMaxSizeCycle()) {
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

    /**
     * Vérifie si la séquence est présente dans une liste de chaine
     * @param pair paire à rechercher
     * @return état du succès
     */
    public boolean isUsedInChain(Pair pair) {
        for(Chain c : this.getChains()) {
            if(c.getSequence().contains(pair)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtenir le meilleur opérateur d'insertion pour une paire
     * @param pairToInsert paire à insérer
     * @return le meilleur operateur d'insertion
     */
    public InsertionPair getMeilleureInsertion(Pair pairToInsert) {
        InsertionPair insMeilleur = new InsertionPair();
        if(pairToInsert == null) return insMeilleur;

        if(this.chains.size() < this.getInstance().getNbAltruists()) {
            LinkedList<Altruist> lastAltruists = new LinkedList<Altruist>(this.getInstance().getAltruists().values());
            for(Chain chain : this.getChains()) {
                for(Base b : chain.getSequence()) {
                    lastAltruists.remove(b);
                }
            }
            this.createChainsWithSpecificAltruists(lastAltruists);
        }

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
            if(insActu.isBest(insMeilleur)) {
                if( (seq.getSequence().size() < this.getInstance().getMaxSizeChain() && seq.getSequence().getFirst() instanceof Altruist)
                        || (seq.getSequence().size() < this.getInstance().getMaxSizeCycle() && seq.getSequence().getFirst() instanceof Pair))
                    insMeilleur = insActu;
            }

        }
        return insMeilleur;
    }

    /**
     * Obtenir le meilleur opérateur de remplacement pour une paire
     * @param pairToReplace paire à remplacer
     * @return meilleur opérateur de remplacement
     */
    public ReplacementPair getMeilleureReplacement(Pair pairToReplace) {
        ReplacementPair insMeilleur = new ReplacementPair();

        if(pairToReplace == null) return insMeilleur;

        boolean presence = false;
        if(this.chains.size() < this.getInstance().getNbAltruists()) {
            ArrayList<Altruist> altruists = new ArrayList<>(this.getInstance().getAltruists().values());
            for(Altruist a : altruists) {
                for(Chain c : this.getChains()) {
                    if(c.sequence.contains(a))
                        presence = true;
                }
                if(!presence) {
                    Chain ch = new Chain(a);
                    this.getChains().addLast(ch);
                }
            }
        }

        LinkedList<Sequence> sequenceChainCycle = new LinkedList<>();
        for(Sequence chain : this.chains) {
            sequenceChainCycle.add(chain);
        }
        for(Sequence cycle : this.cycles) {
            sequenceChainCycle.add(cycle);
        }


        ReplacementPair insActu;
        for(Sequence seq : sequenceChainCycle) {
            insActu = seq.getMeilleureReplacement(pairToReplace);
            if(insActu.isBest(insMeilleur)) {
                insMeilleur = insActu;
            }
        }
        return insMeilleur;
    }

    public CycleNotValide getMeilleurCycleNotValide(Pair pairToInsert) {
        CycleNotValide meilleurCycleNotValide = new CycleNotValide();
        if(pairToInsert == null) return meilleurCycleNotValide;

        CycleNotValide actuelCycleNotValide;
        for(Sequence cycle : this.cycles) {
            actuelCycleNotValide = cycle.getMeilleurCycleNotValide(pairToInsert);
            if(actuelCycleNotValide.isBest(meilleurCycleNotValide)) {
                meilleurCycleNotValide = actuelCycleNotValide;
            }
        }
        return meilleurCycleNotValide;
    }

    /**
     * Réalise l'insertion à partir de l'opérateur
     * @param infos Opérateur d'insertion
     * @return état du succès d'insertion (boolean)
     */
    public boolean doInsertion(InsertionPair infos) {
        if(infos == null) return false;
        if(!this.cycles.contains(infos.getProcessedSequence()) && !this.chains.contains(infos.getProcessedSequence())) return false;
        if(!infos.doMouvementIfRealisable()) return false;

        this.gainMedTotal += infos.getDeltaCout();
        return true;
    }

    /**
     * Réalise le remplacement à partir de l'opérateur
     * @param infos Opérateur de remplacement
     * @return état du succès de remplacement (boolean)
     */
    public boolean doReplacement(ReplacementPair infos) {
        if(infos == null) return false;
        if(!this.cycles.contains(infos.getProcessedSequence()) && !this.chains.contains(infos.getProcessedSequence())) return false;
        if(!infos.doMouvementIfRealisable()) return false;

        this.gainMedTotal += infos.getDeltaCout();
        return true;
    }

    /**
     *
     * @param infos Opérateur d'insertion dans un cycle non valide
     * @return état du succès d'insertion (boolean)
     */
    public boolean doAddLast(CycleNotValide infos) {
        if(infos == null) return false;
        if(!this.cycles.contains(infos.getProcessedSequence())) return false;
        if(!infos.doMouvementIfRealisable()) return false;
        return true;
    }

    /**
     * Récupération de la liste des paires faisant partie d'aucune solution
     * @return liste de paires (LinkedList<Pair>)
     */
    public LinkedList<Pair> restOfPairs() {
        LinkedList<Pair> pairsNotAssigned = new LinkedList<Pair>(this.getInstance().getPairs().values());
        for(Sequence seq : this.getCycles()) {
            for(Base b : seq.getSequence()) {
                pairsNotAssigned.remove(b);
            }
        }
        for(Sequence seq : this.getChains()) {
            for(Base b : seq.getSequence()) {
                pairsNotAssigned.remove(b);
            }
        }
        return pairsNotAssigned;
    }

    /**
     * Récupération de la liste des altruists faisant partie d'aucune solution
     * @return liste d'altruists (LinkedList<Altruist>)
     */
    public LinkedList<Altruist> restOfAltruists() {
        LinkedList<Altruist> altruitsNotAssigned = new LinkedList<Altruist>(this.getInstance().getAltruists().values());
        for(Sequence seq : this.getChains()) {
            altruitsNotAssigned.remove(seq.getSequence().getFirst());
        }
        return altruitsNotAssigned;
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/testInstance.txt");
            Instance i = reader.readInstance();
            System.out.println("Instance lue avec success !");

            Solution s = new Solution(i);
            System.out.println(s.getInstance().getPairs());
            System.out.println(s);
            System.out.println("Etat du check : " + s.check());

            SolutionWriter sw = new SolutionWriter(s, "testSolution");

        }
        catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
