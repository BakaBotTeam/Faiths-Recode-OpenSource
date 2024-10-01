package dev.faiths.command.impl;

import dev.faiths.Faiths;
import dev.faiths.command.AbstractCommand;
import dev.faiths.module.CheatModule;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BindCommand extends AbstractCommand {
    public BindCommand() {
        super("bind");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length > 2) {
            // Get module by name
            String moduleName = args[1];
            CheatModule module = Faiths.moduleManager.getModule(moduleName);

            if (module == null) {
                chat("Module §a§l" + moduleName + "§3 not found.");
                return;
            }
            // Find key by name and change
            int key = Keyboard.getKeyIndex(args[2].toUpperCase());
            module.setKeyBind(key);

            // Response to user
            chat("Bound module §d" + module.getName() + "§c to key §d" + Keyboard.getKeyName(key) + "§c.");
            playEdit();
            return;
        }

        chatSyntax(new String[]{"<module> <key>", "<module> none"});
    }

    @Override
    public List<String> tabComplete(final String[] args) {
        if (args.length == 0) return new ArrayList<>();
        String moduleName = args[1];
        if (args.length == 2) {
            return Faiths.moduleManager.getModules().stream()
                    .map(CheatModule::getName)
                    .filter(name -> name.toLowerCase().startsWith(moduleName.toLowerCase())).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
//        return new ArrayList<>();
    }
}