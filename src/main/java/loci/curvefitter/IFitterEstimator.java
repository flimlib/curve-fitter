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
 * This interface allows for some peculiarities of TRI2 to ensure matching results,
 * particularly when doing a fit with an RLD estimate combined with an LMA fit.
 *
 * @author Aivar Grislis
 */
public interface IFitterEstimator {

    /**
     * Gets default A for RLD fits.
     * 
     * @return 
     */
    public double getDefaultA();

    /**
     * Gets default T for RLD fits.
     * 
     * @return 
     */
    public double getDefaultT();

    /**
     * Gets default Z for RLD fits.
     * 
     * @return 
     */
    public double getDefaultZ();

    /**
     * TRI2 adjusts the start index for the initial RLD estimate.
     * 
     * @param yCount
     * @param start
     * @param stop
     * @return adjusted start index
     */
    public int getEstimateStartIndex(double[] yCount, int start, int stop);

    /**
     * TRI2 adjusts the starting A estimate for the initial RLD estimate fit.
     * 
     * @param A
     * @param yCount
     * @param start
     * @param stop
     * @return initial A estimate
     */
    public double getEstimateAValue(double A, double[] yCount, int start, int stop);

    /**
     * TRI2 uses hardcoded noise model.
     * 
     * @param noiseModel
     * @return 
     */
    public NoiseModel getEstimateNoiseModel(NoiseModel noiseModel);
    
    /**
     * Given an initial estimate of A, tau, and Z sets up the parameters for
     * further fitting.
     * 
     * @param params
     * @param free
     * @param fitFunction
     * @param A
     * @param tau
     * @param Z
     */
    public void adjustEstimatedParams(double[] params, boolean[] free,
            FitFunction fitFunction, double A, double tau, double Z);

    /**
     * Converts a bin index to a time value.
     * 
     * @param bin
     * @param inc
     * @return 
     */
    public double binToValue(int bin, double inc);

    /**
     * Converts a time value to a bin index.
     * 
     * @param value
     * @param inc
     * @return 
     */
    public int valueToBin(double value, double inc);

    /**
     * Rounds a double floating point to a specified number of decimal places.
     * 
     * @param value
     * @param decimalPlaces
     * @return 
     */
    public double roundToDecimalPlaces(double value, int decimalPlaces);
}
