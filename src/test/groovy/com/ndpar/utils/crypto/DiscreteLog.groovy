package com.ndpar.utils.crypto

@SuppressWarnings('all')
class DiscreteLog {

    BigInteger B = 2.power(20)

    def meetInTheMiddle(BigInteger p, BigInteger g, BigInteger h) {
        def hash = [:]
        (0..B).each {
            x1 -> hash[(h * g.modPow(x1, p).modInverse(p)).mod(p)] = x1
        }
        (0..B).each { x0 ->
            def x1 = hash[g.modPow(B, p).modPow(x0, p)]
            if (x1) println(x0 * B + x1)
        }
    }

    static final void main(String[] args) {
        def dl = new DiscreteLog()
        dl.meetInTheMiddle(
                13407807929942597099574024998205846127479365820592393377723561443721764030073546976801874298166903427690031858186486050853753882811946569946433649006084171,
                11717829880366207009516117596335367088558084999998952205599979459063929499736583746670572176471460312928594829675428279466566527115212748467589894601965568,
                3239475104050450443565264378728065788649097520952449527834792452971981976143292558073856937958553180532878928001494706097394108577585732452307673444020333
        )
    }
}
