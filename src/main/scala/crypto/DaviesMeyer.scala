package crypto

import java.security.MessageDigest
import crypto.Block.requiredLength
import crypto.BlockMessage.Xor

object DaviesMeyer {

  def customCompressionFunction(block1: Block, block2: Block): Block= {
      val md = MessageDigest.getInstance("MD5")
      val md1 = MessageDigest.getInstance("MD5")
      md.update(block2.bytes)
      val msg_digest = md.digest()
      md1.update(block1.bytes)
      val iv_digest = md1.digest()
      val digest_block = Block.create(msg_digest)
      val iv_digest_block = Block.create(iv_digest)
      val digest_block_xor_iv_digest_block = digest_block.xor(iv_digest_block)
      val final_digest = digest_block_xor_iv_digest_block.xor(iv_digest_block)
      final_digest
  }

  def lengthExtension(result: Block): Unit = {

    val random_message: String = "AAAAAABBBABDrld!"
    val block2 = random_message.getBytes
    val messageLength2 = block2.length
    val block_message2 = BlockMessage(block2)
    println("block_message2 "+block_message2)
    val block_hash2 = BlockHash(customCompressionFunction, result)
    val result2 = block_hash2.hash(block_message2)
    println("extra result: "+result2.toString)


    val new_message: String = ";ext"
    val block3 = new_message.getBytes
    val block_message3 = BlockMessage(block3)
    val block_hash3 = BlockHash(customCompressionFunction, result)
    val result3 = block_hash2.hash(block_message3)
    println("new result: "+result3.toString)
  }

  def main(args:Array[String]): Unit ={
    val iv: Block = Block.create(LazyList.continually(0xCC) map (_.toByte) take 16)
    val message: String = "Hello World!"
    val block = message.getBytes
    val messageLength = block.length
    val block_message = BlockMessage(block)
    val block_hash = BlockHash(customCompressionFunction, iv)
    val result = block_hash.hash(block_message)
    println("result: "+result.toString)

    lengthExtension(result)

  }
}

