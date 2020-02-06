package jackyy.integrationforegoing.integration.compat;

import com.buuz135.industrial.entity.EntityPinkSlime;
import gnu.trove.map.hash.THashMap;
import jackyy.gunpowderlib.helper.ObjectHelper;
import jackyy.integrationforegoing.util.ModNames;
import jackyy.integrationforegoing.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.shared.FluidsClientProxy;
import slimeknights.tconstruct.tools.TinkerTraits;
import slimeknights.tconstruct.tools.traits.TraitSlimey;

import java.util.Map;

public class TConstructCompat {

    private static final Map<String, Material> materials = new THashMap<>();
    private static final AbstractTrait SLIMEY_PINK = new TraitSlimey("pink", EntityPinkSlime.class);

    public static void preInit() {
        Material plastic = new Material(Reference.MODID + ".plastic", 0xFFADADAD);
        plastic.setCraftable(true);
        plastic.addTrait(TinkerTraits.stonebound);
        TinkerRegistry.addMaterialStats(
                plastic,
                new HeadMaterialStats(1500, 6.0f, -1.0f, HarvestLevels.STONE),
                new HandleMaterialStats(0.1f, 1500),
                new ExtraMaterialStats(150),
                new BowMaterialStats(20.0f, 4.2f, 2.5f)
        );
        TinkerRegistry.integrate(plastic).preInit();
        materials.put("plastic", plastic);

        Material pinkSlime = new Material(Reference.MODID + ".pink_slime", 0xFFF3AEC6);
        pinkSlime.setCraftable(true);
        pinkSlime.addTrait(SLIMEY_PINK);
        TinkerRegistry.addMaterialStats(
                pinkSlime,
                new HeadMaterialStats(2000, 3.0f, 1.0f, HarvestLevels.STONE),
                new HandleMaterialStats(2.5f, 2000),
                new ExtraMaterialStats(200),
                new BowMaterialStats(15.0f, 4.7f, 3.0f)
        );
        TinkerRegistry.integrate(pinkSlime).preInit();
        materials.put("pink_slime", pinkSlime);

        Material reinforcedPinkSlime = new Material(Reference.MODID + ".reinforced_pink_slime", 0xFFC279B6);
        reinforcedPinkSlime.addTrait(SLIMEY_PINK, MaterialTypes.HEAD);
        reinforcedPinkSlime.addTrait(TinkerTraits.dense, MaterialTypes.HEAD);
        reinforcedPinkSlime.addTrait(SLIMEY_PINK, MaterialTypes.EXTRA);
        reinforcedPinkSlime.addTrait(TinkerTraits.unnatural, MaterialTypes.EXTRA);
        reinforcedPinkSlime.addTrait(SLIMEY_PINK, MaterialTypes.HANDLE);
        reinforcedPinkSlime.addTrait(TinkerTraits.unnatural, MaterialTypes.HANDLE);
        TinkerRegistry.addMaterialStats(
                reinforcedPinkSlime,
                new HeadMaterialStats(2800, 7.5f, 5.5f, HarvestLevels.DIAMOND),
                new HandleMaterialStats(3.2f, 2350),
                new ExtraMaterialStats(270),
                new BowMaterialStats(18.5f, 5.3f, 6.0f)
        );
        TinkerRegistry.integrate(reinforcedPinkSlime).preInit();
        materials.put("reinforced_pink_slime", reinforcedPinkSlime);

        FluidMoltenReinforcedPinkSlime.registerFluid();
    }

    public static void init() {
        final Material plastic = materials.get("plastic");
        plastic.addItem(ObjectHelper.getItemByName(ModNames.IF, "plastic"), 1, Material.VALUE_Fragment);
        plastic.setRepresentativeItem(ObjectHelper.getItemByName(ModNames.IF, "plastic"));

        final Material pinkSlime = materials.get("pink_slime");
        pinkSlime.addItem(ObjectHelper.getItemByName(ModNames.IF, "pink_slime"), 1, Material.VALUE_Fragment);
        pinkSlime.setRepresentativeItem(ObjectHelper.getItemByName(ModNames.IF, "pink_slime"));

        final Material reinforcedPinkSlime = materials.get("reinforced_pink_slime");
        reinforcedPinkSlime.addItem(ObjectHelper.getItemByName(ModNames.IF, "pink_slime_ingot"), 1, Material.VALUE_Ingot);
        reinforcedPinkSlime.setRepresentativeItem(ObjectHelper.getItemByName(ModNames.IF, "pink_slime_ingot"));
        reinforcedPinkSlime.setCraftable(false).setCastable(true);
        reinforcedPinkSlime.setFluid(FluidRegistry.getFluid("molten_reinforced_pink_slime"));
        FluidMoltenReinforcedPinkSlime.registerRecipes();
    }

    public static void initClient() {
        FluidMoltenReinforcedPinkSlime.registerRenderers();
    }

    private static class FluidMoltenReinforcedPinkSlime {

        private static final ResourceLocation STILL = new ResourceLocation("tconstruct:blocks/fluids/molten_metal_flow");
        private static final ResourceLocation FLOWING = new ResourceLocation("tconstruct:blocks/fluids/molten_metal");
        private static Fluid moltenPinkSlimeFluid;

        private static void registerFluid() {
            moltenPinkSlimeFluid = new Fluid("molten_reinforced_pink_slime", FLOWING, STILL) {
                @Override
                public int getColor() {
                    return 0xFF000000 | 0xC279B6;
                }
            }.setUnlocalizedName(Reference.MODID + ".molten_reinforced_pink_slime");
            moltenPinkSlimeFluid.setDensity(2000).setLuminosity(10).setTemperature(1000).setViscosity(10000);
            FluidRegistry.registerFluid(moltenPinkSlimeFluid);
            FluidRegistry.addBucketForFluid(moltenPinkSlimeFluid);

            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("fluid", moltenPinkSlimeFluid.getName());
            tag.setString("ore", "ReinforcedPinkSlime");
            tag.setBoolean("toolforge", true);
            FMLInterModComms.sendMessage("tconstruct", "integrateSmeltery", tag);
        }

        @SideOnly(Side.CLIENT)
        private static void registerRenderers() {
            if (moltenPinkSlimeFluid == null) {
                return;
            }
            Block block = moltenPinkSlimeFluid.getBlock();
            if (block == null) {
                return;
            }
            FluidsClientProxy.FluidStateMapper mapper = new FluidsClientProxy.FluidStateMapper(moltenPinkSlimeFluid);
            ModelLoader.setCustomStateMapper(block, mapper);
            Item item = Item.getItemFromBlock(block);
            if (item != Items.AIR) {
                ModelLoader.registerItemVariants(item);
                ModelLoader.setCustomMeshDefinition(item, mapper);
            }
        }

        private static void registerRecipes() {
            Fluid f = FluidRegistry.getFluid("molten_reinforced_pink_slime");
            if (f != null) {
                TinkerRegistry.registerMelting(
                        ObjectHelper.getItemByName(ModNames.IF, "pink_slime_ingot"),
                        f, Material.VALUE_Ingot
                );
                TinkerRegistry.registerTableCasting(
                        ObjectHelper.getItemStackByName(ModNames.IF, "pink_slime_ingot", 1 ,0),
                        ObjectHelper.getItemStackByName(ModNames.TCON, "cast_custom", 1, 0),
                        f, Material.VALUE_Ingot
                );
            }
        }

    }

}
