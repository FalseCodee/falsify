package falsify.falsify.utils;

import falsify.falsify.Falsify;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class GifReader {

    public final ImageFrame[] frames;

    private final String id;
    private final Timer timer = new Timer();
    private int index = 0;

    public GifReader(String id, String url) {
        this.id = id;
        try {
            frames = getGifFrames(new URL(url).openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ImageFrame[] getGifFrames(InputStream stream) throws IOException{
        ArrayList<ImageFrame> frames = new ArrayList<>(2);

        ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
        reader.setInput(ImageIO.createImageInputStream(stream));

        int width = -1;
        int height = -1;

        IIOMetadata metadata = reader.getStreamMetadata();
        if (metadata != null) {
            IIOMetadataNode globalRoot = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());

            NodeList globalScreenDescriptor = globalRoot.getElementsByTagName("LogicalScreenDescriptor");

            if (globalScreenDescriptor != null && globalScreenDescriptor.getLength() > 0) {
                IIOMetadataNode screenDescriptor = (IIOMetadataNode) globalScreenDescriptor.item(0);

                if (screenDescriptor != null) {
                    width = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenWidth"));
                    height = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenHeight"));
                }
            }
        }

        BufferedImage master = null;
        Graphics2D masterGraphics = null;

        for (int frameIndex = 0;; frameIndex++) {
            BufferedImage image;
            try {
                image = reader.read(frameIndex);
            } catch (IndexOutOfBoundsException io) {
                break;
            }

            if (width == -1 || height == -1) {
                width = image.getWidth();
                height = image.getHeight();
            }

            IIOMetadataNode root = (IIOMetadataNode) reader.getImageMetadata(frameIndex).getAsTree("javax_imageio_gif_image_1.0");
            IIOMetadataNode gce = (IIOMetadataNode) root.getElementsByTagName("GraphicControlExtension").item(0);
            int delay = Integer.parseInt(gce.getAttribute("delayTime")) * 10;
            String disposal = gce.getAttribute("disposalMethod");

            int x = 0;
            int y = 0;

            if (master == null) {
                master = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                masterGraphics = master.createGraphics();
                masterGraphics.setBackground(new Color(0, 0, 0, 0));
            } else {
                NodeList children = root.getChildNodes();
                for (int nodeIndex = 0; nodeIndex < children.getLength(); nodeIndex++) {
                    Node nodeItem = children.item(nodeIndex);
                    if (nodeItem.getNodeName().equals("ImageDescriptor")) {
                        NamedNodeMap map = nodeItem.getAttributes();
                        x = Integer.parseInt(map.getNamedItem("imageLeftPosition").getNodeValue());
                        y = Integer.parseInt(map.getNamedItem("imageTopPosition").getNodeValue());
                    }
                }
            }
            masterGraphics.drawImage(image, x, y, null);

            BufferedImage copy = new BufferedImage(master.getColorModel(), master.copyData(null), master.isAlphaPremultiplied(), null);
            LegacyIdentifier legId = new LegacyIdentifier(id + "-" + frameIndex, copy.getWidth(), copy.getHeight());
            frames.add(new ImageFrame(copy, legId, delay, disposal));
            Falsify.textureCacheManager.loadTexture(legId, copy);

            if (disposal.equals("restoreToPrevious")) {
                BufferedImage from = null;
                for (int i = frameIndex - 1; i >= 0; i--) {
                    if (!frames.get(i).disposal().equals("restoreToPrevious") || frameIndex == 0) {
                        from = frames.get(i).image();
                        break;
                    }
                }

                master = new BufferedImage(from.getColorModel(), from.copyData(null), from.isAlphaPremultiplied(), null);
                masterGraphics = master.createGraphics();
                masterGraphics.setBackground(new Color(0, 0, 0, 0));
            } else if (disposal.equals("restoreToBackgroundColor")) {
                masterGraphics.clearRect(x, y, image.getWidth(), image.getHeight());
            }
        }
        reader.dispose();

        return frames.toArray(new ImageFrame[0]);
    }

    public void destroy() {
        TextureCacheManager tcm = Falsify.textureCacheManager;
        for(ImageFrame frame : frames) {
            tcm.destroyTexture(frame.id);
        }
    }

    public LegacyIdentifier getCurrentFrame() {
        if(frames == null || frames.length == 0) return null;
        ImageFrame frame = frames[index];
        if(timer.hasTimeElapsed(frame.delay(), true)) index = (index + 1) % frames.length;
        return frame.id();
    }

    private record ImageFrame(BufferedImage image, LegacyIdentifier id, int delay, String disposal) { }
}
