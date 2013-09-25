package archadia.complexmachines.core.common.tileentity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import archadia.complexmachines.core.common.ComplexMachines;
import archadia.complexmachines.prefab.tileentity.TileEntityAdvancedMachine;

/**
 * @author Archadia
 *
 */
public class TileEntityExtractor extends TileEntityAdvancedMachine {
	
	private final static TileEntityExtractor tileEntityBase = new TileEntityExtractor();   
 	
	private Random rand = new Random();
	
	private ArrayList<Integer> validOre = new ArrayList<Integer>();
	
	public void addExtractorValidOre(int blockID) {
		validOre.add(blockID);
	}
	
	public void addExtractorVanillaOre() {
		addExtractorValidOre(Block.oreCoal.blockID);
		addExtractorValidOre(Block.oreIron.blockID);
		addExtractorValidOre(Block.oreGold.blockID);
		addExtractorValidOre(Block.oreRedstone.blockID);
		addExtractorValidOre(Block.oreLapis.blockID);
	}
	
	public final static TileEntityExtractor instance() {
		return tileEntityBase;
	}
	 
	public TileEntityExtractor() {
		setInventorySize(6);
		setMaxTicks(200);
		addExtractorVanillaOre();
	}
	
	public void updateEntity() {
		super.updateEntity();

		if(!worldObj.isRemote) {
			if(ComplexMachines.oldExtractorMode) {
				findOre();
			} else {
				if(worldObj.getWorldTime()%20 == 0) findOre();
			}
		}
	}
	
	private void findOre() {
		boolean oreFound = false;
		int tries = 0;
		while(!oreFound) {
			tries++;
			
			if(tries > 5) {
				return;
			}
			
			int targetX = xCoord - 150;
			int targetZ = zCoord - 150;
			int targetY = rand.nextInt(15) + 5;
	
			targetX += (rand.nextInt(300));
			targetZ += (rand.nextInt(300));
	
			int targetId = worldObj.getBlockId(targetX, targetY, targetZ);
			
			boolean ore = isOre(targetId);
			
			if (worldObj.getChunkFromBlockCoords(targetX, targetZ).isChunkLoaded && ore) {
				oreFound = true;
	
				ItemStack drop = Block.blocksList[targetId].getBlockDropped(worldObj, targetX, targetY , targetZ, worldObj.getBlockMetadata(targetX, targetY, targetZ), 0).get(0);
				worldObj.setBlock(targetY, targetY, targetZ, 0);
				dropItems(drop);
			}
		}
	}
	
	private boolean isOre(int id) {
		for(int oreID : validOre) {
			if(id == oreID) {
				return true;
			}
		}
		return false;
	}
	
	private TileEntityChest findChest() {

		TileEntity entityBeingChecked = worldObj.getBlockTileEntity(xCoord,
				yCoord, zCoord);
		if (worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord) instanceof TileEntityChest) {
			return (TileEntityChest) worldObj.getBlockTileEntity(xCoord + 1,
					yCoord, zCoord);
		}
		if (worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord) instanceof TileEntityChest) {
			return (TileEntityChest) worldObj.getBlockTileEntity(xCoord - 1,
					yCoord, zCoord);
		}
		if (worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord) instanceof TileEntityChest) {
			return (TileEntityChest) worldObj.getBlockTileEntity(xCoord,
					yCoord + 1, zCoord);
		}
		if (worldObj.getBlockTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityChest) {
			return (TileEntityChest) worldObj.getBlockTileEntity(xCoord,
					yCoord - 1, zCoord);
		}
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + 1) instanceof TileEntityChest) {
			return (TileEntityChest) worldObj.getBlockTileEntity(xCoord,
					yCoord, zCoord + 1);
		}
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityChest) {
			return (TileEntityChest) worldObj.getBlockTileEntity(xCoord,
					yCoord, zCoord - 1);
		}
		return null;
	}
	
	private void dropItems(ItemStack item) {
		ItemStack itemStack = item;

		if(itemStack != null) {
			for (int i = 0; i < this.inventory.length;i++) {
				itemStack = this.addStackToInventory(i, this, itemStack);
			}
		}

	}

	public ItemStack addStackToInventory(int slotIndex, IInventory inventory, ItemStack itemStack)
	{
		if(itemStack != null) {
			if (inventory.getSizeInventory() > slotIndex)
			{
				ItemStack stackInInventory = inventory.getStackInSlot(slotIndex);
	
				if (stackInInventory == null)
				{
					inventory.setInventorySlotContents(slotIndex, itemStack);
					if (inventory.getStackInSlot(slotIndex) == null)
					{
						return itemStack;
					}
					return null;
				}
				if(stackInInventory.isItemEqual(itemStack) && stackInInventory.isStackable())
				{
					stackInInventory = stackInInventory.copy();
					int stackLim = Math.min(inventory.getInventoryStackLimit(), itemStack.getMaxStackSize());
					int rejectedAmount = Math.max((stackInInventory.stackSize + itemStack.stackSize) - stackLim, 0);
					stackInInventory.stackSize = Math.min(Math.max((stackInInventory.stackSize + itemStack.stackSize - rejectedAmount), 0), inventory.getInventoryStackLimit());
					itemStack.stackSize = rejectedAmount;
					inventory.setInventorySlotContents(slotIndex, stackInInventory);
				}
			}
	
			if (itemStack.stackSize <= 0)
			{
				return null;
			}
		}

		return itemStack;
	}

	public String getInvName() {
		return "Extractor";
	}
}
