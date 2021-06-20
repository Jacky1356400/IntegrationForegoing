package jackyy.integrationforegoing.integration.compat.crafttweaker.actions;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.buuz135.industrial.api.recipe.ore.OreFluidEntrySieve;
import jackyy.integrationforegoing.integration.compat.crafttweaker.CraftTweakerCompat;
import jackyy.integrationforegoing.util.EnumAction;
import jackyy.integrationforegoing.util.ModUtils;
import net.minecraft.item.ItemStack;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.industrialforegoing.FluidSievingMachine")
public class CraftTweakerActionFluidSievingMachine {

    @ZenCodeType.Method
    public static void add(IFluidStack input, IItemStack output, IItemStack sieveItem) {
        OreFluidEntrySieve entry = new OreFluidEntrySieve(input.getInternal(), output.getInternal(), sieveItem.getInternal());
        CraftTweakerAPI.apply(new AddEntry(entry));
    }

    @ZenCodeType.Method
    public static void remove(IItemStack output) {
        CraftTweakerAPI.apply(new RemoveEntry(output.getInternal()));
    }

    private static class AddEntry implements IAction {
        private final OreFluidEntrySieve entry;

        private AddEntry(OreFluidEntrySieve entry) {
            this.entry = entry;
        }

        @Override
        public void apply() {
            CraftTweakerCompat.FLUID_SIEVING_MACHINE_ENTRIES.put(EnumAction.ADD, entry);
        }

        @Override
        public String describe() {
            return "Adding Fluid Sieving Machine Entry " + entry.getOutput().getDisplayName();
        }
    }

    private static class RemoveEntry implements IAction {
        private final ItemStack output;

        private RemoveEntry(ItemStack output) {
            this.output = output;
        }

        @Override
        public void apply() {
            CraftTweakerCompat.FLUID_SIEVING_MACHINE_ENTRIES.put(EnumAction.REMOVE, new OreFluidEntrySieve(ModUtils.getFakeFluid(), output, ItemStack.EMPTY));
        }

        @Override
        public String describe() {
            return "Removing Fluid Sieving Machine Entry " + output.getDisplayName();
        }
    }

}