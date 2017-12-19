package com.ndpar.utils.crypto

abstract class Timer {

    static final ROUNDS = 5

    static long time(Closure f) {
        time ROUNDS, f
    }

    static long time(int rounds, Closure f) {
        def times = []
        rounds.times { times << timeSingle(f) }
        times.sum() / rounds
    }

    private static timeSingle(Closure f) {
        def start = System.currentTimeMillis()
        f.call()
        System.currentTimeMillis() - start
    }
}
