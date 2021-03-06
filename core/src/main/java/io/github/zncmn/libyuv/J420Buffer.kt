package io.github.zncmn.libyuv

import java.nio.ByteBuffer

/**
 * J420 (jpeg) YUV Format. 4:2:0 12bpp
 */
class J420Buffer private constructor(
    private val buffer: ByteBuffer,

    val bufferY: ByteBuffer,
    val bufferU: ByteBuffer,
    val bufferV: ByteBuffer,

    internal val strideY: Int,
    internal val strideU: Int,
    internal val strideV: Int,

    override val width: Int,
    override val height: Int
) : Buffer {
    override fun asByteArray() = buffer.asByteArray()
    override fun asByteArray(dst: ByteArray) = buffer.asByteArray(dst)

    companion object {
        @JvmStatic
        fun getStrideWithCapacity(width: Int, height: Int): IntArray {
            val halfWidth = (width + 1).shr(1)
            val capacity = width * height
            val halfCapacity = (halfWidth + 1).shr(1) * height
            return intArrayOf(width, capacity, halfWidth, halfCapacity, halfWidth, halfCapacity)
        }

        @JvmStatic
        fun allocate(width: Int, height: Int): J420Buffer {
            val (strideY, capacityY, strideU, capacityU, strideV, capacityV) = getStrideWithCapacity(width, height)
            val buffer = createByteBuffer(capacityY + capacityU + capacityV)
            val (bufferY, bufferU, bufferV) = buffer.slice(capacityY, capacityU, capacityV)
            return J420Buffer(buffer, bufferY, bufferU, bufferV, strideY, strideU, strideV, width, height)
        }

        @JvmStatic
        fun wrap(buffer: ByteBuffer, width: Int, height: Int): J420Buffer {
            val (strideY, capacityY, strideU, capacityU, strideV, capacityV) = getStrideWithCapacity(width, height)
            val (bufferY, bufferU, bufferV) = buffer.slice(capacityY, capacityU, capacityV)
            return J420Buffer(buffer.duplicate(), bufferY, bufferU, bufferV, strideY, strideU, strideV, width, height)
        }
    }
}