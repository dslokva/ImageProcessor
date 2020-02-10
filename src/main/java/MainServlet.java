import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

public class MainServlet extends HttpServlet {

    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {

        httpServletRequest.getRequestDispatcher("/index.jsp").forward(httpServletRequest, httpServletResponse);
    }

    public MainServlet() {
        super();
        System.out.println("Servlet created.");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
                if (!multiparts.isEmpty()) {
                    File fileOut = null;
                    for (FileItem item : multiparts) {
                        if (item.getSize() > 0) {
                            File receivedItem = new File(item.getName());
                            String originalFileName = receivedItem.getName();
                            String absoluteDiskPath = getServletContext().getRealPath("/output/");
                            fileOut = new File(absoluteDiskPath + FilenameUtils.getBaseName(receivedItem.getName()) + "/original.jpg");
                            item.write(fileOut);

                            if (fileOut.length() > 0) {
                                SettingsStore settings = SettingsStore.getInstance();
                                String compressEnabled = settings.getCompressEnabled();
                                String blurEnabled = settings.getBlurEnabled();
                                String histogramUpEnabled = settings.getHistogramUpEnabled();
                                String lightUpEnabled = settings.getLightUpEnabled();

                                if (compressEnabled != null && compressEnabled.equals("checked"))
                                    compressJpeg(fileOut);

                                if (blurEnabled != null && blurEnabled.equals("checked"))
                                     blur(fileOut, 4);

                                if (histogramUpEnabled != null && histogramUpEnabled.equals("checked"))
                                    histogramEqualise(fileOut);

                                if (lightUpEnabled != null && lightUpEnabled.equals("checked"))
                                    adjustBrigtness(fileOut);
                            }

                            request.setAttribute("message", "Файл \"" + originalFileName + "\" загружен успешно.");
                            request.setAttribute("filesize", getFileSizeKiloBytes(fileOut));
                            request.setAttribute("errorCode", 0);
                        } else {
                            request.setAttribute("message", "Файл не выбран!");
                            request.setAttribute("filesize", "");
                            request.setAttribute("errorCode", 1);
                        }
                    }
                }
            } catch (Exception ex) {
                request.setAttribute("message", "Ошибка загрузки: " + ex);
            }
        } else {
            request.setAttribute("message", "No File found");
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    private static String getFileSizeKiloBytes(File file) {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(file.length() / 1024) + "  kb";
    }

    private static void applyCLAHE(Mat srcArry, Mat dstArry) {
        //Function that applies the CLAHE algorithm to "dstArry".

        if (srcArry.channels() >= 3) {
            // READ RGB color image and convert it to Lab
            Mat channel = new Mat();
            Imgproc.cvtColor(srcArry, dstArry, Imgproc.COLOR_BGR2Lab);

            // Extract the L channel
            Core.extractChannel(dstArry, channel, 0);

            // apply the CLAHE algorithm to the L channel
            CLAHE clahe = Imgproc.createCLAHE();
            clahe.setClipLimit(0.6);
            clahe.setTilesGridSize(new Size(new Point(3,3)));
            clahe.apply(channel, channel);

            // Merge the the color planes back into an Lab image
            Core.insertChannel(channel, dstArry, 0);

            // convert back to RGB
            Imgproc.cvtColor(dstArry, dstArry, Imgproc.COLOR_Lab2BGR);
            // Temporary Mat not reused, so release from memory.
            channel.release();
        }
    }

    private static Mat BufferedImage2Mat(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);
    }

    private static BufferedImage Mat2BufferedImage(Mat matrix)throws IOException {
        MatOfByte mob=new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, mob);
        return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
    }

    public static void histogramEqualise(File inputFile) {
        // Reading the Image from the file and storing it in to a Matrix object
        String file = inputFile.getAbsolutePath();

        // Load the image
        Mat img = Imgcodecs.imread(file);

        // Creating an empty matrix
        Mat equ = new Mat();
        img.copyTo(equ);

        // Applying blur
        Imgproc.blur(equ, equ, new Size(1, 1));

        // Applying color
        Imgproc.cvtColor(equ, equ, Imgproc.COLOR_BGR2YCrCb);
        List<Mat> channels = new ArrayList<Mat>();

        // Splitting the channels
        Core.split(equ, channels);

        // Equalizing the histogram of the image
        Imgproc.equalizeHist(channels.get(0), channels.get(0));
        Core.merge(channels, equ);
        Imgproc.cvtColor(equ, equ, Imgproc.COLOR_YCrCb2BGR);

        Mat gray = new Mat();
        Imgproc.cvtColor(equ, gray, Imgproc.COLOR_BGR2GRAY);
        Mat grayOrig = new Mat();
        Imgproc.cvtColor(img, grayOrig, Imgproc.COLOR_BGR2GRAY);

        Imgcodecs.imwrite(inputFile.getParentFile()+"/heavyBrightness.jpg", equ);
    }

    private static void adjustBrigtness(File inputFile) throws IOException {
        BufferedImage biImg = ImageIO.read(inputFile);
//        Mat mat = new Mat(biImg.getHeight(), biImg.getWidth(), CvType.CV_8UC3);
        Mat mat = BufferedImage2Mat(biImg);

        applyCLAHE(mat, mat);

        BufferedImage biImgOut = Mat2BufferedImage(mat);
        ImageIO.write(biImgOut, "jpg", new File(inputFile.getParentFile()+"/slightBritness.jpg"));

    }

    public static void blur(File inputFile, int numberOfTimes) throws IOException {
        BufferedImage biImg = ImageIO.read(inputFile);
        Mat input = BufferedImage2Mat(biImg);

        Mat sourceImage;
        Mat destImage = input.clone();
        for (int i=0;i<numberOfTimes;i++) {
            sourceImage = destImage.clone();
            Imgproc.blur(sourceImage, destImage, new Size(5.0, 5.0));
        }
        BufferedImage biImgOut = Mat2BufferedImage(destImage);
        ImageIO.write(biImgOut, "jpg", new File(inputFile.getParentFile()+"/blur.jpg"));
    }

    public static void compressJpeg(File imageFile) throws IOException {
        File compressedImageFile = new File(imageFile.getParentFile()+"/compressed.jpg");

        InputStream is = new FileInputStream(imageFile);
        OutputStream os = new FileOutputStream(compressedImageFile);

        float quality = 0.5f;

        // create a BufferedImage as the result of decoding the supplied InputStream
        BufferedImage image = ImageIO.read(is);

        // get all image writers for JPG format
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");

        if (!writers.hasNext())
            throw new IllegalStateException("No writers found");

        ImageWriter writer = (ImageWriter) writers.next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();

        // compress to a given quality
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        // appends a complete image stream containing a single image and
        //associated stream and image metadata and thumbnails to the output
        writer.write(null, new IIOImage(image, null, null), param);

        // close all streams
        is.close();
        os.close();
        ios.close();
        writer.dispose();
    }
}
