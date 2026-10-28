package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.Set;

/**
 * MiddleClickFriend — Middle mouse click toggle for friend and team trust registries.
 */
public class MiddleClickFriend extends Module {
    private final BooleanSetting alertChat = new BooleanSetting("AlertChat", "Send confirmation message in client chat", true);
    private final BooleanSetting highlightFriends = new BooleanSetting("HighlightFriends", "Render distinct outlines for friendly entities", true);
    private final BooleanSetting teamSync = new BooleanSetting("TeamSync", "Synchronize scoreboard team players as friends", true);

    public MiddleClickFriend() {
        super("middle-click-friend", "Middle Click Friend", "Middle mouse click toggle for friend and team trust registries", Category.PLAYER);
        addSettings(alertChat, highlightFriends, teamSync);
    }

    public BooleanSetting getAlertChat() {
        return alertChat;
    }

    public BooleanSetting getHighlightFriends() {
        return highlightFriends;
    }

    public BooleanSetting getTeamSync() {
        return teamSync;
    }

    public static boolean toggleFriend(Set<String> friendList, String playerName) {
        if (friendList == null || playerName == null) return false;
        String nameLower = playerName.toLowerCase();
        if (friendList.contains(nameLower)) {
            friendList.remove(nameLower);
            return false;
        } else {
            friendList.add(nameLower);
            return true;
        }
    }
}
