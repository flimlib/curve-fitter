//
// ICurveFitter.java
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
 * Interface for a curve fitter.
 *
 * @author Aivar Grislis
 */
public interface ICurveFitter {

    /**
     * Specifies curves that this fits.
     */
    public enum FitFunction {
        SINGLE_EXPONENTIAL, DOUBLE_EXPONENTIAL, TRIPLE_EXPONENTIAL, STRETCHED_EXPONENTIAL
    }

    /**
     * Default increment along x axis (evenly spaced).
     */
    public double DEFAULT_X_INC = 1.0f;

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
     * @return array of data or null if not set
     */
    public double[] getInstrumentResponse();

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

    /**
     * Do the fit.
     *
     * @param data array of data to fit
     * @param start first index to fit
     * @param stop last index to fit (inclusive)
     * @return status code
     */
    public int fitData(ICurveFitData[] data, int start, int stop);
}
