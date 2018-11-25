/*
 * Copyright (C) 2012 Alberto Irurueta Carro (alberto@irurueta.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.irurueta.algebra;

import java.io.Serializable;

/**
 * Class defining a Complex number having real and imaginary parts.
 */
@SuppressWarnings("WeakerAccess")
public class Complex implements Serializable {

    /**
     * Real part of the complex number.
     */
    private double real;
    
    /**
     * Imaginary part of the complex number.
     */
    private double imaginary;
    
    /**
     * Constructor. Sets both real and imaginary parts to zero.
     */
    public Complex() {
        real = imaginary = 0.0;
    }
    
    /**
     * Creates new Complex number having provided real value and an
     * imaginary part equal to zero.
     * @param real Real part.
     */
    public Complex(double real) {
        this.real = real;
        imaginary = 0.0;
    }
    
    /**
     * Creates new Complex number having provided real and imaginary parts.
     * @param real Real part.
     * @param imaginary Imaginary part.
     */
    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }
    
    /**
     * Creates new Complex number by copying provided value.
     * @param initValue Initial value to be copied.
     */
    public Complex(Complex initValue) {
        this.real = initValue.real;
        this.imaginary = initValue.imaginary;
    }
    
    /**
     * Returns real part of complex number.
     * @return Real part.
     */
    public double getReal() {
        return real;
    }
    
    /**
     * Sets real part of complex number.
     * @param real Real part.
     */
    public void setReal(double real) {
        this.real = real;
    }
    
    /**
     * Returns imaginary part of complex number.
     * @return Imaginary part.
     */
    public double getImaginary() {
        return imaginary;
    }
    
    /**
     * Sets imaginary part of complex number.
     * @param imaginary Imaginary part.
     */
    public void setImaginary(double imaginary) {
        this.imaginary = imaginary;
    }
    
    /**
     * Sets both real and imaginary parts of this complex number.
     * @param real Real part.
     * @param imaginary Imaginary part.
     */
    public void setRealAndImaginary(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }
    
    /**
     * Returns modulus of current complex number, which is equal to the
     * length of the vector formed by the real and imaginary parts.
     * @return Modulus.
     */
    public double getModulus() {
        return Math.sqrt(real * real + imaginary * imaginary);
    }
    
    /**
     * Returns phase of current complex number, in radians.
     * Phase is equal to the angle of the vector formed by the real and
     * imaginary parts.
     * @return Phase in radians.
     */
    public double getPhase() {
        return Math.atan2(imaginary, real);
    }
    
    /**
     * Sets both modulus and phase of this complex number.
     * @param modulus Modulus to be set.
     * @param phase Phase to be set in radians.
     */
    public void setModulusAndPhase(double modulus, double phase) {
        real = modulus * Math.cos(phase);
        imaginary = modulus * Math.sin(phase);
    }
    
    /**
     * Computes the complex conjugate of this instance and stores the
     * result into provided complex instance.
     * The complex conjugate is obtained by negating the sign of the
     * imaginary part.
     * @param result Complex instance where conjugate is stored.
     */
    public void conjugate(Complex result) {
        result.real = real;
        result.imaginary = -imaginary;
    }
    
    /**
     * Computes the complex conjugate of this instance and returns the
     * result as a new instance.
     * @return Complex conjugate.
     */
    public Complex conjugateAndReturnNew() {
        Complex result = new Complex();
        conjugate(result);
        return result;
    }
    
    /**
     * Changes this instance into its complex conjugate.
     */
    public void conjugate() {
        conjugate(this);
    }        
    
    /**
     * Adds this instance with provided complex value and stores the result
     * in provided instance
     * @param other Complex to be added to current instance
     * @param result Complex instance where result is stored.
     */
    public void add(Complex other, Complex result) {
        result.real = real + other.real;
        result.imaginary = imaginary + other.imaginary;
    }
    
    /**
     * Adds this instance to provided complex and returns the result as a new
     * instance.
     * @param other Complex to be added to current instance.
     * @return Result of summation
     */
    public Complex addAndReturnNew(Complex other) {
        Complex result = new Complex();
        add(other, result);
        return result;
    }
    
    /**
     * Adds provided complex into this instance
     * @param other Complex to be added to current instance.
     */
    public void add(Complex other) {
        add(other, this);
    }
    
    /**
     * Subtracts provided instance from this instance and stores the result
     * in provided instance.
     * @param other Complex to be subtracted from this instance.
     * @param result Complex instance where result is stored.
     */
    public void subtract(Complex other, Complex result) {
        result.real = real - other.real;
        result.imaginary = imaginary - other.imaginary;
    }
    
    /**
     * Subtracts provided instance from this instance and returns the result
     * as a new instance
     * @param other Complex to be subtracted from current instance.
     * @return Result of subtraction
     */
    public Complex subtractAndReturnNew(Complex other) {
        Complex result = new Complex();
        subtract(other, result);
        return result;
    }
    
    /**
     * Subtracts provided complex from this instance.
     * @param other Complex to be subtracted from current instance.
     */
    public void subtract(Complex other) {
        subtract(other, this);
    }  
    
    /**
     * Multiplies this instance with provided instance and stores the result
     * in provided instance.
     * @param other Complex to be multiplied to this instance
     * @param result Complex instance where result is stored.
     */
    public void multiply(Complex other, Complex result) {
        double tmpReal = (real * other.real) - (imaginary * other.imaginary);
        double tmpImaginary = (imaginary * other.real) + 
                (real * other.imaginary);
        
        result.real = tmpReal;
        result.imaginary = tmpImaginary;
    }
    
    /**
     * Multiplies provided instance with this instance and returns the result
     * as a new instance.
     * @param other Complex to be multiplied to current instance.
     * @return Result of multiplication.
     */
    public Complex multiplyAndReturnNew(Complex other) {
        Complex result = new Complex();
        multiply(other, result);
        return result;
    }
    
    /**
     * Multiplies provided complex with this instance.
     * @param other Complex to be multiplied to this instance.
     */
    public void multiply(Complex other) {
        multiply(other, this);
    }
    
    /**
     * Divides this instance by provided instance and stores the result in
     * provided instance.
     * @param other Complex to divide this instance by.
     * @param result Complex instance where result is stored.
     */
    public void divide(Complex other, Complex result) {
        double tmpReal = ((real * other.real) + (imaginary * other.imaginary)) /
                ((other.real * other.real) + 
                (other.imaginary * other.imaginary));
        double tmpImaginary = ((imaginary * other.real) - 
                (real * other.imaginary)) /
                ((other.real * other.real) + 
                (other.imaginary * other.imaginary));  
        
        result.real = tmpReal;
        result.imaginary = tmpImaginary;        
    }
    
    /**
     * Divides this instance by provided instance and returns the result as
     * a new instance.
     * @param other Complex to divide this instance by.
     * @return Result of division.
     */
    public Complex divideAndReturnNew(Complex other) {
        Complex result = new Complex();
        divide(other, result);
        return result;
    }
    
    /**
     * Divides this instance by provided complex.
     * @param other Complex to divide this instance by.
     */
    public void divide(Complex other) {
        divide(other, this);
    }
    
    /**
     * Multiplies this instance by provided scalar value (multiplying both
     * real and imaginary parts by provided value) and stores the result in
     * provided complex instance.
     * @param scalar Value to multiply this instance by.
     * @param result Complex where result of multiplication is stored.
     */
    public void multiplyByScalar(double scalar, Complex result) {
        result.real = scalar * real;
        result.imaginary = scalar * imaginary;
    }
    
    /**
     * Multiplies this instance by provided scalar value (multiplying both
     * real and imaginary parts by provided value) and returns the result
     * as a new instance.
     * @param scalar Value to multiply this instance by.
     * @return Result of multiplication by scalar.
     */
    public Complex multiplyByScalarAndReturnNew(double scalar) {
        Complex result = new Complex();
        multiplyByScalar(scalar, result);
        return result;
    }
    
    /**
     * Multiplies this instance by provided scalar
     * @param scalar Value to multiply this instance by.
     */
    public void multiplyByScalar(double scalar) {
        multiplyByScalar(scalar, this);
    }
    
    /**
     * Computes the power of this instance by provided exponent and stores
     * the result in provided instance.
     * @param exponent Exponent to power this instance by.
     * @param result Complex where the power is stored.
     */
    public void pow(double exponent, Complex result) {
        result.setModulusAndPhase(Math.pow(getModulus(), exponent), 
                getPhase() * exponent);
    }
    
    /**
     * Computes the power of this instance by provided exponent and returns
     * the result as a new instance.
     * @param exponent Exponent to power this instance by.
     * @return Complex where the power is stored.
     */
    public Complex powAndReturnNew(double exponent) {
        Complex result = new Complex();
        pow(exponent, result);
        return result;
    }
    
    /**
     * Computes the power of this instance by provided exponent
     * @param exponent Exponent to power this instance by.
     */
    public void pow(double exponent) {
        pow(exponent, this);
    }
    
    /**
     * Computes the squared root of this instance and returns the result as
     * a new instance.
     * @param result Complex where squared root is stored.
     */
    public void sqrt(Complex result) {
        pow(0.5, result);
    }
    
    /**
     * Computes the squared root of this instance and returns the result as a
     * new instance.
     * @return Squared root of this instance.
     */
    public Complex sqrtAndReturnNew() {
        Complex result = new Complex();
        sqrt(result);
        return result;
    }
    
    /**
     * Computes the squared root of this instance
     */
    public void sqrt() {
        sqrt(this);
    }
    
    /**
     * Determines whether two Complex objects are equal or not.
     * Two Complex are considered to be equal if both their real and imaginary
     * parts are equal.
     * @param obj Object to compare.
     * @return True if both Complex objects are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null) return false;
        if(!(obj instanceof Complex)) return false;
        
        Complex other = (Complex)obj;
        return equals(other, 0.0);
    }

    /**
     * Computes the hashcode of this instance.
     * @return Hashcode
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (int) (Double.doubleToLongBits(this.real) ^ (Double.doubleToLongBits(this.real) >>> 32));
        hash = 47 * hash + (int) (Double.doubleToLongBits(this.imaginary) ^ (Double.doubleToLongBits(this.imaginary) >>> 32));
        return hash;
    }
    
    /**
     * Determines whether two Complex objects are equal or not up to a certain
     * level of tolerance in both their real and imaginary parts. When the
     * difference in both their real and imaginary parts is below the tolerance
     * level, then both instances are considered to be equal
     * @param other Other Complex to compare
     * @param tolerance Margin of tolerance.
     * @return Returns true if both Complex instances are considered to be 
     * equal.
     */
    public boolean equals(Complex other, double tolerance) {
        if(Math.abs(real - other.real) > tolerance) {
            return false;
        }
        return Math.abs(imaginary - other.imaginary) <= tolerance;
    }
    
    /**
     * Makes a copy of this instance having the same real and imaginary parts
     * @return a copy of this instance
     */
    @Override
    public Complex clone() {
        return new Complex(real, imaginary);
    }
    
    /**
     * Copies provided value into current instance
     * @param value Value to copy from
     */
    public void copyFrom(Complex value) {
        this.real = value.real;
        this.imaginary = value.imaginary;
    }
}
