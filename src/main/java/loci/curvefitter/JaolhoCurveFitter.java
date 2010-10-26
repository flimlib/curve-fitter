//
// JaolhoCurveFitter.java
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

import jaolho.data.lma.LMA;
import jaolho.data.lma.LMAFunction;

/**
 * TODO
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://dev.loci.wisc.edu/trac/java/browser/trunk/projects/curve-fitter/src/main/java/loci/curvefitter/JaolhoCurveFitter.java">Trac</a>,
 * <a href="http://dev.loci.wisc.edu/svn/java/trunk/projects/curve-fitter/src/main/java/loci/curvefitter/JaolhoCurveFitter.java">SVN</a></dd></dl>
 *
 * @author Aivar Grislis grislis at wisc.edu
 */
public class JaolhoCurveFitter extends AbstractCurveFitter {

    /**
     * @inheritDoc
     */
    public int fitData(ICurveFitData[] dataArray, int start, int stop) {
        boolean success;
        int goodPixels = 0;
        int badPixels = 0;
        double[][] lmaData;
        LMAFunction function;
        LMA lma;
        
        int length = stop - start + 1;
        lmaData = new double[2][length];
        double x_value = start * m_xInc;
        for (int i = 0; i < length; ++i) {
            lmaData[0][i] = x_value;
            x_value += m_xInc;
        }

        int numberExponentials = 0;
        int numberParams = dataArray[0].getParams().length;
        if (3 == numberParams) {
            numberExponentials = 1;
        }
        else if (5 == numberParams) {
            numberExponentials = 2;
        }
        else if (7 == numberParams) {
            numberExponentials = 3;
        }
        if (0 == numberExponentials) {
            System.out.println("invalid number of parameters: " + numberParams);
            return 0;
        }
        
        function = new ExpFunction(numberExponentials);
	//lma = new LMA(function, lmaWeights, lmaData);

        for (ICurveFitData data: dataArray) {
            double yData[] = data.getYCount();
            for (int i = 0; i < length; ++i) {
                lmaData[1][i] = yData[start + i];
            }

            lma = new LMA(function, data.getParams(), lmaData);

    // System.out.println("INCOMING param0 " + lma.parameters[0] + ", param1 " + lma.parameters[1] + ", param2 " + lma.parameters[2]);


            try {
                lma.fit();
                ++goodPixels;
                success = true;
            }
            catch (Exception e) {
                ++badPixels;
                success = false;
                System.out.println("exception " + e);
            }

            //if (lma.parameters[1] > 0.0f && lma.parameters[1] < 100.0f && lma.parameters[1] != 1.0f) {
            //    System.out.println("chi2: " + lma.chi2 + ", param0: " + lma.parameters[0] + ", param1: " + lma.parameters[1] + ", param2: " + lma.parameters[2]);
            //}

            //if (false && success) {
            //    System.out.println("iterations: " + lma.iterationCount);
            //    System.out.println("chi2: " + lma.chi2 + ", param0: " + lma.parameters[0] + ", param1: " + lma.parameters[1] + ", param2: " + lma.parameters[2]);
            //}

            for (int i = 0; i < length; ++i) {
                data.getYFitted()[start + i] = function.getY(lmaData[0][i], lma.parameters);
            }
            for (int i = 0; i < data.getParams().length; ++i) {
                data.getParams()[i] = lma.parameters[i];
            }
            data.setChiSquare(lma.chi2);
        }
        //System.out.println("goodPixels " + goodPixels + " badPixels " + badPixels);
        //TODO error return deserves more thought
        return 0;
    }

  /**
   * A summed exponential function of the form:
   * y(t) = a1*e^(-b1*t) + ... + an*e^(-bn*t) + c.
   * <p>
   * From loci.slim.fit.LMCurveFitter by Curtis Rueden.
   */
  public static class ExpFunction extends LMAFunction {

    /** Number of exponentials to fit. */
    private int numExp = 1;

    /** Constructs a function with the given number of summed exponentials. */
    public ExpFunction(int num) { numExp = num; }

    public double getY(double x, double[] a) {
      double sum = 0;
      //System.out.println("numExp is " + numExp);
      //System.out.println("length of a is " + a.length); TODO length of a is # timeBins! s/b # params
      for (int e=0; e<numExp; e++) {
        double aTerm = a[2 * e];
        double bTerm = a[2 * e + 1];
        sum += aTerm * Math.exp(-bTerm * x);
      }
      double cTerm = a[2 * numExp];
      sum += cTerm;
      return sum;
    }

    public double getPartialDerivate(double x, double[] a, int parameterIndex) {
      if (parameterIndex == 2 * numExp) return 1; // c term
      int e = parameterIndex / 2;
      int off = parameterIndex % 2;
      double aTerm = a[2 * e];
      double bTerm = a[2 * e + 1];
      switch (off) {
        case 0:
          return Math.exp(-bTerm * x); // a term
        case 1:
          return -aTerm * x * Math.exp(-bTerm * x); // b term
      }
      throw new RuntimeException("No such parameter index: " +
        parameterIndex);
    }
  }
    
}
