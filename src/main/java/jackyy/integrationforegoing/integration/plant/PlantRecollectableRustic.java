package jackyy.integrationforegoing.integration.plant;

import com.buuz135.industrial.api.plant.PlantRecollectable;
import jackyy.gunpowderlib.helper.ObjectHelper;
import jackyy.integrationforegoing.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collections;
import java.util.List;

public class PlantRecollectableRustic {

    @SubscribeEvent
    public void register(RegistryEvent.Register<PlantRecollectable> event) {
        IForgeRegistry<PlantRecollectable> registry = event.getRegistry();
        registry.register(new PlantRecollectable("rustic_wildberry_bush") {
            @Override
            public boolean canBeHarvested(World world, BlockPos pos, IBlockState blockState) {
                return blockState.getBlock().getMetaFromState(blockState) == 1
                        && blockState.getBlock().getRegistryName().getNamespace().equals(ModNames.RUSTIC)
                        && blockState.getBlock().getRegistryName().getPath().equals("wildberry_bush");
            }

            @Override
            public List<ItemStack> doHarvestOperation(World world, BlockPos pos, IBlockState blockState) {
                NonNullList<ItemStack> stacks = NonNullList.create();
                stacks.add(0, ObjectHelper.getItemStackByName(ModNames.RUSTIC, "wildberries", 1, 0));
                world.setBlockState(pos, blockState.withProperty(ModProps.RUSTIC_WILDBERRIES, false));
                return stacks;
            }

            @Override
            public boolean shouldCheckNextPlant(World world, BlockPos pos, IBlockState blockState) {
                return true;
            }

            @Override
            public List<String> getRecollectablesNames() {
                return Collections.singletonList(Reference.PLANT_TEXT + "rustic_wildberry_bush");
            }
        });
        registry.register(new PlantRecollectable("rustic_herbs") {
            @Override
            public boolean canBeHarvested(World world, BlockPos pos, IBlockState blockState) {
                String path = blockState.getBlock().getRegistryName().getPath();
                return blockState.getBlock().getMetaFromState(blockState) == 3
                        && blockState.getBlock().getRegistryName().getNamespace().equals(ModNames.RUSTIC)
                        && (path.equals("aloe_vera")
                        || path.equals("blood_orchid")
                        || path.equals("chamomile")
                        || path.equals("cohosh")
                        || path.equals("deathstalk_mushroom")
                        || path.equals("horsetail")
                        || path.equals("mooncap_mushroom")
                        || path.equals("wind_thistle")
                        || path.equals("cloudsbluff")
                        || path.equals("core_root")
                        || path.equals("ginseng")
                        || path.equals("marsh_mallow"));
            }

            @Override @SuppressWarnings("deprecation")
            public List<ItemStack> doHarvestOperation(World world, BlockPos pos, IBlockState blockState) {
                List<ItemStack> stacks = blockState.getBlock().getDrops(world, pos, blockState, 0);
                if (ModConfig.misc.rustic.rusticHerbsReplant) {
                    world.setBlockToAir(pos);
                } else {
                    world.setBlockState(pos, blockState.withProperty(ModUtils.getGenericGrowthAge(3), 0));
                }
                return stacks;
            }

            @Override
            public boolean shouldCheckNextPlant(World world, BlockPos pos, IBlockState blockState) {
                return true;
            }

            @Override
            public List<String> getRecollectablesNames() {
                return ModConfig.misc.rustic.rusticHerbsReplant ? Collections.singletonList(Reference.PLANT_TEXT + "rustic_herbs_replant") : Collections.singletonList(Reference.PLANT_TEXT + "rustic_herbs_no_replant");
            }
        });
        registry.register(new PlantRecollectable("rustic_crops") {
            @Override
            public boolean canBeHarvested(World world, BlockPos pos, IBlockState blockState) {
                return (blockState.getBlock().getMetaFromState(blockState) == 3
                        && blockState.getBlock().getRegistryName().getNamespace().equals(ModNames.RUSTIC)
                        && blockState.getBlock().getRegistryName().getPath().equals("tomato_crop"))
                        || (blockState.getBlock().getMetaFromState(blockState) == 3
                        && blockState.getBlock().getRegistryName().getNamespace().equals(ModNames.RUSTIC)
                        && blockState.getBlock().getRegistryName().getPath().equals("chili_crop"))
                        || (world.getBlockState(pos.up()).getBlock().getMetaFromState(world.getBlockState(pos.up())) == 3
                        && world.getBlockState(pos.up()).getBlock().getRegistryName().getNamespace().equals(ModNames.RUSTIC)
                        && world.getBlockState(pos.up()).getBlock().getRegistryName().getPath().equals("tomato_crop"))
                        || (world.getBlockState(pos.up()).getBlock().getMetaFromState(world.getBlockState(pos.up())) == 3
                        && world.getBlockState(pos.up()).getBlock().getRegistryName().getNamespace().equals(ModNames.RUSTIC)
                        && world.getBlockState(pos.up()).getBlock().getRegistryName().getPath().equals("chili_crop"))
                        || (world.getBlockState(pos.up(2)).getBlock().getMetaFromState(world.getBlockState(pos.up(2))) == 3
                        && world.getBlockState(pos.up(2)).getBlock().getRegistryName().getNamespace().equals(ModNames.RUSTIC)
                        && world.getBlockState(pos.up(2)).getBlock().getRegistryName().getPath().equals("tomato_crop"));
            }

            @Override
            public List<ItemStack> doHarvestOperation(World world, BlockPos pos, IBlockState blockState) {
                NonNullList<ItemStack> stacks = NonNullList.create();
                if (blockState.getBlock().getMetaFromState(blockState) == 3) {
                    switch (blockState.getBlock().getRegistryName().getPath()) {
                        case "tomato_crop":
                            stacks.add(0, ObjectHelper.getItemStackByName(ModNames.RUSTIC, "tomato", 1, 0));
                            world.setBlockState(pos, blockState.withProperty(ModUtils.getGenericGrowthAge(3), 2));
                            break;
                        case "chili_crop":
                            stacks.add(0, ObjectHelper.getItemStackByName(ModNames.RUSTIC, "chili_pepper", 1, 0));
                            world.setBlockState(pos, blockState.withProperty(ModUtils.getGenericGrowthAge(3), 2));
                            break;
                    }
                }
                if (world.getBlockState(pos.up()).getBlock().getMetaFromState(world.getBlockState(pos.up())) == 3) {
                    switch (world.getBlockState(pos.up()).getBlock().getRegistryName().getPath()) {
                        case "tomato_crop":
                            stacks.add(0, ObjectHelper.getItemStackByName(ModNames.RUSTIC, "tomato", 1, 0));
                            world.setBlockState(pos.up(), world.getBlockState(pos.up()).withProperty(ModUtils.getGenericGrowthAge(3), 2));
                            break;
                        case "chili_crop":
                            stacks.add(0, ObjectHelper.getItemStackByName(ModNames.RUSTIC, "chili_pepper", 1, 0));
                            world.setBlockState(pos.up(), world.getBlockState(pos.up()).withProperty(ModUtils.getGenericGrowthAge(3), 2));
                            break;
                    }
                }
                if (world.getBlockState(pos.up(2)).getBlock().getMetaFromState(world.getBlockState(pos.up(2))) == 3) {
                    if (world.getBlockState(pos.up(2)).getBlock().getRegistryName().getPath().equals("tomato_crop")) {
                        stacks.add(0, ObjectHelper.getItemStackByName(ModNames.RUSTIC, "tomato", 1, 0));
                        world.setBlockState(pos.up(2), world.getBlockState(pos.up(2)).withProperty(ModUtils.getGenericGrowthAge(3), 2));
                    }
                }
                return stacks;
            }

            @Override
            public boolean shouldCheckNextPlant(World world, BlockPos pos, IBlockState blockState) {
                return true;
            }

            @Override
            public List<String> getRecollectablesNames() {
                return Collections.singletonList(Reference.PLANT_TEXT + "rustic_crops");
            }
        });
    }

}
