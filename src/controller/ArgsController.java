package controller;

import component.RectActor;

public interface ArgsController {

    void updateRole(RectActor actor, boolean lMove, boolean rMove, boolean jump);

    void updateLifeless(RectActor actor);

}
