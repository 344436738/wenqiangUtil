package com.leimingtech.core.util.image;

/**
 * 来自网络上的缩放算法，可以获得很好的缩放画质
 * 
 */
import java.awt.image.BufferedImage;

public class ImageScale {
	private int width;

	private int scaleWidth;

	double support = (double) 3.0;

	double PI = (double) 3.14159265358978;

	double[] contrib;

	double[] normContrib;

	double[] tmpContrib;

	int startContrib, stopContrib;

	int nDots;

	int nHalfDots;

	public BufferedImage doScale(BufferedImage srcBufferImage, int w, int h) {
		width = srcBufferImage.getWidth();
		scaleWidth = w;

		// 以下三行注掉是因为有些bmp不缩放只转换格式则颜色会有问题
		// if (isNeedScale(w, h) == 1) {
		// return srcBufferImage;
		// }
		calContrib();
		BufferedImage pbOut = horizontalFiltering(srcBufferImage, w);
		BufferedImage pbFinalOut = verticalFiltering(pbOut, h);
		return pbFinalOut;
	}

	private double Lanczos(int i, int inWidth, int outWidth, double Support) {
		double x = (double) i * (double) outWidth / (double) inWidth;
		return Math.sin(x * PI) / (x * PI) * Math.sin(x * PI / Support) / (x * PI / Support);
	}

	/**
	 * 计算缩放参数
	 */
	private void calContrib() {
		nHalfDots = (int) ((double) width * support / (double) scaleWidth);
		nDots = nHalfDots * 2 + 1;
		try {
			contrib = new double[nDots];
			normContrib = new double[nDots];
			tmpContrib = new double[nDots];
		} catch (Exception e) {
			System.out.println("init contrib,normContrib,tmpContrib" + e);
		}

		int center = nHalfDots;
		contrib[center] = 1.0;
		double weight = 0.0;
		int i = 0;
		for (i = 1; i <= center; i++) {
			contrib[center + i] = Lanczos(i, width, scaleWidth, support);
			weight += contrib[center + i];
		}
		for (i = center - 1; i >= 0; i--) {
			contrib[i] = contrib[center * 2 - i];
		}
		weight = weight * 2 + 1.0;
		for (i = 0; i <= center; i++) {
			normContrib[i] = contrib[i] / weight;
		}
		for (i = center + 1; i < nDots; i++) {
			normContrib[i] = normContrib[center * 2 - i];
		}
	}

	/**
	 * 处理边缘
	 */
	private void calTempContrib(int start, int stop) {
		double weight = 0;
		int i = 0;
		for (i = start; i <= stop; i++) {
			weight += contrib[i];
		}
		for (i = start; i <= stop; i++) {
			tmpContrib[i] = contrib[i] / weight;
		}
	}

	private int getRedValue(int rgbValue) {
		int temp = rgbValue & 0x00ff0000;
		return temp >> 16;
	}

	private int getGreenValue(int rgbValue) {
		int temp = rgbValue & 0x0000ff00;
		return temp >> 8;
	}

	private int getBlueValue(int rgbValue) {
		return rgbValue & 0x000000ff;
	}

	private int comRGB(int redValue, int greenValue, int blueValue) {
		return (redValue << 16) + (greenValue << 8) + blueValue;
	}

	/**
	 * 行水平滤波
	 */
	private int horizontalFilter(BufferedImage bufImg, int startX, int stopX, int start, int stop, int y,
			double[] pContrib) {
		double valueRed = 0.0;
		double valueGreen = 0.0;
		double valueBlue = 0.0;
		int valueRGB = 0;
		int i, j;
		for (i = startX, j = start; i <= stopX; i++, j++) {
			valueRGB = bufImg.getRGB(i, y);

			valueRed += getRedValue(valueRGB) * pContrib[j];
			valueGreen += getGreenValue(valueRGB) * pContrib[j];
			valueBlue += getBlueValue(valueRGB) * pContrib[j];
		}
		valueRGB = comRGB(clip((int) valueRed), clip((int) valueGreen), clip((int) valueBlue));
		return valueRGB;
	}

	/**
	 * 图片水平滤波
	 */
	private BufferedImage horizontalFiltering(BufferedImage bufImage, int iOutW) {
		int dwInW = bufImage.getWidth();
		int dwInH = bufImage.getHeight();
		int value = 0;
		BufferedImage pbOut = new BufferedImage(iOutW, dwInH, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < iOutW; x++) {
			int startX;
			int start;
			int X = (int) (((double) x) * ((double) dwInW) / ((double) iOutW) + 0.5);
			int y = 0;
			startX = X - nHalfDots;
			if (startX < 0) {
				startX = 0;
				start = nHalfDots - X;
			} else {
				start = 0;
			}
			int stop;
			int stopX = X + nHalfDots;
			if (stopX > (dwInW - 1)) {
				stopX = dwInW - 1;
				stop = nHalfDots + (dwInW - 1 - X);
			} else {
				stop = nHalfDots * 2;
			}
			if (start > 0 || stop < nDots - 1) {
				calTempContrib(start, stop);
				for (y = 0; y < dwInH; y++) {
					value = horizontalFilter(bufImage, startX, stopX, start, stop, y, tmpContrib);
					pbOut.setRGB(x, y, value);
				}
			} else {
				for (y = 0; y < dwInH; y++) {
					value = horizontalFilter(bufImage, startX, stopX, start, stop, y, normContrib);
					pbOut.setRGB(x, y, value);
				}
			}
		}
		return pbOut;

	}

	private int verticalFilter(BufferedImage pbInImage, int startY, int stopY, int start, int stop, int x,
			double[] pContrib) {
		double valueRed = 0.0;
		double valueGreen = 0.0;
		double valueBlue = 0.0;
		int valueRGB = 0;
		int i, j;
		for (i = startY, j = start; i <= stopY; i++, j++) {
			valueRGB = pbInImage.getRGB(x, i);
			valueRed += getRedValue(valueRGB) * pContrib[j];
			valueGreen += getGreenValue(valueRGB) * pContrib[j];
			valueBlue += getBlueValue(valueRGB) * pContrib[j];
		}
		valueRGB = comRGB(clip((int) valueRed), clip((int) valueGreen), clip((int) valueBlue));
		return valueRGB;
	}

	private BufferedImage verticalFiltering(BufferedImage pbImage, int iOutH) {
		int iW = pbImage.getWidth();
		int iH = pbImage.getHeight();
		int value = 0;
		BufferedImage pbOut = new BufferedImage(iW, iOutH, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < iOutH; y++) {
			int startY;
			int start;
			int Y = (int) (((double) y) * ((double) iH) / ((double) iOutH) + 0.5);
			startY = Y - nHalfDots;
			if (startY < 0) {
				startY = 0;
				start = nHalfDots - Y;
			} else {
				start = 0;
			}
			int stop;
			int stopY = Y + nHalfDots;
			if (stopY > (int) (iH - 1)) {
				stopY = iH - 1;
				stop = nHalfDots + (iH - 1 - Y);
			} else {
				stop = nHalfDots * 2;
			}
			if (start > 0 || stop < nDots - 1) {
				calTempContrib(start, stop);
				for (int x = 0; x < iW; x++) {
					value = verticalFilter(pbImage, startY, stopY, start, stop, x, tmpContrib);
					pbOut.setRGB(x, y, value);
				}
			} else {
				for (int x = 0; x < iW; x++) {
					value = verticalFilter(pbImage, startY, stopY, start, stop, x, normContrib);
					pbOut.setRGB(x, y, value);
				}
			}
		}
		return pbOut;

	}

	private int clip(int x) {
		if (x < 0)
			return 0;
		if (x > 255)
			return 255;
		return x;
	}
}