package eladkay.modulargui.lib.base;

import eladkay.modulargui.lib.IModularGuiProvider;

/**
 * This class is meant to show a correct implementation of IModularGuiProvider.
 * This is an abstract implementation: it can be used in more than one element and still, possibly
 * have different content.
 *
 * @author Eladkay
 * @since 1.6
 */
public class StringAbstractModularGuiProvider implements IModularGuiProvider {

    //the content to display.
    private String content;

    /**
     * This is the default constructor.
     *
     * @param s the content to display
     */
    public StringAbstractModularGuiProvider(String s) {
        content = s;
    }

    @Override
    public boolean showElement() {
        return true;
    } //elements using this provider will always be shown.

    @Override
    public String content() {
        return content;
    } //display the content

    @Override
    public boolean ignoreEmptyCheck() {
        return false;
    }

    @Override
    public String getAfterstats() {
        return null;
    }

}

