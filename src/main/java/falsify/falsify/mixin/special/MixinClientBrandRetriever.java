package falsify.falsify.mixin.special;

import falsify.falsify.module.ModuleManager;
import falsify.falsify.module.modules.misc.ClientBrand;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.obfuscate.DontObfuscate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientBrandRetriever.class)
public abstract class MixinClientBrandRetriever {

    /**
     * @author FalseCode
     * @reason Why not.
     */
    @DontObfuscate
    @Overwrite(remap = false)
    public static String getClientModName() {
        ClientBrand brand = ModuleManager.getModule(ClientBrand.class);
        if(brand != null && brand.toggled){
            return brand.brand;
        }
        return "vanilla";
    }
}
