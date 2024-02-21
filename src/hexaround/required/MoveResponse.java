/*
 * Copyright (c) 2023. Gary F. Pollice
 *
 * This files was developed for personal or educational purposes. All rights reserved.
 *
 *  You may use this software for any purpose except as follows:
 *  1) You may not submit this file without modification for any educational assignment
 *      unless it was provided to you as part of starting code that does not require modification.
 *  2) You may not remove this copyright, even if you have modified this file.
 */

package hexaround.required;

import hexaround.required.*;

/**
 * This is what is returned from making a move in a game.
 * @param moveResult
 * @param message
 */
public record MoveResponse(
    MoveResult moveResult,
    String message  // The message must be filled in if there is any error
){
    /**
     * Shortcut that calls the default constructor with a null message.
     * @param moveResult
     */
    public MoveResponse(MoveResult moveResult) {
        this(moveResult, null);
    }
}
