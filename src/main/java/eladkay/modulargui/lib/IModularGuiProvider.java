package eladkay.modulargui.lib;

/**
 * Classes implementing this interface will be able to serve as providers for templates of the Modular GUI
 *
 * @author Eladkay
 * @since 1.6
 */
public interface IModularGuiProvider {
    boolean showElement(); //should this element be shown?

    String content(); //content of the element

    boolean ignoreEmptyCheck(); //return true if this element should be displayed even if the content is empty

    String getAfterstats();
}
