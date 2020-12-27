package day25;

import java.math.BigInteger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    private void execute() {
        //int device = 17807724;
        //int door = 5764801;
        int device = 8421034;
        int door = 15993936;
        int deviceLoop = calc(device);
        int doorLoop = calc(door);
        log.info("{}", step(BigInteger.valueOf(device), BigInteger.valueOf(doorLoop)));
        log.info("{}", step(BigInteger.valueOf(door), BigInteger.valueOf(deviceLoop)));
    }

    private int calc(int publicKey) {
        BigInteger value = BigInteger.ZERO;
        for (int i = 1; i <= 100000000; i++) {
            if (step(BigInteger.valueOf(7), BigInteger.valueOf(i)).equals(BigInteger.valueOf(publicKey))) {
                return i;
            }
        }
        return -1;
    }

    private BigInteger step(BigInteger subjectNumber, BigInteger num) {
        return subjectNumber.modPow(num, BigInteger.valueOf(20201227));
    }
}
