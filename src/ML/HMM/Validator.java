package ML.HMM;

import javafx.util.Pair;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by Ahmed Hani Ibrahim on 12/19/2015.
 */
public class Validator {
    private static Validator ourInstance = new Validator();

    public static Validator getInstance() {
        return ourInstance;
    }

    private Validator() {
    }

    public boolean summationIsOne(Hashtable<String, Double> list) {
        double sum = 0.0;

        for (double item : list.values()) {
            sum += item;
        }

        return sum == 1.0;
    }

    public boolean isValidInitialProbabilities(Vector<String> states, Hashtable<String, Double> initialProbabilities) {
        if (states.size() != initialProbabilities.size())
            return false;

        for (int i = 0; i < states.size(); i++) {
            boolean found = false;
            for (String state : initialProbabilities.keySet()) {
                if (state.equals(states.get(i))) {
                    found = true;
                    break;
                }
            }

            if (!found)
                return false;
        }

        return true;
    }

    public boolean isValidTransitionMatrix(Hashtable<Pair<String, String>, Double> transitionMatrix, Vector<String> states) {
        if (transitionMatrix.size() != states.size() * states.size())
            return false;

        Hashtable<String, Integer> frequency = new Hashtable<String, Integer>();

        for (int i = 0; i < states.size(); i++) {
            int selfLoops = 0;
            int count = 0;
            for (Pair<String, String> item : transitionMatrix.keySet()) {
                if (item.getKey().equals(item.getValue())) {
                    frequency.put(item.getKey(), selfLoops++);

                    if (frequency.get(item.getKey()) > 1) {
                        return false;
                    }
                }

                if (item.getKey().equals(states.get(i)) || item.getValue().equals(states.get(i))) {
                    count++;
                }

                if (count > 2 * states.size()) {
                    return false;
                }
            }
        }

        Hashtable<Pair<String, String>, Boolean> visited = new Hashtable<Pair<String, String>, Boolean>();

        for (Pair<String, String> first : transitionMatrix.keySet()) {
            double sum = 0.0;
            String state = first.getKey();
            for (Pair<String, String> second: transitionMatrix.keySet()) {
                if (state.equals(second.getKey()) && !visited.containsKey(second)) {
                    sum += transitionMatrix.get(second);
                    visited.put(second, true);
                }
            }

            if (sum != 1.0) {
                return false;
            }
        }

        return true;
    }

    public boolean isValidEmissionMatrix(Hashtable<Pair<String, String>, Double> emissionMatrix, Vector<String> states, Vector<String> observations) {
        if (emissionMatrix.size() != observations.size() * states.size()) {
            return false;
        }

        for (Pair<String, String> item : emissionMatrix.keySet()) {
            boolean found = false;
            double sum = 0.0;
            for (int i = 0; i < states.size(); i++) {
                for (int j = 0; j < observations.size(); j++) {
                    if (item.getKey().equals(states.get(i)) && item.getValue().equals(observations.get(j))) {
                        found = true;
                        break;
                    }
                }

                if (found) break;
            }

            if (!found)
                return false;

            for (Pair<String, String> item2 : emissionMatrix.keySet()) {
                if (item == item2)
                    continue;

                if (item.getKey().equals(item2.getKey())) {
                    sum += emissionMatrix.get(item2);
                }
            }

            if (sum != 1.0)
                return false;
        }

        return true;
    }
}
