package kkdt.sample.sso.tools;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;

public class SRPMath {
    public static void main(String[] args) {
        final int bitsize = 2048;
        Random random = new SecureRandom();
        
        System.out.println("A = g^a mod(N)");
        System.out.println("B = g^b mod(N)");
        System.out.println();
        
        try(Scanner in = new Scanner(System.in)) {
            System.out.print("g = ");
            BigInteger g = new BigInteger(in.nextLine());
            
            BigInteger N = new BigInteger(bitsize, random);
            System.out.print("N = " + N);
            
            System.out.println();
            
            System.out.print("a = ");
            BigInteger a = new BigInteger(in.nextLine());
            BigInteger A = g.modPow(a, N);
            
            System.out.print("b = ");
            BigInteger b = new BigInteger(in.nextLine());
            BigInteger B = g.modPow(b, N);
            
            System.out.println();
            
            System.out.println("A = " + A);
            System.out.println("B = " + B);
            
            System.out.println();
            
            System.out.println("S.A = B^a = " + B.modPow(a, N));
            System.out.println("S.B = A^b = " + A.modPow(b, N));
        }
    }
}
