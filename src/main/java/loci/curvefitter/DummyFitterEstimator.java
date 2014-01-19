/*
 * #%L
 * Curve Fitter library for fitting exponential decay curves to sample data.
 * %%
 * Copyright (C) 2010 - 2014 Board of Regents of the University of
 * Wisconsin-Madison.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
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
