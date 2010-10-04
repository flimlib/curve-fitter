//
// CurveFitData.java
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
 * TODO
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://dev.loci.wisc.edu/trac/java/browser/trunk/projects/curve-fitter/src/main/java/loci/curvefitter/CurveFitData.java">Trac</a>,
 * <a href="http://dev.loci.wisc.edu/svn/java/trunk/projects/curve-fitter/src/main/java/loci/curvefitter/CurveFitData.java">SVN</a></dd></dl>
 *
 * @author Aivar Grislis grislis at wisc.edu
 */
public class CurveFitData implements ICurveFitData {
    double[] m_params;
    boolean[] m_free;
    double[] m_yCount;
    double[] m_sig;
    double[] m_yFitted;
    Object m_userData;

    /**
     * @inheritDoc
     */
    public double[] getParams() {
        return m_params;
    }

    /**
     * @inheritDoc
     */
    public void setParams(double[] params) {
        m_params = params;
    }

    /**
     * @inheritDoc
     */
    public boolean[] getFree() {
        return m_free;
    }

    /**
     * @inheritDoc
     */
    public void setFree(boolean[] free) {
        m_free = free;
    }

    /**
     * @inheritDoc
     */
    public double[] getYCount() {
        return m_yCount;
    }

    /**
     * @inheritDoc
     */
    public void setYCount(double yCount[]) {
        m_yCount = yCount;
    }

    /**
     * @inheritDoc
     */
    public double[] getSig() {
        return m_sig;
    }

    /**
     * @inheritDoc
     */
    public void setSig(double sig[]) {
        m_sig = sig;
    }

    /**
     * @inheritDoc
     */
    public double[] getYFitted() {
        return m_yFitted;
    }

    /**
     * @inheritDoc
     */
    public void setYFitted(double yFitted[]) {
        m_yFitted = yFitted;
    }
}
