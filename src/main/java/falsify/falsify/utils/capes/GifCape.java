package falsify.falsify.utils.capes;

import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.GifReader;
import falsify.falsify.utils.LegacyIdentifier;
import org.jetbrains.annotations.Nullable;

public class GifCape extends Cape{
    private GifReader gif;
    public GifCape(String capeName, String gifUrl) {
        super(capeName);
        setGif(gifUrl);
    }

    @Override
    public @Nullable LegacyIdentifier getCape() {
        return getCurrentFrame();
    }

    @Override
    protected void setCape() {

    }

    public void setGif(String url) {
        if(gif != null) {
            gif.destroy();
            gif = null;
        }
        new FalseRunnable() {
            @Override
            public void run() {
                gif = new GifReader(getCapeName(), url);
            }
        }.runTaskAsync();
    }

    public LegacyIdentifier getCurrentFrame() {
        if(gif == null) {
            return null;
        }
        return gif.getCurrentFrame();
    }
}
