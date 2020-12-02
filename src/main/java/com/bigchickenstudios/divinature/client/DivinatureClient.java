package com.bigchickenstudios.divinature.client;

import com.bigchickenstudios.divinature.block.ModBlocks;
import com.bigchickenstudios.divinature.client.multiplayer.ClientResearchManager;
import com.bigchickenstudios.divinature.client.particle.InfuserParticle;
import com.bigchickenstudios.divinature.client.renderer.overlay.MortarOverlayRenderer;
import com.bigchickenstudios.divinature.client.renderer.overlay.OverlayRenderer;
import com.bigchickenstudios.divinature.client.renderer.tileentity.InfuserTileEntityRenderer;
import com.bigchickenstudios.divinature.client.renderer.tileentity.MortarTileEntityRenderer;
import com.bigchickenstudios.divinature.particle.ModParticleTypes;
import com.bigchickenstudios.divinature.tileentity.ModTileEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class DivinatureClient {

    private static final ClientResearchManager RESEARCH_MANAGER = new ClientResearchManager(Minecraft.getInstance());

    public static ClientResearchManager getResearchManager() {
        return RESEARCH_MANAGER;
    }

    public static void registerListeners() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(DivinatureClient::init);
        bus.addListener(DivinatureClient::preStitch);
        bus.addListener(DivinatureClient::blockColours);
        bus.addListener(DivinatureClient::itemColours);
        bus.addListener(DivinatureClient::particleFactories);
    }

    private static void init(FMLClientSetupEvent event) {

        setRenderType(ModBlocks.BLACKBELL, RenderType.getCutout());
        setRenderType(ModBlocks.BURDOCK, RenderType.getCutout());
        setRenderType(ModBlocks.MORTAR, RenderType.getCutout());
        setRenderType(ModBlocks.INFUSER, RenderType.getCutout());
        setRenderType(ModBlocks.STONE_BRAZIER, RenderType.getCutout());
        setRenderType(ModBlocks.GRANITE_BRAZIER, RenderType.getCutout());
        setRenderType(ModBlocks.DIORITE_BRAZIER, RenderType.getCutout());
        setRenderType(ModBlocks.ANDESITE_BRAZIER, RenderType.getCutout());

        bindRenderer(ModTileEntityTypes.MORTAR, (d) -> new MortarTileEntityRenderer(d, Minecraft.getInstance().getItemRenderer()));
        bindRenderer(ModTileEntityTypes.INFUSER, (d) -> new InfuserTileEntityRenderer(d, Minecraft.getInstance().getItemRenderer()));

        bindOverlay(ModTileEntityTypes.MORTAR, MortarOverlayRenderer::new);
    }

    private static <T extends Block> void setRenderType(RegistryObject<T> block, RenderType type) {
        block.ifPresent((b) -> RenderTypeLookup.setRenderLayer(b, type));
    }

    private static <T extends TileEntity> void bindRenderer(RegistryObject<TileEntityType<T>> type, Function<? super TileEntityRendererDispatcher, ? extends TileEntityRenderer<? super T>> supplier) {
        type.ifPresent((t) -> ClientRegistry.bindTileEntityRenderer(t, supplier));
    }

    private static <T extends TileEntity> void bindOverlay(RegistryObject<TileEntityType<T>> type, Supplier<? extends OverlayRenderer<? super T>> supplier) {
        type.ifPresent((t) -> OverlayRenderer.bindOverlayRenderer(t, supplier));
    }

    private static void preStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation() == PlayerContainer.LOCATION_BLOCKS_TEXTURE) {
            event.addSprite(MortarTileEntityRenderer.TEXTURE.getTextureLocation());
        }
    }

    private static void blockColours(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((state, lightReader, pos, i) ->
                        (lightReader != null && pos != null) ? BiomeColors.getFoliageColor(lightReader, pos) : FoliageColors.getDefault(),
                ModBlocks.ELM_LEAVES.get());
    }

    private static void itemColours(ColorHandlerEvent.Item event) {
        event.getItemColors().register((stack, i) ->
            event.getBlockColors().getColor(((BlockItem)stack.getItem()).getBlock().getDefaultState(), null, null, i),
                ModBlocks.ELM_LEAVES.get());
    }

    private static void particleFactories(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(ModParticleTypes.INFUSER.get(), InfuserParticle.Factory::new);
    }

    public static <C extends IInventory, T extends IRecipe<C>> List<T> getRecipes(IRecipeType<T> type) {
        return Minecraft.getInstance().world.getRecipeManager().getRecipesForType(type);
    }

    private DivinatureClient() {}
}
