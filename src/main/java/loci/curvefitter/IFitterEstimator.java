/*
 * #%L
 * Curve Fitter library for fitting exponential decay curves to sample data.
 * %%
 * Copyright (C) 2010 - 2014 Board of Regents of the University of
 * Wisconsin-Madison.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
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
