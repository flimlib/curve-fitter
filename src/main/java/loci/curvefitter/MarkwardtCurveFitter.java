/*
 * #%L
 * Curve Fitter library for fitting exponential decay curves to sample data.
 * %%
 * Copyright (C) 2010 - 2014 Board of Regents of the University of
 * Wisconsin-Madison.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package loci.curvefitter;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * TODO
 *
 * @author Aivar Grislis
 */
public class MarkwardtCurveFitter extends AbstractCurveFitter {
  int m_algType;

  public interface CLibrary extends Library {
    public int markwardt_fit(double x_incr, double y[], int fit_start, int fit_end,
      double param[], int param_free[], int n_param);
  }

  @Override
  public int fitData(ICurveFitData[] dataArray) {
    int start = dataArray[0].getDataStartIndex();
    int stop = dataArray[0].getTransEndIndex();
    CLibrary lib = (CLibrary) Native.loadLibrary("Markwardt", CLibrary.class);

    for (ICurveFitData data: dataArray) {
      double y[] = data.getTransient();
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
