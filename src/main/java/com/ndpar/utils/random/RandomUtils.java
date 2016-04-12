/*
 * Copyright (c) 2016, Kevin Lawler <hello@kevinlawler.com>
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION
 * OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN
 * CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.ndpar.utils.random;

import static java.lang.Math.*;

/**
 * Java port of Kevin Lawler's implementation of Jeffrey Scott Vitter's algorithm.
 * <p/>
 * For details please see:
 * <ul>
 * <li>https://getkerf.wordpress.com/2016/03/30/the-best-algorithm-no-one-knows-about/</li>
 * <li>http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.16.4335&rep=rep1&type=pdf</li>
 * </ul>
 */
public class RandomUtils {

    /**
     * Vitter, J.S. - An Efficient Algorithm for Sequential Random Sampling -
     * ACM Trans. Math. Software 11 (1985), 37-57.
     *
     * @param n is the size of the hand
     * @param N is the upper bound on the random card values
     * @return a sorted array of n cards randomly drawn from N-card deck
     */
    public static long[] draw(int n, long N) {
        long[] a = new long[n];
        vitterD(a, n, N);
        return a;
    }

    private static void vitterD(long[] a, long n, long N) {
        int i = 0;
        long j = -1;
        long t;
        long qu1 = -n + 1 + N;
        long S;
        long negalphainv = -13;
        long threshold = -negalphainv * n;

        double nreal = n;
        double Nreal = N;
        double ninv = 1.0 / n;
        double nmin1inv;
        double Vprime = exp(log(random()) * ninv);

        double qu1real = -nreal + 1.0 + Nreal;
        double negSreal;
        double U;
        double X;
        double y1;
        double y2;
        double top;
        double bottom;
        double limit;

        while (n > 1 && threshold < N) {
            nmin1inv = 1.0 / (-1.0 + nreal);

            while (true) {
                while (true) {
                    X = Nreal * (-Vprime + 1.0);
                    S = (long) floor(X);

                    if (S < qu1) {
                        break;
                    }

                    Vprime = exp(log(random()) * ninv);
                }

                U = random();

                negSreal = -S;

                y1 = exp(log(U * Nreal / qu1real) * nmin1inv);

                Vprime = y1 * (-X / Nreal + 1.0) * (qu1real / (negSreal + qu1real));

                if (Vprime <= 1.0) {
                    break;
                }

                y2 = 1.0;

                top = -1.0 + Nreal;

                if (-1 + n > S) {
                    bottom = -nreal + Nreal;
                    limit = -S + N;
                } else {
                    bottom = -1.0 + negSreal + Nreal;
                    limit = qu1;
                }

                for (t = N - 1; t >= limit; t--) {
                    y2 = (y2 * top) / bottom;
                    top--;
                    bottom--;
                }

                if (Nreal / (-X + Nreal) >= y1 * exp(log(y2) * nmin1inv)) {
                    Vprime = exp(log(random()) * nmin1inv);
                    break;
                }

                Vprime = exp(log(random()) * ninv);
            }

            j += S + 1;

            a[i++] = j;

            N = -S + (-1 + N);

            Nreal = negSreal + (-1.0 + Nreal);

            n--;
            nreal--;
            ninv = nmin1inv;

            qu1 = -S + qu1;
            qu1real = negSreal + qu1real;

            threshold += negalphainv;
        }

        if (n > 1) {
            vitterA(a, i, n, N, j); // if i>0 then n has been decremented
        } else {
            S = (long) floor(N * Vprime);

            j += S + 1;

            a[i] = j;
        }
    }

    private static void vitterA(long[] a, int ix, long n, long N, long j) {
        long S;
        int i = 0;
        double top = N - n, Nreal = N, V, quot;

        while (n >= 2) {
            V = random();
            S = 0;
            quot = top / Nreal;

            while (quot > V) {
                S++;
                top--;
                Nreal--;
                quot = (quot * top) / Nreal;
            }
            j += S + 1;
            a[ix + i++] = j;
            Nreal--;
            n--;
        }

        S = (int) floor(round(Nreal) * random());
        j += S + 1;
        a[ix + i] = j;
    }
}
