package crypto

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.math.BigInteger
import scala.util.Success

class DiffieHellmanMerkleSpecPartB extends AnyFlatSpec with should.Matchers {

  behavior of "DiffieHellmanMerkle"

  private val prime: Prime = Prime(31)
  private val g: BigInt = 11 // a primitive root of 31 (the others are 3, 12, 13, 17, 21, 22, 24)
  private val aliceKey: BigInt = 4
  private val bobKey: BigInt = 3
  private val secret: BigInt = 16
  private val plainText: BigInt = 17
  private val cipherText: BigInt = 14


  it should "construct" in {
    val target = DiffieHellmanMerkle(prime, g)
    target.modulus shouldBe prime
    target.generator shouldBe g
    target.toString shouldBe s"DiffieHellmanMerkle($prime,$g)"
  }

  it should "not construct" in {
    a[java.lang.IllegalArgumentException] should be thrownBy DiffieHellmanMerkle(Prime(4), 0)
    a[java.lang.IllegalArgumentException] should be thrownBy DiffieHellmanMerkle(prime, 4)
  }

  it should "keyExchange" in {
    val target = DiffieHellmanMerkle(prime, g)
    target.keyExchange(aliceKey) shouldBe 9
    target.keyExchange(bobKey) shouldBe 29
  }

  it should "secret" in {
    val target = DiffieHellmanMerkle(prime, g)
    target.secret(aliceKey, bobKey) shouldBe Success(secret)
  }

  it should "get the multiplicative inverse" in {
    val target = DiffieHellmanMerkle(prime, g)
    val sy = target.secret(aliceKey, bobKey)
    sy shouldBe Success(secret)
    val multiplicativeInverse = target.multiplicativeInverse(sy.get)
    multiplicativeInverse shouldBe 2
    val product: BigInt = sy.get * multiplicativeInverse
    product.mod(prime.toBigInt) shouldBe 1
    product shouldBe 32
//    product shouldBe (prime.toBigInt * 7 + 1)
  }

  it should "modPow" in {
    val z = prime.modPow(plainText, secret)
    z shouldBe plainText.pow(secret.toInt).mod(prime.toBigInt)
    z shouldBe cipherText
  }
}
