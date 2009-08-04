package benchmark;

import beagle.Beagle;
import beagle.BeagleFactory;

/**
 * Created by IntelliJ IDEA.
 * User: rambaut
 * Date: Aug 1, 2009
 * Time: 11:48:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class BenchmarkBeagle {
    private final static String human = "AGAAATATGTCTGATAAAAGAGTTACTTTGATAGAGTAAATAATAGGAGCTTAAACCCCCTTATTTCTACTAGGACTATGAGAATCGAACCCATCCCTGAGAATCCAAAATTCTCCGTGCCACCTATCACACCCCATCCTAAGTAAGGTCAGCTAAATAAGCTATCGGGCCCATACCCCGAAAATGTTGGTTATACCCTTCCCGTACTAAGAAATTTAGGTTAAATACAGACCAAGAGCCTTCAAAGCCCTCAGTAAGTTG-CAATACTTAATTTCTGTAAGGACTGCAAAACCCCACTCTGCATCAACTGAACGCAAATCAGCCACTTTAATTAAGCTAAGCCCTTCTAGACCAATGGGACTTAAACCCACAAACACTTAGTTAACAGCTAAGCACCCTAATCAAC-TGGCTTCAATCTAAAGCCCCGGCAGG-TTTGAAGCTGCTTCTTCGAATTTGCAATTCAATATGAAAA-TCACCTCGGAGCTTGGTAAAAAGAGGCCTAACCCCTGTCTTTAGATTTACAGTCCAATGCTTCA-CTCAGCCATTTTACCACAAAAAAGGAAGGAATCGAACCCCCCAAAGCTGGTTTCAAGCCAACCCCATGGCCTCCATGACTTTTTCAAAAGGTATTAGAAAAACCATTTCATAACTTTGTCAAAGTTAAATTATAGGCT-AAATCCTATATATCTTA-CACTGTAAAGCTAACTTAGCATTAACCTTTTAAGTTAAAGATTAAGAGAACCAACACCTCTTTACAGTGA";
    private final static String chimp = "AGAAATATGTCTGATAAAAGAATTACTTTGATAGAGTAAATAATAGGAGTTCAAATCCCCTTATTTCTACTAGGACTATAAGAATCGAACTCATCCCTGAGAATCCAAAATTCTCCGTGCCACCTATCACACCCCATCCTAAGTAAGGTCAGCTAAATAAGCTATCGGGCCCATACCCCGAAAATGTTGGTTACACCCTTCCCGTACTAAGAAATTTAGGTTAAGCACAGACCAAGAGCCTTCAAAGCCCTCAGCAAGTTA-CAATACTTAATTTCTGTAAGGACTGCAAAACCCCACTCTGCATCAACTGAACGCAAATCAGCCACTTTAATTAAGCTAAGCCCTTCTAGATTAATGGGACTTAAACCCACAAACATTTAGTTAACAGCTAAACACCCTAATCAAC-TGGCTTCAATCTAAAGCCCCGGCAGG-TTTGAAGCTGCTTCTTCGAATTTGCAATTCAATATGAAAA-TCACCTCAGAGCTTGGTAAAAAGAGGCTTAACCCCTGTCTTTAGATTTACAGTCCAATGCTTCA-CTCAGCCATTTTACCACAAAAAAGGAAGGAATCGAACCCCCTAAAGCTGGTTTCAAGCCAACCCCATGACCTCCATGACTTTTTCAAAAGATATTAGAAAAACTATTTCATAACTTTGTCAAAGTTAAATTACAGGTT-AACCCCCGTATATCTTA-CACTGTAAAGCTAACCTAGCATTAACCTTTTAAGTTAAAGATTAAGAGGACCGACACCTCTTTACAGTGA";
    private final static String gorilla = "AGAAATATGTCTGATAAAAGAGTTACTTTGATAGAGTAAATAATAGAGGTTTAAACCCCCTTATTTCTACTAGGACTATGAGAATTGAACCCATCCCTGAGAATCCAAAATTCTCCGTGCCACCTGTCACACCCCATCCTAAGTAAGGTCAGCTAAATAAGCTATCGGGCCCATACCCCGAAAATGTTGGTCACATCCTTCCCGTACTAAGAAATTTAGGTTAAACATAGACCAAGAGCCTTCAAAGCCCTTAGTAAGTTA-CAACACTTAATTTCTGTAAGGACTGCAAAACCCTACTCTGCATCAACTGAACGCAAATCAGCCACTTTAATTAAGCTAAGCCCTTCTAGATCAATGGGACTCAAACCCACAAACATTTAGTTAACAGCTAAACACCCTAGTCAAC-TGGCTTCAATCTAAAGCCCCGGCAGG-TTTGAAGCTGCTTCTTCGAATTTGCAATTCAATATGAAAT-TCACCTCGGAGCTTGGTAAAAAGAGGCCCAGCCTCTGTCTTTAGATTTACAGTCCAATGCCTTA-CTCAGCCATTTTACCACAAAAAAGGAAGGAATCGAACCCCCCAAAGCTGGTTTCAAGCCAACCCCATGACCTTCATGACTTTTTCAAAAGATATTAGAAAAACTATTTCATAACTTTGTCAAGGTTAAATTACGGGTT-AAACCCCGTATATCTTA-CACTGTAAAGCTAACCTAGCGTTAACCTTTTAAGTTAAAGATTAAGAGTATCGGCACCTCTTTGCAGTGA";

    private static int[] getStates(String sequence) {
        int[] states = new int[sequence.length()];

        for (int i = 0; i < sequence.length(); i++) {
            switch (sequence.charAt(i)) {
                case 'A':
                    states[i] = 0;
                    break;
                case 'C':
                    states[i] = 1;
                    break;
                case 'G':
                    states[i] = 2;
                    break;
                case 'T':
                    states[i] = 3;
                    break;
                default:
                    states[i] = 4;
                    break;
            }
        }
        return states;
    }

    private static double[] getPartials(String sequence) {
        double[] partials = new double[sequence.length() * 4];

        int k = 0;
        for (int i = 0; i < sequence.length(); i++) {
            switch (sequence.charAt(i)) {
                case 'A':
                    partials[k++] = 1;
                    partials[k++] = 0;
                    partials[k++] = 0;
                    partials[k++] = 0;
                    break;
                case 'C':
                    partials[k++] = 0;
                    partials[k++] = 1;
                    partials[k++] = 0;
                    partials[k++] = 0;
                    break;
                case 'G':
                    partials[k++] = 0;
                    partials[k++] = 0;
                    partials[k++] = 1;
                    partials[k++] = 0;
                    break;
                case 'T':
                    partials[k++] = 0;
                    partials[k++] = 0;
                    partials[k++] = 0;
                    partials[k++] = 1;
                    break;
                default:
                    partials[k++] = 1;
                    partials[k++] = 1;
                    partials[k++] = 1;
                    partials[k++] = 1;
                    break;
            }
        }
        return partials;
    }


    public static void main(String[] argv) {

        // is nucleotides...
        int stateCount = 4;

        // get the number of site patterns
        int nPatterns = human.length();

        // create an instance of the BEAGLE library
        Beagle instance = BeagleFactory.loadBeagleInstance(
                3,				/**< Number of tip data elements (input) */
                5,	            /**< Number of partials buffers to create (input) */
                3,		        /**< Number of compact state representation buffers to create (input) */
                stateCount,		/**< Number of states in the continuous-time Markov chain (input) */
                nPatterns,		/**< Number of site patterns to be handled by the instance (input) */
                1,		        /**< Number of rate matrix eigen-decomposition buffers to allocate (input) */
                4,		        /**< Number of rate matrix buffers (input) */
                1,              /**< Number of rate categories (input) */
                1
        );
        if (instance == null) {
            System.err.println("Failed to obtain BEAGLE instance");
            System.exit(1);
        }

        instance.setTipStates(0, getStates(human));
        instance.setTipStates(1, getStates(chimp));
        instance.setTipStates(2, getStates(gorilla));

        // set the sequences for each tip using partial likelihood arrays
//        instance.setPartials(0, getPartials(human));
//        instance.setPartials(1, getPartials(chimp));
//        instance.setPartials(2, getPartials(gorilla));

        final double[] rates = { 1.0 };
        instance.setCategoryRates(rates);

        // create base frequency array
        final double[] freqs = { 0.25, 0.25, 0.25, 0.25 };

        // create an array containing site category weights
        final double[] weights = { 1.0 };

        // an eigen decomposition for the JC69 model
        final double[] evec = {
                1.0,  2.0,  0.0,  0.5,
                1.0,  -2.0,  0.5,  0.0,
                1.0,  2.0, 0.0,  -0.5,
                1.0,  -2.0,  -0.5,  0.0
        };

        final double[] ivec = {
                0.25,  0.25,  0.25,  0.25,
                0.125,  -0.125,  0.125,  -0.125,
                0.0,  1.0,  0.0,  -1.0,
                1.0,  0.0,  -1.0,  0.0
        };

        double[] eval = { 0.0, -1.3333333333333333, -1.3333333333333333, -1.3333333333333333 };

        // set the Eigen decomposition
        instance.setEigenDecomposition(0, evec, ivec, eval);

        // a list of indices and edge lengths
        int[] nodeIndices = { 0, 1, 2, 3 };
        double[] edgeLengths = { 0.1, 0.1, 0.2, 0.1 };

        // tell BEAGLE to populate the transition matrices for the above edge lengths
        instance.updateTransitionMatrices(
                0,             // eigenIndex
                nodeIndices,   // probabilityIndices
                null,          // firstDerivativeIndices
                null,          // secondDervativeIndices
                edgeLengths,   // edgeLengths
                4);            // count

        // create a list of partial likelihood update operations
        // the order is [dest, destScaling, source1, matrix1, source2, matrix2]
        int[] operations = {
                3, 3, 0, 0, 1, 1,
                4, 4, 2, 2, 3, 3
        };
        int[] rootIndices = { 4 };

        int count = 10000000;
        System.out.println("Running " + count + " iterations...");
        long time0 = System.nanoTime();
        for (int i = 0; i < count; i++) {
            // update the partials
            instance.updatePartials(
                    operations,     // eigenIndex
                    2,              // operationCount
                    false);         // rescale ?
        }
        long time1 = System.nanoTime();
        System.out.println("Time = " + ((double)(time1 - time0) / 1000000000));

        double[] patternLogLik = new double[nPatterns];

        int[] scalingFactorsIndices = {3, 4}; // internal nodes
        int[] scalingFactorsCount = { 2} ;

        // calculate the site likelihoods at the root node
        instance.calculateRootLogLikelihoods(
                rootIndices,            // bufferIndices
                weights,                // weights
                freqs,                 // stateFrequencies
                scalingFactorsIndices,
                patternLogLik);         // outLogLikelihoods

        double logL = 0.0;
        for (int i = 0; i < nPatterns; i++) {
//            System.out.println("site lnL[" + i + "] = " + patternLogLik[i]);
            logL += patternLogLik[i];
        }

        System.out.println();
        System.out.println("logL = " + logL + " (PAUP logL = -1574.63623)");
    }
}