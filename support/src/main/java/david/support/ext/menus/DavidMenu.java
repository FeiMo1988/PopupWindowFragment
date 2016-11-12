package david.support.ext.menus;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import david.support.ext.R;
import david.support.ext.debug.Logger;

/**
 * Created by chendingwei on 16/11/12.
 */

public class DavidMenu {

    private List<DavidMenuItem> mItems = new ArrayList<>();
    private DavidMenuItem mParent;
    private int mBackgroundId = -1;
    private int mBackgroundColor = -1;
    private int mTitleSize = 20;
    private int mTitleColor = Color.WHITE;
    private int mMessageColor = Color.WHITE;
    private int mMessageSize = 10;
    private int mIconWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mIconHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;
    private int mItemHeight;
    private int mItemWidth;
    private int mDividerColor ;
    private View mView;
    private int mDividerHeight ;
    private static final Logger  LOG = new Logger(DavidMenu.class);


    public void setView(View view) {
        this.mView = view;
    }

    public View getView() {
        return this.mView;
    }

    public void readAttr(Context context,AttributeSet attrs, DavidMenu parent,DavidMenuInflater inflater) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MenuStyleableAttrs);

        this.mBackgroundId = a.getResourceId(R.styleable.MenuStyleableAttrs_menu_background, -1);
        this.mBackgroundColor = a.getResourceId(R.styleable.MenuStyleableAttrs_menu_background_color, -1);
        this.mTitleSize = (int)a.getDimension(R.styleable.MenuStyleableAttrs_menu_title_size,-1);
        this.mIconWidth =(int)a.getDimension(R.styleable.MenuStyleableAttrs_menu_icon_width, -1);
        this.mIconHeight = (int)a.getDimension(R.styleable.MenuStyleableAttrs_menu_icon_height, -1);

        this.mMessageSize = (int)a.getDimension(R.styleable.MenuStyleableAttrs_menu_message_size, -1);
        this.mMessageColor = a.getColor(R.styleable.MenuStyleableAttrs_menu_message_color, -1);
        this.mTitleColor = a.getColor(R.styleable.MenuStyleableAttrs_menu_title_color, -1);
        this.mDividerColor =a.getColor(R.styleable.MenuStyleableAttrs_menu_divider_color, -1);
        this.mDividerHeight = (int)a.getDimension(R.styleable.MenuStyleableAttrs_menu_divider_height, -1);

        this.mPaddingLeft = (int)a.getDimension(R.styleable.MenuStyleableAttrs_menu_paddingLeft, -1);
        this.mPaddingRight = (int)a.getDimension(R.styleable.MenuStyleableAttrs_menu_paddingRight, -1);
        this.mPaddingTop = (int)a.getDimension(R.styleable.MenuStyleableAttrs_menu_paddingTop, -1);
        this.mPaddingBottom = (int)a.getDimension(R.styleable.MenuStyleableAttrs_menu_paddingBottom, -1);
        this.mItemWidth = (int)a.getDimension(R.styleable.MenuStyleableAttrs_menu_item_width, -1);

        this.mItemHeight = (int)a.getDimension(R.styleable.MenuStyleableAttrs_menu_item_height, -1);

        if (parent != null) {

            if (mItemWidth == -1) {
                this.mItemWidth = parent.mItemWidth;
            }

            if (mItemHeight == -1) {
                this.mItemHeight = parent.mItemHeight;
            }//15

            if (mMessageColor == -1) {
                this.mMessageColor = parent.mMessageColor;
            }//

            if (mMessageSize == -1) {
                this.mMessageSize = parent.mMessageSize;
            }//

            if (mTitleColor == -1) {
                this.mTitleColor = parent.mTitleColor;
            }//

            if (mTitleSize == -1) {
                this.mTitleSize = parent.mTitleSize;
            }//

            if (mDividerColor == -1) {
                this.mDividerColor = parent.mDividerColor;
            }//10

            if (mDividerHeight == -1) {
                this.mDividerHeight = parent.mDividerHeight;
            }//

            if (mPaddingTop == -1) {
                this.mPaddingTop = parent.mPaddingTop;
            }//

            if (mPaddingBottom == -1) {
                this.mPaddingBottom = parent.mPaddingBottom;
            }//

            if (mPaddingLeft == -1) {
                this.mPaddingLeft = parent.mPaddingLeft;
            }//

            if (mPaddingRight == -1) {
                this.mPaddingRight = parent.mPaddingRight;
            }//5

            if(mBackgroundId == -1) {
                this.mBackgroundId = parent.mBackgroundId;
            }//
            if (mBackgroundColor == -1) {
                this.mBackgroundColor = parent.mBackgroundColor;
            }//

            if (mIconWidth == -1) {
                this.mIconWidth = parent.mIconWidth;
            }//

            if (mIconHeight == -1) {
                this.mIconHeight = parent.mIconHeight;
            }//
        }
        //====
        if (this.mIconWidth == -1) {
            this.mIconWidth = inflater.mDefaultIconWidth;
        }//
        if (this.mIconHeight == -1) {
            this.mIconHeight = inflater.mDefaultIconHeight;
        }//
        if (this.mItemWidth == -1) {
            this.mItemWidth = inflater.mDefaultItemWidth;
        }//
        if (this.mItemHeight == -1) {
            this.mItemHeight = inflater.mDefaultItemHeight;
        }//
        if (mPaddingTop == -1) {
            mPaddingTop = inflater.mDefaultPaddingTop;
        }//5
        if (mPaddingLeft == -1) {
            mPaddingLeft = inflater.mDefaultPaddingLeft;
        }//
        if (mPaddingRight == -1) {
            mPaddingRight = inflater.mDefaultPaddingRight;
        }//
        if (mPaddingBottom == -1) {
            mPaddingBottom = inflater.mDefaultPaddingBottom;
        }//
        if (mBackgroundColor == -1) {
            mBackgroundColor = inflater.mDefaultBackgroundColor;
        }//
        if (mBackgroundId == -1) {
            mBackgroundId = inflater.mDefaultBackgroundId;
        }//10

        if (mMessageSize == -1) {
            mMessageSize = inflater.mDefaultMessageSize;
        }//

        if (mMessageColor == -1) {
            mMessageColor = inflater.mDefaultMessageColor;
        }//

        if (mTitleColor == -1) {
            mTitleColor = inflater.mDefaultTitleColor;
        }

        if (mTitleSize == -1) {
            mTitleSize = inflater.mDefaultTitleSize;
        }

        if (mDividerColor == -1) {
            mDividerColor = inflater.mDefaultDividerColor;
        }

        if (mDividerHeight == -1) {
            this.mDividerHeight = inflater.mDefaultDividerHeight;
        }

        LOG.log("messagesize = "+this.mMessageSize);
        a.recycle();
    }



    public DavidMenuItem findMenuItemById(int id) {
        return findMenuItemById(this,id);
    }

    public DavidMenuItem findMenuItemById(DavidMenu menu,int id) {
        int itemCount = mItems.size();
        DavidMenuItem item = null;
        for (int index = 0; index < itemCount; index++) {
            item = mItems.get(index);
            if (item.id == id) {
                return item;
            } else {
                if (item.subMenu != null) {
                    item = findMenuItemById(item.subMenu,id);
                    if (item  != null) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

    public int getItemWidth() {
        return this.mItemWidth;
    }

    public int getItemHeight() {
        return this.mItemHeight;
    }

    public int getDividerHeight() {
        return this.mDividerHeight;
    }

    public int getDividerColor() {
        return this.mDividerColor;
    }

    public DavidMenu setItemSize(int w,int h) {
        this.mItemWidth = w;
        this.mItemHeight = h;
        return this;
    }

    public int getPaddingLeft() {
        return this.mPaddingLeft;
    }

    public int getPaddingRight() {
        return this.mPaddingRight;
    }

    public int getPaddingTop() {
        return this.mPaddingTop;
    }

    public int getPaddingBottom() {
        return this.mPaddingBottom;
    }

    public int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    public int getTitleColor() {
        return this.mTitleColor;
    }

    public int getMessageColor() {
        return this.mMessageColor;
    }

    public int getMessageSize() {
        return this.mMessageSize;
    }

    public int getBackgroundId() {
        return this.mBackgroundId;
    }

    public int getTitleSize() {
        return this.mTitleSize;
    }

    public int getIconWidth() {
        return this.mIconWidth;
    }

    public int getIconHeight() {
        return this.mIconHeight;
    }


    public DavidMenu setPadding(int l,int r,int t,int b) {
        this.mPaddingLeft = l;
        this.mPaddingRight = r;
        this.mPaddingTop = t;
        this.mPaddingBottom =b;
        return this;
    }

    public DavidMenu setDivider(int color,int height) {
        this.mDividerColor = color;
        this.mDividerHeight = height;
        return this;
    }

    public DavidMenu setIconSize(int iconWidth,int iconHeight) {
        this.mIconWidth = iconWidth;
        this.mIconHeight = iconHeight;
        return this;
    }

    public DavidMenu setMessageColor(int color) {
        this.mMessageColor = color;
        return this;
    }

    public DavidMenu setMessageSize(int size) {
        this.mMessageSize = size;
        return this;
    }

    public DavidMenu setTitleColor(int color) {
        this.mTitleColor = color;
        return this;
    }

    public DavidMenu setTitleSize(int size) {
        this.mTitleSize = size;
        return this;
    }

    public DavidMenu setBackgroundColor(int color) {
        this.mBackgroundColor = color;
        return this;
    }

    public DavidMenu setBackgroundId(int id) {
        this.mBackgroundId = id;
        return this;
    }


    public int size() {
        return this.mItems.size();
    }

    public DavidMenuItem getItem(int index) {
        return this.mItems.get(index);
    }

    public DavidMenuItem getParent() {
        return this.mParent;
    }

    /**
     *
     * <attr name="item_id" format="integer|reference"/>
     <attr name="item_icon" format="reference"/>
     <attr name="item_title" format="string|reference"/>
     <attr name="item_message" format="string|reference"/>
     <attr name="item_leftstub" format="reference"/>
     <attr name="item_rightstub" format="reference"/>
     * */
    public DavidMenuItem addItem(int itemId,CharSequence itemTitle,int itemIconResId,CharSequence itemMessage,
                                 int itemLeftStubResId,int itemRightStubResId) {
        DavidMenuItem item = new DavidMenuItem();
        item.title = itemTitle;
        item.id = itemId;
        item.iconId = itemIconResId;
        item.message = itemMessage;
        item.leftStubId = itemLeftStubResId;
        item.rightStubId = itemRightStubResId;
        mItems.add(item);
        return item;
    }

    public DavidMenu addSubMenus(int itemId,CharSequence itemTitle,int itemIconResId,CharSequence itemMessage,
                                 int itemLeftStubResId,int itemRightStubResId) {
        DavidMenuItem item = this.addItem(itemId,itemTitle,itemIconResId,itemMessage,itemLeftStubResId,itemRightStubResId);
        DavidMenu menu = new DavidMenu();
        menu.mParent = item;
        item.subMenu = menu;
        return menu;
    }

    public static class DavidMenuItem {

        public int id;
        public CharSequence title;
        public CharSequence message;
        public int iconId = -1;
        public DavidMenu subMenu;
        public int leftStubId = -1;
        public int rightStubId = -1;
        private View view;


        public View getView() {
            return this.view;
        }

        public View findViewById(int id) {
            return view.findViewById(id);
        }

        public void setView(View view) {
            this.view = view;
        }

        public int getSubMenuSize() {
            if (subMenu == null) {
                return 0;
            }
            return subMenu.size();
        }

        public DavidMenuItem getSubItem(int index) {
            return subMenu.getItem(index);
        }

        public DavidMenuItem setIcon(int icon) {
            this.iconId = icon;
            return this;
        }

        public DavidMenuItem setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

    }

}
