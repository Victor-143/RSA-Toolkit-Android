package com.example.otp_rsa;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSAUtils {

    private BigInteger p, q, n, phi, e, d;
    private int bitLength = 512; // Key size

    public RSAUtils() {
        generateKeys();
    }

    private void generateKeys() {
        SecureRandom random = new SecureRandom();

        // Generate two large primes p and q
        p = BigInteger.probablePrime(bitLength, random);
        q = BigInteger.probablePrime(bitLength, random);

        n = p.multiply(q);

        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // Choose e such that 1 < e < phi and gcd(e, phi) = 1
        e = BigInteger.valueOf(65537); // common choice for e

        // Calculate d, the mod inverse of e
        d = e.modInverse(phi);
    }

    // Encrypt message (string number) -> ciphertext BigInteger
    public BigInteger encrypt(String message) {
        BigInteger msg = new BigInteger(message);
        return msg.modPow(e, n);
    }

    // Decrypt ciphertext BigInteger -> message string
    public String decrypt(BigInteger cipher) {
        return cipher.modPow(d, n).toString();
    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getD() {
        return d;
    }

    public BigInteger getN() {
        return n;
    }
}
