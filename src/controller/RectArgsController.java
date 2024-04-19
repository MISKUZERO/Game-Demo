package controller;

import component.RectSprite;

public interface RectArgsController extends ArgsController {

    void updateControllable(RectSprite sprite, boolean... signals);

    void updateUncontrollable(RectSprite sprite);

}
