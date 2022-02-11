package org.home;

import org.home.internal.HideEntity;

/**
 * Public Entity to print text on a console
 */
public class MainEntity {

    private static final String MSG = "HELLO WORLD";

    /**
     * Delegates print request to decorated entity. Uses {@link HideEntity}
     * @param entity Message to print
     */
    public void callPrint(HideEntity entity) {
        entity.print(MSG);
    }

}
