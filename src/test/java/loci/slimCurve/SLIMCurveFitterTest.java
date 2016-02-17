package loci.slimCurve;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import loci.curvefitter.ICurveFitter.FitAlgorithm;
import loci.curvefitter.ICurveFitter.NoiseModel;
import loci.curvefitter.CurveFitData;
import loci.curvefitter.SLIMCurveFitter;
//import loci.slim.NarSystem;
//import loci.slim.SLIMCurve;
//import loci.curvefittertest.CurveFitData;
//import loci.curvefittertest.ICurveFitter.FitAlgorithm;
//import loci.curvefittertest.ICurveFitter.NoiseModel;
//import loci.curvefittertest.SLIMCurveFitter;

public class SLIMCurveFitterTest {

//	static {
//		NarSystem.loadLibrary();
//	}


	//TODO: performs tests based on test.ini and data.dat.  Extend to be more general
	static double[] adjustedDecay = {
			0.0,  1.0, 0.0, 0.0, 2.0, 
			1.0, 1.0, 2.0, 1.0, 1.0, 5.0,
			9.0, 10.0, 18.0, 17.0, 17.0, 35.0,
			37.0, 32.0, 33.0, 28.0, 39.0, 36.0,
			29.0, 32.0, 37.0, 38.0, 27.0, 31.0,
			30.0, 32.0, 26.0, 29.0, 25.0, 25.0,
			25.0, 21.0, 35.0, 23.0, 13.0, 15.0,
			21.0, 18.0, 8.0, 16.0, 14.0, 20.0,
			12.0, 18.0, 17.0, 17.0, 13.0, 15.0,
			14.0, 16.0, 12.0, 18.0, 14.0, 10.0,
			8.0, 10.0, 18.0, 7.0, 10.0, 8.0,
			11.0, 11.0, 12.0, 10.0, 13.0, 7.0,
			15.0, 8.0, 6.0, 10.0, 8.0, 7.0,
			9.0, 11.0, 15.0, 6.0, 6.0, 10.0,
			3.0, 8.0, 5.0, 7.0, 9.0, 7.0,
			5.0, 3.0, 5.0, 4.0, 6.0, 5.0,
			6.0, 7.0, 5.0, 8.0, 3.0, 11.0,
			5.0, 5.0, 7.0, 10.0, 3.0, 6.0,
			11.0, 5.0, 10.0, 3.0, 5.0, 4.0,
			7.0, 2.0, 3.0, 3.0, 4.0, 4.0,
			4.0, 5.0, 9.0, 8.0, 5.0, 7.0, 
			5.0, 4.0, 2.0, 9.0, 5.0,
			2.0, 3.0, 7.0, 5.0, 4.0, 4.0,
			0.0, 3.0, 5.0, 6.0, 7.0, 2.0,
			2.0, 0.0, 5.0, 6.0, 1.0, 7.0,
			5.0, 5.0, 1.0, 8.0, 4.0, 3.0,
			7.0, 3.0, 1.0, 3.0, 2.0, 0.0,
			2.0, 9.0, 3.0, 3.0, 3.0, 3.0,
			0.0, 3.0, 2.0, 3.0, 4.0, 5.0,
			2.0, 1.0, 1.0, 1.0, 2.0, 3.0,
			4.0, 2.0, 1.0, 4.0, 2.0, 3.0,
			2.0, 4.0, 1.0, 1.0, 6.0, 1.0,
			3.0, 0.0, 2.0, 2.0, 3.0, 1.0,
			0.0, 1.0, 2.0, 1.0, 1.0, 2.0,
			2.0, 3.0, 3.0, 4.0, 0.0, 2.0,
			2.0, 1.0, 0.0, 0.0, 3.0 
	};
	static double[] yFitted = { 
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,	
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0
	};

	static double adjustedPrompt[] = {
			0.002349222620878183, 0.006073808303434138, 0.007004954724073127,
			0.014454126089185037, 0.01352297966854605, 0.01352297966854605, 0.03028361524004785, 0.03214590808132583, 0.02749017597813088,
			0.02842132239876987, 0.023765590295574925, 0.0340082009226038, 0.031214761660686836, 0.024696736716213915, 0.02749017597813088,
			0.03214590808132583, 0.03307705450196481, 0.02283444387493594, 0.026559029557491894, 0.025627883136852905, 0.02749017597813088,
			0.02190329745429695, 0.024696736716213915, 0.02097215103365796, 0.02097215103365796, 0.02097215103365796, 0.017247565351102005,
			0.03028361524004785, 0.019109858192379984, 0.009798393985990094, 0.011660686827268072, 0.017247565351102005, 0.014454126089185037,
			0.005142661882795149, 0.01259183324790706, 0.010729540406629082, 0.016316418930463015, 0.008867247565351104, 0.014454126089185037,
			0.01352297966854605, 0.01352297966854605, 0.009798393985990094, 0.011660686827268072, 0.010729540406629082, 0.01259183324790706,
			0.008867247565351104, 0.014454126089185037, 0.010729540406629082, 0.007004954724073127, 0.005142661882795149, 0.007004954724073127,
			0.014454126089185037, 0.0042115154621561604, 0.007004954724073127, 0.005142661882795149, 0.007936101144712117, 0.007936101144712117,
			0.008867247565351104, 0.007004954724073127, 0.009798393985990094, 0.0042115154621561604, 0.011660686827268072, 0.005142661882795149
	};
	static int fitStart = 0;
	static int fitStop = 213;
	static double chiSqTarget = 1.5;

	//static SLIMCurve slimCurve = new SLIMCurve();
	static CurveFitData curveFitData = new CurveFitData();
	final static CurveFitData[] data = new CurveFitData[] { curveFitData };
	static SLIMCurveFitter curveFitter = new SLIMCurveFitter();
	
	@BeforeClass
	public static void setUp() {
		final double param[] = new double[4];
		curveFitData.setParams(param);
		curveFitData.setYCount(adjustedDecay);
		curveFitData.setTransStartIndex(0);
		curveFitData.setDataStartIndex(fitStart);
		curveFitData.setTransEndIndex(fitStop);
		curveFitData.setChiSquareTarget(chiSqTarget); // TODO this adjustment
		curveFitData.setSig(null);
		curveFitData.setYFitted(yFitted);

		double xInc = 0.048828125;
		final boolean free[] = new boolean[] { true, true, true };
		//curveFitter.setFitAlgorithm(FitAlgorithm.SLIMCURVE_RLD);
		curveFitter.setXInc(xInc);
		curveFitter.setFree(free);
		curveFitter.setInstrumentResponse(adjustedPrompt);
		curveFitter.setNoiseModel(NoiseModel.POISSON_FIT);
	}

	/** Tests {@link SLIMCurve#fitRLD}. */
	@Test
	public void testFitRLD() {
		curveFitter.setFitAlgorithm(FitAlgorithm.SLIMCURVE_RLD);
		int ret = curveFitter.fitData(data);

		System.out.println("SLIMCurveFitter RLD return value: " + ret);
		System.out.println("A: " + curveFitData.getParams()[2] + " Tau: " + curveFitData.getParams()[3] + " Z: " + curveFitData.getParams()[1] + " X^2: " + curveFitData.getParams()[0]);
		int _rld = 3;
		int _a = 16; //16.308807373046875
		int _tau = 2; //2.768864154815674
		int _z = 1; //1.911532998085022
		int _x2 = 6; //6.781553431919643
		assertEquals("rld value is not correct", _rld, ret);
		assertEquals("a value is not correct", _a, (int) curveFitData.getParams()[2]);
		assertEquals("tau value is not correct", _tau, (int) curveFitData.getParams()[3]);
		assertEquals("z value is not correct", _z, (int) curveFitData.getParams()[1]);
		assertEquals("Chi squared value is not correct", _x2, (int) curveFitData.getParams()[0]);
	}

	/** Tests {@link SLIMCurve#fitLMA}. */
	@Test
	public void testFitLMA() {
		curveFitter.setFitAlgorithm(FitAlgorithm.SLIMCURVE_LMA);
		final double params[] = {16.308807373046875,
				2.768864154815674,
				1.911532998085022,
				 6.781553431919643};
		curveFitData.setParams(params);
		int ret = curveFitter.fitData(data);
		
		System.out.println("SLIMCurveFitter LMA return value: " + ret);
		System.out.println("A: " + curveFitData.getParams()[2] + " Tau: " + curveFitData.getParams()[3] + " Z: " + curveFitData.getParams()[1] + " X^2: " + curveFitData.getParams()[0]);
		int _lma = 20;
		int _a = 0; //0.7064773440361023
		int _tau = 6; //6.781553431919643
		int _z = 77; //77.43111419677734
		int _x2 = 0; //0.014307794116792224
		assertEquals("rld value is not correct", _lma, ret);
		assertEquals("a value is not correct", _a, (int) curveFitData.getParams()[2]);
		assertEquals("tau value is not correct", _tau, (int) curveFitData.getParams()[3]);
		assertEquals("z value is not correct", _z, (int) curveFitData.getParams()[1]);
		assertEquals("Chi squared value is not correct", _x2, (int) curveFitData.getParams()[0]);
	
	}

}

