package net.dungeonz.block.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.dungeonz.block.entity.DungeonPortalEntity;
import net.dungeonz.init.BlockInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DungeonPortalScreenHandler extends ScreenHandler {

    private final World world;
    private final ScreenHandlerContext context;
    private final DungeonPortalEntity dungeonPortalEntity;
    private BlockPos pos;

    // private List<UUID> dungeonPlayerUUIDs = new ArrayList<UUID>();
    private List<String> difficulties = new ArrayList<String>();
    private Map<String, List<ItemStack>> possibleLootDifficultyItemStackMap = new HashMap<String, List<ItemStack>>();
    private List<ItemStack> requiredItemStacks = new ArrayList<ItemStack>();
    // private int maxPlayerCount = 0;
    // private int cooldown = 0;

    public DungeonPortalScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new DungeonPortalEntity(buf.readBlockPos(), playerInventory.player.world.getBlockState(buf.readBlockPos())), ScreenHandlerContext.EMPTY);
        this.pos = buf.readBlockPos();
        int dungeonPlayerCount = buf.readInt();
        List<UUID> dungeonPlayerUUIDs = new ArrayList<UUID>();
        for (int i = 0; i < dungeonPlayerCount; i++) {
            dungeonPlayerUUIDs.add(buf.readUuid());
        }
        int difficultyCount = buf.readInt();
        List<String> difficulties = new ArrayList<String>();
        if (difficultyCount != 0) {
            for (int i = 0; i < difficultyCount; i++) {
                difficulties.add(buf.readString());
            }
        }
        int possibleLootCount = buf.readInt();
        Map<String, List<ItemStack>> possibleLootDifficultyItemStackMap = new HashMap<String, List<ItemStack>>();
        if (possibleLootCount != 0) {
            // possibleLootDifficultyItemStackMap = buf.readMap(PacketByteBuf::readString, PacketByteBuf::readItemStack);
        }
        final Map<String, List<ItemStack>> possibleLootDifficultyItemStacks = possibleLootDifficultyItemStackMap;
        int requiredItemCount = buf.readInt();
        List<ItemStack> requiredItemStacks = new ArrayList<ItemStack>();
        if (requiredItemCount != 0) {
            // requiredItemStacks.add(buf.readItemStack());
        }
        int maxGroupSize = buf.readInt();
        int cooldown = buf.readInt();
        String difficulty = buf.readString();

        this.setDungeonPlayerUUIDs(dungeonPlayerUUIDs);
        this.setDifficulties(difficulties);
        this.setPossibleLootItemStacks(possibleLootDifficultyItemStacks);
        this.setRequiredItemStacks(requiredItemStacks);
        this.setMaxPlayerCount(maxGroupSize);
        this.setCooldown(cooldown);
        this.setDifficulty(difficulty);

        // System.out.println("SET ON CLIENT? " + difficulty + " : " + difficulties);
    }

    public DungeonPortalScreenHandler(int syncId, PlayerInventory playerInventory, DungeonPortalEntity dungeonPortalEntity, ScreenHandlerContext context) {
        super(BlockInit.PORTAL, syncId);
        this.context = context;
        this.world = playerInventory.player.world;
        this.dungeonPortalEntity = dungeonPortalEntity;
        this.pos = dungeonPortalEntity.getPos();
        // System.out.println(this.dungeonPortalEntity);

        // setDungeonPlayerUUIDs(this.dungeonPortalEntity.getDungeonPlayerUUIDs());
        setDifficulties(this.dungeonPortalEntity.getDungeon().getDifficultyList());
        // setPossibleLootItemStacks(possibleLootDifficultyItemStackMap);
        // setRequiredItemStacks(requiredItemStacks);
        // setMaxPlayerCount(this.dungeonPortalEntity.getMaxGroupSize());
        // setCooldown(this.dungeonPortalEntity.getCooldown());

    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        System.out.println(player + " : " + id);
        return true;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity var1, int var2) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.context.get((world, pos) -> {
            if (!this.world.getBlockState(pos).isOf(BlockInit.DUNGEON_PORTAL)) {
                return false;
            }
            return player.squaredDistanceTo((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5) <= 64.0;
        }, true);
    }

    // public List<UUID> getDungeonPlayerUUIDs() {
    // return this.dungeonPlayerUUIDs;
    // }

    // public void setDungeonPlayerUUIDs(List<UUID> dungeonPlayerUUIDs) {
    // this.dungeonPlayerUUIDs = dungeonPlayerUUIDs;
    // }

    // public List<String> getDifficulties() {
    // return this.difficulties;
    // }

    // public void setDifficulties(List<String> difficulties) {
    // this.difficulties = difficulties;
    // }

    // public Map<String, ItemStack> getPossibleLootDifficultyItemStackMap() {
    // return this.possibleLootDifficultyItemStackMap;
    // }

    // public void setPossibleLootItemStacks(Map<String, ItemStack> possibleLootDifficultyItemStackMap) {
    // this.possibleLootDifficultyItemStackMap = possibleLootDifficultyItemStackMap;
    // }

    // public List<ItemStack> getRequiredItemStacks() {
    // return this.requiredItemStacks;
    // }

    // public void setRequiredItemStacks(List<ItemStack> requiredItemStacks) {
    // this.requiredItemStacks = requiredItemStacks;
    // }

    // public int getMaxPlayerCount() {
    // return this.maxPlayerCount;
    // }

    // public void setMaxPlayerCount(int maxPlayerCount) {
    // this.maxPlayerCount = maxPlayerCount;
    // }

    // public int getCooldown() {
    // return this.cooldown;
    // }

    // public void setCooldown(int cooldown) {
    // this.cooldown = cooldown;
    // }

    public List<UUID> getDungeonPlayerUUIDs() {
        return this.dungeonPortalEntity.getDungeonPlayerUUIDs();
    }

    public void setDungeonPlayerUUIDs(List<UUID> dungeonPlayerUUIDs) {
        this.dungeonPortalEntity.setDungeonPlayerUUIDs(dungeonPlayerUUIDs);
    }

    public List<String> getDifficulties() {
        return this.difficulties;
        // return this.dungeonPortalEntity.getDungeon().getDifficultyList();
    }

    public void setDifficulties(List<String> difficulties) {
        this.difficulties = difficulties;
    }

    public Map<String, List<ItemStack>> getPossibleLootDifficultyItemStackMap() {
        return this.possibleLootDifficultyItemStackMap;
    }

    public void setPossibleLootItemStacks(Map<String, List<ItemStack>> possibleLootDifficultyItemStackMap) {
        this.possibleLootDifficultyItemStackMap = possibleLootDifficultyItemStackMap;
    }

    public List<ItemStack> getRequiredItemStacks() {
        return this.requiredItemStacks;
    }

    public void setRequiredItemStacks(List<ItemStack> requiredItemStacks) {
        this.requiredItemStacks = requiredItemStacks;
    }

    public int getMaxPlayerCount() {
        return this.dungeonPortalEntity.getMaxGroupSize();
    }

    public void setMaxPlayerCount(int maxPlayerCount) {
        this.dungeonPortalEntity.setMaxGroupSize(maxPlayerCount);
    }

    public int getCooldown() {
        return this.dungeonPortalEntity.getCooldown();
    }

    public void setCooldown(int cooldown) {
        this.dungeonPortalEntity.setCooldown(cooldown);
    }

    public String getDifficulty() {
        return this.dungeonPortalEntity.getDifficulty();
    }

    public void setDifficulty(String difficulty) {
        this.dungeonPortalEntity.setDifficulty(difficulty);
    }

    public BlockPos getPos() {
        return this.pos;
    }
}
