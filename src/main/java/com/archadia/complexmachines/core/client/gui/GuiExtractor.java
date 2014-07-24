package com.archadia.complexmachines.core.client.gui;

import com.archadia.complexmachines.core.common.container.ContainerExtractor;
import com.archadia.complexmachines.core.common.tileentity.TileEntityExtractor;
import com.archadia.complexmachines.helper.ArchHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * @author Archadia
 */
public class GuiExtractor extends GuiContainer {

  private static TileEntityExtractor tileINV;
  private ArchHelper helper = new ArchHelper();

  public GuiExtractor(InventoryPlayer par1InventoryPlayer, TileEntityExtractor tile) {
    super(new ContainerExtractor(par1InventoryPlayer, tile));
    tileINV = (TileEntityExtractor) tile;

    this.xSize = 209;
  }

  @SuppressWarnings("unchecked")
  protected void drawGuiContainerForegroundLayer(int par1, int par2) {
    int k = (this.width - this.xSize) / 2;
    int l = (this.height - this.ySize) / 2;

    buttonList.clear();
    buttonList.add(new GuiButton(0, k + 147, l + 6, 20, 20, "M"));
    String s = tileINV.hasCustomInventoryName() ? tileINV.getInvName() : I18n.format(tileINV.getInvName());
    this.fontRendererObj.drawString(s, this.xSize / 2 - 44, 7, 4210752);
  }

  protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    drawGuiMain();
  }

  private void drawGuiMain() {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(new ResourceLocation("complexmachines", "textures/gui/extractor.png"));
    int k = (this.width - this.xSize) / 2;
    int l = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
  }

  public void actionPerformed(GuiButton button) {
    switch(button.id){
      case 0:

    }
  }
}
