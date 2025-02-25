package dev.compactmods.machines.machine;

import com.mojang.serialization.Codec;
import dev.compactmods.machines.api.item.component.MachineComponents;
import dev.compactmods.machines.api.machine.MachineColor;
import dev.compactmods.machines.api.machine.MachineConstants;
import dev.compactmods.machines.api.room.template.RoomTemplate;
import dev.compactmods.machines.CMRegistries;
import dev.compactmods.machines.machine.block.BoundCompactMachineBlock;
import dev.compactmods.machines.machine.block.BoundCompactMachineBlockEntity;
import dev.compactmods.machines.machine.block.UnboundCompactMachineBlock;
import dev.compactmods.machines.machine.block.UnboundCompactMachineEntity;
import dev.compactmods.machines.machine.item.BoundCompactMachineItem;
import dev.compactmods.machines.machine.item.UnboundCompactMachineItem;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.IntTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface Machines {
	BlockBehaviour.Properties MACHINE_BLOCK_PROPS = BlockBehaviour.Properties
		.of()
		.instrument(NoteBlockInstrument.COW_BELL)
		.pushReaction(PushReaction.IGNORE)
		.sound(SoundType.METAL)
		.strength(8.0F, 20.0F)
		.requiresCorrectToolForDrops();

	Supplier<Item.Properties> MACHINE_ITEM_PROPS = Item.Properties::new;

	interface Blocks {
		DeferredBlock<UnboundCompactMachineBlock> UNBOUND_MACHINE = CMRegistries.BLOCKS.register("new_machine", () ->
			new UnboundCompactMachineBlock(MACHINE_BLOCK_PROPS));

		DeferredBlock<BoundCompactMachineBlock> BOUND_MACHINE = CMRegistries.BLOCKS.register("machine", () ->
			new BoundCompactMachineBlock(MACHINE_BLOCK_PROPS));

		static void prepare() {
		}
	}

	interface Items {
		DeferredItem<BoundCompactMachineItem> BOUND_MACHINE = CMRegistries.ITEMS.register("machine",
			() -> new BoundCompactMachineItem(MACHINE_ITEM_PROPS.get()));

		DeferredItem<UnboundCompactMachineItem> UNBOUND_MACHINE = CMRegistries.ITEMS.register("new_machine",
			() -> new UnboundCompactMachineItem(MACHINE_ITEM_PROPS.get()));

		static void prepare() {
		}

		static ItemStack unbound() {
			return unboundColored(0xFFFFFFFF);
		}

		static ItemStack unboundColored(int color) {
			final var stack = UNBOUND_MACHINE.toStack();
			stack.set(Machines.DataComponents.MACHINE_COLOR, MachineColor.fromARGB(color));
			return stack;
		}

		static ItemStack boundToRoom(String roomCode) {
			return boundToRoom(roomCode, 0xFFFFFFFF);
		}

		static ItemStack boundToRoom(String roomCode, int color) {
			return boundToRoom(roomCode, MachineColor.fromARGB(color));
		}

		static ItemStack boundToRoom(String roomCode, MachineColor color) {
			ItemStack stack = BOUND_MACHINE.toStack();
			stack.set(Machines.DataComponents.BOUND_ROOM_CODE, roomCode);
			stack.set(Machines.DataComponents.MACHINE_COLOR, color);
			return stack;
		}

		static ItemStack forNewRoom(Holder.Reference<RoomTemplate> templateHolder) {
			var template = templateHolder.value();

			final var stack = UNBOUND_MACHINE.toStack();
			stack.set(Machines.DataComponents.ROOM_TEMPLATE_ID, templateHolder.key().location());
			stack.set(Machines.DataComponents.MACHINE_COLOR, template.defaultMachineColor());
			return stack;
		}
	}

	interface BlockEntities {

		DeferredHolder<BlockEntityType<?>, BlockEntityType<UnboundCompactMachineEntity>> UNBOUND_MACHINE = CMRegistries.BLOCK_ENTITIES.register(MachineConstants.UNBOUND_MACHINE_ENTITY.getPath(), () ->
			BlockEntityType.Builder.of(UnboundCompactMachineEntity::new, Blocks.UNBOUND_MACHINE.get())
				.build(null));

		DeferredHolder<BlockEntityType<?>, BlockEntityType<BoundCompactMachineBlockEntity>> MACHINE = CMRegistries.BLOCK_ENTITIES.register(MachineConstants.BOUND_MACHINE_ENTITY.getPath(), () ->
			BlockEntityType.Builder.of(BoundCompactMachineBlockEntity::new, Blocks.BOUND_MACHINE.get())
				.build(null));

		static void prepare() {
		}
	}

	interface DataComponents {
		String KEY_ROOM_TEMPLATE = "room_template";
		String KEY_ROOM_CODE = "room_code";
		String KEY_MACHINE_COLOR = "machine_color";

		DeferredHolder<DataComponentType<?>, DataComponentType<String>> BOUND_ROOM_CODE = CMRegistries.DATA_COMPONENTS
			.registerComponentType(KEY_ROOM_CODE, MachineComponents.BOUND_ROOM_CODE);

		DeferredHolder<DataComponentType<?>, DataComponentType<MachineColor>> MACHINE_COLOR = CMRegistries.DATA_COMPONENTS
			.registerComponentType(KEY_MACHINE_COLOR, MachineComponents.MACHINE_COLOR);

		DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> ROOM_TEMPLATE_ID = CMRegistries.DATA_COMPONENTS
			.registerComponentType(KEY_ROOM_TEMPLATE, MachineComponents.ROOM_TEMPLATE_ID);

		static void prepare() {
		}
	}

	interface Attachments {
		Supplier<AttachmentType<MachineColor>> MACHINE_COLOR = CMRegistries.ATTACHMENT_TYPES.register("machine_color", () -> AttachmentType
			.builder(() -> MachineColor.fromARGB(CommonColors.WHITE))
			.serialize(MachineColor.CODEC)
			.build());

		static void prepare() {
		}
	}

	static void prepare() {
		Blocks.prepare();
		Items.prepare();
		BlockEntities.prepare();
		DataComponents.prepare();
		Attachments.prepare();
	}

	static void registerEvents(IEventBus modBus) {

	}
}
