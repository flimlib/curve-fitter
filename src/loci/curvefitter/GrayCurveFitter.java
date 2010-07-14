/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package loci.curvefitter;

import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

import ij.*;

/**
 *
 * @author aivar
 */
public class GrayCurveFitter extends AbstractCurveFitter {

    public interface CLibrary extends Library {

        public int nr_GCI_triple_integral_fitting_engine(float xincr, float y[], int fit_start, int fit_end,
									   float instr[], int nInstr, int noise, float sig[],
                                                                           FloatByReference z, FloatByReference a, FloatByReference tau,
                                                                           float fitted[], float residuals[],
                                                                           FloatByReference chiSq, float chiSqTarget);
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











    /**
     * @inheritDoc
     */
    public int fitData(ICurveFitData[] dataArray, int start, int stop) {

 	CLibrary lib = (CLibrary) Native.loadLibrary("GrayCode", CLibrary.class);

        //TODO ARG since initial x = fit_start * xincr we have to supply the unused portion of y[] before fit_start.
        // if this data were already premassaged it might be better to get rid of fit_start & _end, just give the
        // portion to be fitted and specify an initial x.

        float xincr = (float) m_xInc;
        int fit_start = start;
        int fit_end = stop;
        float instr[] = new float[0];
        int nInstr = 0;
        int noise = 0;
        float sig[] = new float[stop+1];
        for (int i = 0; i < sig.length; ++i) {
            sig[i] = 1.0f;
        }
        FloatByReference z = new FloatByReference(1.0f);
        FloatByReference a = new FloatByReference(1.0f);
        FloatByReference tau = new FloatByReference(1.0f);
        float fitted[] = new float[stop+1];
        float residuals[] = new float[stop+1];
        FloatByReference chiSq = new FloatByReference();
        float chiSqTarget = 2.0f;

        boolean success;
        int goodPixels = 0;
        int badPixels = 0;




        double[][] lmaData;

        int length = stop - start + 1;
        lmaData = new double[2][length];
        double x_value = start * m_xInc;
        for (int i = 0; i < length; ++i) {
            lmaData[0][i] = x_value;
            x_value += m_xInc;
        }

        for (ICurveFitData data: dataArray) {
            float y[] = doubleToFloat(data.getYData());



            int returnValue = lib.nr_GCI_triple_integral_fitting_engine(xincr, y, fit_start, fit_end,
									   instr, nInstr, noise, sig,
                                                                           z, a, tau,
                                                                           fitted, residuals,
                                                                           chiSq, chiSqTarget);
            System.out.println("returnValue " + returnValue + " z " + z.getValue() + " a " + a.getValue() + " tau " + tau.getValue() + " chiSq " + chiSq.getValue());
        }

        //TODO error return deserves more thought
        return 0;
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
