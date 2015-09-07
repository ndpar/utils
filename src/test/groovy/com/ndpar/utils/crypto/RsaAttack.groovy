package com.ndpar.utils.crypto

import static javax.xml.bind.DatatypeConverter.*

import static com.ndpar.math.Math.*

class RsaAttack {

    static void main(String[] args) {
        factorN1()
        breakRsa()
        factorN3()

        //factorN2() // slow
    }

    /**
     * N is a products of two primes p and q where |p − q| < 2N^1/4.
     * Attack: √N is close to (p + q) / 2.
     */
    private static factorN1() {
        def N = 179769313486231590772930519078902473361797697894230657273430081157732675805505620686985379449212982959585501387537164015710139858647833778606925583497541085196591615128057575940752635007475935288710823649949940771895617054361149474865046711015101563940680527540071584560878577663743040086340742855278549092581
        def A = sqrt(N) + 1
        def x = sqrt(A * A - N)
        def p = A - x
        def q = A + x

        assert p == 13407807929942597099574024998205846127479365820592393377723561443721764030073662768891111614362326998675040546094339320838419523375986027530441562135724301
        assert q == 13407807929942597099574024998205846127479365820592393377723561443721764030073778560980348930557750569660049234002192590823085163940025485114449475265364281
        assert p * q == N
    }

    /**
     * Using factoring from the previous method.
     */
    private static breakRsa() {
        def p = 13407807929942597099574024998205846127479365820592393377723561443721764030073662768891111614362326998675040546094339320838419523375986027530441562135724301
        def q = 13407807929942597099574024998205846127479365820592393377723561443721764030073778560980348930557750569660049234002192590823085163940025485114449475265364281
        def N = p * q
        BigInteger e = 65537
        def phi = (p - 1) * (q - 1)
        def d = e.modInverse(phi)

        def c = 22096451867410381776306561134883418017410069787892831071731839143676135600120538004282329650473509424343946219751512256465839967942889460764542040581564748988013734864120452325229320176487916666402997509188729971690526083222067771600019329260870009579993724077458967773697817571267229951148662959627934791540
        def t = c.modPow(d, N)
        assert t.modPow(e, N) == c

        def hex = t.toString(16)
        def idx = hex.lastIndexOf('00') + 2
        def text = parseHexBinary(hex[idx..-1])
        assert 'Factoring lets us break RSA.' == new String(text, 'UTF-8')
    }

    /**
     * N is a products of two primes p and q where |p − q| < 2^11 N^1/4.
     * Attack: A − √N < 2^20 so try scanning for A from √N upwards.
     */
    private static factorN2() {
        def N = 648455842808071669662824265346772278726343720706976263060439070378797308618081116462714015276061417569195587321840254520655424906719892428844841839353281972988531310511738648965962582821502504990264452100885281673303711142296421027840289307657458645233683357077834689715838646088239640236866252211790085787877
        def A = sqrt(N) + 1
        def a = A
        def x = sqrt(A * A - N)
        def p = A - x
        def q = A + x
        while (p * q != N) {
            x = sqrt(A * A - N)
            p = A - x
            q = A++ + x
        }
        println(A - a)
        assert p == 25464796146996183438008816563973942229341454268524157846328581927885777969985222835143851073249573454107384461557193173304497244814071505790566593206419759
        assert q == 25464796146996183438008816563973942229341454268524157846328581927885777970106398054491246526970814167632563509541784734741871379856682354747718346471375403
    }

    /**
     * N is a products of two primes p and q where |3p − 2q| < N^1/4.
     * Attack: √6N is close to (3p + 2q) / 2.
     */
    private static factorN3() {
        def N = 720062263747350425279564435525583738338084451473999841826653057981916355690188337790423408664187663938485175264994017897083524079135686877441155132015188279331812309091996246361896836573643119174094961348524639707885238799396839230364676670221627018353299443241192173812729276147530748597302192751375739387929
        def a = sqrt(6 * N)
        def A = a + 0.5 // 3p + 2q is odd
        def x = sqrt((A * A - 6 * N) as BigInteger)
        def p = (a - x) / 3
        def q = (a + x + 1) / 2

        assert p == 21909849592475533092273988531583955898982176093344929030099423584127212078126150044721102570957812665127475051465088833555993294644190955293613411658629209
        assert q == 32864774388713299638410982797375933848473264140017393545149135376190818117189240035825816494954711821626076210364113848440012285863311027426121370050758081
        assert p * q == N
    }
}
