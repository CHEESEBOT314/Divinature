package com.bigchickenstudios.divinature.client;

import com.bigchickenstudios.divinature.block.ModBlocks;
import com.bigchickenstudios.divinature.client.renderer.overlay.MortarOverlayRenderer;
import com.bigchickenstudios.divinature.client.renderer.overlay.OverlayRenderer;
import com.bigchickenstudios.divinature.client.renderer.tileentity.InfuserTileEntityRenderer;
import com.bigchickenstudios.divinature.client.renderer.tileentity.MortarTileEntityRenderer;
import com.bigchickenstudios.divinature.tileentity.ModTileEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.function.Function;
import java.util.function.Supplier;

public final class Setup {

    public static void go() {
        setRenderType(ModBlocks.BLACKBELL, RenderType.getCutout());
        setRenderType(ModBlocks.BURDOCK, RenderType.getCutout());
        setRenderType(ModBlocks.MORTAR, RenderType.getCutout());
        setRenderType(ModBlocks.INFUSER, RenderType.getCutout());

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

    public static void preStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation() == PlayerContainer.LOCATION_BLOCKS_TEXTURE) {
            event.addSprite(MortarTileEntityRenderer.TEXTURE.getTextureLocation());
        }
    }

    public static void blockColours(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((state, lightReader, pos, i) ->
                        (lightReader != null && pos != null) ? BiomeColors.getFoliageColor(lightReader, pos) : FoliageColors.getDefault(),
                ModBlocks.ELM_LEAVES.get());
    }

    public static void itemColours(ColorHandlerEvent.Item event) {
        event.getItemColors().register((stack, i) ->
            event.getBlockColors().getColor(((BlockItem)stack.getItem()).getBlock().getDefaultState(), null, null, i),
                ModBlocks.ELM_LEAVES.get());
    }

    private Setup() {}
}
