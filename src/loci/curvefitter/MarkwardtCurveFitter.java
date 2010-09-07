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
public class MarkwardtCurveFitter extends AbstractCurveFitter {
    int m_algType;

    public interface CLibrary extends Library {
        public int markwardt_fit(double x_incr, double y[], int fit_start, int fit_end,
                double param[], int param_free[], int n_param);
    }

    /**
     * @inheritDoc
     */
    public int fitData(ICurveFitData[] dataArray, int start, int stop) {

 	CLibrary lib = (CLibrary) Native.loadLibrary("Markwardt", CLibrary.class);

        for (ICurveFitData data: dataArray) {
            int nData = data.getYData().length;
            double y[] = data.getYData();
            int nParams = data.getParams().length;
            double params[] = data.getParams();
            int paramFree[] = new int[nParams];
            for (int i = 0; i < nParams; ++i) {
                paramFree[i] = 1;
            }

            int returnValue = lib.markwardt_fit(m_xInc, y, start, stop, params, paramFree, nParams);
        }

        //TODO error return deserves more thought
        return 0;
    }
}
