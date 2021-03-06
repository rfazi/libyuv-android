package io.github.zncmn.libyuv

import java.nio.ByteBuffer

/**
 * ABGR little endian (rgba in memory)
 */
class AbgrBuffer private constructor(
    val bufferABGR: ByteBuffer,

    internal val strideABGR: Int,

    override val width: Int,
    override val height: Int
) : Buffer {
    override fun asByteArray() = bufferABGR.asByteArray()
    override fun asByteArray(dst: ByteArray) = bufferABGR.asByteArray(dst)

    companion object {
        @JvmStatic
        fun getStrideWithCapacity(width: Int, height: Int): IntArray {
            val stride = width.shl(2)
            val capacity = stride * height
            return intArrayOf(stride, capacity)
        }

        @JvmStatic
        fun allocate(width: Int, height: Int): AbgrBuffer {
            val (stride, capacity) = getStrideWithCapacity(width, height)
            val buffer = createByteBuffer(capacity)
            return AbgrBuffer(buffer, stride, width, height)
        }

        @JvmStatic
        fun wrap(buffer: ByteBuffer, width: Int, height: Int): AbgrBuffer {
            val (stride, capacity) = getStrideWithCapacity(width, height)
            return AbgrBuffer(buffer.sliceRange(0, capacity), stride, width, height)
        }
    }
}