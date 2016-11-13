package david.support.ext.menus.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import david.support.ext.R;
import david.support.ext.debug.Logger;
import david.support.ext.menus.DavidMenu;

/**
 * Created by chendingwei on 16/11/12.
 */

public class MenuItemLayout extends RelativeLayout {

    private DavidMenu.DavidMenuItem item = null;
    private DavidMenu menu;
    private TextView titleView;
    private ImageView iconView;
    private TextView messageView;
    private ViewStub headStub;
    private View headStubView;
    private ViewStub middleStub;
    private ViewStub tailStub;
    private View middleStubView;
    private View tailStubView;
    private static final Logger  LOG = new Logger(MenuItemLayout.class);


    public MenuItemLayout(Context context) {
        super(context);
        inflate(this.getContext(), R.layout.inner_menu_item_layout,this);
        titleView = (TextView) this.findViewById(R.id.title);
        iconView = (ImageView) this.findViewById(R.id.icon);
        messageView = (TextView) this.findViewById(R.id.message);
        headStub = (ViewStub) this.findViewById(R.id.head_stub);
        middleStub = (ViewStub) this.findViewById(R.id.middle_stub);
        tailStub = (ViewStub) this.findViewById(R.id.tail_stub);
    }

    public void setItem(DavidMenu.DavidMenuItem item,DavidMenu menu) {
        this.menu = menu;
        this.item = item;
        this.item.setView(this);
        this.prepareTitle();
        this.prepareIcon();
        this.prepareMiddleStub();
        this.prepareHeadStub();
        this.prepareTailStub();
        this.prepareMessage();
    }

    private void prepareMessage() {
        if (!TextUtils.isEmpty(this.item.message)) {
            this.messageView.setTextSize(menu.getMessageSize());
            this.messageView.setTextColor(menu.getMessageColor());
            this.messageView.setText(this.item.message);
            this.messageView.setVisibility(View.VISIBLE);
        } else {
            this.messageView.setVisibility(View.GONE);
        }
    }

    private void prepareTailStub() {
        if (item.tailStubId != -1) {
            LayoutParams params = (LayoutParams) this.tailStub.getLayoutParams();
            params.width = this.menu.getIconWidth();
            params.height = this.menu.getIconHeight();
            tailStub.setLayoutResource(item.tailStubId);
            tailStubView = tailStub.inflate();
            tailStubView.setVisibility(View.VISIBLE);
        } else {
            if (tailStubView != null) {
                tailStubView.setVisibility(View.GONE);
            } else {
                middleStub.setVisibility(View.GONE);
            }
        }
    }

    private void prepareHeadStub() {
        if (item.headStubId != -1) {
            LayoutParams params = (LayoutParams) this.headStub.getLayoutParams();
            params.width = this.menu.getIconWidth();
            params.height = this.menu.getIconHeight();
            headStub.setLayoutResource(item.headStubId);
            headStubView = headStub.inflate();
            headStubView.setVisibility(View.VISIBLE);
            iconView.setVisibility(View.GONE);
        } else {
            if (headStubView != null) {
                headStubView.setVisibility(View.GONE);
            } else {
                headStub.setVisibility(View.GONE);
            }
        }
    }

    private void prepareMiddleStub() {
        if (item.middleStubId != -1) {
            LayoutParams params = (LayoutParams) this.middleStub.getLayoutParams();
            params.width = this.menu.getIconWidth();
            params.height = this.menu.getIconHeight();
            middleStub.setLayoutResource(item.middleStubId);
            middleStubView = middleStub.inflate();
            middleStubView.setVisibility(View.VISIBLE);
        } else {
            if (middleStubView != null) {
                middleStubView.setVisibility(View.GONE);
            } else {
                middleStub.setVisibility(View.GONE);
            }
        }
    }

    private void prepareIcon() {
        if (this.item.iconId != -1) {
            this.iconView.setImageResource(this.item.iconId);
            this.iconView.setVisibility(View.VISIBLE);
            LayoutParams params = (LayoutParams) this.iconView.getLayoutParams();
            params.width = this.menu.getIconWidth();
            params.height = this.menu.getIconHeight();
            this.iconView.setLayoutParams(params);
        } else {
            this.iconView.setVisibility(View.GONE);
        }
    }

    private void prepareTitle() {
        if (!TextUtils.isEmpty(this.item.title)) {
            this.titleView.setTextSize(menu.getTitleSize());
            this.titleView.setTextColor(menu.getTitleColor());
            this.titleView.setText(this.item.title);
            this.titleView.setVisibility(View.VISIBLE);
        } else {
            this.titleView.setVisibility(View.GONE);
        }
     }
}
