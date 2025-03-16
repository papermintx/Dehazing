package com.mk.core.algorithm

import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

object DCP {

    fun removeHaze(inputImage: Mat): Mat {
        val normalizedImage = Mat()
        inputImage.convertTo(normalizedImage, CvType.CV_32F, 1.0 / 255.0)
        val hazeMap = calculateDarkChannel(normalizedImage)
        val atmosphericLight = estimateAtmosphericLight(normalizedImage, hazeMap)
        val transmissionMap = estimateTransmissionMap(normalizedImage, atmosphericLight)
        val refinedTransmissionMap = guidedFilter(normalizedImage, transmissionMap, 15, 1e-3)
        val resultImage = recoverScene(normalizedImage, refinedTransmissionMap, atmosphericLight)
        val outputImage = Mat()
        resultImage.convertTo(outputImage, CvType.CV_8U, 255.0)
        return outputImage
    }

    private fun calculateDarkChannel(image: Mat): Mat {
        val minChannel = Mat()
        val channels = ArrayList<Mat>()
        Core.split(image, channels)

        Core.min(channels[0], channels[1], minChannel)
        Core.min(minChannel, channels[2], minChannel)

        val darkChannel = Mat()
        Imgproc.erode(minChannel, darkChannel, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(15.0, 15.0)))

        return darkChannel
    }

    private fun estimateAtmosphericLight(image: Mat, darkChannel: Mat): DoubleArray {
        val flatDark = darkChannel.reshape(1, 1)
        val flatImage = image.reshape(1, image.rows() * image.cols())

        val indices = Mat()
        Core.sortIdx(flatDark, indices, Core.SORT_DESCENDING or Core.SORT_EVERY_COLUMN)

        val numPixels = (image.rows() * image.cols() * 0.001).toInt()
        val atmosphericLight = DoubleArray(3)

        for (i in 0 until 3) {
            var sum = 0.0
            for (j in 0 until numPixels) {
                val idx = indices.get(0, j)[0].toInt()
                sum += flatImage.get(idx, i)[0]
            }
            atmosphericLight[i] = sum / numPixels
        }
        return atmosphericLight
    }

    private fun estimateTransmissionMap(image: Mat, atmosphericLight: DoubleArray): Mat {
        val normalizedImage = Mat(image.size(), CvType.CV_32FC3)
        val t = Mat(image.size(), CvType.CV_32F)

        for (i in 0 until 3) {
            val channel = Mat()
            image.convertTo(channel, CvType.CV_32F)
            Core.extractChannel(channel, channel, i)
            Core.divide(channel, Scalar(atmosphericLight[i]), channel)
            Core.insertChannel(channel, normalizedImage, i)
        }
        val darkChannel = calculateDarkChannel(normalizedImage)
        Core.subtract(Mat.ones(darkChannel.size(), CvType.CV_32F), darkChannel, t)
        return t
    }

    private fun guidedFilter(image: Mat, transmission: Mat, radius: Int, epsilon: Double): Mat {
        val gray = Mat()
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY)
        val floatGray = Mat()
        gray.convertTo(floatGray, CvType.CV_32F, 1.0 / 255.0)
        val blurredGray = Mat()
        Imgproc.GaussianBlur(floatGray, blurredGray, Size(radius.toDouble(), radius.toDouble()), epsilon)
        val meanTransmission = Mat()
        Imgproc.GaussianBlur(transmission, meanTransmission, Size(radius.toDouble(), radius.toDouble()), epsilon)
        val guidedTransmission = Mat()
        Core.addWeighted(transmission, 1.5, meanTransmission, -0.5, 0.0, guidedTransmission)
        return guidedTransmission
    }

    private fun recoverScene(image: Mat, transmission: Mat, atmosphericLight: DoubleArray): Mat {
        val resultImage = Mat(image.size(), CvType.CV_32FC3)
        val t0 = 0.1

        for (i in 0 until 3) {
            val channel = Mat()
            Core.extractChannel(image, channel, i)
            Core.subtract(channel, Scalar(atmosphericLight[i]), channel)
            val refinedTransmission = Mat()
            Core.max(transmission, Scalar(t0), refinedTransmission)
            Core.divide(channel, refinedTransmission, channel)
            Core.add(channel, Scalar(atmosphericLight[i]), channel)
            Core.insertChannel(channel, resultImage, i)
        }

        return resultImage
    }
}
