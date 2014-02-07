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

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.DoubleByReference;

/**
 * TODO
 *
 * @author Aivar Grislis
 */
public class GrayNRCurveFitter extends AbstractCurveFitter {
    static CLibrary s_library;
    int m_algType;

    public interface CLibrary extends Library {

        //TODO I'm omitting noise, s/b Poisson or Gaussian with lots of photons???
        //TODO I'm omitting residuals, see below also, same thing...

        public int RLD_fit(double xInc,
                           double y[],
                           int fitStart,
                           int fitEnd,
                           double instr[],
                           int nInstr,
                           double sig[],
                           DoubleByReference z,
                           DoubleByReference a,
                           DoubleByReference tau,
                           double fitted[],
                           DoubleByReference chiSquare,
                           double chiSquareTarget
                           );

        //TODO I'm omitted noise, see above and restrainType and fitType, for now
        //TODO also covar, alpha, errAxes and chiSqPercent
        //TODO I'm omitting residuals[] aren't residuals = y 0 yFitted??? is there some weighting I'm missing that is time-consuming/impossible to recreate?

        public int LMA_fit(double xInc,
                           double y[],
                           int fitStart,
                           int fitEnd,
                           double instr[],
                           int n_instr,
                           double sig[],
                           double param[],
                           int paramFree[],
                           int nParam,
                           double fitted[],
                           DoubleByReference chiSquare,
                           double chiSquareTarget
                           );


        /* public int nr_GCI_triple_integral_fitting_engine(float xincr, float y[], int fitStart, int fitEnd,
									   float instr[], int nInstr, int noise, float sig[],
                                                                           FloatByReference z, FloatByReference a, FloatByReference tau,
                                                                           float fitted[], float residuals[],
                                                                           FloatByReference chiSq, float chiSqTarget);

           public int nr_GCI_marquardt_fitting_engine(float xincr, float y[], int nData, int fitStart, int fitEnd,
                                                                           float instr[], int nInstr, int noise, float sig[],
                                                                           float param[], int paramFree[], int nParam,
                                                                           int restrainType, int fitType,
                                                                           float fitted[], float residuals[],
                                                                           FloatByReference chiSq);*/
        //,
        //                                                                   float covar[], float alpha[], float errAxes[],
        //                                                                   float chiSqTarget, int chiSqPercent);


        /*public int nr_GCI_marquardt_fitting_engine(float xincr, float *trans, int ndata, int fit_start, int fit_end,
								 float prompt[], int nprompt, //TODO ARG is this actually instr[] & ninstr?
								 noise_type noise, float sig[],
								 float param[], int paramfree[],
								 int nparam, restrain_type restrain,
								 fit_type fit, //TODO ARG void (*fitfunc)(float, float [], float *, float [], int),
								 float *fitted, float *residuals, float *chisq,
								 float **covar, float **alpha, float **erraxes,
									float chisq_target, int chisq_percent) {*/
    }


    @Override
    public int fitData(ICurveFitData[] dataArray) {
        int start = dataArray[0].getDataStartIndex();
        int stop = dataArray[0].getTransEndIndex();
        int returnValue = 0;
        if (null == s_library) {
            try {
                // load once, on-demand
                s_library = (CLibrary) Native.loadLibrary("GrayNRCode", CLibrary.class);

                System.out.println("s_library is " + s_library);
            }
            catch (UnsatisfiedLinkError e) {
                System.out.println("unable to load dynamic library " + e.getMessage());
                return 0;
            }
        }

        //TODO ARG 9/3/10 these issues still need to be addressed:

        //TODO ARG since initial x = fit_start * xincr we have to supply the unused portion of y[] before fit_start.
        // if this data were already premassaged it might be better to get rid of fit_start & _end, just give the
        // portion to be fitted and specify an initial x.
        //TODO ARG August use initial X of 0.

        DoubleByReference chiSquare = new DoubleByReference();
        double chiSquareTarget = 1.0; //TODO s/b specified incoming
        
        double sig[] = new double[stop+1];
        for (int i = 0; i < sig.length; ++i) {
            sig[i] = 1.0; // basically ignoring sig for now
        }

        if (FitAlgorithm.SLIMCURVE_RLD.equals(m_fitAlgorithm)) {
            // RLD or triple integral fit
            DoubleByReference z = new DoubleByReference();
            DoubleByReference a = new DoubleByReference();
            DoubleByReference tau = new DoubleByReference();

            for (ICurveFitData data: dataArray) {
                // grab incoming parameters
                a.setValue(data.getParams()[2]);
                tau.setValue(data.getParams()[3]);
                z.setValue(data.getParams()[1]);

                returnValue = s_library.RLD_fit(
                        m_xInc,
                        data.getTransient(),
                        start,
                        stop,
                        null, // no instr
                        0,    // nInstr
                        sig,
                        z,
                        a,
                        tau,
                        data.getYFitted(),
                        chiSquare,
                        chiSquareTarget
                        );

                // set outgoing parameters
                data.getParams()[0] = chiSquare.getValue();
                data.getParams()[1] = z.getValue();
                data.getParams()[2] = a.getValue();
                data.getParams()[3] = tau.getValue();

            }
        }
        else {
            // LMA fit
            for (ICurveFitData data: dataArray) {
                returnValue = s_library.LMA_fit(
                        m_xInc,
                        data.getTransient(),
                        start,
                        stop,
                        null, // no instr
                        0,    // nInstr
                        sig,
                        data.getParams(),
                        toIntArray(m_free),
                        data.getParams().length - 1,
                        data.getYFitted(),
                        chiSquare,
                        chiSquareTarget
                        );
            }
        }

        //TODO error return deserves more thought
        return returnValue;
    }

    int[] toIntArray(boolean[] booleanArray) {
        int intArray[] = new int[booleanArray.length];
        for (int i = 0; i < booleanArray.length; ++i) {
            intArray[i] = (booleanArray[i] ? 1 : 0);
        }
        return intArray;
    }

    double[] floatToDouble(float[] f) {
        double d[] = new double[f.length];
        for (int i = 0; i < f.length; ++i) {
            d[i] = f[i];
        }
        return d;
    }

    float[] doubleToFloat(double[] d) {
        float f[] = new float[d.length];
        for (int i = 0; i < d.length; ++i) {
            f[i] = (float) d[i];
        }
        return f;
    }

}
