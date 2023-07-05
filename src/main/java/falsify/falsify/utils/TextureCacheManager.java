package falsify.falsify.utils;

import falsify.falsify.Falsify;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.net.URL;
import java.util.HashMap;

public class TextureCacheManager {
    private final HashMap<String, LegacyIdentifier> textures;
    public final Thread loadingThread;

    public final File textureDir;

    public TextureCacheManager() {
        this.textures = new HashMap<>();
        this.textureDir = new File(Falsify.clientDir + "\\textures");
        textureDir.mkdirs();

        loadingThread = new FalseRunnable() {
            @Override
            public void run() {
                cacheTextureFromUrlAsync("title", "https://raw.githubusercontent.com/FalseCodee/legacy-client-assets/main/legacy_client.png", true);
                cacheTextureFromUrlAsync("pizza-hut", "https://i.imgur.com/yx8Gopg.png", false);
                cacheTextureFromUrlAsync("dev_cape", "https://raw.githubusercontent.com/FalseCodee/legacy-client-assets/main/legacy_dev_cape.png",true);
            }
        }.runTaskAsync();
    }

    public void cacheTextureFromUrl(String textureName, String url, boolean saveTexture) {
        if(saveTexture && loadFileTexture(textureName)) {
            return;
        }
        NativeImage image = null;
        int i = 0;
        while (image == null) {
            if(i > 3) return;
            try {
                URL imageURL = new URL(url);
                InputStream imageStream = imageURL.openStream();

                image = NativeImage.read(imageStream);
                if(saveTexture) saveTexture(textureName, image);
            } catch (IOException ignored) {}
            i++;
        }

        loadTexture(textureName, image);
    }

    private void loadTexture(String textureName, NativeImage image) {
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
        LegacyIdentifier identifier = new LegacyIdentifier(textureName, image.getWidth(), image.getHeight());
        textureManager.registerTexture(identifier, texture);

        textures.put(textureName, identifier);
    }
    public Thread cacheTextureFromUrlAsync(String textureName, String url, boolean saveTexture) {
       return new FalseRunnable() {
            @Override
            public void run() {
                cacheTextureFromUrl(textureName, url, saveTexture);
            }
        }.runTaskAsync();
    }

    public void saveTexture(String textureName, NativeImage image) throws IOException {
        File imageFile = new File(textureDir.getAbsolutePath() + "\\" + textureName + ".png");
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image.getBytes()));
        ImageIO.write(bufferedImage, "png", imageFile);
    }

    public boolean loadFileTexture(String textureName) {
        try {
            File imageFile = new File(textureDir.getAbsolutePath() + "\\" + textureName + ".png");
            if(!imageFile.exists()) return false;
            loadTexture(textureName, NativeImage.read(new FileInputStream(imageFile)));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private byte[] toByteArray(BufferedImage image) {
        return ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
    }

    public void destroyTexture(String textureName) {
        LegacyIdentifier identifier = textures.get(textureName);
        if(identifier == null) return;

        Falsify.mc.getTextureManager().destroyTexture(identifier);
        textures.remove(textureName);
    }

    public LegacyIdentifier getIdentifier(String textureName) {
        return textures.getOrDefault(textureName, new LegacyIdentifier("dirt", 16,16));
    }
}
