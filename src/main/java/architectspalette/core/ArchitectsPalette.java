package architectspalette.core;

import architectspalette.core.config.APConfig;
import architectspalette.core.crafting.WarpingRecipe;
import architectspalette.core.datagen.GatherData;
import architectspalette.core.integration.APCriterion;
import architectspalette.core.integration.APTrades;
import architectspalette.core.integration.APVerticalSlabsCondition;
import architectspalette.core.loot.WitheredBoneLootModifier;
import architectspalette.core.registry.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(value = ArchitectsPalette.MOD_ID)
public class ArchitectsPalette {
    public static final String MOD_ID = "architects_palette";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID.toUpperCase());
    public static ArchitectsPalette instance;

    public ArchitectsPalette() {
        instance = this;

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, APConfig.COMMON_CONFIG);

        MiscRegistry.PARTICLE_TYPES.register(modEventBus);
        APSounds.SOUNDS.register(modEventBus);
        APBlocks.BLOCKS.register(modEventBus);
        APItems.ITEMS.register(modEventBus);
        APFeatures.FEATURES.register(modEventBus);
//        APTileEntities.TILE_ENTITY_TYPES.register(modEventBus);

        modEventBus.addListener(EventPriority.LOWEST, this::setupCommon);
        modEventBus.addListener(EventPriority.LOWEST, this::setupClient);
        modEventBus.addGenericListener(RecipeSerializer.class, this::registerRecipeSerializers);
        modEventBus.addGenericListener(GlobalLootModifierSerializer.class, this::registerLootSerializers);

        forgeBus.addListener(APConfiguredFeatures::biomeLoadEvent);

        CraftingHelper.register(new APVerticalSlabsCondition.Serializer());
        
        GatherData.load();

    }

    void setupCommon(final FMLCommonSetupEvent event) {
        event.enqueueWork(APConfiguredFeatures::registerProcessedFeatures);

        APBlockProperties.registerFlammables();
        APTrades.registerTrades();

        // Is this okay to go here?
        APCriterion.register();
    }

    void registerRecipeSerializers(final RegistryEvent.Register<RecipeSerializer<?>> event) {
        //Register the recipe type
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(WarpingRecipe.TYPE.toString()), WarpingRecipe.TYPE);
        //Register the serializer
        event.getRegistry().register(WarpingRecipe.SERIALIZER);
    }

    void registerLootSerializers(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().register(WitheredBoneLootModifier.SERIALIZER);
    }

    void setupClient(final FMLClientSetupEvent event) {
        APBlockProperties.setupRenderLayers();
    }
}