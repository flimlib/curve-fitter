//
// CurveFitData.java
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

/**
 * TODO
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://dev.loci.wisc.edu/trac/software/browser/trunk/projects/curve-fitter/src/main/java/loci/curvefitter/CurveFitData.java">Trac</a>,
 * <a href="http://dev.loci.wisc.edu/svn/software/trunk/projects/curve-fitter/src/main/java/loci/curvefitter/CurveFitData.java">SVN</a></dd></dl>
 *
 * @author Aivar Grislis grislis at wisc.edu
 */
public class CurveFitData implements ICurveFitData {
    int _channel;
    int _x;
    int _y;
    int _pixels;
    double[] _params;
    double[] _yCount;
    double[] _sig;
    double[] _yFitted;
    int _transStartIndex;
    int _dataStartIndex;
    int _transEndIndex;
    double _chiSquareTarget;
    double _chiSquareDelta;
    double _chiSquare;
    Object _userData;

    @Override
    public int getChannel() {
        return _channel;
    }

    @Override
    public void setChannel(int channel) {
        _channel = channel;
    }

    @Override
    public int getX() {
        return _x;
    }

    @Override
    public void setX(int x) {
        _x = x;
    }

    @Override
    public int getY() {
        return _y;
    }

    @Override
    public void setY(int y) {
        _y = y;
    }

    @Override
    public int getPixels() {
        return _pixels;
    }

    @Override
    public void setPixels(int pixels) {
        _pixels = pixels;
    }

    @Override
    public double[] getParams() {
        return _params;
    }

    @Override
    public void setParams(double[] params) {
        _params = params;
    }

    @Override
    public double[] getTransient() {
        return _yCount;
    }
    
    @Override
    public double[] getAdjustedTransient() {
        int size = _transEndIndex - _transStartIndex;
        double[] adjusted = new double[size];
        for (int i = 0; i < size; ++i) {
            adjusted[i] = _yCount[i + _transStartIndex];
        }
        return adjusted;
    }

    @Override
    public void setYCount(double yCount[]) {
        _yCount = yCount;
    }

    @Override
    public double[] getSig() {
        return _sig;
    }

    @Override
    public void setSig(double sig[]) {
        _sig = sig;
    }

    @Override
    public double[] getYFitted() {
        return _yFitted;
    }

    @Override
    public void setYFitted(double yFitted[]) {
        _yFitted = yFitted;
    }

    @Override
    public int getTransStartIndex() {
        return _transStartIndex;
    }
    
    @Override
    public void setTransStartIndex(int transStartIndex) {
        System.out.println("********** set trans start " + transStartIndex);
        _transStartIndex = transStartIndex;
    }
    
    @Override
    public int getDataStartIndex() {
        return _dataStartIndex;
    }
    
    @Override
    public int getAdjustedDataStartIndex() {
        return _dataStartIndex - _transStartIndex;
    }
    
    @Override
    public void setDataStartIndex(int transFitStartIndex) {
        _dataStartIndex = transFitStartIndex;
    }
    
    @Override
    public int getTransEndIndex() {
        return _transEndIndex;
    }
    
    @Override
    public int getAdjustedTransEndIndex() {
        return _transEndIndex - _transStartIndex;
    }
    
    @Override
    public void setTransEndIndex(int transEndIndex) {
        _transEndIndex = transEndIndex;
    }

    @Override
    public double getChiSquareTarget() {
        return _chiSquareTarget;
    }

    @Override
    public void setChiSquareTarget(double chiSquareTarget) {
        _chiSquareTarget = chiSquareTarget;
    }
    
    @Override
    public double getChiSquareDelta() {
        return _chiSquareDelta;
    }
    
    @Override
    public void setChiSquareDelta(double chiSquareDelta) {
        _chiSquareDelta = chiSquareDelta;
    }
    
    @Override
    public double getChiSquare() {
        return _chiSquare;
    }

    @Override
    public void setChiSquare(double chiSquare) {
        _chiSquare = chiSquare;
    }
}
