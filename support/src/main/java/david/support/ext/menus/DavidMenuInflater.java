package david.support.ext.menus;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import android.view.ViewGroup;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import david.support.ext.R;
import david.support.ext.debug.Logger;

/**
 * Created by chendingwei on 16/11/12.
 */

public class DavidMenuInflater {

    private static final Logger LOG = new Logger(MenuState.class);

    /** Menu tag name in XML. */
    private static final String XML_MENU = "menu";

    private static final int NO_ID = 0;

    /** Item tag name in XML. */
    private static final String XML_ITEM = "item";
    private Context mContext;
    /**
     <item name="menu_title_size">16dp</item>
     <item name="menu_title_color">@android:color/white</item>
     <item name="menu_message_size">14dp</item>
     <item name="menu_message_color">#999999</item>
     <item name="menu_background_color">#2f2f2f</item>

     <item name="menu_item_width">280dp</item>
     <item name="menu_item_height">55dp</item>
     <item name="menu_divider_color">#2e2e2e</item>
     <item name="menu_divider_height">1px</item>
     <item name="menu_paddingLeft">20dp</item>

     <item name="menu_paddingRight">20dp</item>
     * */
     int mDefaultBackgroundId;
     int mDefaultIconWidth;
     int mDefaultIconHeight;

     int mDefaultBackgroundColor;
     int mDefaultTitleSize;
     int mDefaultPaddingLeft;
     int mDefaultPaddingRight;
     int mDefaultPaddingTop;

     int mDefaultPaddingBottom;
     int mDefaultDividerColor;
     int mDefaultDividerHeight;
     int mDefaultTitleColor;
     int mDefaultMessageSize;

     int mDefaultMessageColor;
     int mDefaultItemWidth;
     int mDefaultItemHeight;


    /**
     * Constructs a menu inflater.
     */
    public DavidMenuInflater(Context context) {
        this(context,R.style.DavidMenusTheme_Defualt);
    }

    /**
     * Constructs a menu inflater.
     */
    public DavidMenuInflater(Context context,int theme) {
        mContext = context;
        TypedArray array = context.obtainStyledAttributes(theme,R.styleable.MenuStyleableAttrs);
        mDefaultBackgroundId = array.getResourceId(R.styleable.MenuStyleableAttrs_menu_background,-1);
        mDefaultIconWidth = array.getResourceId(R.styleable.MenuStyleableAttrs_menu_icon_width, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDefaultIconHeight = array.getResourceId(R.styleable.MenuStyleableAttrs_menu_icon_height,ViewGroup.LayoutParams.WRAP_CONTENT);

        mDefaultBackgroundColor = array.getColor(R.styleable.MenuStyleableAttrs_menu_background_color, Color.BLACK);
        mDefaultTitleSize = (int)array.getDimension(R.styleable.MenuStyleableAttrs_menu_title_size,10);
        mDefaultPaddingLeft = (int)array.getDimension(R.styleable.MenuStyleableAttrs_menu_paddingLeft,0);
        mDefaultPaddingRight = (int)array.getDimension(R.styleable.MenuStyleableAttrs_menu_paddingRight,0);
        mDefaultPaddingTop = (int)array.getDimension(R.styleable.MenuStyleableAttrs_menu_paddingTop,0);

        mDefaultPaddingBottom = (int)array.getDimension(R.styleable.MenuStyleableAttrs_menu_paddingBottom,0);
        mDefaultDividerColor = array.getColor(R.styleable.MenuStyleableAttrs_menu_divider_color, Color.WHITE);
        mDefaultDividerHeight = (int)array.getDimension(R.styleable.MenuStyleableAttrs_menu_divider_height, 1);
        mDefaultTitleColor = array.getColor(R.styleable.MenuStyleableAttrs_menu_title_color, Color.WHITE);
        mDefaultMessageColor = array.getColor(R.styleable.MenuStyleableAttrs_menu_message_color, Color.WHITE);

        mDefaultMessageSize = (int)array.getDimension(R.styleable.MenuStyleableAttrs_menu_message_size, 15);
        mDefaultItemWidth = (int)array.getDimension(R.styleable.MenuStyleableAttrs_menu_item_width, 100);
        mDefaultItemHeight = (int)array.getDimension(R.styleable.MenuStyleableAttrs_menu_item_height, 50);

        array.recycle();
    }

    /**
     * Inflate a menu hierarchy from the specified XML resource. Throws
     * {@link InflateException} if there is an error.
     *
     * @param menuRes Resource ID for an XML layout resource to load (e.g.,
     *            <code>R.menu.main_activity</code>)
     * @param menu The Menu to inflate into. The items and submenus will be
     *            added to this Menu.
     */
    public void inflate(int menuRes, DavidMenu menu) {
        XmlResourceParser parser = null;
        try {
            parser = mContext.getResources().getLayout(menuRes);
            AttributeSet attrs = Xml.asAttributeSet(parser);

            parseMenu(parser, attrs, menu,null);
        } catch (XmlPullParserException e) {
            throw new InflateException("Error inflating menu XML", e);
        } catch (IOException e) {
            throw new InflateException("Error inflating menu XML", e);
        } finally {
            if (parser != null) parser.close();
        }
    }

    /**
     * Called internally to fill the given menu. If a sub menu is seen, it will
     * call this recursively.
     */
    private void parseMenu(XmlPullParser parser, AttributeSet attrs, DavidMenu menu,DavidMenu parent)
            throws XmlPullParserException, IOException {
        MenuState menuState = new MenuState(menu);

        int eventType = parser.getEventType();
        String tagName;
        boolean lookingForEndOfUnknownTag = false;
        String unknownTagName = null;

        // This loop will skip to the menu start tag
        do {
            if (eventType == XmlPullParser.START_TAG) {
                tagName = parser.getName();
                if (tagName.equals(XML_MENU)) {
                    menu.readAttr(mContext,attrs,parent,this);
                    eventType = parser.next();
                    break;
                }

                throw new RuntimeException("Expecting menu, got " + tagName);
            }
            eventType = parser.next();
        } while (eventType != XmlPullParser.END_DOCUMENT);

        boolean reachedEndOfMenu = false;
        while (!reachedEndOfMenu) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (lookingForEndOfUnknownTag) {
                        break;
                    }

                    tagName = parser.getName();
                    if (tagName.equals(XML_ITEM)) {
                        menuState.readItem(attrs);
                    } else if (tagName.equals(XML_MENU)) {
                        DavidMenu subMenu = menuState.addSubMenu();
                        parseMenu(parser, attrs, subMenu,menu);
                    } else {
                        lookingForEndOfUnknownTag = true;
                        unknownTagName = tagName;
                    }
                    break;

                case XmlPullParser.END_TAG:
                    tagName = parser.getName();
                    if (lookingForEndOfUnknownTag && tagName.equals(unknownTagName)) {
                        lookingForEndOfUnknownTag = false;
                        unknownTagName = null;
                    } else if (tagName.equals(XML_ITEM)) {
                        if (!menuState.hasAddedItem()) {
                            menuState.addItem();
                        }
                    } else if (tagName.equals(XML_MENU)) {
                        reachedEndOfMenu = true;
                    }
                    break;

                case XmlPullParser.END_DOCUMENT:
                    throw new RuntimeException("Unexpected end of document");
            }

            eventType = parser.next();
        }
    }

    /**
     * State for the current menu.
     * <p>
     * Groups can not be nested unless there is another menu (which will have
     * its state class).
     */
    private class MenuState {
        private DavidMenu menu;
        private boolean itemAdded;
        private int itemId;
        private CharSequence itemTitle;
        private int itemIconResId;
        private CharSequence itemMessage;
        private int itemLeftStubResId;
        private int itemRightStubResId;

        public MenuState(DavidMenu menu) {
            this.menu = menu;
        }

        /**
         * Called when the parser is pointing to an item tag.
         *
         *
         * <attr name="item_id" format="integer|reference"/>
         <attr name="item_icon" format="reference"/>
         <attr name="item_title" format="string|reference"/>
         <attr name="item_message" format="string|reference"/>
         <attr name="item_leftstub" format="reference"/>
         <attr name="item_rightstub" format="reference"/>
         */
        public void readItem(AttributeSet attrs) {
            TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.MenuItemStyleableAttrs);
            itemId = a.getResourceId(R.styleable.MenuItemStyleableAttrs_item_id, 0);
            itemTitle = a.getString(R.styleable.MenuItemStyleableAttrs_item_title);
            itemIconResId = a.getResourceId(R.styleable.MenuItemStyleableAttrs_item_icon, -1);
            itemMessage  = a.getString(R.styleable.MenuItemStyleableAttrs_item_message);
            itemLeftStubResId = a.getResourceId(R.styleable.MenuItemStyleableAttrs_item_leftstub, -1);
            itemRightStubResId = a.getResourceId(R.styleable.MenuItemStyleableAttrs_item_rightstub, -1);
            a.recycle();
            itemAdded = false;
        }

        private void setItem(DavidMenu.DavidMenuItem item) {
                item.setIcon(itemIconResId);

        }

        public void addItem() {
            itemAdded = true;
            setItem(menu.addItem(itemId, itemTitle,itemIconResId,itemMessage,itemLeftStubResId,itemRightStubResId));
        }

        public DavidMenu addSubMenu() {
            itemAdded = true;
            DavidMenu subMenu = menu.addSubMenus(itemId, itemTitle,itemIconResId,itemMessage,itemLeftStubResId,itemRightStubResId);
            setItem(subMenu.getParent());
            return subMenu;
        }

        public boolean hasAddedItem() {
            return itemAdded;
        }
    }

}
