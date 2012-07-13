//
// ICurveFitData.java
//

/*
Curve Fitter library for fitting exponential decay curves.

Copyright (c) 2010, UW-Madison LOCI
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the UW-Madison LOCI nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
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
