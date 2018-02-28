package org.github.mervyn.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ComputeCorrelationCoefficient {
	public static double getCorrelationCoefficient(List<Long> xElements, List<Long> yElements){
		long N = xElements.size();
		long sigmaX = sigmaOfElements(xElements);
		long sigmaY = sigmaOfElements(yElements);
		long sigmaPowerOfX = sigmaOfPowerOfElements(xElements);
		long sigmaPowerOfY = sigmaOfPowerOfElements(yElements);
				
		double dividend =  sigmaOfMultiplyOfXElementAndYElement(xElements,yElements)- ((double)sigmaX*sigmaY)/N;
		double factor1 = sigmaPowerOfX - Math.pow(sigmaX, 2)/ N;
		double factor2 = sigmaPowerOfY - Math.pow(sigmaY, 2)/ N;
		double divisor = Math.sqrt(factor1 * factor2);
		if(divisor == 0){
			return 1;
		}else{
			double result = dividend / divisor;
			return result;
		}
		
		
	}
	
	public static double getCorrelationCoefficientDouble(List<Double> xElements, List<Double> yElements){
		long N = xElements.size();
		double sigmaX = sigmaOfElementsDouble(xElements);
		double sigmaY = sigmaOfElementsDouble(yElements);
		double sigmaPowerOfX = sigmaOfPowerOfElementsDouble(xElements);
		double sigmaPowerOfY = sigmaOfPowerOfElementsDouble(yElements);
				
		double dividend =  sigmaOfMultiplyOfXElementAndYElementDouble(xElements,yElements)- ((double)sigmaX*sigmaY)/N;
		double factor1 = sigmaPowerOfX - Math.pow(sigmaX, 2)/ N;
		double factor2 = sigmaPowerOfY - Math.pow(sigmaY, 2)/ N;
		double divisor = Math.sqrt(factor1 * factor2);
		if(divisor == 0){
			return 1;
		}else{
			double result = dividend / divisor;
			return result;
		}
		
		
	}
	
	public static double getCorrelationCoefficientLongDouble(List<Long> xElements, List<Double> yElements){
		long N = xElements.size();
		double sigmaX = sigmaOfElements(xElements);
		double sigmaY = sigmaOfElementsDouble(yElements);
		double sigmaPowerOfX = sigmaOfPowerOfElements(xElements);
		double sigmaPowerOfY = sigmaOfPowerOfElementsDouble(yElements);
				
		double dividend =  sigmaOfMultiplyOfXElementAndYElementLongDouble(xElements,yElements)- ((double)sigmaX*sigmaY)/N;
		double factor1 = sigmaPowerOfX - Math.pow(sigmaX, 2)/ N;
		double factor2 = sigmaPowerOfY - Math.pow(sigmaY, 2)/ N;
		double divisor = Math.sqrt(factor1 * factor2);
		if(divisor == 0){
			return 1;
		}else{
			double result = dividend / divisor;
			return result;
		}
		
		
	}
	
	
	private static long sigmaOfMultiplyOfXElementAndYElement(List<Long> xElements,List<Long> yElements){
		long sum = 0;
		for(int i = 0; i < xElements.size(); i++){
			sum = sum + xElements.get(i) * yElements.get(i);
		}
		return sum;
	}
	
	private static double sigmaOfMultiplyOfXElementAndYElementLongDouble(List<Long> xElements,List<Double> yElements){
		double sum = 0;
		for(int i = 0; i < xElements.size(); i++){
			sum = sum + xElements.get(i) * yElements.get(i);
		}
		return sum;
	}
	
	private static Double sigmaOfMultiplyOfXElementAndYElementDouble(List<Double> xElements,List<Double> yElements){
		double sum = 0;
		for(int i = 0; i < xElements.size(); i++){
			sum = sum + xElements.get(i) * yElements.get(i);
		}
		return sum;
	}
	private static long sigmaOfElements(List<Long> elements){
		long sum = 0;
		for(int i = 0; i < elements.size(); i++){
			sum += elements.get(i);
		}
		return sum;
	}
	private static double sigmaOfElementsDouble(List<Double> elements){
		double sum = 0;
		for(int i = 0; i < elements.size(); i++){
			sum += elements.get(i);
		}
		return sum;
	}
	private static long sigmaOfPowerOfElements(List<Long> elements){
		long sum = 0;
		for(int i = 0; i < elements.size(); i++){
			sum = (long) (sum + Math.pow(elements.get(i),2));
		}
		return sum;
	}
	
	private static double sigmaOfPowerOfElementsDouble(List<Double> elements){
		double sum = 0;
		for(int i = 0; i < elements.size(); i++){
			//sum = (long) (sum + Math.pow(elements.get(i),2));
			sum =  (sum + Math.pow(elements.get(i),2));
		}
		return sum;
	}
	
	
	
	@Test
	public void testSigmaOfMultiplyOfXElementAndYElement(){
		List<Long> xElements = new ArrayList<Long>();
		xElements.add(1L);
		xElements.add(2L);
		xElements.add(3L);
		List<Long> yElements = new ArrayList<Long>();
		yElements.add(4L);
		yElements.add(1L);
		yElements.add(3L);
		Assert.assertEquals(15L, ComputeCorrelationCoefficient.sigmaOfMultiplyOfXElementAndYElement(xElements,yElements));
	}
	
	
	@Test
	public void testSigmaOfElements(){
		List<Long> elements = new ArrayList<Long>();
		elements.add(1L);
		elements.add(2L);
		elements.add(3L);
		Assert.assertEquals(6L, ComputeCorrelationCoefficient.sigmaOfElements(elements));
	}
	
	@Test
	public void testSigmaOfPowerOfElements(){
		List<Long> elements = new ArrayList<Long>();
		elements.add(1L);
		elements.add(2L);
		elements.add(3L);
		Assert.assertEquals(14, ComputeCorrelationCoefficient.sigmaOfPowerOfElements(elements));
	}
}
