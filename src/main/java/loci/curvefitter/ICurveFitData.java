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
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://dev.loci.wisc.edu/trac/java/browser/trunk/projects/curve-fitter/src/main/java/loci/curvefitter/ICurveFitData.java">Trac</a>,
 * <a href="http://dev.loci.wisc.edu/svn/java/trunk/projects/curve-fitter/src/main/java/loci/curvefitter/ICurveFitData.java">SVN</a></dd></dl>
 *
 * @author Aivar Grislis grislis at wisc.edu
 */
//TODO fix params
//TODO num components?
//TODO SP has int for y data, x c/b int too; double m/b useful for advanced binning
public interface ICurveFitData {

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
     * Gets which parameters are free (vs. fixed). Input to fit only.
     *
     * @return array of booleans
     */
    public boolean[] getFree();

    /**
     * Sets which parameters are free (vs. fixed).  Input to fit only.
     */
    public void setFree(boolean[] free);

    /**
     * Get input data for the fit.  Input to fit only.
     *
     * @return array of data
     */
    public double[] getYData();

    /**
     * Set input data for the fit.  Input to fit only.
     *
     * @param yData array of data
     */
    public void setYData(double yData[]);
    
    /**
     * Gets fitted data from the fit.  Output from fit only.
     *
     * @return array of fitted data
     */
    public double[] getYFitted(); //TODO is this efficient for OpenCL???? s/b asynchronous, don't wait for results???

    /**
     * Sets fitted data from the fit.  Output from fit only.
     *
     * @param yFit array of fitted data
     */
    public void setYFitted(double yFit[]);

    /**
     * Keeps track of user data on behalf of caller.
     *
     * @return user data
     */
    public Object getUserData();

    /**
     * Keeps track of user data on behalf of caller.
     *
     * @param data user data
     */
    public void setUserData(Object data);

}
