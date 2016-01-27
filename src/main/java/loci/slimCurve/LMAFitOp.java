package loci.slimCurve;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.*;
import loci.slim.SLIMCurve;


@Plugin(type = Op.class, name = "LMAFitOp", priority = Priority.LOW_PRIORITY)
public class LMAFitOp extends AbstractOp {
	
	SLIMCurve slimCurve = new SLIMCurve();

	@Parameter (type = ItemIO.OUTPUT)
	private int out;
	@Parameter 
	private double xInc;
	@Parameter
	private double y[];
	@Parameter
	private int fitStart;
	@Parameter
	private int fitEnd;
	@Parameter
	private double instr[];
	@Parameter
	private int nInstr;
	@Parameter
	private int noise;
	@Parameter
	private double sig[];
	@Parameter
	private double param[]; //z, a, tau
	@Parameter
	private int paramFree[];
	@Parameter
	private int nParam = 3;
	@Parameter
	private double fitted[];
	@Parameter
	private double chiSquare[];
	@Parameter
	private double chiSquareTarget;
	@Parameter
	private double chiSquareDelta = .01;

	@Override
	public void run () {
		//Calls into the slimCurve library using JNI
		out = slimCurve.fitLMA(
				xInc, 
				y, 
				fitStart, 
				fitEnd, 
				instr, 
				nInstr, 
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
}


