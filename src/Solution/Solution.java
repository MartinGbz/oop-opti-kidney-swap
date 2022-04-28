package Solution;

import instance.Instance;
import instance.network.Altruist;
import instance.network.Pair;
import io.InstanceReader;
import io.exception.ReaderException;

import java.util.LinkedList;
import java.util.Map;

public class Solution {

    int gainMedTotal;
    private Instance instance;
    private LinkedList<Cycle> cycles;
    private LinkedList<Chain> chains;

    public Solution(Instance instance) {
        this.gainMedTotal = 0;
        this.instance = instance;
        this.cycles = new LinkedList<>();
        this.chains = new LinkedList<>();
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
     * @param pair
     * @return
     */
    public boolean addPairNewCycle(Pair pair) {
        Cycle c = new Cycle();
        if(c.addPairToCycle(pair)) {
            this.cycles.addLast(c);
            this.gainMedTotal += c.getGainMedSequence();
            return true;
        }
        return false;
    }


    /**
     * Ajoute une pair dans un cycle existant
     * @param pair
     * @return
     */
    public boolean addPairExistingCycle(Pair pair) {
        int deltaGain;
        for(Cycle c : this.cycles) {
            deltaGain = c.getGainMedSequence();
            if(c.addPairToCycle(pair)) {
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
        for(Map.Entry altruistEntry : this.instance.getAltruists().entrySet()) {
            Altruist alt = (Altruist) altruistEntry.getValue();
            if(!isAltruistUnique(alt)) {
                System.out.println("Check PresenceUniqueAltruists False (altruiste : " + alt + ")");
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
        for(Map.Entry pairEntry : this.instance.getPairs().entrySet()) {
            Pair p = (Pair) pairEntry.getValue();
            if(!isPairUnique(p)) {
                System.out.println("Check PresenceUniquePairs False (paire : " + p + ")");
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
            System.out.println("Check GainMedical False");
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

    private void solutionInstanceWithCycles() {
        Cycle c = new Cycle();
        boolean status;

        for(Map.Entry pairEntry : this.instance.getPairs().entrySet()) {
            status = false;
            Pair p = (Pair) pairEntry.getValue();
            if(!isUsedInChain(p)) {
                for(Cycle cycle : this.cycles) {
                    if(cycle.getSequence().size() < 2) {
                        status = cycle.addPairToCycle(p);
                        if(status) break;
                    }
                }
                if(!status) {
                    this.addPairNewCycle(p);
                }
            }
        }
    }

    public boolean isUsedInChain(Pair pair) {
        for(Chain c : this.chains) {
            if(c.getSequence().contains(pair)) {
                return true;
            }
        }
        return false;
    }

    private void solutionInstanceWithChain() {
        boolean status = false;
        for(Map.Entry altruistEntry : this.instance.getAltruists().entrySet()) {
            Chain c = new Chain((Altruist) altruistEntry.getValue());
            this.chains.addLast(c);
        }
        for(Map.Entry pairEntry : this.instance.getPairs().entrySet()) {
            status = false;
            for(Chain chain : this.chains) {
                status = chain.addPairToChain((Pair) pairEntry.getValue());
                if(status) break;
            }
        }
    }

    private void SolutionInstance() {
        this.solutionInstanceWithChain();
        this.solutionInstanceWithCycles();
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/testInstance.txt");
            Instance i = reader.readInstance();
            System.out.println("Instance lue avec success !");

            Solution s = new Solution(i);
            // s.solutionInstanceWithCycles();
            // s.solutionInstanceWithChain();
            s.SolutionInstance();
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

        }
        catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }



}

