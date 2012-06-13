/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loci.curvefitter;

import loci.curvefitter.ICurveFitter.FitFunction;

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
    public void adjustEstimatedParams(double[] params, FitFunction fitFunction, double A, double tau, double Z) {
        switch (fitFunction) {
            case SINGLE_EXPONENTIAL:
                params[1] = Z;
                params[2] = A;
                params[3] = tau;
                break;
            case DOUBLE_EXPONENTIAL:
                params[1] = Z;
                params[2] = A;
                params[3] = tau;
                params[4] = A / 2;
                params[5] = tau / 2;
                break;
            case TRIPLE_EXPONENTIAL:
                params[1] = Z;
                params[2] = A;
                params[3] = tau;
                params[4] = A / 2;
                params[5] = tau / 2;
                params[6] = A / 3;
                params[7] = tau / 3;
                break;
            case STRETCHED_EXPONENTIAL:
                params[1] = Z;
                params[2] = A;
                params[3] = tau;
                params[4] = 1.0;
                break;
        }
    }
}
