package eladkay.modulargui.lib;

import com.google.common.collect.Lists;
import eladkay.modulargui.lib.base.EmptyModularGuiProvider;
import eladkay.modulargui.lib.base.NameModularGuiProvider;
import eladkay.modulargui.lib.base.StringAbstractModularGuiProvider;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * This class is meant to serve as a registry to the Modular GUI Lib.
 *
 * @author Eladkay
 * @since 1.6
 */
public class ModularGuiRegistry {

    //Should register example templates?
    public static boolean shouldRegisterExampleElements = false;

    //List of all templates in the Modular GUI
    public static ArrayList<Element> allElements = Lists.newArrayList();
    /**
     * Example templates.
     * You should keep a constant of your templates in some sort of registry class.
     */
    public static final Element TITLE = new Element("", new StringAbstractModularGuiProvider(TextFormatting.AQUA + "Modular" + TextFormatting.GOLD + "GUI"));
    public static final Element NAME = new Element("IGN", new NameModularGuiProvider());
    public static final Element GROUPER = new Element("", new EmptyModularGuiProvider(), true);

    /**
     * Register an element to the modular GUI.
     *
     * @param element The element to register
     */
    public static void registerElement(Element element) {
        if (!allElements.contains(element) || element.allowsDuplicates) {
            allElements.add(element);
            MinecraftForge.EVENT_BUS.register(element.provider);
        } else Logger.getLogger("modulargui").warning("Tried to register element " + element.name +
                " and it was already registered!");
    }

    static {

        if (shouldRegisterExampleElements) { //if it should register example templates...
            //register the example templates
            registerElement(TITLE);
            registerElement(GROUPER);
            registerElement(GROUPER);
            registerElement(NAME);
        }
    }

    /**
     * Instances of this class represent seperate lines in the Modular GUI HUD.
     * This class is immutable.
     *
     * @author Eladkay
     * @since 1.6
     */
    public static class Element {


        @Override
        public String toString() {
            return "Element{" +
                    "nm='" + name + '\'' +
                    ", provider=" + provider +
                    '}';
          }

        /**
         * Default constructor.
         *
         * @param name     The nm to be displayed before the content of the element in the modular GUI HUD
         * @param provider The IModularGuiProvider that provides the content for the element
         */
        public Element(String name, IModularGuiProvider provider) {
            this.name = name;
            this.provider = provider;
            this.allowsDuplicates = false;
        }

        /**
         * Should allow duplicate templates of this type?
         *
         * @param name           The nm to be displayed before the content of the element in the modular GUI HUD
         * @param provider       The IModularGuiProvider that provides the content for the element
         * @param allowDuplicate should duplicate templates of this type be allowed?
         */
        public Element(String name, IModularGuiProvider provider, boolean allowDuplicate) {
            this.name = name;
            this.provider = provider;
            this.allowsDuplicates = allowDuplicate;
        }

        //the nm of the element
        public final String name;
        //the provider of the element
        public final IModularGuiProvider provider;
        //should this templates be allowed to be registered more than once?
        public final boolean allowsDuplicates;
    }
}
