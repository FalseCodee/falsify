package falsify.falsify.module.modules.misc;

import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.TextSetting;

public class ClientBrand extends Module {

    public TextSetting brand = new TextSetting("brand", "Legacy Client");

    public ClientBrand() {
        super("Brand","Change the client brand from fabric to anything!", false, Category.MISC, -1);
        settings.add(brand);
    }
}
