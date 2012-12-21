//
// SLIMCurveFitter.java
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

// used for JNA version
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.DoubleByReference;

import ij.IJ;

import org.scijava.nativelib.NativeLibraryUtil;

import loci.curvefitter.ICurveFitter.NoiseModel;

/**
 * This class is a Java wrapper around the SLIMCurve fitting C code.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://dev.loci.wisc.edu/trac/software/browser/trunk/projects/curve-fitter/src/main/java/loci/curvefitter/SLIMCurveFitter.java">Trac</a>,
 * <a href="http://dev.loci.wisc.edu/svn/software/trunk/projects/curve-fitter/src/main/java/loci/curvefitter/SLIMCurveFitter.java">SVN</a></dd></dl>
 *
 * @author Aivar Grislis grislis at wisc.edu
 */
public class SLIMCurveFitter extends AbstractCurveFitter {
    private static Object s_synchObject = new Object();
    private static volatile boolean s_libraryLoaded = false;
    private static boolean s_libraryOnPath = false;
    private static CLibrary s_library;
private static int counter = 0;
    /**
     * This interface supports loading the library using JNA.
     */
    public interface CLibrary extends Library {

        //TODO I'm omitting noise, s/b Poisson or Gaussian with lots of photons???
        //TODO I'm omitting residuals, see below also, same thing...

        public int RLD_fit(double xInc,
                           double y[],
                           int fitStart,
                           int fitEnd,
                           double instr[],
                           int nInstr,
                           int noise,
                           double sig[],
                           DoubleByReference z,
                           DoubleByReference a,
                           DoubleByReference tau,
                           double fitted[],
                           DoubleByReference chiSquare,
                           double chiSquareTarget
                           );

        //TODO also covar, alpha, errAxes and chiSqPercent
        //TODO I'm omitting residuals[] aren't residuals = y 0 yFitted??? is there some weighting I'm missing that is time-consuming/impossible to recreate?

        public int LMA_fit(double xInc,
                           double y[],
                           int fitStart,
                           int fitEnd,
                           double instr[],
                           int n_instr,
                           int noise,
                           double sig[],
                           double param[],
                           int paramFree[],
                           int nParam,
                           double fitted[],
                           DoubleByReference chiSquare,
                           double chiSquareTarget,
                           double chiSquareDelta
                           );
    }


    /**
     * This supports calling the library using JNI.
     *
     * @param xInc
     * @param y
     * @param fitStart
     * @param fitEnd
     * @param instr
     * @param nInstr
     * @param sig
     * @param z
     * @param a
     * @param tau
     * @param fitted
     * @param chiSquare
     * @param chiSquareTarget
     * @return
     */


   //TODO I'm omitting residuals, see below also, same thing...

    private native int RLD_fit(double xInc,
                           double y[],
                           int fitStart,
                           int fitEnd,
                           double instr[],
                           int nInstr,
                           int noise,
                           double sig[],
                           double z[],
                           double a[],
                           double tau[],
                           double fitted[],
                           double chiSquare[],
                           double chiSquareTarget
                           );

    /**
     * This supports calling the library using JNI.
     *
     * @param xInc
     * @param y
     * @param fitStart
     * @param fitEnd
     * @param instr
     * @param n_instr
     * @param sig
     * @param param
     * @param paramFree
     * @param nParam
     * @param fitted
     * @param chiSquare
     * @param chiSquareTarget
     * @return
     */
    private native int LMA_fit(double xInc,
                           double y[],
                           int fitStart,
                           int fitEnd,
                           double instr[],
                           int n_instr,
                           int noise,
                           double sig[],
                           double param[],
                           int paramFree[],
                           int nParam,
                           double fitted[],
                           double chiSquare[],
                           double chiSquareTarget,
                           double chiSquareDelta
                           );

    @Override
    public int fitData(ICurveFitData[] dataArray) {
        int returnValue = 0;
        int noise = getNoiseModel().ordinal();
        
        // Corresponds to TRI2 'normal' stopping criteria.  ('strict' is 0.0001
        //   and 'very strict' is 0.000001.)
        double chiSquareDelta = 0.01;

        // load the native library, if not already loaded
        if (!s_libraryLoaded) {

            synchronized (s_synchObject) {

                // check again to see if some other thread loaded it
                if (!s_libraryLoaded) {
                    
                    // look for library on path
                    try {
                        //System.out.println("Using JNA");
                        s_library = (CLibrary) Native.loadLibrary("slim-curve-1.0-SNAPSHOT", CLibrary.class);
                        s_libraryLoaded = true;
                        s_libraryOnPath = true;
                    }
                    catch (UnsatisfiedLinkError e) {
                        System.out.println("Library not on path " + e.getMessage());
                    } 
                }


                if (!s_libraryLoaded) {
                    // look for library in jar, using JNI
                    //System.out.println("Using JNI");
                    s_libraryLoaded = NativeLibraryUtil.loadVersionedNativeLibrary(this.getClass(), "slim-curve");
                }
            }
        }
        if (!s_libraryLoaded) {
            IJ.log("Native library not loaded.  Unable to do fit.");
            return 0;
        }
               
        // use arrays to pass double by reference
        double[] chiSquare = new double[1];
        double[] z         = new double[1];
        double[] a         = new double[1];
        double[] tau       = new double[1];

        // count number of free parameters
        int numParamFree = 0;
        for (boolean free : m_free) {
            if (free) {
                ++numParamFree;
            }
        }
		       
        if (FitAlgorithm.SLIMCURVE_RLD.equals(m_fitAlgorithm) || FitAlgorithm.SLIMCURVE_RLD_LMA.equals(m_fitAlgorithm)) {
            // RLD or triple integral fit

			//TODO ARG passing in array of data is broken
            for (ICurveFitData data: dataArray) {
                // set start and stop
                int start = data.getAdjustedDataStartIndex();
                int stop  = data.getAdjustedTransEndIndex();
                double[] trans = data.getAdjustedTransient();
 
                // initialize parameters to be fitted
                a[0]   = getEstimator().getDefaultA();
                tau[0] = getEstimator().getDefaultT();
                z[0]   = getEstimator().getDefaultZ();
                
                // these lines give more TRI2 compatible fit results
                int RLDnoise = noise;
                if (FitAlgorithm.SLIMCURVE_RLD_LMA.equals(m_fitAlgorithm)) {
                    start = getEstimator().getEstimateStartIndex
                                (trans, start, stop);
                    a[0]  = getEstimator().getEstimateAValue
                                (a[0], trans, start, stop);
                    NoiseModel noiseModel = NoiseModel.values()[noise];
                    RLDnoise = getEstimator().getEstimateNoiseModel(noiseModel).ordinal();
                }
                    
                int chiSquareAdjust = stop - start - 3;
                    
                returnValue = doRLDFit(
                        m_xInc,
                        trans,
                        start,
                        stop,
                        m_instrumentResponse,
                        null == m_instrumentResponse ? 0 : m_instrumentResponse.length,
                        RLDnoise,
                        data.getSig(),
                        z,
                        a,
                        tau,
                        data.getYFitted(),
                        chiSquare,
                        data.getChiSquareTarget() * chiSquareAdjust
                        );
				//System.out.println("RLD returns " + returnValue);

				//TODO actually RLD fitting won't return an error code; in an error situation it should recover with best guess probably
				//if (returnValue < 0) { System.out.println("RLD fails " + returnValue); ; return returnValue; }//TODO ARG don't do LMA if RLD fails
				//TODO actually TRI2 recovers from a bad RLD estimate
                              
                if (FitAlgorithm.SLIMCURVE_RLD.equals(m_fitAlgorithm)) {
                    // set outgoing parameters; note m_free ignored here
                    data.getParams()[0] = chiSquare[0] / chiSquareAdjust;
                    data.getParams()[1] = z[0];
                    data.getParams()[2] = a[0];
                    data.getParams()[3] = tau[0];
                }
            }
        }

        if (FitAlgorithm.SLIMCURVE_LMA.equals(m_fitAlgorithm) || FitAlgorithm.SLIMCURVE_RLD_LMA.equals(m_fitAlgorithm)) {

            //TODO ARG the idea of processing many ICurveFitData's in a loop is broken; here and elsewhere
            
            // if we are doing an LMA but just did an RLD estimate we may need to
            //   adjust those monoexponential RLD results to be initial estimates for
            //   a triexponential LMA fit, for example.
            if (FitAlgorithm.SLIMCURVE_RLD_LMA.equals(m_fitAlgorithm)) {
                getEstimator().adjustEstimatedParams
                            (dataArray[0].getParams(), m_free, m_fitFunction, a[0], tau[0], z[0]);
            }
            
            // LMA fit
            for (ICurveFitData data: dataArray) {
                    
                // set start and stop
                int start = data.getAdjustedDataStartIndex();
                int stop  = data.getAdjustedTransEndIndex();
                double[] trans = data.getAdjustedTransient();
                    
                int chiSquareAdjust = stop - start - numParamFree;
                
                returnValue = doLMAFit(
                        m_xInc,
                        trans,
                        start,
                        stop,
                        m_instrumentResponse,
                        null == m_instrumentResponse ? 0 : m_instrumentResponse.length,
                        noise,
                        data.getSig(),
                        data.getParams(),
                        toIntArray(m_free),
                        data.getParams().length - 1,
                        data.getYFitted(),
                        chiSquare,
                        data.getChiSquareTarget() * chiSquareAdjust,
                        chiSquareDelta
                        );
				
				//System.out.println("LMA fit returns chisq of " + chiSquare[0] + " or " + data.getParams()[0]);
				
				if (returnValue < 0 && returnValue != -2) {  // Error code -2 means "k > MAX_ITERS"
					// error, so params are meaningless
					for (int i = 0; i < data.getParams().length; ++i) {
						data.getParams()[i] = Double.NaN;
					}
				}
				else {
					// compute reduced chi square
                    data.getParams()[0] /= chiSquareAdjust;
					
					// compute AIC
					//System.out.println("chisq from LMA is " + chiSquare[0]);
					chiSquare[0] += 2 * numParamFree;
					data.setChiSquare(chiSquare[0]);
					//System.out.println(" -> becomes AIC " + chiSquare[0]);
					//TODO ARG since chisquare is returned in two places I am misusing one to be the AIC.
					//  if AIC based model selection takes off might be cleaner to add as a parameter?
				}
            }
        }

        return returnValue;

    }
 
    /*
     * Does the RLD fit according to whether the library is accessed via JNA or
     * JNI.
     */
    private int doRLDFit(double xInc,
                         double y[],
                         int fitStart,
                         int fitEnd,
                         double instr[],
                         int nInstr,
                         int noise,
                         double sig[],
                         double z[],
                         double a[],
                         double tau[],
                         double fitted[],
                         double chiSquare[],
                         double chiSquareTarget
                         )
    {
        int returnValue = 0;
        if (s_libraryOnPath) {
            // JNA version
            
            DoubleByReference zRef         = new DoubleByReference(z[0]);
            DoubleByReference aRef         = new DoubleByReference(a[0]);
            DoubleByReference tauRef       = new DoubleByReference(tau[0]);
            DoubleByReference chiSquareRef = new DoubleByReference(chiSquare[0]);
            
            returnValue = s_library.RLD_fit(
                    xInc,
                    y,
                    fitStart,
                    fitEnd,
                    instr,
                    nInstr,
                    noise,
                    sig,
                    zRef,
                    aRef,
                    tauRef,
                    fitted,
                    chiSquareRef,
                    chiSquareTarget);
            
            z[0]         = zRef.getValue();
            a[0]         = aRef.getValue();
            tau[0]       = tauRef.getValue();
            chiSquare[0] = chiSquareRef.getValue();
        }
        else {
            // JNI version
            
            returnValue = RLD_fit(
                    xInc,
                    y,
                    fitStart,
                    fitEnd,
                    instr,
                    nInstr,
                    noise,
                    sig,
                    z,
                    a,
                    tau,
                    fitted,
                    chiSquare,
                    chiSquareTarget);
        }
        return returnValue;
    }
 
    /*
     * Does the LMA fit according to whether the library is accessed via JNA or
     * JNI.
     */
    private int doLMAFit(double xInc,
                         double y[],
                         int fitStart,
                         int fitEnd,
                         double instr[],
                         int n_instr,
                         int noise,
                         double sig[],
                         double param[],
                         int paramFree[],
                         int nParam,
                         double fitted[],
                         double chiSquare[],
                         double chiSquareTarget,
                         double chiSquareDelta
                         )
    {
        int returnValue = 0;
        
        if (s_libraryOnPath) {
            // JNA version
            
            DoubleByReference chiSquareRef = new DoubleByReference(chiSquare[0]);
            
            returnValue = s_library.LMA_fit(
                    xInc,
                    y,
                    fitStart,
                    fitEnd,
                    instr,
                    n_instr,
                    noise,
                    sig,
                    param,
                    paramFree,
                    nParam,
                    fitted,
                    chiSquareRef,
                    chiSquareTarget,
                    chiSquareDelta);
            
            chiSquare[0] = chiSquareRef.getValue();
        }
        else {
            // JNI version
            
            returnValue = LMA_fit(
                    xInc,
                    y,
                    fitStart,
                    fitEnd,
                    instr,
                    n_instr,
                    noise,
                    sig,
                    param,
                    paramFree,
                    nParam,
                    fitted,
                    chiSquare,
                    chiSquareTarget,
                    chiSquareDelta);
        }
        return returnValue;  
    }

    private int[] toIntArray(boolean[] booleanArray) {
        int intArray[] = new int[booleanArray.length];
        for (int i = 0; i < booleanArray.length; ++i) {
            intArray[i] = (booleanArray[i] ? 1 : 0);
        }
        return intArray;
    }
}
