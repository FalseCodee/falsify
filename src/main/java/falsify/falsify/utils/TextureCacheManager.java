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
    private final ConcurrentHashMap<String, CompletableFuture<LegacyIdentifier>> textures;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    public final File textureDir;

    public TextureCacheManager() {
        this.textures = new ConcurrentHashMap<>();
        this.textureDir = new File(Falsify.clientDir + "\\textures");
        textureDir.mkdirs();
        Falsify.logger.info("Created: Texture Cache Manager");
    }

    public void registerTextures() {
        cacheTextureFromUrlAsync("title", "https://cdn.discordapp.com/attachments/755141818743652444/1129797788981534740/New_Project_1.png", true);
        cacheTextureFromUrlAsync("pizza-hut", "https://i.imgur.com/g74aFlz.png", false);
        cacheTextureFromUrlAsync("armorup_cape", "https://cdn.discordapp.com/attachments/1069468268013834258/1129811644567015565/armorup_staff_cape.png", true);
        cacheTextureFromUrlAsync("dev_cape", "https://raw.githubusercontent.com/FalseCodee/legacy-client-assets/main/legacy_dev_cape.png",true);
    }



    public LegacyIdentifier cacheTextureFromUrl(String textureName, String url, boolean saveTexture) {
        if(saveTexture) {
            LegacyIdentifier identifier = loadFileTexture(textureName);
            if(identifier != null) return identifier;
        }
        NativeImage image = null;
        int i = 0;
        while (image == null) {
            if(i > 3) return null;
            try {
                URL imageURL = new URL(url);
                InputStream imageStream = imageURL.openStream();

                image = NativeImage.read(imageStream);
                if(saveTexture) saveTexture(textureName, image);
            } catch (IOException e) {e.printStackTrace();}
            i++;
        }

        return loadTexture(textureName, image);
    }

    private LegacyIdentifier loadTexture(String textureName, NativeImage image) {
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
        LegacyIdentifier identifier = new LegacyIdentifier(textureName, image.getWidth(), image.getHeight());
        textureManager.registerTexture(identifier, texture);
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
    public void cacheTextureFromUrlAsync(String textureName, String url, boolean saveTexture) {
        CompletableFuture<LegacyIdentifier> future = CompletableFuture.supplyAsync(() -> cacheTextureFromUrl(textureName, url, saveTexture), executor);
        textures.put(textureName, future);
    }

    public void saveTexture(String textureName, NativeImage image) throws IOException {
        File imageFile = new File(textureDir.getAbsolutePath() + "\\" + textureName + ".png");
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image.getBytes()));
        ImageIO.write(bufferedImage, "png", imageFile);
    }

    public LegacyIdentifier loadFileTexture(String textureName) {
        try {
            File imageFile = new File(textureDir.getAbsolutePath() + "\\" + textureName + ".png");
            if(!imageFile.exists()) return null;
            return loadTexture(textureName, NativeImage.read(new FileInputStream(imageFile)));
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
        LegacyIdentifier identifier = textures.get(textureName).getNow(null);
        if(identifier == null) return;

        destroyTexture(identifier);
        textures.remove(textureName);
    }

    public void destroyTexture(Identifier id) {
        Falsify.mc.getTextureManager().destroyTexture(id);
    }

    public CompletableFuture<LegacyIdentifier> getIdentifier(String textureName) {
        return textures.get(textureName);
    }

    public ConcurrentHashMap<String, CompletableFuture<LegacyIdentifier>> getTextures() {
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
