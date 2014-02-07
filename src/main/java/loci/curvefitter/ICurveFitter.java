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

/**
 * Interface for a curve fitter.
 *
 * @author Aivar Grislis
 */
public interface ICurveFitter {

    /**
     * Specifies fitting region.
     */
    public static enum FitRegion {
        SUMMED, ROI, POINT, EACH
    }

    /**
     * Specifies fitting algorithm.
     * 
     */
    public static enum FitAlgorithm {
       JAOLHO, SLIMCURVE_RLD, SLIMCURVE_LMA, SLIMCURVE_RLD_LMA
    }
    // was RLD, LMA, RLD_LMA
    
    /**
     * Specifies curves that this fits.
     */
    public static enum FitFunction {
        SINGLE_EXPONENTIAL, DOUBLE_EXPONENTIAL, TRIPLE_EXPONENTIAL, STRETCHED_EXPONENTIAL
    }
    
    /**
     * Specifies noise model for fit.
     */
    public static enum NoiseModel {
        CONST, GIVEN, POISSON_DATA, POISSON_FIT, GAUSSIAN_FIT, MAXIMUM_LIKELIHOOD
    }

    /**
     * Default increment along x axis (evenly spaced).
     */
    public double DEFAULT_X_INC = 1.0f;
    
    /**
     * Get fitting estimator.
     * 
     * @return fitting estimator
     */
    public IFitterEstimator getEstimator();
    
    /**
     * Set fitting estimator.
     * 
     * @param fitting estimator
     */
    public void setEstimator(IFitterEstimator estimator);
    
    /**
     * Get fitting algorithm.
     * 
     * @return fitting algorithm
     */
    public FitAlgorithm getFitAlgorithm();
    
    /**
     * Set fitting algorithm.
     * 
     * @param fitting algorithm
     */
    public void setFitAlgorithm(FitAlgorithm algorithm);

    /**
     * Get function we are fitting.
     *
     * @return function type
     */
    public FitFunction getFitFunction();

    /**
     * Set function we are fitting.
     *
     * @param function
     */
    public void setFitFunction(FitFunction function);
    
    /**
     * Get noise model for fit.
     * 
     * @return 
     */
    public NoiseModel getNoiseModel();

    /**
     * Sets noise model for fit.
     * 
     * @param noiseModel 
     */
    public void setNoiseModel(NoiseModel noiseModel);

    /**
     * Get number of function components.
     *
     * @return number of components
     */
    public int getNumberComponents();

    /**
     * Get increment along x axis (evenly spaced).
     *
     * @return x increment
     */
    public double getXInc();

    /**
     * Set increment along x axis (evenly spaced).
     *
     * @param xInc x increment
     */
    public void setXInc(double xInc);

    /**
     * Gets which parameters are free (vs. fixed).
     *
     * @return array of booleans
     */
    public boolean[] getFree();

    /**
     * Sets which parameters are free (vs. fixed).
     */
    public void setFree(boolean[] free);

    /**
     * Get instrument response data.  Input to fit only.
     *
     * @param scale to this number of pixels
     * @return array of data or null if not set
     */
    public double[] getInstrumentResponse(int pixels);

    /**
     * Set instrument response data.  Input to fit only.
     *
     * @param response array of data
     */
    public void setInstrumentResponse(double response[]); 

    /**
     * Do the fit.
     *
     * @param data array of data to fit
     * @return status code
     */
    public int fitData(ICurveFitData[] data); 
}
