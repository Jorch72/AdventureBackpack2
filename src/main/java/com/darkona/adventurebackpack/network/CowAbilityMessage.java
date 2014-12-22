package com.darkona.adventurebackpack.network;

import com.darkona.adventurebackpack.common.IAdvBackpack;
import com.darkona.adventurebackpack.inventory.BackpackContainer;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;

import java.util.UUID;

/**
 * Created on 16/10/2014
 *
 * @author Darkona
 */
public class CowAbilityMessage implements IMessage
{

    private byte action;
    private String playerID;
    public static final byte CONSUME_WHEAT = 0;

    public CowAbilityMessage()
    {
    }

    public CowAbilityMessage(String playerID, byte action)
    {
        this.playerID = playerID;
        this.action = action;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {

        playerID = ByteBufUtils.readUTF8String(buf);
        action = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, playerID);
        buf.writeByte(action);
    }

    public static class CowAbilityMessageServerHandler implements IMessageHandler<CowAbilityMessage, CowAbilityMessage>
    {

        @Override
        public CowAbilityMessage onMessage(CowAbilityMessage message, MessageContext ctx)
        {

            return null;
        }


    }

    public static class CowAbilityMessageClientHandler implements IMessageHandler<CowAbilityMessage, CowAbilityMessage>
    {

        @Override
        public CowAbilityMessage onMessage(CowAbilityMessage message, MessageContext ctx)
        {
            EntityPlayer player = Minecraft.getMinecraft().theWorld.func_152378_a(UUID.fromString(message.playerID));

            if(player.openContainer instanceof BackpackContainer)
            {
                BackpackContainer cont = ((BackpackContainer)player.openContainer);
                cont.detectAndSendChanges();
                IAdvBackpack inv = cont.inventory;
                switch(message.action)
                {
                    case CONSUME_WHEAT: inv.consumeInventoryItem(Items.wheat);
                }
                player.inventoryContainer.detectAndSendChanges();
                inv.onInventoryChanged();
                inv.saveChanges();
            }

            return null;
        }
    }
}