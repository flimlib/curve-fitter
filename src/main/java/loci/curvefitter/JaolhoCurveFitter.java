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

import jaolho.data.lma.LMA;
import jaolho.data.lma.LMAFunction;

/**
 * Curve fitter that uses the Jaolho curve fitting package.
 *
 * @author Aivar Grislis
 */
public class JaolhoCurveFitter extends AbstractCurveFitter {

  @Override
  public int fitData(ICurveFitData[] dataArray) {
    int start = dataArray[0].getDataStartIndex();
    int stop = dataArray[0].getTransEndIndex();
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

    if (ICurveFitter.FitFunction.STRETCHED_EXPONENTIAL.equals(getFitFunction())) {
      System.out.println("Stretched exponentials not supported in Jaolho at this time.");
      return 0;
    }
    function = new ExpFunction(getNumberComponents());

    for (ICurveFitData data: dataArray) {
      double yData[] = data.getTransient();
      for (int i = 0; i < length; ++i) {
        lmaData[1][i] = yData[start + i];
      }

      double inParams[] = data.getParams();
      double params[] = new double[inParams.length - 1];
      switch (getNumberComponents()) {
        case 1:
          params[0] = inParams[2]; // A1
          params[1] = inParams[3]; // T1
          params[2] = inParams[1]; // C
          break;
        case 2:
          params[0] = inParams[2]; // A1
          params[1] = inParams[3]; // T1
          params[2] = inParams[4]; // A2
          params[3] = inParams[5]; // T2
          params[4] = inParams[1]; // C
          break;
        case 3:
          params[0] = inParams[2]; // A1
          params[1] = inParams[3]; // T1
          params[2] = inParams[4]; // A2
          params[3] = inParams[5]; // T2
          params[4] = inParams[6]; // A3
          params[5] = inParams[7]; // T3
          params[6] = inParams[1]; // C
          break;
      }
      lma = new LMA(function, params, lmaData);

      try {
        lma.fit();
        ++goodPixels;
      }
      catch (Exception e) {
        ++badPixels;
        System.out.println("exception " + e);
      }
      for (int i = 0; i < length; ++i) {
        data.getYFitted()[start + i] = function.getY(lmaData[0][i], lma.parameters);
      }
      double outParams[] = data.getParams();
      switch (getNumberComponents()) {
        case 1:
          outParams[0] = lma.chi2;
          outParams[1] = params[2]; // C
          outParams[2] = params[0]; // A1
          outParams[3] = params[1]; // T1
          break;
        case 2:
          outParams[0] = lma.chi2;
          outParams[1] = params[4]; // C
          outParams[2] = params[0]; // A1
          outParams[3] = params[1]; // T1
          outParams[4] = params[2]; // A2
          outParams[5] = params[3]; // T2
          break;
        case 3:
          outParams[0] = lma.chi2;
          outParams[1] = params[6]; // C
          outParams[2] = params[0]; // A1
          outParams[3] = params[1]; // T1
          outParams[4] = params[2]; // A2
          outParams[5] = params[3]; // T2
          outParams[6] = params[4]; // A3
          outParams[7] = params[5]; // T3
          break;
      }
    }
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

    @Override
    public double getY(double x, double[] a) {
      double sum = 0;
      //System.out.println("numExp is " + numExp);
      //System.out.println("length of a is " + a.length); TODO length of a is # timeBins! s/b # params
      for (int e=0; e<numExp; e++) {
        double aTerm = a[2 * e];
        double bTerm = a[2 * e + 1];
        sum += aTerm * Math.exp(-x / bTerm);
      }
      double cTerm = a[2 * numExp];
      sum += cTerm;
      return sum;
    }

    @Override
    public double getPartialDerivate(double x, double[] a, int parameterIndex) {
      if (parameterIndex == 2 * numExp) return 1; // c term
      int e = parameterIndex / 2;
      int off = parameterIndex % 2;
      double aTerm = a[2 * e];
      double bTerm = a[2 * e + 1];
      switch (off) {
        case 0:
          return Math.exp(-x / bTerm); // a term
        case 1:
          return -aTerm * x * Math.exp(-x / bTerm); // b term
      }
      throw new RuntimeException("No such parameter index: " +
        parameterIndex);
    }
  }

}
