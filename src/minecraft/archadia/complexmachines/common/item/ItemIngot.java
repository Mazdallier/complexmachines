package archadia.complexmachines.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import archadia.complexmachines.common.ComplexMachines;
import archadia.complexmachines.common.helper.ArchLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Archadia
 *
 */
public class ItemIngot extends ItemBase {

	public ItemIngot(int id, String name) {
		super(id, name);
		setCreativeTab(ComplexMachines.tabComplexMachines);
		setUnlocalizedName(name);
		addToLibrary(this);
		
	}
	
	public void addToLibrary(Item i) {
		ArchLoader.ingotLibrary.add(i);
	}
}
