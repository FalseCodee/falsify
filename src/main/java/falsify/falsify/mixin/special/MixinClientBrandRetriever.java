package falsify.falsify.mixin.special;

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
        return "Feather Fabric";
    }
}
