package david.support.ext.menus.views;

import android.content.Context;
import android.graphics.Color;
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
    private ViewStub leftStub;
    private View leftStubView;
    private ViewStub rightStub;
    private View rightStubView;
    private static final Logger  LOG = new Logger(MenuItemLayout.class);


    public MenuItemLayout(Context context) {
        super(context);
        inflate(this.getContext(), R.layout.inner_menu_item_layout,this);
        titleView = (TextView) this.findViewById(R.id.title);
        iconView = (ImageView) this.findViewById(R.id.icon);
        messageView = (TextView) this.findViewById(R.id.message);
        leftStub = (ViewStub) this.findViewById(R.id.left_stub);
        rightStub = (ViewStub) this.findViewById(R.id.right_stub);
    }

    public void setItem(DavidMenu.DavidMenuItem item,DavidMenu menu) {
        this.menu = menu;
        this.item = item;
        this.item.setView(this);
        this.prepareTitle();
        this.prepareIcon();
        this.prepareLeftStub();
        this.prepareRightStub();
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

    private void prepareLeftStub() {
        if (item.leftStubId != -1) {
            LayoutParams params = (LayoutParams) this.leftStub.getLayoutParams();
            params.width = this.menu.getIconWidth();
            params.height = this.menu.getIconHeight();
            leftStub.setLayoutResource(item.leftStubId);
            leftStubView = leftStub.inflate();
            leftStubView.setVisibility(View.VISIBLE);
            iconView.setVisibility(View.GONE);
        } else {
            if (leftStubView != null) {
                leftStubView.setVisibility(View.GONE);
            } else {
                leftStub.setVisibility(View.GONE);
            }
        }
    }

    private void prepareRightStub() {
        if (item.rightStubId != -1) {
            LayoutParams params = (LayoutParams) this.rightStub.getLayoutParams();
            params.width = this.menu.getIconWidth();
            params.height = this.menu.getIconHeight();
            rightStub.setLayoutResource(item.rightStubId);
            rightStubView = rightStub.inflate();
            rightStubView.setVisibility(View.VISIBLE);
        } else {
            if (rightStubView != null) {
                rightStubView.setVisibility(View.GONE);
            } else {
                rightStub.setVisibility(View.GONE);
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
