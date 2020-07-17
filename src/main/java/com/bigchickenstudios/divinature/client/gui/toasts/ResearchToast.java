package com.bigchickenstudios.divinature.client.gui.toasts;

import com.bigchickenstudios.divinature.Constants;
import com.bigchickenstudios.divinature.research.Research;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

public class ResearchToast implements IToast {

    private static final ResourceLocation TEXTURES = Constants.rl("textures/gui/toasts.png");

    private final Research research;

    public ResearchToast(Research researchIn) {
        this.research = researchIn;
    }

    @Nonnull
    @Override
    public Visibility func_230444_a_(@Nonnull MatrixStack matrixStackIn, @Nonnull ToastGui toastGuiIn, long timeIn) {
        toastGuiIn.getMinecraft().getTextureManager().bindTexture(TEXTURES);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        toastGuiIn.func_238474_b_(matrixStackIn, 0, 0, 0, 0, this.func_230445_a_(), this.func_238540_d_());
        boolean flag = false;
        ITextComponent info = this.research.getInfo();
        if (info != null) {
            flag = true;
            List<ITextProperties> list = toastGuiIn.getMinecraft().fontRenderer.func_238425_b_(info, 125);
            if (list.size() == 1) {
                toastGuiIn.getMinecraft().fontRenderer.func_238421_b_(matrixStackIn, I18n.format("divinature.research.toast"), 30.0F, 7.0F, 0x00FFFF00 | 0xFF000000);
                toastGuiIn.getMinecraft().fontRenderer.func_238422_b_(matrixStackIn, list.get(0), 30.0F, 18.0F, 0xFFFFFFFF);
            }
            else {
                int alpha;
                if (timeIn < 1500L) {
                    alpha = MathHelper.floor(MathHelper.clamp((float)(1500L - timeIn) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 0x04000000;
                    toastGuiIn.getMinecraft().fontRenderer.func_238421_b_(matrixStackIn, I18n.format("divinature.research.toast"), 30.0F, 11.0F, 0x00FFFF00 | alpha);
                }
                else {
                    alpha = MathHelper.floor(MathHelper.clamp((float)(timeIn - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 0x04000000;
                    int x = (this.func_238540_d_() - list.size() * 9) / 2;
                    for (Iterator<ITextProperties> iterator = list.iterator(); iterator.hasNext(); x += 9) {
                        ITextProperties iTextProperties = iterator.next();
                        toastGuiIn.getMinecraft().fontRenderer.func_238422_b_(matrixStackIn, iTextProperties, 30.0F, (float)x, 0x00FFFFFF | alpha);
                    }
                }
            }
        }

        ItemStack icon = this.research.getIcon();
        if (icon != null) {
            flag = true;
            toastGuiIn.getMinecraft().getItemRenderer().func_239390_c_(icon, 8, 8);
        }

        return !flag || timeIn > 5000L ? Visibility.HIDE : Visibility.SHOW;
    }
}
