package falsify.falsify.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public class TextureCacheManager {
    private final HashMap<String, LegacyIdentifier> textures;

    public TextureCacheManager() {
        this.textures = new HashMap<>();
        new FalseRunnable() {
            @Override
            public void run() {
                cacheTextureFromUrl("pizza-hut", "https://i.imgur.com/yx8Gopg.png");
            }
        }.runTaskAsync();
    }

    public void cacheTextureFromUrl(String textureName, String url) {
        NativeImage image = null;
        try {
            URL imageURL = new URL(url);
            InputStream imageStream = imageURL.openStream();

            image = NativeImage.read(imageStream);
        } catch (Exception e) {
            return;
        }

        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
        LegacyIdentifier identifier = new LegacyIdentifier(textureName, image.getWidth(), image.getHeight());
        textureManager.registerTexture(identifier, texture);

        textures.put(textureName, identifier);
    }

    public LegacyIdentifier getIdentifier(String textureName) {
        return textures.getOrDefault(textureName, new LegacyIdentifier("dirt", 16,16));
    }
}
