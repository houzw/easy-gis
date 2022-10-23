package org.egc.gis.gdal.raster;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.gdal.IOFactory;
import org.gdal.gdal.*;
import org.gdal.gdalconst.gdalconstConstants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

/**
 * description:
 * look at https://github.com/qikunlun/rsdatastore/blob/master/src/main/java/ai/geodata/GDAL2Thumbnail.java
 *
 * @author houzhiwei
 * @date 2022/10/23/0023 15:33
 */
@Slf4j
public class GDALThumbnail {

    /**
     * 根据影像和目标缩略图大小，计算要缩放的尺度
     *
     * @param inputFileName 输入影像路径
     * @param size          生成影像的长和宽的最大值
     * @return scale
     */
    public double getScale(String inputFileName, int size) {
        gdal.AllRegister();
        Dataset inDataset = gdal.Open(inputFileName, gdalconstConstants.GA_ReadOnly);
        if (inDataset == null) {
            log.error("GDALOpen failed - " + gdal.GetLastErrorNo());
            log.error(gdal.GetLastErrorMsg());
            return -1.0;
        }
        gdal.GDALDestroyDriverManager();
        double nCols = inDataset.getRasterXSize();
        double nRows = inDataset.getRasterYSize();

        return Math.min(size / nCols, size / nRows);
    }

    private String getImageType(String inputFileName) {
        inputFileName = inputFileName.toLowerCase();
        if (inputFileName.endsWith(".bmp")) {
            return "BMP";
        } else if (inputFileName.endsWith(".jpg")) {
            return "JPEG";
        } else if (inputFileName.endsWith(".png")) {
            return "PNG";
        } else if (inputFileName.endsWith(".gif")) {
            return "GIF";
        }
        return null;
    }

    /**
     * 生成缩略图
     *
     * @param inputFileName  输入影像路径
     * @param outputFileName 输出缩略图路径
     * @param band           选择单个波段
     * @param scale          选择缩放的尺度 (0~1)
     * @return 是否执行成功
     */
    public boolean createThumbnail(String inputFileName, String outputFileName, int band, double scale) {
        int bands[] = {band};
        return createThumbnail(inputFileName, outputFileName, bands, scale);
    }

    /**
     * 生成缩略图
     *
     * @param inputFileName  输入影像路径
     * @param outputFileName 输出缩略图路径
     * @param redBand        选择红色波段
     * @param greenBand      选择蓝色波段
     * @param blueBand       选择绿色波段
     * @param scale          选择缩放的尺度
     * @return 是否执行成功
     */
    public boolean createThumbnail(String inputFileName, String outputFileName, int redBand, int greenBand, int blueBand, double scale) {
        int bands[] = {redBand, greenBand, blueBand};
        return createThumbnail(inputFileName, outputFileName, bands, scale);
    }

    private boolean createThumbnail(String inputFileName, String outputFileName, int[] bands, double scale) {
        if (bands.length != 1 && bands.length != 3) {
            log.error("Bands must have 1 or 3 elements.");
            return false;
        }
        String gType = getImageType(outputFileName);
        if (gType == null) {
            log.error("Only support format of bmp, jpg, png or gif.");
            return false;
        }
        gdal.AllRegister();
        Dataset inDataset = gdal.Open(inputFileName, gdalconstConstants.GA_ReadOnly);
        if (inDataset == null) {
            log.error("GDALOpen failed - " + gdal.GetLastErrorNo());
            log.error(gdal.GetLastErrorMsg());
            return false;
        }

        int nBand = inDataset.getRasterCount();
        if (nBand < bands.length) {
            log.error("The Number of bands smaller than length of bands");
            return false;
        }
        int nCols = inDataset.getRasterXSize();
        int nRows = inDataset.getRasterYSize();

        float minVal[] = new float[3];
        float maxVal[] = new float[3];
        float buffer[] = new float[nCols];
        for (int k = 0; k < bands.length; k++) {
            Band band = inDataset.GetRasterBand(bands[k]);
            Double nodata[] = new Double[1];
            for (int i = 0; i < nRows; i++) {
                if (band.ReadRaster(0, i, nCols, 1, buffer) != gdalconstConstants.CE_None) {
                    log.error("影像数据的读取失败.");
                    return false;
                }
                for (int j = 0; j < nCols; j++) {
                    if ((i == 0) && (j == 0)) {
                        maxVal[k] = minVal[k] = buffer[0];
                    }
                    if (buffer[j] < minVal[k]) {
                        minVal[k] = buffer[j];
                    }
                    if (buffer[j] > maxVal[k]) {
                        maxVal[k] = buffer[j];
                    }
                }
            }
        }

        Driver driver = gdal.GetDriverByName("BMP");
        if (driver == null) {
            log.error("Fail to create bmp image driver");
            return false;
        }

        int stepSize = (int) (1.0 / scale);
        int dstCols = nCols / stepSize + ((nCols % stepSize) == 0 ? 0 : 1);
        int dstRows = nRows / stepSize + ((nRows % stepSize) == 0 ? 0 : 1);

        String bmpFileName = outputFileName;
        if (!gType.equals("BMP")) {
            UUID uuid = UUID.randomUUID();
            String tempPath = System.getProperty("java.io.tmpdir");
            bmpFileName = tempPath + File.separator + uuid.toString() + ".bmp";
        }

        Dataset bmpDataset = driver.Create(bmpFileName, dstCols, dstRows, bands.length, gdalconstConstants.GDT_Byte);
        if (bmpDataset == null) {
            log.error("Fail to create bmp image driver");
            return false;
        }
        byte dstBuffer[] = new byte[dstCols];//输出缓存

        for (int k = 0; k < bands.length; k++) {
            Band bandWrite = bmpDataset.GetRasterBand(k + 1);
            Band bandRead = inDataset.GetRasterBand(bands[k]);

            int offset, offsetY = 0;
            for (int i = 0; i < nRows; i += stepSize) {
                if (bandRead.ReadRaster(0, i, nCols, 1, buffer) != gdalconstConstants.CE_None) {
                    log.error("读取影像数据失败.");
                    return false;
                }
                offset = 0;
                for (int j = 0; j < nCols; j += stepSize) {
                    dstBuffer[offset] = (byte) ((buffer[j] - minVal[k]) * 256 / (maxVal[k] - minVal[k] + 1));
                    offset++;
                }
                bandWrite.WriteRaster(0, offsetY, dstCols, 1, dstBuffer);
                offsetY++;
            }
        }
        bmpDataset.delete();
        driver.delete();

        if (gType.equals("PNG")) {
            try {
                BufferedImage source = ImageIO.read(new File(bmpFileName));
                int color = getNoDataRGB(source);
                Image image = makeColorTransparent(source, new Color(color));
                BufferedImage transparent = imageToBufferedImage(image);
                ImageIO.write(transparent, "PNG", new File(outputFileName));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                return false;
            }
        } else if (!gType.equals("BMP")) {
            Driver outDriver = gdal.GetDriverByName(gType);
            if (outDriver == null) {
                log.error("Fail to create " + gType + " image driver");
                return false;
            }
            Dataset outDataset = outDriver.CreateCopy(outputFileName, bmpDataset, 0);
            outDataset.delete();
        }
        inDataset.delete();
        gdal.GDALDestroyDriverManager();
        return true;
    }

    private static BufferedImage imageToBufferedImage(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }

    public static Image makeColorTransparent(BufferedImage im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {
            // the color we are looking for... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFF000000;

            @Override
            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    return rgb;
                }
            }
        };
        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }


    private int getNoDataRGB(BufferedImage source) {
        int[][] boundVals = new int[4][];
        boundVals[0] = source.getRGB(0, 0, 1, source.getHeight(), null, 0, source.getWidth());
        boundVals[1] = source.getRGB(0, 0, source.getWidth(), 1, null, 0, source.getWidth());
        boundVals[2] = source.getRGB(source.getWidth() - 1, 0, 1, source.getHeight(), null, 0, source.getWidth());
        boundVals[3] = source.getRGB(0, source.getHeight() - 1, source.getWidth(), 1, null, 0, source.getWidth());

        Map<Integer, Integer> valueCount = countValuesMap(boundVals);
        int rgbMax = 0, countMax = 0;
        for (Integer val : valueCount.keySet()) {
            int curVal = valueCount.get(val);
            if (curVal > countMax) {
                rgbMax = val;
                countMax = curVal;
            }
        }
        return rgbMax;
    }

    private Map countValuesMap(int[][] array) {
        Map<Object, Integer> map = new HashMap<Object, Integer>();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                int val = array[i][j];
                Integer integer = map.get(val);
                map.put(val, integer == null ? 1 : integer + 1);
            }
        }
        return map;
    }

    //----------------------------------------------------------------------------------------------------------

    /**
     * generate PNG format true-color thumbnail
     *
     * @param src       source file
     * @param dst       destination/output file (PNG)
     * @param redBand   red band
     * @param greenBand greed band
     * @param blueBand  blue band
     * @param scale     in percentage(%), e.g., 30
     */
    public void truecolorThumbnail(String src, String dst, int redBand, int greenBand, int blueBand, float scale) {
        Dataset ds = IOFactory.createRasterIO().read(src);
        int[] rgbBands = new int[]{redBand, greenBand, blueBand};

        Vector<String> options = new Vector<>();
        options.add("-of");
        options.add("PNG");

        int bandCount = ds.GetRasterCount();

        for (int i = 0; i < 3; i++) {
            if (rgbBands[i] > bandCount) {
                RasterIO.closeDataSet(ds);
                log.error("The selected band number cannot be larger than {}", bandCount);
                return; // no target band exists
            }
            if (rgbBands[i] <= 0) {
                RasterIO.closeDataSet(ds);
                log.error("The selected band number cannot be less than 0");
                return;
            }
        }

        if (bandCount < 3) {
            log.warn("The input image only has {} band(s)", bandCount);
            for (int i = 0; i < bandCount; i++) {
                options.add("-b");
                options.add(String.valueOf(i));
            }
        } else {
            options.add("-b");
            options.add(String.valueOf(redBand));
            options.add("-b");
            options.add(String.valueOf(greenBand));
            options.add("-b");
            options.add(String.valueOf(blueBand));
        }
        options.add("-outsize");
        options.add(String.valueOf(scale) + "%");
        options.add(String.valueOf(scale) + "%");
        TranslateOptions translateOptions = new TranslateOptions(options);
        gdal.Translate(dst, ds, translateOptions);
        RasterIO.closeDataSet(ds);
    }

    /**
     * generate PNG format true-color thumbnail
     *
     * @param src   source file
     * @param dst   destination/output file
     * @param scale in percentage(%)
     */
    public void rasterThumbnail(String src, String dst, float scale) {
        Dataset ds = IOFactory.createRasterIO().read(src);
        Vector<String> options = new Vector<>();
        options.add("-of");
        options.add("PNG");
        options.add("-b");
        options.add(String.valueOf(1));
        options.add("-outsize");
        options.add(String.valueOf(scale) + "%");
        options.add(String.valueOf(scale) + "%");
        TranslateOptions translateOptions = new TranslateOptions(options);
        gdal.Translate(dst, ds, translateOptions);
        RasterIO.closeDataSet(ds);
    }

}
