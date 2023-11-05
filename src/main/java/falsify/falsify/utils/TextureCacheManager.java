package falsify.falsify.utils;

import falsify.falsify.Falsify;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TextureCacheManager {
    private final ConcurrentHashMap<String, LegacyIdentifier> textures;
    private final ExecutorService executor;
    public final File textureDir;

    public TextureCacheManager() {
        executor = Executors.newCachedThreadPool();
        this.textures = new ConcurrentHashMap<>();
        this.textureDir = new File(Falsify.clientDir + "\\textures");
        textureDir.mkdirs();
        Falsify.logger.info("Created: Texture Cache Manager");
    }

    public void registerTextures() {
        cacheTextureFromUrlAsync("title", "https://cdn.discordapp.com/attachments/755141818743652444/1129797788981534740/New_Project_1.png", true);
        cacheTextureFromUrlAsync("pizza-hut", "https://cdn.discordapp.com/attachments/1145507886852739152/1145507906041679892/image.png", false);
        cacheTextureFromUrlAsync("armorup_cape", "https://media.discordapp.net/attachments/756973530159251581/1165306341074669578/image.png", true);
        cacheTextureFromUrlAsync("title_background", "https://wallpaperaccess.com/full/4003568.png", false);
        cacheTextureFromUrlAsync("dev_cape", "https://raw.githubusercontent.com/FalseCodee/legacy-client-assets/main/legacy_dev_cape.png",true);
    }



    public LegacyIdentifier cacheTextureFromUrl(String textureName, String url, boolean saveTexture) {
        return loadTexture(textureName, getTexture(textureName, url, saveTexture));
    }

    private NativeImage getTexture(String textureName, String url, boolean couldBeSaved) {
        if(couldBeSaved) {
            NativeImage image = getFileTexture(textureName);
            if(image != null) return image;
        }
        NativeImage image = null;
        int i = 0;
        while (image == null) {
            if(i > 3) return null;
            try {
                URL imageURL = new URL(url);
                InputStream imageStream = imageURL.openStream();

                image = NativeImage.read(imageStream);
                if(couldBeSaved) saveTexture(textureName, image);
            } catch (IOException e) {e.printStackTrace();}
            i++;
        }

        return image;
    }

    private LegacyIdentifier loadTexture(String textureName, NativeImage image) {
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
        LegacyIdentifier identifier = new LegacyIdentifier(textureName, image.getWidth(), image.getHeight());
        textureManager.registerTexture(identifier, texture);
        Falsify.logger.info("Loaded Texture: " + textureName);
        return identifier;
    }

    public Identifier loadTexture(Identifier identifier, BufferedImage image) {
        NativeImage ni;
        try {
            ni = NativeImage.read(toByteArray(image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        NativeImageBackedTexture texture = new NativeImageBackedTexture(ni);
        textureManager.registerTexture(identifier, texture);
        return identifier;
    }
    public CompletableFuture<NativeImage> cacheTextureFromUrlAsync(String textureName, String url, boolean saveTexture) {
        CompletableFuture<NativeImage> future = CompletableFuture.supplyAsync(() -> getTexture(textureName, url, saveTexture), executor);
        return future.whenCompleteAsync((nativeImage, throwable) -> {
            textures.put(textureName, loadTexture(textureName, nativeImage));
        }, Falsify.mc);
    }

    public void saveTexture(String textureName, NativeImage image) throws IOException {
        File imageFile = new File(textureDir.getAbsolutePath() + "\\" + textureName + ".png");
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image.getBytes()));
        ImageIO.write(bufferedImage, "png", imageFile);
    }

    public NativeImage getFileTexture(String textureName) {
        try {
            File imageFile = new File(textureDir.getAbsolutePath() + "\\" + textureName + ".png");
            if(!imageFile.exists()) return null;
            return NativeImage.read(new FileInputStream(imageFile));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ByteArrayInputStream toByteArray(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public void destroyTexture(String textureName) {
        LegacyIdentifier identifier = textures.get(textureName);
        if(identifier == null) return;

        destroyTexture(identifier);
        textures.remove(textureName);
    }

    public void destroyTexture(Identifier id) {
        Falsify.mc.getTextureManager().destroyTexture(id);
    }

    public LegacyIdentifier getIdentifier(String textureName) {
        return textures.get(textureName);
    }

    public ConcurrentHashMap<String, LegacyIdentifier> getTextures() {
        return textures;
    }

    private static final char RND_START = 'a';
    private static final char RND_END = 'z';
    private static final Random RND = new Random();

    private static String randomString() {
        return IntStream.range(0, 32)
                .mapToObj(operand -> String.valueOf((char) RND.nextInt(RND_START, RND_END + 1)))
                .collect(Collectors.joining());
    }

    @Contract(value = "-> new", pure = true)
    public static Identifier randomIdentifier() {
        return new Identifier("legacy", "temp/" + randomString());
    }
}
