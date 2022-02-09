package architectspalette.core.compat;

import architectspalette.core.ArchitectsPalette;
import architectspalette.core.crafting.WarpingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Stream;

import static architectspalette.core.registry.APBlocks.*;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(ArchitectsPalette.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new WarpingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
//        registration.addRecipeCatalyst(new ItemStack(Blocks.NETHER_PORTAL.asItem()), WarpingRecipeCategory.UID);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        //Register recipes
        registration.addRecipes(Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(WarpingRecipe.TYPE), WarpingRecipeCategory.UID);

        //Register item info
        addItemInfo(registration, CHISELED_ABYSSALINE_BRICKS, "chiseled_chargeable");
        Stream.of(ABYSSALINE, ABYSSALINE_BRICKS, ABYSSALINE_PILLAR, ABYSSALINE_BRICK_SLAB, ABYSSALINE_TILES, ABYSSALINE_TILE_SLAB, ABYSSALINE_LAMP_BLOCK, ABYSSALINE_BRICK_VERTICAL_SLAB, ABYSSALINE_TILE_VERTICAL_SLAB).
                forEach((i) -> addItemInfo(registration, i, "chargeable"));
        Stream.of(PLACID_ACACIA_TOTEM, GRINNING_ACACIA_TOTEM, SHOCKED_ACACIA_TOTEM, BLANK_ACACIA_TOTEM)
                .forEach((i) -> addItemInfo(registration, i, "totem_carving"));
    }

    private static void addItemInfo(IRecipeRegistration register, RegistryObject<? extends ItemLike> item, String infoString) {
        addItemInfo(register, item.get(), infoString);
    }

    private static void addItemInfo(IRecipeRegistration register, ItemLike item, String infoString) {
        register.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM, new TranslatableComponent(ArchitectsPalette.MOD_ID + ".info." + infoString));
    }
}