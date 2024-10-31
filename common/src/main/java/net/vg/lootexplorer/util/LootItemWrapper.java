//package net.vg.lootexplorer.util;
//
//import net.minecraft.core.Holder;
//import net.minecraft.core.HolderSet;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.world.item.*;
//import net.minecraft.world.item.enchantment.Enchantment;
//import net.minecraft.world.item.enchantment.EnchantmentInstance;
//import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
//
//import java.util.*;
//
//public class LootItemWrapper {
//    private ItemStack stack;
//    private TreeSet<EnchantmentInstance> enchantments;
//
//    private static TreeSet<EnchantmentInstance> createEnchSet() {
//        return new TreeSet<>((a, b) -> {
//            if (a.enchantment != b.enchantment) {
//                return BuiltInRegistries.ENCHANTMENT.getId(a.enchantment).compareTo(BuiltInRegistries.ENCHANTMENT.getId(b.enchantment));
//            } else {
//                return Integer.compare(a.level, b.level);
//            }
//        });
//    }
//
//    private static final TreeSet<EnchantmentInstance> allDiscoverableEnchants = createEnchSet();
//
//    static {
//        for (Enchantment ench : BuiltInRegistries.ENCHANTMENT) {
//            if (ench.isDiscoverable()) {
//                for (int lvl = ench.getMinLevel(); lvl <= ench.getMaxLevel(); lvl++) {
//                    allDiscoverableEnchants.add(new EnchantmentInstance(ench, lvl));
//                }
//            }
//        }
//    }
//
//    public LootItemWrapper(Item item) {
//        this.stack = new ItemStack(item);
//        this.enchantments = createEnchSet();
//    }
//
//    public void enchantWithLevels(NumberProvider levels, boolean treasure) {
//        IntRange lvls = resolveNumber(levels);
//        int minPoints = lvls.start + 1;
//        int maxPoints = lvls.endInclusive + 1 + (stack.getItem().getEnchantmentValue() / 4) * 2;
//        minPoints = Math.round(minPoints * 0.85f);
//        maxPoints = Math.round(maxPoints * 1.15f);
//        minPoints = Math.max(minPoints, 1);
//        maxPoints = Math.max(maxPoints, 1);
//
//        for (Enchantment ench : BuiltInRegistries.ENCHANTMENT) {
//            if ((ench.isTreasureOnly() && !treasure)
//                    || !ench.isDiscoverable()
//                    || (!stack.is(Items.BOOK) && (!ench.canEnchant(stack) || !ench.canApplyAtEnchantingTable(stack)))) {
//                continue;
//            }
//            for (int lvl = ench.getMinLevel(); lvl <= ench.getMaxLevel(); lvl++) {
//                if (ench.getMaxCost(lvl) < minPoints || ench.getMinCost(lvl) > maxPoints) {
//                    continue;
//                }
//                enchantments.add(new EnchantmentInstance(ench, lvl));
//            }
//        }
//        tryEnchantedBook();
//        // ReltUtils.wrapHoverName(stack);
//    }
//
//    public void enchantRandom(HolderSet<Enchantment> enchants) {
//        tryEnchantedBook();
//        for (Holder<Enchantment> enchHolder : enchants) {
//            Enchantment ench = enchHolder.value();
//            for (int lvl = ench.getMinLevel(); lvl <= ench.getMaxLevel(); lvl++) {
//                enchantments.add(new EnchantmentInstance(ench, lvl));
//            }
//        }
//    }
//
//    public void enchantRandom() {
//        tryEnchantedBook();
//        enchantments = new TreeSet<>(allDiscoverableEnchants);
//    }
//
//    private void tryEnchantedBook() {
//        if (stack.getItem() instanceof BookItem) {
//            ItemStack newStack = new ItemStack(Items.ENCHANTED_BOOK);
//            newStack.setTag(stack.getTag());
//            stack = newStack;
//        }
//    }
//
//    public void writeMap() {
//        if (stack.getItem() instanceof MapItem) {
//            ItemStack newStack = new ItemStack(Items.FILLED_MAP);
//            newStack.setTag(stack.getTag());
//            stack = newStack;
//        }
//        ReltUtils.wrapHoverName(stack);
//    }
//
//    public Collection<ItemStack> genStacks() {
//        if (enchantments.isEmpty()) {
//            return Collections.singletonList(stack); // TODO: cache these?
//        }
//        List<ItemStack> res = new ArrayList<>();
//        for (EnchantmentInstance pair : enchantments) {
//            if (stack.getItem() != Items.ENCHANTED_BOOK && (!pair.enchantment.canEnchant(stack) || !pair.enchantment.canApplyAtEnchantingTable(stack))) {
//                continue;
//            }
//            ItemStack newStack = stack.copy();
//            newStack.enchant(pair.enchantment, pair.level);
//            res.add(newStack);
//        }
//        return res;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        LootItemWrapper that = (LootItemWrapper) o;
//        if (!ItemStack.isSameItemSameTags(stack, that.stack)) return false;
//        if (enchantments.equals(that.enchantments)) return true;
//        if (enchantments.size() != that.enchantments.size()) return false;
//        Iterator<EnchantmentInstance> i1 = enchantments.iterator();
//        Iterator<EnchantmentInstance> i2 = that.enchantments.iterator();
//        while (i1.hasNext()) {
//            if (!i1.next().equals(i2.next())) return false;
//        }
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        return stack.getItem().hashCode();
//    }
//
//    // You'll need to implement this method
//    private IntRange resolveNumber(NumberProvider provider) {
//        // Implementation depends on how NumberProvider works
//        throw new UnsupportedOperationException("Not implemented");
//    }
//
//    // Inner class to represent IntRange
//    private static class IntRange {
//        final int start;
//        final int endInclusive;
//
//        IntRange(int start, int endInclusive) {
//            this.start = start;
//            this.endInclusive = endInclusive;
//        }
//    }
//}