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
