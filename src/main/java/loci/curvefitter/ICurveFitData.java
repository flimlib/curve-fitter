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
 * Interface to the curve fitting data.
 *
 * @author Aivar Grislis
 */
//TODO fix params
//TODO num components?
public interface ICurveFitData {

    /**
     * Gets channel of the fitted pixel.
     *
     * @return channel
     */
    public int getChannel();

    /**
     * Sets channel of the fitted pixel.
     *
     * @param channel
     */
    public void setChannel(int channel);
    
    /**
     * Gets x location of the fitted pixel.
     *
     * @return x
     */
    public int getX();

    /**
     * Sets x location of the fitted pixel.
     *
     * @param x
     */
    public void setX(int x);

    /**
     * Gets y location of the fitted pixel.
     *
     * @return y
     */
    public int getY();

    /**
     * Sets y location of the fitted pixel.
     *
     * @param y
     */
    public void setY(int y);

    /**
     * Sets how many pixels went into this data point.
     *
     * @param pixels
     */
    public void setPixels(int pixels);

    /**
     * Gets how many pixels went into this data point.
     *
     * @return number of pixels
     */
    public int getPixels();

    /**
     * Gets parameters of the fit.  Could represent multiple components.
     * Input and output to the fit.
     *
     * @return array of parameters
     */
    public double[] getParams();

    /**
     * Sets parameters of the fit.  Could represent multiple components.
     * Input and output to the fit.
     *
     * @param params array of parameters
     */
    public void setParams(double[] params);

    /**
     * Get input data for the fit.  Input to fit only.
     *
     * @return array of data
     */
    public double[] getTransient();

    /**
     * Get input data for the fit, starting with data start index.  Input to fit only.
     * 
     * @return array of data
     */
    public double[] getAdjustedTransient();

    /**
     * Set input data for the fit.  Input to fit only.
     *
     * @param yCount array of data
     */
    public void setYCount(double yCount[]);

    /**
     * Get error for the fit data.  Input to fit only.
     *
     * @return array of error data or null if not set.
     */
    public double[] getSig();

    /**
     * Set error for the fit data.  Input to fit only.
     *
     * @param sig array of error data.
     */
    public void setSig(double sig[]);
    
    /**
     * Gets fitted data from the fit.  Output from fit only.
     *
     * @return array of fitted data
     */
    public double[] getYFitted();

    /**
     * Sets fitted data from the fit.  Output from fit only.
     *
     * @param yFit array of fitted data
     */
    public void setYFitted(double yFit[]);

    /**
     * Gets start index in transient.
     * 
     * @return 
     */
    public int getTransStartIndex();
    
    /**
     * Sets start index in transient.
     * 
     * @param transStartIndex 
     */
    public void setTransStartIndex(int transStartIndex);
    
    /**
     * Gets fitting start index in transient.
     * 
     * @return 
     */
    public int getDataStartIndex();

    /**
     * Gets fitting start index adjusted for transient start.
     * 
     * @return 
     */
    public int getAdjustedDataStartIndex();
    
    /**
     * Sets fitting start index in transient.
     * 
     * @param dataStartIndex 
     */
    public void setDataStartIndex(int dataStartIndex);
    
    /**
     * Gets end index in transient.
     * 
     * @return 
     */
    public int getTransEndIndex();

    /**
     * Gets end index in transient adjusted for transient start.
     * 
     * @return 
     */
    public int getAdjustedTransEndIndex();
    
    /**
     * Sets end index in transient.
     * 
     * @param transEndIndex 
     */
    public void setTransEndIndex(int transEndIndex);

    /**
     * Gets chi square target.
     * 
     * @return targeted chi square
     */
    public double getChiSquareTarget();
    
    /**
     * Sets chi square target
     * 
     * @param chiSquare targeted chi square
     */
    public void setChiSquareTarget(double chiSquareTarget);

    /**
     * Sets chi square delta.
     * 
     * @return 
     */
    public double getChiSquareDelta();

    /**
     * Gets chi square delta.
     * 
     * @param chiSquareDelta 
     */
    public void setChiSquareDelta(double chiSquareDelta);

    /**
     * Gets fitted chi square.
     * 
     * @return fitted chi square
     */
    public double getChiSquare();
    
    /**
     * Sets fitted chi square
     * 
     * @param chiSquare fitted chi square
     */
    public void setChiSquare(double chiSquare);
}
