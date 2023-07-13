package com.mio.keyconverter;

import static com.mio.keyconverter.KeyMap.*;

import static net.kdt.pojavlaunch.LwjglGlfwKeycode.*;

import net.kdt.pojavlaunch.customcontrols.ControlData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author mio
 */
public class MioKeyConverter {

    private static MioKeyConverter INSTANCE=new MioKeyConverter();

    private final HashMap<String, Short> keyMap;
    private final HashMap<Short, String> nameMap;
    private MioKeyConverter() {
        keyMap = new HashMap<>();
        nameMap = new HashMap<>();
        init();
    }
    public static MioKeyConverter getInstance() {
        return INSTANCE;
    }

    private void init() {
        keyMap.put(KEYMAP_KEY_0, GLFW_KEY_0);
        keyMap.put(KEYMAP_KEY_1, GLFW_KEY_1);
        keyMap.put(KEYMAP_KEY_2, GLFW_KEY_2);
        keyMap.put(KEYMAP_KEY_3, GLFW_KEY_3);
        keyMap.put(KEYMAP_KEY_4, GLFW_KEY_4);
        keyMap.put(KEYMAP_KEY_5, GLFW_KEY_5);
        keyMap.put(KEYMAP_KEY_6, GLFW_KEY_6);
        keyMap.put(KEYMAP_KEY_7, GLFW_KEY_7);
        keyMap.put(KEYMAP_KEY_8, GLFW_KEY_8);
        keyMap.put(KEYMAP_KEY_9, GLFW_KEY_9);
        keyMap.put(KEYMAP_KEY_A, GLFW_KEY_A);
        keyMap.put(KEYMAP_KEY_B, GLFW_KEY_B);
        keyMap.put(KEYMAP_KEY_C, GLFW_KEY_C);
        keyMap.put(KEYMAP_KEY_D, GLFW_KEY_D);
        keyMap.put(KEYMAP_KEY_E, GLFW_KEY_E);
        keyMap.put(KEYMAP_KEY_F, GLFW_KEY_F);
        keyMap.put(KEYMAP_KEY_G, GLFW_KEY_G);
        keyMap.put(KEYMAP_KEY_H, GLFW_KEY_H);
        keyMap.put(KEYMAP_KEY_I, GLFW_KEY_I);
        keyMap.put(KEYMAP_KEY_J, GLFW_KEY_J);
        keyMap.put(KEYMAP_KEY_K, GLFW_KEY_K);
        keyMap.put(KEYMAP_KEY_L, GLFW_KEY_L);
        keyMap.put(KEYMAP_KEY_M, GLFW_KEY_M);
        keyMap.put(KEYMAP_KEY_N, GLFW_KEY_N);
        keyMap.put(KEYMAP_KEY_O, GLFW_KEY_O);
        keyMap.put(KEYMAP_KEY_P, GLFW_KEY_P);
        keyMap.put(KEYMAP_KEY_Q, GLFW_KEY_Q);
        keyMap.put(KEYMAP_KEY_R, GLFW_KEY_R);
        keyMap.put(KEYMAP_KEY_S, GLFW_KEY_S);
        keyMap.put(KEYMAP_KEY_T, GLFW_KEY_T);
        keyMap.put(KEYMAP_KEY_U, GLFW_KEY_U);
        keyMap.put(KEYMAP_KEY_V, GLFW_KEY_V);
        keyMap.put(KEYMAP_KEY_W, GLFW_KEY_W);
        keyMap.put(KEYMAP_KEY_X, GLFW_KEY_X);
        keyMap.put(KEYMAP_KEY_Y, GLFW_KEY_Y);
        keyMap.put(KEYMAP_KEY_Z, GLFW_KEY_Z);
        keyMap.put(KEYMAP_KEY_MINUS, GLFW_KEY_MINUS);
        keyMap.put(KEYMAP_KEY_EQUALS, GLFW_KEY_EQUAL);
        keyMap.put(KEYMAP_KEY_LBRACKET, GLFW_KEY_LEFT_BRACKET);
        keyMap.put(KEYMAP_KEY_RBRACKET, GLFW_KEY_RIGHT_BRACKET);
        keyMap.put(KEYMAP_KEY_SEMICOLON, GLFW_KEY_SEMICOLON);
        keyMap.put(KEYMAP_KEY_APOSTROPHE, GLFW_KEY_APOSTROPHE);
        keyMap.put(KEYMAP_KEY_GRAVE, GLFW_KEY_GRAVE_ACCENT);
        keyMap.put(KEYMAP_KEY_BACKSLASH, GLFW_KEY_BACKSLASH);
        keyMap.put(KEYMAP_KEY_COMMA, GLFW_KEY_COMMA);
        keyMap.put(KEYMAP_KEY_PERIOD, GLFW_KEY_PERIOD);
        keyMap.put(KEYMAP_KEY_SLASH, GLFW_KEY_SLASH);
        keyMap.put(KEYMAP_KEY_ESC, GLFW_KEY_ESCAPE);
        keyMap.put(KEYMAP_KEY_F1, GLFW_KEY_F1);
        keyMap.put(KEYMAP_KEY_F2, GLFW_KEY_F2);
        keyMap.put(KEYMAP_KEY_F3, GLFW_KEY_F3);
        keyMap.put(KEYMAP_KEY_F4, GLFW_KEY_F4);
        keyMap.put(KEYMAP_KEY_F5, GLFW_KEY_F5);
        keyMap.put(KEYMAP_KEY_F6, GLFW_KEY_F6);
        keyMap.put(KEYMAP_KEY_F7, GLFW_KEY_F7);
        keyMap.put(KEYMAP_KEY_F8, GLFW_KEY_F8);
        keyMap.put(KEYMAP_KEY_F9, GLFW_KEY_F9);
        keyMap.put(KEYMAP_KEY_F10, GLFW_KEY_F10);
        keyMap.put(KEYMAP_KEY_F11, GLFW_KEY_F11);
        keyMap.put(KEYMAP_KEY_F12, GLFW_KEY_F12);
        keyMap.put(KEYMAP_KEY_TAB, GLFW_KEY_TAB);
        keyMap.put(KEYMAP_KEY_BACKSPACE, GLFW_KEY_BACKSPACE);
        keyMap.put(KEYMAP_KEY_SPACE, GLFW_KEY_SPACE);
        keyMap.put(KEYMAP_KEY_CAPITAL, GLFW_KEY_CAPS_LOCK);
        keyMap.put(KEYMAP_KEY_ENTER, GLFW_KEY_ENTER);
        keyMap.put(KEYMAP_KEY_LSHIFT, GLFW_KEY_LEFT_SHIFT);
        keyMap.put(KEYMAP_KEY_LCTRL, GLFW_KEY_LEFT_CONTROL);
        keyMap.put(KEYMAP_KEY_LALT, GLFW_KEY_LEFT_ALT);
        keyMap.put(KEYMAP_KEY_RSHIFT, GLFW_KEY_RIGHT_SHIFT);
        keyMap.put(KEYMAP_KEY_RCTRL, GLFW_KEY_RIGHT_CONTROL);
        keyMap.put(KEYMAP_KEY_RALT, GLFW_KEY_RIGHT_ALT);
        keyMap.put(KEYMAP_KEY_UP, GLFW_KEY_UP);
        keyMap.put(KEYMAP_KEY_DOWN, GLFW_KEY_DOWN);
        keyMap.put(KEYMAP_KEY_LEFT, GLFW_KEY_LEFT);
        keyMap.put(KEYMAP_KEY_RIGHT, GLFW_KEY_RIGHT);
        keyMap.put(KEYMAP_KEY_PAGEUP, GLFW_KEY_PAGE_UP);
        keyMap.put(KEYMAP_KEY_PAGEDOWN, GLFW_KEY_PAGE_DOWN);
        keyMap.put(KEYMAP_KEY_HOME, GLFW_KEY_HOME);
        keyMap.put(KEYMAP_KEY_END, GLFW_KEY_END);
        keyMap.put(KEYMAP_KEY_INSERT, GLFW_KEY_INSERT);
        keyMap.put(KEYMAP_KEY_DELETE, GLFW_KEY_DELETE);
        keyMap.put(KEYMAP_KEY_PAUSE, GLFW_KEY_PAUSE);
        keyMap.put(KEYMAP_KEY_NUMPAD0, GLFW_KEY_KP_0);
        keyMap.put(KEYMAP_KEY_NUMPAD1, GLFW_KEY_KP_1);
        keyMap.put(KEYMAP_KEY_NUMPAD2, GLFW_KEY_KP_2);
        keyMap.put(KEYMAP_KEY_NUMPAD3, GLFW_KEY_KP_3);
        keyMap.put(KEYMAP_KEY_NUMPAD4, GLFW_KEY_KP_4);
        keyMap.put(KEYMAP_KEY_NUMPAD5, GLFW_KEY_KP_5);
        keyMap.put(KEYMAP_KEY_NUMPAD6, GLFW_KEY_KP_6);
        keyMap.put(KEYMAP_KEY_NUMPAD7, GLFW_KEY_KP_7);
        keyMap.put(KEYMAP_KEY_NUMPAD8, GLFW_KEY_KP_8);
        keyMap.put(KEYMAP_KEY_NUMPAD9, GLFW_KEY_KP_9);
        keyMap.put(KEYMAP_KEY_NUMLOCK, GLFW_KEY_NUM_LOCK);
        keyMap.put(KEYMAP_KEY_SCROLL, GLFW_KEY_SCROLL_LOCK);
        keyMap.put(KEYMAP_KEY_SUBTRACT, GLFW_KEY_KP_SUBTRACT);
        keyMap.put(KEYMAP_KEY_ADD, GLFW_KEY_KP_ADD);
        keyMap.put(KEYMAP_KEY_DECIMAL, GLFW_KEY_KP_DECIMAL);
        keyMap.put(KEYMAP_KEY_NUMPADENTER, GLFW_KEY_KP_ENTER);
        keyMap.put(KEYMAP_KEY_DIVIDE, GLFW_KEY_KP_DIVIDE);
        keyMap.put(KEYMAP_KEY_MULTIPLY, GLFW_KEY_KP_MULTIPLY);
        keyMap.put(KEYMAP_KEY_PRINT, GLFW_KEY_PRINT_SCREEN);

        keyMap.put("鼠标左", (short) ControlData.SPECIALBTN_MOUSEPRI);
        keyMap.put("鼠标中", (short) ControlData.SPECIALBTN_MOUSEMID);
        keyMap.put("鼠标右", (short) ControlData.SPECIALBTN_MOUSESEC);
        keyMap.put("滚轮上", (short) ControlData.SPECIALBTN_SCROLLUP);
        keyMap.put("滚轮下", (short) ControlData.SPECIALBTN_SCROLLDOWN);
        keyMap.put("鼠标", (short) ControlData.SPECIALBTN_VIRTUALMOUSE);
        keyMap.put("GUI", (short) ControlData.SPECIALBTN_TOGGLECTRL);
        keyMap.put("菜单", (short) ControlData.SPECIALBTN_MENU);
        keyMap.put("键盘", (short) ControlData.SPECIALBTN_KEYBOARD);
        keyMap.put("UNKNOWN",GLFW_KEY_UNKNOWN);

        Set<String> keys=keyMap.keySet();
        for (String key : keys) {
            nameMap.put(keyMap.get(key),key);
        }
    }

    public short nameToKey(String keyName) {
        if (keyMap.containsKey(keyName)) {
            return keyMap.get(keyName);
        } else {
            return 0;
        }
    }
    public String keyToName(short keyCode) {
        if (nameMap.containsKey(keyCode)) {
            return nameMap.get(keyCode);
        } else {
            return null;
        }
    }
}
