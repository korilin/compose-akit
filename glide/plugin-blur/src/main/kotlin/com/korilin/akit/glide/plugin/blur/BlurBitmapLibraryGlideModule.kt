package com.korilin.akit.glide.plugin.blur

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.resource.bitmap.ByteBufferBitmapDecoder
import com.bumptech.glide.load.resource.bitmap.Downsampler
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder
import com.bumptech.glide.module.LibraryGlideModule
import java.io.InputStream
import java.nio.ByteBuffer

@GlideModule
class BlurBitmapLibraryGlideModule : LibraryGlideModule() {

    override fun registerComponents(
        context: Context, glide: Glide, registry: Registry
    ) {

        val bitmapPool = glide.bitmapPool
        val arrayPool = glide.arrayPool
        val downsampler = Downsampler(
            registry.imageHeaderParsers,
            context.resources.displayMetrics,
            bitmapPool, arrayPool
        )
        val byteBufferBitmapDecoder: ResourceDecoder<ByteBuffer, Bitmap> =
            ByteBufferBitmapDecoder(downsampler)

        val streamBitmapDecoder: ResourceDecoder<InputStream, Bitmap> =
            StreamBitmapDecoder(downsampler, arrayPool)

        // InputStream
        // ByteBuffer
        registry.prepend(
            Registry.BUCKET_BITMAP,
            ByteBuffer::class.java,
            Bitmap::class.java,
            byteBufferBitmapDecoder
        )
        registry.prepend(
            Registry.BUCKET_BITMAP,
            InputStream::class.java,
            Bitmap::class.java,
            streamBitmapDecoder
        )
    }

}