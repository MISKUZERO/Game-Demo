package controller;

import component.RectActor;

public interface ArgsController {

    void updateControllable(RectActor actor,  boolean... signals);

    void updateUncontrollable(RectActor actor);

}
