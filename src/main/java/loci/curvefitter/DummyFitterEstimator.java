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
    public double getDefaultA() {
        return 1000.0;
    }
    
    @Override
    public double getDefaultT() {
        return 2.0;
    }
    
    @Override
    public double getDefaultZ() {
        return 0.0;
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
    
    @Override
    public double binToValue(int bin, double inc) {
        return bin * inc;
    }

    @Override
    public int valueToBin(double value, double inc) {
        int bin = (int)(value / inc);
        return bin;
    }
    
    @Override
    public double roundToDecimalPlaces(double value, int decimalPlaces) {
        double decimalTerm = Math.pow(value, decimalPlaces);
        int tmp = roundToNearestInteger(value * decimalTerm);
        return (double) tmp / decimalTerm;
    }
    
    private int roundToNearestInteger(double value) {
        return (int) (value + 0.5);
    }
}
