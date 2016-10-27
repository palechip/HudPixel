package eladkay.modulargui.lib.base;

import eladkay.modulargui.lib.IModularGuiProvider;

public abstract class SimpleModularGuiProvider implements IModularGuiProvider {
    @Override
    public boolean ignoreEmptyCheck() {
        return false;
    }
}
