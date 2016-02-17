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

// used for JNA version
import com.sun.jna.Library;
import com.sun.jna.ptr.DoubleByReference;

import net.imagej.ImageJ;
//import loci.curvefittertest.ImageJ;
//import loci.slim.NarSystem;
import net.imagej.ops.DefaultOpService;
import net.imagej.ops.OpService;

/**
 * This class is a Java wrapper around the SLIMCurve fitting C code.
 *
 * @author Aivar Grislis
 */
public class SLIMCurveFitter extends AbstractCurveFitter {

	
	private static ImageJ ij = new ImageJ();  //TODO better way to do this? ex: DefaultOpServic?
    OpService ops = ij.op();
//	DefaultOpService ops = new DefaultOpService();
	private static Object s_synchObject = new Object();
	private static volatile boolean s_libraryLoaded = false;
	private static boolean s_libraryOnPath = false;
	private static CLibrary s_library;
	private static int counter = 0;
	/**
	 * This interface supports loading the library using JNA.
	 */

//	static { //TODO: not needed?
//		NarSystem.loadLibrary();
//		//System.err.println("SLIM Curve loaded!");
//	}

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

	//    static {
	//		NarSystem.loadLibrary();
	//	}

	@Override
	public int fitData(ICurveFitData[] dataArray) {
		int returnValue = 0;
		int noise = getNoiseModel().ordinal();
		//for (int i = 0; i < dataArray.length; i++)
		// System.out.println("index " + i + ": " + dataArray[i]); 
		// Corresponds to TRI2 'normal' stopping criteria.  ('strict' is 0.0001
		//   and 'very strict' is 0.000001.)
		double chiSquareDelta = 0.01;

		// load the native library, if not already loaded
		//        if (!s_libraryLoaded) {
		//
		//            synchronized (s_synchObject) {
		//
		//                // check again to see if some other thread loaded it
		//                if (!s_libraryLoaded) {
		//                    
		//                    // look for library on path
		//                    try {
		//                        //System.out.println("Using JNA");
		//                        s_library = (CLibrary) Native.loadLibrary("slim-curve-1.0-SNAPSHOT", CLibrary.class);
		//                        s_libraryLoaded = true;
		//                        s_libraryOnPath = true;
		//                    }
		//                    catch (UnsatisfiedLinkError e) {
		//                        System.out.println("Library not on path " + e.getMessage());
		//                    } 
		//                }
		//
		//
		//                if (!s_libraryLoaded) {
		//                    // look for library in jar, using JNI
		//                    System.out.println("Using JNI");
		//                    //TODO: issue here when calling NativeLibraryUtil
		//                    s_libraryLoaded = NativeLibraryUtil.loadVersionedNativeLibrary(this.getClass(), "slim-curve"); 
		//                    //s_libraryLoaded = NarSystem.loadLibrary();
		//
		//                }
		//            }
		//        }
		//        if (!s_libraryLoaded) {
		//          //  IJ.log("Native library not loaded.  Unable to do fit.");
		//            return 0;
		//        }

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
				double[] sig = {};
				if (data.getSig() != null)
					sig = data.getSig();


				returnValue = doRLDFit(
						m_xInc,
						trans,
						start,
						stop,
						m_instrumentResponse,
						null == m_instrumentResponse ? 0 : m_instrumentResponse.length,
								RLDnoise,
								sig,
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
				
				double[] sig = {};
				if (data.getSig() != null)
					sig = data.getSig();

				returnValue = doLMAFit(
						m_xInc,
						trans,
						start,
						stop,
						m_instrumentResponse,
						null == m_instrumentResponse ? 0 : m_instrumentResponse.length,
								noise, //check
								sig,
								data.getParams(), //check
								toIntArray(m_free), //check
								data.getParams().length - 1, //check
								data.getYFitted(),
								chiSquare,
								data.getChiSquareTarget() * chiSquareAdjust,
								chiSquareDelta
						);
				//System.out.println("LMA returns " + returnValue);
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
	//private
	public int doRLDFit(double xInc,
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
			returnValue = (Integer) ops.run("RLDFitOp", 
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
			returnValue = (Integer) ops.run("LMAFitOp", //TODO issue passing in null
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
					chiSquareDelta 
					);
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
