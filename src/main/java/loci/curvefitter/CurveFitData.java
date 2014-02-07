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

/**
 * TODO
 *
 * @author Aivar Grislis
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
