package falsify.falsify.utils.fonts;


import falsify.falsify.Falsify;
import org.spongepowered.asm.util.Files;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Fonts {

    private final List<Font> fonts;

    public Fonts() {
        fonts = new ArrayList<>();
        init();
    }
    public void init() {
        addFont("Comfortaa-Regular.ttf");
    }

    public void addFont(String fontName) {
        fonts.add(createFont(fontName));
    }

    public Font createFont(String fontName) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, getFontFile(fontName));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream getFontFile(String fileName) {
        return Falsify.class.getClassLoader().getResourceAsStream("assets/legacy/fonts/" + fileName);
    }

    public Font[] getFonts() {
        Font[] font = new Font[fonts.size()];
        return fonts.toArray(font);
    }
}
