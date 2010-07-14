/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package loci.curvefitter;

import jaolho.data.lma.LMA;
import jaolho.data.lma.LMAFunction;

import ij.*;

/**
 *
 * @author Aivar Grislis
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
        
        function = new ExpFunction(1);
	//lma = new LMA(function, lmaWeights, lmaData);

        for (ICurveFitData data: dataArray) {
            double yData[] = data.getYData();
            for (int i = 0; i < length; ++i) {
                lmaData[1][i] = yData[start + i];
            }

            lma = new LMA(function, data.getParams(), lmaData);

     System.out.println("INCOMING param0 " + lma.parameters[0] + ", param1 " + lma.parameters[1] + ", param2 " + lma.parameters[2]);


            try {
                lma.fit();
                ++goodPixels;
                success = true;
            }
            catch (Exception e) {
                ++badPixels;
                success = false;
                System.out.println("exception " + e);
                IJ.log("exception " + e);
            }

            if (success) {
                System.out.println("iterations: " + lma.iterationCount);
                System.out.println("chi2: " + lma.chi2 + ", param0: " + lma.parameters[0] + ", param1: " + lma.parameters[1] + ", param2: " + lma.parameters[2]);
                IJ.log("iterations: " + lma.iterationCount);
                IJ.log("chi2: " + lma.chi2 + ", param0: " + lma.parameters[0] + ", param1: " + lma.parameters[1] + ", param2: " + lma.parameters[2]);
            }

            for (int i = 0; i < length; ++i) {
                data.getYFitted()[start + i] = function.getY(lmaData[0][i], lma.parameters);
            }
            for (int i = 0; i < data.getParams().length; ++i) {
                data.getParams()[i] = lma.parameters[i];
            }
        }
        System.out.println("goodPixels " + goodPixels + " badPixels " + badPixels);
        IJ.log("goodPixels " + goodPixels + " badPixels " + badPixels);
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
