/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package loci.curvefitter;

import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

import ij.*;

/**
 *
 * @author aivar
 */
public class GrayNRCurveFitter extends AbstractCurveFitter {
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

    public GrayNRCurveFitter(int algType) {
        m_algType = algType;
    }

    public GrayNRCurveFitter() {
        m_algType = 0;
    }


    /**
     * @inheritDoc
     */
    public int fitData(ICurveFitData[] dataArray, int start, int stop) {
        int returnValue = 0;
 	CLibrary lib = (CLibrary) Native.loadLibrary("GrayNRCode", CLibrary.class);

        //TODO ARG 9/3/10 these issues still need to be addressed:

        //TODO ARG since initial x = fit_start * xincr we have to supply the unused portion of y[] before fit_start.
        // if this data were already premassaged it might be better to get rid of fit_start & _end, just give the
        // portion to be fitted and specify an initial x.
        //TODO ARG August use initial X of 0.

        DoubleByReference chiSquare = new DoubleByReference();
        double chiSquareTarget = 1.0; //TODO s/b specified incoming

        if (0 == m_algType) { //TODO crude; use enums
            // RLD or triple integral fit
            DoubleByReference z = new DoubleByReference();
            DoubleByReference a = new DoubleByReference();
            DoubleByReference tau = new DoubleByReference();

            double sig[] = new double[stop+1];
            for (int i = 0; i < sig.length; ++i) {
                sig[i] = 1.0; // basically ignoring sig
            }

            for (ICurveFitData data: dataArray) {
                // grab incoming parameters
                a.setValue(data.getParams()[0]);
                tau.setValue(1.0 / data.getParams()[1]); // convert lambda to tau
                z.setValue(data.getParams()[2]);

                returnValue = lib.RLD_fit(
                        m_xInc,
                        data.getYData(), //TODO data get data???
                        start,
                        stop,
                        null, // no instr
                        0,    // nInstr
                        null, // no sig
                        z,
                        a,
                        tau,
                        null, //fitted,
                        chiSquare,
                        chiSquareTarget
                        );

                // set outgoing parameters
                data.getParams()[0] = a.getValue();
                data.getParams()[1] = 1.0 / tau.getValue();
                data.getParams()[2] = z.getValue();
            }
        }
        else {
            // LMA fit
            for (ICurveFitData data: dataArray) {
                returnValue = lib.LMA_fit(
                        m_xInc,
                        data.getYData(),
                        start,
                        stop,
                        null, // no instr
                        0,    // nInstr
                        null, // no sig
                        data.getParams(),
                        toIntArray(data.getFree()),
                        data.getParams().length,
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
