/*
 * This file is part of Hot or Not.
 *
 * Copyright 2018, Buuz135
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.buuz135.togetherforever.command;

import com.buuz135.togetherforever.api.command.SubCommandAction;
import com.buuz135.togetherforever.api.command.TogetherForeverCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class HelpCommand extends SubCommandAction {

    public HelpCommand() {
        super("help");
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (TogetherForeverCommand.command != null) {
            sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "Together Forever Commands: "));
            for (SubCommandAction action : TogetherForeverCommand.command.getSubCommandActions()) {
                if (!action.getSubCommandName().equals(this.getSubCommandName()))
                    sender.sendMessage(new TextComponentString(TextFormatting.BLUE + " /tofe " + action.getSubCommandName() + ' ' + getUsage() + TextFormatting.GRAY + "- " + TextFormatting.AQUA + action.getInfo()));
            }
        }
        return true;
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getInfo() {
        return "";
    }
}
