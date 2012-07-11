/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loci.curvefitter;

import loci.curvefitter.ICurveFitter.FitFunction;
import loci.curvefitter.ICurveFitter.NoiseModel;

/**
 * This is a dummy implementation of the IFitterEstimator interface that turns
 * off fit-time TRI2 behavior.
 * 
 * @author Aivar Grislis
 */
public class DummyFitterEstimator implements IFitterEstimator {
    
    @Override
    public boolean usePrompt() {
        return true;
    }

    @Override
    public int getEstimateStartIndex(double[] yCount, int start, int stop) {
        return start;
    }

    @Override
    public double getEstimateAValue(double A, double[] yCount, int start, int stop) {
        return A;
    }

    @Override
    public NoiseModel getEstimateNoiseModel(NoiseModel noiseModel) {
        return noiseModel;
    }
    
    @Override
    public void adjustEstimatedParams(double[] params, boolean[] free, FitFunction fitFunction, double A, double tau, double Z) {
        switch (fitFunction) {
            case SINGLE_EXPONENTIAL:
                if (free[1]) {
                    params[1] = Z;
                }
                if (free[2]) {
                    params[2] = A;
                }
                if (free[3]) {
                    params[3] = tau;
                }
                break;
            case DOUBLE_EXPONENTIAL:
                if (free[1]) {
                    params[1] = Z;
                }
                if (free[2]) {
                    params[2] = A;
                }
                if (free[3]) {
                    params[3] = tau;
                }
                if (free[4]) {
                    params[4] = A / 2;
                }
                if (free[5]) {
                    params[5] = tau / 2;
                }
                break;
            case TRIPLE_EXPONENTIAL:
                if (free[1]) {
                    params[1] = Z;
                }
                if (free[2]) {
                    params[2] = A;
                }
                if (free[3]) {
                    params[3] = tau;
                }
                if (free[4]) {
                    params[4] = A / 2;
                }
                if (free[5]) {
                    params[5] = tau / 2;
                }
                if (free[6]) {
                    params[6] = A / 3;
                }
                if (free[7]) {
                    params[7] = tau / 3;
                }
                break;
            case STRETCHED_EXPONENTIAL:
                if (free[1]) {
                    params[1] = Z;
                }
                if (free[2]) {
                    params[2] = A;
                }
                if (free[3]) {
                    params[3] = tau;
                }
                if (free[4]) {
                    params[4] = 1.0;
                }
                break;
        }
    }
}
